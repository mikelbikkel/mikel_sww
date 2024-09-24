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

package org.subwayworld.metrogen.output;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import org.subwayworld.metrogen.MetroException;
import org.mikel.dbwrapper.BatchQueryOwner;
import org.mikel.dbwrapper.DBEnv;
import org.mikel.dbwrapper.DBException;
import org.mikel.dbwrapper.IDBQueryOwner;
import org.subwayworld.metrogen.network.DirectionOverride;
import org.subwayworld.metrogen.network.MetroCity;
import org.subwayworld.metrogen.network.MetroDirection;
import org.subwayworld.metrogen.network.MetroLine;
import org.subwayworld.metrogen.network.MetroNetwork;
import org.subwayworld.metrogen.network.MetroSegment;
import org.subwayworld.metrogen.network.MetroStation;
import org.subwayworld.metrogen.network.MetroTraject;
import org.subwayworld.metrogen.network.TransformInfo;

/**
 * Updates the SWW database.
 */
class DBUpdate {

  private MetroNetwork m_data;

  // private String m_encoding;

  private DBEnv m_env;

  public void print(MetroNetwork data, DBEnv env, String encoding)
      throws MetroException {
    m_data = data;
    m_env = env;
    // m_encoding = encoding;

    checkCity();

    try {
      m_env.startTransaction();

      insertDeletes();

      insertStations();

      insertLines();

      insertDirections();

      insertDirectionOverrides();

      insertSegments();

      insertSegmentStops();

      insertSegmentDirs();

      insertTransformInfo();

      insertTrajectInfo();
      insertTrajectDirections();
      insertTrajectStops();

      insertStationEEL(m_data.city.code);

      cleanupLandmarkStations();
      cleanupLocations();

      updateCity();

      m_env.commit();
    } catch (SQLException | DBException e) {
      m_env.rollback();
      throw new MetroException(e);
    }
  }

  private void insertDeletes() throws SQLException, DBException {
    String sqltext;

    sqltext = "UPDATE city SET has_line_info = 'N' where city_id = '"
        + m_data.city.id + "'";
    m_env.execStmt(sqltext);

    sqltext = "DELETE FROM direction_override WHERE station_id LIKE '"
        + m_data.city.code + "%'";
    m_env.execStmt(sqltext);

    sqltext = "DELETE FROM sp_seg_stop WHERE segment_id LIKE '"
        + m_data.city.code + "%'";
    m_env.execStmt(sqltext);

    sqltext = "DELETE FROM sp_seg_direction WHERE dir_id LIKE '"
        + m_data.city.code + "%'";
    m_env.execStmt(sqltext);

    sqltext = "DELETE FROM transform_point WHERE station_id LIKE '"
        + m_data.city.code + "%'";
    m_env.execStmt(sqltext);

    sqltext = "DELETE FROM sp_segment WHERE city_id = '" + m_data.city.id + "'";
    m_env.execStmt(sqltext);

    sqltext = "DELETE FROM direction WHERE city_id = '" + m_data.city.id + "'";
    m_env.execStmt(sqltext);

    sqltext = "DELETE FROM station WHERE city_id = '" + m_data.city.id + "'";
    m_env.execStmt(sqltext);

    sqltext = "DELETE FROM swwline WHERE city_id = '" + m_data.city.id + "'";
    m_env.execStmt(sqltext);

    if (m_data.city.id.startsWith("TEST")) {
      sqltext = "DELETE FROM city WHERE city_id = '" + m_data.city.id + "'";
      m_env.execStmt(sqltext);
    }

    sqltext = "DELETE FROM ti_traject_stop WHERE traject_id LIKE '"
        + m_data.city.code + "%'";
    m_env.execStmt(sqltext);

    sqltext = "DELETE FROM ti_traject WHERE traject_id LIKE '"
        + m_data.city.code + "%'";
    m_env.execStmt(sqltext);

    sqltext = "DELETE FROM station_eel WHERE station_id LIKE '"
        + m_data.city.code + "%'";
    m_env.execStmt(sqltext);
  }

  private void insertStations() throws SQLException, DBException {
    if (m_data.city.id.startsWith("TEST")) {
      StringBuilder sb = new StringBuilder();
      sb.append("INSERT INTO city(city_id, city_code, description, "
          + "continent_id, country_id) VALUES(");
      sb.append("'" + m_data.city.id + "', '" + m_data.city.code + "', '"
          + m_data.city.id + "', 'EUROPE', 'NETHERLANDS')");
      String sqltext = sb.toString();
      m_env.execStmt(sqltext);
    }

    m_env.insertBatch(m_data.stats);
  }

  private void insertDirections() throws SQLException, DBException {
    String qry = "insert into direction(dir_id, city_id, description, line_id, endpoint) values (?, ?, ?, ?, ?)";
    m_env.execBatch(qry, m_data.dirs, new BatchQueryOwner<MetroDirection>() {
      @Override
      public void setParams(PreparedStatement stmt, MetroDirection obj)
          throws DBException, SQLException {
        stmt.setString(1, obj.m_dir_id);
        stmt.setString(2, m_data.city.id);
        stmt.setString(3, obj.m_desc);
        stmt.setString(4, obj.m_line.m_line_id);
        stmt.setString(5, obj.m_endpoint);
      }
    });
  }

  private void insertSegments() throws SQLException, DBException {
    String qry = "insert into sp_segment(segment_id, city_id, transport_id)  values (?, ?, ?)";
    m_env.execBatch(qry, m_data.segs, new BatchQueryOwner<MetroSegment>() {
      @Override
      public void setParams(PreparedStatement stmt, MetroSegment obj)
          throws DBException, SQLException {
        stmt.setString(1, obj.m_id);
        stmt.setString(2, m_data.city.id);
        stmt.setString(3, m_data.city.linetype);
      }
    });
  }

  /**
   * Helper class, to have an updatable counter inside setParams.
   * <p>
   * The variable used as a counter must be final. So we cannot use a simple
   * {@code int} value. When we wrap the integer in a class instance, and we
   * make the class instance final... then... the member {@code i} can be updated.
   */
  private class Countertje {
    int i;
  }

  private void insertSegmentStops() throws SQLException, DBException {
    String qry = "insert into sp_seg_stop(segment_id, seqno, station_id)  values (?, ?, ?)";
    final Countertje c = new Countertje();
    for (final MetroSegment seg : m_data.segs) {
      c.i = 0;
      m_env.execBatch(qry, seg.m_stations, new BatchQueryOwner<MetroStation>() {
        @Override
        public void setParams(PreparedStatement stmt, MetroStation obj)
            throws DBException, SQLException {
          stmt.setString(1, seg.m_id);
          stmt.setInt(2, c.i);
          stmt.setString(3, obj.getId());
          c.i++;
        }
      });
    }
  }

  private void insertTransformInfo() throws SQLException, DBException {
    String qry = "insert into transform_point(station_id, pre_dir_id, post_dir_id)  values (?, ?, ?)";
    m_env.execBatch(qry, m_data.trans, new BatchQueryOwner<TransformInfo>() {
      @Override
      public void setParams(PreparedStatement stmt, TransformInfo obj)
          throws DBException, SQLException {
        stmt.setString(1, obj.station.getId());
        stmt.setString(2, obj.mdPre.m_dir_id);
        stmt.setString(3, obj.mdPost.m_dir_id);
      }
    });
  }

  private void insertSegmentDirs() throws SQLException, DBException {
    String qry = "insert into sp_seg_direction(segment_id, dir_id, direction_type)  values (?, ?, ?)";
    for (final MetroSegment seg : m_data.segs) {
      m_env.execBatch(qry, seg.m_fwd, new BatchQueryOwner<MetroDirection>() {
        @Override
        public void setParams(PreparedStatement stmt, MetroDirection obj)
            throws DBException, SQLException {
          stmt.setString(1, seg.m_id);
          stmt.setString(2, obj.m_dir_id);
          stmt.setString(3, "FWD");
        }
      });
      m_env.execBatch(qry, seg.m_bck, new BatchQueryOwner<MetroDirection>() {
        @Override
        public void setParams(PreparedStatement stmt, MetroDirection obj)
            throws DBException, SQLException {
          stmt.setString(1, seg.m_id);
          stmt.setString(2, obj.m_dir_id);
          stmt.setString(3, "BCK");
        }
      });
    }
  }

  private void insertLines() throws SQLException, DBException {
    String qry = "insert into swwline(line_id, city_id, description, service_type, remark, editinfo)  values (?, ?, ?, ?, ?, ?)";
    m_env.execBatch(qry, m_data.lines, new BatchQueryOwner<MetroLine>() {
      @Override
      public void setParams(PreparedStatement stmt, MetroLine obj)
          throws DBException, SQLException {
        stmt.setString(1, obj.m_line_id);
        stmt.setString(2, m_data.city.id);
        stmt.setString(3, obj.m_desc);
        stmt.setString(4, obj.m_svtype);
        if (null == obj.m_remark) {
          stmt.setNull(5, Types.VARCHAR);
        } else {
          stmt.setString(5, obj.m_remark);
        }
        if (null == obj.m_editinfo) {
          stmt.setNull(6, Types.VARCHAR);
        } else {
          stmt.setString(6, obj.m_editinfo);
        }
      }
    });
  }

  private void insertTrajectInfo() throws SQLException, DBException {
    String qry = "insert into ti_traject(traject_id, city_id, transport_id) values (?, ?, ?)";
    m_env.execBatch(qry, m_data.traject_info,
        new BatchQueryOwner<MetroTraject>() {
          @Override
          public void setParams(PreparedStatement stmt, MetroTraject obj)
              throws DBException, SQLException {
            stmt.setString(1, obj.m_id);
            stmt.setString(2, m_data.city.id);
            stmt.setString(3, m_data.city.linetype);
          }
        });
  }

  private void insertTrajectDirections() throws SQLException, DBException {
    String qry = "insert into ti_traject_dir(traject_id, fwd_dir_id) values (?, ?)";
    m_env.execBatch(qry, m_data.traject_info,
        new BatchQueryOwner<MetroTraject>() {
          @Override
          public void setParams(PreparedStatement stmt, MetroTraject obj)
              throws DBException, SQLException {
            stmt.setString(1, obj.m_id);
            stmt.setString(2, obj.m_fwd.m_dir_id);
          }
        });
  }

  private void insertTrajectStops() throws SQLException, DBException {
    String qry = "insert into ti_traject_stop(traject_id, stop_sequence, station_id) values(?, ?, ?)";
    final Countertje c = new Countertje();
    for (final MetroTraject tr : m_data.traject_info) {
      c.i = 0;
      m_env.execBatch(qry, tr.m_stations, new BatchQueryOwner<MetroStation>() {
        @Override
        public void setParams(PreparedStatement stmt, MetroStation obj)
            throws DBException, SQLException {
          stmt.setString(1, tr.m_id);
          stmt.setInt(2, c.i);
          stmt.setString(3, obj.getId());
          c.i++;
        }
      });
    }
  }

  private void insertDirectionOverrides() throws SQLException, DBException {
    String qry = "insert into direction_override(station_id, dir_id, override_dir_id) values (?, ?, ?)";
    m_env.execBatch(qry, m_data.odirs,
        new BatchQueryOwner<DirectionOverride>() {
          @Override
          public void setParams(PreparedStatement stmt, DirectionOverride obj)
              throws DBException, SQLException {
            stmt.setString(1, obj.m_station.getId());
            stmt.setString(2, obj.m_dijkstraDirection.m_dir_id);
            stmt.setString(3, obj.m_overrideDirection.m_dir_id);
          }
        });
  }

  private void insertStationEEL(final String city_code)
      throws SQLException, DBException {
    m_env.execProcedure("{ call gen_station_eel(?) }", new IDBQueryOwner() {
      @Override
      public void setParams(PreparedStatement stmt)
          throws DBException, SQLException {
        stmt.setString(1, city_code);
      }
    });
  }

  /**
   * Checks if the city_id and the city_code is a known combination.
   * 
   * @throws MetroException
   *           if combination city_id and city_code is unknown.
   */
  private void checkCity() throws MetroException {
    if (m_data.city.id.startsWith("TEST")) {
      // test cities are added after this check.
      // So the check is not applicable for test cities.
      return;
    }

    SubwayReader sr = new SubwayReader(m_env);
    MetroCity ct = sr.getCity(m_data.city.id);
    if (!ct.code.equals(m_data.city.code)) {
      throw new MetroException("Unknown city_id/city_code combination.");
    }

  }

  private void updateCity() throws SQLException, DBException {
    String sqltext = "UPDATE city SET has_line_info = 'Y' where city_id = '"
        + m_data.city.id + "'";
    m_env.execStmt(sqltext);
  }

  /**
   * Delete landmark-station records that reference a station that no longer
   * exist. This happens when a station is renamed.
   * 
   * @throws SQLException
   *           when delete goes wrong.
   */
  private void cleanupLandmarkStations() throws SQLException, DBException {
    String sqltext = "DELETE FROM landmark_station WHERE station_id NOT IN "
        + "(SELECT station_id FROM station) AND landmark_id LIKE '"
        + m_data.city.code + "%'";
    m_env.execStmt(sqltext);
  }

  /**
   * Delete location records that reference a station that no longer exist. This
   * happens when a station is renamed.
   * 
   * @throws SQLException
   *           when delete goes wrong.
   */
  private void cleanupLocations() throws SQLException, DBException {
    String sqltext = "DELETE FROM location WHERE location_id NOT IN "
        + "(SELECT station_id FROM station) AND upper(location_type) = 'STATION' AND "
        + " location_id LIKE '" + m_data.city.code + "%'";
    m_env.execStmt(sqltext);
  }
}
