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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.subwayworld.routeservice.calculation.CalculationGateway;

/**
 * 2 apr 2010
 * 
 */
public class TestBase {

  protected RSEnv m_env;

  protected IRouteService m_rs;

  protected CalculationGateway m_calc;

  /**
   * Database connection properties.
   */
  private Properties m_props;

  /**
   * 
   */
  public TestBase() {
    super();
  }

  private boolean getProperties() {
    m_props = new Properties();
    InputStream in = null;
    in = getClass().getResourceAsStream("/dbconn.properties");
    if (null == in) {
      return false;
    }
    try {
      m_props.load(in);

      in.close();
    } catch (IOException e) {
      return false;
    }
    return true;
  }

  protected void initService() throws RSConnectionException {
    boolean res;
    res = getProperties();
    if (!res) {
      throw new RSConnectionException("load properties error");
    }
    String dbdriver = m_props.getProperty("db.driver");
    if (null == dbdriver) {
      throw new RSConnectionException("db.driver error");
    }
    String dburl = m_props.getProperty("db.url");
    if (null == dburl) {
      throw new RSConnectionException("db.url error");
    }
    String dbuser = m_props.getProperty("db.user");
    if (null == dbuser) {
      throw new RSConnectionException("db.user error");
    }
    String dbpassword = m_props.getProperty("db.password");
    if (null == dbpassword) {
      throw new RSConnectionException("db.password error");
    }
    m_env = new RSEnvOwned(dbdriver, dburl, dbuser, dbpassword);
    m_rs = RouteServiceFactory.getService(m_env);

    m_calc = new CalculationGateway();
    m_calc.setEnv(m_env);
  }

  protected void stopService() {
    m_calc = null;
    m_rs = null;
    if (null != m_env) {
      m_env.destroy();
    }
  }
}
