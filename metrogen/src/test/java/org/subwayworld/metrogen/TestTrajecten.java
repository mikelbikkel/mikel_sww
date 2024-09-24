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
package org.subwayworld.metrogen;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;
import org.subwayworld.metrogen.console.ConsoleWriter;
import org.subwayworld.metrogen.network.MetroNetwork;
import org.subwayworld.metrogen.network.MetroSegment;
import org.subwayworld.metrogen.network.MetroTraject;

import junit.framework.TestCase;

public class TestTrajecten extends TestCase {

  @Test
  public void testForwardBackward() {
    MetroNetwork mn = null;

    try (InputStream is = TestTrajecten.class
        .getResourceAsStream("test_01.txt")) {
      MessageWriter mw = new ConsoleWriter("res.bundles.mgen");
      MetroSettings ms = new MetroSettings();
      MetroGenerator mgen = new MetroGenerator(ms, mw);
      mn = mgen.parse(is);
    } catch (IOException | MetroException e) {
      e.printStackTrace();
      assertTrue(false);
    }
    assertNotNull(mn);
    for (MetroTraject mt : mn.traject_info) {
      System.out.println(mt);
    }
  }

  @Test
  public void testOverride() {
    MetroNetwork mn = null;

    try (InputStream is = TestTrajecten.class
        .getResourceAsStream("test_02.txt")) {
      MessageWriter mw = new ConsoleWriter("res.bundles.mgen");
      MetroSettings ms = new MetroSettings();
      MetroGenerator mgen = new MetroGenerator(ms, mw);
      mn = mgen.parse(is);
    } catch (IOException | MetroException e) {
      e.printStackTrace();
      assertTrue(false);
    }
    assertNotNull(mn);
    for (MetroTraject mt : mn.traject_info) {
      System.out.println(mt);
    }
  }

  @Test
  public void testTransformation() {
    MetroNetwork mn = null;

    try (InputStream is = TestTrajecten.class
        .getResourceAsStream("test_03.txt")) {
      MessageWriter mw = new ConsoleWriter("res.bundles.mgen");
      MetroSettings ms = new MetroSettings();
      MetroGenerator mgen = new MetroGenerator(ms, mw);
      mn = mgen.parse(is);
    } catch (IOException | MetroException e) {
      e.printStackTrace();
      assertTrue(false);
    }
    assertNotNull(mn);
    for (MetroTraject mt : mn.traject_info) {
      System.out.println(mt);
    }
    for (MetroSegment ms : mn.segs) {
      System.out.println(ms);
    }
  }
}
