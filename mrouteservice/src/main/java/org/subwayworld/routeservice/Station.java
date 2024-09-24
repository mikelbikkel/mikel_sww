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
 * Represents a station in the metro network.
 * 
 */
public class Station implements ILocation {

  /**
   * The code that uniquely identifies this station.
   */
  private final String m_code;

  /**
   * The name of this station.
   */
  private final String m_name;

  /**
   * GPS information of this station.
   * <p>
   * null if GPS information is not available.
   */
  private GPSInfo m_gps;

  /**
   * The local name of this station.
   */
  private final String m_localname;

  /**
   * The type of station.
   * <p>
   * One of: REGULAR, NETWORK or COMPLEX.
   */
  private final String m_type;

  public Station() {
    this("", "", "REGULAR", null);
  }

  /**
   * Creates a new station without GPS information.
   * 
   * @param code
   *          code. For example: ROTTERDAM.
   * @param name
   *          name. For example: Rotterdam.
   * @param localname
   *          the localname. Null if not available.
   * @param stationType
   *          the station type.
   * 
   * @throws IllegalArgumentException
   *           if code, name or stationType is null.
   */
  public Station(String code, String name, String localname, String stationType) {
    if (null == code || null == name || null == stationType) {
      throw new IllegalArgumentException();
    }
    m_code = code;
    m_name = name;
    m_localname = localname;
    m_type = stationType;
    m_gps = null;
  }

  /**
   * Creates a new station with GPS information.
   * 
   * @param code
   *          code. For example: ROTTERDAM.
   * @param name
   *          name. For example: Rotterdam.
   * @param localname
   *          the localname. Null if not available.
   * @param lat
   *          latitude.
   * @param longitude
   *          longitude.
   * 
   * @throws IllegalArgumentException
   *           if code or name is null.
   */
  public Station(String code, String name, String localname,
      String stationType, double lat, double lon) {
    this(code, name, localname, stationType);
    m_gps = new GPSInfo(code, lat, lon);
  }

  /**
   * Returns true if station is part of a complex.
   * 
   * @return true if station is part of a complex.
   */
  public boolean isComplex() {
    if ("NETWORK".equals(m_type) || "COMPLEX".equals(m_type)) {
      return true;
    }
    return false;
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

  /**
   * Returns the station type. <br>
   * One of: REGULAR, NETWORK or COMPLEX.
   * 
   * @return the station type.
   */
  public String getType() {
    return m_type;
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
    if (!(obj instanceof Station)) {
      return false;
    }
    Station other = (Station) obj;
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
