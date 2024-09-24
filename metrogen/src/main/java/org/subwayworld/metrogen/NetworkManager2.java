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

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.logging.Logger;

import org.subwayworld.metrogen.network.MetroSegment;
import org.subwayworld.metrogen.split.SegmentSplitter;

public class NetworkManager2 {

  Set<MetroSegment> m_graph;

  public NetworkManager2() {
    m_graph = Collections.emptySet();
  }

  public NetworkManager2(Set<MetroSegment> segs, String cityCode)
      throws MetroException {
    if (null == segs || null == cityCode) {
      throw new NullPointerException();
    }
    Set<MetroSegment> res1 = splitAndMerge2(segs);
    Set<MetroSegment> res2 = splitMNN(res1);
    assignSegmentId(res2, cityCode);

    m_graph = res2;
  }

  private void assignSegmentId(Set<MetroSegment> segs, String cityCode) {
    int i = 0;
    for (MetroSegment ms : segs) {
      ms.m_id = cityCode + "_SEG_" + i;
      i++;
    }
  }

  /**
   * Scaffolding function to test <code>splitMNN</code>.<br>
   * 
   * @param setIn
   *          input set.
   * @return output set.
   */
  public Set<MetroSegment> testSplitMNN(Set<MetroSegment> setIn) {
    return splitMNN(setIn);
  }

  /**
   * Splits segments that contain a MNN.
   * 
   */
  private Set<MetroSegment> splitMNN(Set<MetroSegment> setIn) {
    Set<MetroSegment> setOut = new LinkedHashSet<>();
    MetroSegment lower;
    MetroSegment upper = null;
    boolean isSplit;
    for (MetroSegment seg : setIn) {
      isSplit = false;
      upper = null;
      for (int i = 1, start = 0; i < seg.m_stations.size() - 1; i++) {
        if (seg.m_stations.get(i).isMNN()) {
          // Add part between start and MNN to output set.
          lower = seg.getSubSegment(start, i);
          setOut.add(lower);
          // Remember the part above the MNN.
          upper = seg.getUpperSubSegment(i);

          isSplit = true;
          start = i; // Remember starting position of next subsegment.
        }
      }
      if (isSplit) {
        if (null != upper) {
          setOut.add(upper);
        }
      } else {
        setOut.add(seg);
      }
    }
    return setOut;
  }

  private Set<MetroSegment> splitAndMerge2(Set<MetroSegment> segs)
      throws MetroException {
    Queue<MetroSegment> worklist = new LinkedList<>();
    Set<MetroSegment> finalSet = new LinkedHashSet<>();

    logEnter("splitAndMerge2");

    worklist.addAll(segs);
    segs = null; // Safety.

    SegmentSplitter splitter = new SegmentSplitter();

    MetroSegment root;
    Set<MetroSegment> splitSet = null;
    logQueue("entry worklist: ", worklist);
    logSet("entry finalset: ", finalSet);

    MetroSegment other;
    while (!worklist.isEmpty()) {
      logWrite("Start of loop");
      root = worklist.remove();
      splitSet = null;
      Iterator<MetroSegment> itr = worklist.iterator();
      while (itr.hasNext()) {
        other = itr.next();
        splitSet = splitter.processSegments(root, other);
        if (null != splitSet) {
          itr.remove(); // Remove other from the worklist.
          break; // break out of the iteration.
        }
      } // end for
      if (null != splitSet) {
        // The root has splitted the other segment.
        // Add all parts to the worklist.
        worklist.addAll(splitSet);
      } else {
        // The root has been matched against all other segments.
        // And the root intersected with NONE of them.
        // Move the root to the final set.
        finalSet.add(root);
        logWrite("move to finalSet: " + root.toString());
      }
      logQueue("loop worklist: ", worklist);
      logSet("loop finalset: ", finalSet);
    } // while worklist not empty.

    logQueue("return worklist: ", worklist);
    logSet("return finalset: ", finalSet);
    logExit("splitAndMerge2");

    return finalSet;
  }

  public Set<MetroSegment> getGraph() {
    return m_graph;
  }

  /*
   * Logging functionality.
   */
  private static final String LOG_CLASS_NAME = "org.subwayworld.metrogen.NetworkManager2";

  static Logger log = Logger.getLogger(LOG_CLASS_NAME);

  private void logEnter(String name) {
    if (log.isLoggable(java.util.logging.Level.FINER)) {
      log.finer("ENTRY: " + name);
    }
  }

  private void logExit(String name) {
    if (log.isLoggable(java.util.logging.Level.FINER)) {
      log.finer("RETURN: " + name);
    }
  }

  private void logWrite(String msg) {
    if (log.isLoggable(java.util.logging.Level.FINER)) {
      log.finer(msg);
    }
  }

  private void logSet(String title, Set<MetroSegment> mset) {
    if (log.isLoggable(java.util.logging.Level.FINER)) {
      log.finer(title);
      if (null == mset || mset.isEmpty()) {
        log.finer("  Empty.");
        return;
      }
      for (MetroSegment ms : mset) {
        log.finer("  " + ms);
      }
    }
  }

  private void logQueue(String title, Queue<MetroSegment> mset) {
    if (log.isLoggable(java.util.logging.Level.FINER)) {
      log.finer(title);
      if (null == mset || mset.isEmpty()) {
        log.finer("  Empty.");
        return;
      }
      for (MetroSegment ms : mset) {
        log.finer("  " + ms);
      }
    }
  }
}
