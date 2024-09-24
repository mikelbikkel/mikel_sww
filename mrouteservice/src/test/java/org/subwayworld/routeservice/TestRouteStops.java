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

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.subwayworld.routeservice.RouteResult.RCODE;

/**
 * Test if stops on a route are calculated correctly.
 * 
 */
public class TestRouteStops extends TestBase {

  @Before
  public void setUp() throws Exception {
    initService();
  }

  @After
  public void tearDown() throws Exception {
    stopService();
  }

  /**
   * Test route between two stations on the same node.
   * 
   * @throws RSException
   */
  @Test
  public void testMergeStops() throws RSException {
    RouteResult rr = new RouteResult();
    List<RouteSegment> rt = null;
    String[] stops = { "RDMSTADHUIS", "RDMBEURS", "RDMLEUVEHAVEN",
        "RDMWILHELMINAPLEIN", "RDMRIJNHAVEN", "RDMMAASHAVEN" };
    String st;
    rr.reset();
    rt = m_rs.getRoute("RDMROTTERDAM_CENTRAAL", "RDMZUIDPLEIN", null, rr);
    assertFalse(null == rt);
    assertFalse(rt.isEmpty());
    assertTrue(2 == rt.size());
    assertTrue(RCODE.OK == rr.getCode());
    int i = 0;
    for (RouteSegment rseg : rt) {
      // System.out.println(rseg);
      assertFalse(null == rseg);
      assertFalse(null == rseg.getLocation());
      switch (i) {
      case 0:
        assertTrue(TransferType.TRANSFER == rseg.getTransferType());
        assertTrue("RDMROTTERDAM_CENTRAAL".equals(rseg.getLocation().getCode()));
        assertFalse(rseg.getDirections().isEmpty());
        assertFalse(rseg.getStops().isEmpty());
        for (int j = 0; j < stops.length; j++) {
          st = rseg.getStops().get(j).getCode();
          // System.out.println("  " + st);
          assertEquals(stops[j], st);
        }
        break;
      case 1:
        assertTrue(TransferType.FINISH == rseg.getTransferType());
        assertTrue("RDMZUIDPLEIN".equals(rseg.getLocation().getCode()));
        assertTrue(rseg.getDirections().isEmpty());
        assertTrue(rseg.getStops().isEmpty());
        break;
      }
      i++;
    }
  }
}
