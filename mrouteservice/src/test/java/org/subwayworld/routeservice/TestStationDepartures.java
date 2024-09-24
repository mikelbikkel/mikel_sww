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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Station functions from the RouteService interface.
 * 
 */
public class TestStationDepartures extends TestBase {

  @Before
  public void setUp() throws Exception {
    initService();
  }

  @After
  public void tearDown() throws Exception {
    stopService();
  }

  @Test
  public void testGetDeparturesBlaak() throws RSException {
    Set<String> soll = new HashSet<String>();
    soll.add("Caland line A" + ":" + "Binnenhof");
    soll.add("Caland line A" + ":" + "Schiedam Centrum");
    soll.add("Caland line B" + ":" + "Nesselande");
    soll.add("Caland line B" + ":" + "Schiedam Centrum");
    soll.add("Caland line C" + ":" + "De Terp");
    soll.add("Caland line C" + ":" + "De Akkers");

    Set<String> ist = new HashSet<String>();

    List<SegmentDirection> stats = null;
    stats = m_rs.getStationDepartures("RDMBLAAK");
    assertNotNull(stats);
    assertFalse(stats.isEmpty());
    for (SegmentDirection sd : stats) {
      assertNotNull(sd);
      System.out.println(sd);
      ist.add(sd.getLine() + ":" + sd.getEndpoint());
    }
    assertEquals(soll, ist);
  }

  @Test
  public void testGetDeparturesOsloComplex() throws RSException {
    Set<String> soll = new HashSet<String>();
    soll.add("Caland line A" + ":" + "Binnenhof");
    soll.add("Caland line A" + ":" + "Schiedam Centrum");
    soll.add("Caland line B" + ":" + "Nesselande");
    soll.add("Caland line B" + ":" + "Schiedam Centrum");
    soll.add("Caland line C" + ":" + "De Terp");
    soll.add("Caland line C" + ":" + "De Akkers");

    Set<String> ist = new HashSet<String>();

    List<SegmentDirection> stats = null;
    stats = m_rs.getStationDepartures("OSLMAJORSTUEN_X__X_2");
    assertNotNull(stats);
    assertFalse(stats.isEmpty());
    for (SegmentDirection sd : stats) {
      assertNotNull(sd);
      System.out.println(sd);
      ist.add(sd.getLine() + ":" + sd.getEndpoint());
    }
    // assertEquals(soll, ist);
  }

  @Test
  public void testGetDeparturesBerlin() throws RSException {
    Set<String> soll = new HashSet<String>();
    soll.add("S1" + ":" + "Oranienburg");
    soll.add("S1" + ":" + "Wannsee");
    soll.add("S1" + ":" + "Wannsee");
    soll.add("S1" + ":" + "Frohnau");
    soll.add("S1 express" + ":" + "Potsdamer Platz");
    soll.add("S1 express" + ":" + "Zehlendorf");

    Set<String> ist = new HashSet<String>();

    List<SegmentDirection> stats = null;
    // This station is part of 2 trajects. Check for duplicates...
    stats = m_rs.getStationDepartures("BERJULIUS_LEBER_BRUCKE");
    assertNotNull(stats);
    assertFalse(stats.isEmpty());
    for (SegmentDirection sd : stats) {
      assertNotNull(sd);
      System.out.println(sd);
      ist.add(sd.getLine() + ":" + sd.getEndpoint());
    }
    assertEquals(soll, ist);
  }

}
