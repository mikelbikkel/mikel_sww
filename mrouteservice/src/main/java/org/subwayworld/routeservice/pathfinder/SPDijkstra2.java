/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.subwayworld.routeservice.pathfinder;

import java.util.BitSet;

import org.subwayworld.routeservice.DebugSettings;

public class SPDijkstra2 {

  private Halte2[] s;

  private Halte2[] q;

  private int cntQ;

  private int cntV;

  private static final int TRANSFER_COST = 4;
  private static final int TRANSFER_TO_DIRECT_LINE_RESTITUTION = 2;

  /**
   * Finds the shortest path.<br>
   * Conditions for shortest path:
   * <ul>
   * <li>Destination vertex is found.</li>
   * <li>Destination station is located on edge pointing to the destination vertex.</li>
   * </ul>
   * 
   * @param start
   *          start vertex.
   * @param end
   *          destination vertex.
   * @param dest
   *          name of the destination station.
   * @return true if route was found, false otherwise.
   */
  public Link2[] solve(Halte2[] v, Halte2 start, Halte2 end) {
    Halte2 u;
    int d;

    s = new Halte2[v.length];
    q = new Halte2[v.length];
    cntQ = v.length;
    cntV = v.length;

    if (DebugSettings.DEBUG_PRINT) {
      System.out.println("solve: " + start + " => " + end);
    }

    BitSet destinationDirs = new BitSet();
    destinationDirs.clear();
    for (int q2 = 0; q2 < cntV; q2++) {
      v[q2].getStraightDirectionsForDestination(end, destinationDirs);
    }
    // System.out.println("---------------------------------\n destinationDirs: "
    // + destinationDirs + "\n\n");

    /*
     * for (Link2 es = end.adj.next; null != es; es = es.next) {
     * 
     * System.out.println("---------------------------------");
     * System.out.println(es.locations[1].getName()); System.out.println(es.m_dirinfo);
     * System.out.println("---------------------------------"); destinationDirs = (BitSet)
     * es.m_dirinfo; }
     */

    // initialize
    for (int i = 0; i < this.cntV; i++) {
      this.s[i] = null;
      this.q[i] = v[i];
      this.q[i].spe = null;
      this.q[i].spd = Integer.MAX_VALUE;
    }

    u = start;
    u.spd = 0;
    if (DebugSettings.DEBUG_PRINT) {
      System.out.println("Start: " + u);
    }
    // the algorithm itself
    while (0 != this.cntQ) {
      u = findMinimumQ();
      if (null == u) {
        if (DebugSettings.DEBUG_PRINT) {
          System.out.println("solve: not found.");
        }
        return null;
      }
      if (DebugSettings.DEBUG_PRINT) {
        System.out.println("");
        System.out.println("Found minQ: " + u + " [" + u.spd + "]");
        System.out.println("  Edge: " + u.spe);
      }
      if (u.id == end.id) {
        if (DebugSettings.DEBUG_PRINT) {
          System.out.println("Found destination.");
        }
        Link2[] path = createPath(end);
        for (int ip = 0; ip < path.length; ip++) {
          if (DebugSettings.DEBUG_PRINT) {
            System.out.println("  Path[" + ip + "]: " + path[ip]);
          }
        }
        return path;
      }
      fromQtoS(u);

      for (Link2 e = u.adj.next; null != e; e = e.next) {
        if (DebugSettings.DEBUG_PRINT) {
          System.out.println("  Outgoing edge: " + e);
        }
        if (null == u.spe) {
          e.m_spDirinfo = (BitSet) e.m_dirinfo.clone();
        } else {
          e.m_spDirinfo = (BitSet) u.spe.m_spDirinfo.clone();
          e.m_spDirinfo.and(e.m_dirinfo);
        }
        d = u.spd + e.weight;

        if (0 == e.m_spDirinfo.cardinality()) {
          // This is a transfer. Now we must:
          // - determine the type of the transfer.
          // - calculate the cost of the transfer.
          // - add the cost to the distance.
          // - determine the new direction set.

          if (DebugSettings.DEBUG_PRINT) {
            System.out.println("  Transfer.");
          }
          // Assume a normal transfer.
          e.sptype = TransType.PF_TRANSFER;
          int cost = TRANSFER_COST;
          e.m_spDirinfo = (BitSet) e.m_dirinfo.clone();

          /*
           * if it's a transfer to a line directly connected to the destination station, don't count
           * a transfer penalty, because this may be an attractive transfer to make.
           */
          if (e.m_dirinfo.intersects(destinationDirs)) {
            if (DebugSettings.DEBUG_PRINT) {
              System.out.println("  Direct line.");
            }
            cost -= TRANSFER_TO_DIRECT_LINE_RESTITUTION;
            // System.out.println("Direct line found " + e.m_dirinfo + " -> "
            // + e.m_dirinfo.intersects(destinationDirs));

            // Remove all directions that are not direct lines.
            e.m_spDirinfo.and(destinationDirs);
          }

          // Now check if transfer is a metro transformation.
          BitSet bs = (BitSet) u.spe.m_spDirinfo.clone();
          bs.or(e.m_dirinfo);
          for (BitSet trans : u.m_transformations) {
            if (bs.intersects(trans)) {
              if (DebugSettings.DEBUG_PRINT) {
                System.out.println("  Transformation.");
              }
              e.sptype = TransType.PF_TRANSFORMATION;
              cost -= TRANSFER_COST;

              // Remove all directions that are not transformations.
              e.m_spDirinfo.and(trans);
              break;
            }
          }

          if (cost < 0) {
            // calculated cost can be less than zero in case of transformation
            // on a directly connected line.
            // Correct this, because the real cost must be greater or equal than
            // zero.
            cost = 0;
          }
          if (DebugSettings.DEBUG_PRINT) {
            System.out.println("  Transfer cost: " + cost);
          }
          // Add the transfer cost to the distance.
          d += cost;

        }
        if (d < e.w.spd) {
          e.w.spd = d;
          e.w.spe = e;
          if (DebugSettings.DEBUG_PRINT) {
            System.out.println("Add to fringe: " + e.w + " [" + e.w.spd + "]");
            System.out.println("  Edge: " + e.w.spe);
          }
        }
      }
    }
    return null;
  }

  private void fromQtoS(Halte2 v) {
    this.q[v.id] = null;
    this.s[v.id] = v;
    cntQ--;
  }

  private Halte2 findMinimumQ() {
    Halte2 min = null;
    int d = Integer.MAX_VALUE;
    for (int i = 0; i < this.cntV; i++) {
      if (null == this.q[i]) {
        continue;
      }
      if (this.q[i].spd < d) {
        d = this.q[i].spd;
        min = this.q[i];
      }
    }
    return min;
  }

  private Link2[] createPath(Halte2 end) {
    int cnt = 0;
    for (Link2 lnk = end.spe; null != lnk; lnk = lnk.v.spe) {
      cnt++;
    }
    Link2[] path = new Link2[cnt];
    cnt--;
    // The route calculated by Dijkstra starts at the destination
    // and points backward to the start.
    for (Link2 lnk = end.spe; null != lnk; lnk = lnk.v.spe) {
      path[cnt] = lnk;
      cnt--;
    }
    return path;
  }
}
