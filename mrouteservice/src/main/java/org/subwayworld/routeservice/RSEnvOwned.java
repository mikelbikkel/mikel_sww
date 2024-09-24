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

package org.subwayworld.routeservice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
public class RSEnvOwned implements RSEnv {

  private Log m_log = LogFactory.getLog(getClass());

  private Connection m_con;

  /**
   * Lifetime method. Same as servlet.
   * 
   * @param ctype
   *          Type of connection that is requested.
   * @param drivername
   *          For owned connection, name of driver to load.
   * @throws RSConnectionException
   */
  public RSEnvOwned(String drivername, String url, String username,
      String password) throws RSConnectionException {
    try {
      // The newInstance() call is a work around for some
      // broken Java implementations
      Class.forName(drivername).newInstance();
      if (null == username) {
        m_con = DriverManager.getConnection(url);
      } else {
        m_con = DriverManager.getConnection(url, username, password);
      }

      // m_con = DriverManager.getConnection("jdbc:mysql://localhost/sww?"
      // + "user=sww_reader&password=sww_reader&useServerPrepStmts=true");
    } catch (Exception ex) {
      if (m_log.isErrorEnabled()) {
        String msg;
        msg = "Exception [RSEnvOwned.ctor]: " + ex.getMessage();
        m_log.error(msg, ex);
      }
      RSConnectionException se = new RSConnectionException();
      se.initCause(ex);
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
   * @return
   * @throws IllegalStateException
   */
  @Override
  public synchronized Connection getDBConnection() throws RSConnectionException {
    return m_con;
  }

  /**
   * @param con
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
      if (m_log.isErrorEnabled()) {
        String msg;
        msg = "SQLException [RSEnvOwned.closeCon]: " + e.getSQLState() + " [ "
            + e.getErrorCode() + " ].";
        m_log.error(msg, e);
      }
      // ignore
    } finally {
      m_con = null;
    }
  }
}
