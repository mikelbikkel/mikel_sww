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
/**
 * 
 */
package org.subwayworld.routeservice;

/**
 * Common features of Station and MetroAlias.
 */
public interface ILocation {

  /**
   * Returns the code that identifies this location.
   * <p>
   * For example: RDMBLAAK.
   * 
   * @return the code that identifies this location.
   */
  public String getCode();

  /**
   * Returns the descriptive name of this location.
   * <p>
   * For example: Blaak.
   * 
   * @return the descriptive name of this location.
   */
  public String getName();

  /**
   * Returns true if location has GPS info.
   * 
   * @return true if location has GPS info, false otherwise.
   */
  public boolean getHasGPSInfo();

  /**
   * Returns the GPS information of this location.
   * 
   * @return the GPS information of this location.
   */
  public GPSInfo getGPSInfo();

  /**
   * Returns the local name of this location.
   * <p>
   * The local name is the name as spelled in the non-Western alphabet. For
   * example, Cyrillic, Russian or Chinese.
   * 
   * @return the local name of this location, or null if a local name is not
   *         available.
   */
  public String getLocalName();

}
