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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Alias functions from the RouteService interface.
 * 
 */
public class TestEEL extends TestBase {

  @Before
  public void setUp() throws Exception {
    initService();
  }

  @After
  public void tearDown() throws Exception {
    stopService();
  }

  @Test
  public void testGetStationEELs() throws RSException {
    List<String> eels = null;
    eels = m_rs.getStationEELs("ROTTERDAM");
    assertNotNull(eels);
    assertFalse(eels.isEmpty());
    for (String s : eels) {
      assertNotNull(s);
      System.out.println(s);
    }
  }

  @Test
  public void testGetStations() throws RSException {
    List<Station> stats = null;
    stats = m_rs.getStations("ROTTERDAM", "S");
    assertNotNull(stats);
    assertFalse(stats.isEmpty());
    for (Station s : stats) {
      assertNotNull(s);
      System.out.println(s);
    }
  }

  @Test
  public void testGetLandmarkEELs() throws RSException {
    List<String> eels = null;
    eels = m_rs.getLandmarkEELs("PARIS");
    assertNotNull(eels);
    assertFalse(eels.isEmpty());
    for (String s : eels) {
      assertNotNull(s);
      System.out.println(s);
    }
  }

  @Test
  public void testGetLandmarks() throws RSException {
    List<Landmark> lms = null;
    lms = m_rs.getLandmarks("PARIS", "E");
    assertNotNull(lms);
    assertFalse(lms.isEmpty());
    for (Landmark lm : lms) {
      assertNotNull(lm);
      System.out.println(lm);
    }
  }

  @Test
  public void testGetLandmarks2() throws RSException {
    List<Landmark> lms = null;
    lms = m_rs.getLandmarks("PARIS", "T");
    assertNotNull(lms);
    assertFalse(lms.isEmpty());
    for (Landmark lm : lms) {
      assertNotNull(lm);
      System.out.println(lm);
    }
  }

  @Test
  public void testGetEELsForLocations() throws RSException {
    List<String> eels = null;
    eels = m_rs.getEELsForLocations("ROTTERDAM");
    assertNotNull(eels);
    assertFalse(eels.isEmpty());
    for (String s : eels) {
      assertNotNull(s);
      System.out.println(s);
    }
  }

  @Test
  public void testGetLocationsWithEEL() throws RSException {
    List<ILocation> locs = null;
    locs = m_rs.getLocationsWithEEL("ROTTERDAM", "E");
    assertNotNull(locs);
    assertFalse(locs.isEmpty());
    for (ILocation loc : locs) {
      assertNotNull(loc);
      System.out.println(loc);
    }
  }

}
