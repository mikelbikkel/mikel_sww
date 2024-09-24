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
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

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
public class RSEnvPooled implements RSEnv {

  private Log m_log = LogFactory.getLog(getClass());

  private DataSource m_ds = null;

  public RSEnvPooled(String dsname) throws RSConnectionException {
    try {
      Context initCtx = new InitialContext();
      Context envCtx = (Context) initCtx.lookup("java:comp/env");

      m_ds = (DataSource) envCtx.lookup(dsname);
    } catch (NamingException e) {
      if (m_log.isErrorEnabled()) {
        String msg;
        msg = "NamingException [RSEnvPooled.ctor]: " + e.getMessage();
        m_log.error(msg, e);
      }
      m_ds = null;
      RSConnectionException ce = new RSConnectionException();
      ce.initCause(e);
      throw ce;
    }
  }

  /**
   * Lifetime method. Same as servlet.
   */
  @Override
  public synchronized void destroy() {
  }

  /**
   * @return
   * @throws RSConnectionException
   */
  @Override
  public synchronized Connection getDBConnection() throws RSConnectionException {
    Connection con = null;
    try {
      con = m_ds.getConnection();
    } catch (SQLException e) {
      if (m_log.isErrorEnabled()) {
        String msg;
        msg = "SQLException [RSEnvPooled.getDBConnection]: " + e.getSQLState()
            + " [ " + e.getErrorCode() + " ].";
        m_log.error(msg, e);
      }

      RSConnectionException ce = new RSConnectionException();
      ce.initCause(e);
      throw ce;
    }
    return con;
  }

  /**
   * @param con
   */
  @Override
  public synchronized void disconnect(Connection con) {
    try {
      // Close connection to return it to the pool.
      con.close();
    } catch (SQLException e) {
      String msg;
      msg = "SQLException [RSEnvPooled.disconnect]: " + e.getSQLState() + " [ "
          + e.getErrorCode() + " ].";
      m_log.error(msg, e);
      // ignore
    }
  }
}
