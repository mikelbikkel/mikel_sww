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
import java.util.List;

import org.subwayworld.metrogen.network.MetroDirection;
import org.subwayworld.metrogen.network.MetroSegment;
import org.subwayworld.metrogen.network.MetroStation;
import org.subwayworld.metrogen.network.StationType;

class Utils {

  private static final String CITY_ID = "DUCK_TOWN";

  static MetroSegment createSegment(String[] stat, MetroDirection fwd,
      MetroDirection bck) {
    List<MetroStation> slist = new ArrayList<>();
    MetroStation ms;
    for (String s : stat) {
      ms = new MetroStation(CITY_ID, s, s, s, s);
      slist.add(ms);
    }
    MetroSegment mseg = new MetroSegment(slist);
    mseg.m_fwd.add(fwd);
    mseg.m_bck.add(bck);
    return mseg;
  }

  static MetroSegment createSegment(String[] stat) {
    List<MetroStation> slist = new ArrayList<>();
    MetroStation ms;
    for (String s : stat) {
      ms = new MetroStation(CITY_ID, s, s, s, s);
      slist.add(ms);
    }
    MetroSegment mseg = new MetroSegment(slist);
    return mseg;
  }

  private static MetroStation createStation(String statName) {
    return new MetroStation(CITY_ID, statName, statName, statName,
        StationType.REGULAR, statName, false);
  }

  private static MetroStation createMnnStation(String statName,
      String nodeName) {
    return new MetroStation(CITY_ID, statName, statName, statName,
        StationType.REGULAR, nodeName, true);
  }

  private static MetroDirection createDirection(String desc) {
    return new MetroDirection(desc, desc, null, null);
  }

  static MetroSegment createSegment(String[] stats, String dir) {
    MetroSegment seg = new MetroSegment();
    MetroStation ms;
    for (String s : stats) {
      ms = createStation(s);
      seg.addMetroStation(ms);
    }
    MetroDirection md = createDirection(dir);
    seg.addForward(md);
    return seg;
  }

  static MetroSegment createMNNSegment(String[] stats, String[] nodes,
      String dir) {
    MetroSegment seg = new MetroSegment();
    MetroStation ms;
    for (int i = 0; i < stats.length; i++) {
      if (null == nodes[i]) {
        ms = createStation(stats[i]);
      } else {
        ms = createMnnStation(stats[i], nodes[i]);
      }
      seg.addMetroStation(ms);
    }
    MetroDirection md = createDirection(dir);
    seg.addForward(md);
    return seg;
  }

}
