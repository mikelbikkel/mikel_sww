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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.Locale;

import org.subwayworld.metrogen.MessageWriter;
import org.subwayworld.metrogen.MetroException;
import org.subwayworld.metrogen.MetroSettings.DBTYPE;
import org.subwayworld.metrogen.network.DirectionOverride;
import org.subwayworld.metrogen.network.MetroDirection;
import org.subwayworld.metrogen.network.MetroLine;
import org.subwayworld.metrogen.network.MetroNetwork;
import org.subwayworld.metrogen.network.MetroSegment;
import org.subwayworld.metrogen.network.MetroStation;
import org.subwayworld.metrogen.network.MetroTraject;
import org.subwayworld.metrogen.network.TransformInfo;

/**
 * Prints metro network information in MySQL insert statements.
 */
class SQLPrinter {

  private MetroNetwork m_data;

  private PrintWriter m_file;

  private String m_encoding;

  private DBTYPE m_dbtype;

  private MessageWriter m_mw;

  public SQLPrinter(MessageWriter mw) {
    m_mw = mw;
  }

  public void print(MetroNetwork data, String encoding, DBTYPE dbt)
      throws MetroException {
    m_data = data;
    m_encoding = encoding;
    m_dbtype = dbt;

    String basename = "line_info_" + m_data.city.id.toLowerCase(Locale.ENGLISH);
    String fname = basename + ".sql";

    m_mw.write("output_file", fname);

    FileOutputStream fos;
    try {
      fos = new FileOutputStream(fname);
    } catch (FileNotFoundException e) {
      throw new MetroException(e);
    }
    OutputStreamWriter osr;
    osr = new OutputStreamWriter(fos, Charset.forName(m_encoding));
    m_file = new PrintWriter(osr);

    printHeader(basename + ".log");

    insertCheckCity();
    m_file.println("");

    insertDeletes();
    m_file.println("");

    insertStations();
    m_file.println("");

    insertLines();
    m_file.println("");

    insertDirections();
    m_file.println("");

    insertDirectionOverrides();
    m_file.println("");

    insertSegments();
    m_file.println("");

    insertSegmentStops();
    m_file.println("");

    insertSegmentDirs();
    m_file.println("");

    insertTransformInfo();
    m_file.println("");

    insertTrajectInfo();
    m_file.println("");

    insertTrajectDirections();
    m_file.println("");

    insertTrajectStops();
    m_file.println("");

    insertStationEEL();
    m_file.println("");

    cleanupLandmarkStations();
    m_file.println("");

    cleanupLocations();
    m_file.println("");

    updateCity();
    m_file.println("");

    m_file.println("COMMIT;");
    m_file.println("");

    printFooter();

    m_file.println("-- End of file");
    m_file.println("");

    closeFile();
  }

  private void closeFile() {
    if (null == m_file) {
      return;
    }
    m_file.flush();
    m_file.close();
    m_file = null;
  }

  private void printHeader(String logfile) {
    if (DBTYPE.DBT_MYSQL == m_dbtype) {
      m_file.println("-- SQL generator.");
      m_file.println("");

      if (m_encoding.equals("UTF-8")) {
        m_file.println("set names 'utf8';");
      } else {
        m_file.println("set names 'latin1';");
      }
      m_file.println("");

      m_file.println("-- Use START TRANSACTION to disable autocommit.");
      m_file.println("START TRANSACTION;");
      m_file.println("");
      return;
    }
    if (DBTYPE.DBT_DB2 == m_dbtype) {
      m_file.println("-- SQL generator.");
      m_file.println("");
      m_file.println("--");
      m_file.println("-- Execute this script using: db2 -tvf scriptname");
      m_file.println("--");
      m_file.println("");
      m_file.println("CONNECT TO MKTEST01;");
      m_file.println("");
      m_file.println("SET SCHEMA MIKELXML;");
      m_file.println("");
      m_file.println("-- Disable autocommit.");
      m_file.println("UPDATE COMMAND OPTIONS USING c OFF;");
      m_file.println("");
      m_file.println("-- Stop processing after error.");
      m_file.println("UPDATE COMMAND OPTIONS USING s ON;");
      m_file.println("");
      return;
    }
    if (DBTYPE.DBT_ORACLE == m_dbtype) {
      m_file.println("-- SQL generator.");
      m_file.println("set autocommit off");
      m_file.println("");

      m_file.println("spool " + logfile);
      m_file.println("");
      return;
    }
  }

  private void insertDeletes() {
    m_file.println("");
    m_file.println("--");
    m_file.println("-- Delete existing network data.");
    m_file.println("--");

    m_file.println("UPDATE city SET has_line_info = 'N' where city_id = '"
        + m_data.city.id + "';");
    m_file.println("");

    m_file.println("");
    m_file.println("DELETE FROM direction_override WHERE station_id LIKE '"
        + m_data.city.code + "%';");

    m_file.println("DELETE FROM sp_seg_stop WHERE segment_id LIKE '"
        + m_data.city.code + "%';");
    m_file.println("");
    m_file.println("DELETE FROM sp_seg_direction WHERE dir_id LIKE '"
        + m_data.city.code + "%';");
    m_file.println("");
    m_file.println("DELETE FROM transform_point WHERE station_id LIKE '"
        + m_data.city.code + "%';");
    m_file.println("");
    m_file.println(
        "DELETE FROM sp_segment WHERE city_id = '" + m_data.city.id + "';");
    m_file.println("");
    m_file.println(
        "DELETE FROM direction WHERE city_id = '" + m_data.city.id + "';");
    m_file.println("");
    m_file.println(
        "DELETE FROM station WHERE city_id = '" + m_data.city.id + "';");
    m_file.println("");
    m_file.println(
        "DELETE FROM swwline WHERE city_id = '" + m_data.city.id + "';");
    m_file.println("");
    if (m_data.city.id.startsWith("TEST")) {
      m_file.println(
          "DELETE FROM city WHERE city_id = '" + m_data.city.id + "';");
      m_file.println("");
    }
    m_file.println("DELETE FROM ti_traject_stop WHERE traject_id LIKE '"
        + m_data.city.code + "%';");
    m_file.println("");
    m_file.println("DELETE FROM ti_traject WHERE traject_id LIKE '"
        + m_data.city.code + "%';");
    m_file.println("");
    m_file.println("--");
    m_file.println("-- Insert new network data.");
    m_file.println("--");
    m_file.println("");
  }

  private void printFooter() {
    if (DBTYPE.DBT_MYSQL == m_dbtype) {
      m_file.println("");
      return;
    }
    if (DBTYPE.DBT_DB2 == m_dbtype) {
      m_file.println("");
      m_file.println("CONNECT RESET;");
      m_file.println("");
      m_file.println("TERMINATE;");
      m_file.println("");
      return;
    }
    if (DBTYPE.DBT_ORACLE == m_dbtype) {
      m_file.println("");
      m_file.println("spool off");
      m_file.println("exit");
      m_file.println("");
      return;
    }
  }

  private void insertStations() {
    if (m_data.city.id.startsWith("TEST")) {
      m_file.println("INSERT INTO city(city_id, city_code, description, "
          + "continent_id, country_id)");
      m_file.print("VALUES( ");
      m_file.print("'" + m_data.city.id + "', '" + m_data.city.code + "', '"
          + m_data.city.id + "', 'EUROPE', 'NETHERLANDS'");
      m_file.println(");");
      m_file.println("");
    }

    for (MetroStation s : m_data.stats) {
      m_file.println("INSERT INTO station(station_id, description, city_id,"
          + " node_id, station_type)");
      m_file.print("VALUES( ");
      m_file.print(
          "'" + s.getId() + "', '" + s.getName() + "', '" + m_data.city.id
              + "', '" + s.getNode() + "', '" + s.getType().toString() + "')");
      m_file.println(";");
    }
  }

  private void insertDirections() {
    for (MetroDirection md : m_data.dirs) {
      m_file.print("INSERT INTO direction");
      m_file.println("(dir_id, city_id, description, line_id, endpoint)");
      m_file.print("VALUES( ");
      m_file.print(
          "'" + md.m_dir_id + "', '" + m_data.city.id + "', '" + md.m_desc
              + "', '" + md.m_line.m_line_id + "', '" + md.m_endpoint + "')");
      m_file.println(";");
    }
  }

  private void insertSegments() {
    for (MetroSegment seg : m_data.segs) {
      m_file.println(
          "INSERT INTO sp_segment(segment_id, city_id, " + "transport_id)");
      m_file.print("VALUES( ");
      m_file.println("'" + seg.m_id + "', '" + m_data.city.id + "', '"
          + m_data.city.linetype + "');");
    }
  }

  private void insertSegmentStops() {
    int i = 0;
    String stat_id;
    for (MetroSegment seg : m_data.segs) {
      i = 0;
      for (MetroStation s : seg.m_stations) {
        stat_id = s.getId();
        m_file.println(
            "INSERT INTO sp_seg_stop(segment_id, seqno, " + "station_id)");
        m_file.print("VALUES( ");
        m_file.println("'" + seg.m_id + "', " + i + ", '" + stat_id + "');");
        i++;
      }
    }
  }

  private void insertTransformInfo() {
    String stat_id;
    for (TransformInfo ti : m_data.trans) {
      stat_id = ti.station.getId();
      m_file.println("INSERT INTO transform_point(station_id, "
          + " pre_dir_id, post_dir_id)");
      m_file.print("VALUES( ");
      m_file.print("'" + stat_id + "', '" + ti.mdPre.m_dir_id + "', '"
          + ti.mdPost.m_dir_id + "')");
      m_file.println(";");
    }
  }

  private void insertSegmentDirs() {
    for (MetroSegment seg : m_data.segs) {
      for (MetroDirection md : seg.m_fwd) {
        m_file.println("INSERT INTO sp_seg_direction(segment_id, "
            + "dir_id, direction_type)");
        m_file.print("VALUES( ");
        m_file.print("'" + seg.m_id + "', '" + md.m_dir_id + "', 'FWD')");
        m_file.println(";");
      }
      for (MetroDirection md : seg.m_bck) {
        m_file.println("INSERT INTO sp_seg_direction(segment_id, "
            + "dir_id, direction_type)");
        m_file.print("VALUES( ");
        m_file.print("'" + seg.m_id + "', '" + md.m_dir_id + "', 'BCK')");
        m_file.println(";");
      }
    }
  }

  private void insertLines() {
    String remark;
    String editinfo;

    for (MetroLine line : m_data.lines) {
      if (null == line.m_remark) {
        remark = "NULL"; // without quotes.
      } else {
        remark = "'" + line.m_remark + "'"; // with quotes.
      }
      if (null == line.m_editinfo) {
        editinfo = "NULL"; // without quotes.
      } else {
        editinfo = "'" + line.m_editinfo + "'"; // with quotes.
      }
      m_file.print("INSERT INTO swwline");
      m_file.println("(line_id, city_id, description, service_type, "
          + " remark, editinfo)");
      m_file.print("VALUES( ");
      m_file.print("'" + line.m_line_id + "', '" + m_data.city.id + "', '"
          + line.m_desc + "', '" + line.m_svtype + "', " + remark + ", "
          + editinfo + ")");
      m_file.println(";");
    }
  }

  private void insertDirectionOverrides() {
    for (DirectionOverride dor : m_data.odirs) {
      m_file.println("INSERT INTO direction_override "
          + " (station_id, dir_id, override_dir_id)");
      m_file.print(" VALUES( ");
      m_file.print("'" + dor.m_station.getId() + "', '"
          + dor.m_dijkstraDirection.m_dir_id + "', '"
          + dor.m_overrideDirection.m_dir_id + "')");
      m_file.println(";");
    }
  }

  private void insertStationEEL() {
    if (DBTYPE.DBT_MYSQL == m_dbtype) {
      m_file.println("CALL gen_station_eel_one('" + m_data.city.code + "');");
      m_file.println("");
      return;
    }
  }

  private void updateCity() {
    m_file.println("UPDATE city SET has_line_info = 'Y' where city_id = '"
        + m_data.city.id + "'");
    m_file.println(";");
  }

  private void insertTrajectInfo() {
    for (MetroTraject tr : m_data.traject_info) {
      m_file
          .println("INSERT INTO ti_traject(traject_id, city_id, transport_id)");
      m_file.print("VALUES( ");
      m_file.println("'" + tr.m_id + "', '" + m_data.city.id + "', '"
          + m_data.city.linetype + "');");
    }
  }

  private void insertTrajectDirections() {
    for (MetroTraject tr : m_data.traject_info) {
      m_file.println("INSERT INTO ti_traject_dir(traject_id, fwd_dir_id)");
      m_file
          .println("VALUES( '" + tr.m_id + "', '" + tr.m_fwd.m_dir_id + "');");
    }
  }

  private void insertTrajectStops() {
    int i = 0;
    String stat_id;
    for (MetroTraject tr : m_data.traject_info) {
      i = 0;
      for (MetroStation s : tr.m_stations) {
        stat_id = s.getId();
        m_file.println("INSERT INTO ti_traject_stop(traject_id, "
            + "stop_sequence, station_id)");
        m_file.print("VALUES( ");
        m_file.println("'" + tr.m_id + "', " + i + ", '" + stat_id + "');");
        i++;
      }
    }
  }

  private void insertCheckCity() {
    if (m_data.city.id.startsWith("TEST")) {
      // test cities are added after this check.
      // So the check is not applicable for test cities.
      return;
    }
    if (DBTYPE.DBT_MYSQL == m_dbtype) {
      m_file.println("CALL check_city_id_code('" + m_data.city.id + "', '"
          + m_data.city.code + "');");
      m_file.println("");
      return;
    }
  }

  /**
   * Delete landmark-station records that reference a station that no longer
   * exist. This happens when a station is renamed.
   * 
   */
  private void cleanupLandmarkStations() {
    String sqltext = "DELETE FROM landmark_station WHERE station_id NOT IN "
        + "(SELECT station_id FROM station) AND landmark_id LIKE '"
        + m_data.city.code + "%'";
    m_file.println(sqltext);
    m_file.println(";");
  }

  /**
   * Delete location records that reference a station that no longer exist. This
   * happens when a station is renamed.
   * 
   * @throws SQLException
   *           when delete goes wrong.
   */
  private void cleanupLocations() {
    String sqltext = "DELETE FROM location WHERE location_id NOT IN "
        + "(SELECT station_id FROM station) AND upper(type) = 'STATION' AND "
        + " location_id LIKE '" + m_data.city.code + "%'";
    m_file.println(sqltext);
    m_file.println(";");
  }
}
