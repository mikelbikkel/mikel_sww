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

import java.util.List;

public class RawSegment {

  public final String fwdDirection;

  public final String bckDirection;

  public List<String> stations;

  public RawSegment(String fwd, String bck, List<String> st) {
    fwdDirection = fwd;
    bckDirection = bck;
    stations = st;
  }

  public RawSegment(String fwd, List<String> st) {
    fwdDirection = fwd;
    bckDirection = null;
    stations = st;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result
        + ((bckDirection == null) ? 0 : bckDirection.hashCode());
    result = prime * result
        + ((fwdDirection == null) ? 0 : fwdDirection.hashCode());
    result = prime * result + ((stations == null) ? 0 : stations.hashCode());
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
    if (!(obj instanceof RawSegment)) {
      return false;
    }
    RawSegment other = (RawSegment) obj;
    if (bckDirection == null) {
      if (other.bckDirection != null) {
        return false;
      }
    } else if (!bckDirection.equals(other.bckDirection)) {
      return false;
    }
    if (fwdDirection == null) {
      if (other.fwdDirection != null) {
        return false;
      }
    } else if (!fwdDirection.equals(other.fwdDirection)) {
      return false;
    }
    if (stations == null) {
      if (other.stations != null) {
        return false;
      }
    } else if (!stations.equals(other.stations)) {
      return false;
    }
    return true;
  }

}
