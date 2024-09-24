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

/**
 * Represents a direction for a route segment.
 * 
 */
public class SegmentDirection {

  private ServiceType m_svtype;

  private String m_line;

  private String m_endpoint;

  private String m_remark;

  private String m_dir_id;

  /**
   * Dijkstra dir_id.
   * <p>
   * Differs from m_dir_id after direction override.
   */
  public String m_dijkstra_dir_id;

  /**
   * RDMBLAAK.
   */
  public final String m_locationCode;

  public SegmentDirection() {
    this(null, null, null, null, null, null);
  }

  public SegmentDirection(ServiceType svtype, String line, String endpoint,
      String remark, String dir_id, String locationCode) {
    m_svtype = svtype;
    m_line = line;
    m_endpoint = endpoint;
    m_remark = remark;
    m_dir_id = dir_id;
    m_dijkstra_dir_id = dir_id;
    m_locationCode = locationCode;
  }

  public String getDirId() {
    return m_dir_id;
  }

  public void setDirId(String dir_id) {
    if (null == dir_id) {
      return;
    }
    m_dir_id = dir_id;
    if (null == m_dijkstra_dir_id) {
      m_dijkstra_dir_id = dir_id;
    }
  }

  public String getDijkstraDirId() {
    return m_dijkstra_dir_id;
  }

  public String getLocationCode() {
    return m_locationCode;
  }

  public ServiceType getServiceType() {
    return m_svtype;
  }

  public void setServiceType(String svtype) {
    if (null == svtype) {
      m_svtype = ServiceType.REGULAR;
    } else if (svtype.equals("PARTIAL")) {
      m_svtype = ServiceType.PARTIAL;
    } else if (svtype.equals("EXTENDED")) {
      m_svtype = ServiceType.EXTENDED;
    } else {
      m_svtype = ServiceType.REGULAR;
    }
  }

  public String getLine() {
    return m_line;
  }

  public void setLine(String e) {
    m_line = e;
  }

  public String getEndpoint() {
    return m_endpoint;
  }

  public void setEndpoint(String e) {
    m_endpoint = e;
  }

  public String getRemark() {
    return m_remark;
  }

  public void setRemark(String m) {
    m_remark = m;
  }

  @Override
  public String toString() {
    return m_dir_id + " [" + m_line + ":" + m_endpoint + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result
        + ((m_dijkstra_dir_id == null) ? 0 : m_dijkstra_dir_id.hashCode());
    result = prime * result
        + ((m_locationCode == null) ? 0 : m_locationCode.hashCode());
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
    if (!(obj instanceof SegmentDirection)) {
      return false;
    }
    SegmentDirection other = (SegmentDirection) obj;
    if (m_dijkstra_dir_id == null) {
      if (other.m_dijkstra_dir_id != null) {
        return false;
      }
    } else if (!m_dijkstra_dir_id.equals(other.m_dijkstra_dir_id)) {
      return false;
    }
    if (m_locationCode == null) {
      if (other.m_locationCode != null) {
        return false;
      }
    } else if (!m_locationCode.equals(other.m_locationCode)) {
      return false;
    }
    return true;
  }
}
