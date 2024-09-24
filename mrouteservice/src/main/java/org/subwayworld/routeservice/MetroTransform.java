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
package org.subwayworld.routeservice;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * Information about metro transformations at a station.
 */
public class MetroTransform {

  /**
   * The station where the transformation takes place.
   */
  public final Station m_station;

  /**
   * The pre and post dircode, combined into one transformation code.
   * <p>
   * Pre and post are combined using a bitwise AND.
   */
  public List<BitSet> m_transforms;

  public MetroTransform(Station stat) {
    m_station = stat;
    m_transforms = new ArrayList<BitSet>();
  }

  public void add(int idxPre, int idxPost) {
    BitSet bs = new BitSet();
    bs.set(idxPre);
    bs.set(idxPost);
    m_transforms.add(bs);
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
    result = prime * result + ((m_station == null) ? 0 : m_station.hashCode());
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
    if (!(obj instanceof MetroTransform)) {
      return false;
    }
    MetroTransform other = (MetroTransform) obj;
    if (m_station == null) {
      if (other.m_station != null) {
        return false;
      }
    } else if (!m_station.equals(other.m_station)) {
      return false;
    }
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "MetroTransform [stationCode=" + m_station + ", transforms="
        + m_transforms + "]";
  }
}
