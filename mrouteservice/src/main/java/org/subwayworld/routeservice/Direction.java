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
 * Represents an endpoint for a metro line.
 * 
 */
public class Direction {

  private final ServiceType m_svtype;

  private final String m_line;

  private final String m_endpoint;

  private final String m_remark;

  /**
   * Index of this direction on the DirectionList.
   * <p>
   * Index into forward and backward BitSets of SegmentInfo.
   * <p>
   * First entry has index 0.
   */
  public final int m_idx;

  /**
   * Primary key.
   */
  private final String m_dir_id;

  public Direction() {
    this(-1, null, null, null, null, null);
  }

  public Direction(int idx, String svtype, String line, String endpoint,
      String remark, String dir_id) {
    m_idx = idx;
    if (null == svtype) {
      m_svtype = ServiceType.REGULAR;
    } else if (svtype.equals("PARTIAL")) {
      m_svtype = ServiceType.PARTIAL;
    } else if (svtype.equals("EXTENDED")) {
      m_svtype = ServiceType.EXTENDED;
    } else {
      m_svtype = ServiceType.REGULAR;
    }
    m_line = line;
    m_endpoint = endpoint;
    m_remark = remark;
    m_dir_id = dir_id;
  }

  public String getDirId() {
    return m_dir_id;
  }

  public ServiceType getServiceType() {
    return m_svtype;
  }

  public String getLine() {
    return m_line;
  }

  public String getEndpoint() {
    return m_endpoint;
  }

  public String getRemark() {
    return m_remark;
  }

  @Override
  public String toString() {
    return "[" + m_dir_id + ":" + m_line + "-" + m_endpoint + "[" + m_idx
        + "]]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((m_dir_id == null) ? 0 : m_dir_id.hashCode());
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
    if (!(obj instanceof Direction)) {
      return false;
    }
    Direction other = (Direction) obj;
    if (m_dir_id == null) {
      if (other.m_dir_id != null) {
        return false;
      }
    } else if (!m_dir_id.equals(other.m_dir_id)) {
      return false;
    }
    return true;
  }

}
