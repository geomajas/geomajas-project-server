/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.geocoder.service;

import com.vividsolutions.jts.geom.Coordinate;

import org.codehaus.jackson.map.ObjectMapper;
import org.geomajas.geometry.Bbox;
import org.geomajas.annotation.Api;
import org.geomajas.geometry.Crs;
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
import java.util.Locale;
import java.util.Map;

/**
 * Geocoder service using the Yahoo! PlaceFinder. See <a
 * href="http://developer.yahoo.com/geo/placefinder/"
 * >http://developer.yahoo.com/geo/placefinder/</a>
 * 
 * @author Joachim Van der Auwera
 * @author Jan Venstermans
 * @since 1.4.0
 */
@Api
public class YahooPlaceFinderGeocoderService implements GeocoderService {

	private final Logger log = LoggerFactory
			.getLogger(YahooPlaceFinderGeocoderService.class);

	private static final double DELTA = 1e-20; // max coordinate variation for
												// equal locations
	private static final String USER_AGENT = "Geomajas Yahoo! PlaceFinder geocoder service";
	private static final int READ_TIMEOUT = 120000;
	private static final int CONNECT_TIMEOUT = 10000;
	private static final String URL_BASE = "http://query.yahooapis.com/v1/public/yql?";

	@Autowired
	private GeoService geoService;

	@Autowired
	private DtoConverterService dtoConverterService;

	@Autowired
	private SplitCommaReverseService splitCommaReverseService;

	private Crs crs;

	private String name = "YahooPlaceFinder";

	private String appId;
	private String appIdProperty;
	private boolean skipAppIdCheck;

	@PostConstruct
	protected void initCrs() throws Exception {
		crs = geoService.getCrs2("EPSG:4326"); // WGS-84 latlong

		if (null != appIdProperty) {
			appId = System.getProperty(appIdProperty);
		}
		if (null == appId && !skipAppIdCheck) {
			throw new RuntimeException(
					"Cannot instantiate YahooPlaceFinderGeocoderService, appId not known, "
							+ "you need to set appId or appIdProperty.");
		}
	}

	@Override
	public String getName() {
		return name;
	}

	/**
	 * Set the name for this geocoder service.
	 * 
	 * @param name
	 *            name
	 * @since 1.0.0
	 */
	@Api
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Set the appId which is required for accessing the service.
	 * 
	 * @param appId
	 *            Yahoo! appId
	 * @since 1.0.0
	 */
	@Api
	public void setAppId(String appId) {
		this.appId = appId;
	}

	/**
	 * Set a property which contains the appId. This allows setting the appId
	 * from the command line instead of the configuration files.
	 * 
	 * @param appIdProperty
	 *            property which contains the appId
	 * @since 1.0.0
	 */
	@Api
	public void setAppIdProperty(String appIdProperty) {
		this.appIdProperty = appIdProperty;
	}

	/**
	 * When setting this to true, you can avoid having an exception throws when
	 * the appId is not known. It will just find nothing (it won't even try).
	 * This can be useful in combination with the setAappIdProperty() to make it
	 * run then the property is not set.
	 * 
	 * @param skipAppIdCheck
	 *            true to disable throwing the error when no appId is specified
	 * @since 1.0.0
	 */
	@Api
	public void setSkipAppIdCheck(boolean skipAppIdCheck) {
		this.skipAppIdCheck = skipAppIdCheck;
	}

	@Override
	public CoordinateReferenceSystem getCrs() {
		return crs;
	}

	@Override
	public GetLocationResult[] getLocation(List<String> location,
			int maxAlternatives, Locale locale) {
		// if (null == appId) {
		// return null;
		// }
		try {
			String query = splitCommaReverseService.combine(location);
			List<GetLocationResult> locations = search(query, maxAlternatives,
					locale);
			return locations.toArray(new GetLocationResult[locations.size()]);
		} catch (Exception ex) { // NOSONAR
			log.error("Search failed", ex);
			return null;
		}
	}

	/**
	 * Call the Yahoo! PlaceFinder service for a result.
	 * 
	 * @param q
	 *            search string
	 * @param maxRows
	 *            max number of rows in result, or 0 for all
	 * @param locale
	 *            locale for strings
	 * @return list of found results
	 * @throws Exception
	 *             oops
	 * @see <a
	 *      href="http://developer.yahoo.com/boss/geo/docs/free_YQL.html#table_pf">Yahoo!
	 *      Boss Geo PlaceFinder</a>
	 */
	public List<GetLocationResult> search(String q, int maxRows, Locale locale)
			throws Exception {
		List<GetLocationResult> searchResult = new ArrayList<GetLocationResult>();

		String url = URLEncoder.encode(q, "UTF8");
		url = "q=select%20*%20from%20geo.placefinder%20where%20text%3D%22"
				+ url + "%22";
		if (maxRows > 0) {
			url = url + "&count=" + maxRows;
		}
		url = url + "&flags=GX";
		if (null != locale) {
			url = url + "&locale=" + locale;
		}
		if (appId != null) {
			url = url + "&appid=" + appId;
		}

		InputStream inputStream = connect(url);
		if (null != inputStream) {
			SAXBuilder parser = new SAXBuilder();
			Document doc = parser.build(inputStream);

			Element root = doc.getRootElement();

			// check code for exception
			String message = root.getChildText("Error");
			// Integer errorCode = Integer.parseInt(message);
			if (message != null && Integer.parseInt(message) != 0) {
				throw new Exception(root.getChildText("ErrorMessage"));
			}
			Element results = root.getChild("results");
			for (Object obj : results.getChildren("Result")) {
				Element toponymElement = (Element) obj;
				GetLocationResult location = getLocationFromElement(toponymElement);
				searchResult.add(location);
			}
		}
		return searchResult;
	}

	/**
	 * Open the connection to the server.
	 * 
	 * @param url
	 *            the url to connect to
	 * @return returns the input stream for the connection
	 * @throws java.io.IOException
	 *             cannot get result
	 */
	private InputStream connect(String url) throws IOException {
		log.debug(URL_BASE + url);
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

		String woeid = locationElement.getChildText("woeid");

		try {
			getBoudingBox(woeid, location);
		} catch (Exception e) {
			// don't do anything: no bounding box, location is point
		}

		location.setCoordinate(new Coordinate(Double
				.parseDouble(locationElement.getChildText("longitude")), Double
				.parseDouble(locationElement.getChildText("latitude"))));

		return location;
	}

	private void canonicalAdd(List<String> canonical, Element locationElement,
			String tag) {
		String value = locationElement.getChildText(tag);
		if (null != value && value.length() > 0) {
			canonical.add(value);
		}
	}

	private void getBoudingBox(String woeid, GetLocationResult location)
			throws Exception {
		String url = "q=select%20boundingBox%20from%20geo.places%20where%20woeid%3D%22"
				+ woeid + "%22&format=json";
		InputStream inputStream = connect(url);
		if (null != inputStream) {
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> data = mapper.readValue(inputStream, Map.class);
			Map<String, Object> query = (Map<String, Object>) data.get("query");
			Map<String, Object> results = (Map<String, Object>) query
					.get("results");
			Map<String, Object> place = (Map<String, Object>) results
					.get("place");
			Map<String, Object> boundingBox = (Map<String, Object>) place
					.get("boundingBox");

			Map<String, Object> southWest = (Map<String, Object>) boundingBox
					.get("southWest");
			double south = Double.parseDouble((String) southWest
					.get("latitude"));
			double west = Double.parseDouble((String) southWest
					.get("longitude"));
			Map<String, Object> northEast = (Map<String, Object>) boundingBox
					.get("northEast");
			double north = Double.parseDouble((String) northEast
					.get("latitude"));
			double east = Double.parseDouble((String) northEast
					.get("longitude"));
			log.debug(south + " " + west + " " + north + " " + east);

			Bbox box = null;
			if (Math.abs(north - south) > DELTA
					|| Math.abs(east - west) > DELTA) {
				// return point when bbox is a point
				box = new Bbox(west, south, east - west, north - south);
			}
			if (box != null) {
				location.setEnvelope(dtoConverterService.toInternal(box));
			}
		}
	}
}
