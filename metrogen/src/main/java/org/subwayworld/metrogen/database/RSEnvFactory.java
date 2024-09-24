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

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;

/**
 * Factory to create RSEnv instances.
 * 
 */
public class RSEnvFactory {
  /**
   * Creates a new owned database environment.
   * <p>
   * Connection info properties:
   * <ul>
   * <li>dbtype</li>
   * <li>hostname</li>
   * <li>database</li>
   * <li>username</li>
   * <li>password</li>
   * </ul>
   * <p>
   * 
   * @param connectinfo
   *          connection info
   * @throws RSConnectionException
   *           when cannot create the database environment.
   */
  public RSEnv createOwned(Properties connectinfo) throws RSConnectionException {
    Properties dbinfo = getDBInfo("dbconn.properties");

    String dbtype = getProp(connectinfo, "dbtype");
    String dbdriver = getProp(dbinfo, dbtype + ".driver");
    String inputURL = getProp(dbinfo, dbtype + ".url");
    String dburl;
    if ("mysql".equals(dbtype)) {
      dburl = mysqlConnect(connectinfo, inputURL);
    } else {
      throw new RSConnectionException("dbtype unkown: " + dbtype);
    }

    // false = disable autocommit mode
    return new RSEnvOwned(dbdriver, dburl, null, null, false);
  }

  /**
   * Creates a new pooled database environment.
   * 
   * @throws RSConnectionException
   *           when cannot create the database environment.
   */
  public RSEnv createPooled(String resname) throws RSConnectionException {
    throw new RSConnectionException("pooled connections not implemented.");
    // return new RSEnvPooled(resname);
  }

  private Properties getDBInfo(String pfile) throws RSConnectionException {
    Properties p = new Properties();
    try (InputStream in = this.getClass().getResourceAsStream(pfile)) {
      p.load(in);
    } catch (IOException | IllegalArgumentException e) {
      throw new RSConnectionException("cannot read: " + pfile, e);
    }
    return p;
  }

  private String mysqlConnect(Properties connectinfo, String inputURL)
      throws RSConnectionException {
    String hostname = getProp(connectinfo, "hostname");
    String database = getProp(connectinfo, "database");
    String password = getProp(connectinfo, "password");
    String finalURL = MessageFormat.format(inputURL, hostname, database,
        password);
    return finalURL;
  }

  private String getProp(Properties p, String name)
      throws RSConnectionException {
    String value = p.getProperty(name);
    if (null == value) {
      throw new RSConnectionException("cannot find property: " + name);
    }
    return value;
  }
}
