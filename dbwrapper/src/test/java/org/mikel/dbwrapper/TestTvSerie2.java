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
 *
 */
package org.mikel.dbwrapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestTvSerie2 {

  private DBEnv m_env = null;

  private Properties readConnectInfo() throws DBException {
    final String CONFIG_FILE = "/dbconn.properties";
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

  @Before
  public void setUp() throws DBException {
    Properties props = readConnectInfo();
    DBEnvFactory fac = new DBEnvFactory();
    m_env = fac.createOwned(props);
  }

  @Test
  public void testSerie2() throws SQLException, DBException {
    m_env.execStmt("delete from tv_episode_2");
    m_env.execStmt("delete from tv_serie_2");

    TvSerie2 tvs = new TvSerie2();
    tvs.setName("Suits");
    tvs.setDescription("New York lawyers.. sharper than sharks.");
    tvs.setProducer("Suit Supply Company, inc.");
    m_env.insertEntryReturnKey(tvs);

    String qry = "select id s_id, name s_name, description s_description, producer s_producer from tv_serie_2";
    List<TvSerie2> series = new ArrayList<>();
    m_env.getItems(TvSerie2.class, series, qry);
    assertEquals(1, series.size());
    TvSerie2 tvdb = series.get(0);
    assertEquals(tvs.getId(), tvdb.getId());
    assertEquals(tvs.getName(), tvdb.getName());
    assertEquals(tvs.getDescription(), tvdb.getDescription());
    assertEquals(tvs.getProducer(), tvdb.getProducer());

    tvs.setProducer("Netflix");
    m_env.updateEntry(tvs);
    series.clear();
    m_env.getItems(TvSerie2.class, series, qry);
    assertEquals(1, series.size());
    tvdb = series.get(0);
    assertEquals(tvs.getId(), tvdb.getId());
    assertEquals(tvs.getName(), tvdb.getName());
    assertEquals(tvs.getDescription(), tvdb.getDescription());
    assertEquals(tvs.getProducer(), tvdb.getProducer());

    m_env.deleteEntry(tvs);
    series.clear();
    m_env.getItems(TvSerie2.class, series, qry);
    assertTrue(series.isEmpty());

    m_env.execStmt("delete from tv_episode_2");
    m_env.execStmt("delete from tv_serie_2");
  }

  @After
  public void tearDown() {
    if (null != m_env) {
      m_env.destroy();
      m_env = null;
    }
  }
}
