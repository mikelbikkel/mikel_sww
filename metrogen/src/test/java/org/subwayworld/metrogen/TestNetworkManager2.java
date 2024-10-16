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

import junit.framework.TestCase;

import org.junit.Test;
//import org.subwayworld.metrogen.NetworkManager2;
import org.subwayworld.metrogen.network.MetroSegment;

public class TestNetworkManager2 extends TestCase {

  /**
   * 2 segments without MNN stations.
   */
  @Test
  public void testSplitMNN() {

    String[] stats = { "a", "b", "c", "d", "e" };
    MetroSegment seg = Utils.createSegment(stats, "line A towards X");

    String[] stats2 = { "e", "f" };
    MetroSegment seg2 = Utils.createSegment(stats2, "line Z towards Q");

    Set<MetroSegment> inSet = new LinkedHashSet<>();
    inSet.add(seg);
    inSet.add(seg2);

    NetworkManager2 nm = new NetworkManager2();
    Set<MetroSegment> outSet = nm.testSplitMNN(inSet);

    assertEquals(inSet, outSet);
  }

  /**
   * 1 segment with 1 MNN, 1 segment without MNN.
   */
  @Test
  public void testSplitMNN2() {

    String[] stats = { "a", "b", "c" };
    String[] nodes = { null, "x", null };
    MetroSegment seg = Utils.createMNNSegment(stats, nodes, "line A towards X");

    String[] stats2 = { "e", "f" };
    MetroSegment seg2 = Utils.createSegment(stats2, "line Z towards Q");

    Set<MetroSegment> inSet = new LinkedHashSet<>();
    inSet.add(seg);
    inSet.add(seg2);

    NetworkManager2 nm = new NetworkManager2();
    Set<MetroSegment> outSet = nm.testSplitMNN(inSet);

    String[] stats3 = { "a", "b" };
    String[] nodes3 = { null, "x" };
    MetroSegment seg3 = Utils.createMNNSegment(stats3, nodes3, "line A towards X");

    String[] stats4 = { "b", "c" };
    String[] nodes4 = { "x", null };
    MetroSegment seg4 = Utils.createMNNSegment(stats4, nodes4, "line A towards X");

    Set<MetroSegment> sollSet = new LinkedHashSet<>();
    sollSet.add(seg3);
    sollSet.add(seg4);
    sollSet.add(seg2);
    
    assertEquals(outSet, sollSet);
  }

  /**
   * 1 segment with 2 MNNs, 1 segment without MNN.
   */
  @Test
  public void testSplitMNN3() {

    String[] stats = { "a", "b", "c", "d", "e", "f", "g", "h" };
    String[] nodes = { null, "x", null, null, "y", null, "z", null };
    MetroSegment seg = Utils.createMNNSegment(stats, nodes, "line A towards X");

    String[] stats2 = { "e", "f" };
    MetroSegment seg2 = Utils.createSegment(stats2, "line Z towards Q");

    Set<MetroSegment> inSet = new LinkedHashSet<>();
    inSet.add(seg);
    inSet.add(seg2);

    NetworkManager2 nm = new NetworkManager2();
    Set<MetroSegment> outSet = nm.testSplitMNN(inSet);

    String[] stats3 = { "a", "b" };
    String[] nodes3 = { null, "x" };
    MetroSegment seg3 = Utils.createMNNSegment(stats3, nodes3, "line A towards X");

    String[] stats4 = { "b", "c", "d", "e" };
    String[] nodes4 = { "x", null, null, "y" };
    MetroSegment seg4 = Utils.createMNNSegment(stats4, nodes4, "line A towards X");

    String[] stats5 = { "e", "f", "g" };
    String[] nodes5 = { "y", null, "z" };
    MetroSegment seg5 = Utils.createMNNSegment(stats5, nodes5, "line A towards X");

    String[] stats6 = { "g", "h" };
    String[] nodes6 = { "z", null };
    MetroSegment seg6 = Utils.createMNNSegment(stats6, nodes6, "line A towards X");

    Set<MetroSegment> sollSet = new LinkedHashSet<>();
    sollSet.add(seg3);
    sollSet.add(seg4);
    sollSet.add(seg5);
    sollSet.add(seg6);
    sollSet.add(seg2);
    
    assertEquals(outSet, sollSet);
  }

}
