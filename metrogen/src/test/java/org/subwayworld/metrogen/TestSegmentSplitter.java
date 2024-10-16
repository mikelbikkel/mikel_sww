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

import java.util.Set;

import junit.framework.TestCase;

import org.junit.Test;
//import org.subwayworld.metrogen.MetroException;
import org.subwayworld.metrogen.network.MetroDirection;
import org.subwayworld.metrogen.network.MetroSegment;
import org.subwayworld.metrogen.split.SegmentSplitter;

/**
 * Unit tests for SegmentSplitter.
 * 
 */
public class TestSegmentSplitter extends TestCase {

  public static void main(String[] args) {
    junit.textui.TestRunner.run(TestSegmentSplitter.class);
  }

  /**
   * Test crossing segments
   */
  @Test
  public void testSplitCrossTypeT() {
    String[] st = { "A", "B", "C", "D", "E" };
    MetroDirection md01 = new MetroDirection("01", "01");
    MetroDirection md02 = new MetroDirection("02", "02");
    MetroDirection md03 = new MetroDirection("03", "03");
    MetroDirection md04 = new MetroDirection("04", "04");

    MetroSegment ls = Utils.createSegment(st, md01, md02);

    String[] st2 = { "C", "F", "G" };
    MetroSegment ls2 = Utils.createSegment(st2, md03, md04);

    SegmentSplitter split = new SegmentSplitter();
    Set<MetroSegment> segOut = null;
    try {
      segOut = split.processSegments(ls, ls2);
    } catch (MetroException e) {
      fail("MetroException: " + e.getMessage());
    }
    assertNotNull(segOut);
    assertEquals(3, segOut.size());

    boolean fnd;
    String[] st3 = { "A", "B", "C" };
    MetroSegment ls3 = Utils.createSegment(st3, md01, md02);
    fnd = segOut.contains(ls3);
    assertTrue(fnd);

    String[] st4 = { "C", "D", "E" };
    MetroSegment ls4 = Utils.createSegment(st4, md01, md02);
    fnd = segOut.contains(ls4);
    assertTrue(fnd);

    String[] st5 = { "C", "F", "G" };
    MetroSegment ls5 = Utils.createSegment(st5, md03, md04);
    fnd = segOut.contains(ls5);
    assertTrue(fnd);
  }

  /**
   * Test crossing segments
   */
  @Test
  public void testSplitCrossTypeX() {
    String[] st = { "A", "B", "C", "D", "E" };
    MetroDirection md01 = new MetroDirection("01", "01");
    MetroDirection md02 = new MetroDirection("02", "02");
    MetroDirection md03 = new MetroDirection("03", "03");
    MetroDirection md04 = new MetroDirection("04", "04");
    MetroSegment ls = Utils.createSegment(st, md01, md02);

    String[] st2 = { "I", "J", "C", "F", "G" };
    MetroSegment ls2 = Utils.createSegment(st2, md03, md04);

    SegmentSplitter split = new SegmentSplitter();
    Set<MetroSegment> segOut = null;
    try {
      segOut = split.processSegments(ls, ls2);
    } catch (MetroException e) {
      fail("MetroException: " + e.getMessage());
    }
    assertNotNull(segOut);
    assertEquals(4, segOut.size());

    boolean fnd;
    String[] st3 = { "A", "B", "C" };
    MetroSegment ls3 = Utils.createSegment(st3, md01, md02);
    fnd = segOut.contains(ls3);
    assertTrue(fnd);

    String[] st4 = { "C", "D", "E" };
    MetroSegment ls4 = Utils.createSegment(st4, md01, md02);
    fnd = segOut.contains(ls4);
    assertTrue(fnd);

    String[] st5 = { "C", "F", "G" };
    MetroSegment ls5 = Utils.createSegment(st5, md03, md04);
    fnd = segOut.contains(ls5);
    assertTrue(fnd);

    String[] st6 = { "I", "J", "C" };
    MetroSegment ls6 = Utils.createSegment(st6, md03, md04);
    fnd = segOut.contains(ls6);
    assertTrue(fnd);
  }

  /**
   * Test segments that have a common endpoint
   */
  @Test
  public void testSplitCrossEndPoints() {
    MetroDirection md01 = new MetroDirection("01", "01");
    MetroDirection md02 = new MetroDirection("02", "02");
    MetroDirection md03 = new MetroDirection("03", "03");
    MetroDirection md04 = new MetroDirection("04", "04");

    String[] st = { "A", "B", "C" };
    MetroSegment ls = Utils.createSegment(st, md01, md02);

    String[] st2 = { "C", "F", "G" };
    MetroSegment ls2 = Utils.createSegment(st2, md03, md04);

    SegmentSplitter split = new SegmentSplitter();
    Set<MetroSegment> segOut = null;
    try {
      segOut = split.processSegments(ls, ls2);
    } catch (MetroException e) {
      fail("MetroException: " + e.getMessage());
    }
    assertNull(segOut);
  }

  /**
   * Duplicate segment.
   */
  @Test
  public void testSplitSelf() {
    MetroDirection md01 = new MetroDirection("01", "01");
    MetroDirection md02 = new MetroDirection("02", "02");

    String[] st = { "A", "B", "C" };
    MetroSegment ls = Utils.createSegment(st, md01, md02);

    String[] st2 = { "A", "B", "C" };
    MetroSegment ls2 = Utils.createSegment(st2, md01, md02);

    SegmentSplitter split = new SegmentSplitter();
    Set<MetroSegment> segOut = null;
    try {
      segOut = split.processSegments(ls, ls2);
    } catch (MetroException e) {
      fail("MetroException: " + e.getMessage());
    }
    assertNull(segOut);
  }

  /**
   * Test segment split itself
   */
  @Test
  public void testSplitSelf2() {
    MetroDirection md01 = new MetroDirection("01", "01");
    MetroDirection md02 = new MetroDirection("02", "02");

    String[] st = { "A", "B", "C" };
    MetroSegment ls = Utils.createSegment(st, md01, md02);

    SegmentSplitter split = new SegmentSplitter();
    Set<MetroSegment> segOut = null;
    try {
      segOut = split.processSegments(ls, ls);
    } catch (MetroException e) {
      fail("MetroException: " + e.getMessage());
    }
    assertNull(segOut);
  }

  /**
   * Test segments without overlap
   */
  @Test
  public void testSplitNoOverlap() {
    MetroDirection md01 = new MetroDirection("01", "01");
    MetroDirection md02 = new MetroDirection("02", "02");

    String[] st = { "A", "B", "C", "D", "E" };
    MetroSegment ls = Utils.createSegment(st, md01, md02);

    String[] st2 = { "F", "G" };
    MetroSegment ls2 = Utils.createSegment(st2, md01, md02);

    SegmentSplitter split = new SegmentSplitter();
    Set<MetroSegment> segOut = null;
    try {
      segOut = split.processSegments(ls, ls2);
    } catch (MetroException e) {
      fail("MetroException: " + e.getMessage());
    }
    assertNull(segOut);
  }

  /**
   * Test 2 segments with same stations and different directions.
   */
  @Test
  public void testSplitSameStations() {
    MetroDirection md01 = new MetroDirection("01", "01");
    MetroDirection md02 = new MetroDirection("02", "02");
    MetroDirection md03 = new MetroDirection("03", "03");
    MetroDirection md04 = new MetroDirection("04", "04");

    String[] st = { "A", "B", "C" };
    MetroSegment ls = Utils.createSegment(st, md01, md02);

    MetroSegment ls2 = Utils.createSegment(st, md03, md04);

    SegmentSplitter split = new SegmentSplitter();
    Set<MetroSegment> segOut = null;
    try {
      segOut = split.processSegments(ls, ls2);
    } catch (MetroException e) {
      fail("MetroException: " + e.getMessage());
    }
    assertNotNull(segOut);
    assertEquals(1, segOut.size());

    boolean fnd;
    MetroSegment ls3 = Utils.createSegment(st);
    ls3.m_fwd.add(md01);
    ls3.m_fwd.add(md03);
    ls3.m_bck.add(md02);
    ls3.m_bck.add(md04);
    fnd = segOut.contains(ls3);
    assertTrue(fnd);
  }

  /**
   * Test 2 segments with same stations and different directions.
   */
  @Test
  public void testSplitSameStationsOneDir() {
    MetroDirection md01 = new MetroDirection("01", "01");
    MetroDirection md02 = new MetroDirection("02", "02");
    MetroDirection md03 = new MetroDirection("03", "03");

    String[] st = { "A", "B", "C" };
    MetroSegment ls = Utils.createSegment(st, md01, md02);

    MetroSegment ls2 = Utils.createSegment(st);
    ls2.m_fwd.add(md03);

    SegmentSplitter split = new SegmentSplitter();
    Set<MetroSegment> segOut = null;
    try {
      segOut = split.processSegments(ls, ls2);
    } catch (MetroException e) {
      fail("MetroException: " + e.getMessage());
    }
    assertNotNull(segOut);
    assertEquals(1, segOut.size());

    boolean fnd;
    MetroSegment ls3 = Utils.createSegment(st);
    ls3.m_fwd.add(md01);
    ls3.m_fwd.add(md03);
    ls3.m_bck.add(md02);

    fnd = segOut.contains(ls3);
    assertTrue(fnd);
  }

  /**
   * Test 2 segments with same endpoints and different directions.
   */
  @Test
  public void testSplitSameStartEnd() {
    MetroDirection md01 = new MetroDirection("01", "01");
    MetroDirection md02 = new MetroDirection("02", "02");
    MetroDirection md03 = new MetroDirection("03", "03");
    MetroDirection md04 = new MetroDirection("04", "04");

    String[] st = { "A", "B", "C" };
    MetroSegment ls = Utils.createSegment(st, md01, md02);

    String[] st2 = { "A", "D", "C" };
    MetroSegment ls2 = Utils.createSegment(st2, md03, md04);

    SegmentSplitter split = new SegmentSplitter();
    Set<MetroSegment> segOut = null;
    try {
      segOut = split.processSegments(ls, ls2);
    } catch (MetroException e) {
      fail("MetroException: " + e.getMessage());
    }
    assertNull(segOut);
  }

  /**
   * Test partial overlap at start
   */
  @Test
  public void testSplitPartialStart() {
    MetroDirection md01 = new MetroDirection("01", "01");
    MetroDirection md02 = new MetroDirection("02", "02");
    MetroDirection md03 = new MetroDirection("03", "03");
    MetroDirection md04 = new MetroDirection("04", "04");

    String[] st = { "A", "B", "C", "D", "E" };
    MetroSegment ls = Utils.createSegment(st, md01, md02);

    String[] st2 = { "A", "B", "C" };
    MetroSegment ls2 = Utils.createSegment(st2, md03, md04);

    SegmentSplitter split = new SegmentSplitter();
    Set<MetroSegment> segOut = null;
    try {
      segOut = split.processSegments(ls, ls2);
    } catch (MetroException e) {
      fail("MetroException: " + e.getMessage());
    }
    assertNotNull(segOut);
    assertEquals(2, segOut.size());

    boolean fnd;
    String[] st3 = { "A", "B", "C" };
    MetroSegment ls3 = Utils.createSegment(st3);
    ls3.m_fwd.add(md01);
    ls3.m_fwd.add(md03);
    ls3.m_bck.add(md02);
    ls3.m_bck.add(md04);
    fnd = segOut.contains(ls3);
    assertTrue(fnd);

    String[] st4 = { "C", "D", "E" };
    MetroSegment ls4 = Utils.createSegment(st4, md01, md02);
    fnd = segOut.contains(ls4);
    assertTrue(fnd);
  }

  /**
   * Test partial overlap at end
   */
  @Test
  public void testSplitPartialEnd() {
    MetroDirection md01 = new MetroDirection("01", "01");
    MetroDirection md02 = new MetroDirection("02", "02");
    MetroDirection md03 = new MetroDirection("03", "03");
    MetroDirection md04 = new MetroDirection("04", "04");

    String[] st = { "A", "B", "C", "D", "E" };
    MetroSegment ls = Utils.createSegment(st, md01, md02);

    String[] st2 = { "C", "D", "E" };
    MetroSegment ls2 = Utils.createSegment(st2, md03, md04);

    SegmentSplitter split = new SegmentSplitter();
    Set<MetroSegment> segOut = null;
    try {
      segOut = split.processSegments(ls, ls2);
    } catch (MetroException e) {
      fail("MetroException: " + e.getMessage());
    }
    assertNotNull(segOut);
    assertEquals(2, segOut.size());

    boolean fnd;
    String[] st3 = { "A", "B", "C" };
    MetroSegment ls3 = Utils.createSegment(st3, md01, md02);
    fnd = segOut.contains(ls3);
    assertTrue(fnd);

    String[] st4 = { "C", "D", "E" };
    MetroSegment ls4 = Utils.createSegment(st4);
    ls4.m_fwd.add(md01);
    ls4.m_fwd.add(md03);
    ls4.m_bck.add(md02);
    ls4.m_bck.add(md04);
    fnd = segOut.contains(ls4);
    assertTrue(fnd);
  }

  /**
   * Test partial overlap in the middle
   */
  @Test
  public void testSplitPartialMiddle() {
    MetroDirection md01 = new MetroDirection("01", "01");
    MetroDirection md02 = new MetroDirection("02", "02");
    MetroDirection md03 = new MetroDirection("03", "03");
    MetroDirection md04 = new MetroDirection("04", "04");

    String[] st = { "A", "B", "C", "D", "E" };
    MetroSegment ls = Utils.createSegment(st, md01, md02);

    String[] st2 = { "C", "D" };
    MetroSegment ls2 = Utils.createSegment(st2, md03, md04);

    SegmentSplitter split = new SegmentSplitter();
    Set<MetroSegment> segOut = null;
    try {
      segOut = split.processSegments(ls, ls2);
    } catch (MetroException e) {
      fail("MetroException: " + e.getMessage());
    }
    assertNotNull(segOut);
    assertEquals(3, segOut.size());

    boolean fnd;
    String[] st3 = { "A", "B", "C" };
    MetroSegment ls3 = Utils.createSegment(st3, md01, md02);
    fnd = segOut.contains(ls3);
    assertTrue(fnd);

    String[] st4 = { "C", "D" };
    MetroSegment ls4 = Utils.createSegment(st4);
    ls4.m_fwd.add(md01);
    ls4.m_fwd.add(md03);
    ls4.m_bck.add(md02);
    ls4.m_bck.add(md04);
    fnd = segOut.contains(ls4);
    assertTrue(fnd);

    String[] st5 = { "D", "E" };
    MetroSegment ls5 = Utils.createSegment(st5, md01, md02);
    fnd = segOut.contains(ls5);
    assertTrue(fnd);
  }

  /**
   * Test full overlap of inverse segments.
   */
  @Test
  public void testSplitInverseFull() {
    MetroDirection md01 = new MetroDirection("01", "01");
    MetroDirection md02 = new MetroDirection("02", "02");

    String[] st = { "A", "B", "C" };
    MetroSegment ls = Utils.createSegment(st, md01, md02);

    String[] st2 = { "C", "B", "A" };
    MetroSegment ls2 = Utils.createSegment(st2, md02, md01);

    SegmentSplitter split = new SegmentSplitter();
    Set<MetroSegment> segOut = null;
    try {
      segOut = split.processSegments(ls, ls2);
    } catch (MetroException e) {
      fail("MetroException: " + e.getMessage());
    }
    assertNotNull(segOut);
    assertEquals(1, segOut.size());

    boolean fnd;
    String[] st3 = { "A", "B", "C" };
    MetroSegment ls3 = Utils.createSegment(st3, md01, md02);
    fnd = segOut.contains(ls3);
    assertTrue(fnd);
  }

  /**
   * Test partial overlap of inverse segments.
   */
  @Test
  public void testSplitInversePartial() {
    MetroDirection md01 = new MetroDirection("01", "01");
    MetroDirection md02 = new MetroDirection("02", "02");
    MetroDirection md03 = new MetroDirection("03", "03");
    MetroDirection md04 = new MetroDirection("04", "04");

    String[] st = { "A", "B", "C" };
    MetroSegment ls = Utils.createSegment(st, md01, md02);

    String[] st2 = { "C", "B", "D" };
    MetroSegment ls2 = Utils.createSegment(st2, md03, md04);

    SegmentSplitter split = new SegmentSplitter();
    Set<MetroSegment> segOut = null;
    try {
      segOut = split.processSegments(ls, ls2);
    } catch (MetroException e) {
      fail("MetroException: " + e.getMessage());
    }
    assertNotNull(segOut);
    assertEquals(3, segOut.size());

    boolean fnd;
    String[] st3 = { "A", "B" };
    MetroSegment ls3 = Utils.createSegment(st3, md01, md02);
    fnd = segOut.contains(ls3);
    assertTrue(fnd);

    String[] st4 = { "B", "C" };
    MetroSegment ls4 = Utils.createSegment(st4);
    ls4.m_fwd.add(md01);
    ls4.m_fwd.add(md04);
    ls4.m_bck.add(md02);
    ls4.m_bck.add(md03);
    fnd = segOut.contains(ls4);
    assertTrue(fnd);

    String[] st5 = { "B", "D" };
    MetroSegment ls5 = Utils.createSegment(st5, md03, md04);
    fnd = segOut.contains(ls5);
    assertTrue(fnd);
  }

}
