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
 * TC910: test split of segments.
 * <p>
 * line 1: ABCDE<br>
 * line 2: ABCDEFGH<br>
 * line 3: ICJ
 * 
 * 
 */
public class TestTC910 extends TestBase {

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
    rr.reset();
    rt = m_rs.getRoute("910JUNCTION_BOULEVARD___ROOSEVELT_AVENUE",
        "910WOODSIDE_61_STREET___ROOSEVELT_AVENUE", null, rr);
    assertFalse(null == rt);
    assertFalse(rt.isEmpty());
    // assertTrue(2 == rt.size());
    assertTrue(RCODE.OK == rr.getCode());
    System.out.println("testA_D");
    // int i = 0;
    for (RouteSegment rseg : rt) {
      System.out.println(rseg);
      assertFalse(null == rseg);
      assertFalse(null == rseg.getLocation());
      // switch (i) {
      // case 0:
      // assertTrue(TransferType.TRANSFER == rseg.getTransferType());
      // assertTrue("909A".equals(rseg.getLocation().getCode()));
      // assertTrue(2 == rseg.getDirections().size());
      // assertEquals(rseg.toString(),
      // "TRANSFER: 909A. [[Line 1, towards E, Line 2, towards H]]");
      // break;
      // case 1:
      // assertTrue(TransferType.FINISH == rseg.getTransferType());
      // assertTrue("909D".equals(rseg.getLocation().getCode()));
      // assertTrue(rseg.getDirections().isEmpty());
      // assertEquals(rseg.toString(), "FINISH: 909D. []");
      // break;
      // }
      // i++;
    }
  }
}
