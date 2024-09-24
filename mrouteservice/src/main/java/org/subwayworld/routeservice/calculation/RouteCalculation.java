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
/**
 * 
 */
package org.subwayworld.routeservice.calculation;

import java.sql.SQLException;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.subwayworld.routeservice.RSCalculationException;
import org.subwayworld.routeservice.RSConnectionException;
import org.subwayworld.routeservice.RSDataException;
import org.subwayworld.routeservice.RSEnv;
import org.subwayworld.routeservice.RSException;
import org.subwayworld.routeservice.RouteResult;
import org.subwayworld.routeservice.RouteResult.RCODE;
import org.subwayworld.routeservice.RouteSegment;
import org.subwayworld.routeservice.ServiceType;

/**
 * @author Michel
 * 
 */
public class RouteCalculation {

  private Log m_log = LogFactory.getLog(getClass());

  private CalculationGateway m_storage;

  private MetroInfo m_info;

  public RouteCalculation() {
    this(null);
  }

  public RouteCalculation(RSEnv env) {
    if (null == env) {
      throw new NullPointerException();
    }
    m_storage = new CalculationGateway();
    m_storage.setEnv(env);
  }

  public void setCity(String cityCode) throws RSException {
    try {
      m_info = getMetroInfo(cityCode);
    } catch (SQLException e) {
      handleSQLException(e);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.subwayworld.routeservice.IRouteService#getRoute(String, boolean, String, boolean,
   * EnumSet<ServiceType>, RouteResult)
   */
  public List<RouteSegment> getRoute(String start, String end, EnumSet<ServiceType> stRequest,
      RouteResult rr) throws RSException {
    List<RouteSegment> lst = null;

    if (null == rr) {
      // create dummy so code below always has a rr that is not null.
      rr = new RouteResult();
    }

    // Input validation.
    if (!checkRouteInput(start, end, rr)) {
      return Collections.emptyList();
    }
    try {
      MetroInfo mi;
      /*
       * Return error FROM_UNKNOWN if city code in station name (RDMBLAAK: RDM) cannot be used to
       * find the city.
       */
      if (null == m_info) {
        String ct = m_storage.findCityForStation(start);
        if (null == ct) {
          // If we get here, we cannot find a city with a city code equal to the
          // first 3 characters from the station name.
          rr.setCode(RCODE.FROM_UNKNOWN);
          return Collections.emptyList();
        }
        mi = getMetroInfo(ct);
      } else {
        String ccode = start.substring(0, 3);
        if (!ccode.equals(m_info.m_cityId)) {
          // If we get here, the in-memory city code does not match the first 3
          // characters from the station name.
          rr.setCode(RCODE.FROM_UNKNOWN);
          return Collections.emptyList();
        }
        mi = m_info;
      }
      rr.setCode(RCODE.OK);

      // Calculate shortest route.
      lst = mi.shortestPath(start, end, stRequest, rr);

    } catch (SQLException e) {
      rr.setCode(RCODE.SQL_ERROR);
      handleSQLException(e);
      assert false;
      return Collections.emptyList(); // this call is never executed.
    } catch (RSCalculationException e2) {
      rr.setCode(RCODE.CALC_ERROR);
      // Wrap calculation error in DataException, to keep interface signature
      // unchanged.
      throw new RSDataException(e2);
    }

    // [4] Return shortest route.
    return lst;
  }

  /**
   * Handles an SQLException.
   * <p>
   * The SQLException is logged at error level and transformed into an appropriate RouteService
   * exception. Retry-able errors are sql state:
   * <ul>
   * <li>08S01, communications error.</li>
   * <li>40001, deadlock.</li>
   * </ul>
   * 
   * @param e
   *          the SQLException that was caught.
   * @throws RSConnectionException
   *           if the SQLException is a retry-able error.
   * @throws RSDataException
   *           if the SQLException is not a retry-able error.
   */
  private void handleSQLException(SQLException e) throws RSConnectionException, RSDataException {
    if (m_log.isErrorEnabled()) {
      String msg;
      msg = "SQLException: " + e.getSQLState() + " [ " + e.getErrorCode() + " ].";
      m_log.error(msg, e);
    }
    // Get a properly formatted sqlstate
    String sqlstate = e.getSQLState();
    sqlstate = sqlstate.toUpperCase().trim();

    // Two sqlstates indicate an error where retry is possible:
    // 08S01: network communication error
    // 40001: deadlock
    if ("08S01".equals(sqlstate) || "40001".equals(sqlstate)) {
      RSConnectionException ce = new RSConnectionException(e);
      throw ce;
    } else {
      RSDataException de = new RSDataException(e);
      throw de;
    }
    // SQL state 08S01: Communications error.
    // This SQL state is used by multiple DBs: Oracle (ORA-03113), MySQL,
    // DB2 and SQL Server.
    // if ("08S01".equals(e.getSQLState())) { reconnect?
  }

  private MetroInfo getMetroInfo(String cityCode) throws RSException, SQLException {
    MetroInfo mi = new MetroInfo();

    mi.m_cityCode = cityCode;
    mi.m_cityId = m_storage.findCityCode(cityCode);

    m_storage.getStationsNodes(mi.m_cityCode, mi.m_statMap);
    m_storage.getLineDirections(mi.m_cityCode, mi.m_dirs);
    m_storage.getSegmentInfo(mi.m_cityCode, mi.m_segs, mi.m_statMap);
    m_storage.getMNNSegments(mi.m_cityCode, mi.m_segs, mi.m_statMap);
    m_storage.getSegmentDirections(mi.m_cityCode, mi.m_segs, mi.m_dirs);
    m_storage.getTransformations(mi.m_cityCode, mi.m_trans, mi.m_statMap, mi.m_dirs);
    m_storage.getLandmarks(mi.m_cityCode, mi.m_marks, mi.m_statMap);
    m_storage.getComplexInfo(mi.m_cityCode, mi.m_complexMap, mi.m_statMap);
    mi.addSegmentNodeInfo();
    m_storage.getDirectionOverrides(mi.m_cityCode, mi.m_dirOverrides);
    return mi;
  }

  private boolean checkRouteInput(String scodeFrom, String scodeTo, RouteResult rr) {
    if (null == scodeFrom) {
      rr.setCode(RCODE.FROM_NULL);
      return false;
    }
    if (null == scodeTo) {
      rr.setCode(RCODE.TO_NULL);
      return false;
    }
    if (scodeFrom.equals(scodeTo)) {
      rr.setCode(RCODE.FROM_IS_TO);
      return false;
    }
    String ctS = scodeFrom.substring(0, 3);
    String ctEnd = scodeTo.substring(0, 3);
    if (!ctS.equals(ctEnd)) {
      rr.setCode(RCODE.CITY);
      return false;
    }
    return true;
  }
}
