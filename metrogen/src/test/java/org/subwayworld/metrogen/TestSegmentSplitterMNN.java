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

import org.junit.Test;
//import org.subwayworld.metrogen.MetroException;
import org.subwayworld.metrogen.network.MetroSegment;
import org.subwayworld.metrogen.split.SegmentSplitter;

import junit.framework.TestCase;

/**
 * Unit tests for SegmentSplitter.
 * 
 */
public class TestSegmentSplitterMNN extends TestCase {

  public static void main(String[] args) {
    junit.textui.TestRunner.run(TestSegmentSplitterMNN.class);
  }

  /**
   * Test t-splitsing, crossing at the MNN.
   */
  @Test
  public void testSplitCrossTypeT() {

    String[] stats = { "a", "b", "c" };
    String[] nodes = { null, "x", null };
    MetroSegment seg = Utils.createMNNSegment(stats, nodes, "line A towards X");

    String[] stats2 = { "e", "f" };
    String[] nodes2 = { null, "x" };
    MetroSegment seg2 = Utils.createMNNSegment(stats2, nodes2,
        "line B towards Y");

    SegmentSplitter split = new SegmentSplitter();
    Set<MetroSegment> segOut = null;
    try {
      segOut = split.processSegments(seg, seg2);
    } catch (MetroException e) {
      fail("MetroException: " + e.getMessage());
    }
    assertNull(segOut);
  }

}
