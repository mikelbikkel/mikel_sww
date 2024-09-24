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

import org.subwayworld.metrogen.network.MetroDirection;
import org.subwayworld.metrogen.network.MetroLine;

public interface ILineLookup {

  /**
   * Finds a line, given a name.
   * 
   * @param name
   *          the name of the line.
   * 
   * @return the MetroLine.
   * 
   * @throws MetroException
   *           if name is not a line.
   * 
   * @throws NullPointerException
   *           if name is null.
   */
  public MetroLine findLine(String name) throws MetroException;

  /**
   * Finds a direction, given a name.
   * 
   * @param name
   *          the name of the direction. For example: Line E towards F.
   * 
   * @return the MetroDirection.
   * 
   * @throws MetroException
   *           if name is not a direction.
   *
   * @throws NullPointerException
   *           if name is null.
   */
  public MetroDirection findDirection(String name) throws MetroException;

}
