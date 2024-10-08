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

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test getRoute with landmarks.
 * 
 */
public class TestRouteLandmark extends TestBase {

  @Before
  public void setUp() throws Exception {
    initService();
  }

  @After
  public void tearDown() throws Exception {
    stopService();
  }

  @Test
  public void testLandmarkFrom() throws RSException {
    RouteResult rr = new RouteResult();
    List<RouteSegment> rt = null;
    rr.reset();
    rt = m_rs.getRoute("RDMEUROMAST", "RDMBLAAK", null, rr);
    assertFalse(null == rt);
    assertFalse(rt.isEmpty());
    System.out.println("Landmark from");
    for (RouteSegment rseg : rt) {
      assertFalse(null == rseg.getLocation());
      System.out.println(rseg.toString());
    }
  }

  @Test
  public void testLandmarkTo() throws RSException {
    RouteResult rr = new RouteResult();
    List<RouteSegment> rt = null;
    rr.reset();
    rt = m_rs.getRoute("RDMBLAAK", "RDMEUROMAST", null, rr);
    assertFalse(null == rt);
    assertFalse(rt.isEmpty());
    System.out.println("Landmark to");
    for (RouteSegment rseg : rt) {
      assertFalse(null == rseg.getLocation());
      System.out.println(rseg);
    }
  }
}
