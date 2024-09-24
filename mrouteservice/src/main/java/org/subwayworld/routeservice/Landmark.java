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

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a landmark in the metro network.
 * 
 */
public class Landmark implements ILocation {

  /**
   * The code that uniquely identifies this landmark.
   * <p>
   * For example: PRSTOUR_EIFFEL
   */
  private final String m_code;

  /**
   * The name of this landmark.
   * <p>
   * For example: Tour Eiffel.
   */
  private final String m_name;

  /**
   * The local name of this landmark.
   */
  private final String m_localname;

  /**
   * The stations that are linked to this landmark.
   */
  private List<Station> m_stations;

  /**
   * The GPS information of this landmark.
   * <p>
   * {@code Null} if GPS information is not available.
   */
  private GPSInfo m_gps;

  // Make sure code and name are supplied.
  public Landmark() {
    this("", "", null);
  }

  public Landmark(String code, String name, String localname) {
    if (null == code || null == name) {
      throw new IllegalArgumentException();
    }
    m_code = code;
    m_name = name;
    m_localname = localname;
    m_stations = new ArrayList<Station>();
    m_gps = null;
  }

  public Landmark(String code, String name, String localname, double lat,
      double lon) {
    this(code, name, localname);
    m_gps = new GPSInfo(code, lat, lon);
  }

  /**
   * Links this landmark to a station.
   * 
   * @param stat
   *          the station.
   */
  public void addStation(Station stat) {
    if (null == stat) {
      throw new IllegalArgumentException();
    }
    m_stations.add(stat);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.subwayworld.routeservice.ILocation#getCode()
   */
  @Override
  public String getCode() {
    return m_code;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.subwayworld.routeservice.ILocation#getName()
   */
  @Override
  public String getName() {
    return m_name;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.subwayworld.routeservice.ILocation#hasGPSInfo()
   */
  @Override
  public boolean getHasGPSInfo() {
    if (null == m_gps) {
      return false;
    }
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.subwayworld.routeservice.ILocation#getGPSInfo()
   */
  @Override
  public GPSInfo getGPSInfo() {
    return m_gps;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.subwayworld.routeservice.ILocation#getLocalName()
   */
  @Override
  public String getLocalName() {
    return m_localname;
  }

  public List<Station> getStations() {
    return m_stations;
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
    result = prime * result + ((m_code == null) ? 0 : m_code.hashCode());
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
    if (!(obj instanceof Landmark)) {
      return false;
    }
    Landmark other = (Landmark) obj;
    if (m_code == null) {
      if (other.m_code != null) {
        return false;
      }
    } else if (!m_code.equals(other.m_code)) {
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
    return m_code;
  }
}
