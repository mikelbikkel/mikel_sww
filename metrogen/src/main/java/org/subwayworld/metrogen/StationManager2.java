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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.subwayworld.metrogen.input.RawStation;
import org.subwayworld.metrogen.input.UTFHandler;
import org.subwayworld.metrogen.network.MetroStation;
import org.subwayworld.metrogen.network.StationType;

/**
 * StationManager is an immutable object.
 *
 */
public class StationManager2 implements IStationLookup {

  private Set<MetroStation> m_stations;

  private class NodeInfo {

    /**
     * The basic nodename for an MNN.<br>
     * For example: "MNN_Beurs" or "MNN_Blaak X::X 1"
     * <p>
     * The final node name is different, for example: MNN_BLAAK_X__X_1.
     */
    String name;

    StationType m_stype;

    NodeInfo(String n, StationType t) {
      name = n;
      m_stype = t;
    }
  }

  /**
   * Use the other ctor to initialize the StationManager.
   */
  @SuppressWarnings("unused")
  private StationManager2() {
    return;
  }

  public StationManager2(String cityId, String cityCode, Set<RawStation> stats,
      Set<List<String>> mnn) throws MetroException {
    // Check input.
    if (null == cityCode || "".equals(cityCode)) {
      throw new MetroException();
    }
    if (null == cityId || "".equals(cityId)) {
      throw new MetroException();
    }
    if (null == stats) {
      throw new MetroException();
    }
    checkMnnReferences(stats, mnn);

    // Create a mapping from station name to NodeInfo.
    Map<String, NodeInfo> mni = new HashMap<>();

    // Add regular MNNs to node info.
    if (null != mnn) {
      addMnnNodeInfo(mnn, mni);
    }
    // Add complex MNNs to node info.
    addComplexNodeInfo(stats, mni);

    // Create MetroStations from raw stations and node info.
    m_stations = createMetroStations(cityId, cityCode, stats, mni);
  }

  private void addComplexNodeInfo(Set<RawStation> stats,
      Map<String, NodeInfo> niMap) {
    // Map complex name to a list of complex stations.
    Map<String, List<String>> complexMap = new HashMap<>();

    for (RawStation rs : stats) {
      if (null != rs.m_complex) {
        List<String> slist;
        if (complexMap.containsKey(rs.m_complex)) {
          slist = complexMap.get(rs.m_complex);
          if (!slist.contains(rs.m_name)) {
            slist.add(rs.m_name);
          }
        } else {
          slist = new ArrayList<>();
          slist.add(rs.m_name);
          complexMap.put(rs.m_complex, slist);
        }
      }
    }

    String nodeName;
    NodeInfo ni;
    boolean isFirst;
    StationType stype;
    for (List<String> stations : complexMap.values()) {
      nodeName = "MNN_" + stations.get(0); // first entry gives name to the
                                           // node.
      isFirst = true;
      for (String statName : stations) {
        if (isFirst) {
          stype = StationType.NETWORK;
          isFirst = false;
        } else {
          stype = StationType.COMPLEX;
        }
        ni = new NodeInfo(nodeName, stype);
        niMap.put(statName, ni);
      }
    }
  }

  private void addMnnNodeInfo(Set<List<String>> mnn,
      Map<String, NodeInfo> niMap) {
    String nodeName;
    NodeInfo ni;
    for (List<String> node : mnn) {
      nodeName = "MNN_" + node.get(0); // first entry gives name to the node.
      ni = new NodeInfo(nodeName, StationType.REGULAR);
      for (String statName : node) {
        niMap.put(statName, ni);
      }
    }
  }

  private Set<MetroStation> createMetroStations(String cityId, String cityCode,
      Set<RawStation> stats, Map<String, NodeInfo> mni) throws MetroException {
    Set<MetroStation> ms = new LinkedHashSet<>();
    MetroStation s;
    NodeInfo ni;
    String station_id;
    String node_id;
    boolean isMNN;
    for (RawStation rs : stats) {
      ni = mni.get(rs.m_name);
      if (null == ni) {
        ni = new NodeInfo(rs.m_name, StationType.REGULAR);
        isMNN = false;
      } else {
        isMNN = true;
      }
      station_id = generateId(cityCode + rs.m_name);
      checkDuplicates(station_id, rs.m_name, ms);
      node_id = generateId(cityCode + ni.name);
      s = new MetroStation(cityId, rs.m_name, rs.m_displayname, station_id,
          ni.m_stype, node_id, isMNN);
      ms.add(s);
    }
    return ms;
  }

  private String generateId(String name) throws MetroException {
    String id = name.toUpperCase();
    id = id.replace("\\'", "_");
    id = UTFHandler.removeAccents(id);
    return id;
  }

  /**
   * Throws an exception if id X with name A already exists in set mset as a
   * MetroStation with id X and name B.
   * 
   * @param id
   *          the id to check (X).
   * @param name
   *          the name to check (A)
   * @param mset
   *          the set of MetroStations.
   * @throws MetroException
   *           if id (X) already exists with a different name (B).
   */
  private void checkDuplicates(String id, String name, Set<MetroStation> mset)
      throws MetroException {
    String idStation;
    String nameStation;
    for (MetroStation ms : mset) {
      idStation = ms.getId();
      nameStation = ms.getName();
      if (id.equals(idStation) && !name.equals(nameStation)) {
        // TODO: localize error message.
        String msg = "<" + id + "> => <" + name + ">,<" + nameStation
            + ">. Probably one of these names is a typo and should be corrected.";
        throw new MetroException(msg);
      }
    }
  }

  /**
   * Checks that all MNN station names do exist in the master list of stations.
   * <p>
   * TODO: what f an MNN names is a complex station??
   * 
   * @param stats
   *          the master list of station names.
   * @param mnn
   *          the MNN station names.
   * @throws MetroException
   *           if an MNN station names does not exist in the master list.
   */
  private void checkMnnReferences(Set<RawStation> stats, Set<List<String>> mnn)
      throws MetroException {
    List<String> statnames = new ArrayList<>();
    for (RawStation rs : stats) {
      statnames.add(rs.m_name);
    }

    for (List<String> lst : mnn) {
      for (String s : lst) {
        if (!statnames.contains(s)) {
          throw new MetroException("Unknown MNN name: " + s);
        }
      }
    }
    return;
  }

  @Override
  public MetroStation findStation(String name) throws MetroException {
    for (MetroStation m : m_stations) {
      if (m.getRawName().equals(name)) {
        return m;
      }
    }
    throw new MetroException("Cannot find station: " + name);
  }

  public Set<MetroStation> getStations() {
    return m_stations;
  }
}
