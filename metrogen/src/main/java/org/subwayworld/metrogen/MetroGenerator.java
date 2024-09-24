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

import java.io.InputStream;
import java.util.Set;

import org.subwayworld.metrogen.input.MetroData;
import org.subwayworld.metrogen.network.MetroNetwork;
import org.subwayworld.metrogen.network.MetroSegment;
import org.subwayworld.metrogen.output.OutputManager;
import org.subwayworld.metrogen.segparser.ParseException;
import org.subwayworld.metrogen.segparser.SegParser;

/**
 * Processes a metro network file.
 * <p>
 * Steps to process a metro network file:
 * <ul>
 * <li>Parse input file and collect raw data in a <code>MetroData</code>
 * structure.</li>
 * <li>Process the <code>MetroData</code> and store the result in a
 * <code>MetroNework</code> structure.</li>
 * <li>Generate output from the <code>MetroNework</code> structure.</li>
 * </ul>
 * Output can be generated in different formats and for different databases.<br>
 * Use commandline parameters to specify the output options.<br>
 */
public class MetroGenerator {

  private final MetroSettings mset;

  private final MessageWriter m_messages;

  public MetroGenerator(MetroSettings ms, MessageWriter mw) {
    mset = ms;
    m_messages = mw;
  }

  /**
   * Parse the network data and generate SQL output.
   * 
   * The parser creates a Reader from the bytes in the InputStream, using the
   * encoding from the MetroSettings.
   * 
   * @param in
   *          the network data, in the format of a byte stream.
   * @param inputName
   *          name that identified the network data. For example the filename.
   * @throws MetroException
   */
  public void go(InputStream in, String inputName) throws MetroException {
    if (null == in) {
      throw new MetroException(m_messages.get("null_param", "in"));
    }
    if (null == inputName) {
      throw new MetroException(m_messages.get("null_param", "inputName"));
    }

    m_messages.write("version_info");
    m_messages.write("parsing", inputName);

    MetroNetwork mn = parse(in);

    writeOutput(mn);

    m_messages.write("finished");
  }

  public MetroNetwork parse(InputStream in) throws MetroException {
    MetroData md = new MetroData();
    try {
      SegParser parser = new SegParser(in, mset.encoding);
      parser.setHandler(md);
      parser.CompilationUnit();
    } catch (ParseException e) {
      throw new MetroException(e);
    }
    return processMetroData2(md);
  }

  /**
   * Transforms the parsed entries into a metro network.
   */
  private MetroNetwork processMetroData2(MetroData mdat) throws MetroException {
    MetroNetwork mn2 = new MetroNetwork();
    mn2.city = mdat.m_city;

    LineManager2 lm2 = new LineManager2(mdat.m_city.code, mdat.m_directions,
        mdat.m_lines);
    mn2.dirs = lm2.getDirections();
    mn2.lines = lm2.getLines();

    StationManager2 sm2 = new StationManager2(mdat.m_city.id, mdat.m_city.code,
        mdat.m_stats, mdat.m_mnn);
    mn2.stats = sm2.getStations();

    SegmentManager2 segman2 = new SegmentManager2(mdat.m_city.code,
        mdat.m_segments, mdat.m_overrides, mdat.m_rawTrans, lm2, sm2);
    mn2.odirs = segman2.getOverrides();
    mn2.trans = segman2.getTransformations();
    mn2.traject_info = segman2.getTrajecten();

    Set<MetroSegment> segs = segman2.getSegments();
    NetworkManager2 netman2 = new NetworkManager2(segs, mdat.m_city.code);
    mn2.segs = netman2.getGraph();

    QualityManager2 qm2 = new QualityManager2();
    qm2.checkGraph(sm2.getStations());

    return mn2;
  }

  /**
   * Generates the output files for the metro network.
   */
  public void writeOutput(MetroNetwork mnet) throws MetroException {
    OutputManager oman = new OutputManager();
    oman.generateOutput(mnet, mset, m_messages);
  }

}
