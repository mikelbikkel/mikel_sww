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

// import org.mikel.dbwrapper.DestinationTable;
import org.mikel.dbwrapper.PrimaryKey;
import org.mikel.dbwrapper.SourceColumn;
import org.subwayworld.metrogen.MetroException;
import org.subwayworld.metrogen.input.UTFHandler;

// @DestinationTable("city")
public class MetroCity {

  /**
   * City name.<br>
   * Example: Rotterdam.
   */
  @SourceColumn("description")
  public final String name;

  /**
   * City identifier.<br>
   * Example: ROTTERDAM.
   */
  @PrimaryKey(supplied=true)
  @SourceColumn("city_id")
  public final String id;

  /**
   * 3-letter city code.<br>
   * Example: RDM.
   */
  @SourceColumn(value = "city_code")
  public final String code;

  public final String linetype;

  public MetroCity() {
    name = null;
    id = null;
    code = null;
    linetype = null;
  }

  /**
   * Creates a new city with a metro network.
   * <p>
   * Generates id for this city from the name. For example, the is for New York
   * is: NEW_YORK.
   * 
   * @param nm
   *          the name of the city. For example: Rotterdam
   * @param cd
   *          the 3-letter city code. For example: RDM.
   * @param lt
   *          the linetype of this network.
   * @throws MetroException
   *           when city names contains a unicode charactter that cannot be
   *           translated into an ASCII equivalent.
   */
  public MetroCity(String nm, String cd, String lt) throws MetroException {
    name = nm;
    code = cd;
    linetype = lt;
    String idtmp = nm.toUpperCase();
    idtmp = idtmp.replace("\\'", "_");
    id = UTFHandler.removeAccents(idtmp);
  }

  @Override
  public String toString() {
    return "<CITY:" + name + "[" + code + "] " + linetype + ">";
  }
}
