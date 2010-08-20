/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.plugin.geocoder.service;

import com.vividsolutions.jts.geom.Coordinate;
import org.geomajas.geometry.Bbox;
import org.geomajas.global.Api;
import org.geomajas.plugin.geocoder.api.GeocoderService;
import org.geomajas.plugin.geocoder.api.GetLocationResult;
import org.geomajas.service.DtoConverterService;
import org.geomajas.service.GeoService;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Geocoder service using the Yahoo! PlaceFinder.
 * See <a href="http://developer.yahoo.com/geo/placefinder/">http://developer.yahoo.com/geo/placefinder/</a>
 *
 * @author Joachim Van der Auwera
 */
public class YahooPlaceFinderGeocoderService implements GeocoderService {

	private final Logger log = LoggerFactory.getLogger(GeonamesGeocoderService.class);

	private static final int MAX_ROWS = 50; // max number of rows in result
	private static final double DELTA = 1e-20; // max coordinate variation for equal locations
	private static final String USER_AGENT = "Geomajas Yahoo! PlaceFinder geocoder service";
	private static final int READ_TIMEOUT = 120000;
	private static final int CONNECT_TIMEOUT = 10000;
	private static final String URL_BASE = "http://where.yahooapis.com/geocode?";

	@Autowired
	private GeoService geoService;

	@Autowired
	private DtoConverterService dtoConverterService;

	@Autowired
	private SplitCommaReverseService splitCommaReverseService;

	private CoordinateReferenceSystem crs;

	private String name = "GeoNames";

	private String appId;
	private String appIdProperty;
	private boolean skipAppIdCheck;

	@PostConstruct
	private void initCrs() throws Exception {
		crs = geoService.getCrs("EPSG:4326"); // WGS-84 latlong

		if (null != appIdProperty) {
			appId = System.getProperty(appIdProperty);
		}
		if (null == appId && !skipAppIdCheck) {
			throw new RuntimeException("Cannot instantiate YahooPlaceFinderGeocoderService, appid not known, " +
					"you need to set appId or appIdProperty.");
		}
	}

	public String getName() {
		return name;
	}

	/**
	 * Set the name for this geocoder service.
	 *
	 * @param name name
	 */
	@Api
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Set the appId which is required for accessing the service.
	 *
	 * @param appId Yahoo! appId
	 */
	@Api
	public void setAppId(String appId) {
		this.appId = appId;
	}

	/**
	 * Set a property which contains the appId. This allows setting the appId from the command line instead of the
	 * configuration files.
	 *
	 * @param appIdProperty property which contains the appId
	 */
	@Api
	public void setAppIdProperty(String appIdProperty) {
		this.appIdProperty = appIdProperty;
	}

	/**
	 * When setting this to true, you can avoid having an exception throws when the appId is not known.
	 * It will just find nothing (it won't even try).
	 * This can be useful in combination with the setAappIdProperty() to make it run then the property is not set.
	 *
	 * @param skipAppIdCheck true to disable throwing the error when no appId is specified
	 */
	@Api
	public void setSkipAppIdCheck(boolean skipAppIdCheck) {
		this.skipAppIdCheck = skipAppIdCheck;
	}

	public CoordinateReferenceSystem getCrs() {
		return crs;
	}

	public GetLocationResult[] getLocation(List<String> location) {
		if (null == appId) {
			return null;
		}
		try {
			List<GetLocationResult> locations = search(splitCommaReverseService.combine(location), MAX_ROWS);
			return locations.toArray(new GetLocationResult[locations.size()]);
		} catch (Exception ex) {
			log.error("Search failed", ex);
			return null;
		}
	}

	/**
	 * Call the Yahoo! PlaceFinder service for a result.
	 *
	 * @param q search string
	 * @param maxRows max number of rows in result, or 0 for all
	 * @return list of found results
	 * @throws Exception oops
	 * @see <a href="http://www.geonames.org/export/geonames-search.html">search web service documentation</a>
	 */
	public List<GetLocationResult> search(String q, int maxRows) throws Exception {
		List<GetLocationResult> searchResult = new ArrayList<GetLocationResult>();

		String url = "q=" + URLEncoder.encode(q, "UTF8");
		if (maxRows > 0) {
			url = url + "&count=" + maxRows;
		}
		url = url + "&flags=GX";
		url = url + "&appid=" + appId;

		SAXBuilder parser = new SAXBuilder();
		Document doc = parser.build(connect(url));

		Element root = doc.getRootElement();

		// check for exception
		String message = root.getChildText("ErrorMessage");
		if (message != null && message.trim().length() > 0 && !"No error".equals(message)) {
			throw new Exception(message);
		}

		for (Object obj : root.getChildren("Result")) {
			Element toponymElement = (Element) obj;
			GetLocationResult location = getLocationFromElement(toponymElement);
			searchResult.add(location);
		}

		return searchResult;
	}

	/**
	 * Open the connection to the server.
	 *
	 * @param url the url to connect to
	 * @return returns the input stream for the connection
	 * @throws java.io.IOException cannot get result
	 */
	private InputStream connect(String url) throws IOException {
		URLConnection conn = new URL(URL_BASE + url).openConnection();
		conn.setConnectTimeout(CONNECT_TIMEOUT);
		conn.setReadTimeout(READ_TIMEOUT);
		conn.setRequestProperty("User-Agent", USER_AGENT);
		return conn.getInputStream();
	}

	private GetLocationResult getLocationFromElement(Element locationElement) {
		GetLocationResult location = new GetLocationResult();

		List<String> canonical = new ArrayList<String>();
		canonicalAdd(canonical, locationElement, "line4");
		canonicalAdd(canonical, locationElement, "line3");
		canonicalAdd(canonical, locationElement, "line2");
		canonicalAdd(canonical, locationElement, "line1");
		if (0 == canonical.size()) {
			canonicalAdd(canonical, locationElement, "level5");
			canonicalAdd(canonical, locationElement, "level4");
			canonicalAdd(canonical, locationElement, "level3");
			canonicalAdd(canonical, locationElement, "level2");
			canonicalAdd(canonical, locationElement, "level1");
			canonicalAdd(canonical, locationElement, "level0");
		}
		location.setCanonicalStrings(canonical);

		Element area = locationElement.getChild("boundingbox");
		if (null != area) {
			double north = Double.parseDouble(area.getChildText("north"));
			double south = Double.parseDouble(area.getChildText("south"));
			double east = Double.parseDouble(area.getChildText("east"));
			double west = Double.parseDouble(area.getChildText("west"));
			if (Math.abs(north - south) > DELTA || Math.abs(east - west) > DELTA) {
				// return point when bbox is a point
				Bbox bbox = new Bbox(south, west, north - south, east - west);
				location.setEnvelope(dtoConverterService.toInternal(bbox));
			}
		}

		location.setCoordinate(new Coordinate(Double.parseDouble(locationElement.getChildText("longitude")),
				Double.parseDouble(locationElement.getChildText("latitude"))));

		return location;
	}

	private void canonicalAdd(List<String> canonical, Element locationElement, String tag) {
		String value = locationElement.getChildText(tag);
		if (null != value && value.length() > 0) {
			canonical.add(value);
		}
	}

}
