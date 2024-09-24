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
 * Represents a line in the metro network.
 * 
 */
public class MetroLine {

  /**
   * Default service type for a line.
   */
  private static final String DEFAULT_SVTYPE = "REGULAR";

  /**
   * Description of the line.<br>
   * For example: Erasmus lijn.
   * <p>
   * The description must be a unique identifer for the line.
   */
  public final String m_desc;

  /**
   * Service type.
   */
  public String m_svtype;

  /**
   * SWW identifier for the line.<br>
   * For example: PRG_LINE_2.
   * <p>
   * The id is an internal SWW identifier for a line.
   */
  public final String m_line_id;

  /**
   * Remarks for this line.
   */
  public String m_remark;

  /**
   * Editor information for this line.
   */
  public String m_editinfo;

  public MetroLine(String line_id, String desc) {
    m_line_id = line_id;
    m_desc = desc;
    m_svtype = DEFAULT_SVTYPE;
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
    if (!(obj instanceof MetroLine)) {
      return false;
    }
    MetroLine other = (MetroLine) obj;
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
    return m_desc;
  }
}
