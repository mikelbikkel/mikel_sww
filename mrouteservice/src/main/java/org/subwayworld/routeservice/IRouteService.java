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

package org.subwayworld.routeservice;

import java.util.EnumSet;
import java.util.List;

/**
 * Functions to get core network information and to calculate a shortest route
 * between two stations.
 * <p>
 * Methods throw a {@code NullPointerException} if parameters are {@code null}.
 * <p>
 */
public interface IRouteService {

	/**
	 * Returns all the continents for which metro information is available.
	 * 
	 * @return all the continents for which metro information is available.
	 * 
	 * @throws RSException
	 *             if error occurs.
	 */
	public List<Continent> getContinents() throws RSException;

	/**
	 * Returns all the countries on an continent for which metro information is
	 * available.
	 * 
	 * @param continentCode
	 *            The continent for which countries are requested.
	 * 
	 * @return all the countries for which metro information is available.
	 * 
	 * @throws RSException
	 *             if error occurs.
	 */
	public List<Country> getCountries(String continentCode) throws RSException;

	/**
	 * Returns all the countries worldwide for which metro information is
	 * available.
	 * 
	 * @return all the countries for which metro information is available.
	 * 
	 * @throws RSException
	 *             if error occurs.
	 */
	public List<Country> getCountries() throws RSException;

	/**
	 * Returns all the cities with metro information on a specific continent.
	 * 
	 * @param ccode
	 *            the code of the continent whose cities are requested.
	 * @return all the cities with metro information on a specific continent.
	 * 
	 * @throws RSException
	 *             if error occurs.
	 * 
	 * @throws NullPointerException
	 *             if {@code ccode} is null.
	 */
	public List<City> getCities(String ccode) throws RSException;

	/**
	 * Returns all the cities with metro information in a specific country.
	 * 
	 * @param countryCode
	 *            the code of the country whose cities are requested.
	 * 
	 * @return all the cities with metro information in a specific country.
	 * 
	 * @throws RSException
	 *             if error occurs.
	 * 
	 * @throws NullPointerException
	 *             if {@code countryCode} is null.
	 */
	public List<City> getCitiesForCountry(String countryCode) throws RSException;

	/**
	 * Returns all the cities with metro information .
	 * 
	 * @return all the cities with metro information.
	 * 
	 * @throws RSException
	 *             if error occurs.
	 */
	public List<String> getAllCityCodes() throws RSException;

	/**
	 * Returns all the station codes and landmark codes in a city.<br>
	 * For example: RDMBLAAK, RDMEUROMAST.
	 * <p>
	 * The list includes display stations and landmarks. Network stations are
	 * excluded.
	 * 
	 * @param cityCode
	 *            the city whose station are requested. For example: NEW_YORK.
	 * 
	 * @return all the station and landmark codes in a city.
	 * 
	 * @throws RSException
	 *             if error occurs.
	 */
	public List<String> getAllLocationCodes(String cityCode) throws RSException;

	/**
	 * Returns all stations of a city with the specified EEL. <br>
	 * The list is sorted by station name.
	 * <p>
	 * The list contains display stations. Network stations are excluded.
	 * 
	 * @param cityCode
	 *            the city whose station are requested. For example: NEW_YORK.
	 * 
	 * @param eel
	 *            the Eerste Echte Letter.
	 * 
	 * @return all stations of a city with the specified EEL.
	 * 
	 * @throws RSException
	 *             if error occurs.
	 *
	 * @throws NullPointerException
	 *             if {@code cityCode} is null or {@code eel} is null.
	 */
	public List<Station> getStations(String cityCode, String eel) throws RSException;

	/**
	 * Returns city information.
	 * 
	 * @param cityCode
	 *            the city whose information is requested.
	 * 
	 * @return the city information.
	 * 
	 * @throws RSException
	 *             if error occurs.
	 * 
	 * @throws NullPointerException
	 *             if {@code cityCode} is null.
	 */
	public List<CityInfo> getCityInfo(String cityCode) throws RSException;

	/**
	 * Returns all landmarks of a city with the specified EEL. <br>
	 * The list is sorted by landmark name.
	 * 
	 * @param cityCode
	 *            the city whose landmarks are requested.
	 * 
	 * @param eel
	 *            the Eerste Echte Letter.
	 * 
	 * @return all landmarks of a city with the specified EEL.
	 * 
	 * @throws RSException
	 *             if error occurs.
	 * 
	 * @throws NullPointerException
	 *             if {@code cityCode} is null or {@code eel} is null.
	 */
	public List<Landmark> getLandmarks(String cityCode, String eel) throws RSException;

	/**
	 * Returns all landmarks of a city. <br>
	 * The list is sorted by name.
	 * 
	 * @param cityCode
	 *            the city whose landmarks are requested. For example:
	 *            ROTTERDAM.
	 * 
	 * @return all landmarks of a city.
	 * 
	 * @throws RSException
	 *             if error occurs.
	 * 
	 * @throws NullPointerException
	 *             if {@code cityCode} is null.
	 */
	public List<Landmark> getLandmarksInCity(String cityCode) throws RSException;

	/**
	 * Returns all landmarks and stations of a city with the specified EEL. <br>
	 * The list is sorted by location name.
	 * <p>
	 * The list contains display stations and landmarks. Network stations are
	 * excluded.
	 * 
	 * @param cityCode
	 *            the city whose locations are requested.
	 * 
	 * @param eel
	 *            the Eerste Echte Letter.
	 * 
	 * @return all locations of a city with the specified EEL.
	 * 
	 * @throws RSException
	 *             if error occurs.
	 * 
	 * @throws NullPointerException
	 *             if {@code cityCode} is null or {@code eel} is null.
	 */
	public List<ILocation> getLocationsWithEEL(String cityCode, String eel) throws RSException;

	/**
	 * Returns the shortest route between 2 metro locations.
	 * <p>
	 * Start location can be a display station, a network station or a
	 * landmark.<br>
	 * End location can be a display station, a network station or a
	 * landmark.<br>
	 * 
	 * The route consists of zero, one or more route segments.
	 * <p>
	 * If there is no shortest route between the two locations, for example
	 * because the locations are not in the same metronetwork, an empty set is
	 * returned. If the parameter rr is not null, the {@code rr.code} attribute
	 * contains the errorcode that explains why an empty set is returned.
	 * 
	 * @param start
	 *            the code of the location where the route starts.
	 * @param end
	 *            the code of the location where the route ends.
	 * @param stRequest
	 *            the ServiceTypes that must be included in the search. Use null
	 *            to include all service types.
	 * @param rr
	 *            the result of the route calculation.
	 * @return the shortest route between 2 metro locations, or an empty set.
	 * 
	 * @throws RSException
	 *             if error occurs.
	 */
	public List<RouteSegment> getRoute(String start, String end, EnumSet<ServiceType> stRequest, RouteResult rr)
			throws RSException;

	/**
	 * Returns the available servicetypes for a city.
	 * <p>
	 * 
	 * @param cityCode
	 *            the code of the city.
	 * @return the set of servicetypes that is available for a city.
	 * 
	 * @throws RSException
	 *             if error occurs.
	 */
	public EnumSet<ServiceType> getServiceTypes(String cityCode) throws RSException;

	/**
	 * Returns the available station EELs for a city.
	 * <p>
	 * 
	 * @param cityCode
	 *            the code of the city.
	 * @return the set of station EELs that is available for a city.
	 * 
	 * @throws RSException
	 *             if error occurs.
	 */
	public List<String> getStationEELs(String cityCode) throws RSException;

	/**
	 * Returns the available landmark EELs for a city.
	 * <p>
	 * 
	 * @param cityCode
	 *            the code of the city, for example PARIS.
	 * 
	 * @return the set of landmark EELs that is available for the city.
	 * 
	 * @throws RSException
	 *             if error occurs.
	 */
	public List<String> getLandmarkEELs(String cityCode) throws RSException;

	/**
	 * Returns the sorted list of EELs from the stations and landmarks in a
	 * city.
	 * <p>
	 * 
	 * @param cityCode
	 *            the code of the city.
	 * @return the set of station and landmark EELs that is available for a
	 *         city.
	 * 
	 * @throws RSException
	 *             if error occurs.
	 */
	public List<String> getEELsForLocations(String cityCode) throws RSException;

	/**
	 * Returns a Continent object for a continent code.
	 * 
	 * @param code
	 *            Continent code
	 * @return a Continent object.
	 * 
	 * @throws RSException
	 *             if error occurs.
	 */
	public Continent findContinent(String code) throws RSException;

	/**
	 * Returns a City object for a city code.
	 * 
	 * @param code
	 *            City code
	 * @return a City object.
	 * 
	 * @throws RSException
	 *             if error occurs.
	 */
	public City findCity(String code) throws RSException;

	/**
	 * Returns a City object for a set of coordinates (lat/lon).
	 * 
	 * @param latitude
	 * @param longitude
	 * 
	 * @return a City object.
	 * 
	 * @throws RSException
	 *             if error occurs.
	 */
	public City findNearestCity(double latitude, double longitude) throws RSException;

	/**
	 * Returns a Station object for a station code.
	 * <p>
	 * The code can be for a display station or a network station.
	 * 
	 * @param code
	 *            Station code, for example RDMBLAAK.
	 * 
	 * @return a station object.
	 * 
	 * @throws RSException
	 *             if error occurs.
	 */
	public Station findStation(String code) throws RSException;

	/**
	 * Returns a Country object for a country code.
	 * 
	 * @param code
	 *            Country code
	 * @return a Country object.
	 * 
	 * @throws RSException
	 *             if error occurs.
	 */
	public Country findCountry(String code) throws RSException;

	/**
	 * Returns a Landmark object for a landmark code.
	 * 
	 * @param code
	 *            Landmark code. For example: PRSTOUR_EIFFEL
	 * 
	 * @return a Landmark object.
	 * 
	 * @throws RSException
	 *             if error occurs.
	 */
	public Landmark findLandmark(String code) throws RSException;

	/**
	 * Returns a ILocation object for a location code.
	 * <p>
	 * The location code can be for a display station, a network station or a
	 * landmark.
	 * 
	 * @param code
	 *            Location code
	 * @return a location object, or null if a location is not found.
	 * 
	 * @throws RSException
	 *             if error occurs.
	 */
	public ILocation findLocation(String code) throws RSException;

	/**
	 * Find the nearest station, given GPS-coordinates.
	 * <p>
	 * The list includes only display stations. Network stations are not in the
	 * result set.
	 * 
	 * @param longitude
	 * @param latitude
	 * @param maxHits
	 *            the number of nearest stations to return
	 * @param maxKilometers
	 *            the search radius.
	 * 
	 * @return the nearest stations, or an empty list if a nearest station can
	 *         not be found.
	 * 
	 * @throws RSException
	 *             if error occurs.
	 */
	public List<NearestStation> findNearestStation(double latitude, double longitude, int maxHits, int maxKilometers)
			throws RSException;

	public void setCity(String cityCode) throws RSException;

	/**
	 * Returns a location object for a location code.
	 * <p>
	 * The location code can be for a display station, a network station or a
	 * landmark.
	 * <p>
	 * Returns null if:
	 * <ul>
	 * <li>the location code does not exist in the SWW database.</li>
	 * <li>the location code refers to an SWW object without GPS
	 * information.</li>
	 * </ul>
	 * 
	 * @param loc
	 *            the code of the location. For example: RDMBLAAK.
	 * 
	 * @return a location object, or null if a location cannot be found for the
	 *         loc.
	 * 
	 * @throws RSException
	 *             if error occurs.
	 * 
	 * @throws NullPointerException
	 *             if loc is null.
	 */
	public ILocation getLocation(String loc) throws RSException;

	/**
	 * Returns an ordered list of publication messages.
	 * <p>
	 * The list is ordered by publication date.
	 * <p>
	 * An empty list is returned if publication messages are not found.
	 * 
	 * @return an ordered list of publication messages.
	 * 
	 * @throws RSException
	 *             if error occurs.
	 */
	public List<PublishMessage> getNewMessages() throws RSException;

	/**
	 * Returns all station and landmark locations.
	 * <p>
	 * Function for test purposes, to calculate all distances between all
	 * locations.
	 * <p>
	 * The list includes display stations, network stations and landmarks.
	 * 
	 * @return a list with all locations.
	 * 
	 * @throws RSException
	 *             if error occurs.
	 */
	public List<ILocation> testGetLocations() throws RSException;

	/**
	 * Returns a list of locations that match a searchname.
	 * <p>
	 * The list includes landmarks and display stations. Network stations are
	 * not included.
	 * 
	 * @param citycode
	 *            city for which stations are requested. For example ROTTERDAM.
	 * @param searchname
	 *            the search name.
	 * @return a list of locations that match the searchname.
	 * 
	 * @throws RSException
	 *             if error occurs.
	 */
	public List<ILocation> getFilteredLocations(String citycode, String searchname) throws RSException;

	/**
	 * Returns all departure directions on a station.
	 * <p>
	 * The station code can be for a display station and for a network station.
	 * 
	 * @param stationCode
	 *            the station code, for example RDMBLAAK.
	 * @return all directions on a station.
	 * 
	 * @throws RSException
	 *             if error occurs.
	 */
	public List<SegmentDirection> getStationDepartures(String stationCode) throws RSException;
}
