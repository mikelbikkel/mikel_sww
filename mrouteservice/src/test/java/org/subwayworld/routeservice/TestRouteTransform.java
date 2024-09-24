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
 * Test getRoute with metro transformation.
 * 
 */
public class TestRouteTransform extends TestBase {

  @Before
  public void setUp() throws Exception {
    initService();
  }

  @After
  public void tearDown() throws Exception {
    stopService();
  }

  @Test
  public void testParisTransform() throws RSException {
    RouteResult rr = new RouteResult();
    List<RouteSegment> rt = null;
    rr.reset();
    rt = m_rs.getRoute("PRSLOUIS_BLANC", "PRSDANUBE", null, rr);
    assertFalse(null == rt);
    assertFalse(rt.isEmpty());
    assertTrue(rr.getCode() == RCODE.OK);
    System.out.println("Paris transformation.");
    System.out.println("  Louis Blanc => Danube:");
    for (RouteSegment rseg : rt) {
      assertFalse(null == rseg.getLocation());
      System.out.println(rseg);
    }
    System.out.println(".");
  }

  @Test
  public void testOsloTransform_6to4() throws RSException {
    RouteResult rr = new RouteResult();
    List<RouteSegment> rt = null;
    rr.reset();
    rt = m_rs.getRoute("OSLCARL_BERNERS_PLASS", "OSLNYDALEN", null, rr);
    assertFalse(null == rt);
    assertFalse(rt.isEmpty());
    assertTrue(rr.getCode() == RCODE.OK);
    System.out.println("Oslo transformation from 6 to 4.");
    System.out.println("  Carl Berners plass => Nydalen:");
    for (RouteSegment rseg : rt) {
      assertFalse(null == rseg.getLocation());
      System.out.println(rseg);
    }
    System.out.println(".");
  }

  @Test
  public void testOsloTransform_6to4_2() throws RSException {
    RouteResult rr = new RouteResult();
    List<RouteSegment> rt = null;
    rr.reset();
    rt = m_rs.getRoute("OSLCARL_BERNERS_PLASS", "OSLFORSKNINGSPARKEN_X__X_1",
        null, rr);
    assertFalse(null == rt);
    assertFalse(rt.isEmpty());
    assertTrue(rr.getCode() == RCODE.OK);
    System.out.println("Oslo transformation from 6 to 4.");
    System.out.println("  Carl Berners plass => Forskningsparken:");
    for (RouteSegment rseg : rt) {
      assertFalse(null == rseg.getLocation());
      System.out.println(rseg);
    }
    System.out.println(".");
  }

  @Test
  public void testOsloTransform_4to6() throws RSException {
    RouteResult rr = new RouteResult();
    List<RouteSegment> rt = null;
    rr.reset();
    rt = m_rs.getRoute("OSLFORSKNINGSPARKEN_X__X_1", "OSLSINSEN", null, rr);
    assertFalse(null == rt);
    assertFalse(rt.isEmpty());
    assertTrue(rr.getCode() == RCODE.OK);
    System.out.println("Oslo transformation from 4 to 6.");
    System.out.println("  Forskningsparken => Sinsen:");
    for (RouteSegment rseg : rt) {
      assertFalse(null == rseg.getLocation());
      System.out.println(rseg);
    }
    System.out.println(".");
  }

  @Test
  public void testOsloTransform_Storo() throws RSException {
    RouteResult rr = new RouteResult();
    List<RouteSegment> rt = null;
    rr.reset();
    rt = m_rs.getRoute("OSLFORSKNINGSPARKEN_X__X_1", "OSLSTORO", null, rr);
    assertFalse(null == rt);
    assertFalse(rt.isEmpty());
    assertTrue(rr.getCode() == RCODE.OK);
    System.out.println("Oslo transformation Storo.");
    System.out.println("  Forskningsparken => Storo:");
    for (RouteSegment rseg : rt) {
      assertFalse(null == rseg.getLocation());
      System.out.println(rseg);
    }
    System.out.println(".");
  }

  @Test
  public void testFranciscoTransformFWD() throws RSException {
    RouteResult rr = new RouteResult();
    List<RouteSegment> rt = null;
    rr.reset();
    rt = m_rs.getRoute("SFROCEAN_AND_JULES", "SFRFOLSOM_AND_THE_EMBARCADERO",
        null, rr);
    assertFalse(null == rt);
    assertFalse(rt.isEmpty());
    assertTrue(rr.getCode() == RCODE.OK);
    System.out.println("SFR transformation forward:");
    for (RouteSegment rseg : rt) {
      assertFalse(null == rseg.getLocation());
      System.out.println(rseg);
    }
  }

  @Test
  public void testFranciscoTransformBCK() throws RSException {
    RouteResult rr = new RouteResult();
    List<RouteSegment> rt = null;
    rr.reset();
    rt = m_rs.getRoute("SFRFOLSOM_AND_THE_EMBARCADERO", "SFROCEAN_AND_JULES",
        null, rr);
    assertFalse(null == rt);
    assertFalse(rt.isEmpty());
    assertTrue(rr.getCode() == RCODE.OK);
    System.out.println("SFR transformation back:");
    for (RouteSegment rseg : rt) {
      assertFalse(null == rseg.getLocation());
      System.out.println(rseg);
    }
  }

}
