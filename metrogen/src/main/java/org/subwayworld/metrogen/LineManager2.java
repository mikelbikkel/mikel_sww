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

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.subwayworld.metrogen.input.LineInfo;
import org.subwayworld.metrogen.input.RawDirection;
import org.subwayworld.metrogen.network.MetroDirection;
import org.subwayworld.metrogen.network.MetroLine;

/**
 * Manages lines, directions and endpoints.
 * <p>
 * Replaces the LineManager and DirectionManager.
 * <p>
 * Immutable object, initialized in the ctor.
 *
 */
public class LineManager2 implements ILineLookup {

  private Map<String, MetroLine> m_lines;

  private Map<String, MetroDirection> m_dirs;

  private Set<MetroLine> m_setLines;

  private Set<MetroDirection> m_setDirs;

  public LineManager2() {
    m_lines = Collections.emptyMap();
    m_dirs = Collections.emptyMap();
    m_setLines = Collections.emptySet();
    m_setDirs = Collections.emptySet();
  }

  /**
   * Creates MetroLine and MetroDirection information from the raw MetroData input.
   * 
   * @param cityCode
   *          the code of the city that is parsed. For example: RDM.
   * @param rdirs
   *          raw direction information.
   * @param info
   *          raw line information.
   * 
   * @throws MetroException
   *           if the raw input data contains errors.
   * 
   * @throws NullPointerException
   *           if <code>cityCode</code> or <code>rdirs</code> are
   *           <code>null</code>.
   */
  public LineManager2(String cityCode, Set<RawDirection> rdirs,
      Set<LineInfo> info) throws MetroException {

    if (null == cityCode || null == rdirs) {
      throw new NullPointerException();
    }

    Map<String, MetroLine> lines = new HashMap<>();
    Map<String, MetroDirection> dirs = new HashMap<>();

    // TODO: integrate rawline and raw lineInfo processing. Make MetroLine immutable.
    MetroLine line;
    for (RawDirection d : rdirs) {
      line = lines.get(d.line);
      // Create line.
      if (null == line) {
        int cnt = lines.size();
        String line_id = cityCode + "_LINE_" + cnt;
        line = new MetroLine(line_id, d.line);
        lines.put(d.line, line);
      }

      // Create direction.
      MetroDirection md = dirs.get(d.direction);
      if (null == md) {
        int idx = dirs.size();
        String dir_id = cityCode + "_DIR_" + idx;
        md = new MetroDirection(dir_id, d.direction, line, d.endpoint);
        dirs.put(d.direction, md);
      }
    }

    if (null != info) {
      // Add line info to line.
      for (LineInfo li : info) {
        line = lines.get(li.name);
        if (null == line) {
          throw new MetroException("LineInfo unknown line: " + li.name);
        }
        if (null != li.svtype) {
          line.m_svtype = li.svtype.toUpperCase();
          if (!"REGULAR".equals(line.m_svtype)
              && !"PARTIAL".equals(line.m_svtype)
              && !"EXTENDED".equals(line.m_svtype)) {
            throw new MetroException("Unknown service type: " + line.m_svtype);
          }
        }
        if (null != li.remark) {
          line.m_remark = li.remark;
        }
        if (null != li.editinfo) {
          line.m_editinfo = li.editinfo;
        }
      }
    }

    m_lines = Collections.unmodifiableMap(lines);
    m_dirs = Collections.unmodifiableMap(dirs);

    Set<MetroLine> lns = new LinkedHashSet<>();
    for (MetroLine ln : m_lines.values()) {
      lns.add(ln);
    }
    m_setLines = Collections.unmodifiableSet(lns);

    Set<MetroDirection> dr = new LinkedHashSet<>();
    for (MetroDirection dir : m_dirs.values()) {
      dr.add(dir);
    }
    m_setDirs = Collections.unmodifiableSet(dr);
  }

  @Override
  public MetroLine findLine(String name) throws MetroException {
    if (null == name) {
      throw new NullPointerException("name is null");
    }
    MetroLine line = m_lines.get(name);
    if (null == line) {
      throw new MetroException("Cannot find line: " + name);
    }
    return line;
  }

  @Override
  public MetroDirection findDirection(String name) throws MetroException {
    if (null == name) {
      throw new NullPointerException("name is null");
    }
    MetroDirection md = m_dirs.get(name);
    if (null == md) {
      throw new MetroException("Cannot find direction: " + name);
    }
    return md;
  }

  /**
   * Returns the MetroLine collection.
   * 
   * @return the MetroLine collection
   */
  public Set<MetroLine> getLines() {
    return m_setLines;
  }

  /**
   * Returns the MetroDirection collection.
   * 
   * @return the MetroDirection collection
   */
  public Set<MetroDirection> getDirections() {
    return m_setDirs;
  }
}
