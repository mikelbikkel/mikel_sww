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

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import org.subwayworld.routeservice.calculation.RouteCalculation;
import org.subwayworld.routeservice.selection.RouteSelection;

/**
 * RouteService implementation with a MySQL database.
 * 
 */
public class RouteService implements IRouteService {

	/**
	 * Number of retries in case of retry-able exceptions.
	 */
	public static final int NUM_RETRIES = 2;

	// private Log m_log = LogFactory.getLog(getClass());

	private RouteSelection m_select;

	private RouteCalculation m_calc;

	public RouteService() {
		this(null);
	}

	public RouteService(RSEnv env) {
		if (null == env) {
			throw new NullPointerException();
		}
		m_select = new RouteSelection(env);
		m_calc = new RouteCalculation(env);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see RouteService#getCities(String)
	 */
	@Override
	public List<City> getCities(String ccode) throws RSException {
		int numRetries = 0;
		while (numRetries < NUM_RETRIES) {
			try {
				return m_select.getCities(ccode);
			} catch (RSConnectionException e) {
				// retry if a retry-able exception is thrown.
				numRetries++;
			}
		}
		// last retry.
		// Any exception that occurs is propagated to the caller.
		return m_select.getCities(ccode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.subwayworld.routeservice.IRouteService#getAllCities()
	 */
	@Override
	public List<String> getAllCityCodes() throws RSException {
		int numRetries = 0;
		while (numRetries < NUM_RETRIES) {
			try {
				return m_select.getAllCityCodes();
			} catch (RSConnectionException e) {
				// retry if a retry-able exception is thrown.
				numRetries++;
			}
		}
		// last retry.
		// Any exception that occurs is propagated to the caller.
		return m_select.getAllCityCodes();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see RouteService#getContinents()
	 */
	@Override
	public List<Continent> getContinents() throws RSException {
		int numRetries = 0;
		while (numRetries < NUM_RETRIES) {
			try {
				return m_select.getContinents();
			} catch (RSConnectionException e) {
				// retry if a retry-able exception is thrown.
				numRetries++;
			}
		}
		// last retry.
		// Any exception that occurs is propagated to the caller.
		return m_select.getContinents();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.subwayworld.routeservice.IRouteService#getCountries()
	 */
	@Override
	public List<Country> getCountries(String continentCode) throws RSException {
		int numRetries = 0;
		while (numRetries < NUM_RETRIES) {
			try {
				return m_select.getCountries(continentCode);
			} catch (RSConnectionException e) {
				// retry if a retry-able exception is thrown.
				numRetries++;
			}
		}
		// last retry.
		// Any exception that occurs is propagated to the caller.
		return m_select.getCountries(continentCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.subwayworld.routeservice.IRouteService#getCountries()
	 */
	@Override
	public List<Country> getCountries() throws RSException {
		int numRetries = 0;
		while (numRetries < NUM_RETRIES) {
			try {
				return m_select.getCountries();
			} catch (RSConnectionException e) {
				// retry if a retry-able exception is thrown.
				numRetries++;
			}
		}
		// last retry.
		// Any exception that occurs is propagated to the caller.
		return m_select.getCountries();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.subwayworld.routeservice.IRouteService#getCitiesForCountry(String)
	 */
	@Override
	public List<City> getCitiesForCountry(String countryCode) throws RSException {
		int numRetries = 0;
		while (numRetries < NUM_RETRIES) {
			try {
				return m_select.getCitiesForCountry(countryCode);
			} catch (RSConnectionException e) {
				// retry if a retry-able exception is thrown.
				numRetries++;
			}
		}
		// last retry.
		// Any exception that occurs is propagated to the caller.
		return m_select.getCitiesForCountry(countryCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.subwayworld.routeservice.IRouteService#findContinent(java.lang
	 * .String)
	 */
	@Override
	public Continent findContinent(String code) throws RSException {
		int numRetries = 0;
		while (numRetries < NUM_RETRIES) {
			try {
				return m_select.findContinent(code);
			} catch (RSConnectionException e) {
				// retry if a retry-able exception is thrown.
				numRetries++;
			}
		}
		// last retry.
		// Any exception that occurs is propagated to the caller.
		return m_select.findContinent(code);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.subwayworld.routeservice.IRouteService#findCity(java.lang.String)
	 */
	@Override
	public City findCity(String code) throws RSException {
		int numRetries = 0;
		while (numRetries < NUM_RETRIES) {
			try {
				return m_select.findCity(code);
			} catch (RSConnectionException e) {
				// retry if a retry-able exception is thrown.
				numRetries++;
			}
		}
		// last retry.
		// Any exception that occurs is propagated to the caller.
		return m_select.findCity(code);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.subwayworld.routeservice.IRouteService#findCity(java.lang.String)
	 */
	@Override
	public City findNearestCity(double latitude, double longitude) throws RSException {
		int numRetries = 0;
		while (numRetries < NUM_RETRIES) {
			try {
				return m_select.findNearestCity(latitude, longitude);
			} catch (RSConnectionException e) {
				// retry if a retry-able exception is thrown.
				numRetries++;
			}
		}
		// last retry.
		// Any exception that occurs is propagated to the caller.
		return m_select.findNearestCity(latitude, longitude);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.subwayworld.routeservice.IRouteService#findStation(java.lang.String )
	 */
	@Override
	public Station findStation(String code) throws RSException {
		int numRetries = 0;
		while (numRetries < NUM_RETRIES) {
			try {
				return m_select.findStation(code);
			} catch (RSConnectionException e) {
				// retry if a retry-able exception is thrown.
				numRetries++;
			}
		}
		// last retry.
		// Any exception that occurs is propagated to the caller.
		return m_select.findStation(code);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.subwayworld.routeservice.IRouteService#findCountry(String)
	 */
	@Override
	public Country findCountry(String code) throws RSException {
		int numRetries = 0;
		while (numRetries < NUM_RETRIES) {
			try {
				return m_select.findCountry(code);
			} catch (RSConnectionException e) {
				// retry if a retry-able exception is thrown.
				numRetries++;
			}
		}
		// last retry.
		// Any exception that occurs is propagated to the caller.
		return m_select.findCountry(code);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.subwayworld.routeservice.IRouteService#findLandmark(String)
	 */
	@Override
	public Landmark findLandmark(String code) throws RSException {
		int numRetries = 0;
		while (numRetries < NUM_RETRIES) {
			try {
				return m_select.findLandmark(code);
			} catch (RSConnectionException e) {
				// retry if a retry-able exception is thrown.
				numRetries++;
			}
		}
		// last retry.
		// Any exception that occurs is propagated to the caller.
		return m_select.findLandmark(code);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.subwayworld.routeservice.IRouteService#findLocation(String)
	 */
	@Override
	public ILocation findLocation(String code) throws RSException {
		ILocation loc = null;
		int numRetries = 0;
		while (numRetries < NUM_RETRIES) {
			try {
				loc = m_select.findStation(code);
				if (null == loc) {
					loc = m_select.findLandmark(code);
				}
				return loc;
			} catch (RSConnectionException e) {
				// retry if a retry-able exception is thrown.
				numRetries++;
			}
		}
		// last retry.
		// Any exception that occurs is propagated to the caller.
		loc = m_select.findStation(code);
		if (null == loc) {
			loc = m_select.findLandmark(code);
		}
		return loc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see IRouteService#getStations(String, String)
	 */
	@Override
	public List<Station> getStations(String cityCode, String eel) throws RSException {
		int numRetries = 0;
		while (numRetries < NUM_RETRIES) {
			try {
				return m_select.getStations(cityCode, eel);
			} catch (RSConnectionException e) {
				// retry if a retry-able exception is thrown.
				numRetries++;
			}
		}
		// last retry.
		// Any exception that occurs is propagated to the caller.
		return m_select.getStations(cityCode, eel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.subwayworld.routeservice.IRouteService#getCityInfo(String)
	 */
	@Override
	public List<CityInfo> getCityInfo(String cityCode) throws RSException {
		int numRetries = 0;
		while (numRetries < NUM_RETRIES) {
			try {
				return m_select.getCityInfo(cityCode);
			} catch (RSConnectionException e) {
				// retry if a retry-able exception is thrown.
				numRetries++;
			}
		}
		// last retry.
		// Any exception that occurs is propagated to the caller.
		return m_select.getCityInfo(cityCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.subwayworld.routeservice.IRouteService#getAllStationCodes(String)
	 */
	@Override
	public List<String> getAllLocationCodes(String cityCode) throws RSException {
		int numRetries = 0;
		while (numRetries < NUM_RETRIES) {
			try {
				return m_select.getAllLocationCodes(cityCode);
			} catch (RSConnectionException e) {
				// retry if a retry-able exception is thrown.
				numRetries++;
			}
		}
		// last retry.
		// Any exception that occurs is propagated to the caller.
		return m_select.getAllLocationCodes(cityCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.subwayworld.routeservice.IRouteService#getLandmarks(String)
	 */
	@Override
	public List<Landmark> getLandmarks(String cityCode, String eel) throws RSException {
		int numRetries = 0;
		while (numRetries < NUM_RETRIES) {
			try {
				return m_select.getLandmarks(cityCode, eel);
			} catch (RSConnectionException e) {
				// retry if a retry-able exception is thrown.
				numRetries++;
			}
		}
		// last retry.
		// Any exception that occurs is propagated to the caller.
		return m_select.getLandmarks(cityCode, eel);
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
		int numRetries = 0;
		while (numRetries < NUM_RETRIES) {
			try {
				return m_select.getLandmarksInCity(cityCode);
			} catch (RSConnectionException e) {
				// retry if a retry-able exception is thrown.
				numRetries++;
			}
		}
		// last retry.
		// Any exception that occurs is propagated to the caller.
		return m_select.getLandmarksInCity(cityCode);
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
		int numRetries = 0;
		while (numRetries < NUM_RETRIES) {
			try {
				return m_calc.getRoute(start, end, stRequest, rr);
			} catch (RSConnectionException e) {
				// retry if a retry-able exception is thrown.
				numRetries++;
			}
		}
		// last retry.
		// Any exception that occurs is propagated to the caller.
		return m_calc.getRoute(start, end, stRequest, rr);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.subwayworld.routeservice.IRouteService#getServiceTypes(String)
	 */
	@Override
	public EnumSet<ServiceType> getServiceTypes(String cityCode) throws RSException {
		int numRetries = 0;
		while (numRetries < NUM_RETRIES) {
			try {
				return m_select.getServiceTypes(cityCode);
			} catch (RSConnectionException e) {
				// retry if a retry-able exception is thrown.
				numRetries++;
			}
		}
		// last retry.
		// Any exception that occurs is propagated to the caller.
		return m_select.getServiceTypes(cityCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.subwayworld.routeservice.IRouteService#getStationEELs(String)
	 */
	@Override
	public List<String> getStationEELs(String cityCode) throws RSException {
		int numRetries = 0;
		while (numRetries < NUM_RETRIES) {
			try {
				return m_select.getStationEELs(cityCode);
			} catch (RSConnectionException e) {
				// retry if a retry-able exception is thrown.
				numRetries++;
			}
		}
		// last retry.
		// Any exception that occurs is propagated to the caller.
		return m_select.getStationEELs(cityCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.subwayworld.routeservice.IRouteService#getLandmarkEELs(String)
	 */
	@Override
	public List<String> getLandmarkEELs(String cityCode) throws RSException {
		int numRetries = 0;
		while (numRetries < NUM_RETRIES) {
			try {
				return m_select.getLandmarkEELs(cityCode);
			} catch (RSConnectionException e) {
				// retry if a retry-able exception is thrown.
				numRetries++;
			}
		}
		// last retry.
		// Any exception that occurs is propagated to the caller.
		return m_select.getLandmarkEELs(cityCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.subwayworld.routeservice.IRouteService#findNearestStation(double,
	 * double, int, int)
	 */
	@Override
	public List<NearestStation> findNearestStation(double latitude, double longitude, int maxHits, int maxKilometers)
			throws RSException {

		if (maxHits < 0 || maxKilometers < 0) {
			return Collections.emptyList();
		}
		int maxMeters = maxKilometers * 1000;

		// Find city
		City c;
		// findNearestCity handles retries.
		c = findNearestCity(latitude, longitude);
		if (null == c) {
			return Collections.emptyList();
		}

		// Find stations in city with GPS info.
		List<NearestStation> stations = new ArrayList<NearestStation>();
		String citycode = c.getCode();
		m_select.findStationsWithGPSInfo(citycode, stations);

		// Remove stations outside search radius.
		double dist;
		int idist;
		Iterator<NearestStation> iter = stations.iterator();
		NearestStation ns;
		while (iter.hasNext()) {
			ns = iter.next();
			dist = ns.getStation().getGPSInfo().getDistance(latitude, longitude);
			idist = (int) (dist * 1000.0);
			if (idist > maxMeters) {
				iter.remove();
			} else {
				ns.setDistance(idist);
			}
		}

		// Order the stations by distance from request-location.
		Collections.sort(stations);

		// return the first maxHits entries.
		if (stations.size() <= maxHits) {
			return stations;
		}
		List<NearestStation> res = new ArrayList<NearestStation>();
		res.addAll(stations.subList(0, maxHits));
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.subwayworld.routeservice.IRouteService#setCity(String)
	 */
	@Override
	public void setCity(String cityCode) throws RSException {
		m_calc.setCity(cityCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.subwayworld.routeservice.IRouteService#getLocation(java.lang.String)
	 */
	@Override
	public ILocation getLocation(String loc) throws RSException {
		if (null == loc) {
			throw new NullPointerException();
		}
		int numRetries = 0;
		while (numRetries < NUM_RETRIES) {
			try {
				return m_select.getLocation(loc);
			} catch (RSConnectionException e) {
				// retry if a retry-able exception is thrown.
				numRetries++;
			}
		}
		// last retry.
		// Any exception that occurs is propagated to the caller.
		return m_select.getLocation(loc);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.subwayworld.routeservice.IRouteService#getNewMessages()
	 */
	@Override
	public List<PublishMessage> getNewMessages() throws RSException {
		int numRetries = 0;
		while (numRetries < NUM_RETRIES) {
			try {
				return m_select.getNewMessages();
			} catch (RSConnectionException e) {
				// retry if a retry-able exception is thrown.
				numRetries++;
			}
		}
		// last retry.
		// Any exception that occurs is propagated to the caller.
		return m_select.getNewMessages();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.subwayworld.routeservice.IRouteService#testGetLocations()
	 */
	@Override
	public List<ILocation> testGetLocations() throws RSException {
		int numRetries = 0;
		while (numRetries < NUM_RETRIES) {
			try {
				return m_select.testGetLocations();
			} catch (RSConnectionException e) {
				// retry if a retry-able exception is thrown.
				numRetries++;
			}
		}
		// last retry.
		// Any exception that occurs is propagated to the caller.
		return m_select.testGetLocations();
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
		int numRetries = 0;
		while (numRetries < NUM_RETRIES) {
			try {
				return m_select.getLocationEELs(cityCode);
			} catch (RSConnectionException e) {
				// retry if a retry-able exception is thrown.
				numRetries++;
			}
		}
		// last retry.
		// Any exception that occurs is propagated to the caller.
		return m_select.getLocationEELs(cityCode);
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
		int numRetries = 0;
		while (numRetries < NUM_RETRIES) {
			try {
				return m_select.getLocationsWithEEL(cityCode, eel);
			} catch (RSConnectionException e) {
				// retry if a retry-able exception is thrown.
				numRetries++;
			}
		}
		// last retry.
		// Any exception that occurs is propagated to the caller.
		return m_select.getLocationsWithEEL(cityCode, eel);
	}

	@Override
	public List<ILocation> getFilteredLocations(String cityCode, String filter) throws RSException {
		int numRetries = 0;
		while (numRetries < NUM_RETRIES) {
			try {
				return m_select.getFilteredLocations(cityCode, filter);
			} catch (RSConnectionException e) {
				// retry if a retry-able exception is thrown.
				numRetries++;
			}
		}
		// last retry.
		// Any exception that occurs is propagated to the caller.
		return m_select.getFilteredLocations(cityCode, filter);
	}

	@Override
	public List<SegmentDirection> getStationDepartures(String stationCode) throws RSException {
		int numRetries = 0;
		while (numRetries < NUM_RETRIES) {
			try {
				return m_select.getStationDepartures(stationCode);
			} catch (RSConnectionException e) {
				// retry if a retry-able exception is thrown.
				numRetries++;
			}
		}
		// last retry.
		// Any exception that occurs is propagated to the caller.
		return m_select.getStationDepartures(stationCode);
	}

}
