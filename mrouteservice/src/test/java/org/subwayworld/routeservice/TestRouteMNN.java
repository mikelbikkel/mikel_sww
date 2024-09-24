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
public class TestRouteMNN extends TestBase {

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
  public void testMNN() throws RSException {
    RouteResult rr = new RouteResult();
    List<RouteSegment> rt = null;
    rr.reset();
    rt = m_rs.getRoute("MSWCHEKHOVSKAYA", "MSWTVERSKAYA", null, rr);
    assertFalse(null == rt);
    assertFalse(rt.isEmpty());
    assertTrue(2 == rt.size());
    assertTrue(RCODE.OK == rr.getCode());
    int i = 0;
    for (RouteSegment rseg : rt) {
      assertFalse(null == rseg);
      assertFalse(null == rseg.getLocation());
      switch (i) {
      case 0:
        // assertTrue(TransferType.MNN_WALK == rseg.getTransferType());
        assertTrue("MSWCHEKHOVSKAYA".equals(rseg.getLocation().getCode()));
        assertFalse(rseg.getDirections().isEmpty());
        break;
      case 1:
        assertTrue(TransferType.FINISH == rseg.getTransferType());
        assertTrue("MSWTVERSKAYA".equals(rseg.getLocation().getCode()));
        assertTrue(rseg.getDirections().isEmpty());
        break;
      }
      i++;
    }
  }

  /**
   * Test inverse route between two stations on the same node.
   * 
   * @throws RSException
   */
  @Test
  public void testMNNRev() throws RSException {
    RouteResult rr = new RouteResult();
    List<RouteSegment> rt = null;
    rr.reset();
    rt = m_rs.getRoute("MSWTVERSKAYA", "MSWCHEKHOVSKAYA", null, rr);
    assertFalse(null == rt);
    assertFalse(rt.isEmpty());
    assertTrue(2 == rt.size());
    assertTrue(RCODE.OK == rr.getCode());
    int i = 0;
    for (RouteSegment rseg : rt) {
      assertFalse(null == rseg);
      assertFalse(null == rseg.getLocation());
      switch (i) {
      case 0:
        // assertTrue(TransferType.MNN_WALK == rseg.getTransferType());
        assertTrue("MSWTVERSKAYA".equals(rseg.getLocation().getCode()));
        break;
      case 1:
        assertTrue(TransferType.FINISH == rseg.getTransferType());
        assertTrue("MSWCHEKHOVSKAYA".equals(rseg.getLocation().getCode()));
        assertTrue(rseg.getDirections().isEmpty());
        break;
      }
      i++;
    }
  }

  /**
   * Test route that starts on a multinamed node.
   * 
   * @throws RSException
   */
  @Test
  public void testMNNStart() throws RSException {
    RouteResult rr = new RouteResult();
    List<RouteSegment> rt = null;
    rr.reset();
    rt = m_rs.getRoute("MSWTVERSKAYA", "MSWBEGOVAYA", null, rr);
    assertFalse(null == rt);
    assertFalse(rt.isEmpty());
    assertTrue(3 == rt.size());
    assertTrue(RCODE.OK == rr.getCode());
    int i = 0;
    System.out.println("MNNStart");
    for (RouteSegment rseg : rt) {
      assertFalse(null == rseg);
      assertFalse(null == rseg.getLocation());
      switch (i) {
      case 0:
        // assertTrue(TransferType.MNN_WALK == rseg.getTransferType());
        assertTrue("MSWTVERSKAYA".equals(rseg.getLocation().getCode()));
        break;
      case 1:
        assertTrue(TransferType.TRANSFER == rseg.getTransferType());
        assertTrue("MSWPUSHKINSKAYA".equals(rseg.getLocation().getCode()));
        break;
      case 2:
        assertTrue(TransferType.FINISH == rseg.getTransferType());
        assertTrue("MSWBEGOVAYA".equals(rseg.getLocation().getCode()));
        assertTrue(rseg.getDirections().isEmpty());
        break;
      }
      System.out.println(rseg);
      i++;
    }
  }

  /**
   * Test route that ends on a multinamed node.
   * 
   * @throws RSException
   */
  @Test
  public void testMNNEnd() throws RSException {
    RouteResult rr = new RouteResult();
    List<RouteSegment> rt = null;
    rr.reset();
    rt = m_rs.getRoute("MSWBEGOVAYA", "MSWTVERSKAYA", null, rr);
    assertFalse(null == rt);
    assertFalse(rt.isEmpty());
    assertTrue(3 == rt.size());
    assertTrue(RCODE.OK == rr.getCode());
    int i = 0;
    System.out.println("MNNEnd");
    for (RouteSegment rseg : rt) {
      assertFalse(null == rseg);
      assertFalse(null == rseg.getLocation());
      switch (i) {
      case 0:
        assertTrue("MSWBEGOVAYA".equals(rseg.getLocation().getCode()));
        assertTrue(TransferType.TRANSFER == rseg.getTransferType());
        break;
      case 1:
        // assertTrue(TransferType.MNN_WALK == rseg.getTransferType());
        assertTrue("MSWPUSHKINSKAYA".equals(rseg.getLocation().getCode()));
        break;
      case 2:
        assertTrue(TransferType.FINISH == rseg.getTransferType());
        assertTrue("MSWTVERSKAYA".equals(rseg.getLocation().getCode()));
        assertTrue(rseg.getDirections().isEmpty());
        break;
      }
      System.out.println(rseg);
      i++;
    }
  }

  /**
   * Test route that contains an MNN walk in the middle.
   * 
   * @throws RSException
   */
  @Test
  public void testMNNInter() throws RSException {
    RouteResult rr = new RouteResult();
    List<RouteSegment> rt = null;
    rr.reset();
    rt = m_rs
        .getRoute("MSWBEGOVAYA", "MSWMAYAKOVSKAYA", null, rr);
    assertFalse(null == rt);
    assertFalse(rt.isEmpty());
    assertTrue(4 == rt.size());
    assertTrue(RCODE.OK == rr.getCode());
    int i = 0;
    System.out.println("MNNInter");
    for (RouteSegment rseg : rt) {
      assertFalse(null == rseg);
      assertFalse(null == rseg.getLocation());
      switch (i) {
      case 0:
        assertTrue("MSWBEGOVAYA".equals(rseg.getLocation().getCode()));
        assertTrue(TransferType.TRANSFER == rseg.getTransferType());
        break;
      case 1:
        // assertTrue(TransferType.MNN_WALK == rseg.getTransferType());
        assertTrue("MSWPUSHKINSKAYA".equals(rseg.getLocation().getCode()));
        break;
      case 2:
        assertTrue(TransferType.TRANSFER == rseg.getTransferType());
        assertTrue("MSWTVERSKAYA".equals(rseg.getLocation().getCode()));
        break;
      case 3:
        assertTrue(TransferType.FINISH == rseg.getTransferType());
        assertTrue("MSWMAYAKOVSKAYA".equals(rseg.getLocation().getCode()));
        assertTrue(rseg.getDirections().isEmpty());
        break;
      }
      System.out.println(rseg);
      i++;
    }
  }
}
