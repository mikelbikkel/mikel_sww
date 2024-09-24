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

public class RawDirection {

  /**
   * Example: Line E towards F.
   */
  public final String direction;

  /**
   * Example: Line E.
   */
  public final String line;

  /**
   * Example: F.
   */
  public final String endpoint;

  public RawDirection(String dir) throws MetroException {
    if (null == dir || "".equals(dir)) {
      throw new MetroException("RawDirection");
    }

    String[] res = splitDirection(dir);

    if (null == res || 2 != res.length) {
      throw new MetroException("RawDirection - line endpoint error: " + dir);
    }
    if (null == res[0] || "".equals(res[0])) {
      throw new MetroException("RawDirection - line error: " + dir);
    }
    if (null == res[1] || "".equals(res[1])) {
      throw new MetroException("RawDirection - endpoint error: " + dir);
    }

    direction = dir;
    line = res[0];
    endpoint = res[1];
  }

  private String[] splitDirection(String dir) {
    String[] res;
    String[] splitters = { ", richting ", ", towards ", " richting ",
        " towards ", " y ", ", " };
    for (int i = 0; i < splitters.length; i++) {
      res = dir.split(splitters[i]);
      if (2 == res.length) {
        res[0] = res[0].trim();
        res[1] = res[1].trim();
        return res;
      }
    }
    return null;
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
    result = prime * result + ((direction == null) ? 0 : direction.hashCode());
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
    if (!(obj instanceof RawDirection)) {
      return false;
    }
    RawDirection other = (RawDirection) obj;
    if (direction == null) {
      if (other.direction != null) {
        return false;
      }
    } else if (!direction.equals(other.direction)) {
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
    return "RD<" + direction + ">";
  }

}
