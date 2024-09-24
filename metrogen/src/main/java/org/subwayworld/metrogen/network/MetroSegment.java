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
package org.subwayworld.metrogen.network;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 */
public class MetroSegment {

  /**
   * Metro stations.
   */
  public List<MetroStation> m_stations;

  /**
   * Forward directions.
   */
  public Set<MetroDirection> m_fwd;

  /**
   * Backward directions.
   */
  public Set<MetroDirection> m_bck;

  public String m_id;

  public MetroSegment() {
    m_stations = new ArrayList<>();
    m_fwd = new HashSet<>();
    m_bck = new HashSet<>();
  }

  public MetroSegment(List<MetroStation> stations) {
    this();
    m_stations.addAll(stations);
  }

  public void addMetroStation(MetroStation ms) {
    m_stations.add(ms);
  }

  public void addForward(Set<MetroDirection> fwd) {
    m_fwd.addAll(fwd);
  }

  public void addBackward(Set<MetroDirection> bck) {
    m_bck.addAll(bck);
  }

  public void addForward(MetroDirection md) {
    m_fwd.add(md);
  }

  public void addBackward(MetroDirection md) {
    m_bck.add(md);
  }

  public boolean isForwardOnly() {
    return 0 == m_bck.size();
  }

  @Override
  public String toString() {
    if (null == m_stations) {
      return "[]";
    }
    int last = m_stations.size() - 1;
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    sb.append(m_stations.get(0).getId());
    sb.append(", ");
    sb.append(m_stations.get(last).getId());
    sb.append("]F: [");
    if (null != m_fwd) {
      for (MetroDirection md : m_fwd) {
        sb.append(md.m_dir_id);
        sb.append(" ");
      }
    }
    sb.append("]B: [");
    if (null != m_bck) {
      for (MetroDirection md : m_bck) {
        sb.append(md.m_dir_id);
        sb.append(" ");
      }
    }
    sb.append("]");
    return sb.toString();
  }

  /**
   * Up to and including s. [first, s]
   * 
   * @param idx the upper bound
   * @return the lower subsegment.
   */
  public MetroSegment getLowerSubSegment(int idx) {
    MetroSegment seg = getSegmentPart(0, idx);
    return seg;
  }

  /**
   * From and including s. [s, last]
   * 
   * @param idx the lower bound
   * @return the upper subsegment
   */
  public MetroSegment getUpperSubSegment(int idx) {
    int last = this.m_stations.size() - 1;
    MetroSegment seg = getSegmentPart(idx, last);
    return seg;
  }

  /**
   * [start, end]
   * 
   * @param idxFrom the lower bound
   * @param idxTo the upper bound
   * @return the subsegment
   */
  public MetroSegment getSubSegment(int idxFrom, int idxTo) {
    MetroSegment seg = getSegmentPart(idxFrom, idxTo);
    return seg;
  }

  private MetroSegment getSegmentPart(int idxFrom, int idxTo) {
    List<MetroStation> sub = this.m_stations.subList(idxFrom, idxTo + 1);
    MetroSegment seg = new MetroSegment(sub);
    seg.addForward(this.m_fwd);
    seg.addBackward(this.m_bck);
    return seg;
  }

  public int getNodeIndex(MetroStation stat) {
    String node = stat.getNode();
    int cnt = m_stations.size();
    MetroStation ms;
    String myNode;
    for (int i = 0; i < cnt; i++) {
      ms = m_stations.get(i);
      myNode = ms.getNode();
      if (myNode.equals(node)) {
        return i;
      }
    }
    return -1;
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
    result = prime * result + ((m_bck == null) ? 0 : m_bck.hashCode());
    result = prime * result + ((m_fwd == null) ? 0 : m_fwd.hashCode());
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
    if (!(obj instanceof MetroSegment)) {
      return false;
    }
    MetroSegment other = (MetroSegment) obj;
    if (m_bck == null) {
      if (other.m_bck != null) {
        return false;
      }
    } else if (!m_bck.equals(other.m_bck)) {
      return false;
    }
    if (m_fwd == null) {
      if (other.m_fwd != null) {
        return false;
      }
    } else if (!m_fwd.equals(other.m_fwd)) {
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
