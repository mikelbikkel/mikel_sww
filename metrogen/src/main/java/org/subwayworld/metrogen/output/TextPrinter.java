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

package org.subwayworld.metrogen.output;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.subwayworld.metrogen.MetroException;
import org.subwayworld.metrogen.network.MetroDirection;
import org.subwayworld.metrogen.network.MetroNetwork;
import org.subwayworld.metrogen.network.MetroSegment;
import org.subwayworld.metrogen.network.MetroStation;
import org.subwayworld.metrogen.network.TransformInfo;

/**
 * Prints metro network information in segment format.
 * <p>
 * Incomplete implementation.
 */
class TextPrinter {

  private MetroNetwork m_data;

  private PrintWriter m_file;

  public TextPrinter() {
  }

  public void print(MetroNetwork data, String encoding) throws MetroException {
    m_data = data;
    String basename = "seg_" + m_data.city.id.toLowerCase(Locale.ENGLISH);
    FileOutputStream fos;
    try {
      fos = new FileOutputStream(basename + ".txt");
    } catch (FileNotFoundException e) {
      throw new MetroException(e);
    }
    OutputStreamWriter osr;
    osr = new OutputStreamWriter(fos, Charset.forName(encoding));
    m_file = new PrintWriter(osr);
    printHeader();
    m_file.println("");

    insertSegments();
    m_file.println("");

    // insertTransformInfo();
    // m_file.println("");

    m_file.println("# End of file");
    m_file.println("");

    closeFile();
  }

  private void closeFile() {
    if (null == m_file) {
      return;
    }
    m_file.flush();
    m_file.close();
    m_file = null;
  }

  private void printHeader() {
    m_file.println("");
    m_file.println("CITY");
    m_file.println("CITYNAME");
    m_file.println(m_data.city.name);
    m_file.println("CITYNAME_END");
    m_file.println("CITYCODE");
    m_file.println(m_data.city.code);
    m_file.println("CITYCODE_END");
    m_file.println("LINETYPE");
    m_file.println(m_data.city.linetype);
    m_file.println("LINETYPE_END");
    m_file.println("CITYEND");
  }

  private void insertSegments() {
    for (MetroSegment seg : m_data.segs) {
      m_file.println("");
      m_file.println("SEGMENT");
      printForward(seg.m_fwd);
      printBackward(seg.m_bck);
      printStations(seg.m_stations);
      m_file.println("SEGMENTEND");
    }
  }

  private void printForward(Set<MetroDirection> dirs) {
    m_file.println("FORWARD");
    for (MetroDirection md : dirs) {
      m_file.println("\"" + md.m_desc + "\"");
    }
    m_file.println("FORWARDEND");
  }

  private void printBackward(Set<MetroDirection> dirs) {
    if (0 == dirs.size()) {
      return;
    }
    m_file.println("BACKWARD");
    for (MetroDirection md : dirs) {
      m_file.println("\"" + md.m_desc + "\"");
    }
    m_file.println("BACKWARDEND");
  }

  private void printStations(List<MetroStation> stats) {
    m_file.println("STATIONS");
    for (MetroStation ms : stats) {
      m_file.println("\"" + ms.getName() + " " + ms.toString() + "\"");
    }
    m_file.println("STATIONSEND");
  }

  @SuppressWarnings("unused")
  private void insertTransformInfo() {
    String stat_id;
    for (TransformInfo ti : m_data.trans) {
      stat_id = ti.station.getId();
      m_file.println("INSERT INTO transform_point(station_id, "
          + " pre_dir_id, post_dir_id)");
      m_file.print("VALUES( ");
      m_file.print("'" + stat_id + "', '" + ti.mdPre.m_dir_id + "', '"
          + ti.mdPost.m_dir_id + "')");
      m_file.println(";");
    }
  }

}
