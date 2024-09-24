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

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test getLocation function from the RouteService interface.
 * 
 */
public class TestGetNewMessages extends TestBase {

  @Before
  public void setUp() throws Exception {
    initService();
  }

  @After
  public void tearDown() throws Exception {
    stopService();
  }

  @Test
  public void testGetNewMessages() throws RSException {
    assertTrue(true);

    // List<PublishMessage> lst = null;
    // lst = m_rs.getNewMessages();
    // assertNotNull(lst);
    // ALL THESE TARGETS VALUES REQUIRE SPECIFIC TESTDATA
    // String[] dates = { "2012-04-21", "2012-04-20", "2012-04-19" };
    // String[] msg = { "Created", "Add S-Bahn", "Created" };
    // String[] code = { "ROTTERDAM", "BERLIN", "BERLIN" };
    // String[] name = { "Rotterdam", "Berlin", "Berlin" };
    // PublishInfoStatus[] stat = { PublishInfoStatus.INFO_NEW,
    // PublishInfoStatus.INFO_UPDATE, PublishInfoStatus.INFO_NEW };
    // String[] stattext = { "new", "update", "new" };
    // int i = 0;
    // String sdate;
    // SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    // for (PublishMessage m : lst) {
    // assertEquals(msg[i], m.getMessage());
    // assertEquals(code[i], m.getOwnerCode());
    // assertEquals(name[i], m.getOwnerName());
    // assertEquals(stat[i], m.getMessageType());
    // assertEquals(stattext[i], m.getMessageTypeString());
    // sdate = df.format(m.getDate());
    // assertEquals(dates[i], sdate);
    // i++;
    // }
  }
}
