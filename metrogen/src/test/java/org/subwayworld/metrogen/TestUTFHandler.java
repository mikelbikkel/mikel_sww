/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.subwayworld.metrogen;

import org.junit.Test;
//import org.subwayworld.metrogen.MetroException;
import org.subwayworld.metrogen.input.UTFHandler;

import junit.framework.TestCase;

public class TestUTFHandler extends TestCase {

  @Test
  public void testDuplicateName() {
    try {
      String var1 = UTFHandler.removeAccents("Yenikapı");
      String var2 = UTFHandler.removeAccents("Yenikapi");
      assertEquals("Yenikapi", var1);
      assertEquals("Yenikapi", var2);
    } catch (MetroException e) {
      fail("MetroException: " + e.getMessage());
    }
  }
}
