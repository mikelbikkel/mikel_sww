/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.subwayworld.routeservice.calculation;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.subwayworld.routeservice.DebugSettings;
import org.subwayworld.routeservice.Direction;
import org.subwayworld.routeservice.DirectionOverride;
import org.subwayworld.routeservice.ILocation;
import org.subwayworld.routeservice.Landmark;
import org.subwayworld.routeservice.MetroTransform;
import org.subwayworld.routeservice.RSCalculationException;
import org.subwayworld.routeservice.RouteResult;
import org.subwayworld.routeservice.RouteResult.RCODE;
import org.subwayworld.routeservice.RouteSegment;
import org.subwayworld.routeservice.SegmentDirection;
import org.subwayworld.routeservice.ServiceType;
import org.subwayworld.routeservice.Station;
import org.subwayworld.routeservice.TransferType;
import org.subwayworld.routeservice.pathfinder.Halte2;
import org.subwayworld.routeservice.pathfinder.Link2;
import org.subwayworld.routeservice.pathfinder.SPDijkstra2;
import org.subwayworld.routeservice.pathfinder.TransType;

/**
 * Information about metro network in a city.
 * <p>
 * Once created, this information is immutable and does not change. This makes
 * it possible to re-use the collected information for multiple route
 * calculations.
 * <p>
 * It acts as a cache for the collected database information.
 * <p>
 * Design decision:<br>
 * do not add functionality to initialize from m_storage to MetroInfo. This
 * keeps the data structures in MetroInfo separate from the back end that stores
 * the metro data. MetroInfo is the in memory representation of the network.
 * <p>
 * The MetroInfo does not know:
 * <ul>
 * <li>where the network is stored on disk.</li>
 * <li>what structures are used on disk (files, database tables).</li>
 * <li>how to transform on disk structures to in memory structures.</li>
 * </ul>
 */
public class MetroInfo {

  // private Log m_log;

  private enum stopAction {
    INIT, MERGE
  };

  /**
   * City code.
   * <p>
   * For example: ROTTERDAM.
   */
  String m_cityCode;

  /**
   * City id.
   * <p>
   * For example: RDM.
   */
  String m_cityId;

  /**
   * Locations that are segment endpoints.
   */
  Set<ILocation> m_nodes;

  Set<SegmentInfo> m_segs;

  Set<MetroTransform> m_trans;

  /**
   * Maps a station id (RDMBLAAK) to a station object.
   */
  Map<String, Station> m_statMap;

  List<Direction> m_dirs;

  Map<String, Landmark> m_marks;

  List<DirectionOverride> m_dirOverrides;

  /**
   * Maps a complex MNN id (NCWMNN_MONUMENT_X__X_1) to a list of stations.
   */
  Map<String, List<Station>> m_complexMap;

  public MetroInfo() {
    // m_log = LogFactory.getLog(getClass());
    m_nodes = new HashSet<ILocation>();
    m_segs = new HashSet<SegmentInfo>();
    m_marks = new HashMap<String, Landmark>();
    m_dirs = new ArrayList<Direction>();
    m_statMap = new HashMap<String, Station>();
    m_trans = new HashSet<MetroTransform>();
    m_complexMap = new HashMap<String, List<Station>>();
    m_dirOverrides = new ArrayList<DirectionOverride>();
  }

  /**
   * Create initial set with nodes from segment endpoints.
   */
  public void addSegmentNodeInfo() {
    ILocation loc;

    assert null != m_nodes;
    assert null != m_segs;

    for (SegmentInfo si : m_segs) {
      loc = si.getLocationStart();
      m_nodes.add(loc);

      loc = si.getLocationEnd();
      m_nodes.add(loc);
    }
  }

  /**
   * Find the shortest path between 2 locations.
   * 
   * @param locFrom
   *          start
   * @param locTo
   *          destination
   * @param stat
   *          es servicetypes that must be included in the search.
   * @return Shortest path, or null if statFrom and statTo are not connected.
   * @throws RSCalculationException
   *           if internal error occurs.
   */
  public List<RouteSegment> shortestPath(String locFromCode, String locToCode,
      EnumSet<ServiceType> es, RouteResult rr) throws RSCalculationException {

    assert null != locFromCode;
    assert null != locToCode;
    assert null != rr;

    ILocation locFrom = findLocation(locFromCode);
    if (null == locFrom) {
      rr.setCode(RCODE.FROM_UNKNOWN);
      return Collections.emptyList();
    }
    ILocation locTo = findLocation(locToCode);
    if (null == locTo) {
      rr.setCode(RCODE.TO_UNKNOWN);
      return Collections.emptyList();
    }

    if (m_complexMap.isEmpty()) {
      // Keep it simple if complexes do not exist in this city.
      List<RouteSegment> rt = findRoute(locFrom, locTo, es, rr);
      return rt;
    }

    // All Complex-handling code is located in this function.
    // Without complexes, this function would look like this:
    //
    // List<RouteSegment> route = findRoute(locFrom, locTo, es, rr);
    // return route;
    //
    if (isSameComplex(locFrom, locTo)) {
      rr.setCode(RCODE.FROM_IS_TO);
      return Collections.emptyList();
    }

    // In case a complex is start or end of route, we must calculate multiple
    // routes, determine the shortest shortest route and remove MNN walks
    // between complex stations.
    List<ILocation> startLocations = findNodesForLocation(locFrom);
    List<ILocation> endLocations = findNodesForLocation(locTo);
    List<RouteSegment> shortestRoute = null;
    int shortestDistance = Integer.MAX_VALUE;
    List<RouteSegment> route = Collections.emptyList();
    int len;
    int distance;
    for (ILocation f : startLocations) {
      for (ILocation t : endLocations) {
        route = findRoute(f, t, es, rr);
        len = route.size();
        if (len > 0 && RCODE.OK == rr.getCode()) {
          distance = route.get(len - 1).getDistance();
          if (distance < shortestDistance) {
            shortestDistance = distance;
            shortestRoute = route;
          }
        }
      }
    }
    if (null == shortestRoute) {
      rr.setCode(RCODE.NO_ROUTE);
      return Collections.emptyList();
    }

    removeComplexMNNWalks(shortestRoute);

    // return shortestRoute that is found.
    return shortestRoute;
  }

  /**
   * Creates a segment direction.
   * <p>
   * Handles the special case of the WALK direction and applies direction
   * overrides.
   * 
   * @param d
   *          the direction.
   * @param fromLocationCode
   *          the code of the starting location. For example RDMBLAAK.
   * @param locW
   *          the location that is the endpoint for this segment.
   * @return the segment direction.
   */
  private SegmentDirection createSegmentDirection(Direction d,
      String fromLocationCode, ILocation locW) {
    // Determine attributes, taking care of the special case of the
    // WALK-direction.
    ServiceType svtype = d.getServiceType();
    String line;
    String endpoint;
    String remark;
    String dir_id = null;
    if (WalkDirection.DIRECTION_INDEX == d.m_idx) {
      line = WalkDirection.LINE;
      endpoint = locW.getName();
      remark = WalkDirection.REMARK;
      dir_id = WalkDirection.DIR_ID + "_" + locW.getCode();
    } else {
      line = d.getLine();
      endpoint = d.getEndpoint();
      remark = d.getRemark();
      dir_id = d.getDirId();
    }

    // Create the segment direction.
    SegmentDirection sdir;
    sdir = new SegmentDirection(svtype, line, endpoint, remark, dir_id,
        fromLocationCode);

    // Apply direction override, if applicable.
    for (DirectionOverride dor : m_dirOverrides) {
      if (dor.m_station_id.equals(fromLocationCode)
          && dor.m_dir_id.equals(dir_id)) {
        sdir.setDirId(dor.m_override_dir_id);
        sdir.setLine(dor.m_line);
        sdir.setEndpoint(dor.m_endpoint);
        sdir.setServiceType(dor.m_servicetype);
        sdir.setRemark(dor.m_remark);
      }
    }

    return sdir;
  }

  private void postAddDirections(RouteSegment rs, BitSet dirinfo, ILocation locW) {
    SegmentDirection sdir;
    for (Direction d : m_dirs) {
      if (dirinfo.get(d.m_idx)) {
        if (WalkDirection.DIRECTION_INDEX == d.m_idx) {
          // This is a HACK.
          rs.setTransferType(WalkDirection.TRANSFER_TYPE);
        }
        sdir = createSegmentDirection(d, rs.getLocation().getCode(), locW);
        rs.addDirection(sdir);
      }
    }
  }

  private void postMergeDirections(RouteSegment rs, BitSet dirinfo,
      ILocation locW) {
    SegmentDirection sdir;
    List<SegmentDirection> dirs = new ArrayList<SegmentDirection>();
    for (Direction d : m_dirs) {
      if (dirinfo.get(d.m_idx)) {
        if (WalkDirection.DIRECTION_INDEX == d.m_idx) {
          // This is a HACK.
          rs.setTransferType(WalkDirection.TRANSFER_TYPE);
        }
        sdir = createSegmentDirection(d, rs.getLocation().getCode(), locW);
        dirs.add(sdir);
      }
    }
    rs.mergeDirections(dirs);
  }

  private ILocation findLocation(String locCode) {
    ILocation loc = m_statMap.get(locCode);
    if (null == loc) {
      loc = m_marks.get(locCode);
    }
    return loc;
  }

  /**
   * Transforms a shortest path into a metro route.
   * 
   * @param path
   *          the shortest path.
   * 
   * @return the metro route.
   */
  private List<RouteSegment> postCreateRoute(Link2[] path) {
    RouteSegment seg;
    List<RouteSegment> lst = new ArrayList<RouteSegment>();

    assert null != path;
    assert path.length >= 1;

    // Initialize RouteSegment list with first link.
    seg = new RouteSegment(path[0].v.loc, path[0].v.spd);
    seg.setTransferType(TransferType.TRANSFER);
    postAddDirections(seg, path[0].m_spDirinfo, path[0].w.loc);
    postAddStops(seg, path[0].locations, stopAction.INIT);
    lst.add(seg);

    int i, prev;
    for (i = 1, prev = 0; i < path.length; i++, prev++) {
      switch (path[i].sptype) {
      case PF_TRANSFER:
        // fall tru to PF_TRANSFORMATION
      case PF_TRANSFORMATION:
        assert path[i].isTransfer(path[prev]);
        // Make new RouteSegment and append to list.
        seg = new RouteSegment(path[i].v.loc, path[i].v.spd);
        if (TransType.PF_TRANSFER == path[i].sptype) {
          seg.setTransferType(TransferType.TRANSFER);
        } else {
          seg.setTransferType(TransferType.TRANSFORMATION);
        }
        postAddDirections(seg, path[i].m_spDirinfo, path[i].w.loc);
        postAddStops(seg, path[i].locations, stopAction.INIT);
        lst.add(seg);
        break;
      case PF_NULL:
        assert !path[i].isTransfer(path[prev]);
        // Merge link with RouteSegment.
        // For the merged segment, the directions must contain the directions
        // that exist on ALL input segments.
        // So: ABC, BC, BD => B.
        // Implement:
        // create segment with first set (ABC).
        // For each merged segment: perform an AND of segment-dirset and
        // incoming dirset.
        postMergeDirections(seg, path[i].m_spDirinfo, path[i].w.loc);
        postAddStops(seg, path[i].locations, stopAction.MERGE);
        break;
      default:
        // Entries generated by dijkstra must have type PF_NULL, PF_TRANSFER,
        // PF_TRANSFORMATION.
        assert false;
        throw new IllegalArgumentException();
      }
    }

    // Add last RouteSegment for destination.
    int last = path.length - 1;
    seg = new RouteSegment(path[last].w.loc, path[last].w.spd);
    seg.setTransferType(TransferType.FINISH);
    lst.add(seg);

    return lst;
  }

  private void postAddStops(RouteSegment seg, ILocation[] stats, stopAction sact) {
    int startIndex = -1;
    switch (sact) {
    case INIT:
      // if adding stops for the first segment, the first station must be
      // excluded.
      startIndex = 1;
      break;
    case MERGE:
      // if adding stops when segments are merged, the first station of the
      // merged segment must be added as a stop.
      startIndex = 0;
      break;
    }
    // The last entry is not added.
    for (int i = startIndex; i < stats.length - 1; i++) {
      seg.addStop(stats[i]);
    }
  }

  private List<RouteSegment> findRoute(ILocation locFrom, ILocation locTo,
      EnumSet<ServiceType> es, RouteResult rr) throws RSCalculationException {
    DijkstraInfo di = new DijkstraInfo();
    di.init(locFrom, locTo, m_nodes, m_trans, m_segs, es, m_dirs);

    if (DebugSettings.DEBUG_PRINT) {
      for (Direction d : m_dirs) {
        System.out.println(d);
      }
    }

    SPDijkstra2 sp = new SPDijkstra2();
    Halte2 hS = di.getStartHalte();
    Halte2 hE = di.getEndHalte();
    Halte2[] h = di.getHaltes();
    Link2[] path = sp.solve(h, hS, hE);
    if (null == path) {
      rr.setCode(RCODE.NO_ROUTE);
      return Collections.emptyList();
    }

    List<RouteSegment> route = postCreateRoute(path);
    return route;

  }

  private Station getComplexStation(ILocation loc) {
    if (loc instanceof Station && ((Station) loc).isComplex()) {
      return (Station) loc;
    }
    return null;
  }

  private boolean isSameComplex(ILocation locFrom, ILocation locTo) {
    Station statFrom = getComplexStation(locFrom);
    if (null == statFrom) {
      return false;
    }
    Station statTo = getComplexStation(locTo);
    if (null == statTo) {
      return false;
    }
    // HACK: if stations have same, they are the same complex.
    // MNN-name is not available. Therefore, use name....
    String fromName = statFrom.getName();
    String toName = statTo.getName();
    return fromName.equals(toName);
  }

  /**
   * Find network nodes that are related to a location. <br>
   * A REGULAR station and landmarks have 1 network node.<br>
   * NETWORK and COMPLEX stations have multiple network nodes.
   * 
   * @param loc
   *          the location
   * @return the network nodes for the location.
   */
  private List<ILocation> findNodesForLocation(ILocation loc) {
    List<ILocation> locations = new ArrayList<ILocation>();
    Station s = getComplexStation(loc);
    if (null == s) {
      locations.add(loc);
    } else {
      findNodesForComplex(s, locations);
    }
    // at least one location must be found.
    assert locations.size() >= 1;
    return locations;
  }

  /**
   * Returns all the locations that are part of the complex.
   * 
   * @param s
   *          the station that identifies the complex.
   * @param startLocations
   *          all the locations that are part of the complex.
   */
  private void findNodesForComplex(Station s, List<ILocation> startLocations) {
    assert null != s;
    assert startLocations.isEmpty();

    // Add complex locations
    for (List<Station> lst : m_complexMap.values()) {
      if (lst.contains(s)) {
        startLocations.addAll(lst);
      }
    }
  }

  private void removeComplexMNNWalks(List<RouteSegment> route) {
    ILocation from;
    ILocation to;
    int last = route.size();
    boolean[] remove = new boolean[last];
    RouteSegment segPrev;
    RouteSegment seg;

    assert route != null;
    assert route.size() > 1;

    for (int prev = 0, i = 1; i < last; prev++, i++) {
      remove[prev] = false;
      segPrev = route.get(prev);
      seg = route.get(i);
      if (TransferType.WALK == segPrev.getTransferType()) {
        from = segPrev.getLocation();
        to = seg.getLocation();
        if (isSameComplex(from, to)) {
          remove[prev] = true;
        }
      }
    }
    // Remove in reverse index order, to keep index values valid.
    for (int i = last - 1; i >= 0; i--) {
      if (remove[i]) {
        route.remove(i);
      }
    }
    // If size == 1, the route was a walk between 2 stations that belong to the
    // same complex.
    // this should be detected by shortestPath() before a route is calculated.
    assert route.size() > 1;
  }

}
