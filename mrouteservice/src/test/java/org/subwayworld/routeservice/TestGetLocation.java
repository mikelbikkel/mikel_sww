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
 * Test getLocation function from the RouteService interface.
 * 
 */
public class TestGetLocation extends TestBase {

  @Before
  public void setUp() throws Exception {
    initService();
  }

  @After
  public void tearDown() throws Exception {
    stopService();
  }

  @Test
  public void testGetWrongCode() throws RSException {
    ILocation loc = null;
    loc = m_rs.getLocation("XYZ_PRUTTEL");
    assertNull(loc);
  }

  @Test(expected = NullPointerException.class)
  public void testGetNullCode() throws RSException {
    m_rs.getLocation(null);
  }

  @Test
  public void testGetStation() throws RSException {
    ILocation loc = null;
    loc = m_rs.getLocation("RDMCOOLHAVEN");
    assertNotNull(loc);

    String data;
    data = loc.getCode();
    assertNotNull(data);
    assertEquals(data, "RDMCOOLHAVEN");

    data = loc.getName();
    assertNotNull(data);
    assertEquals(data, "Coolhaven");

    GPSInfo gi = null;
    gi = loc.getGPSInfo();
    assertNotNull(gi);
    double lat = gi.getLatitude();
    double lon = gi.getLongitude();
    assertEquals(51.909722, lat, 0.001);
    assertEquals(4.458056, lon, 0.001);
  }

  @Test
  public void testGetLandmark() throws RSException {
    ILocation loc = null;
    loc = m_rs.getLocation("RDMEUROMAST");
    assertNotNull(loc);

    String data;
    data = loc.getCode();
    assertNotNull(data);
    assertEquals(data, "RDMEUROMAST");

    data = loc.getName();
    assertNotNull(data);
    assertEquals(data, "Euromast");

    GPSInfo gi = null;
    gi = loc.getGPSInfo();
    assertNotNull(gi);
    double lat = gi.getLatitude();
    double lon = gi.getLongitude();
    assertEquals(51.9052, lat, 0.001);
    assertEquals(4.46647, lon, 0.001);
  }

  @Test
  public void testGetCity() throws RSException {
    ILocation loc = null;
    loc = m_rs.getLocation("ROTTERDAM");
    assertNotNull(loc);

    String data;
    data = loc.getCode();
    assertNotNull(data);
    assertEquals(data, "ROTTERDAM");

    data = loc.getName();
    assertNotNull(data);
    assertEquals(data, "Rotterdam");

    GPSInfo gi = null;
    gi = loc.getGPSInfo();
    assertNotNull(gi);
    double lat = gi.getLatitude();
    double lon = gi.getLongitude();
    assertEquals(51.921667, lat, 0.001);
    assertEquals(4.481111, lon, 0.001);
  }

  @Test
  public void testCalculateAllDistances() throws RSException {
    List<ILocation> locs = m_rs.testGetLocations();
    double dist;
    double lat;
    double lng;
    for (ILocation fromLoc : locs) {
      if (!fromLoc.getHasGPSInfo()) {
        continue;
      }
      for (ILocation toLoc : locs) {
        if (toLoc.getHasGPSInfo()) {
          lat = toLoc.getGPSInfo().getLatitude();
          lng = toLoc.getGPSInfo().getLongitude();
          dist = fromLoc.getGPSInfo().getDistance(lat, lng);
          assertFalse(Double.isNaN(dist));
          assertFalse(Double.isInfinite(dist));
        }
      } // for toLoc
    } // for fromLoc
  }

  // latitude: -90 south pole. 90 north pole
  // longitude: -180 west of prime meridian. 180 east of meridian.
  // ILocation test = locs.get(5);
  // dist = test.getGPSInfo().getDistance(100, 2);
  // if (Double.isNaN(dist)) {
  // numErrors++;
  // System.out.println("NaN: " + test.getCode());
  // }
  // if (Double.isInfinite(dist)) {
  // numErrors++;
  // System.out.println("Infinite: " + test.getCode());
  // }

}
