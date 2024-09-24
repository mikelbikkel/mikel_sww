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
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestTvEpisode2 {

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
  public void testEpisode2() throws SQLException, DBException {
    m_env.execStmt("delete from tv_episode_2");
    m_env.execStmt("delete from tv_serie_2");

    TvSerie2 tvs = new TvSerie2();
    tvs.setName("Suits");
    tvs.setDescription("New York lawyers.. sharper than sharks.");
    tvs.setProducer("Suit Supply Company, inc.");
    m_env.insertEntryReturnKey(tvs);

    TvEpisode2 ep = new TvEpisode2();
    ep.setSerie(tvs);
    ep.setSeason(1);
    ep.setEpisode(1);
    ep.setName("Harvey meets Mike!");
    ep.setDescription("A blazing start of this serie");
    m_env.insertEntryReturnKey(ep);

    String qry = "select * from tv_episode_all_2";
    List<TvEpisode2> episodes = new ArrayList<>();
    m_env.getItems(TvEpisode2.class, episodes, qry);
    assertEquals(1, episodes.size());
    TvEpisode2 tvep = episodes.get(0);
    assertEquals(ep.getId(), tvep.getId());
    assertEquals(ep.getSerie(), tvep.getSerie());
    assertEquals(ep.getSerie().getId(), tvep.getSerie().getId());
    assertEquals(ep.getSeason(), tvep.getSeason());
    assertEquals(ep.getEpisode(), tvep.getEpisode());
    assertEquals(ep.getName(), tvep.getName());
    assertEquals(ep.getDescription(), tvep.getDescription());

    ep.setDescription("A sizzling start");
    m_env.updateEntry(ep);
    episodes.clear();
    m_env.getItems(TvEpisode2.class, episodes, qry);
    assertEquals(1, episodes.size());
    tvep = episodes.get(0);
    assertEquals(ep.getId(), tvep.getId());
    assertEquals(ep.getSerie(), tvep.getSerie());
    assertEquals(ep.getSerie().getId(), tvep.getSerie().getId());
    assertEquals(ep.getSeason(), tvep.getSeason());
    assertEquals(ep.getEpisode(), tvep.getEpisode());
    assertEquals(ep.getName(), tvep.getName());
    assertEquals(ep.getDescription(), tvep.getDescription());

    TvEpisode2 ep2 = new TvEpisode2();
    ep2.setSerie(tvs);
    ep2.setSeason(1);
    ep2.setEpisode(2);
    ep2.setName("Rites of spring");
    ep2.setDescription("Mike and Harvey working together on their first case");
    m_env.insertEntryReturnKey(ep2);

    episodes.clear();
    m_env.getItems(TvEpisode2.class, episodes, qry);
    assertEquals(2, episodes.size());
    TvEpisode2 res = episodes.get(0);
    TvSerie2 p1 = null;
    TvSerie2 p2 = null;
    if (res.getId() == tvep.getId()) {
      p1 = res.getSerie();
      assertEquals(res.getId(), tvep.getId());
      assertEquals(res.getSerie(), tvep.getSerie());
      assertEquals(res.getSerie().getId(), tvep.getSerie().getId());
      assertEquals(res.getSeason(), tvep.getSeason());
      assertEquals(res.getEpisode(), tvep.getEpisode());
      assertEquals(res.getName(), tvep.getName());
      assertEquals(res.getDescription(), tvep.getDescription());
    } else if (res.getId() == ep2.getId()) {
      p2 = res.getSerie();
      assertEquals(res.getId(), ep2.getId());
      assertEquals(res.getSerie(), ep2.getSerie());
      assertEquals(res.getSerie().getId(), ep2.getSerie().getId());
      assertEquals(res.getSeason(), ep2.getSeason());
      assertEquals(res.getEpisode(), ep2.getEpisode());
      assertEquals(res.getName(), ep2.getName());
      assertEquals(res.getDescription(), ep2.getDescription());
    } else {
      fail("Unknown episode");
    }
    res = episodes.get(1);
    if (res.getId() == tvep.getId()) {
      p1 = res.getSerie();
      assertEquals(res.getId(), tvep.getId());
      assertEquals(res.getSerie(), tvep.getSerie());
      assertEquals(res.getSerie().getId(), tvep.getSerie().getId());
      assertEquals(res.getSeason(), tvep.getSeason());
      assertEquals(res.getEpisode(), tvep.getEpisode());
      assertEquals(res.getName(), tvep.getName());
      assertEquals(res.getDescription(), tvep.getDescription());
    } else if (res.getId() == ep2.getId()) {
      p2 = res.getSerie();
      assertEquals(res.getId(), ep2.getId());
      assertEquals(res.getSerie(), ep2.getSerie());
      assertEquals(res.getSerie().getId(), ep2.getSerie().getId());
      assertEquals(res.getSeason(), ep2.getSeason());
      assertEquals(res.getEpisode(), ep2.getEpisode());
      assertEquals(res.getName(), ep2.getName());
      assertEquals(res.getDescription(), ep2.getDescription());
    } else {
      fail("Unknown episode");
    }
    assertTrue(p1 == p2);
    assertEquals(p1, p2);

    m_env.deleteEntry(ep);
    m_env.deleteEntry(ep2);
    episodes.clear();
    m_env.getItems(TvEpisode2.class, episodes, qry);
    assertTrue(episodes.isEmpty());

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
