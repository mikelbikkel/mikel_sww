/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.subwayworld.routeservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test City functions from the RouteService interface.
 * 
 */
public class TestCity extends TestBase {

  @Before
  public void setUp() throws Exception {
    initService();
  }

  @After
  public void tearDown() throws Exception {
    stopService();
  }

  @Test
  public void testGetCitiesAfrica() throws RSException {
    List<City> cities = null;
    cities = m_rs.getCities("AFRICA");
    assertFalse(null == cities);
    assertFalse(cities.isEmpty());
    String data;
    List<String> targetNames = new ArrayList<String>();
    List<String> targetCodes = new ArrayList<String>();
    for (City c : cities) {
      data = c.getCode();
      assertFalse(null == data);
      targetCodes.add(data);

      data = c.getName();
      assertFalse(null == data);
      targetNames.add(data);
    }

    assertTrue(2 == targetCodes.size());
    Collections.sort(targetCodes);
    assertEquals("ALGIERS", targetCodes.get(0));
    assertEquals("CAIRO", targetCodes.get(1));

    assertTrue(2 == targetNames.size());
    Collections.sort(targetNames);
    assertEquals("Algiers", targetNames.get(0));
    assertEquals("Cairo", targetNames.get(1));
  }

  @Test
  public void testGetCitiesChile() throws RSException {
    List<City> cities = null;
    cities = m_rs.getCitiesForCountry("CHILE");
    assertNotNull(cities);
    assertFalse(cities.isEmpty());
    assertEquals(2, cities.size());
    String data;
    String[] targetCodes = { "SANTIAGO", "VALPARAISO" };
    int i = 0;
    for (City c : cities) {
      data = c.getCode();
      assertNotNull(data);
      assertEquals(targetCodes[i], data);
      i++;
    }
  }

  @Test
  public void testFindCity() throws RSException {
    City c = null;
    c = m_rs.findCity("AMSTERDAM");
    assertFalse(null == c);
    String data;
    data = c.getCode();
    assertNotNull(data);
    assertEquals("AMSTERDAM", data);

    data = c.getName();
    assertNotNull(data);
    assertEquals("Amsterdam", data);

    Continent con;
    con = c.getContinent();
    assertNotNull(con);
    assertEquals("EUROPE", con.getCode());
    assertEquals("Europe", con.getName());

    Country cntry;
    cntry = c.getCountry();
    assertNotNull(cntry);
    assertEquals("NETHERLANDS", cntry.getCode());
    assertEquals("Netherlands", cntry.getName());
  }

  @Test
  public void testFindCityPublishUpdate() throws RSException {
    City c = null;
    c = m_rs.findCity("BERLIN");
    assertNotNull(c);

    String data;
    data = c.getCode();
    assertNotNull(data);
    assertEquals("BERLIN", data);

    data = c.getName();
    assertNotNull(data);
    assertEquals("Berlin", data);

    Continent con;
    con = c.getContinent();
    assertNotNull(con);
    assertEquals("EUROPE", con.getCode());
    assertEquals("Europe", con.getName());

    Country cntry;
    cntry = c.getCountry();
    assertNotNull(cntry);
    assertEquals("GERMANY", cntry.getCode());
    assertEquals("Germany", cntry.getName());

    // PublishInfoStatus stat = c.getStatus();
    // assertTrue(stat == PublishInfoStatus.INFO_UPDATE);
    //
    // List<PublishMessage> info;
    // info = c.getMessages();
    // assertTrue(info.size() > 0);
    // for (PublishMessage msg : info) {
    // System.out.println(msg);
    // data = msg.getOwnerCode();
    // assertEquals("BERLIN", data);
    //
    // data = msg.getOwnerName();
    // assertEquals("Berlin", data);
    // }
  }

  @Test
  public void testFindCityPublishNew() throws RSException {
    City c = null;
    c = m_rs.findCity("ROTTERDAM");
    assertNotNull(c);

    String data;
    data = c.getCode();
    assertNotNull(data);
    assertEquals("ROTTERDAM", data);

    data = c.getName();
    assertNotNull(data);
    assertEquals("Rotterdam", data);

    Continent con;
    con = c.getContinent();
    assertNotNull(con);
    assertEquals("EUROPE", con.getCode());
    assertEquals("Europe", con.getName());

    Country cntry;
    cntry = c.getCountry();
    assertNotNull(cntry);
    assertEquals("NETHERLANDS", cntry.getCode());
    assertEquals("Netherlands", cntry.getName());

    // PublishInfoStatus stat = c.getStatus();
    // assertTrue(stat == PublishInfoStatus.INFO_NEW);
    //
    // List<PublishMessage> info;
    // info = c.getMessages();
    // assertTrue(info.size() > 0);
    // for (PublishMessage msg : info) {
    // System.out.println(msg);
    // data = msg.getOwnerCode();
    // assertEquals("ROTTERDAM", data);
    //
    // data = msg.getOwnerName();
    // assertEquals("Rotterdam", data);
    // }
  }

  @Test
  public void testGetServiceTypes() throws RSException {
    EnumSet<ServiceType> st = null;
    st = m_rs.getServiceTypes("BERLIN");
    assertFalse(null == st);
    assertFalse(st.isEmpty());
    assertTrue(st.contains(ServiceType.REGULAR));
  }

  @Test
  public void testFindCityCode() throws RSException, SQLException {
    String citycode = m_calc.findCityCode("ROTTERDAM");
    assertNotNull(citycode);
    assertEquals("RDM", citycode);
  }

  @Test
  public void testFindCityForStation() throws SQLException, RSException {
    String city = m_calc.findCityForStation("RDMBLAAK");
    assertNotNull(city);
    assertEquals("ROTTERDAM", city);
  }

  @Test
  public void testFindNearestCity() throws RSException {
    // Wilhelminaplein: 51.907, 4.494
    City c = m_rs.findNearestCity(51.907, 4.494);
    assertNotNull(c);
    assertEquals("ROTTERDAM", c.getCode());
    assertTrue(c.getHasGPSInfo());
  }

  @Test
  public void testGetAllCityCodes() throws RSException {
    List<String> clist = m_rs.getAllCityCodes();
    assertNotNull(clist);
    System.out.println("City count: " + clist.size());
  }

  @Test
  public void testCityLocalname() throws RSException {
    City c = null;
    c = m_rs.findCity("ROTTERDAM");
    assertNotNull(c);
    // String data;
    // data = c.getLocalName();
    // assertNotNull(data);
    // assertEquals("mikel city", data);
  }

}
