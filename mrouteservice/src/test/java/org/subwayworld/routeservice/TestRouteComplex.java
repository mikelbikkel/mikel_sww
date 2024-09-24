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
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.subwayworld.routeservice.RouteResult.RCODE;

/**
 * Test getRoute with multi named node routes.
 * 
 */
public class TestRouteComplex extends TestBase {

  @Before
  public void setUp() throws Exception {
    initService();
  }

  @After
  public void tearDown() throws Exception {
    stopService();
  }

  /**
   * Test route with complex as destination.
   * 
   * @throws RSException
   */
  @Test
  public void testComplexEndIsComplex() throws RSException {
    RouteResult rr = new RouteResult();
    List<RouteSegment> rt = null;
    rr.reset();
    rt = m_rs.getRoute("NWCST_JAMES", "NWCMONUMENT_X__X_1", null, rr);
    assertFalse(null == rt);
    assertFalse(rt.isEmpty());
    assertTrue(2 == rt.size());
    assertTrue(RCODE.OK == rr.getCode());
    System.out.println("Route: NWCST_JAMES => NWCMONUMENT_X__X_1");
    int i = 0;
    for (RouteSegment rseg : rt) {
      assertFalse(null == rseg);
      assertFalse(null == rseg.getLocation());
      System.out.println(rseg);
      switch (i) {
      case 0:
        assertTrue(TransferType.TRANSFER == rseg.getTransferType());
        assertTrue("NWCST_JAMES".equals(rseg.getLocation().getCode()));
        // assertFalse(rseg.getDirections().isEmpty());
        break;
      case 1:
        assertTrue(TransferType.FINISH == rseg.getTransferType());
        // YES.... _2!!! That is correct! TWO.
        assertTrue("NWCMONUMENT_X__X_2".equals(rseg.getLocation().getCode()));
        assertTrue(rseg.getDirections().isEmpty());
        break;
      }
      i++;
    }
  }

  /**
   * Test route with complex as destination.
   * 
   * @throws RSException
   */
  @Test
  public void testComplexEndIsNetwork() throws RSException {
    RouteResult rr = new RouteResult();
    List<RouteSegment> rt = null;
    rr.reset();
    rt = m_rs.getRoute("NWCST_JAMES", "NWCMONUMENT_X__X_2", null, rr);
    assertFalse(null == rt);
    assertFalse(rt.isEmpty());
    assertTrue(2 == rt.size());
    assertTrue(RCODE.OK == rr.getCode());
    int i = 0;
    System.out.println("Route: NWCST_JAMES => NWCMONUMENT_X__X_2");
    for (RouteSegment rseg : rt) {
      assertFalse(null == rseg);
      assertFalse(null == rseg.getLocation());
      System.out.println(rseg);
      switch (i) {
      case 0:
        assertTrue(TransferType.TRANSFER == rseg.getTransferType());
        assertTrue("NWCST_JAMES".equals(rseg.getLocation().getCode()));
        // assertFalse(rseg.getDirections().isEmpty());
        break;
      case 1:
        assertTrue(TransferType.FINISH == rseg.getTransferType());
        // YES.... _2!!! That is correct! TWO.
        assertTrue("NWCMONUMENT_X__X_2".equals(rseg.getLocation().getCode()));
        assertTrue(rseg.getDirections().isEmpty());
        break;
      }
      i++;
    }
  }

  /**
   * Test route with complex as inermediate station (east to west).
   * 
   * @throws RSException
   */
  @Test
  public void testComplexInterEastWest() throws RSException {
    RouteResult rr = new RouteResult();
    List<RouteSegment> rt = null;
    rr.reset();
    rt = m_rs.getRoute("NWCST_JAMES", "NWCMANORS", null, rr);
    assertFalse(null == rt);
    assertFalse(rt.isEmpty());
    assertTrue(2 == rt.size());
    assertTrue(RCODE.OK == rr.getCode());
    int i = 0;
    System.out.println("Route: NWCST_JAMES => NWCMANORS");
    for (RouteSegment rseg : rt) {
      assertFalse(null == rseg);
      assertFalse(null == rseg.getLocation());
      System.out.println(rseg);
      switch (i) {
      case 0:
        assertTrue(TransferType.TRANSFER == rseg.getTransferType());
        assertTrue("NWCST_JAMES".equals(rseg.getLocation().getCode()));
        // assertFalse(rseg.getDirections().isEmpty());
        break;
      case 1:
        assertTrue(TransferType.FINISH == rseg.getTransferType());
        assertTrue("NWCMANORS".equals(rseg.getLocation().getCode()));
        assertTrue(rseg.getDirections().isEmpty());
        break;
      }
      i++;
    }
  }

  /**
   * Test route with complex as inermediate station (east to west).
   * 
   * @throws RSException
   */
  @Test
  public void testComplexInterNorthSouth() throws RSException {
    RouteResult rr = new RouteResult();
    List<RouteSegment> rt = null;
    rr.reset();
    rt = m_rs.getRoute("NWCHAYMARKET", "NWCCENTRAL_STATION", null, rr);
    assertFalse(null == rt);
    assertFalse(rt.isEmpty());
    assertTrue(2 == rt.size());
    assertTrue(RCODE.OK == rr.getCode());
    int i = 0;
    System.out.println("Route: NWCHAYMARKET => NWCCENTRAL_STATION");
    for (RouteSegment rseg : rt) {
      assertFalse(null == rseg);
      assertFalse(null == rseg.getLocation());
      System.out.println(rseg);
      switch (i) {
      case 0:
        assertTrue(TransferType.TRANSFER == rseg.getTransferType());
        assertTrue("NWCHAYMARKET".equals(rseg.getLocation().getCode()));
        // assertFalse(rseg.getDirections().isEmpty());
        break;
      case 1:
        assertTrue(TransferType.FINISH == rseg.getTransferType());
        assertTrue("NWCCENTRAL_STATION".equals(rseg.getLocation().getCode()));
        assertTrue(rseg.getDirections().isEmpty());
        break;
      }
      i++;
    }
  }

  /**
   * Test route with complex as transfer point.
   * 
   * @throws RSException
   */
  @Test
  public void testComplexTransfer() throws RSException {
    RouteResult rr = new RouteResult();
    List<RouteSegment> rt = null;
    rr.reset();
    rt = m_rs.getRoute("NWCHAYMARKET", "NWCST_JAMES", null, rr);
    assertFalse(null == rt);
    assertFalse(rt.isEmpty());
    assertTrue(3 == rt.size());
    assertTrue(RCODE.OK == rr.getCode());
    int i = 0;
    System.out.println("Route: NWCHAYMARKET => NWCST_JAMES");
    for (RouteSegment rseg : rt) {
      assertFalse(null == rseg);
      assertFalse(null == rseg.getLocation());
      System.out.println(rseg);
      switch (i) {
      case 0:
        assertTrue(TransferType.TRANSFER == rseg.getTransferType());
        assertTrue("NWCHAYMARKET".equals(rseg.getLocation().getCode()));
        // assertFalse(rseg.getDirections().isEmpty());
        break;
      case 1:
        assertTrue(TransferType.TRANSFER == rseg.getTransferType());
        assertTrue("NWCMONUMENT_X__X_2".equals(rseg.getLocation().getCode()));
        // assertTrue(rseg.getDirections().isEmpty());
        break;
      case 2:
        assertTrue(TransferType.FINISH == rseg.getTransferType());
        assertTrue("NWCST_JAMES".equals(rseg.getLocation().getCode()));
        assertTrue(rseg.getDirections().isEmpty());
        break;
      }
      i++;
    }
  }
}
