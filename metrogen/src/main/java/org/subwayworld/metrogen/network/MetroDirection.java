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

/**
 * Represents an endpoint on a line in the metro network.<br>
 * 
 */
public class MetroDirection {

  public final String m_desc;

  public final String m_dir_id;

  public final String m_endpoint;

  public final MetroLine m_line;

  public MetroDirection(String dir_id, String desc) {
    m_dir_id = dir_id;
    m_desc = desc;
    m_endpoint = null;
    m_line = null;
  }

  public MetroDirection(String dir_id, String desc, MetroLine line,
      String endpoint) {
    m_dir_id = dir_id;
    m_desc = desc;
    m_line = line;
    m_endpoint = endpoint;
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
    result = prime * result + ((m_desc == null) ? 0 : m_desc.hashCode());
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
    if (!(obj instanceof MetroDirection)) {
      return false;
    }
    MetroDirection other = (MetroDirection) obj;
    if (m_desc == null) {
      if (other.m_desc != null) {
        return false;
      }
    } else if (!m_desc.equals(other.m_desc)) {
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
    String txt = "<DIR: >" + m_dir_id;
    if (null != m_line && null != m_endpoint) {
      txt = txt + "[" + m_line.m_desc + ", " + m_endpoint + "]";
    }
    txt = txt + ">";
    return txt;
  }

}
