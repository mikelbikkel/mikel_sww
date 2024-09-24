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

package org.subwayworld.routeservice.selection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

import org.subwayworld.routeservice.City;
import org.subwayworld.routeservice.CityInfo;
import org.subwayworld.routeservice.Continent;
import org.subwayworld.routeservice.Country;
import org.subwayworld.routeservice.ILocation;
import org.subwayworld.routeservice.IPublishInfo;
import org.subwayworld.routeservice.Landmark;
import org.subwayworld.routeservice.NearestStation;
import org.subwayworld.routeservice.PublishMessage;
import org.subwayworld.routeservice.RSConnectionException;
import org.subwayworld.routeservice.RSEnv;
import org.subwayworld.routeservice.SegmentDirection;
import org.subwayworld.routeservice.ServiceType;
import org.subwayworld.routeservice.Station;

/**
 * Access the Metro database.
 * 
 */
public class SelectionGateway {

  private RSEnv m_env;

  public void setEnv(RSEnv env) {
    m_env = env;
  }

  public Continent findContinent(String code) throws SQLException, RSConnectionException {
    String qry = "select continent_id, continent_desc, info_status, info_timestamp, info_message "
        + " from rs_continent_publish where continent_id = ?"
        + " order by continent_desc, info_timestamp";
    Continent con = null;
    Connection dbcon = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      dbcon = m_env.getDBConnection();
      stmt = dbcon.prepareStatement(qry);
      stmt.setString(1, code);
      rs = stmt.executeQuery();
      while (rs.next()) {
        if (null == con) {
          con = createContinent(rs);
        }
        createPublishMessage(rs, con, con.getCode(), con.getName());
      }
    } finally {
      closeQuery(stmt, rs);
      m_env.disconnect(dbcon);
    }
    return con;
  }

  public Country findCountry(String code) throws SQLException, RSConnectionException {
    String qry = "select country_id, country_desc, info_status, info_timestamp, info_message "
        + " from rs_country_publish where country_id = ?";
    Country cntry = null;
    Connection dbcon = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      dbcon = m_env.getDBConnection();
      stmt = dbcon.prepareStatement(qry);
      stmt.setString(1, code);
      rs = stmt.executeQuery();
      while (rs.next()) {
        if (null == cntry) {
          cntry = createCountry(rs);
        }
        createPublishMessage(rs, cntry, cntry.getCode(), cntry.getName());
      }
    } finally {
      closeQuery(stmt, rs);
      m_env.disconnect(dbcon);
    }
    return cntry;
  }

  private static final String sql_city_select = "select city_id, city_code, city_desc, local_name, "
      + " country_id, country_desc, continent_id, continent_desc,"
      + " info_status, info_timestamp, info_message, "
      + " latitude, longitude from rs_city_publish ";

  private static final String sql_city_order = " order by city_desc, info_timestamp";

  public City findCity(String code) throws SQLException, RSConnectionException {
    String qry = sql_city_select + " where city_id = ? " + sql_city_order;
    City ct = null;
    Connection dbcon = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      dbcon = m_env.getDBConnection();
      stmt = dbcon.prepareStatement(qry);
      stmt.setString(1, code);
      rs = stmt.executeQuery();
      while (rs.next()) {
        if (null == ct) {
          ct = createCity(rs);
        }
        createPublishMessage(rs, ct, ct.getCode(), ct.getName());
      }
    } finally {
      closeQuery(stmt, rs);
      m_env.disconnect(dbcon);
    }
    return ct;
  }

  public Station findStation(String code) throws SQLException, RSConnectionException {
    String qry = "select station_id, description, local_name, station_type, "
        + " latitude, longitude from rs_network_station where station_id = ?";
    Station stat = null;
    Connection dbcon = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      dbcon = m_env.getDBConnection();
      stmt = dbcon.prepareStatement(qry);
      stmt.setString(1, code);
      rs = stmt.executeQuery();
      while (rs.next()) {
        stat = createStation(rs);
        assert null != stat;
      }
    } finally {
      closeQuery(stmt, rs);
      m_env.disconnect(dbcon);
    }
    return stat;
  }

  public Landmark findLandmark(String code) throws SQLException, RSConnectionException {
    String qry = "select landmark_id, description, local_name, "
        + " latitude, longitude from rs_landmark where landmark_id = ?";
    String id;
    String desc;
    String local_name;
    double lat;
    double lon;
    Landmark mark = null;
    Connection dbcon = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      dbcon = m_env.getDBConnection();
      stmt = dbcon.prepareStatement(qry);
      stmt.setString(1, code);
      rs = stmt.executeQuery();
      while (rs.next()) {
        id = rs.getString("landmark_id");
        desc = rs.getString("description");
        local_name = rs.getString("local_name");
        if (rs.wasNull()) {
          local_name = null;
        }
        lat = rs.getDouble("latitude");
        lon = rs.getDouble("longitude");
        if (rs.wasNull()) {
          mark = new Landmark(id, desc, local_name);
        } else {
          mark = new Landmark(id, desc, local_name, lat, lon);
        }
        assert null != mark;
      }
    } finally {
      closeQuery(stmt, rs);
      m_env.disconnect(dbcon);
    }
    return mark;
  }

  public void getCitiesForContinents(List<City> cityset, String ccode)
      throws SQLException, RSConnectionException {
    String qry = sql_city_select + " where continent_id = ? " + sql_city_order;
    String city_desc;
    City ct = null;
    Connection dbcon = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String currentCity = "";
    try {
      dbcon = m_env.getDBConnection();
      stmt = dbcon.prepareStatement(qry);
      stmt.setString(1, ccode);
      rs = stmt.executeQuery();
      while (rs.next()) {
        // If information messages are available, multiple records exist
        // for one city.
        city_desc = rs.getString("city_desc");
        if (!currentCity.equals(city_desc)) {
          // This desc is not same as previous, so a new city is found.
          // First, create a new city.
          ct = createCity(rs);

          // Second, add city to set and make the new city the current city
          cityset.add(ct);
          currentCity = city_desc;
        }
        if (null != ct) {
          createPublishMessage(rs, ct, ct.getCode(), ct.getName());
        }
      }
    } finally {
      closeQuery(stmt, rs);
      m_env.disconnect(dbcon);
    }
  }

  public void getCitiesForCountry(List<City> cityset, String countryCode)
      throws SQLException, RSConnectionException {
    String qry = sql_city_select + " where country_id = ? " + sql_city_order;
    String desc;
    City ct = null;
    Connection dbcon = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String currentCity = "";
    try {
      dbcon = m_env.getDBConnection();
      stmt = dbcon.prepareStatement(qry);
      stmt.setString(1, countryCode);
      rs = stmt.executeQuery();
      while (rs.next()) {
        // If information messages are available, multiple records exist
        // for one city.
        desc = rs.getString("city_desc");
        if (!currentCity.equals(desc)) {
          ct = createCity(rs);

          // Second, add city to set and make the new city the current city
          cityset.add(ct);
          currentCity = desc;
        }
        if (null != ct) {
          createPublishMessage(rs, ct, ct.getCode(), ct.getName());
        }
      }
    } finally {
      closeQuery(stmt, rs);
      m_env.disconnect(dbcon);
    }
  }

  public void getContinents(List<Continent> conset) throws SQLException, RSConnectionException {
    String qry = "select continent_id, continent_desc, info_status, info_timestamp, info_message "
        + " from rs_continent_publish" + " order by continent_desc, info_timestamp";
    String desc;
    Continent con = null;
    Connection dbcon = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String currentContinent = "";
    try {
      dbcon = m_env.getDBConnection();
      stmt = dbcon.prepareStatement(qry);
      rs = stmt.executeQuery();
      while (rs.next()) {
        // If information messages are available, multiple records exist
        // for one continent.
        desc = rs.getString("continent_desc");
        if (!currentContinent.equals(desc)) {
          // This desc is not same as previous, so a new continent is found.
          // Create a new continent
          con = createContinent(rs);

          // Add to set, and make the new continent the current continent
          conset.add(con);
          currentContinent = desc;
        }
        if (null != con) {
          createPublishMessage(rs, con, con.getCode(), con.getName());
        }
      }
    } finally {
      closeQuery(stmt, rs);
      m_env.disconnect(dbcon);
    }
  }

  /**
   * Adds countries with a metro network to the list.
   * 
   * @param countries
   *          The list to add the countries to.
   * @param continentCode
   *          Code of the continent for which cities are requested.
   * @throws SQLException
   *           If an error occurs.
   */
  public void getCountries(List<Country> countries, String continentCode)
      throws SQLException, RSConnectionException {
    String qry = "select country_id, country_desc, info_status, info_timestamp, info_message "
        + " from rs_country_publish where continent_id = ? "
        + " order by country_desc, info_timestamp";
    Country cntry = null;
    Connection dbcon = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String currentCountry = "";
    String desc;
    try {
      dbcon = m_env.getDBConnection();
      stmt = dbcon.prepareStatement(qry);
      stmt.setString(1, continentCode);
      rs = stmt.executeQuery();
      while (rs.next()) {
        // If information messages are available, multiple records exist
        // for one continent.
        desc = rs.getString("country_desc");
        if (!currentCountry.equals(desc)) {
          cntry = createCountry(rs);
          countries.add(cntry);
          currentCountry = desc;
        }
        if (null != cntry) {
          createPublishMessage(rs, cntry, cntry.getCode(), cntry.getName());
        }
      }
    } finally {
      closeQuery(stmt, rs);
      m_env.disconnect(dbcon);
    }
  }

  /**
   * Adds ALL countries WORLDWIDE with a metro network to the list.
   * 
   * @param countries
   *          The list to add the countries to.
   * @throws SQLException
   *           If an error occurs.
   */
  public void getCountries(List<Country> countries) throws SQLException, RSConnectionException {
    String qry = "select country_id, country_desc, info_status, info_timestamp, info_message "
        + " from rs_country_publish " + " order by country_desc, info_timestamp";
    Country cntry = null;
    Connection dbcon = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String currentCountry = "";
    String desc;
    try {
      dbcon = m_env.getDBConnection();
      stmt = dbcon.prepareStatement(qry);
      rs = stmt.executeQuery();
      while (rs.next()) {
        // If information messages are available, multiple records exist
        // for one continent.
        desc = rs.getString("country_desc");
        if (!currentCountry.equals(desc)) {
          cntry = createCountry(rs);
          countries.add(cntry);
          currentCountry = desc;
        }
        if (null != cntry) {
          createPublishMessage(rs, cntry, cntry.getCode(), cntry.getName());
        }
      }
    } finally {
      closeQuery(stmt, rs);
      m_env.disconnect(dbcon);
    }
  }

  /**
   * @param cityCode
   * @param statset
   * @throws SQLException
   */
  public void getStations(String cityCode, String eel, List<Station> statset)
      throws SQLException, RSConnectionException {
    String qry = "select station_id, description, local_name, station_type, "
        + " latitude, longitude from rs_station_eel "
        + "where city_id = ? and eel = ? order by description";
    Station stat;
    Connection dbcon = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      dbcon = m_env.getDBConnection();
      stmt = dbcon.prepareStatement(qry);
      stmt.setString(1, cityCode);
      stmt.setString(2, eel);
      rs = stmt.executeQuery();
      while (rs.next()) {
        stat = createStation(rs);
        assert null != stat;
        statset.add(stat);
      }
    } finally {
      closeQuery(stmt, rs);
      m_env.disconnect(dbcon);
    }
  }

  /**
   * @param cityCode
   * @param ciset
   * @throws SQLException
   */
  public void getCityInfo(String cityCode, List<CityInfo> ciset)
      throws SQLException, RSConnectionException {
    String qry = "select city_id, url, category, description, validUrl " + "from city_info "
        + "where city_id = ? order by indexno, category, description";
    String city_id;
    String url, cat, desc;
    boolean isValidUrl;
    CityInfo ci;
    Connection dbcon = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      dbcon = m_env.getDBConnection();
      stmt = dbcon.prepareStatement(qry);
      stmt.setString(1, cityCode);
      rs = stmt.executeQuery();
      while (rs.next()) {
        city_id = rs.getString("city_id");
        url = rs.getString("url");
        cat = rs.getString("category");
        desc = rs.getString("description");
        isValidUrl = rs.getBoolean("validUrl");
        ci = new CityInfo(city_id, url, cat, desc, isValidUrl);
        ciset.add(ci);
        ci = null;
      }
    } finally {
      closeQuery(stmt, rs);
      m_env.disconnect(dbcon);
    }
  }

  /**
   * @param cityCode
   * @param statset
   * @throws SQLException
   */
  public void getAllLocationCodes(String cityCode, List<String> statset)
      throws SQLException, RSConnectionException {
    String qry = "select station_id as loc_id from rs_display_station " + "where city_id = ? union "
        + "select landmark_id as loc_id from rs_landmark " + "where city_id = ?";
    String id;
    Connection dbcon = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      dbcon = m_env.getDBConnection();
      stmt = dbcon.prepareStatement(qry);
      stmt.setString(1, cityCode);
      stmt.setString(2, cityCode);
      rs = stmt.executeQuery();
      while (rs.next()) {
        id = rs.getString("loc_id");
        statset.add(id);
      }
    } finally {
      closeQuery(stmt, rs);
      m_env.disconnect(dbcon);
    }
  }

  /**
   * @param cityCode
   * @param statset
   * @throws SQLException
   */
  public void testGetLocations(List<ILocation> statset) throws SQLException, RSConnectionException {
    String qry = "select location_id, description, local_name, station_type, "
        + " latitude, longitude, location_type "
        + " from rs_locations where location_type in ('STATION', 'LANDMARK', 'CITY')";
    Connection dbcon = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    ILocation location = null;
    try {
      dbcon = m_env.getDBConnection();
      stmt = dbcon.prepareStatement(qry);
      rs = stmt.executeQuery();
      while (rs.next()) {
        location = createLocation(rs);
        statset.add(location);
      }
    } finally {
      closeQuery(stmt, rs);
      m_env.disconnect(dbcon);
    }
  }

  /**
   * @param statset
   * @throws SQLException
   */
  public void getAllCityCodes(List<String> statset) throws SQLException, RSConnectionException {
    String qry = "select distinct city_id from rs_city_publish order by city_id";
    String id;
    Connection dbcon = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      dbcon = m_env.getDBConnection();
      stmt = dbcon.prepareStatement(qry);
      rs = stmt.executeQuery();
      while (rs.next()) {
        id = rs.getString("city_id");
        statset.add(id);
      }
    } finally {
      closeQuery(stmt, rs);
      m_env.disconnect(dbcon);
    }
  }

  /**
   * @param cityCode
   * @param statset
   * @throws SQLException
   */
  public void getLandmarks(String cityCode, String eel, List<Landmark> marks)
      throws SQLException, RSConnectionException {
    String qry = "select landmark_id, description, local_name, latitude, longitude"
        + " from rs_landmark_eel where city_id = ? and eel = ? " + " order by description";
    String id;
    String desc;
    String local_name;
    double lat;
    double lon;
    Landmark mark;
    Connection dbcon = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      dbcon = m_env.getDBConnection();
      stmt = dbcon.prepareStatement(qry);
      stmt.setString(1, cityCode);
      stmt.setString(2, eel);
      rs = stmt.executeQuery();
      while (rs.next()) {
        id = rs.getString("landmark_id");
        desc = rs.getString("description");
        local_name = rs.getString("local_name");
        if (rs.wasNull()) {
          local_name = null;
        }
        lat = rs.getDouble("latitude");
        lon = rs.getDouble("longitude");
        if (rs.wasNull()) {
          mark = new Landmark(id, desc, local_name);
        } else {
          mark = new Landmark(id, desc, local_name, lat, lon);
        }
        assert null != mark;
        marks.add(mark);
      }
    } finally {
      closeQuery(stmt, rs);
      m_env.disconnect(dbcon);
    }
  }

  /**
   * @param cityCode
   * @param marks
   * @throws SQLException
   * @throws RSConnectionException
   */
  public void getLandmarksInCity(String cityCode, List<Landmark> marks)
      throws SQLException, RSConnectionException {
    String qry = "select distinct landmark_id, description, local_name, latitude, longitude"
        + " from rs_landmark_eel where city_id = ? order by description";
    String id;
    String desc;
    String local_name;
    double lat;
    double lon;
    Landmark mark;
    Connection dbcon = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      dbcon = m_env.getDBConnection();
      stmt = dbcon.prepareStatement(qry);
      stmt.setString(1, cityCode);
      rs = stmt.executeQuery();
      while (rs.next()) {
        id = rs.getString("landmark_id");
        desc = rs.getString("description");
        local_name = rs.getString("local_name");
        if (rs.wasNull()) {
          local_name = null;
        }
        lat = rs.getDouble("latitude");
        lon = rs.getDouble("longitude");
        if (rs.wasNull()) {
          mark = new Landmark(id, desc, local_name);
        } else {
          mark = new Landmark(id, desc, local_name, lat, lon);
        }
        assert null != mark;
        marks.add(mark);
      }
    } finally {
      closeQuery(stmt, rs);
      m_env.disconnect(dbcon);
    }
  }

  /**
   * @param cityCode
   * @param eel
   * @param marks
   * @throws SQLException
   * @throws RSConnectionException
   */
  public void getLocationsWithEEL(String cityCode, String eel, List<ILocation> marks)
      throws SQLException, RSConnectionException {
    String qry = "select location_id, description, local_name, station_type, "
        + " location_type, latitude, longitude "
        + " from rs_location_eel where city_id = ? and eel = ? " + " order by description";
    ILocation loc;
    Connection dbcon = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      dbcon = m_env.getDBConnection();
      stmt = dbcon.prepareStatement(qry);
      stmt.setString(1, cityCode);
      stmt.setString(2, eel);
      rs = stmt.executeQuery();
      while (rs.next()) {
        loc = createLocation(rs);
        assert null != loc;
        marks.add(loc);
      }
    } finally {
      closeQuery(stmt, rs);
      m_env.disconnect(dbcon);
    }
  }

  /**
   * @param cityCode
   * @param eels
   * @throws SQLException
   */
  public void getStationEELs(String cityCode, List<String> eels)
      throws SQLException, RSConnectionException {
    String qry = "select distinct eel from rs_station_eel " + "where city_id = ? order by eel";
    String eel;
    Connection dbcon = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      dbcon = m_env.getDBConnection();
      stmt = dbcon.prepareStatement(qry);
      stmt.setString(1, cityCode);
      rs = stmt.executeQuery();
      while (rs.next()) {
        eel = rs.getString("eel");
        eels.add(eel);
      }
    } finally {
      closeQuery(stmt, rs);
      m_env.disconnect(dbcon);
    }
  }

  /**
   * @param cityCode
   * @param eels
   * @throws SQLException
   */
  public void getLandmarkEELs(String cityCode, List<String> eels)
      throws SQLException, RSConnectionException {
    String qry = "select distinct eel from rs_landmark_eel " + "where city_id = ? order by eel";
    String eel;
    Connection dbcon = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      dbcon = m_env.getDBConnection();
      stmt = dbcon.prepareStatement(qry);
      stmt.setString(1, cityCode);
      rs = stmt.executeQuery();
      while (rs.next()) {
        eel = rs.getString("eel");
        eels.add(eel);
      }
    } finally {
      closeQuery(stmt, rs);
      m_env.disconnect(dbcon);
    }
  }

  public void getEELsForLocations(String cityCode, List<String> eels)
      throws SQLException, RSConnectionException {
    String qry = "select distinct eel from rs_location_eel " + "where city_id = ? order by eel";
    String eel;
    Connection dbcon = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      dbcon = m_env.getDBConnection();
      stmt = dbcon.prepareStatement(qry);
      stmt.setString(1, cityCode);
      rs = stmt.executeQuery();
      while (rs.next()) {
        eel = rs.getString("eel");
        eels.add(eel);
      }
    } finally {
      closeQuery(stmt, rs);
      m_env.disconnect(dbcon);
    }
  }

  /**
   * Adds all servicetypes of a city to the servicetype collection.
   * 
   * @param cityCode
   *          The city for which service types are requested.
   * @param si
   *          the collection to add the information to.
   * @throws SQLException
   * @throws RSConnectionException
   */
  public void getServiceTypes(String cityCode, EnumSet<ServiceType> stypes)
      throws SQLException, RSConnectionException {
    String qry = "select distinct service_type " + " from rs_direction where city_id = ?";
    String svtype;
    Connection dbcon = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      dbcon = m_env.getDBConnection();
      stmt = dbcon.prepareStatement(qry);
      stmt.setString(1, cityCode);
      rs = stmt.executeQuery();
      while (rs.next()) {
        svtype = rs.getString("service_type");
        if (svtype.equals("PARTIAL")) {
          stypes.add(ServiceType.PARTIAL);
        } else if (svtype.equals("EXTENDED")) {
          stypes.add(ServiceType.EXTENDED);
        } else {
          stypes.add(ServiceType.REGULAR);
        }
      }
    } finally {
      closeQuery(stmt, rs);
      m_env.disconnect(dbcon);
    }
    return;
  }

  public void getStationsWithGPSInfo(String cityCode, List<NearestStation> statset)
      throws SQLException, RSConnectionException {
    String qry = "select station_id, description, local_name, station_type, "
        + " latitude, longitude from rs_display_station "
        + "where city_id = ? and latitude is not null and longitude is not null";
    Station stat;
    NearestStation ns;
    Connection dbcon = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      dbcon = m_env.getDBConnection();
      stmt = dbcon.prepareStatement(qry);
      stmt.setString(1, cityCode);
      rs = stmt.executeQuery();
      while (rs.next()) {
        stat = createStation(rs);
        assert null != stat;
        ns = new NearestStation(stat);
        statset.add(ns);
        stat = null;
        ns = null;
      }
    } finally {
      closeQuery(stmt, rs);
      m_env.disconnect(dbcon);
    }
  }

  /**
   * Adds all cities with GPS information to the list.
   * 
   * @param statset
   *          list of all cities with GPS information.
   * @throws SQLException
   * @throws RSConnectionException
   */
  public void getCityGPS(List<City> statset) throws SQLException, RSConnectionException {
    String qry = sql_city_select + " where latitude is not null and longitude is not null"
        + sql_city_order;
    String desc;
    City ct = null;
    Connection dbcon = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String currentCity = "";
    try {
      dbcon = m_env.getDBConnection();
      stmt = dbcon.prepareStatement(qry);
      rs = stmt.executeQuery();
      while (rs.next()) {
        // If information messages are available, multiple records exist
        // for one city.
        desc = rs.getString("city_desc");
        if (!currentCity.equals(desc)) {
          ct = createCity(rs);

          // Second, add city to set and make the new city the current city
          statset.add(ct);
          currentCity = desc;
        }
        if (null != ct) {
          createPublishMessage(rs, ct, ct.getCode(), ct.getName());
        }
      }
    } finally {
      closeQuery(stmt, rs);
      m_env.disconnect(dbcon);
    }
  }

  public ILocation getLocation(String loc) throws RSConnectionException, SQLException {
    assert null != loc;
    String qry = "select location_id, description, local_name, station_type, "
        + " latitude, longitude, location_type " + " from rs_locations where location_id = ?";
    Connection dbcon = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    ILocation location = null;
    try {
      dbcon = m_env.getDBConnection();
      stmt = dbcon.prepareStatement(qry);
      stmt.setString(1, loc);
      rs = stmt.executeQuery();
      while (rs.next()) {
        location = createLocation(rs);
      }
    } finally {
      closeQuery(stmt, rs);
      m_env.disconnect(dbcon);
    }
    return location;
  }

  public void getNewMessages(List<PublishMessage> lst) throws RSConnectionException, SQLException {
    String qry = "select city_id, city_desc, " + " info_status, info_timestamp, info_message "
        + " from rs_city_publish where info_status is not null" + " order by info_timestamp desc";
    String name;
    String code;
    Connection dbcon = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    try {
      dbcon = m_env.getDBConnection();
      stmt = dbcon.prepareStatement(qry);
      rs = stmt.executeQuery();
      while (rs.next()) {
        code = rs.getString("city_id");
        name = rs.getString("city_desc");
        createPublishMessage2(rs, lst, code, name);
      }
    } finally {
      closeQuery(stmt, rs);
      m_env.disconnect(dbcon);
    }
  }

  /**
   * @param cityCode
   * @param statset
   * @throws SQLException
   */
  public void getFilteredLocations(String cityCode, List<ILocation> statset, String filter)
      throws SQLException, RSConnectionException {
    /*
     * Use lower(description) and filter.toLowerCase() to ensure case-insensitive match.
     */
    String qry = "select distinct(location_id), description, local_name, station_type, "
        + " location_type, latitude, longitude "
        + " from rs_location_eel where city_id = ? and lower(description) like ? "
        + " order by description";
    Connection dbcon = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    ILocation location = null;
    try {
      dbcon = m_env.getDBConnection();
      stmt = dbcon.prepareStatement(qry);
      stmt.setString(1, cityCode);
      stmt.setString(2, "%" + filter.toLowerCase() + "%");
      rs = stmt.executeQuery();
      while (rs.next()) {
        location = createLocation(rs);
        statset.add(location);
      }
    } finally {
      closeQuery(stmt, rs);
      m_env.disconnect(dbcon);
    }
  }

  public void getOverrideSegDirections(String stationCode, List<SegmentDirection> lst)
      throws SQLException, RSConnectionException {
    String qry = "select distinct station_id, dir_id, override_dir_id, line, endpoint, "
        + " service_type, remark from rs_dir_override where station_id = ?";
    Connection dbcon = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    SegmentDirection dir;
    String val;
    try {
      dbcon = m_env.getDBConnection();
      stmt = dbcon.prepareStatement(qry);
      stmt.setString(1, stationCode);
      rs = stmt.executeQuery();
      while (rs.next()) {
        dir = new SegmentDirection();
        val = rs.getString("dir_id");
        dir.setDirId(val);
        val = rs.getString("override_dir_id");
        dir.setDirId(val);
        val = rs.getString("line");
        dir.setLine(val);
        val = rs.getString("endpoint");
        dir.setEndpoint(val);
        val = rs.getString("service_type");
        dir.setServiceType(val);
        val = rs.getString("remark");
        dir.setRemark(val);
        lst.add(dir);
      }
    } finally {
      closeQuery(stmt, rs);
      m_env.disconnect(dbcon);
    }
    return;
  }

  public void getNoOverrideSegDirections(String stationCode, List<SegmentDirection> lst)
      throws SQLException, RSConnectionException {
    String qry = "select distinct station_id, dir_id, line, endpoint, "
        + " service_type, remark from rs_dir_no_override where station_id = ?";
    Connection dbcon = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    SegmentDirection dir;
    String val;
    try {
      dbcon = m_env.getDBConnection();
      stmt = dbcon.prepareStatement(qry);
      stmt.setString(1, stationCode);
      rs = stmt.executeQuery();
      while (rs.next()) {
        dir = new SegmentDirection();
        val = rs.getString("dir_id");
        dir.setDirId(val);
        val = rs.getString("line");
        dir.setLine(val);
        val = rs.getString("endpoint");
        dir.setEndpoint(val);
        val = rs.getString("service_type");
        dir.setServiceType(val);
        val = rs.getString("remark");
        dir.setRemark(val);
        lst.add(dir);
      }
    } finally {
      closeQuery(stmt, rs);
      m_env.disconnect(dbcon);
    }
    return;
  }

  /**
   * Append all network station codes for a station to the list.<br>
   * Required to handle complexes, where multiple network stations combine to form a complex.
   * 
   * @param stationCode
   *          station code
   * @param lst
   *          list with network station codes
   * @throws SQLException
   * @throws RSConnectionException
   */
  public void getCodesForStation(String stationCode, List<String> lst)
      throws SQLException, RSConnectionException {
    String qry = "select station_id from rs_network_station where node_id = "
        + " (select node_id from rs_network_station where station_id = ?)";
    Connection dbcon = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
    String val;
    try {
      dbcon = m_env.getDBConnection();
      stmt = dbcon.prepareStatement(qry);
      stmt.setString(1, stationCode);
      rs = stmt.executeQuery();
      while (rs.next()) {
        val = rs.getString("station_id");
        lst.add(val);
      }
    } finally {
      closeQuery(stmt, rs);
      m_env.disconnect(dbcon);
    }
    return;
  }

  private void closeQuery(Statement stmt, ResultSet rs) {
    if (null != rs) {
      try {
        rs.close();
      } catch (SQLException e) {
        // do not propagate.
      }
    }

    if (null != stmt) {
      try {
        stmt.close();
      } catch (SQLException e) {
        // do not propagate.
      }
    }
  }

  private Country createCountry(ResultSet rs) throws SQLException {
    String id;
    String desc;
    Country cntry;
    id = rs.getString("country_id");
    desc = rs.getString("country_desc");
    cntry = new Country(id, desc);
    return cntry;
  }

  private Continent createContinent(ResultSet rs) throws SQLException {
    String id;
    String desc;
    Continent con;
    id = rs.getString("continent_id");
    desc = rs.getString("continent_desc");
    con = new Continent(id, desc);
    return con;
  }

  private void createPublishMessage(ResultSet rs, IPublishInfo owner, String code, String name)
      throws SQLException {
    PublishMessage msg = null;
    String info_status;
    Date info_timestamp;
    String info_message;
    info_status = rs.getString("info_status");
    if (!rs.wasNull()) { // info_status not null. Add publish info
      info_timestamp = rs.getDate("info_timestamp");
      info_message = rs.getString("info_message");
      msg = new PublishMessage(info_timestamp, info_message, info_status, code, name);
      owner.addMessage(msg);
    }
  }

  private void createPublishMessage2(ResultSet rs, List<PublishMessage> lst, String code,
      String name) throws SQLException {
    PublishMessage msg = null;
    String info_status;
    Date info_timestamp;
    String info_message;
    info_status = rs.getString("info_status");
    if (!rs.wasNull()) { // info_status not null. Add publish info
      info_timestamp = rs.getDate("info_timestamp");
      info_message = rs.getString("info_message");
      msg = new PublishMessage(info_timestamp, info_message, info_status, code, name);
      lst.add(msg);
    }
  }

  private City createCity(ResultSet rs) throws SQLException {
    String id;
    String desc;
    String shortcode;
    String local_name;
    double lat;
    double lon;
    City ct;

    Continent con = createContinent(rs);
    Country cntry = createCountry(rs);

    id = rs.getString("city_id");
    shortcode = rs.getString("city_code");
    desc = rs.getString("city_desc");
    local_name = rs.getString("local_name");
    if (rs.wasNull()) {
      local_name = null;
    }
    lat = rs.getDouble("latitude");
    lon = rs.getDouble("longitude");
    if (rs.wasNull()) {
      ct = new City(id, shortcode, desc, local_name, cntry, con);
    } else {
      ct = new City(id, shortcode, desc, local_name, cntry, con, lat, lon);
    }
    return ct;
  }

  private Station createStation(ResultSet rs) throws SQLException {
    String id;
    String desc;
    String local_name;
    String station_type;
    double lat;
    double lon;
    Station stat;
    id = rs.getString("station_id");
    desc = rs.getString("description");
    station_type = rs.getString("station_type");
    local_name = rs.getString("local_name");
    if (rs.wasNull()) {
      local_name = null;
    }
    lat = rs.getDouble("latitude");
    lon = rs.getDouble("longitude");
    if (rs.wasNull()) {
      stat = new Station(id, desc, local_name, station_type);
    } else {
      stat = new Station(id, desc, local_name, station_type, lat, lon);
    }
    return stat;
  }

  private ILocation createLocation(ResultSet rs) throws SQLException {

    ILocation location = null;
    String location_id = null;
    String loc_type = null;
    String description = null;
    String local_name = null;
    String station_type = null;
    double latitude;
    double longitude;
    boolean hasGPSinfo = true;

    location_id = rs.getString("location_id");
    loc_type = rs.getString("location_type");
    description = rs.getString("description");
    station_type = rs.getString("station_type");
    local_name = rs.getString("local_name");
    if (rs.wasNull()) {
      local_name = null;
    }
    latitude = rs.getDouble("latitude");
    longitude = rs.getDouble("longitude");
    if (rs.wasNull()) {
      hasGPSinfo = false;
    }

    if ("LANDMARK".equals(loc_type)) {
      if (hasGPSinfo) {
        location = new Landmark(location_id, description, local_name, latitude, longitude);
      } else {
        location = new Landmark(location_id, description, local_name);
      }
    } else if ("STATION".equals(loc_type)) {
      if (hasGPSinfo) {
        location = new Station(location_id, description, local_name, station_type, latitude,
            longitude);
      } else {
        location = new Station(location_id, description, local_name, station_type);
      }
    } else if ("CITY".equals(loc_type)) {
      if (hasGPSinfo) {
        location = new City(location_id, "", description, local_name, null, null, latitude,
            longitude);
      } else {
        location = new City(location_id, "", description, local_name, null, null);
      }
    }
    return location;
  }

}
