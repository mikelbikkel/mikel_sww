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

import org.mikel.dbwrapper.DestinationTable;
import org.mikel.dbwrapper.PrimaryKey;
import org.mikel.dbwrapper.SourceColumn;
import org.subwayworld.metrogen.MetroException;

/**
 * Represents a station in the metro network.
 * <p>
 * <h4>Simple station: Slinge.</h4> <b>Raw: </b>Slinge.<br>
 * <b>Name: </b>Slinge.<br>
 * <b>Id: </b>RDM_SLINGE.<br>
 * <b>Node: </b>RDM_SLINGE.<br>
 * <b>Type: </b>REGULAR.<br>
 * <p>
 * <h4>Complex stations: Blaak 1 and 2.</h4> <b>Raw: </b>Blaak X::X 1.<br>
 * <b>Name: </b>Blaak.<br>
 * <b>Id: </b>RDM_BLAAK_X::X_1.<br>
 * <b>Node: </b>RDMMNN_BLAAK_X::X_1.<br>
 * <b>Type: </b>COMPLEX.<br>
 * <p>
 * <b>Raw: </b>Blaak X::X 2.<br>
 * <b>Name: </b>Blaak.<br>
 * <b>Id: </b>RDM_BLAAK_X::X_2.<br>
 * <b>Node: </b>RDMMNN_BLAAK_X::X_1.<br>
 * <b>Type: </b>NETWORK.<br>
 * <p>
 * <h4>Multi named node: Beurs and Beursplein.</h4> <b>Raw: </b>Beurs.<br>
 * <b>Name: </b>Beurs.<br>
 * <b>Id: </b>RDM_BEURS.<br>
 * <b>Node: </b>RDMMNN_BEURS.<br>
 * <b>Type: </b>REGULAR.<br>
 * <p>
 * <b>Raw: </b>Beursplein.<br>
 * <b>Name: </b>Beursplein.<br>
 * <b>Id: </b>RDM_BEURSPLEIN.<br>
 * <b>Node: </b>RDMMNN_BEURS.<br>
 * <b>Type: </b>REGULAR.<br>
 * <p>
 * 
 */
@DestinationTable("station")
public class MetroStation {

  /**
   * Identifier of the city this station belongs to.<br>
   * For example: {@code ROTTERDAM}.
   */
  @SourceColumn("city_id")
  private final String m_cityId;

  /**
   * The name of this station, as found in the network file.<br>
   * For example: {@code Centraal station X::X 1}.
   */
  private final String m_rawName;

  /**
   * The name of this station, as shown to the traveler on the perron.<br>
   * For example: {@code Centraal station}.
   */
  @SourceColumn("description")
  private final String m_name;

  /**
   * The identifier of this station.<br>
   * For example: {@code RDMCENTRAAL_STATION}.
   */
  @PrimaryKey(supplied=true)
  @SourceColumn("station_id")
  private final String m_id;

  /**
   * The node of this station.
   * <p>
   * For example: {@code RDMCENTRAAL_STATION}.
   */
  @SourceColumn("node_id")
  private final String m_node;

  private final boolean m_isMNN;

  /**
   * Station type.
   * <p>
   * One of: REGULAR, NETWORK, COMPLEX.
   */
  @SourceColumn("station_type")
  private StationType m_type;

  public MetroStation(String cityId, String rawName, String name, String id,
      String node) {
    m_cityId = cityId;
    m_rawName = rawName;
    m_name = name;
    m_id = id;
    m_node = node;
    m_type = StationType.REGULAR;
    m_isMNN = false;
  }

  public MetroStation(String cityId, String rawName, String name, String id,
      StationType type, String nodeId, boolean isMNN) {
    m_cityId = cityId;
    m_rawName = rawName;
    m_name = name;
    m_id = id;
    m_type = type;
    m_node = nodeId;
    m_isMNN = isMNN;
  }

  public boolean isMNN() {
    return m_isMNN;
  }

  /**
   * Returns the name of this station, as shown to the traveler on the
   * perron.<br>
   * For example: Centraal station.
   * 
   * @return the name of this station, as shown to the traveler on the perron.
   */
  public String getName() {
    return m_name;
  }

  /**
   * Returns the name of this station, as found in the network file.<br>
   * For example: Centraal station X::X 1.
   * 
   * @return the name of this station, as found in the network file.
   */
  public String getRawName() {
    return m_rawName;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "<STAT:" + m_id + "[" + m_node + "," + m_rawName + "]" + m_type
        + ">";
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
    result = prime * result + ((m_id == null) ? 0 : m_id.hashCode());
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
    if (!(obj instanceof MetroStation)) {
      return false;
    }
    MetroStation other = (MetroStation) obj;
    if (m_id == null) {
      if (other.m_id != null) {
        return false;
      }
    } else if (!m_id.equals(other.m_id)) {
      return false;
    }
    return true;
  }

  /**
   * Returns the id of this station.<br>
   * For example: RDMCENTRAAL_STATION.
   * 
   * @return the id of this station.
   */
  public String getId() {
    return m_id;
  }

  /**
   * Returns the node of this station.<br>
   * For example: RDMCENTRAAL_STATION.
   * 
   * @return the node of this station.
   */
  public String getNode() {
    return m_node;
  }

  /**
   * Sets the type of this station.<br>
   * 
   */
  public void setType(StationType tp) throws MetroException {
    if (null == tp) {
      throw new MetroException("station type null.");
    }
    m_type = tp;
  }

  /**
   * Returns the type of this station.<br>
   * 
   * @return the type of this station.
   */
  public StationType getType() {
    return m_type;
  }

  /**
   * Returns the cityId of this station.<br>
   * 
   * @return the cityId of this station.
   */
  public String getCityId() {
    return m_cityId;
  }

}
