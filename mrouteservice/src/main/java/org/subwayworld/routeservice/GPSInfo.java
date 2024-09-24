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

public class GPSInfo {

  private final String m_code;

  private final double m_latitude;

  private final double m_longitude;

  public GPSInfo(String code, double latitude, double longitude) {
    m_code = code;
    m_latitude = latitude;
    m_longitude = longitude;
  }

  /**
   * Returns the distance from this GPS location to another GPS location in
   * kilometres.
   * 
   * @param latitude
   *          the latitude of the other location.
   * @param longitude
   *          the longitude of the other location.
   * @return the distance in kilometres.
   */
  public double getDistance(double latitude, double longitude) {
    double theta = m_longitude - longitude;
    double dist = Math.sin(deg2rad(m_latitude)) * Math.sin(deg2rad(latitude))
        + Math.cos(deg2rad(m_latitude)) * Math.cos(deg2rad(latitude))
        * Math.cos(deg2rad(theta));
    assert false == Double.isNaN(dist);
    dist = Math.min(dist, 1); // arg for acos must be <= 1
    dist = Math.acos(dist);
    assert false == Double.isNaN(dist);
    dist = rad2deg(dist);
    dist = dist * 60 * 1.1515; // distance in miles
    // if (unit == "K") {
    dist = dist * 1.609344; // distance in kilometres
    // } else if (unit == "N") {
    // dist = dist * 0.8684; // distance in nautical miles
    // }
    return dist;
  }

  /**
   * Returns the code that identifies this GPS information.
   * <p>
   * For example: RDMBLAAK.
   * 
   * @return the code that identifies this GPS information.
   */
  public String getCode() {
    return m_code;
  }

  /**
   * Returns the latitude of this GPS location.
   * 
   * @return the latitude
   */
  public double getLatitude() {
    return m_latitude;
  }

  /**
   * Returns the longitude of this GPS location.
   * 
   * @return the longitude
   */
  public double getLongitude() {
    return m_longitude;
  }

  private double deg2rad(double deg) {
    return (deg * Math.PI / 180.0);
  }

  private double rad2deg(double rad) {
    return (rad * 180 / Math.PI);
  }
}
