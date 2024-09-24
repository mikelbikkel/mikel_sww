/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.subwayworld.routeservice;

import java.sql.Connection;

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
public interface RSEnv {

  /**
   * Lifetime method. Same as servlet.
   */
  public void destroy();

  /**
   * @return
   * @throws IllegalStateException
   */
  public Connection getDBConnection() throws RSConnectionException ;

  /**
   * @param con
   */
  public void disconnect(Connection con);
}
