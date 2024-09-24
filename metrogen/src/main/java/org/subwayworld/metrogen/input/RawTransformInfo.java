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

public class RawTransformInfo {

  /**
   * The name of the station, as found in the network file.
   * <p>
   * For example: Centraal Station X::X 1.
   */
  public final String statCode;

  /**
   * The name of the direction when the metro arrives at the station, as found in the network file.
   * <p>
   * For example: Line E, towards F.
   */
  public final String preDirection;

  /**
   * The name of the direction when the metro departs from the station, as found in the network file.
   * <p>
   * For example: Line X, towards Y.
   */
  public final String postDirection;

  public RawTransformInfo(String scode, String predir, String postdir) {
    statCode = scode;
    preDirection = predir;
    postDirection = postdir;
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
    result = prime * result
        + ((postDirection == null) ? 0 : postDirection.hashCode());
    result = prime * result
        + ((preDirection == null) ? 0 : preDirection.hashCode());
    result = prime * result + ((statCode == null) ? 0 : statCode.hashCode());
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
    if (!(obj instanceof RawTransformInfo)) {
      return false;
    }
    RawTransformInfo other = (RawTransformInfo) obj;
    if (postDirection == null) {
      if (other.postDirection != null) {
        return false;
      }
    } else if (!postDirection.equals(other.postDirection)) {
      return false;
    }
    if (preDirection == null) {
      if (other.preDirection != null) {
        return false;
      }
    } else if (!preDirection.equals(other.preDirection)) {
      return false;
    }
    if (statCode == null) {
      if (other.statCode != null) {
        return false;
      }
    } else if (!statCode.equals(other.statCode)) {
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
    return "RTI<" + statCode + ", pre: "
        + preDirection + ", post: " + postDirection + ">";
  }
}
