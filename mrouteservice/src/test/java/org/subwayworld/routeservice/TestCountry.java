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
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Country functions from the RouteService interface.
 * 
 */
public class TestCountry extends TestBase {

  @Before
  public void setUp() throws Exception {
    initService();
  }

  @After
  public void tearDown() throws Exception {
    stopService();
  }

  @Test
  public void testGetCountries() throws RSException {
    List<Country> countries = null;
    countries = m_rs.getCountries("AFRICA");
    assertFalse(null == countries);
    assertFalse(countries.isEmpty());
    String data;

    List<String> targetNames = new ArrayList<String>();
    List<String> targetCodes = new ArrayList<String>();

    for (Country c : countries) {
      data = c.getCode();
      assertFalse(null == data);
      targetCodes.add(data);

      data = c.getName();
      assertFalse(null == data);
      targetNames.add(data);
    }

    assertTrue(2 == targetCodes.size());
    Collections.sort(targetCodes);
    assertEquals("ALGERIA", targetCodes.get(0));
    assertEquals("EGYPT", targetCodes.get(1));

    assertTrue(2 == targetNames.size());
    Collections.sort(targetNames);
    assertEquals("Algeria", targetNames.get(0));
    assertEquals("Egypt", targetNames.get(1));
  }

  @Test
  public void testFindCountry() throws RSException {
    Country c = null;
    c = m_rs.findCountry("UNITED_KINGDOM");
    assertFalse(null == c);
    String data;
    data = c.getCode();
    assertFalse(null == data);
    assertEquals("UNITED_KINGDOM", data);

    data = c.getName();
    assertFalse(null == data);
    assertEquals("United Kingdom", data);

  }

  @Test
  public void testGetCitiesForCountry() throws RSException {
    List<City> cities = null;
    cities = m_rs.getCitiesForCountry("PORTUGAL");
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
    assertEquals("LISBON", targetCodes.get(0));
    assertEquals("OPORTO", targetCodes.get(1));

    assertTrue(2 == targetNames.size());
    Collections.sort(targetNames);
    assertEquals("Lisbon", targetNames.get(0));
    assertEquals("Oporto", targetNames.get(1));
  }

}
