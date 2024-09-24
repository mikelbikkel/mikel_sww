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

package org.subwayworld.routeservice.selftest;

import static org.junit.Assert.assertFalse;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.subwayworld.routeservice.ILocation;
import org.subwayworld.routeservice.RSException;
import org.subwayworld.routeservice.RouteResult;
import org.subwayworld.routeservice.RouteResult.RCODE;
import org.subwayworld.routeservice.RouteSegment;

/**
 * Batch test to calculate routes.
 * 
 */
public class TestBatch extends TestBase {

  private Date tsStart;

  public static void main(String[] args) {
    TestBatch t = new TestBatch();
    try {
      System.out.println("Start");
      t.initService();
      if ("a".equals(args[0])) {
        t.checkAllCities();
      } else if ("c".equals(args[0])) {
        t.checkCity(args[1]);
      } else if ("r".equals(args[0])) {
        t.checkRoute(args[1], args[2]);
      } else if ("d".equals(args[0])) {
        t.checkAllDistances();
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      t.stopService();
      System.out.println("Finished");
    }
  }

  private void checkAllCities() {
    PrintWriter pw = null;
    pw = openFile("checkall");
    if (null == pw) {
      return;
    }
    List<String> clist = null;
    try {
      clist = m_rs.getAllCityCodes();
    } catch (Exception e) {
      e.printStackTrace(pw);
      closeFile(pw);
      return;
    }
    try {
      for (String c : clist) {
        checkCityRoutes(c, pw);
      }
    } finally {
      closeFile(pw);
    }
  }

  private void checkCity(String cityCode) {
    PrintWriter pw;
    pw = openFile(cityCode);
    if (null == pw) {
      return;
    }
    try {
      checkCityRoutes(cityCode, pw);
    } finally {
      closeFile(pw);
    }
  }

  private void checkRoute(String start, String end) {
    PrintWriter pw;
    pw = openFile(start + "_" + end);
    if (null == pw) {
      return;
    }
    try {
      RouteResult rr = new RouteResult();
      RCODE res;
      DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
      java.util.Date tsStart2 = new java.util.Date();
      String hdr = start + " => " + end;
      pw.println(hdr + ": START. " + dateFormat.format(tsStart2));
      System.out.println(hdr);
      List<RouteSegment> rt = null;
      try {
        rt = m_rs.getRoute(start, end, null, rr);
      } catch (Exception e) {
        e.printStackTrace(pw);
        pw.println(hdr + ": ABORT.");
        pw.flush();
        System.out.println(hdr + ": ABORT.");
        return;
      }
      res = rr.getCode();
      if (RCODE.OK != res && RCODE.FROM_IS_TO != res) {
        System.out.println(hdr + ": " + res);
        pw.println(hdr + ": " + res);
      } else {
        String txt;
        for (RouteSegment rseg : rt) {
          assertFalse(null == rseg.getLocation());
          txt = printSegment(rseg);
          System.out.println(txt);
          pw.println(txt);
        }
      }
      java.util.Date tsEnd = new java.util.Date();
      long duration = (tsEnd.getTime() - tsStart.getTime()) / 1000;
      String dur = " [" + duration + " sec]";
      pw.println(hdr + ": END.   " + dateFormat.format(tsEnd) + dur);
      pw.flush();
      System.out.println(hdr + ": END.   " + dateFormat.format(tsEnd) + dur);
    } finally {
      closeFile(pw);
    }
  }

  private String printSegment(RouteSegment rs) {
    StringBuilder sb = new StringBuilder();
    sb.append(rs.toString());
    sb.append(" <");
    sb.append(rs.getDistance());
    sb.append(">. ");
    for (ILocation loc : rs.getStops()) {
      sb.append(loc.getName());
      sb.append(",");
    }
    return sb.toString();
  }

  private PrintWriter openFile(String basename) {
    PrintWriter pw = null;
    String fname = createFilename(basename);
    try {
      BufferedWriter bw = new BufferedWriter(new FileWriter(fname), 4096);
      pw = new PrintWriter(bw);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    tsStart = new java.util.Date();
    pw.println("Start : " + dateFormat.format(tsStart));
    return pw;
  }

  private void closeFile(PrintWriter pw) {
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    java.util.Date tsEnd = new java.util.Date();
    long duration = (tsEnd.getTime() - tsStart.getTime()) / 1000;
    String dur = " [" + duration + " sec]";
    pw.println("End : " + dateFormat.format(tsEnd) + dur);
    pw.flush();
    pw.close();
  }

  private void checkCityRoutes(String cityCode, PrintWriter pw) {
    RouteResult rr = new RouteResult();
    RCODE res;
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    java.util.Date tsStart2 = new java.util.Date();
    pw.print(cityCode + ": START. " + dateFormat.format(tsStart2));
    System.out.print(cityCode);
    List<String> stats = null;
    try {
      m_rs.setCity(cityCode);
      stats = m_rs.getAllLocationCodes(cityCode);
    } catch (Exception e) {
      e.printStackTrace(pw);
      return;
    }
    pw.println(".  [" + stats.size() + " stations/landmarks]");
    System.out.print(".  [" + stats.size() + " stations/landmarks]");
    int errCount = 0;
    int routeCnt = 0;
    for (String start : stats) {
      for (String end : stats) {
        routeCnt++;
        if (start.equals(end)) {
          continue;
        }
        rr.reset();
        try {
          m_rs.getRoute(start, end, null, rr);
        } catch (Exception e) {
          e.printStackTrace(pw);
        }
        res = rr.getCode();
        if (RCODE.OK != res) {
          errCount++;
          pw.println(start + " => " + end + ": " + res);
          System.out.println(start + " => " + end + ": " + res);
        }
      }
    }
    java.util.Date tsEnd = new java.util.Date();
    long duration = (tsEnd.getTime() - tsStart.getTime()) / 1000;
    String dur = " [" + duration + " sec]";
    String erc = " [" + +routeCnt + " routes. " + errCount + " errors]";
    pw.println(cityCode + ": END.   " + dateFormat.format(tsEnd) + dur + erc);
    pw.flush();
    System.out.println(dur + erc);
  }

  public void checkAllDistances() throws RSException {
    List<ILocation> locs = m_rs.testGetLocations();
    double dist;
    double lat;
    double lng;
    int i = 0;
    int numErrors = 0;
    for (ILocation fromLoc : locs) {
      if (!fromLoc.getHasGPSInfo()) {
        continue;
      }
      for (ILocation toLoc : locs) {
        if (toLoc.getHasGPSInfo()) {
          lat = toLoc.getGPSInfo().getLatitude();
          lng = toLoc.getGPSInfo().getLongitude();
          dist = fromLoc.getGPSInfo().getDistance(lat, lng);
          if (Double.isNaN(dist)) {
            numErrors++;
            System.out.println("NaN: " + fromLoc.getCode() + "-" + toLoc.getCode());
          }
          if (Double.isInfinite(dist)) {
            numErrors++;
            System.out.println("Infinite: " + fromLoc.getCode() + "-" + toLoc.getCode());
          }
          i++;
        }
      } // for toLoc
    } // for fromLoc
    System.out.println("Distance count: " + i);
    System.out.println("Error count: " + numErrors);
  }

  private String createFilename(String basename) {
    DateFormat df = new SimpleDateFormat("yyyyMMdd_HHmm");
    Date d = new Date();
    return basename + "_" + df.format(d) + ".log";
  }
}
