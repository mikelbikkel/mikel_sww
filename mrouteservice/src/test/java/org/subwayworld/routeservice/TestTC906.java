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
 * TC906: test Inverse partial overlap with MNN.
 * 
 */
public class TestTC906 extends TestBase {

  @Before
  public void setUp() throws Exception {
    initService();
  }

  @After
  public void tearDown() throws Exception {
    stopService();
  }

  @Test
  public void testAC() throws RSException {
    RouteResult rr = new RouteResult();
    List<RouteSegment> rt = null;
    rr.reset();
    rt = m_rs.getRoute("906A", "906C", null, rr);
    assertFalse(null == rt);
    assertFalse(rt.isEmpty());
    // assertTrue(3 == rt.size());
    assertTrue(RCODE.OK == rr.getCode());
    System.out.println("testAC");
    int i = 0;
    for (RouteSegment rseg : rt) {
      // assertFalse(null == rseg);
      // assertFalse(null == rseg.getLocation());
      switch (i) {
      case 0:
        // assertTrue(TransferType.TRANSFER == rseg.getTransferType());
        // assertTrue("901A".equals(rseg.getLocation().getCode()));
        // assertFalse(rseg.getDirections().isEmpty());
        break;
      case 1:
        // assertTrue(TransferType.TRANSFORMATION == rseg.getTransferType());
        // assertTrue("901F".equals(rseg.getLocation().getCode()));
        // assertFalse(rseg.getDirections().isEmpty());
        break;
      case 2:
        // assertTrue(TransferType.FINISH == rseg.getTransferType());
        // assertTrue("901G".equals(rseg.getLocation().getCode()));
        // assertTrue(rseg.getDirections().isEmpty());
        break;
      }
      i++;
      System.out.println(rseg);
    }
  }

  @Test
  public void testDC() throws RSException {
    RouteResult rr = new RouteResult();
    List<RouteSegment> rt = null;
    rr.reset();
    rt = m_rs.getRoute("906D", "906C", null, rr);
    assertFalse(null == rt);
    assertFalse(rt.isEmpty());
    // assertTrue(3 == rt.size());
    assertTrue(RCODE.OK == rr.getCode());
    System.out.println("testDC");
    int i = 0;
    for (RouteSegment rseg : rt) {
      // assertFalse(null == rseg);
      // assertFalse(null == rseg.getLocation());
      switch (i) {
      case 0:
        // assertTrue(TransferType.TRANSFER == rseg.getTransferType());
        // assertTrue("901A".equals(rseg.getLocation().getCode()));
        // assertFalse(rseg.getDirections().isEmpty());
        break;
      case 1:
        // assertTrue(TransferType.TRANSFORMATION == rseg.getTransferType());
        // assertTrue("901F".equals(rseg.getLocation().getCode()));
        // assertFalse(rseg.getDirections().isEmpty());
        break;
      case 2:
        // assertTrue(TransferType.FINISH == rseg.getTransferType());
        // assertTrue("901G".equals(rseg.getLocation().getCode()));
        // assertTrue(rseg.getDirections().isEmpty());
        break;
      }
      i++;
      System.out.println(rseg);
    }
  }

  @Test
  public void testAE() throws RSException {
    RouteResult rr = new RouteResult();
    List<RouteSegment> rt = null;
    rr.reset();
    rt = m_rs.getRoute("906A", "906E", null, rr);
    assertFalse(null == rt);
    assertFalse(rt.isEmpty());
    // assertTrue(3 == rt.size());
    assertTrue(RCODE.OK == rr.getCode());
    System.out.println("testAE");
    int i = 0;
    for (RouteSegment rseg : rt) {
      // assertFalse(null == rseg);
      // assertFalse(null == rseg.getLocation());
      switch (i) {
      case 0:
        // assertTrue(TransferType.TRANSFER == rseg.getTransferType());
        // assertTrue("901A".equals(rseg.getLocation().getCode()));
        // assertFalse(rseg.getDirections().isEmpty());
        break;
      case 1:
        // assertTrue(TransferType.TRANSFORMATION == rseg.getTransferType());
        // assertTrue("901F".equals(rseg.getLocation().getCode()));
        // assertFalse(rseg.getDirections().isEmpty());
        break;
      case 2:
        // assertTrue(TransferType.FINISH == rseg.getTransferType());
        // assertTrue("901G".equals(rseg.getLocation().getCode()));
        // assertTrue(rseg.getDirections().isEmpty());
        break;
      }
      i++;
      System.out.println(rseg);
    }
  }

  @Test
  public void testCA() throws RSException {
    RouteResult rr = new RouteResult();
    List<RouteSegment> rt = null;
    rr.reset();
    rt = m_rs.getRoute("906C", "906A", null, rr);
    assertFalse(null == rt);
    assertFalse(rt.isEmpty());
    // assertTrue(3 == rt.size());
    assertTrue(RCODE.OK == rr.getCode());
    System.out.println("testCA");
    int i = 0;
    for (RouteSegment rseg : rt) {
      // assertFalse(null == rseg);
      // assertFalse(null == rseg.getLocation());
      switch (i) {
      case 0:
        // assertTrue(TransferType.TRANSFER == rseg.getTransferType());
        // assertTrue("901A".equals(rseg.getLocation().getCode()));
        // assertFalse(rseg.getDirections().isEmpty());
        break;
      case 1:
        // assertTrue(TransferType.TRANSFORMATION == rseg.getTransferType());
        // assertTrue("901F".equals(rseg.getLocation().getCode()));
        // assertFalse(rseg.getDirections().isEmpty());
        break;
      case 2:
        // assertTrue(TransferType.FINISH == rseg.getTransferType());
        // assertTrue("901G".equals(rseg.getLocation().getCode()));
        // assertTrue(rseg.getDirections().isEmpty());
        break;
      }
      i++;
      System.out.println(rseg);
    }
  }

  @Test
  public void testCD() throws RSException {
    RouteResult rr = new RouteResult();
    List<RouteSegment> rt = null;
    rr.reset();
    rt = m_rs.getRoute("906C", "906D", null, rr);
    assertFalse(null == rt);
    assertFalse(rt.isEmpty());
    // assertTrue(3 == rt.size());
    assertTrue(RCODE.OK == rr.getCode());
    System.out.println("testCD");
    int i = 0;
    for (RouteSegment rseg : rt) {
      // assertFalse(null == rseg);
      // assertFalse(null == rseg.getLocation());
      switch (i) {
      case 0:
        // assertTrue(TransferType.TRANSFER == rseg.getTransferType());
        // assertTrue("901A".equals(rseg.getLocation().getCode()));
        // assertFalse(rseg.getDirections().isEmpty());
        break;
      case 1:
        // assertTrue(TransferType.TRANSFORMATION == rseg.getTransferType());
        // assertTrue("901F".equals(rseg.getLocation().getCode()));
        // assertFalse(rseg.getDirections().isEmpty());
        break;
      case 2:
        // assertTrue(TransferType.FINISH == rseg.getTransferType());
        // assertTrue("901G".equals(rseg.getLocation().getCode()));
        // assertTrue(rseg.getDirections().isEmpty());
        break;
      }
      i++;
      System.out.println(rseg);
    }
  }

  @Test
  public void testCB() throws RSException {
    RouteResult rr = new RouteResult();
    List<RouteSegment> rt = null;
    rr.reset();
    rt = m_rs.getRoute("906C", "906B", null, rr);
    assertFalse(null == rt);
    // assertTrue(3 == rt.size());
    assertTrue(RCODE.OK == rr.getCode());
    System.out.println("testCB");
    int i = 0;
    for (RouteSegment rseg : rt) {
      // assertFalse(null == rseg);
      // assertFalse(null == rseg.getLocation());
      switch (i) {
      case 0:
        // assertTrue(TransferType.TRANSFER == rseg.getTransferType());
        // assertTrue("901A".equals(rseg.getLocation().getCode()));
        // assertFalse(rseg.getDirections().isEmpty());
        break;
      case 1:
        // assertTrue(TransferType.TRANSFORMATION == rseg.getTransferType());
        // assertTrue("901F".equals(rseg.getLocation().getCode()));
        // assertFalse(rseg.getDirections().isEmpty());
        break;
      case 2:
        // assertTrue(TransferType.FINISH == rseg.getTransferType());
        // assertTrue("901G".equals(rseg.getLocation().getCode()));
        // assertTrue(rseg.getDirections().isEmpty());
        break;
      }
      i++;
      System.out.println(rseg);
    }
  }

  @Test
  public void testCE() throws RSException {
    RouteResult rr = new RouteResult();
    List<RouteSegment> rt = null;
    rr.reset();
    rt = m_rs.getRoute("906C", "906E", null, rr);
    assertFalse(null == rt);
    assertFalse(rt.isEmpty());
    // assertTrue(3 == rt.size());
    assertTrue(RCODE.OK == rr.getCode());
    System.out.println("testCE");
    int i = 0;
    for (RouteSegment rseg : rt) {
      // assertFalse(null == rseg);
      // assertFalse(null == rseg.getLocation());
      switch (i) {
      case 0:
        // assertTrue(TransferType.TRANSFER == rseg.getTransferType());
        // assertTrue("901A".equals(rseg.getLocation().getCode()));
        // assertFalse(rseg.getDirections().isEmpty());
        break;
      case 1:
        // assertTrue(TransferType.TRANSFORMATION == rseg.getTransferType());
        // assertTrue("901F".equals(rseg.getLocation().getCode()));
        // assertFalse(rseg.getDirections().isEmpty());
        break;
      case 2:
        // assertTrue(TransferType.FINISH == rseg.getTransferType());
        // assertTrue("901G".equals(rseg.getLocation().getCode()));
        // assertTrue(rseg.getDirections().isEmpty());
        break;
      }
      i++;
      System.out.println(rseg);
    }
  }

  @Test
  public void testBE() throws RSException {
    RouteResult rr = new RouteResult();
    List<RouteSegment> rt = null;
    rr.reset();
    rt = m_rs.getRoute("906B", "906E", null, rr);
    assertFalse(null == rt);
    assertFalse(rt.isEmpty());
    // assertTrue(3 == rt.size());
    assertTrue(RCODE.OK == rr.getCode());
    System.out.println("testBE");
    int i = 0;
    for (RouteSegment rseg : rt) {
      // assertFalse(null == rseg);
      // assertFalse(null == rseg.getLocation());
      switch (i) {
      case 0:
        // assertTrue(TransferType.TRANSFER == rseg.getTransferType());
        // assertTrue("901A".equals(rseg.getLocation().getCode()));
        // assertFalse(rseg.getDirections().isEmpty());
        break;
      case 1:
        // assertTrue(TransferType.TRANSFORMATION == rseg.getTransferType());
        // assertTrue("901F".equals(rseg.getLocation().getCode()));
        // assertFalse(rseg.getDirections().isEmpty());
        break;
      case 2:
        // assertTrue(TransferType.FINISH == rseg.getTransferType());
        // assertTrue("901G".equals(rseg.getLocation().getCode()));
        // assertTrue(rseg.getDirections().isEmpty());
        break;
      }
      i++;
      System.out.println(rseg);
    }
  }

  @Test
  public void testEB() throws RSException {
    RouteResult rr = new RouteResult();
    List<RouteSegment> rt = null;
    rr.reset();
    rt = m_rs.getRoute("906E", "906B", null, rr);
    assertFalse(null == rt);
    assertFalse(rt.isEmpty());
    // assertTrue(3 == rt.size());
    assertTrue(RCODE.OK == rr.getCode());
    System.out.println("testEB");
    int i = 0;
    for (RouteSegment rseg : rt) {
      // assertFalse(null == rseg);
      // assertFalse(null == rseg.getLocation());
      switch (i) {
      case 0:
        // assertTrue(TransferType.TRANSFER == rseg.getTransferType());
        // assertTrue("901A".equals(rseg.getLocation().getCode()));
        // assertFalse(rseg.getDirections().isEmpty());
        break;
      case 1:
        // assertTrue(TransferType.TRANSFORMATION == rseg.getTransferType());
        // assertTrue("901F".equals(rseg.getLocation().getCode()));
        // assertFalse(rseg.getDirections().isEmpty());
        break;
      case 2:
        // assertTrue(TransferType.FINISH == rseg.getTransferType());
        // assertTrue("901G".equals(rseg.getLocation().getCode()));
        // assertTrue(rseg.getDirections().isEmpty());
        break;
      }
      i++;
      System.out.println(rseg);
    }
  }
}
