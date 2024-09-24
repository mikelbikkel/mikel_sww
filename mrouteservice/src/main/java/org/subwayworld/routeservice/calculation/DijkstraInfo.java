/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.subwayworld.routeservice.calculation;

import java.util.BitSet;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.subwayworld.routeservice.Direction;
import org.subwayworld.routeservice.ILocation;
import org.subwayworld.routeservice.Landmark;
import org.subwayworld.routeservice.MetroTransform;
import org.subwayworld.routeservice.RSCalculationException;
import org.subwayworld.routeservice.ServiceType;
import org.subwayworld.routeservice.Station;
import org.subwayworld.routeservice.calculation.SegmentInfo.SegType;
import org.subwayworld.routeservice.pathfinder.Halte2;
import org.subwayworld.routeservice.pathfinder.Link2;

public class DijkstraInfo {

  private Log m_log;

  private Map<ILocation, Halte2> m_mapHalte;

  private int m_halteIndex;

  private ILocation m_start;

  private ILocation m_end;

  public DijkstraInfo() {
    m_log = LogFactory.getLog(getClass());
  }

  public void init(ILocation locFrom, ILocation locTo, Set<ILocation> nodes,
      Set<MetroTransform> trans, Set<SegmentInfo> segs,
      EnumSet<ServiceType> es, List<Direction> dirs)
      throws RSCalculationException {
    m_start = locFrom;
    m_end = locTo;
    createHaltes(nodes, trans);

    BitSet dirmask2 = createDirMask2(es, dirs);
    Set<SegmentInfo> cpSegs = copySegments(segs, dirmask2);

    mergeLocation(m_start, cpSegs);
    mergeLocation(m_end, cpSegs);

    createLinks(cpSegs);
  }

  public Halte2[] getHaltes() {
    Halte2[] haltes = new Halte2[m_mapHalte.size()];
    for (Halte2 h : m_mapHalte.values()) {
      haltes[h.id] = h;
    }
    return haltes;
  }

  public Halte2 getStartHalte() throws RSCalculationException {
    return findHalte(m_start);
  }

  public Halte2 getEndHalte() throws RSCalculationException {
    return findHalte(m_end);
  }

  private Set<SegmentInfo> copySegments(Set<SegmentInfo> segs, BitSet dm2) {
    Set<SegmentInfo> cp = new HashSet<SegmentInfo>();
    SegmentInfo sif;
    for (SegmentInfo si : segs) {
      // Filter on servicetype
      sif = filterSegment(si, dm2);
      if (null != sif) {
        cp.add(sif);
      }
    }
    return cp;
  }

  private void mergeLocation(ILocation loc, Set<SegmentInfo> segs) {
    if (loc instanceof Station) {
      split(loc, segs);
      return;
    }
    if (loc instanceof Landmark) {
      // Create new segments between landmark and associated stations.
      Landmark m = (Landmark) loc;
      addHalte(loc);
      for (Station s : m.getStations()) {
        split(s, segs);
        SegmentInfo si = new SegmentInfo(m.getCode() + "_" + s.getCode(), 2);
        si.addLocation(m, 0);
        si.addLocation(s, 1);
        si.m_forward.set(WalkDirection.DIRECTION_INDEX);
        si.m_back.set(WalkDirection.DIRECTION_INDEX);
        si.m_type = SegType.BI_DIRECTIONAL;
        segs.add(si);
      }
    }
  }

  private void split(ILocation loc, Set<SegmentInfo> segs) {
    if (m_mapHalte.containsKey(loc)) {
      return;
    }
    addHalte(loc);

    SegmentInfo siLower = null;
    SegmentInfo siUpper = null;

    Set<SegmentInfo> newSegs = new HashSet<SegmentInfo>();

    int idx;
    SegmentInfo seg = null;
    Iterator<SegmentInfo> iter = segs.iterator();
    while (iter.hasNext()) {
      seg = iter.next();
      idx = seg.canSplit(loc);
      if (-1 != idx) {
        siLower = seg.getLower(loc);
        siUpper = seg.getUpper(loc);
        iter.remove();
        newSegs.add(siLower);
        newSegs.add(siUpper);
      }
    }
    segs.addAll(newSegs);
  }

  /**
   * Creates Haltes from nodes, adding Haltes for start and end locations if
   * required.
   * <p>
   * This step is the same for Stations and Landmarks.
   * 
   * @param nodes
   *          the initial set of nodes.
   * @param trans
   *          transformation information.
   * @throws RSCalculationException
   *           when a location cannot be transformed into a Halte
   */
  private void createHaltes(Set<ILocation> nodes, Set<MetroTransform> trans)
      throws RSCalculationException {
    m_mapHalte = new HashMap<ILocation, Halte2>();
    m_halteIndex = 0;

    /*
     * Step 1: Transform nodes into Haltes and add to m_mapHalte.
     */
    for (ILocation n : nodes) {
      addHalte(n);
    }

    /*
     * Step 2: add transform information to Haltes.
     */
    BitSet[] bs = new BitSet[0];
    Halte2 h;
    for (MetroTransform mt : trans) {
      h = findHalte(mt.m_station);
      h.m_transformations = mt.m_transforms.toArray(bs);
    }
  }

  private void addHalte(ILocation loc) {
    Halte2 h = new Halte2(m_halteIndex, loc);
    m_mapHalte.put(loc, h);
    m_halteIndex++;
  }

  /**
   * Creates links and attaches them to the haltes.
   * 
   * @param haltes
   * @throws RSCalculationException
   */
  private void createLinks(Set<SegmentInfo> segs) throws RSCalculationException {
    int id = 0;
    for (SegmentInfo si : segs) {
      id = transformSegment(si, id);
    }
  }

  private int transformSegment(SegmentInfo si, int id)
      throws RSCalculationException {
    Halte2 hV = null;
    Halte2 hW = null;
    Link2 lnk = null;
    ILocation stat;
    stat = si.getLocationStart();
    hV = findHalte(stat);
    stat = si.getLocationEnd();
    hW = findHalte(stat);
    ILocation[] stats = si.getLocations();
    lnk = new Link2(id, hV, hW, stats.length - 1);
    lnk.m_dirinfo = si.m_forward;
    lnk.locations = stats; // not needed by Dijkstra
    id++;
    hV.addEdge(lnk);
    if (SegmentInfo.SegType.BI_DIRECTIONAL == si.m_type) {
      // Backward link.
      lnk = new Link2(id, hW, hV, stats.length - 1);
      lnk.m_dirinfo = si.m_back;
      ILocation[] statsBack = inverse(stats);
      lnk.locations = statsBack; // not needed by Dijkstra
      id++;
      hW.addEdge(lnk);
    }
    return id;
  }

  private Halte2 findHalte(ILocation loc) throws RSCalculationException {
    Halte2 h = m_mapHalte.get(loc);
    assert null != h;
    if (null == h) {
      throw new RSCalculationException("not found: " + loc);
    }
    return h;
  }

  /**
   * Creates a mask to filter directions with unwanted service types from
   * dircodes.
   * 
   * @param es
   *          the set of service types that must be included.
   * @return a mask with bits set to zero for directions with an unwanted
   *         service type, and bits set to 1 for all other entries.
   */
  private BitSet createDirMask2(EnumSet<ServiceType> es, List<Direction> dirs) {
    int len = dirs.size();
    BitSet dirmask = new BitSet(len);
    // All bits initially set to 1 (disables filtering)
    dirmask.flip(0, len);
    ServiceType st;
    if (null == es || es.isEmpty()) {
      return dirmask;
    }
    for (Direction dir : dirs) {
      st = dir.getServiceType();
      if (!es.contains(st)) {
        dirmask.clear(dir.m_idx);
      }
    }
    return dirmask;
  }

  private SegmentInfo filterSegment(SegmentInfo si, BitSet dm2) {
    BitSet forward = (BitSet) si.m_forward.clone();
    forward.and(dm2);
    BitSet back = (BitSet) si.m_back.clone();
    back.and(dm2);

    // if (fwd == si.m_fwd && bck == si.m_bck) {
    if (forward.equals(si.m_forward) && back.equals(si.m_back)) {
      // Case 1: filter has no effect, segment unchanged.
      // Action: return incoming segment.
      return si;
    }
    // cardinality is number of bits set to 1.
    boolean fwdZero = (0 == forward.cardinality());
    boolean bckZero = (0 == back.cardinality());
    // if (0L == fwd && 0L == bck) {
    if (fwdZero && bckZero) {
      // Case 2: filter has removed all forward and all backward directions.
      // Action: discard segment.
      if (m_log.isTraceEnabled()) {
        String msg = "Remove: " + si.m_name + ".";
        m_log.trace(msg);
      }
      return null;
    }

    /*
     * If we get here, the mask has changed the direction info of the segment.
     * The incoming segment must not be changed. Instead, create a new segment
     * that contains the remaining directions.
     */
    SegmentInfo siNew;

    ILocation[] locs = si.getLocations();
    // if (0L != fwd && 0L == bck) {
    if (!fwdZero && bckZero) {
      // Case 3: filter has removed all backward directions, 1 or more forward
      // remain.
      // Action: create a new, unidirectional segment.
      siNew = new SegmentInfo(si.m_name, locs.length);
      siNew.m_type = SegmentInfo.SegType.UNI_DIRECTIONAL;
      siNew.m_back = back;
      siNew.m_forward = forward;
      for (int i = 0; i < locs.length; i++) {
        siNew.addLocation(locs[i], i);
      }
      if (m_log.isTraceEnabled()) {
        String msg = "Make uni-directional: " + si.m_name + ".";
        m_log.trace(msg);
      }
      return siNew;
    }

    // if (0L == fwd && 0L != bck) {
    if (fwdZero && !bckZero) {
      // Case 4: filter has removed all forward directions, 1 or more backward
      // remain.
      // Action: create a new, unidirectional and reversed segment.
      if (m_log.isTraceEnabled()) {
        String msg = "Make uni-directional and reverse: " + si.m_name + ".";
        m_log.trace(msg);
      }
      siNew = new SegmentInfo(si.m_name, locs.length);
      siNew.m_type = SegmentInfo.SegType.UNI_DIRECTIONAL;
      siNew.m_forward = back;
      siNew.m_back = new BitSet(); // 0L
      for (int i = 0, j = locs.length - 1; i < locs.length; i++, j--) {
        // Use j to reverse stations on new segment.
        siNew.addLocation(locs[i], j);
      }
      return siNew;
    }

    // Case 5: filter has removed some directions, 1 or more forward
    // remain AND 1 or more backward remain.
    // Action: create a new bi-directional segment.
    siNew = new SegmentInfo(si.m_name, locs.length);
    siNew.m_type = SegmentInfo.SegType.BI_DIRECTIONAL;
    siNew.m_back = back;
    siNew.m_forward = forward;
    for (int i = 0; i < locs.length; i++) {
      siNew.addLocation(locs[i], i);
    }
    if (m_log.isTraceEnabled()) {
      String msg = "Filtered bi-directional: " + si.m_name + ".";
      m_log.trace(msg);
    }
    return siNew;
  }

  private ILocation[] inverse(ILocation[] as) {
    ILocation[] ai = new ILocation[as.length];
    for (int i = 0, j = as.length - 1; i < as.length; i++, j--) {
      ai[i] = as[j];
    }
    return ai;
  }
}
