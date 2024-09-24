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
 * Information about a nearest station.
 */
public class NearestStation implements Comparable<NearestStation> {

  /**
   * A station within the search radius.
   */
  private Station m_station;

  /**
   * Distance in meters.
   */
  private int m_distance;

  public NearestStation() {
  }

  public NearestStation(Station s) {
    m_station = s;
  }

  /**
   * Returns the station.
   * 
   * @return the station
   */
  public Station getStation() {
    return m_station;
  }

  /**
   * Sets the station.
   * 
   * @param station
   *          the station to set
   */
  public void setStation(Station station) {
    m_station = station;
  }

  /**
   * Returns the distance (in meters).
   * 
   * @return the distance
   */
  public int getDistance() {
    return m_distance;
  }

  /**
   * Sets the distance (in meters).
   * 
   * @param distance
   *          the distance to set
   */
  public void setDistance(int distance) {
    m_distance = distance;
  }

  @Override
  public int compareTo(NearestStation o) {
    // The distance determines the sort order for NearestStations.
    // Closer to request location means: smaller
    if (m_distance < o.m_distance) {
      return -1;
    }
    if (m_distance == o.m_distance) {
      return 0;
    }
    return 1;
  }

  @Override
  public String toString() {
    return "NearestStation [" + m_station.getCode() + ", " + m_distance + "]";
  }
}
