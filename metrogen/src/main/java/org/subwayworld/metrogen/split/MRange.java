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

import org.subwayworld.metrogen.MetroException;

/**
 * Matching range
 *
 */
public class MRange {
  public int startA;
  public int endA;
  public final int lastA;

  public int startB;
  public int endB;
  public final int lastB;

  private boolean goForward;

  MRange(int lastA, int lastB) {
    startA = -1;
    endA = -1;
    this.lastA = lastA;

    startB = -1;
    endB = -1;
    this.lastB = lastB;

    goForward = true;
  }

  public void init(int stA, int stB) {
    startA = stA;
    endA = stA;
    startB = stB;
    endB = stB;
  }

  public void setBackward() {
    goForward = false;
  }

  public boolean goesBackward() {
    return !goForward;
  }

  public void setEnd(int increase) throws MetroException {
    if (increase <= 0) {
      throw new MetroException("increase invalid");
    }

    if (startA + increase > lastA) {
      throw new MetroException("A over-run");
    }
    endA = startA + increase;

    if (goForward) {
      if (startB + increase > lastB) {
        throw new MetroException("B over-run");
      }
      endB = startB + increase;
    } else {
      if (startB - increase < 0) {
        throw new MetroException("B under-run");
      }
      endB = startB - increase;
    }
  }

  public SegmentOverlap getOverlapType() {
    if (-1 == startA) {
      return SegmentOverlap.NONE;
    }

    if (startA == endA) {
      if (isEndpointAS() && isEndpointBS()) {
        return SegmentOverlap.NONE;
      }
      return SegmentOverlap.CROSS;
    }

    if (isEndpointAS() && isEndpointAE() && isEndpointBS() && isEndpointBE()) {
      return SegmentOverlap.FULL;
    }

    return SegmentOverlap.PARTIAL;
  }

  public boolean isEndpointAS() {
    if ((0 == startA) || (lastA == startA)) {
      return true;
    }
    return false;
  }

  public boolean isEndpointAE() {
    if ((0 == endA) || (lastA == endA)) {
      return true;
    }
    return false;
  }

  public boolean isEndpointBS() {
    if ((0 == startB) || (lastB == startB)) {
      return true;
    }
    return false;
  }

  public boolean isEndpointBE() {
    if ((0 == endB) || (lastB == endB)) {
      return true;
    }
    return false;
  }

  @Override
  public String toString() {
    return "MRange [startA=" + startA + ", endA=" + endA + ", lastA=" + lastA
        + ", startB=" + startB + ", endB=" + endB + ", lastB=" + lastB
        + ", goForward=" + goForward + "]";
  }
}
