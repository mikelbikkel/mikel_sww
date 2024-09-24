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
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Alias functions from the RouteService interface.
 * 
 */
public class TestLandmark extends TestBase {

  @Before
  public void setUp() throws Exception {
    initService();
  }

  @After
  public void tearDown() throws Exception {
    stopService();
  }

  @Test
  public void testGetLandmarks() throws RSException {
    System.out.println("testGetLandmarks [PARIS, T]: start");
    List<Landmark> marks = null;
    marks = m_rs.getLandmarks("PARIS", "T");
    assertFalse(null == marks);
    assertFalse(marks.isEmpty());
    String data;
    for (Landmark c : marks) {
      data = c.getCode();
      assertNotNull(data);
      data = c.getName();
      assertNotNull(data);
      System.out.println(data);
    }
    System.out.println("testGetLandmarks: end");
  }

  @Test
  public void testGetLandmarksInCity() throws RSException {
    System.out.println("testGetLandmarksInCity [PARIS]: start");
    List<Landmark> marks = null;
    marks = m_rs.getLandmarksInCity("PARIS");
    assertFalse(null == marks);
    assertFalse(marks.isEmpty());
    String data;
    for (Landmark c : marks) {
      data = c.getCode();
      assertNotNull(data);
      data = c.getName();
      assertNotNull(data);
      System.out.println(data);
    }
    System.out.println("testGetLandmarksInCity: end");
  }

  @Test
  public void testFindLandmark() throws RSException {
    Landmark c = null;
    c = m_rs.findLandmark("PRSTOUR_EIFFEL");
    assertNotNull(c);
    String data;
    data = c.getCode();
    assertNotNull(data);
    data = c.getName();
    assertNotNull(data);
  }

  @Test
  public void testFindLandmarkLocation() throws RSException {
    ILocation c = null;
    c = m_rs.findLocation("PRSTOUR_EIFFEL");
    assertNotNull(c);
    String data;
    data = c.getCode();
    assertNotNull(data);
    data = c.getName();
    assertNotNull(data);
  }

  @Test
  public void testFindLandmarkErr() throws RSException {
    Landmark c = null;
    c = m_rs.findLandmark("PRSPRUTTEL");
    assertNull(c);
  }

  @Test
  public void testFindLandmarkNull() throws RSException {
    Landmark c = null;
    c = m_rs.findLandmark(null);
    assertNull(c);
  }

  @Test
  public void testFindLandmarkEmpty() throws RSException {
    Landmark c = null;
    c = m_rs.findLandmark("");
    assertNull(c);
  }

  @Test
  public void testGetLandmarkEELs() throws RSException {
    List<String> marks = null;
    marks = m_rs.getLandmarkEELs("PARIS");
    assertFalse(null == marks);
    assertTrue(marks.contains("T"));
  }

  @Test
  public void testFindLandmarkNoGPS() throws RSException {
    Landmark c = null;
    c = m_rs.findLandmark("908LM01");
    assertNotNull(c);
    String data;
    data = c.getCode();
    assertNotNull(data);
    data = c.getName();
    assertNotNull(data);
    GPSInfo gi = null;
    gi = c.getGPSInfo();
    assertNull(gi);
  }

  @Test
  public void testFindLandmarkGPS() throws RSException {
    Landmark c = null;
    c = m_rs.findLandmark("PRSTOUR_EIFFEL");
    assertNotNull(c);
    String data;
    data = c.getCode();
    assertNotNull(data);
    data = c.getName();
    assertNotNull(data);
    GPSInfo gi = null;
    gi = c.getGPSInfo();
    assertNotNull(gi);
    double lat = gi.getLatitude();
    double lon = gi.getLongitude();
    assertEquals(48.8583, lat, 0.001);
    assertEquals(2.2945, lon, 0.001);
    double dist = gi.getDistance(lat, lon);
    assertEquals(0.0, dist, 0.001);
  }

  @Test
  public void testFindLandmarkUnicode() throws RSException {
    Landmark c = null;
    c = m_rs.findLandmark("PRSSACRE_COEUR_DE_MONTMARTRE");
    assertNotNull(c);
    String data;
    data = c.getCode();
    assertNotNull(data);
    data = c.getName();
    assertNotNull(data);
    System.out.println(data);
  }

  @Test
  public void testLandmarkLocalname() throws RSException {
    Landmark c = null;
    c = m_rs.findLandmark("RDMEUROMAST");
    assertNotNull(c);
    // String data;
    // data = c.getLocalName();
    // assertNotNull(data);
    // assertEquals("mikel landmark", data);
  }

}
