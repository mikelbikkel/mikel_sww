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
/**
 * 
 */
package org.subwayworld.routeservice.selection;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.subwayworld.routeservice.City;
import org.subwayworld.routeservice.CityInfo;
import org.subwayworld.routeservice.Continent;
import org.subwayworld.routeservice.Country;
import org.subwayworld.routeservice.ILocation;
import org.subwayworld.routeservice.Landmark;
import org.subwayworld.routeservice.NearestStation;
import org.subwayworld.routeservice.PublishMessage;
import org.subwayworld.routeservice.RSConnectionException;
import org.subwayworld.routeservice.RSDataException;
import org.subwayworld.routeservice.RSEnv;
import org.subwayworld.routeservice.SegmentDirection;
import org.subwayworld.routeservice.ServiceType;
import org.subwayworld.routeservice.Station;

public class RouteSelection {

  private Log m_log = LogFactory.getLog(getClass());

  private SelectionGateway m_storage;

  public RouteSelection() {
    this(null);
  }

  public RouteSelection(RSEnv env) {
    if (null == env) {
      throw new NullPointerException();
    }
    m_storage = new SelectionGateway();
    m_storage.setEnv(env);
  }

  /*
   * (non-Javadoc)
   * 
   * @see IRouteService#getStations(String, String)
   */
  public List<Station> getStations(String cityCode, String eel)
      throws RSConnectionException, RSDataException {
    if (null == cityCode) {
      throw new NullPointerException();
    }
    List<Station> cs = new ArrayList<Station>();
    try {
      m_storage.getStations(cityCode, eel, cs);
    } catch (SQLException e) {
      handleSQLException(e);
      return null;
    }
    return cs;
  }

  public List<CityInfo> getCityInfo(String cityCode)
      throws RSConnectionException, RSDataException {

    if (null == cityCode) {
      throw new NullPointerException();
    }
    List<CityInfo> cs = new ArrayList<CityInfo>();
    try {
      m_storage.getCityInfo(cityCode, cs);
    } catch (SQLException e) {
      handleSQLException(e);
      return null;
    }
    return cs;
  }

  /*
   * (non-Javadoc)
   * 
   * @see IRouteService#getLandmarks(String, String)
   */
  public List<Landmark> getLandmarks(String cityCode, String eel)
      throws RSConnectionException, RSDataException {
    if (null == cityCode) {
      throw new NullPointerException();
    }
    List<Landmark> marks = new ArrayList<Landmark>();
    try {
      m_storage.getLandmarks(cityCode, eel, marks);
    } catch (SQLException e) {
      handleSQLException(e);
      return null;
    }
    return marks;
  }

  /**
   * Returns all landmarks in a city.
   * 
   * @param cityCode
   *          city code
   * @return all landmakrs in a city
   * @throws RSConnectionException
   * @throws RSDataException
   * @throws NullPointerException
   *           if cityCode is null.
   */
  public List<Landmark> getLandmarksInCity(String cityCode)
      throws RSConnectionException, RSDataException {
    if (null == cityCode) {
      throw new NullPointerException();
    }
    List<Landmark> marks = new ArrayList<Landmark>();
    try {
      m_storage.getLandmarksInCity(cityCode, marks);
    } catch (SQLException e) {
      handleSQLException(e);
      return null;
    }
    return marks;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.subwayworld.routeservice.IRouteService#getLocationsWithEEL(java.lang
   * .String, java.lang.String)
   */
  public List<ILocation> getLocationsWithEEL(String cityCode, String eel)
      throws RSConnectionException, RSDataException {
    if (null == cityCode) {
      throw new NullPointerException();
    }
    List<ILocation> locs = new ArrayList<ILocation>();
    try {
      m_storage.getLocationsWithEEL(cityCode, eel, locs);
    } catch (SQLException e) {
      handleSQLException(e);
      return null;
    }
    return locs;
  }

  /*
   * (non-Javadoc)
   */
  public List<String> getAllLocationCodes(String cityCode)
      throws RSConnectionException, RSDataException {
    if (null == cityCode) {
      throw new NullPointerException();
    }
    List<String> cs = new ArrayList<String>();
    try {
      m_storage.getAllLocationCodes(cityCode, cs);
    } catch (SQLException e) {
      handleSQLException(e);
      return null;
    }
    return cs;
  }

  /*
   * (non-Javadoc)
   */
  public List<String> getAllCityCodes() throws RSConnectionException,
      RSDataException {
    List<String> cs = new ArrayList<String>();
    try {
      m_storage.getAllCityCodes(cs);
    } catch (SQLException e) {
      handleSQLException(e);
      return null;
    }
    return cs;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.subwayworld.routeservice.IRouteService#findCountry(String)
   */
  public Country findCountry(String code) throws RSConnectionException,
      RSDataException {
    Country cn = null;
    try {
      cn = m_storage.findCountry(code);
    } catch (SQLException e) {
      handleSQLException(e);
      cn = null;
    }
    return cn;
  }

  /*
   * (non-Javadoc)
   * 
   * @see RouteService#getCities(String)
   */
  public List<City> getCities(String ccode) throws RSConnectionException,
      RSDataException {
    if (null == ccode) {
      throw new NullPointerException();
    }
    List<City> ctset = new ArrayList<City>();
    try {
      m_storage.getCitiesForContinents(ctset, ccode);
    } catch (SQLException e) {
      handleSQLException(e);
      return null;
    }
    return ctset;
  }

  /*
   * (non-Javadoc)
   * 
   * @see RouteService#getContinents()
   */
  public List<Continent> getContinents() throws RSConnectionException,
      RSDataException {
    List<Continent> cs = new ArrayList<Continent>();
    try {
      m_storage.getContinents(cs);
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return cs;
  }

  /*
   * (non-Javadoc)
   * 
   * @see RouteService#getContinents()
   */
  public List<ILocation> testGetLocations() throws RSConnectionException,
      RSDataException {
    List<ILocation> locs = new ArrayList<ILocation>();
    try {
      m_storage.testGetLocations(locs);
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return locs;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.subwayworld.routeservice.IRouteService#getCountries()
   */
  public List<Country> getCountries(String continentCode)
      throws RSConnectionException, RSDataException {
    List<Country> countries = new ArrayList<Country>();
    if (null == continentCode) {
      throw new NullPointerException();
    }
    try {
      m_storage.getCountries(countries, continentCode);
    } catch (SQLException e) {
      handleSQLException(e);
      return null;
    }
    return countries;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.subwayworld.routeservice.IRouteService#getCountries()
   */
  public List<Country> getCountries() throws RSConnectionException,
      RSDataException {
    List<Country> countries = new ArrayList<Country>();
    try {
      m_storage.getCountries(countries);
    } catch (SQLException e) {
      handleSQLException(e);
      return null;
    }
    return countries;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.subwayworld.routeservice.IRouteService#getCitiesForCountry(String)
   */
  public List<City> getCitiesForCountry(String countryCode)
      throws RSConnectionException, RSDataException {
    List<City> cities = new ArrayList<City>();
    if (null == countryCode) {
      throw new NullPointerException();
    }
    try {
      m_storage.getCitiesForCountry(cities, countryCode);
    } catch (SQLException e) {
      handleSQLException(e);
      return null;
    }
    return cities;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.subwayworld.routeservice.IRouteService#findContinent(java.lang
   * .String)
   */
  public Continent findContinent(String code) throws RSConnectionException,
      RSDataException {
    Continent con = null;
    try {
      con = m_storage.findContinent(code);
    } catch (SQLException e) {
      handleSQLException(e);
      con = null;
    }
    return con;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.subwayworld.routeservice.IRouteService#findCity(java.lang.String)
   */
  public City findCity(String code) throws RSConnectionException,
      RSDataException {
    City ct = null;
    try {
      ct = m_storage.findCity(code);
    } catch (SQLException e) {
      handleSQLException(e);
      ct = null;
    }
    return ct;
  }

  /**
   * Returns a Station object for a station code.
   * 
   * @param code
   *          Station code, for example RDMBLAAK.
   * 
   * @return a station object.
   * 
   * @throws RSConnectionException
   *           if connection error with Metro Data Storage occurs.
   * 
   * @throws RSDataException
   *           if data cannot be fetched from Metro Data Storage.
   */
  public Station findStation(String code) throws RSConnectionException,
      RSDataException {
    Station stat = null;
    try {
      stat = m_storage.findStation(code);
    } catch (SQLException e) {
      handleSQLException(e);
      stat = null;
    }
    return stat;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.subwayworld.routeservice.IRouteService#findLandmark(String)
   */
  public Landmark findLandmark(String code) throws RSConnectionException,
      RSDataException {
    Landmark mark = null;
    try {
      mark = m_storage.findLandmark(code);
    } catch (SQLException e) {
      handleSQLException(e);
      mark = null;
    }
    return mark;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.subwayworld.routeservice.IRouteService#getStationEELs(String)
   */
  public List<String> getStationEELs(String cityCode)
      throws RSConnectionException, RSDataException {
    if (null == cityCode) {
      throw new NullPointerException();
    }
    List<String> eels = new ArrayList<String>();
    try {
      m_storage.getStationEELs(cityCode, eels);
    } catch (SQLException e) {
      handleSQLException(e);
      return null;
    }
    return eels;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.subwayworld.routeservice.IRouteService#getLandmarkEELs(String)
   */
  public List<String> getLandmarkEELs(String cityCode)
      throws RSConnectionException, RSDataException {
    if (null == cityCode) {
      throw new NullPointerException();
    }
    List<String> eels = new ArrayList<String>();
    try {
      m_storage.getLandmarkEELs(cityCode, eels);
    } catch (SQLException e) {
      handleSQLException(e);
      return null;
    }
    return eels;
  }

  public List<String> getLocationEELs(String cityCode)
      throws RSConnectionException, RSDataException {
    if (null == cityCode) {
      throw new NullPointerException();
    }
    List<String> lst = new ArrayList<String>();
    try {
      m_storage.getEELsForLocations(cityCode, lst);
    } catch (SQLException e) {
      handleSQLException(e);
      return null;
    }
    return lst;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.subwayworld.routeservice.IRouteService#getServiceTypes(String)
   */
  public EnumSet<ServiceType> getServiceTypes(String cityCode)
      throws RSConnectionException, RSDataException {
    if (null == cityCode) {
      throw new NullPointerException();
    }
    EnumSet<ServiceType> es = EnumSet.noneOf(ServiceType.class);
    try {
      m_storage.getServiceTypes(cityCode, es);
    } catch (SQLException e) {
      handleSQLException(e);
      return null;
    }
    return es;

  }

  /*
   * (non-Javadoc)
   * 
   * @see org.subwayworld.routeservice.IRouteService#findNearestCity(double,
   * double)
   */
  public City findNearestCity(double latitude, double longitude)
      throws RSConnectionException, RSDataException {
    List<City> sg = new ArrayList<City>();
    try {
      m_storage.getCityGPS(sg);
    } catch (SQLException e) {
      handleSQLException(e);
      return null;
    }
    double minDist = Double.MAX_VALUE;
    City nearestCity = null;
    double dist;
    for (City c : sg) {
      dist = c.getGPSInfo().getDistance(latitude, longitude);
      if (dist < minDist) {
        minDist = dist;
        nearestCity = c;
      }
    }
    return nearestCity;
  }

  /**
   * Returns all stations in city with GPS information.
   * 
   * @param cityCode
   * @param latitude
   * @param longitude
   * @param maxKilometers
   * @param stations
   * @throws RSConnectionException
   * @throws RSDataException
   */
  public void findStationsWithGPSInfo(String cityCode,
      List<NearestStation> stations) throws RSConnectionException,
      RSDataException {
    try {
      m_storage.getStationsWithGPSInfo(cityCode, stations);
    } catch (SQLException e) {
      handleSQLException(e);
      return;
    }
  }

  public ILocation getLocation(String loc) throws RSConnectionException,
      RSDataException {
    assert null != loc;
    ILocation location = null;
    try {
      location = m_storage.getLocation(loc);
    } catch (SQLException e) {
      handleSQLException(e);
      location = null;
    }
    return location;
  }

  public List<PublishMessage> getNewMessages() throws RSConnectionException,
      RSDataException {
    List<PublishMessage> lst = new ArrayList<PublishMessage>();
    try {
      m_storage.getNewMessages(lst);
    } catch (SQLException e) {
      handleSQLException(e);
      return Collections.emptyList();
    }
    return lst;
  }

  public List<ILocation> getFilteredLocations(String cityCode, String filter)
      throws RSConnectionException, RSDataException {
    if (null == cityCode) {
      throw new NullPointerException();
    }
    List<ILocation> cs = new ArrayList<ILocation>();
    try {
      m_storage.getFilteredLocations(cityCode, cs, filter);
    } catch (SQLException e) {
      handleSQLException(e);
      return null;
    }
    return cs;
  }

  /**
   * Returns all departure directions on a station.
   * 
   * @param stationCode
   *          the station code, for example RDMBLAAK.
   * @return all directions on a station, or null if an error occurs.
   * 
   * @throws RSConnectionException
   *           if SWW database connection fails.
   * 
   * @throws RSDataException
   *           if SWW database processing fails.
   */
  public List<SegmentDirection> getStationDepartures(String stationCode)
      throws RSConnectionException, RSDataException {
    List<String> stats = new ArrayList<String>();
    List<SegmentDirection> deps = new ArrayList<SegmentDirection>();
    try {
      // Check if station is a complex.
      // If complex: find departures for all stations that are part of the
      // complex.
      m_storage.getCodesForStation(stationCode, stats);
      for (String s : stats) {
        m_storage.getOverrideSegDirections(s, deps);
        m_storage.getNoOverrideSegDirections(s, deps);
      }
    } catch (SQLException e) {
      handleSQLException(e);
    }
    return deps;
  }

  /**
   * Handles an SQLException.
   * <p>
   * The SQLException is logged at error level and transformed into an
   * appropriate RouteService exception. Retry-able errors are sql state:
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
  private void handleSQLException(SQLException e) throws RSConnectionException,
      RSDataException {
    if (m_log.isErrorEnabled()) {
      String msg;
      msg = "SQLException: " + e.getSQLState() + " [ " + e.getErrorCode()
          + " ].";
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

}
