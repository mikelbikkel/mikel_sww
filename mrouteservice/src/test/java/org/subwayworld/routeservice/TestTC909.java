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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.subwayworld.routeservice.RouteResult.RCODE;

/**
 * TC909: test merge of segments.
 * <p>
 * line 1: ABCDE<br>
 * line 2: ABCDEFGH<br>
 * line 3: ICJ
 * 
 * 
 */
public class TestTC909 extends TestBase {

  @Before
  public void setUp() throws Exception {
    initService();
  }

  @After
  public void tearDown() throws Exception {
    stopService();
  }

  @Test
  public void testA_D() throws RSException {
    RouteResult rr = new RouteResult();
    List<RouteSegment> rt = null;
    Set<String> soll = new HashSet<String>();
    soll.add("Line 1E");
    soll.add("Line 2H");
    Set<String> ist = new HashSet<String>();
    SegmentDirection sd;
    rr.reset();
    rt = m_rs.getRoute("909A", "909D", null, rr);
    assertFalse(null == rt);
    assertFalse(rt.isEmpty());
    assertTrue(2 == rt.size());
    assertTrue(RCODE.OK == rr.getCode());
    System.out.println("testA_D");
    int i = 0;
    for (RouteSegment rseg : rt) {
      System.out.println(rseg);
      assertFalse(null == rseg);
      assertFalse(null == rseg.getLocation());
      switch (i) {
      case 0:
        assertTrue(TransferType.TRANSFER == rseg.getTransferType());
        assertTrue("909A".equals(rseg.getLocation().getCode()));
        assertTrue(2 == rseg.getDirections().size());
        sd = rseg.getDirections().get(0);
        ist.add(sd.getLine() + sd.getEndpoint());
        sd = rseg.getDirections().get(1);
        ist.add(sd.getLine() + sd.getEndpoint());
        assertEquals(ist, soll);
        break;
      case 1:
        assertTrue(TransferType.FINISH == rseg.getTransferType());
        assertTrue("909D".equals(rseg.getLocation().getCode()));
        assertTrue(rseg.getDirections().isEmpty());
        break;
      }
      i++;
    }
  }

  @Test
  public void testA_G() throws RSException {
    RouteResult rr = new RouteResult();
    List<RouteSegment> rt = null;
    rr.reset();
    rt = m_rs.getRoute("909A", "909G", null, rr);
    assertFalse(null == rt);
    assertFalse(rt.isEmpty());
    assertTrue(2 == rt.size());
    assertTrue(RCODE.OK == rr.getCode());
    System.out.println("testA_G");
    int i = 0;
    for (RouteSegment rseg : rt) {
      System.out.println(rseg);
      assertFalse(null == rseg);
      assertFalse(null == rseg.getLocation());
      switch (i) {
      case 0:
        assertTrue(TransferType.TRANSFER == rseg.getTransferType());
        assertTrue("909A".equals(rseg.getLocation().getCode()));
        assertTrue(1 == rseg.getDirections().size());

        String dir = rseg.getDirections().get(0).getLine();
        assertEquals(dir, "Line 2");
        dir = rseg.getDirections().get(0).getEndpoint();
        assertEquals(dir, "H");
        break;
      case 1:
        assertTrue(TransferType.FINISH == rseg.getTransferType());
        assertTrue("909G".equals(rseg.getLocation().getCode()));
        assertTrue(rseg.getDirections().isEmpty());
        break;
      }
      i++;
    }
  }

  @Test
  public void testD_A() throws RSException {
    Set<String> soll = new HashSet<String>();
    soll.add("Line 1A");
    soll.add("Line 2A");
    Set<String> ist = new HashSet<String>();
    SegmentDirection sd;
    RouteResult rr = new RouteResult();
    List<RouteSegment> rt = null;
    rr.reset();
    rt = m_rs.getRoute("909D", "909A", null, rr);
    assertFalse(null == rt);
    assertFalse(rt.isEmpty());
    assertTrue(2 == rt.size());
    assertTrue(RCODE.OK == rr.getCode());
    System.out.println("testD_A");
    int i = 0;
    for (RouteSegment rseg : rt) {
      System.out.println(rseg);
      assertFalse(null == rseg);
      assertFalse(null == rseg.getLocation());
      switch (i) {
      case 0:
        assertTrue(TransferType.TRANSFER == rseg.getTransferType());
        assertTrue("909D".equals(rseg.getLocation().getCode()));
        assertTrue(2 == rseg.getDirections().size());
        sd = rseg.getDirections().get(0);
        ist.add(sd.getLine() + sd.getEndpoint());
        sd = rseg.getDirections().get(1);
        ist.add(sd.getLine() + sd.getEndpoint());
        assertEquals(ist, soll);
        break;
      case 1:
        assertTrue(TransferType.FINISH == rseg.getTransferType());
        assertTrue("909A".equals(rseg.getLocation().getCode()));
        assertTrue(rseg.getDirections().isEmpty());
        break;
      }
      i++;
    }
  }

  @Test
  public void testG_A() throws RSException {
    RouteResult rr = new RouteResult();
    List<RouteSegment> rt = null;
    rr.reset();
    rt = m_rs.getRoute("909G", "909A", null, rr);
    assertFalse(null == rt);
    assertFalse(rt.isEmpty());
    assertTrue(2 == rt.size());
    assertTrue(RCODE.OK == rr.getCode());
    System.out.println("testG_A");
    int i = 0;
    for (RouteSegment rseg : rt) {
      System.out.println(rseg);
      assertFalse(null == rseg);
      assertFalse(null == rseg.getLocation());
      switch (i) {
      case 0:
        assertTrue(TransferType.TRANSFER == rseg.getTransferType());
        assertTrue("909G".equals(rseg.getLocation().getCode()));
        assertTrue(1 == rseg.getDirections().size());

        String dir = rseg.getDirections().get(0).getLine();
        assertEquals(dir, "Line 2");
        dir = rseg.getDirections().get(0).getEndpoint();
        assertEquals(dir, "A");
        break;
      case 1:
        assertTrue(TransferType.FINISH == rseg.getTransferType());
        assertTrue("909A".equals(rseg.getLocation().getCode()));
        assertTrue(rseg.getDirections().isEmpty());
        break;
      }
      i++;
    }
  }
}
