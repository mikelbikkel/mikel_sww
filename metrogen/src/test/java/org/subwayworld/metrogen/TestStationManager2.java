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
package org.subwayworld.metrogen;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.subwayworld.metrogen.input.RawStation;
import org.subwayworld.metrogen.network.MetroStation;
import org.subwayworld.metrogen.network.StationType;

import junit.framework.TestCase;

/**
 * Test StationManager2
 *
 */
public class TestStationManager2 extends TestCase {

  private static final String CITY_ID = "DUCK_TOWN";

  @Test
  public void testRawSimple() throws MetroException {
    Set<RawStation> stats = new LinkedHashSet<>();
    RawStation rs = new RawStation("Slinge");
    stats.add(rs);

    Set<List<String>> mnn = new LinkedHashSet<>();

    // TODO: test null params.
    StationManager2 sm2 = new StationManager2(CITY_ID, "RDM", stats, mnn);

    Set<MetroStation> res = sm2.getStations();
    assertEquals(1, res.size());

    int numFound = 0;
    for (MetroStation st : res) {
      if (st.getId().equals("RDMSLINGE")) {
        assertEquals("Slinge", st.getName());
        assertEquals("RDMSLINGE", st.getId());
        assertEquals("RDMSLINGE", st.getNode());
        assertEquals(StationType.REGULAR, st.getType());
        numFound++;
      }
    }
    assertEquals(1, numFound);
  }

  @Test
  public void testRawMNN() throws MetroException {
    String[] statnames = { "Beurs", "Beursplein" };

    Set<RawStation> stats = new LinkedHashSet<>();
    List<String> mnndef = new ArrayList<>();
    for (String s : statnames) {
      RawStation rs = new RawStation(s);
      stats.add(rs);
      mnndef.add(s);
    }

    Set<List<String>> mnn = new LinkedHashSet<>();
    mnn.add(mnndef);

    StationManager2 sm2 = new StationManager2(CITY_ID, "RDM", stats, mnn);

    Set<MetroStation> res = sm2.getStations();
    assertEquals(statnames.length, res.size());

    int[] fnd = { 0, 0 };
    for (MetroStation st : res) {
      if (st.getId().equals("RDMBEURS")) {
        assertEquals("Beurs", st.getName());
        assertEquals("RDMBEURS", st.getId());
        assertEquals("RDMMNN_BEURS", st.getNode());
        assertEquals(StationType.REGULAR, st.getType());
        fnd[0]++;
      } else if (st.getId().equals("RDMBEURSPLEIN")) {
        assertEquals("Beursplein", st.getName());
        assertEquals("RDMBEURSPLEIN", st.getId());
        assertEquals("RDMMNN_BEURS", st.getNode());
        assertEquals(StationType.REGULAR, st.getType());
        fnd[1]++;
      }
    }
    assertEquals(1, fnd[0]);
    assertEquals(1, fnd[1]);
  }

  @Test
  public void testRawComplex() throws MetroException {
    String[] statnames = { "Blaak X::X 1", "Blaak X::X 2", "Blaak X::X 3" };

    Set<RawStation> stats = new LinkedHashSet<>();
    for (String s : statnames) {
      RawStation rs = new RawStation(s);
      stats.add(rs);
    }

    Set<List<String>> mnn = new LinkedHashSet<>();

    StationManager2 sm2 = new StationManager2(CITY_ID, "RDM", stats, mnn);

    Set<MetroStation> res = sm2.getStations();
    assertEquals(statnames.length, res.size());

    int[] fnd = { 0, 0, 0 };
    for (MetroStation st : res) {
      if (st.getId().equals("RDMBLAAK_X__X_1")) {
        assertEquals("Blaak", st.getName());
        assertEquals("RDMBLAAK_X__X_1", st.getId());
        assertEquals("RDMMNN_BLAAK_X__X_1", st.getNode());
        assertEquals(StationType.NETWORK, st.getType());
        fnd[0]++;
      } else if (st.getId().equals("RDMBLAAK_X__X_2")) {
        assertEquals("Blaak", st.getName());
        assertEquals("RDMBLAAK_X__X_2", st.getId());
        assertEquals("RDMMNN_BLAAK_X__X_1", st.getNode());
        assertEquals(StationType.COMPLEX, st.getType());
        fnd[1]++;
      } else if (st.getId().equals("RDMBLAAK_X__X_3")) {
        assertEquals("Blaak", st.getName());
        assertEquals("RDMBLAAK_X__X_3", st.getId());
        assertEquals("RDMMNN_BLAAK_X__X_1", st.getNode());
        assertEquals(StationType.COMPLEX, st.getType());
        fnd[2]++;
      }
    }
    assertEquals(1, fnd[0]);
    assertEquals(1, fnd[1]);
    assertEquals(1, fnd[2]);
  }

  @Test
  public void testRawAll() throws MetroException {
    String[] statnames = { "Slinge", "Beurs", "Beursplein", "Blaak X::X 1",
        "Blaak X::X 2", "Blaak X::X 3" };

    Set<RawStation> stats = new LinkedHashSet<>();
    List<String> mnndef = new ArrayList<>();
    for (String s : statnames) {
      RawStation rs = new RawStation(s);
      stats.add(rs);
      if (s.equals("Beurs") || s.equals("Beursplein"))
        mnndef.add(s);
    }

    Set<List<String>> mnn = new LinkedHashSet<>();
    mnn.add(mnndef);

    StationManager2 sm2 = new StationManager2(CITY_ID, "RDM", stats, mnn);

    Set<MetroStation> res = sm2.getStations();
    assertEquals(statnames.length, res.size());

    int[] fnd = { 0, 0, 0, 0, 0, 0 };
    for (MetroStation st : res) {
      if (st.getId().equals("RDMSLINGE")) {
        assertEquals("Slinge", st.getName());
        assertEquals("RDMSLINGE", st.getId());
        assertEquals("RDMSLINGE", st.getNode());
        assertEquals(StationType.REGULAR, st.getType());
        fnd[0]++;
      } else if (st.getId().equals("RDMBEURS")) {
        assertEquals("Beurs", st.getName());
        assertEquals("RDMBEURS", st.getId());
        assertEquals("RDMMNN_BEURS", st.getNode());
        assertEquals(StationType.REGULAR, st.getType());
        fnd[1]++;
      } else if (st.getId().equals("RDMBEURSPLEIN")) {
        assertEquals("Beursplein", st.getName());
        assertEquals("RDMBEURSPLEIN", st.getId());
        assertEquals("RDMMNN_BEURS", st.getNode());
        assertEquals(StationType.REGULAR, st.getType());
        fnd[2]++;
      } else if (st.getId().equals("RDMBLAAK_X__X_1")) {
        assertEquals("Blaak", st.getName());
        assertEquals("RDMBLAAK_X__X_1", st.getId());
        assertEquals("RDMMNN_BLAAK_X__X_1", st.getNode());
        assertEquals(StationType.NETWORK, st.getType());
        fnd[3]++;
      } else if (st.getId().equals("RDMBLAAK_X__X_2")) {
        assertEquals("Blaak", st.getName());
        assertEquals("RDMBLAAK_X__X_2", st.getId());
        assertEquals("RDMMNN_BLAAK_X__X_1", st.getNode());
        assertEquals(StationType.COMPLEX, st.getType());
        fnd[4]++;
      } else if (st.getId().equals("RDMBLAAK_X__X_3")) {
        assertEquals("Blaak", st.getName());
        assertEquals("RDMBLAAK_X__X_3", st.getId());
        assertEquals("RDMMNN_BLAAK_X__X_1", st.getNode());
        assertEquals(StationType.COMPLEX, st.getType());
        fnd[5]++;
      }
    }
    assertEquals(1, fnd[0]);
    assertEquals(1, fnd[1]);
    assertEquals(1, fnd[2]);
    assertEquals(1, fnd[3]);
    assertEquals(1, fnd[4]);
    assertEquals(1, fnd[5]);
  }

  @Test
  public void testFind() throws MetroException {
    Set<RawStation> stats = new LinkedHashSet<>();
    RawStation rs = new RawStation("Slinge");
    stats.add(rs);

    Set<List<String>> mnn = new LinkedHashSet<>();

    StationManager2 sm2 = new StationManager2(CITY_ID, "RDM", stats, mnn);
    MetroStation ms = sm2.findStation("Slinge");
    assertEquals("Slinge", ms.getName());
  }

  @Test
  public void testFindComplex() throws MetroException {
    Set<RawStation> stats = new LinkedHashSet<>();
    RawStation rs = new RawStation("Slinge X::X 1");
    stats.add(rs);

    Set<List<String>> mnn = new LinkedHashSet<>();

    StationManager2 sm2 = new StationManager2(CITY_ID, "RDM", stats, mnn);
    MetroStation ms = sm2.findStation("Slinge X::X 1");
    assertEquals("Slinge", ms.getName());
    assertEquals("Slinge X::X 1", ms.getRawName());
    assertEquals("RDMMNN_SLINGE_X__X_1", ms.getNode());
  }

  /*
   * Negative testcases.
   */
  @Test
  public void testMNNError() {
    String[] statnames = { "Beurs", "Beursplein" };

    Set<RawStation> stats = new LinkedHashSet<>();
    List<String> mnndef = new ArrayList<>();
    try {
      for (String s : statnames) {
        RawStation rs = new RawStation(s);
        stats.add(rs);
        mnndef.add(s);
      }
      mnndef.add("Baasje");
    } catch (MetroException e) {
      fail("No MetroException");
    }

    Set<List<String>> mnn = new LinkedHashSet<>();
    mnn.add(mnndef);

    try {
      @SuppressWarnings("unused")
      StationManager2 sm2 = new StationManager2(CITY_ID, "RDM", stats, mnn);
    } catch (MetroException e) {
      assertEquals("Unknown MNN name: Baasje", e.getMessage());
      return;
    }
    fail("No MetroException");
  }

  @Test
  public void testFindComplexError() {
    Set<RawStation> stats = new LinkedHashSet<>();
    RawStation rs = null;
    try {
      rs = new RawStation("Slinge X::X 1");
    } catch (MetroException e) {
      fail("MetroException" + e.getMessage());
    }
    stats.add(rs);

    Set<List<String>> mnn = new LinkedHashSet<>();

    try {
      StationManager2 sm2 = new StationManager2(CITY_ID, "RDM", stats, mnn);
      sm2.findStation("Slinge");
    } catch (MetroException e) {
      assertEquals("Cannot find station: Slinge", e.getMessage());
      return;
    }
    fail("No MetroException");
  }

  @Test
  public void testDifferentNameSameId() throws MetroException {
    Set<RawStation> stats = new LinkedHashSet<>();
    RawStation rs = new RawStation("Yenikapi"); // id = YeniKapi.
    stats.add(rs);
    RawStation rs2 = new RawStation("Üçyüzlü");
    stats.add(rs2);
    RawStation rs3 = new RawStation("Yenikapı"); // id = Yenikapi.
    stats.add(rs3);

    Set<List<String>> mnn = new LinkedHashSet<>();

    try {
      @SuppressWarnings("unused")
      StationManager2 sm2 = new StationManager2(CITY_ID, "IST", stats, mnn);
    } catch (MetroException e) {
      String msg = e.getMessage();
      assertTrue(msg.startsWith("<ISTYENIKAPI> => <Yenikapı>,<Yenikapi>"));
      return;
    }
    fail("No MetroException");
  }
}
