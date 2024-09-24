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
import static org.junit.Assert.assertTrue;

import java.util.EnumSet;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.subwayworld.routeservice.RouteResult.RCODE;

/**
 * TC900: test the service types.
 * 
 */
public class TestTC900 extends TestBase {

  @Before
  public void setUp() throws Exception {
    initService();
  }

  @After
  public void tearDown() throws Exception {
    stopService();
  }

  @Test
  public void testAD_All() throws RSException {
    RouteResult rr = new RouteResult();
    List<RouteSegment> rt = null;
    rr.reset();
    rt = m_rs.getRoute("900A", "900D", null, rr);
    System.out.println("testAD_All: " + rr.getCode());
    assertNotNull(rt);
    assertFalse(rt.isEmpty());
    assertTrue(2 == rt.size());
    assertTrue(RCODE.OK == rr.getCode());
    int i = 0;
    for (RouteSegment rseg : rt) {
      System.out.println(rseg + " <" + rseg.getDistance() + ">");
      switch (i) {
      case 0:
        assertTrue(TransferType.TRANSFER == rseg.getTransferType());
        assertTrue("900A".equals(rseg.getLocation().getCode()));
        assertTrue(1 == rseg.getDirections().size());

        String dir = rseg.getDirections().get(0).getLine();
        assertEquals(dir, "Main line (partial)");
        dir = rseg.getDirections().get(0).getEndpoint();
        assertEquals(dir, "F");
        break;
      case 1:
        assertTrue(TransferType.FINISH == rseg.getTransferType());
        assertTrue("900D".equals(rseg.getLocation().getCode()));
        assertTrue(rseg.getDirections().isEmpty());
        break;
      }
      i++;
    }
  }

  @Test
  public void testAD_Regular() throws RSException {
    RouteResult rr = new RouteResult();
    List<RouteSegment> rt = null;
    EnumSet<ServiceType> es = EnumSet.of(ServiceType.REGULAR);
    rr.reset();
    // 900D can only be reached using the PARTIAL line.
    rt = m_rs.getRoute("900A", "900D", es, rr);
    System.out.println("testAD_Regular: " + rr.getCode());
    assertNotNull(rt);
    assertTrue(rt.isEmpty());
    assertTrue(RCODE.NO_ROUTE == rr.getCode());
  }

  @Test
  public void testAE_All() throws RSException {
    RouteResult rr = new RouteResult();
    List<RouteSegment> rt = null;
    rr.reset();
    rt = m_rs.getRoute("900A", "900E", null, rr);
    System.out.println("testAE_All: " + rr.getCode());
    assertNotNull(rt);
    assertFalse(rt.isEmpty());
    assertTrue(2 == rt.size());
    assertTrue(RCODE.OK == rr.getCode());
    int i = 0;
    for (RouteSegment rseg : rt) {
      System.out.println(rseg + " <" + rseg.getDistance() + ">");
      switch (i) {
      case 0:
        assertTrue(TransferType.TRANSFER == rseg.getTransferType());
        assertTrue("900A".equals(rseg.getLocation().getCode()));
        assertTrue(1 == rseg.getDirections().size());
        String dir = rseg.getDirections().get(0).getLine();
        assertEquals(dir, "Main line (partial)");
        dir = rseg.getDirections().get(0).getEndpoint();
        assertEquals(dir, "F");
        break;
      case 1:
        assertTrue(TransferType.FINISH == rseg.getTransferType());
        assertTrue("900E".equals(rseg.getLocation().getCode()));
        assertTrue(rseg.getDirections().isEmpty());
        break;
      }
      i++;
    }
  }

  @Test
  public void testAE_Regular() throws RSException {
    RouteResult rr = new RouteResult();
    List<RouteSegment> rt = null;
    EnumSet<ServiceType> es = EnumSet.of(ServiceType.REGULAR);
    rr.reset();
    rt = m_rs.getRoute("900A", "900E", es, rr);
    System.out.println("testAE_Regular: " + rr.getCode());
    assertNotNull(rt);
    assertFalse(rt.isEmpty());
    assertTrue(3 == rt.size());
    assertTrue(RCODE.OK == rr.getCode());
    int i = 0;
    String dir;
    for (RouteSegment rseg : rt) {
      System.out.println(rseg + " <" + rseg.getDistance() + ">");
      switch (i) {
      case 0:
        assertTrue(TransferType.TRANSFER == rseg.getTransferType());
        assertTrue("900A".equals(rseg.getLocation().getCode()));
        assertTrue(1 == rseg.getDirections().size());
        dir = rseg.getDirections().get(0).getLine();
        assertEquals(dir, "Main line");
        dir = rseg.getDirections().get(0).getEndpoint();
        assertEquals(dir, "C");
        break;
      case 1:
        assertTrue(TransferType.TRANSFER == rseg.getTransferType());
        assertTrue("900B".equals(rseg.getLocation().getCode()));
        assertTrue(1 == rseg.getDirections().size());

        dir = rseg.getDirections().get(0).getLine();
        assertEquals(dir, "Support line");
        dir = rseg.getDirections().get(0).getEndpoint();
        assertEquals(dir, "G");
        break;
      case 2:
        assertTrue(TransferType.FINISH == rseg.getTransferType());
        assertTrue("900E".equals(rseg.getLocation().getCode()));
        assertTrue(rseg.getDirections().isEmpty());
        break;
      }
      i++;
    }
  }

  @Test
  public void testDF_All() throws RSException {
    RouteResult rr = new RouteResult();
    List<RouteSegment> rt = null;
    rr.reset();
    rt = m_rs.getRoute("900D", "900F", null, rr);
    System.out.println("testDF_All: " + rr.getCode());
    assertNotNull(rt);
    assertFalse(rt.isEmpty());
    assertTrue(2 == rt.size());
    assertTrue(RCODE.OK == rr.getCode());
    int i = 0;
    String dir;
    SegmentDirection sd;
    for (RouteSegment rseg : rt) {
      System.out.println(rseg + " <" + rseg.getDistance() + ">");
      switch (i) {
      case 0:
        assertTrue(TransferType.TRANSFER == rseg.getTransferType());
        assertTrue("900D".equals(rseg.getLocation().getCode()));
        assertTrue(1 == rseg.getDirections().size());
        sd = rseg.getDirections().get(0);
        dir = sd.getLine();
        assertEquals(dir, "Main line (partial)");
        dir = sd.getEndpoint();
        assertEquals(dir, "F");
        break;
      case 1:
        assertTrue(TransferType.FINISH == rseg.getTransferType());
        assertTrue("900F".equals(rseg.getLocation().getCode()));
        assertTrue(rseg.getDirections().isEmpty());
        break;
      }
      i++;
    }
  }

  @Test
  public void testDF_Regular() throws RSException {
    RouteResult rr = new RouteResult();
    List<RouteSegment> rt = null;
    EnumSet<ServiceType> es = EnumSet.of(ServiceType.REGULAR);
    rr.reset();
    // Both D and F can only be reached using the PARTIAL line.
    // This generates a graph of 2 unconnected nodes, D and F.
    rt = m_rs.getRoute("900D", "900F", es, rr);
    System.out.println("testDF_Regular: " + rr.getCode());
    assertNotNull(rt);
    assertTrue(rt.isEmpty());
    assertTrue(RCODE.NO_ROUTE == rr.getCode());
  }

  @Test
  public void testServiceTypes() throws RSException {
    EnumSet<ServiceType> es = m_rs.getServiceTypes("TEST_TC_900");
    assertFalse(es.contains(ServiceType.EXTENDED));
    assertTrue(es.contains(ServiceType.REGULAR));
    assertTrue(es.contains(ServiceType.PARTIAL));
  }
}
