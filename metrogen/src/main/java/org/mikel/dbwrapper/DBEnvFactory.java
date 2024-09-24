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

import java.util.Properties;

/**
 * Factory to create DBEnv instances.
 * 
 */
public class DBEnvFactory {

  /**
   * Creates an owned database environment.
   * 
   * @param dbinfo
   *          information to make a database connection.
   * @throws DBException
   *           when cannot create the database environment.
   */
  public DBEnv createOwned(Properties dbinfo) throws DBException {
    String dbdriver = getProp(dbinfo, "driver");
    String dburl = getProp(dbinfo, "url");
    String uname = getProp(dbinfo, "username");
    String pwd = getProp(dbinfo, "password");

    // false = no autocommit. true = autocommit
    return new DBEnvOwned(dbdriver, dburl, uname, pwd, true);
  }

  /**
   * Creates a pooled database environment.
   * 
   * @param dsname
   *          the datasource name.
   * @throws DBException
   *           when cannot create the database environment.
   */
  public DBEnv createPooled(String dsname) throws DBException {
    return new DBEnvPooled(dsname);
  }

  private String getProp(Properties p, String name) throws DBException {
    String value = p.getProperty(name);
    if (null == value) {
      throw new DBException("cannot find property: " + name);
    }
    return value;
  }
}
