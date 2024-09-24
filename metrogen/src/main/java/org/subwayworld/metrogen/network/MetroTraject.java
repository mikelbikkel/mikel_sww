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
import java.util.Collections;
import java.util.List;

/**
 * Metro traject: a list of stations with 1 forward direction.
 */
public class MetroTraject {

  /**
   * Metro stations.
   */
  public List<MetroStation> m_stations;

  /**
   * The direction.
   */
  public MetroDirection m_fwd;

  public String m_id;

  public final boolean isForwardInFile;

  public MetroTraject(boolean fwd, List<MetroStation> stations) {
    isForwardInFile = fwd;

    m_stations = new ArrayList<>();
    m_stations.addAll(stations);
    if (!isForwardInFile) {
      Collections.reverse(m_stations);
    }
  }

  public void setForward(MetroDirection fwd) {
    m_fwd = fwd;
  }

  public String printEndpoints() {
    if (null == m_stations) {
      return "[]";
    }
    int last = m_stations.size() - 1;
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    sb.append(m_stations.get(0));
    sb.append(", ");
    sb.append(m_stations.get(last));
    sb.append("]");
    return sb.toString();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((m_fwd == null) ? 0 : m_fwd.hashCode());
    result = prime * result
        + ((m_stations == null) ? 0 : m_stations.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof MetroTraject)) {
      return false;
    }
    MetroTraject other = (MetroTraject) obj;
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
        sb.append(m_fwd.m_dir_id);
    }
    sb.append("]");
    return sb.toString();
  }
}
