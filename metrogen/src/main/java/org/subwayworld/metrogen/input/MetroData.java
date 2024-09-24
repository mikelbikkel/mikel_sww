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
package org.subwayworld.metrogen.input;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.subwayworld.metrogen.IMetroHandler;
import org.subwayworld.metrogen.MetroException;
import org.subwayworld.metrogen.network.MetroCity;

/**
 * Implements the callback functions called by the parser.
 * <p>
 * Transforms the tokens from the parser into raw metro objects.
 * 
 * <p>
 * MetroData elements are:
 * <ul>
 * <li>multi named node definitions: StationManager</li>
 * <li>station definitions: StationManager</li>
 * <li>metro transform definitions: Set<RawTransformInfo</li>
 * <li>line definitions: LineManager</li>
 * <li>direction definitions</li>
 * <li>segment definitions: Set<LineSegments></li>
 * <li>city definition: MetroCity</li>
 * </ul>
 * 
 * 
 */
public class MetroData implements IMetroHandler {

  /*
   * Logging functionality.
   */
  private static final String LOG_CLASS_NAME = "org.subwayworld.metrogen.input";
  static Logger log = Logger.getLogger(LOG_CLASS_NAME);

  public MetroCity m_city;

  public Set<LineSegment> m_segs;

  public Set<RawTransformInfo> m_rawTrans;

  /**
   * Set to exclude duplicates.
   */
  public Set<RawDirection> m_directions;

  public Set<LineInfo> m_lines;

  public Set<RawStation> m_stats;

  public Set<List<String>> m_mnn;

  public Set<RawSegment> m_segments;

  public Set<RawOverrideSegment2> m_overrides;

  /**
   * The override segment that is being parsed.
   * <p>
   * Override sections are added to this override segment.
   */
  private RawOverrideSegment2 m_currentOverride2;

  public MetroData() {
    m_segs = new LinkedHashSet<>();
    m_rawTrans = new LinkedHashSet<>();
    m_directions = new LinkedHashSet<>();
    m_lines = new LinkedHashSet<>();
    m_stats = new LinkedHashSet<>();
    m_mnn = new LinkedHashSet<>();
    m_segments = new LinkedHashSet<>();
    m_overrides = new LinkedHashSet<>();
  }

  @Override
  public void setCity(String name, String code, String linetype)
      throws MetroException {
    if (null == name || "".equals(name)) {
      throw new MetroException("setCity");
    }
    if (null == code || "".equals(code)) {
      throw new MetroException("setCityCode");
    }
    if (null == linetype || "".equals(linetype)) {
      throw new MetroException("setLineType");
    }
    m_city = new MetroCity(name, code, linetype);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.subwayworld.metrogen.IMetroHandler#addSegment(java.util.List,
   * java.util.List, java.util.List)
   */
  @Override
  public void addSegment(String fwd, String bck, List<String> stats)
      throws MetroException {
    boolean result;

    if (null == fwd || null == stats) {
      throw new MetroException("addSegment - fwd or stats is null");
    }

    if (log.isLoggable(java.util.logging.Level.FINER)) {
      log.entering("MetroData", "addSegment");
    }

    RawSegment rseg = new RawSegment(fwd, bck, stats);
    m_segments.add(rseg);

    RawDirection rd;
    rd = new RawDirection(fwd);
    result = m_directions.add(rd);
    if (log.isLoggable(java.util.logging.Level.FINER)) {
      log.finer(
          "Forward: " + fwd + ". RawDir: " + rd + ". Added: " + result + ".");
    }

    if (null != bck) {
      rd = new RawDirection(bck);
      result = m_directions.add(rd);
      if (log.isLoggable(java.util.logging.Level.FINER)) {
        log.finer(
            "Back: " + bck + ". RawDir: " + rd + ". Added: " + result + ".");
      }
    }

    RawStation rs;
    for (String s : stats) {
      rs = new RawStation(s);
      result = m_stats.add(rs);
      if (log.isLoggable(java.util.logging.Level.FINER)) {
        log.finer("Station: " + s + ". RawStation: " + rs + ". Added: " + result
            + ".");
      }
    }
    if (log.isLoggable(java.util.logging.Level.FINER)) {
      log.exiting("MetroData", "addSegment");
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.subwayworld.metrogen.IMetroHandler#addMultiNamedNode(java.util.List)
   */
  @Override
  public void addMultiNamedNode(List<String> node) throws MetroException {
    int cnt = node.size();
    if (cnt < 2) {
      return;
    }

    m_mnn.add(node);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.subwayworld.metrogen.IMetroHandler#addMetroTransformation(java.lang
   * .String, java.lang.String, java.lang.String)
   */
  @Override
  public void addMetroTransformation(String statCode, String preDir,
      String postDir) throws MetroException {
    RawTransformInfo rti = new RawTransformInfo(statCode, preDir, postDir);
    m_rawTrans.add(rti);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.subwayworld.metrogen.IMetroHandler#addLine(java.lang.String,
   * java.lang.String, java.lang.String, java.lang.String)
   */
  @Override
  public void addLine(String name, String svtype, String remark,
      String editinfo) throws MetroException {
    LineInfo li = new LineInfo(name, svtype, remark, editinfo);
    m_lines.add(li);
  }

  @Override
  public void startOverride(String dir) throws MetroException {
    assert null == m_currentOverride2;

    m_currentOverride2 = new RawOverrideSegment2(dir);

    RawDirection rd = new RawDirection(dir);
    m_directions.add(rd);
  }

  @Override
  public void endOverride() throws MetroException {
    assert null != m_currentOverride2;
    m_overrides.add(m_currentOverride2);
    m_currentOverride2 = null;
  }

  @Override
  public void addOverrideSection(String dir, List<String> st)
      throws MetroException {
    assert null != m_currentOverride2;
    RawSegment rs2 = new RawSegment(dir, st);
    m_currentOverride2.addSegment(rs2);

    RawDirection rd = new RawDirection(dir);
    m_directions.add(rd);

    RawStation rs;
    for (String s : st) {
      rs = new RawStation(s);
      m_stats.add(rs);
    }
  }

}
