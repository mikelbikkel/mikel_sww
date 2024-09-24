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
package org.subwayworld.metrogen;

import org.subwayworld.metrogen.network.MetroStation;

public interface IStationLookup {

  /**
   * Finds the metro station for a station name.
   * <p>
   * The name is the name as found in the network file, for example:
   * <code>Centraal Station X::X 1</code>.
   * 
   * @param name
   *          the station name as found in the network file.
   * 
   * @return the MetroStation.
   * 
   * @throws MetroException
   *           if name is not a metro station.
   */
  public MetroStation findStation(String name) throws MetroException;

}
