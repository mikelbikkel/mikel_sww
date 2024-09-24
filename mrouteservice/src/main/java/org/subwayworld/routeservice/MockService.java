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

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

/**
 * 21 nov 2009
 * 
 */
public class MockService implements IRouteService {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.subwayworld.routeservice.IRouteService#getCities(String)
	 */
	@Override
	public List<City> getCities(String ccode) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.subwayworld.routeservice.IRouteService#getAllCities()
	 */
	@Override
	public List<String> getAllCityCodes() throws RSException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.subwayworld.routeservice.IRouteService#getContinents()
	 */
	@Override
	public List<Continent> getContinents() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.subwayworld.routeservice.IRouteService#getRoute(String, boolean,
	 * String, boolean, EnumSet<ServiceType>, RouteResult)
	 */
	@Override
	public List<RouteSegment> getRoute(String start, String end, EnumSet<ServiceType> stRequest, RouteResult rr)
			throws RSException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.subwayworld.routeservice.IRouteService#findContinent(String)
	 */
	@Override
	public Continent findContinent(String code) throws RSException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.subwayworld.routeservice.IRouteService#findCity(String)
	 */
	@Override
	public City findCity(String code) throws RSException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.subwayworld.routeservice.IRouteService#findStation(String)
	 */
	@Override
	public Station findStation(String code) throws RSException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.subwayworld.routeservice.IRouteService#getAllStationCodes(String)
	 */
	@Override
	public List<String> getAllLocationCodes(String cityCode) throws RSException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.subwayworld.routeservice.IRouteService#getStations(String)
	 */
	@Override
	public List<Station> getStations(String cityCode, String eel) throws RSException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.subwayworld.routeservice.IRouteService#getLandmarks(String)
	 */
	@Override
	public List<Landmark> getLandmarks(String cityCode, String eel) throws RSException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.subwayworld.routeservice.IRouteService#getLandmarksInCity(java.lang
	 * .String)
	 */
	@Override
	public List<Landmark> getLandmarksInCity(String cityCode) throws RSException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.subwayworld.routeservice.IRouteService#getServiceTypes(String)
	 */
	@Override
	public EnumSet<ServiceType> getServiceTypes(String cityCode) throws RSException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.subwayworld.routeservice.IRouteService#findCountry(String)
	 */
	@Override
	public Country findCountry(String code) throws RSException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.subwayworld.routeservice.IRouteService#findLandmark(String)
	 */
	@Override
	public Landmark findLandmark(String code) throws RSException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.subwayworld.routeservice.IRouteService#findLocation(String)
	 */
	@Override
	public ILocation findLocation(String code) throws RSException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.subwayworld.routeservice.IRouteService#getCountries()
	 */
	@Override
	public List<Country> getCountries(String continentCode) throws RSException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.subwayworld.routeservice.IRouteService#getCountries()
	 */
	@Override
	public List<Country> getCountries() throws RSException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.subwayworld.routeservice.IRouteService#getCitiesForCountry(String)
	 */
	@Override
	public List<City> getCitiesForCountry(String countryCode) throws RSException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.subwayworld.routeservice.IRouteService#getStationEELs(String)
	 */
	@Override
	public List<String> getStationEELs(String cityCode) throws RSException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.subwayworld.routeservice.IRouteService#getLandmarkEELs(String)
	 */
	@Override
	public List<String> getLandmarkEELs(String code) throws RSException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.subwayworld.routeservice.IRouteService#findNearestStation(double,
	 * double, int, int)
	 */
	@Override
	public List<NearestStation> findNearestStation(double longitude, double latitude, int maxHits, int maxKilometers)
			throws RSException {
		return Collections.emptyList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.subwayworld.routeservice.IRouteService#setCity(String)
	 */
	@Override
	public void setCity(String cityCode) throws RSException {
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.subwayworld.routeservice.IRouteService#findNearestCity(double,
	 * double)
	 */
	@Override
	public City findNearestCity(double latitude, double longitude) throws RSException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.subwayworld.routeservice.IRouteService#getCityInfo(java.lang.String)
	 */
	@Override
	public List<CityInfo> getCityInfo(String cityCode) throws RSException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.subwayworld.routeservice.IRouteService#getLocation(java.lang.String)
	 */
	@Override
	public ILocation getLocation(String loc) throws RSException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.subwayworld.routeservice.IRouteService#getNewMessages()
	 */
	@Override
	public List<PublishMessage> getNewMessages() throws RSException {
		return Collections.emptyList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.subwayworld.routeservice.IRouteService#testGetLocations()
	 */
	@Override
	public List<ILocation> testGetLocations() throws RSException {
		return Collections.emptyList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.subwayworld.routeservice.IRouteService#getLocationEELs(java.lang.
	 * String )
	 */
	@Override
	public List<String> getEELsForLocations(String cityCode) throws RSException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.subwayworld.routeservice.IRouteService#getLocationsWithEEL(java.lang
	 * .String, java.lang.String)
	 */
	@Override
	public List<ILocation> getLocationsWithEEL(String cityCode, String eel) throws RSException {
		return null;
	}

	@Override
	public List<ILocation> getFilteredLocations(String cityCode, String filter) throws RSException {
		return null;
	}

	@Override
	public List<SegmentDirection> getStationDepartures(String stationCode) throws RSException {
		return null;
	}

}
