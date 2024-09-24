/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.subwayworld.routeservice.calculation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.subwayworld.routeservice.DebugSettings;
import org.subwayworld.routeservice.Direction;
import org.subwayworld.routeservice.DirectionOverride;
import org.subwayworld.routeservice.Landmark;
import org.subwayworld.routeservice.MetroTransform;
import org.subwayworld.routeservice.RSConnectionException;
import org.subwayworld.routeservice.RSDataException;
import org.subwayworld.routeservice.RSEnv;
import org.subwayworld.routeservice.RSException;
import org.subwayworld.routeservice.Station;
import org.subwayworld.routeservice.calculation.SegmentInfo.SegType;

/**
 * Access the Metro database.
 * 
 */
public class CalculationGateway {

  private RSEnv m_env;

  public void setEnv(RSEnv env) {
    m_env = env;
  }

  /**
   * Finds the city_id (ROTTERDAM), given a station code (RDMBLAAK).
   * 
   * @param code
   *          the station code, for example: RDMBLAAK.
   * @return the city id (for example: ROTTERDAM), or null if no city found.
   * @throws RSConnectionException
   *           if cannot connect to database.
   * @throws SQLException
   *           if SQL error occurs.
   */
  public String findCityForStation(String code) throws RSException, SQLException {
    String qry = "select city_id from rs_city where city_code = ?";
    String id = null;
    Connection dbcon = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String ccode = code.substring(0, 3);
    try {
      dbcon = m_env.getDBConnection();
      stmt = dbcon.prepareStatement(qry);
      stmt.setString(1, ccode);
      rs = stmt.executeQuery();
      while (rs.next()) {
        id = rs.getString("city_id");
      }
    } finally {
      closeQuery(stmt, rs);
      m_env.disconnect(dbcon);
    }
    return id;
  }

  /**
   * @param cityCode
   * @param statset
   * @throws SQLException
   */
  public void getStationsNodes(String cityCode, Map<String, Station> statmap)
      throws SQLException, RSException {
    String qry = "select station_id, description, local_name, station_type, "
        + " latitude, longitude from rs_network_station where city_id = ?";
    String id;
    String desc;
    String local_name;
    Station stat;
    String station_type;
    double lat;
    double lon;
    Connection dbcon = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      dbcon = m_env.getDBConnection();
      stmt = dbcon.prepareStatement(qry);
      stmt.setString(1, cityCode);
      rs = stmt.executeQuery();
      while (rs.next()) {
        id = rs.getString("station_id");
        desc = rs.getString("description");
        station_type = rs.getString("station_type");
        local_name = rs.getString("local_name");
        if (rs.wasNull()) {
          local_name = null;
        }
        lat = rs.getDouble("latitude");
        lon = rs.getDouble("longitude");
        if (rs.wasNull()) {
          stat = new Station(id, desc, local_name, station_type);
        } else {
          stat = new Station(id, desc, local_name, station_type, lat, lon);
        }
        assert null != stat;
        statmap.put(id, stat);
        stat = null;
      }
    } finally {
      closeQuery(stmt, rs);
      m_env.disconnect(dbcon);
    }
  }

  public void getLineDirections(String cityCode, List<Direction> dset)
      throws SQLException, RSException {
    String qry = "select dir_id, service_type, line, endpoint, remark "
        + " from rs_direction where city_id = ? order by description";
    String dir_id;
    String svtype;
    String line;
    String endpoint;
    String remark;
    Direction d;
    Connection dbcon = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    // Add special directions.
    // Walk-direction has hardcoded index = 0.
    Direction walk = new Direction(WalkDirection.DIRECTION_INDEX, WalkDirection.SERVICE_TYPE,
        WalkDirection.LINE, WalkDirection.ENDPOINT, WalkDirection.REMARK, WalkDirection.DIR_ID);
    dset.add(walk);

    // Set starting index for regular directions from database.
    int idx = 1;

    try {
      dbcon = m_env.getDBConnection();
      stmt = dbcon.prepareStatement(qry);
      stmt.setString(1, cityCode);
      rs = stmt.executeQuery();
      while (rs.next()) {
        dir_id = rs.getString("dir_id");
        svtype = rs.getString("service_type");
        line = rs.getString("line");
        endpoint = rs.getString("endpoint");
        remark = rs.getString("remark");
        d = new Direction(idx, svtype, line, endpoint, remark, dir_id);
        idx++;
        dset.add(d);
      }
    } finally {
      closeQuery(stmt, rs);
      m_env.disconnect(dbcon);
    }
  }

  /**
   * Adds information of all segments of a city to the segmentinfo collection.
   * 
   * @param ct
   *          The city for which segments are requested.
   * @param si
   *          the collection to add the information to.
   * @throws SQLException
   */
  public void getSegmentInfo(String cityCode, Set<SegmentInfo> si, Map<String, Station> statmap)
      throws SQLException, RSException {
    // Get parent and child info in one query.
    String qry = "select segment_id, "
        + " seqno, station_id from rs_seg_stop_metro where city_id = ?"
        + " order by segment_id, seqno desc";
    Connection dbcon = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String segCurrent = "init";
    String seg_id;
    String stat_id;
    int numStats = 0;
    int seqno = -1;
    SegmentInfo s = null;
    Station stat;
    try {
      dbcon = m_env.getDBConnection();
      stmt = dbcon.prepareStatement(qry);
      stmt.setString(1, cityCode);
      rs = stmt.executeQuery();
      while (rs.next()) {
        seg_id = rs.getString("segment_id");
        seqno = rs.getInt("seqno");
        if (!segCurrent.equals(seg_id)) {
          segCurrent = seg_id;
          numStats = seqno + 1; // seqno starts with zero.
          s = new SegmentInfo(seg_id, numStats);
          if (rs.wasNull()) {
            s.m_type = SegType.UNI_DIRECTIONAL;
          } else {
            s.m_type = SegType.BI_DIRECTIONAL;
          }
          si.add(s);
        }
        if (null == s) {
          throw new RSDataException("SegmentInfo is null");
        }
        stat_id = rs.getString("station_id");
        stat = statmap.get(stat_id);
        s.addLocation(stat, seqno);
      }
    } finally {
      closeQuery(stmt, rs);
      m_env.disconnect(dbcon);
    }
  }

  /**
   * Adds MNN segments to the segment collection.
   * 
   * @param cityCode
   * @param si
   *          the segment collection.
   * @param statmap
   * @throws SQLException
   * @throws RSConnectionException
   */
  public void getMNNSegments(String cityCode, Set<SegmentInfo> si, Map<String, Station> statmap)
      throws SQLException, RSException {
    String qry = "select from_stat, to_stat " + " from rs_mnn_segments where city_id = ?";
    Connection dbcon = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String fromStatId;
    String toStatId;
    SegmentInfo s = null;
    Station fromStat;
    Station toStat;
    int cnt = 0;
    try {
      dbcon = m_env.getDBConnection();
      stmt = dbcon.prepareStatement(qry);
      stmt.setString(1, cityCode);
      rs = stmt.executeQuery();
      while (rs.next()) {
        fromStatId = rs.getString("from_stat");
        fromStat = statmap.get(fromStatId);
        assert null != fromStat;
        toStatId = rs.getString("to_stat");
        toStat = statmap.get(toStatId);
        assert null != toStat;
        s = new SegmentInfo("MNN_" + cnt, 2);
        s.m_type = SegType.UNI_DIRECTIONAL;
        s.addLocation(fromStat, 0);
        s.addLocation(toStat, 1);
        s.m_forward.set(WalkDirection.DIRECTION_INDEX);
        si.add(s);
        cnt++;
      }
    } finally {
      closeQuery(stmt, rs);
      m_env.disconnect(dbcon);
    }
  }

  /**
   * Adds direction information to segments of a city.
   * <p>
   * MNN segments already have direction information and not processed (because they do not exist in
   * the segment_direction view.
   * 
   * @param cityCode
   *          The city for which segments are requested.
   * @param si
   *          the segments to add the information to.
   * @param dirs
   *          the directions. Required to get index (used for the bitvector).
   * @throws SQLException
   */
  public void getSegmentDirections(String cityCode, Set<SegmentInfo> si, List<Direction> dirs)
      throws SQLException, RSException {
    String qry = "select segment_id, dir_id, direction_type "
        + " from rs_seg_directions where city_id = ? order by segment_id";
    Connection dbcon = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String segCurrent = "init";
    String seg_id;
    String dir_id;
    String dir_type;
    SegmentInfo s = null;
    Direction d = null;

    Map<String, SegmentInfo> segmap = new HashMap<String, SegmentInfo>();
    for (SegmentInfo sinfo : si) {
      segmap.put(sinfo.m_name, sinfo);
    }
    Map<String, Direction> dirmap = new HashMap<String, Direction>();
    for (Direction dir : dirs) {
      dirmap.put(dir.getDirId(), dir);
    }
    try {
      dbcon = m_env.getDBConnection();
      stmt = dbcon.prepareStatement(qry);
      stmt.setString(1, cityCode);
      rs = stmt.executeQuery();
      while (rs.next()) {
        seg_id = rs.getString("segment_id");
        assert null != seg_id;
        if (!segCurrent.equals(seg_id)) {
          segCurrent = seg_id;
          s = segmap.get(segCurrent);
          if (null == s) {
            throw new RSDataException("SegmentInfo is null");
          }
          s.m_type = SegType.UNI_DIRECTIONAL;
        }
        dir_id = rs.getString("dir_id");
        assert null != dir_id;
        d = dirmap.get(dir_id);
        assert null != d;
        dir_type = rs.getString("direction_type");
        assert null != dir_type;
        if (null == s) {
          throw new RSDataException("SegmentInfo is null");
        }
        if ("FWD".equals(dir_type)) {
          s.m_forward.set(d.m_idx);
        } else {
          assert "BCK".equals(dir_type);
          s.m_back.set(d.m_idx);
          s.m_type = SegType.BI_DIRECTIONAL;
        }
      }
    } finally {
      closeQuery(stmt, rs);
      m_env.disconnect(dbcon);
    }
  }

  public void getTransformations(String cityCode, Set<MetroTransform> tr,
      Map<String, Station> statmap, List<Direction> dirs) throws SQLException, RSException {
    String qry = "select station_id, pre_dir_id, "
        + " post_dir_id from rs_transform_metro where city_id = ? " + " order by station_id";
    String statCurrent = "init";
    String statcode;
    Station stat;
    MetroTransform mt = null;

    Map<String, Direction> dirmap = new HashMap<String, Direction>();
    for (Direction dir : dirs) {
      dirmap.put(dir.getDirId(), dir);
    }

    String dir_id;
    Direction dirPre;
    Direction dirPost;

    Connection dbcon = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      dbcon = m_env.getDBConnection();
      stmt = dbcon.prepareStatement(qry);
      stmt.setString(1, cityCode);
      rs = stmt.executeQuery();
      while (rs.next()) {
        statcode = rs.getString("station_id");
        if (!statCurrent.equals(statcode)) {
          stat = statmap.get(statcode);
          mt = new MetroTransform(stat);
          tr.add(mt);
        }
        if (null != mt) {
          // TODO: handle NULL values.
          dir_id = rs.getString("pre_dir_id");
          assert null != dir_id;
          dirPre = dirmap.get(dir_id);
          assert null != dirPre;
          dir_id = rs.getString("post_dir_id");
          assert null != dir_id;
          dirPost = dirmap.get(dir_id);
          assert null != dirPost;
          mt.add(dirPre.m_idx, dirPost.m_idx);
        }
      }
    } finally {
      closeQuery(stmt, rs);
      m_env.disconnect(dbcon);
    }
  }

  /**
   * Adds information of all segments of a city to the segmentinfo collection.
   * 
   * @param ct
   *          The city for which segments are requested.
   * @param si
   *          the collection to add the information to.
   * @throws SQLException
   */
  public void getLandmarks(String cityCode, Map<String, Landmark> si, Map<String, Station> statmap)
      throws SQLException, RSException {
    // Get parent and child info in one query.
    String qry = "select landmark_id, description, local_name, latitude, "
        + " longitude, station_id from rs_landmark_station where city_id = ?"
        + " order by landmark_id";
    Connection dbcon = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String markCurrent = "init";
    String mark_id;
    String stat_id;
    String desc;
    String local_name;
    double lat;
    double lon;
    Landmark m = null;
    Station stat;
    try {
      dbcon = m_env.getDBConnection();
      stmt = dbcon.prepareStatement(qry);
      stmt.setString(1, cityCode);
      rs = stmt.executeQuery();
      while (rs.next()) {
        mark_id = rs.getString("landmark_id");
        if (!markCurrent.equals(mark_id)) {
          markCurrent = mark_id;
          desc = rs.getString("description");
          local_name = rs.getString("local_name");
          if (rs.wasNull()) {
            local_name = null;
          }
          lat = rs.getDouble("latitude");
          lon = rs.getDouble("longitude");
          if (rs.wasNull()) {
            m = new Landmark(mark_id, desc, local_name);
          } else {
            m = new Landmark(mark_id, desc, local_name, lat, lon);
          }
          si.put(mark_id, m);
        }
        if (null == m) {
          throw new RSDataException("Landmark is null");
        }

        stat_id = rs.getString("station_id");
        if (null != stat_id) { // To prevent that get() throws an NPE.
          stat = statmap.get(stat_id);
          if (null != stat) {
            // Database does not enforce referential integrity from
            // landmark-station to station. Therefore, the station lookup can
            // return null.
            m.addStation(stat);
          }
        }
      }
    } finally {
      closeQuery(stmt, rs);
      m_env.disconnect(dbcon);
    }
  }

  /**
   * Finds the city code (RDM) for a city name (ROTTERDAM)
   * 
   * @param cityName
   *          city name the city name, for example: ROTTERDAM.
   * @return city code, for example: RDM. Or null if not found.
   * @throws RSConnectionException
   * @throws SQLException
   */
  public String findCityCode(String cityName) throws RSException, SQLException {
    String qry = "select city_code from rs_city where city_id = ?";
    String citycode = null;
    Connection dbcon = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      dbcon = m_env.getDBConnection();
      stmt = dbcon.prepareStatement(qry);
      stmt.setString(1, cityName);
      rs = stmt.executeQuery();
      while (rs.next()) {
        citycode = rs.getString("city_code");
      }
    } finally {
      closeQuery(stmt, rs);
      m_env.disconnect(dbcon);
    }
    return citycode;
  }

  /**
   * Collect complex information for a city.
   * 
   * @param ct
   *          The city for which complex information is requested.
   * @param ci
   *          the collection to add the information to.
   * @throws SQLException
   * @throws RSConnectionException
   */
  public void getComplexInfo(String cityCode, Map<String, List<Station>> ci,
      Map<String, Station> statmap) throws SQLException, RSException {
    // Get parent and child info in one query.
    String qry = "select node_id, station_id " + " from rs_mnn_nodes where city_id = ?"
        + " and station_type in ('NETWORK', 'COMPLEX') " + " order by node_id, station_id";
    Connection dbcon = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String nodeCurrent = "init";
    String node_id;
    String stat_id;
    Station stat;
    List<Station> lst = new ArrayList<Station>();
    try {
      dbcon = m_env.getDBConnection();
      stmt = dbcon.prepareStatement(qry);
      stmt.setString(1, cityCode);
      rs = stmt.executeQuery();
      while (rs.next()) {
        node_id = rs.getString("node_id");
        if (!nodeCurrent.equals(node_id)) {
          nodeCurrent = node_id;
          lst = new ArrayList<Station>();
          ci.put(node_id, lst);
        }
        stat_id = rs.getString("station_id");
        if (null != stat_id) {
          stat = statmap.get(stat_id);
          assert null != stat;
          if (null == stat) {
            // Station lookup failure.
            if (DebugSettings.DEBUG_PRINT) {
              System.out.println("getComplexInfo. unknown station: " + stat_id);
            }
          } else {
            // Station found, add to list.
            lst.add(stat);
          }
        }
      }
    } finally {
      closeQuery(stmt, rs);
      m_env.disconnect(dbcon);
    }
  }

  /**
   * Appends direction overrides to the list.
   * 
   * @param cityCode
   *          city identifier. For example: OSLO.
   * @param lst
   * @throws SQLException
   * @throws RSConnectionException
   */
  public void getDirectionOverrides(String cityCode, List<DirectionOverride> lst)
      throws SQLException, RSException {
    String qry = "select station_id, dir_id, override_dir_id, line, endpoint, "
        + " service_type, remark from rs_dir_override where city_id = ?";
    Connection dbcon = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    DirectionOverride dir;
    try {
      dbcon = m_env.getDBConnection();
      stmt = dbcon.prepareStatement(qry);
      stmt.setString(1, cityCode);
      rs = stmt.executeQuery();
      while (rs.next()) {
        dir = new DirectionOverride();
        dir.m_station_id = rs.getString("station_id");
        dir.m_dir_id = rs.getString("dir_id");
        dir.m_override_dir_id = rs.getString("override_dir_id");
        dir.m_line = rs.getString("line");
        dir.m_endpoint = rs.getString("endpoint");
        dir.m_servicetype = rs.getString("service_type");
        dir.m_remark = rs.getString("remark");
        lst.add(dir);
      }
    } finally {
      closeQuery(stmt, rs);
      m_env.disconnect(dbcon);
    }
    return;
  }

  private void closeQuery(Statement stmt, ResultSet rs) {
    if (null != rs) {
      try {
        rs.close();
      } catch (SQLException e) {
        // do not propagate.
      }
    }

    if (null != stmt) {
      try {
        stmt.close();
      } catch (SQLException e) {
        // do not propagate.
      }
    }
  }
}
