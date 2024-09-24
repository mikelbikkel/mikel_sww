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
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

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
class RSEnvPooled implements RSEnv {

  private DataSource m_ds = null;

  public RSEnvPooled(String dsname) throws RSConnectionException {
    try {
      Context initCtx = new InitialContext();
      Context envCtx = (Context) initCtx.lookup("java:comp/env");

      m_ds = (DataSource) envCtx.lookup(dsname);
    } catch (NamingException e) {
      m_ds = null;
      String msg = "envPooled: " + e.getMessage();
      RSConnectionException se = new RSConnectionException(msg, e);
      throw se;
    }
  }

  /**
   * Lifetime method. Same as servlet.
   */
  @Override
  public synchronized void destroy() {
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
    Connection con = m_ds.getConnection();
    return con;
  }

  /**
   * Returns the database connection to the connection pool.
   * 
   * @param con
   *          the database connection.
   */
  @Override
  public synchronized void disconnect(Connection con) {
    try {
      // Close connection to return it to the pool.
      con.close();
    } catch (SQLException e) {
      // ignore
    }
  }
}
