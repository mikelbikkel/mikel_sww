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

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test getNearestStation from the RouteService interface.
 * 
 */
public class TestNearestStation extends TestBase {

  @Before
  public void setUp() throws Exception {
    initService();
  }

  @After
  public void tearDown() throws Exception {
    stopService();
  }

  @Test
  public void testNearestWilhelminaplein() throws RSException {
    List<NearestStation> stat = null;
    // Wilhelminaplein: 51.907, 4.494
    System.out.println("");
    System.out.println("Find 5 stations for Wilhelminaplein within 2 km.");
    stat = m_rs.findNearestStation(51.907, 4.494, 5, 2);
    for (NearestStation ns : stat) {
      System.out.println(ns);
    }
    assertTrue(true);
  }

  @Test
  public void testNearestMaritiemMuseum() throws RSException {
    List<NearestStation> stat = null;
    // 'RDMMARITIME_MUSEUM', 51.917471, 4.481821, 'LANDMARK'
    System.out.println("");
    System.out.println("Find 7 stations for Maritiem Museum within 3 km:");
    stat = m_rs.findNearestStation(51.917471, 4.481821, 7, 3);
    for (NearestStation ns : stat) {
      System.out.println(ns);
    }
    assertTrue(true);
  }

  @Test
  public void testNearestMarknesse() throws RSException {
    List<NearestStation> stat = null;
    // Marknesse: 52.70980, 5.86365
    System.out.println("");
    System.out.println("Find 2 stations for Marknesse within 30 km:");
    stat = m_rs.findNearestStation(52.70980, 5.86365, 2, 30);
    for (NearestStation ns : stat) {
      System.out.println(ns);
    }
    assertTrue(true);
  }
}
