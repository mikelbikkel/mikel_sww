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
 * TC908: test landmarks.
 * 
 */
public class TestTC908 extends TestBase {

  @Before
  public void setUp() throws Exception {
    initService();
  }

  @After
  public void tearDown() throws Exception {
    stopService();
  }

  @Test
  public void testLM01_E() throws RSException {
    RouteResult rr = new RouteResult();
    List<RouteSegment> rt = null;
    rr.reset();
    rt = m_rs.getRoute("908LM01", "908E", null, rr);
    assertFalse(null == rt);
    assertFalse(rt.isEmpty());
    // assertTrue(3 == rt.size());
    assertTrue(RCODE.OK == rr.getCode());
    System.out.println("testLM01_E");
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
  public void testE_LM01() throws RSException {
    RouteResult rr = new RouteResult();
    List<RouteSegment> rt = null;
    rr.reset();
    rt = m_rs.getRoute("908E", "908LM01", null, rr);
    assertFalse(null == rt);
    assertFalse(rt.isEmpty());
    // assertTrue(3 == rt.size());
    assertTrue(RCODE.OK == rr.getCode());
    System.out.println("testLM01_E");
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
  public void testLM02_E() throws RSException {
    RouteResult rr = new RouteResult();
    List<RouteSegment> rt = null;
    rr.reset();
    rt = m_rs.getRoute("908LM02", "908E", null, rr);
    assertFalse(null == rt);
    assertFalse(rt.isEmpty());
    // assertTrue(3 == rt.size());
    assertTrue(RCODE.OK == rr.getCode());
    System.out.println("testLM02_E");
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
  public void testE_LM02() throws RSException {
    RouteResult rr = new RouteResult();
    List<RouteSegment> rt = null;
    rr.reset();
    rt = m_rs.getRoute("908E", "908LM02", null, rr);
    assertFalse(null == rt);
    assertFalse(rt.isEmpty());
    // assertTrue(3 == rt.size());
    assertTrue(RCODE.OK == rr.getCode());
    System.out.println("testE_LM02");
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
  public void testLM02_B() throws RSException {
    RouteResult rr = new RouteResult();
    List<RouteSegment> rt = null;
    rr.reset();
    rt = m_rs.getRoute("908LM02", "908B", null, rr);
    assertFalse(null == rt);
    assertFalse(rt.isEmpty());
    // assertTrue(3 == rt.size());
    assertTrue(RCODE.OK == rr.getCode());
    System.out.println("testLM02_B");
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
  public void testB_LM02() throws RSException {
    RouteResult rr = new RouteResult();
    List<RouteSegment> rt = null;
    rr.reset();
    rt = m_rs.getRoute("908B", "908LM02", null, rr);
    assertFalse(null == rt);
    assertFalse(rt.isEmpty());
    // assertTrue(3 == rt.size());
    assertTrue(RCODE.OK == rr.getCode());
    System.out.println("testB_LM02");
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
  public void testLM01_LM02() throws RSException {
    RouteResult rr = new RouteResult();
    List<RouteSegment> rt = null;
    rr.reset();
    rt = m_rs.getRoute("908LM01", "908LM02", null, rr);
    assertFalse(null == rt);
    assertFalse(rt.isEmpty());
    // assertTrue(3 == rt.size());
    assertTrue(RCODE.OK == rr.getCode());
    System.out.println("testLM01_LM02");
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
  public void testLM02_LM01() throws RSException {
    RouteResult rr = new RouteResult();
    List<RouteSegment> rt = null;
    rr.reset();
    rt = m_rs.getRoute("908LM02", "908LM01", null, rr);
    assertFalse(null == rt);
    assertFalse(rt.isEmpty());
    // assertTrue(3 == rt.size());
    assertTrue(RCODE.OK == rr.getCode());
    System.out.println("testLM02_LM01");
    int i = 0;
    for (RouteSegment rseg : rt) {
      System.out.println(rseg);
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
        assertFalse(rseg.getDirections().isEmpty());
        assertTrue(1 == rseg.getDirections().size());
        break;
      case 2:
        // assertTrue(TransferType.FINISH == rseg.getTransferType());
        // assertTrue("901G".equals(rseg.getLocation().getCode()));
        // assertTrue(rseg.getDirections().isEmpty());
        break;
      }
      i++;
    }
  }

  @Test
  public void testLM03_G() throws RSException {
    RouteResult rr = new RouteResult();
    List<RouteSegment> rt = null;
    rt = m_rs.getRoute("908LM03", "908G", null, rr);
    assertFalse(null == rt);
    assertTrue(rt.isEmpty());
    assertTrue(RCODE.FROM_UNKNOWN == rr.getCode());
    System.out.println("testLM03_G: no route");
  }

  @Test
  public void testG_LM03() throws RSException {
    RouteResult rr = new RouteResult();
    List<RouteSegment> rt = null;
    rt = m_rs.getRoute("908G", "908LM03", null, rr);
    assertFalse(null == rt);
    assertTrue(rt.isEmpty());
    assertTrue(RCODE.TO_UNKNOWN == rr.getCode());
    System.out.println("testG_LM03: no route");
  }

  @Test
  public void testLM02_LM02() throws RSException {
    RouteResult rr = new RouteResult();
    List<RouteSegment> rt = null;
    rt = m_rs.getRoute("908LM02", "908LM02", null, rr);
    assertFalse(null == rt);
    assertTrue(rt.isEmpty());
    assertTrue(RCODE.FROM_IS_TO == rr.getCode());
    System.out.println("testLM02_LM02: no route");
  }

  @Test
  public void testLM02_BLAAK() throws RSException {
    RouteResult rr = new RouteResult();
    List<RouteSegment> rt = null;
    rt = m_rs.getRoute("908LM02", "RDMBLAAK", null, rr);
    assertFalse(null == rt);
    assertTrue(rt.isEmpty());
    assertTrue(RCODE.CITY == rr.getCode());
    System.out.println("testLM02_BLAAK: no route");
  }

  @Test
  public void testBLAAK_LM02() throws RSException {
    RouteResult rr = new RouteResult();
    List<RouteSegment> rt = null;
    rt = m_rs.getRoute("RDMBLAAK", "908LM02", null, rr);
    assertFalse(null == rt);
    assertTrue(rt.isEmpty());
    assertTrue(RCODE.CITY == rr.getCode());
    System.out.println("testBLAAK_LM02: no route");
  }

}
