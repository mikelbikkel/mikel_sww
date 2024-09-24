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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Continent functions from the RouteService interface.
 * 
 */
public class TestContinent extends TestBase {

  @Before
  public void setUp() throws Exception {
    initService();
  }

  @After
  public void tearDown() throws Exception {
    stopService();
  }

  @Test
  public void testGetContinents() throws RSException {
    List<Continent> cons = null;
    cons = m_rs.getContinents();
    assertNotNull(cons);
    assertFalse(cons.isEmpty());
    String data;

    List<String> targetNames = new ArrayList<String>();
    List<String> targetCodes = new ArrayList<String>();

    for (Continent c : cons) {
      data = c.getCode();
      assertNotNull(data);
      targetCodes.add(data);

      data = c.getName();
      assertNotNull(data);
      targetNames.add(data);
    }

    assertTrue(5 == targetCodes.size());
    Collections.sort(targetCodes);
    assertEquals("AFRICA", targetCodes.get(0));
    assertEquals("AMERICA", targetCodes.get(1));
    assertEquals("ASIA", targetCodes.get(2));
    assertEquals("EUROPE", targetCodes.get(3));
    assertEquals("OCEANIA", targetCodes.get(4));

    assertTrue(5 == targetNames.size());
    Collections.sort(targetNames);
    assertEquals("Africa", targetNames.get(0));
    assertEquals("America", targetNames.get(1));
    assertEquals("Asia", targetNames.get(2));
    assertEquals("Europe", targetNames.get(3));
    assertEquals("Oceania", targetNames.get(4));
  }

  @Test
  public void testFindContinent() throws RSException {
    Continent c = null;
    c = m_rs.findContinent("EUROPE");
    assertNotNull(c);
    String data;
    data = c.getCode();
    assertNotNull(data);
    assertEquals("EUROPE", data);

    data = c.getName();
    assertNotNull(data);
    assertEquals("Europe", data);
  }

  @Test
  public void testFindContinentPublish() throws RSException {
    Continent c = null;
    c = m_rs.findContinent("EUROPE");
    assertNotNull(c);
    String data;
    data = c.getCode();
    assertNotNull(data);
    assertEquals("EUROPE", data);

    data = c.getName();
    assertNotNull(data);
    assertEquals("Europe", data);

    // PublishInfoStatus stat = c.getStatus();
    // assertTrue(stat == PublishInfoStatus.INFO_UPDATE);
    //
    // List<PublishMessage> info;
    // info = c.getMessages();
    // assertTrue(info.size() > 0);
    // for (PublishMessage msg : info) {
    // System.out.println(msg);
    // data = msg.getOwnerCode();
    // assertEquals("EUROPE", data);
    //
    // data = msg.getOwnerName();
    // assertEquals("Europe", data);
    // }
  }

}
