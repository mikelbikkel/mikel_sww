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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Environment for RouteService.<br>
 * Manages resources that are acquired via the application.
 * <p>
 * Container can be a servlet or a standalone java application.
 * <p>
 * Policy: When to create a new connection When to cloase a connection.
 * Scenario's: servlet context standalone java context.
 * 
 */
class RSEnvOwned implements RSEnv {

  private Connection m_con;

  /**
   * Creates a database connection that is owned by the application itself.
   * 
   * @param drivername
   *          name of JDBC driver
   * @param url
   *          url for the connection
   * @param username
   *          username
   * @param password
   *          password
   * @param autocommit
   *          sets the autocommit property for the connection
   * @throws RSConnectionException
   *           when a database connection cannot be made.
   */
  public RSEnvOwned(String drivername, String url, String username,
      String password, boolean autocommit) throws RSConnectionException {
    try {
      // The newInstance() call is a work around for some
      // broken Java implementations
      Class.forName(drivername).newInstance();
      if (null == username) {
        m_con = DriverManager.getConnection(url);
      } else {
        m_con = DriverManager.getConnection(url, username, password);
      }
      m_con.setAutoCommit(autocommit);

      // m_con = DriverManager.getConnection("jdbc:mysql://localhost/sww?"
      // + "user=sww_reader&password=sww_reader&useServerPrepStmts=true");
    } catch (Exception e) {
      String msg = "envOwned: " + e.getMessage();
      RSConnectionException se = new RSConnectionException(msg, e);
      throw se;
    }
  }

  /**
   * Lifetime method. Same as servlet.
   */
  @Override
  public synchronized void destroy() {
    closeCon(m_con);
    m_con = null;
  }

  /**
   * Returns the database conenction.
   * 
   * @return the database connection
   * @throws SQLException
   *           when connection cannot be made.
   */
  @Override
  public synchronized Connection getDBConnection() throws SQLException {
    return m_con;
  }

  /**
   * Do nothing<br>
   * For a standalone context, the destroy method will close the connection.
   * 
   * @param con
   *          the database connection.
   */
  @Override
  public synchronized void disconnect(Connection con) {
    // Do not close connection in standalone mode.
    // closeCon(con);
  }

  private void closeCon(Connection con) {
    if (null == con) {
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
