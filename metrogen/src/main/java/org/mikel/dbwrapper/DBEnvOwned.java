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

package org.mikel.dbwrapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database environment for a connection owned by the application.<br>
 */
class DBEnvOwned extends DBEnv {

  private Connection m_con;

  public DBEnvOwned(String drivername, String url, String username, String password,
      boolean autocommit) throws DBException {
    try {
      // The newInstance() call is a work around for some
      // broken Java implementations
      Class.forName(drivername).getDeclaredConstructor().newInstance();
      if (null == username) {
        m_con = DriverManager.getConnection(url);
      } else {
        m_con = DriverManager.getConnection(url, username, password);
      }
      m_con.setAutoCommit(autocommit);
    } catch (Exception e) {
      String msg = "envOwned: " + e.getMessage();
      DBException se = new DBException(msg, e);
      throw se;
    }
  }

  @Override
  protected synchronized Connection getDBConnection() throws SQLException {
    return m_con;
  }

  @Override
  protected synchronized void disconnect(Connection con) {
    // Do not close connection in standalone mode.
  }

  @Override
  public synchronized void destroy() {
    if (null == m_con) {
      return;
    }
    try {
      m_con.close();
    } catch (SQLException e) {
      // ignore
    } finally {
      m_con = null;
    }
  }
}
