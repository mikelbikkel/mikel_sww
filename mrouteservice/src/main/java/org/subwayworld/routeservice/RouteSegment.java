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

package org.subwayworld.routeservice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Represents an element of a shortest path.
 * 
 */
public class RouteSegment {

  /**
   * First location of this segment.
   */
  private final ILocation m_location;

  /*
   * Cannot use dircode and bitwise AND, because dircode 1L can occur multiple
   * times with different descriptions.
   * 
   * Example: walk from landmark and multi-named-node walk.
   */
  private List<SegmentDirection> m_dirs;

  private TransferType m_trans;

  /**
   * Locations on this route segment where the metro stops.
   * <p>
   * First and last location are not on this list. <br>
   * If segment has no intermediate locations, this is an empty list.
   */
  private List<ILocation> m_stops;

  /**
   * Distance between start of journey and start of this segment.<br>
   */
  private final int m_distance;

  /**
   * Creates a new route segment.
   * 
   * @param loc
   *          the start location of tis segment.
   * @param distance
   *          the distance between the start of the journey and the start of
   *          this segment.
   */
  public RouteSegment(ILocation loc, int distance) {
    m_location = loc;
    m_distance = distance;
  }

  /**
   * Removes all directions from the member set that do not exist in the
   * incoming set.
   * 
   * @param newdirs
   *          the incoming set.
   */
  public void mergeDirections(List<SegmentDirection> newdirs) {
    Iterator<SegmentDirection> iter = m_dirs.iterator();
    SegmentDirection dir;
    while (iter.hasNext()) {
      dir = iter.next();
      if (!newdirs.contains(dir)) {
        iter.remove();
      }
    }
  }

  public void addDirection(SegmentDirection dir) {
    if (null == dir) {
      return;
    }
    if (null == m_dirs) {
      m_dirs = new ArrayList<SegmentDirection>();
    }
    m_dirs.add(dir);
  }

  public void addStop(ILocation loc) {
    if (null == loc) {
      return;
    }
    if (null == m_stops) {
      m_stops = new ArrayList<ILocation>();
    }
    m_stops.add(loc);
  }

  public TransferType getTransferType() {
    return m_trans;
  }

  public void setTransferType(TransferType tt) {
    m_trans = tt;
  }

  public List<SegmentDirection> getDirections() {
    if (null == m_dirs) {
      return Collections.emptyList();
    }
    return m_dirs;
  }

  public ILocation getLocation() {
    return m_location;
  }

  /**
   * Locations on this route segment where the metro stops.
   * <p>
   * First and last location are not on this list. <br>
   * If segment has no intermediate stations, this is an empty list.
   */
  public List<ILocation> getStops() {
    if (null == m_stops) {
      return Collections.emptyList();
    }
    return m_stops;
  }

  /**
   * Returns the distance between the start of the journey and the start of this
   * segment.
   * 
   * @return the distance between the start of the journey and the start of this
   *         segment.
   */
  public int getDistance() {
    return m_distance;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(m_trans);
    sb.append(": ");
    sb.append(getLocation().getCode());
    sb.append(". (");
    sb.append(m_distance);
    sb.append("). ");
    String dirs = "";
    if (null != m_dirs) {
      dirs = m_dirs.toString();
    }
    sb.append("[");
    sb.append(dirs);
    sb.append("]");
    return sb.toString();
  }
}
