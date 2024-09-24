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
 * A station as defined in the segments of the network file.
 * <p>
 * Normal station.<br>
 * Name = Slinge. Complex = null. Displayname = Slinge.
 * </p>
 *
 * <p>
 * Complex station 1.<br>
 * Name = Blaak X::X 1. Complex = Blaak. Displayname = Blaak.
 * </p>
 *
 * <p>
 * Complex station 2.<br>
 * Name = Blaak X::X 2. Complex = Blaak. Displayname = Blaak.
 * </p>
 *
 */
public class RawStation {

  /**
   * The name of the station, as found in the network file.
   * <p>
   * For example: Centraal Station X::X 1.
   */
  public final String m_name;

  public final String m_complex;

  /**
   * The name ofthe station, as shown to the traveler on the perron.
   * <p>
   * For example: Centraal Station.
   */
  public final String m_displayname;

  private static final String COMPLEX_SEPARATOR = "X::X";

  public RawStation(String name) throws MetroException {
    if (null == name || "".equals(name)) {
      throw new MetroException("RawStation");
    }
    if (name.contains(COMPLEX_SEPARATOR)) {
      String[] sinfo = name.split(COMPLEX_SEPARATOR);
      if (sinfo.length != 2) {
        throw new MetroException("Invalid complex name: " + name);
      }
      m_complex = sinfo[0].trim();
      m_displayname = sinfo[0].trim();
    } else {
      m_complex = null;
      m_displayname = name;
    }
    m_name = name;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((m_name == null) ? 0 : m_name.hashCode());
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
    if (!(obj instanceof RawStation)) {
      return false;
    }
    RawStation other = (RawStation) obj;
    if (m_name == null) {
      if (other.m_name != null) {
        return false;
      }
    } else if (!m_name.equals(other.m_name)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "RS<" + m_name + ">";
  }

}
