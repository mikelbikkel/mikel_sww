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

package org.subwayworld.routeservice.calculation;

import java.util.BitSet;

import org.subwayworld.routeservice.ILocation;

/**
 * Segment information.
 * 
 */
public class SegmentInfo {

  public enum SegType {
    UNI_DIRECTIONAL, BI_DIRECTIONAL
  };

  public SegType m_type;

  public String m_name;

  /**
   * Forward directions of this segment.
   */
  public BitSet m_forward;

  /**
   * Backward directions of this segment.
   */
  public BitSet m_back;

  /**
   * The locations on this segment, including v and w.
   */
  private ILocation[] m_locations;

  public SegmentInfo(String name, int numStats) {
    m_name = name;
    m_locations = new ILocation[numStats];
    m_forward = new BitSet();
    m_back = new BitSet();
  }

  public void addLocation(ILocation stat, int idx) {
    assert null != stat;
    assert idx >= 0;
    assert idx < m_locations.length;
    assert null == m_locations[idx];

    if (null == stat) {
      return;
    }
    m_locations[idx] = stat;
  }

  public ILocation[] getLocations() {
    return m_locations;
  }

  public int canSplit(ILocation stat) {
    // Exclude endpoints from search.
    for (int i = 1; i < m_locations.length - 1; i++) {
      if (m_locations[i].equals(stat)) {
        return i;
      }
    }
    return -1;
  }

  public SegmentInfo getLower(ILocation stat) {
    int idx = canSplit(stat);
    if (-1 == idx) {
      return null; // not supposed to get here.
    }
    SegmentInfo si = new SegmentInfo(this.m_name + "_L", idx + 1);
    si.m_type = this.m_type;
    si.m_forward = this.m_forward;
    si.m_back = this.m_back;
    for (int i = 0; i <= idx; i++) {
      si.m_locations[i] = this.m_locations[i];
    }
    return si;
  }

  public SegmentInfo getUpper(ILocation stat) {
    int idx = canSplit(stat);
    if (-1 == idx) {
      return null; // not supposed to get here.
    }
    int cnt = m_locations.length - idx;
    SegmentInfo si = new SegmentInfo(this.m_name + "_U", cnt);
    si.m_type = this.m_type;
    si.m_forward = this.m_forward;
    si.m_back = this.m_back;
    for (int i = 0; i < cnt; i++, idx++) {
      si.m_locations[i] = this.m_locations[idx];
    }
    return si;
  }

  /**
   * Returns the start location of this segment.
   * 
   * @return the start location of this segment.
   */
  public ILocation getLocationStart() {
    return m_locations[0];
  }

  /**
   * Returns the end location of this segment.
   * 
   * @return the end location of this segment.
   */
  public ILocation getLocationEnd() {
    return m_locations[m_locations.length - 1];
  }

  @Override
  public String toString() {
    return m_name + "[" + m_type + "]";
  }
}
