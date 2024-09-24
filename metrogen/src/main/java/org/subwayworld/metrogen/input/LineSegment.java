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
package org.subwayworld.metrogen.input;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.subwayworld.metrogen.MetroException;
import org.subwayworld.metrogen.network.MetroDirection;

/**
 *
 */
public class LineSegment {

  private Set<MetroDirection> forward;

  private Set<MetroDirection> back;

  /**
   * Display names for nodes.
   */
  private List<String> m_stations; // first = from. last = to.

  public LineSegment(List<String> st) throws MetroException {
    if (null == st || st.size() < 2) {
      throw new MetroException("addStations");
    }
    m_stations = st;
    forward = new HashSet<>();
    back = new HashSet<>();
  }

  public void addForward(MetroDirection md) {
    forward.add(md);
  }

  public void addBackward(MetroDirection md) {
    back.add(md);
  }

  public Set<MetroDirection> getForwardSet() {
    return forward;
  }

  public Set<MetroDirection> getBackSet() {
    return back;
  }

  public List<String> getStations() {
    return m_stations;
  }

  @Override
  public String toString() {
    return m_stations.toString();
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((back == null) ? 0 : back.hashCode());
    result = prime * result + ((forward == null) ? 0 : forward.hashCode());
    result = prime * result
        + ((m_stations == null) ? 0 : m_stations.hashCode());
    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof LineSegment)) {
      return false;
    }
    LineSegment other = (LineSegment) obj;
    if (back == null) {
      if (other.back != null) {
        return false;
      }
    } else if (!back.equals(other.back)) {
      return false;
    }
    if (forward == null) {
      if (other.forward != null) {
        return false;
      }
    } else if (!forward.equals(other.forward)) {
      return false;
    }
    if (m_stations == null) {
      if (other.m_stations != null) {
        return false;
      }
    } else if (!m_stations.equals(other.m_stations)) {
      return false;
    }
    return true;
  }
}
