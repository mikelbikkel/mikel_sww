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

package org.subwayworld.metrogen.database;

import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This module handles all interactions with the SWW database.
 */
public class MetroDB {

  private RSEnv m_env;

  /**
   * Log file.
   */
  private PrintWriter m_log;

  public void initService(RSEnv env, PrintWriter log) {
    m_log = log;
    m_env = env;
  }

  public void stopService() {
    if (null != m_env) {
      m_env.destroy();
      m_env = null;
    }
  }

  public int execDML(String sqltext) throws SQLException {
    Connection dbcon = null;
    Statement stmt = null;
    int cnt = -1;
    try {
      log(sqltext);
      dbcon = m_env.getDBConnection();
      stmt = dbcon.createStatement();
      cnt = stmt.executeUpdate(sqltext);
      log("OK. Rows affected: " + cnt);
    } finally {
      closeDML(stmt);
      m_env.disconnect(dbcon);
    }
    return cnt;
  }

  public void execProcStationEel(String city_code) throws SQLException {
    execEELProc("{call gen_station_eel_one(?)}", city_code);
  }

  public void execProcLandmarkEel(String city_code) throws SQLException {
    execEELProc("{call gen_landmark_eel_one(?)}", city_code);
  }

  public void execProcCheckCity(String city_id, String city_code)
      throws SQLException {
    execCheckCityProc("{call check_city_id_code(?, ?)}", city_id, city_code);
  }

  private void execEELProc(String sqltext, String city_code)
      throws SQLException {
    Connection dbcon = null;
    CallableStatement cs = null;
    try {
      log(sqltext + " [" + city_code + "]");
      dbcon = m_env.getDBConnection();
      cs = dbcon.prepareCall(sqltext);
      cs.setString(1, city_code);
      cs.executeQuery();
      log("OK.");
    } finally {
      closeDML(cs);
      m_env.disconnect(dbcon);
    }
  }

  private void execCheckCityProc(String sqltext, String city_id,
      String city_code) throws SQLException {
    Connection dbcon = null;
    CallableStatement cs = null;
    try {
      log(sqltext + " [" + city_id + "], [" + city_code + "]");
      dbcon = m_env.getDBConnection();
      cs = dbcon.prepareCall(sqltext);
      cs.setString(1, city_id);
      cs.setString(2, city_code);
      cs.executeQuery();
      log("OK.");
    } finally {
      closeDML(cs);
      m_env.disconnect(dbcon);
    }
  }

  @SuppressWarnings("unused")
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

  private void closeDML(Statement stmt) {
    if (null != stmt) {
      try {
        stmt.close();
      } catch (SQLException e) {
        // do not propagate.
      }
    }
  }

  private void log(String txt) {
    if (null == m_log) {
      return;
    }
    m_log.println(txt);
    m_log.flush();
    if (m_log.checkError()) {
      System.err.println("Log error: " + txt);
    }

  }
}
