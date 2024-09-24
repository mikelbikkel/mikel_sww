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

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Test;
import org.subwayworld.metrogen.input.LineInfo;
import org.subwayworld.metrogen.input.RawDirection;
import org.subwayworld.metrogen.network.MetroDirection;
import org.subwayworld.metrogen.network.MetroLine;

import junit.framework.TestCase;

public class TestLineManager2 extends TestCase {

  @Test
  public void testLineSplit() {
    RawDirection rd;

    try {
      rd = new RawDirection("Lijn E richting Slinge ");
      assertEquals("Lijn E", rd.line);
      assertEquals("Slinge", rd.endpoint);

      rd = new RawDirection("Lijn E towards Slinge ");
      assertEquals("Lijn E", rd.line);
      assertEquals("Slinge", rd.endpoint);

      rd = new RawDirection("Lijn E, richting Slinge ");
      assertEquals("Lijn E", rd.line);
      assertEquals("Slinge", rd.endpoint);

      rd = new RawDirection("Lijn E, towards Slinge ");
      assertEquals("Lijn E", rd.line);
      assertEquals("Slinge", rd.endpoint);

      rd = new RawDirection("Lijn E y Slinge ");
      assertEquals("Lijn E", rd.line);
      assertEquals("Slinge", rd.endpoint);
    } catch (MetroException e) {
      fail("MetroException: " + e.getMessage());
    }
  }

  @Test
  public void testProcessLines() throws MetroException {
    String[] dirnames = { "Lijn E richting Slinge ",
        " Lijn E richting Oudewater", "Lijn F richting Bierkelder",
        "Lijn F richting Slinge" };

    Set<RawDirection> dirs = new LinkedHashSet<>();
    for (String s : dirnames) {
      RawDirection rd = new RawDirection(s);
      dirs.add(rd);
    }
    LineManager2 lm = new LineManager2("RDM", dirs, null);

    Set<MetroLine> lines = lm.getLines();

    assertEquals(2, lines.size());

    int[] fnd = { 0, 0 };

    for (MetroLine ln : lines) {
      if (ln.m_desc.equals("Lijn E")) {
        assertEquals("REGULAR", ln.m_svtype);
        assertNull(ln.m_editinfo);
        assertNull(ln.m_remark);
        fnd[0]++;
      } else if (ln.m_desc.equals("Lijn F")) {
        assertEquals("REGULAR", ln.m_svtype);
        assertNull(ln.m_editinfo);
        assertNull(ln.m_remark);
        fnd[1]++;
      }
    }
    assertEquals(1, fnd[0]);
    assertEquals(1, fnd[1]);

    Set<MetroDirection> endpoints = lm.getDirections();

    assertEquals(dirnames.length, endpoints.size());

    int[] fnd2 = { 0, 0, 0, 0 };
    for (MetroDirection ep : endpoints) {
      if (ep.m_endpoint.equals("Slinge") && ep.m_line.m_desc.equals("Lijn E")) {
        fnd2[0]++;
      } else if (ep.m_endpoint.equals("Slinge")
          && ep.m_line.m_desc.equals("Lijn F")) {
        fnd2[1]++;
      } else if (ep.m_endpoint.equals("Oudewater")) {
        assertEquals("Lijn E", ep.m_line.m_desc);
        fnd2[2]++;
      } else if (ep.m_endpoint.equals("Bierkelder")) {
        assertEquals("Lijn F", ep.m_line.m_desc);
        fnd2[3]++;
      }
    }
    assertEquals(1, fnd2[0]);
    assertEquals(1, fnd2[1]);
    assertEquals(1, fnd2[2]);
    assertEquals(1, fnd2[3]);
  }

  @Test
  public void testLineInfo() throws MetroException {
    String[] dirnames = { "Lijn E richting Slinge ",
        " Lijn E richting Oudewater", "Lijn F richting Bierkelder",
        "Lijn F richting Slinge" };

    LineInfo li = new LineInfo("Lijn E", "EXTENDED", "remark", "editinfo");
    Set<LineInfo> sli = new LinkedHashSet<>();
    sli.add(li);

    Set<RawDirection> dirs = new LinkedHashSet<>();
    for (String s : dirnames) {
      RawDirection rd = new RawDirection(s);
      dirs.add(rd);
    }
    LineManager2 lm = new LineManager2("RDM", dirs, sli);

    Set<MetroLine> lines = lm.getLines();

    assertEquals(2, lines.size());

    int[] fnd = { 0, 0 };

    for (MetroLine ln : lines) {
      if (ln.m_desc.equals("Lijn E")) {
        assertEquals("EXTENDED", ln.m_svtype);
        assertEquals("editinfo", ln.m_editinfo);
        assertEquals("remark", ln.m_remark);
        fnd[0]++;
      } else if (ln.m_desc.equals("Lijn F")) {
        assertEquals("REGULAR", ln.m_svtype);
        assertNull(ln.m_editinfo);
        assertNull(ln.m_remark);
        fnd[1]++;
      }
    }
    assertEquals(1, fnd[0]);
    assertEquals(1, fnd[1]);

    Set<MetroDirection> endpoints = lm.getDirections();

    assertEquals(dirnames.length, endpoints.size());

    int[] fnd2 = { 0, 0, 0, 0 };
    for (MetroDirection ep : endpoints) {
      if (ep.m_endpoint.equals("Slinge") && ep.m_line.m_desc.equals("Lijn E")) {
        fnd2[0]++;
      } else if (ep.m_endpoint.equals("Slinge")
          && ep.m_line.m_desc.equals("Lijn F")) {
        fnd2[1]++;
      } else if (ep.m_endpoint.equals("Oudewater")) {
        assertEquals("Lijn E", ep.m_line.m_desc);
        fnd2[2]++;
      } else if (ep.m_endpoint.equals("Bierkelder")) {
        assertEquals("Lijn F", ep.m_line.m_desc);
        fnd2[3]++;
      }
    }
    assertEquals(1, fnd2[0]);
    assertEquals(1, fnd2[1]);
    assertEquals(1, fnd2[2]);
    assertEquals(1, fnd2[3]);
  }

  @Test
  public void testFindLineNull() throws MetroException {
    String[] dirnames = { "Lijn E richting Slinge ",
        " Lijn E richting Oudewater", "Lijn F richting Bierkelder",
        "Lijn F richting Slinge" };

    Set<RawDirection> dirs = new LinkedHashSet<>();
    for (String s : dirnames) {
      RawDirection rd = new RawDirection(s);
      dirs.add(rd);
    }
    LineManager2 lm = new LineManager2("RDM", dirs, null);
    try {
    lm.findLine(null);
    fail("null parameter accepted");
    } catch(NullPointerException e) {
      assertNotNull(e);
    }
  }

  @Test
  public void testFindDirection() {
    assertTrue(true);
  }

  @Test
  public void testFindDirectionNull() throws MetroException {
    String[] dirnames = { "Lijn E richting Slinge ",
        " Lijn E richting Oudewater", "Lijn F richting Bierkelder",
        "Lijn F richting Slinge" };

    Set<RawDirection> dirs = new LinkedHashSet<>();
    for (String s : dirnames) {
      RawDirection rd = new RawDirection(s);
      dirs.add(rd);
    }
    LineManager2 lm = new LineManager2("RDM", dirs, null);
    try {
    lm.findDirection(null);
    fail("null parameter accepted");
    } catch(NullPointerException e) {
      assertNotNull(e);
    }
  }


}
