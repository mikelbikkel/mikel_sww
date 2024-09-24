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

import java.util.List;

/**
 * Functions called by the parser.
 */
public interface IMetroHandler {

  /**
   * Sets city information.
   * 
   * @param name
   *          the name of this city.
   * @param code
   *          the code of this city.
   * @param linetype
   *          the linetype of the network.
   */
  public void setCity(String name, String code, String linetype)
      throws MetroException;

  /**
   * Adds a line segment.
   * 
   * @param fwd
   *          the forward direction for this segment.
   * @param bck
   *          the backward direction for this segment. Optional.
   * @param stats
   *          ordered list of stations for this segment.
   */
  public void addSegment(String fwd, String bck, List<String> stats)
      throws MetroException;

  /**
   * Adds a multi named node.
   * 
   * @param node
   *          the set of stations that make up this node.
   */
  public void addMultiNamedNode(List<String> node) throws MetroException;

  /**
   * Adds a metro transformation point.
   * 
   * @param statCode
   *          the station where the transformation takes place.
   * @param preDir
   *          the direction before the transformation.
   * @param postDir
   *          the direction after the transformation.
   */
  public void addMetroTransformation(String statCode, String preDir,
      String postDir) throws MetroException;

  /**
   * Adds line information
   * 
   * @param name
   *          name of this line.
   * @param svtype
   *          service type of this line.
   * @param remark
   *          remarks for this line.
   * @param editinfo
   *          editor information for this line.
   */
  public void addLine(String name, String svtype, String remark, String editinfo)
      throws MetroException;

  public void startOverride(String dir) throws MetroException;

  public void endOverride() throws MetroException;

  public void addOverrideSection(String dir, List<String> st)
      throws MetroException;

}
