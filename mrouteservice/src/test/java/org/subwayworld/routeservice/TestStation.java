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
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Station functions from the RouteService interface.
 * 
 */
public class TestStation extends TestBase {

  @Before
  public void setUp() throws Exception {
    initService();
  }

  @After
  public void tearDown() throws Exception {
    stopService();
  }

  @Test
  public void testGetStations() throws RSException {
    List<Station> stats = null;
    stats = m_rs.getStations("ROTTERDAM", "C");
    assertNotNull(stats);
    assertFalse(stats.isEmpty());
    String data;
    for (Station c : stats) {
      data = c.getCode();
      assertNotNull(data);
      data = c.getName();
      assertNotNull(data);
      System.out.println(c);
    }
  }

  @Test
  public void testFindStation() throws RSException {
    Station s = null;
    s = m_rs.findStation("AMSAMSTEL");
    assertNotNull(s);
    String data;
    data = s.getCode();
    assertNotNull(data);
    data = s.getName();
    assertNotNull(data);
  }

  @Test
  public void testFindStationLocation() throws RSException {
    ILocation s = null;
    s = m_rs.findLocation("AMSAMSTEL");
    assertNotNull(s);
    String data;
    data = s.getCode();
    assertNotNull(data);
    data = s.getName();
    assertNotNull(data);
  }

  @Test
  public void testFindStationNoGPS() throws RSException {
    Station s = null;
    s = m_rs.findStation("900F");
    assertNotNull(s);
    String data;
    data = s.getCode();
    assertNotNull(data);
    data = s.getName();
    assertNotNull(data);
    GPSInfo gi = s.getGPSInfo();
    assertNull(gi);
  }

  @Test
  public void testFindStationGPS() throws RSException {
    Station s = null;
    s = m_rs.findStation("RDMCOOLHAVEN");
    assertNotNull(s);
    String data;
    data = s.getCode();
    assertNotNull(data);
    data = s.getName();
    assertNotNull(data);
    GPSInfo gi = null;
    gi = s.getGPSInfo();
    assertNotNull(gi);
    double lat = gi.getLatitude();
    double lon = gi.getLongitude();
    assertEquals(51.909722, lat, 0.001);
    assertEquals(4.458056, lon, 0.001);
    double dist = gi.getDistance(lat, lon);
    assertEquals(0.0, dist, 0.001);
  }

  @Test
  public void testStationLocalname() throws RSException {
    Station c = null;
    c = m_rs.findStation("RDMBLAAK");
    assertNotNull(c);
    // String data;
    // data = c.getLocalName();
    // assertNotNull(data);
    // assertEquals("mikel station", data);
  }

  @Test
  public void testFindStationMatch() throws RSException {
    List<ILocation> lst = null;
    lst = m_rs.getFilteredLocations("ROTTERDAM", "la");
    for (ILocation loc : lst) {
      assertNotNull(loc);
      System.out.println("Filter la: " + loc.getName() + " [" + loc.getCode()
          + "]");
    }
  }

  @Test
  public void testFindNetworkStation() throws RSException {
    ILocation s = null;
    s = m_rs.findLocation("OSLTOYEN_X__X_1");
    assertNotNull(s);
    String data;
    data = s.getCode();
    assertNotNull(data);
    data = s.getName();
    assertNotNull(data);
  }

  @Test
  public void testFindComplexStation() throws RSException {
    ILocation s = null;
    s = m_rs.findLocation("OSLTOYEN_X__X_2");
    assertNotNull(s);
    String data;
    data = s.getCode();
    assertNotNull(data);
    data = s.getName();
    assertNotNull(data);
  }

  @Test
  public void testFindRegularStation() throws RSException {
    ILocation s = null;
    s = m_rs.findLocation("OSLBERG");
    assertNotNull(s);
    String data;
    data = s.getCode();
    assertNotNull(data);
    data = s.getName();
    assertNotNull(data);
  }
}
