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

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestSuppliedPK {

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
  public void testSimplePK() throws SQLException, DBException {
    m_env.execStmt("delete from test_city");

    TestCity tc = new TestCity();
    tc.setName("Rotterdam");
    tc.setDescription("Havenstad aan de Maas");
    m_env.insertEntry(tc);

    String qry = "select name, description from test_city";
    List<TestCity> items = new ArrayList<>();
    m_env.getItems(TestCity.class, items, qry);
    assertEquals(1, items.size());
    tc = items.get(0);
    assertEquals("Rotterdam", tc.getName());
    assertEquals("Havenstad aan de Maas", tc.getDescription());

    tc.setDescription("Testing...");
    m_env.updateEntry(tc);
    items.clear();
    m_env.getItems(TestCity.class, items, qry);
    assertEquals(1, items.size());
    tc = items.get(0);
    assertEquals("Rotterdam", tc.getName());
    assertEquals("Testing...", tc.getDescription());

    m_env.deleteEntry(tc);
    items.clear();
    m_env.getItems(TestCity.class, items, qry);
    assertTrue(items.isEmpty());
  }

  @Test
  public void testCompositePK() throws SQLException, DBException {
    m_env.execStmt("delete from tv_episode");
    m_env.execStmt("delete from tv_serie");

    TvSerie tvs = new TvSerie();
    tvs.setName("Suits");
    tvs.setDescription("New York lawyers.. sharper than sharks.");
    tvs.setProducer("Suit Supply Company, inc.");
    m_env.insertEntry(tvs);

    TvEpisode tve = new TvEpisode();
    tve.setNameSerie("Suits");
    tve.setSeason(1);
    tve.setEpisode(1);
    tve.setName("An unexpected meeting.");
    tve.setDescription("A blazing start. Mike meets Harvey!");
    m_env.insertEntry(tve);

    String qry = "select * from tv_episode_all";
    List<TvEpisode> episodes = new ArrayList<>();
    m_env.getItems(TvEpisode.class, episodes, qry);
    assertEquals(1, episodes.size());
    TvEpisode epdb = episodes.get(0);
    assertEquals(tve.getNameSerie(), epdb.getNameSerie());
    assertEquals(tve.getSeason(), epdb.getSeason());
    assertEquals(tve.getEpisode(), epdb.getEpisode());
    assertEquals(tve.getName(), epdb.getName());
    assertEquals(tve.getDescription(), epdb.getDescription());

    tve.setDescription("Testing...");
    m_env.updateEntry(tve);
    episodes.clear();
    m_env.getItems(TvEpisode.class, episodes, qry);
    assertEquals(1, episodes.size());
    epdb = episodes.get(0);
    assertEquals(tve.getNameSerie(), epdb.getNameSerie());
    assertEquals(tve.getSeason(), epdb.getSeason());
    assertEquals(tve.getEpisode(), epdb.getEpisode());
    assertEquals(tve.getName(), epdb.getName());
    assertEquals(tve.getDescription(), epdb.getDescription());

    m_env.deleteEntry(tve);
    episodes.clear();
    qry = "select * from tv_episode_all";
    m_env.getItems(TvEpisode.class, episodes, qry);
    assertTrue(episodes.isEmpty());
    
    m_env.deleteEntry(tvs);
    List<TvSerie> series = new ArrayList<>();
    qry = "select * from tv_serie";
    m_env.getItems(TvSerie.class, series, qry);
    assertTrue(series.isEmpty());

    m_env.execStmt("delete from tv_episode");
    m_env.execStmt("delete from tv_serie");
  }

  @After
  public void tearDown() {
    if (null != m_env) {
      m_env.destroy();
      m_env = null;
    }
  }
}
