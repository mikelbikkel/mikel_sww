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
/**
 * 
 */
package org.subwayworld.metrogen.output;

import org.subwayworld.metrogen.MessageWriter;
import org.subwayworld.metrogen.MetroException;
import org.subwayworld.metrogen.MetroSettings;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.mikel.dbwrapper.DBEnv;
import org.mikel.dbwrapper.DBEnvFactory;
import org.mikel.dbwrapper.DBException;
import org.subwayworld.metrogen.network.MetroNetwork;

/**
 * Generates output.
 */
public class OutputManager {

  /**
   * Generate output using the data and output options in mn.
   * 
   * @param mn
   *          the data and output options
   */
  public void generateOutput(MetroNetwork mn, MetroSettings ms,
      MessageWriter messages) throws MetroException {

    if (ms.genText) {
      TextPrinter tp = new TextPrinter();
      tp.print(mn, ms.encoding);
    }

    if (ms.dbDirect) {
      DBUpdate dbu = new DBUpdate();
      try {
        Properties dbinfo = readConnectInfo();
        DBEnvFactory fac = new DBEnvFactory();
        DBEnv env = fac.createOwned(dbinfo);
        dbu.print(mn, env, ms.encoding);
      } catch (DBException e) {
        throw new MetroException(e);
      }
    } else {
      SQLPrinter sp = new SQLPrinter(messages);
      sp.print(mn, ms.encoding, ms.m_dbtype);
    }
  }

  private Properties readConnectInfo() throws DBException {
    final String CONFIG_FILE = "/res/config/dbconn.properties";
    Properties p = new Properties();
    try (InputStream in = this.getClass().getResourceAsStream(CONFIG_FILE)) {
      if (null == in) {
        throw new DBException("cannot find: " + CONFIG_FILE);
      }
      p.load(in);
    } catch (IOException | IllegalArgumentException e) {
      throw new DBException("cannot read: " + CONFIG_FILE, e);
    }
    return p;
  }
}
