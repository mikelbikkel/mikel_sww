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

import java.util.LinkedHashSet;
import java.util.Set;

import org.subwayworld.metrogen.input.RawOverrideSegment2;
import org.subwayworld.metrogen.input.RawSegment;
import org.subwayworld.metrogen.input.RawTransformInfo;
import org.subwayworld.metrogen.network.DirectionOverride;
import org.subwayworld.metrogen.network.MetroDirection;
import org.subwayworld.metrogen.network.MetroSegment;
import org.subwayworld.metrogen.network.MetroStation;
import org.subwayworld.metrogen.network.MetroTraject;
import org.subwayworld.metrogen.network.TransformInfo;

/**
 * Creates Segments, Override information, Metro Transformations and Trajecten
 * from parsed input.
 * <p>
 * The SegmentManager is an immutable object.
 *
 */
public class SegmentManager2 {

  private Set<MetroSegment> m_segments;

  private Set<DirectionOverride> m_overrides;

  private Set<TransformInfo> m_transformations;

  private Set<MetroTraject> m_trajecten;

  /**
   * Use the other ctor to initialize the SegmentManager2.
   */
  @SuppressWarnings("unused")
  private SegmentManager2() {
    return;
  }

  /**
   * @param cityCode
   * @param rsegs
   *          the regular segments from the input file.
   * @param ovsegs
   *          the override segments from the input file.
   * @param lineFinder
   *          translates names into Direction objects.
   * @param statFinder
   *          translates names into Station objects.
   * @throws MetroException
   *           when something goes wrong.
   * @throws NullPointerException
   *           when argument is null.
   */
  public SegmentManager2(String cityCode, Set<RawSegment> rsegs,
      Set<RawOverrideSegment2> ovsegs, Set<RawTransformInfo> trans,
      ILineLookup lineFinder, IStationLookup statFinder) throws MetroException {
    if (null == cityCode || null == rsegs || null == trans || null == lineFinder
        || null == statFinder) {
      throw new NullPointerException();
    }

    m_segments = new LinkedHashSet<>();
    m_overrides = new LinkedHashSet<>();
    m_trajecten = new LinkedHashSet<>();
    m_transformations = new LinkedHashSet<>();

    processSegments(cityCode, rsegs, ovsegs, lineFinder, statFinder);
    processTransforms(trans, lineFinder, statFinder);
  }

  /**
   * Creates MetroSegments and DirectionOverrides from the input segments.
   *
   * @param rsegs
   *          the regular segments from the input file.
   * @param ovsegs
   *          the override segments from the input file.
   * @param lineFinder
   *          translates names into Direction objects.
   * @param statFinder
   *          translates names into Station objects.
   * @throws MetroException
   *           when something goes wrong.
   */
  private void processSegments(String cityCode, Set<RawSegment> rsegs,
      Set<RawOverrideSegment2> ovsegs, ILineLookup lineFinder,
      IStationLookup statFinder) throws MetroException {

    processRegularSegments(rsegs, lineFinder, statFinder);
    if (null != ovsegs) {
      processOverrideSegments(ovsegs, lineFinder, statFinder);
    }
    setTrajectenId(cityCode);
  }

  /**
   * Creates Metro Transformations from parsed information.
   * 
   * @param trans
   *          the parsed information
   * @param lineFinder
   * @param statFinder
   * @throws MetroException
   */
  private void processTransforms(Set<RawTransformInfo> trans,
      ILineLookup lineFinder, IStationLookup statFinder) throws MetroException {
    TransformInfo ti;
    MetroStation stat;
    MetroDirection pre;
    MetroDirection post;
    for (RawTransformInfo tr : trans) {
      stat = statFinder.findStation(tr.statCode);
      pre = lineFinder.findDirection(tr.preDirection);
      post = lineFinder.findDirection(tr.postDirection);
      ti = new TransformInfo(stat, pre, post);
      m_transformations.add(ti);
    }
  }

  private void processRegularSegments(Set<RawSegment> rsegs,
      ILineLookup lineFinder, IStationLookup statFinder) throws MetroException {
    MetroSegment seg;
    MetroStation stat;
    MetroDirection mdir;
    for (RawSegment rs : rsegs) {
      seg = new MetroSegment();
      for (String s : rs.stations) {
        stat = statFinder.findStation(s);
        seg.addMetroStation(stat);
      }

      mdir = lineFinder.findDirection(rs.fwdDirection);
      seg.addForward(mdir);

      if (null != rs.bckDirection) {
        mdir = lineFinder.findDirection(rs.bckDirection);
        seg.addBackward(mdir);
      }

      m_segments.add(seg);

      // Translate segment into forward traject and, optionally, a backward
      // traject.
      MetroTraject trj;
      trj = new MetroTraject(true, seg.m_stations);
      mdir = lineFinder.findDirection(rs.fwdDirection);
      trj.setForward(mdir);
      m_trajecten.add(trj);
      if (null != rs.bckDirection) {
        // if backward direction does not exist (rs.bckDirection = null), do not
        // create second traject.
        trj = new MetroTraject(false, seg.m_stations);
        mdir = lineFinder.findDirection(rs.bckDirection);
        trj.setForward(mdir);
        m_trajecten.add(trj);
      }
    }
  }

  private void processOverrideSegments(Set<RawOverrideSegment2> ovsegs,
      ILineLookup lineFinder, IStationLookup statFinder) throws MetroException {
    MetroSegment seg;
    MetroStation stat;
    String d;
    DirectionOverride dov;

    for (RawOverrideSegment2 rs : ovsegs) {
      d = rs.getDirection();
      MetroDirection mdir = lineFinder.findDirection(d);

      for (RawSegment raw : rs.getSegments()) {
        seg = new MetroSegment();
        MetroDirection ovdir = lineFinder.findDirection(raw.fwdDirection);
        for (String s : raw.stations) {
          stat = statFinder.findStation(s);
          seg.addMetroStation(stat);

          dov = new DirectionOverride(stat, mdir, ovdir);
          m_overrides.add(dov);
        }
        seg.addForward(mdir);
        m_segments.add(seg);

        // Translate sub-segment into forward traject.
        MetroTraject trj;
        trj = new MetroTraject(true, seg.m_stations);
        trj.setForward(ovdir);
        m_trajecten.add(trj);
      }
    }
  }

  private void setTrajectenId(String cityCode) {
    int i = 0;
    String id;
    // This works f the set is ordered in insertion order. And if the fwd/bck
    // trjacten of the same segment
    // are ordered: first forward, then backward.
    for (MetroTraject trj : m_trajecten) {
      if (trj.isForwardInFile) {
        i++;
        id = cityCode + "_TRJ_" + i + "_0_FWD";
      } else {
        id = cityCode + "_TRJ_" + i + "_1_BCK";
      }
      trj.m_id = id;
    }
  }

  public Set<MetroSegment> getSegments() {
    return m_segments;
  }

  public Set<DirectionOverride> getOverrides() {
    return m_overrides;
  }

  public Set<TransformInfo> getTransformations() {
    return m_transformations;
  }

  public Set<MetroTraject> getTrajecten() {
    return m_trajecten;
  }
}
