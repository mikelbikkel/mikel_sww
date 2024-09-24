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
package org.subwayworld.routeservice.pathfinder;

import java.util.BitSet;

import org.subwayworld.routeservice.ILocation;

/**
 * A link is an edge in the metro network.
 */
public class Link2 {

  public final int id;

  public final int weight;

  /**
   * Node where edge starts.
   */
  public final Halte2 v;

  /**
   * Node where edge ends.
   */
  public final Halte2 w;

  /**
   * Pointer for linked list maintained by Halte.
   */
  public Link2 next;

  /**
   * Segment stops, including v and w.
   */
  public ILocation[] locations;

  /**
   * Direction information from segment.<br>
   * Initialized with value from segment when this Link2 is created.
   */
  public BitSet m_dirinfo;

  /**
   * Direction information for shortest path.<br>
   * Initialized to OL (null).
   */
  public BitSet m_spDirinfo;

  /**
   * Transfer type, relative to v.spe
   */
  public TransType sptype;

  public Link2() {
    this(-1, null, null, -1);
  }

  /**
   * Creates a new link.<br>
   * 
   * @param id
   *          edge identifier
   * @param v
   *          from vertex
   * @param w
   *          to vertex
   * @param weight
   *          the distance as used in shortest path calculations
   */
  public Link2(int id, Halte2 v, Halte2 w, int weight) {
    this.id = id;
    this.v = v;
    this.w = w;
    this.weight = weight;
    this.sptype = TransType.PF_NULL;
  }

  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append("[Lnk");
    sb.append(id);
    sb.append(": ");
    sb.append(v);
    sb.append("=>");
    sb.append(w);
    sb.append(" weight=");
    sb.append(weight);
    sb.append(". txtype=");
    sb.append(sptype);
    sb.append(". dirinfo=");
    sb.append(m_dirinfo);
    sb.append(". SPdirinfo=");
    sb.append(m_spDirinfo);
    sb.append("]");
    return sb.toString();
  }

  public boolean isTransfer(Link2 from) {
    if (null == from) {
      return false;
    }
    // Transfer means: the Directions of the two links do not intersect.
    boolean res = !from.m_spDirinfo.intersects(this.m_dirinfo);
    return res;
  }
}
