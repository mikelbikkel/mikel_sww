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
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Database environment for pooled connections.<br>
 */
class DBEnvPooled extends DBEnv {

  private DataSource m_ds = null;

  /**
   * Creates a new database environment for pooled connections.
   * 
   * @param dsname
   *          JNDI data source name
   * @throws DBException
   *           if cannot connect to the data source
   */
  public DBEnvPooled(String dsname) throws DBException {
    try {
      Context initCtx = new InitialContext();
      Context envCtx = (Context) initCtx.lookup("java:comp/env");

      m_ds = (DataSource) envCtx.lookup(dsname);
    } catch (NamingException e) {
      m_ds = null;
      String msg = "envPooled: " + e.getMessage();
      DBException se = new DBException(msg, e);
      throw se;
    }
  }

  /**
   * Gets a database connection from the pool.
   * 
   * @return a database connection.
   * @throws SQLException
   */
  @Override
  protected synchronized Connection getDBConnection() throws SQLException {
    Connection con = m_ds.getConnection();
    return con;
  }

  /**
   * Returns a database connection to the pool.
   * 
   * @param con
   *          a database connection.
   */
  @Override
  protected synchronized void disconnect(Connection con) {
    if (null == con) {
      return;
    }
    try {
      // Close connection to return it to the pool.
      con.close();
    } catch (SQLException e) {
      // ignore
    }
  }

  @Override
  public synchronized void destroy() {
  }

}
