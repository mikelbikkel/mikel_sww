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
package org.subwayworld.metrogen.network;

/**
 * The type of a station.
 * <p>
 * A regular station is one station for the user and one node inside the metro
 * network.
 * <p>
 * A complex station appears as 1 station for the user.<br>
 * But inside the metro network a complex consists of multiple nodes.<br>
 * Vergelijk het met 1 station met meerdere sporen.
 * </p>
 * <p>
 * If a station is not a Complex, it is a REGULAR station.<br>
 * For a Complex, the first station is a NETWORK station. The other stations are
 * COMPLEX stations.
 * </p>
 */
public enum StationType {
  /**
   * A regular, not complex, station.
   */
  REGULAR,
  /**
   * The first station of a Complex.
   */
  NETWORK,
  /**
   * The remaining stations of a Complex.
   */
  COMPLEX;
}
