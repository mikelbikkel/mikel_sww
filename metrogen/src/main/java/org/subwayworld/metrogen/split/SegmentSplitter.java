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

package org.subwayworld.metrogen.split;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import org.subwayworld.metrogen.MetroException;
import org.subwayworld.metrogen.network.MetroSegment;
import org.subwayworld.metrogen.network.MetroStation;

/**
 * Split and merge 2 segments if they overlap.
 * <p>
 * 
 */
public class SegmentSplitter {

  private MetroSegment m_segA;

  private MetroSegment m_segB;

  private Set<MetroSegment> m_segs;

  public SegmentSplitter() {
  }

  /**
   * Returns the set of processed segments.<br>
   * If segA and segB do not overlap and have not been split, returns
   * {@code null}.
   * 
   * @param segA
   *          segment A
   * @param segB
   *          segment B
   * @return the set containing the split segments, or {@code null} if segA and
   *         segB have not been split.
   * @throws MetroException
   *           if error occurs.
   */
  public Set<MetroSegment> processSegments(MetroSegment segA, MetroSegment segB)
      throws MetroException {
    logEnter("processSegments");

    if (null == segA || null == segB) {
      throw new MetroException("null Segment");
    }
    if (segA.equals(segB)) {
      return null;
    }
    m_segA = segA;
    m_segB = segB;

    logWrite("  segA:" + m_segA.toString());
    logWrite("  segB:" + m_segB.toString());

    m_segs = new HashSet<>();
    MRange range = new MRange(m_segA.m_stations.size() - 1,
        m_segB.m_stations.size() - 1);

    checkOverlap(range);
    SegmentOverlap overlap = range.getOverlapType();
    switch (overlap) {
    case NONE:
      handleNoOverlap();
      break;
    case CROSS:
      handleCross(range);
      break;
    case FULL:
      handleFullOverlap(range);
      break;
    case PARTIAL:
      handlePartialOverlap(range);
      break;
    }
    checkPostConditions(m_segA, m_segB, m_segs);

    logSet("msegs: ", m_segs);
    logExit("processSegments");

    return m_segs;
  }

  private void handlePartialOverlap(MRange range) {
    MetroSegment ls;

    logWrite("  Overlap: Partial");

    // Regular case:
    // segA: ABC. segB: BCD. m_overlap: BC. Correct for segA and segB.
    // Inverse overlap case:
    // segA: ABC. segB: CBD. m_overlap: BC. Overlap must be CB for segB.
    if (!range.isEndpointAS()) {
      ls = m_segA.getLowerSubSegment(range.startA);
      m_segs.add(ls);
    }
    if (!range.isEndpointAE()) {
      ls = m_segA.getUpperSubSegment(range.endA);
      m_segs.add(ls);
    }
    if (!range.isEndpointBS()) {
      if (range.goesBackward()) {
        ls = m_segB.getUpperSubSegment(range.startB);
      } else {
        ls = m_segB.getLowerSubSegment(range.startB);
      }
      m_segs.add(ls);
    }
    if (!range.isEndpointBE()) {
      if (range.goesBackward()) {
        ls = m_segB.getLowerSubSegment(range.endB);
      } else {
        ls = m_segB.getUpperSubSegment(range.endB);
      }
      m_segs.add(ls);
    }
    // Merge overlapping parts.
    ls = m_segA.getSubSegment(range.startA, range.endA);
    if (range.goesBackward()) {
      ls.addForward(m_segB.m_bck);
      ls.addBackward(m_segB.m_fwd);
    } else {
      ls.addForward(m_segB.m_fwd);
      ls.addBackward(m_segB.m_bck);
    }
    m_segs.add(ls);
  }

  private void handleFullOverlap(MRange range) {
    // Merge A and B in one segment that has all dircodes of A and B.
    logWrite("  Overlap: Full");
    if (range.goesBackward()) {
      m_segA.addForward(m_segB.m_bck);
      m_segA.addBackward(m_segB.m_fwd);
    } else {
      m_segA.addForward(m_segB.m_fwd);
      m_segA.addBackward(m_segB.m_bck);
    }
    m_segs.add(m_segA);
  }

  private void handleNoOverlap() {
    logWrite("  Overlap: None");
    m_segs = null;
  }

  private void handleCross(MRange range) {
    MetroSegment ls;
    MetroStation junction = m_segA.m_stations.get(range.startA);
    logWrite("  Cross: " + junction);

    if (range.isEndpointAS() && range.isEndpointBS()) {
      m_segs = null;
      return;
    }

    if (range.isEndpointAS()) {
      m_segs.add(m_segA);
    } else {
      ls = m_segA.getLowerSubSegment(range.startA);
      m_segs.add(ls);
      ls = m_segA.getUpperSubSegment(range.startA);
      m_segs.add(ls);
    }

    if (range.isEndpointBS()) {
      m_segs.add(m_segB);
    } else {
      ls = m_segB.getLowerSubSegment(range.startB);
      m_segs.add(ls);
      ls = m_segB.getUpperSubSegment(range.startB);
      m_segs.add(ls);
    }
  }

  /**
   * Checks the postconditions for splits.
   * <p>
   * Postcondition 1: if m_segs is not null, all stations on segA and segB must
   * exist in m_segs.
   * 
   * @throw MetroException if postconditions are not met.
   */
  private void checkPostConditions(MetroSegment segA, MetroSegment segB,
      Set<MetroSegment> segs) throws MetroException {
    if (null == segs) {
      return;
    }
    for (MetroStation stat : segA.m_stations) {
      stationInSplitSet(stat);
    }
    for (MetroStation stat : segB.m_stations) {
      stationInSplitSet(stat);
    }
  }

  private void stationInSplitSet(MetroStation stat) throws MetroException {
    boolean found = false;
    for (MetroSegment s : m_segs) {
      if (s.m_stations.contains(stat)) {
        found = true;
        break;
      }
    }
    if (!found) {
      throw new MetroException("Station not in splitSet: " + stat.getName());
    }
  }

  /**
   * Compare stations using their Id, not their name. Not their node name. Uf
   * you compare using the node name, MNN stations are merged and one of the MNN
   * stations disappears as part of the merge.
   * 
   * @param range
   * @throws MetroException
   */
  private void checkOverlap(MRange range) throws MetroException {
    MetroStation s;
    int j;
    boolean forwardRangeMatch;
    for (int i = 0; i <= range.lastA; i++) {
      s = m_segA.m_stations.get(i);
      j = m_segB.m_stations.indexOf(s);
      if (-1 != j) {
        range.init(i, j);
        forwardRangeMatch = findForwardRange(range);
        if (!forwardRangeMatch) {
          findReverseRange(range);
        }
        return;
      } // -1 != j
    }
    return;
  }

  /**
   * 
   * @param range
   * @return true if matching range is found, false otherwise.
   * @throws MetroException
   */
  private boolean findForwardRange(MRange range) throws MetroException {
    MetroStation myNextStation = null;
    MetroStation otherNextStation = null;

    // Check that first common station on A and B is found.
    assert m_segA.m_stations.get(range.startA).equals(
        m_segB.m_stations.get(range.startB));

    int len = 1; // start at first station after the first match.
    while (range.startA + len <= range.lastA
        && range.startB + len <= range.lastB) {
      myNextStation = m_segA.m_stations.get(range.startA + len);
      otherNextStation = m_segB.m_stations.get(range.startB + len);
      if (myNextStation.equals(otherNextStation)) {
        len++;
      } else {
        break;
      }
    }
    len--; // len starts at 1, so length of Matching range is: len - 1;
    if (len > 0) {
      range.setEnd(len);
      return true;
    }
    return false; // no matching range found.
  }

  private boolean findReverseRange(MRange range) throws MetroException {
    MetroStation myNextStation = null;
    MetroStation otherNextStation = null;

    // Check that first common station on A and B is found.
    assert m_segA.m_stations.get(range.startA).equals(
        m_segB.m_stations.get(range.startB));

    range.setBackward();

    int len = 1; // start at first station after the first match.
    while (range.startA + len <= range.lastA && range.startB - len >= 0) {
      myNextStation = m_segA.m_stations.get(range.startA + len);
      otherNextStation = m_segB.m_stations.get(range.startB - len);
      if (myNextStation.equals(otherNextStation)) {
        len++;
      } else {
        break;
      }
    }

    len--; // len starts at 1, so length of Matching range is: len - 1;
    if (len > 0) {
      range.setEnd(len);
      return true;
    }
    return false; // no matching range found.
  }

  /*
   * Logging functionality.
   */
  private static final String LOG_CLASS_NAME = "org.subwayworld.metrogen.SegmentSplitter";

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
}
