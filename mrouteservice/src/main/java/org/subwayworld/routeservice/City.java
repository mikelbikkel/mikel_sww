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

import java.util.List;

/**
 * Represents a city that has a metro network.
 * 
 */
public class City implements ILocation, IPublishInfo {

  /**
   * The code that identifies a city.<br>
   * For example: ROTTERDAM.
   */
  private final String m_code;

  /**
   * The 3-letter code that identifies a city.<br>
   * For example: RDM.
   * <p>
   * This short code is used as prefix for the stations and landmarks in this
   * city. For example: RDMBLAAK, RDMEUROMAST.
   */
  private final String m_shortCode;

  /**
   * The name of the city.<br>
   * For example: Rotterdam.
   */
  private final String m_name;

  /**
   * The local name of this city.
   */
  private String m_localname;

  private final Country m_country;
  private final Continent m_continent;

  private GPSInfo m_gps;

  // Make sure code and name are supplied.
  public City() {
    this("error", "error", "error", null, null, null);
  }

  /**
   * Creates a new city without GPS information.
   * 
   * @param code
   * @param shortCode
   * @param name
   * @param localname
   * @param cntry
   * @param con
   */
  public City(String code, String shortCode, String name, String localname,
      Country cntry, Continent con) {
    if (null == code || null == name) {
      throw new IllegalArgumentException();
    }
    m_code = code;
    m_shortCode = shortCode;
    m_name = name;
    m_localname = localname;

    m_country = cntry;
    m_continent = con;

    m_gps = null;
  }

  /**
   * Creates a new city ith GPS information.
   * 
   * @param code
   * @param shortCode
   * @param name
   * @param localname
   * @param cntry
   * @param con
   * @param lat
   * @param lon
   */
  public City(String code, String shortCode, String name, String localname,
      Country cntry, Continent con, double lat, double lon) {
    this(code, shortCode, name, localname, cntry, con);
    m_gps = new GPSInfo(code, lat, lon);
  }

  /**
   * Returns the city code.<br>
   * For example: ROTTERDAM.
   * 
   * @return the city code.
   */
  public String getCode() {
    return m_code;
  }

  /**
   * Returns the 3-letter city identifier.<br>
   * For example: RDM.
   * 
   * @return the 3-letter city identifier.
   */
  public String getShortCode() {
    return m_shortCode;
  }

  /**
   * Returns the name of the city.<br>
   * For example: Rotterdam.
   * 
   * @return the name of the city.
   */
  public String getName() {
    return m_name;
  }

  public Country getCountry() {
    return m_country;
  }

  public Continent getContinent() {
    return m_continent;
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

  public void setLocalName(String name) {
    m_localname = name;
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
    if (!(obj instanceof City)) {
      return false;
    }
    City other = (City) obj;
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
    return "<" + m_code + "> " + m_name + " <" + m_publish + ">";
  }

  /**
   * The publish information for this city.
   * <p>
   * Composition strategy.<br>
   * This objects encapsulates the common functionality of all things that
   * implement the IPublishInfo interface.<br>
   * This allows for re-use of the common functionality.<br>
   * The alternative strategy for re-use, inheritance, would enforce all
   * implementations of the IPublishInterface to extend from a common base.
   */
  private PublishInfo m_publish = new PublishInfo();

  /*
   * (non-Javadoc)
   * 
   * @see org.subwayworld.routeservice.IPublishInfo#getStatus()
   */
  @Override
  public PublishInfoStatus getStatus() {
    return m_publish.getStatus();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.subwayworld.routeservice.IPublishInfo#getMessages()
   */
  @Override
  public List<PublishMessage> getMessages() {
    return m_publish.getMessages();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.subwayworld.routeservice.IPublishInfo#addMessage(org.subwayworld.
   * routeservice.PublishMessage)
   */
  @Override
  public void addMessage(PublishMessage msg) {
    m_publish.addMessage(msg);
  }
}
