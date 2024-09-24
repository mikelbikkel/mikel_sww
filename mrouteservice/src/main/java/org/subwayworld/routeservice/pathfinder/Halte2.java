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
import org.subwayworld.routeservice.Landmark;
import org.subwayworld.routeservice.Station;

/**
 * A halte is an vertex in the metro network.
 */
public class Halte2 {

  /**
   * Identifier for Halte.<br>
   * SPDijkstra uses id as index into Node-arrays.
   */
  public final int id;

  /**
   * The location in the network that this halte represents.
   */
  public final ILocation loc;

  /**
   * Array of combined pre- and post-dircodes.<br>
   * Each element represents a metro transformation at this halte.
   * <p>
   * A zero-length array if the halte is not a transformation point.
   */
  public BitSet[] m_transformations;

  /**
   * Anchor for linked list of edges where halte is v.
   */
  public Link2 adj;

  /**
   * Edge for shortest path. <br>
   * This Halte2 is the W on spe.
   */
  public Link2 spe;

  /**
   * Distance for shortest path.
   */
  public int spd;

  public Halte2() {
    this(-1, null);
  }

  /**
   * Creates a new halte.
   * 
   * @param id
   *          halte identifier
   * @param node_id
   *          the node identifier
   * @param tr
   *          transform information
   */
  public Halte2(int id, ILocation node_id) {
    this.id = id;
    this.loc = node_id;
    m_transformations = new BitSet[0];
    adj = new Link2();
  }

  /**
   * Adds an adjacent edge to this vertex.
   * 
   * @param e
   *          an adjacent edge.
   * @throws IllegalArgumentException
   *           if e is not an adjacent edge.
   * @throws NullPointerException
   *           if e is null.
   */
  public void addEdge(Link2 e) {
    if (null == e) {
      throw new NullPointerException();
    }
    if (e.v.id != this.id) {
      throw new IllegalArgumentException();
    }

    e.next = adj.next;
    adj.next = e;
  }

  @Override
  public String toString() {
    return "[H" + id + ". Node: " + loc + "]";
  }

  /**
   * get outbound Links from this Halte that are inbound for destination Halte
   * hDest
   * 
   * @param hDest
   *          Destination Halte
   * @param destinationDirs
   *          BitSet to contain results (if any)
   */
  public void getStraightDirectionsForDestination(Halte2 hDest,
      BitSet destinationDirs) {

    String destination = hDest.loc.getCode();

    /*
     * When our destination is a landmark rather than a station, we must try all
     * to the Landmark associated stations as our direct-line destination. The
     * Landmark itself will never have a direct connection because there is
     * always a walk included.
     */
    if (hDest.loc.getClass().getSimpleName().equals("Landmark")) {
      Landmark lm = (Landmark) hDest.loc;

      // Concatenate a String containing all station codes
      destination = "";
      for (Station s : lm.getStations()) {
        destination += s.getCode() + " ";
      }
    }

    /*
     * Destination now contains at least one station code (when destination is a
     * station) and maybe more station codes (when destination is a landmark).
     * Example Landmark Paris: Bois de Boulogne: destination =
     * PRSBOULOGNE_JEAN_JAURES PRSMICHEL_ANGE_AUTEUIL PRSMICHEL_ANGE_MOLITOR
     * PRSPORTE_MAILLOT
     */
    // System.out.print ( destination );
    for (Link2 es = this.adj.next; null != es; es = es.next) {
      // System.out.println(this);
      if (destination.contains(es.w.loc.getCode())) {
        // System.out.println("---------------------------------");
        // System.out.println(es.w.loc.getCode() + ": " + es.m_dirinfo);
        destinationDirs.or(es.m_dirinfo);
      }
    }
  }
}
