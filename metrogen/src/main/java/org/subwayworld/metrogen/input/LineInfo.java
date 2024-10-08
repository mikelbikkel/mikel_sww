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

import org.subwayworld.metrogen.MetroException;

/**
 * Line information from the network file.
 */
public class LineInfo {

  /**
   * The name of this line.
   */
  public final String name;

  /**
   * The service type of this line.
   */
  public final String svtype;

  public final String remark;

  public final String editinfo;

  public LineInfo(String name, String svtype, String remark, String editinfo)
      throws MetroException {
    if (null == name || "".equals(name)) {
      throw new MetroException("LineInfo");
    }

    this.name = name;
    this.svtype = svtype;
    this.remark = remark;
    this.editinfo = editinfo;
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
    result = prime * result + ((name == null) ? 0 : name.hashCode());
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
    if (!(obj instanceof LineInfo)) {
      return false;
    }
    LineInfo other = (LineInfo) obj;
    if (name == null) {
      if (other.name != null) {
        return false;
      }
    } else if (!name.equals(other.name)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "LI<" + name + ": " + svtype + ">";
  }
}
