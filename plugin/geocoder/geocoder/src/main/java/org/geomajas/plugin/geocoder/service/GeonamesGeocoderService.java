/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.geocoder.service;

import com.vividsolutions.jts.geom.Coordinate;
import org.geomajas.annotation.Api;
import org.geomajas.global.GeomajasException;
import org.geomajas.plugin.geocoder.api.GeocoderService;
import org.geomajas.plugin.geocoder.api.GetLocationResult;
import org.geomajas.service.GeoService;
import org.geonames.FeatureClass;
import org.geonames.Style;
import org.geonames.Toponym;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Geocoder services which uses the geonames.org search web service.
 * <p/>
 * See <a href="http://www.geonames.org/export/geonames-search.html">geonames API</a>.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api
public class GeonamesGeocoderService implements GeocoderService {

	private final Logger log = LoggerFactory.getLogger(GeonamesGeocoderService.class);

	private static final double DELTA = 1e-20; // max coordinate variation for equal locations
	private static final String USER_AGENT = "Geomajas GeoNames geocoder service";
	private static final double FUZZY_VALUE = 0.8; // value for fuzzy searches
	private static final int READ_TIMEOUT = 120000;
	private static final int CONNECT_TIMEOUT = 10000;
	private static final String URL_BASE = "http://ws.geonames.org/search?";

	@Autowired
	private GeoService geoService;

	@Autowired
	private SplitCommaReverseService splitCommaReverseService;

	private CoordinateReferenceSystem crs;

	private String name = "GeoNames";

	private String userName;
	private String userNameProperty;

	@PostConstruct
	protected void initCrs() throws GeomajasException {
		crs = geoService.getCrs2("EPSG:4326"); // WGS-84 latlong

		if (null != userNameProperty) {
			userName = System.getProperty(userNameProperty);
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
	 * Set user name which is passed on to the Geonames service.
	 * This is needed as Geonames uses this to prevent DDOS attacks.
	 *
	 * @param userName geonames user name
	 * @since 1.1.0
	 */
	@Api
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Set property which needs to be used to read the user name to pass to Geonames.
	 *
	 * @param userNameProperty property to read
	 * @since 1.1.0
	 */
	@Api
	public void setUserNameProperty(String userNameProperty) {
		this.userNameProperty = userNameProperty;
	}

	public CoordinateReferenceSystem getCrs() {
		return crs;
	}

	public GetLocationResult[] getLocation(List<String> location, int maxAlternatives, Locale ignore) {
		GetLocationResult[] result;
		try {
			/* code for using the geonames library, does not support fuzzy or isNameRequired
			ToponymSearchCriteria criteria = new ToponymSearchCriteria();
			criteria.setQ(splitCommaReverseService.combine(location));
			criteria.setMaxRows(MAX_ROWS + 1);
			//criteria.setNameRequired(true);
			//criteria.setFuzzy(FUZZY_VALUE);
			GetLocationResult result = null;
			List<Toponym> toponyms = search(splitCommaReverseService.combine(location), MAX_ROWS + 1, false);
			List<Toponym> toponyms = tsr.getToponyms();
			*/
			List<Toponym> toponyms = search(splitCommaReverseService.combine(location), maxAlternatives, false);
			int resCount = toponyms.size();
			/*
			- original plan was to search one more than maximum and don't return results is too many as too ambiguous
			- but that doesn't work from straightforward queries like "london"
			if (resCount > MAX_ROWS) {
				// too many results, treat as not found
				return null;
			}
			*/

			if (0 == resCount) {
				// try fuzzy search when no results
				toponyms = search(splitCommaReverseService.combine(location), maxAlternatives, true);
			}

			// remove duplicates from the results (eg because featureClass is different)
			removeDuplicates(toponyms);
			resCount = toponyms.size();

			result = new GetLocationResult[resCount];
			for (int i = 0; i < resCount; i++) {
				Toponym toponym = toponyms.get(i);
				GetLocationResult one = new GetLocationResult();
				List<String> prefResult = new ArrayList<String>();
				prefResult.add(toponym.getCountryCode());
				prefResult.add(toponym.getName());
				one.setCanonicalStrings(prefResult);
				Coordinate coordinate = new Coordinate();
				coordinate.x = toponym.getLongitude();
				coordinate.y = toponym.getLatitude();
				one.setCoordinate(coordinate);
				result[i] = one;
			}
			return result;
		} catch (Exception ex) { // NOSONAR
			log.error("Search failed", ex);
			return null;
		}
	}

	private void removeDuplicates(List<Toponym> toponyms) {
		for (int pos = 0; pos < toponyms.size(); pos++) {
			Toponym left = toponyms.get(pos);
			for (int sec = toponyms.size() - 1; sec > pos; sec--) {
				Toponym right = toponyms.get(sec);
				if (ObjectUtils.nullSafeEquals(left.getName(), right.getName())
						&& ObjectUtils.nullSafeEquals(left.getCountryCode(), right.getCountryCode())
						&& Math.abs(left.getLongitude() - right.getLongitude()) < DELTA
						&& Math.abs(left.getLatitude() - right.getLatitude()) < DELTA) {
					toponyms.remove(sec);
				}
			}
		}
	}

	/**
	 * Full text search on the GeoNames database. Modified version from the geonames library to allow using 
	 * isNameRequired and fuzzy.
	 * <p/>
	 * This service gets the number of toponyms defined by the 'maxRows' parameter. The parameter 'style' determines
	 * which fields are returned by the service.
	 *
	 * @param q search string
	 * @param maxRows max number of rows in result, or 0 for all
	 * @param fuzzy should search be fuzzy?
	 * @return list of found toponyms
	 * @throws Exception oops
	 * @see <a href="http://www.geonames.org/export/geonames-search.html">search web service documentation</a>
	 */
	public List<Toponym> search(String q, int maxRows, boolean fuzzy) throws Exception {
		List<Toponym> searchResult = new ArrayList<Toponym>();

		String url = "q=" + URLEncoder.encode(q, "UTF8");
		url += "&isNameRequired=true";
		if (fuzzy) {
			url += "&fuzzy=" + FUZZY_VALUE;
		}
		if (maxRows > 0) {
			url += "&maxRows=" + maxRows;
		}
		url += "&style=" + Style.SHORT;
		if (null != userName) {
			url += "&username=" + userName;
		}

		InputStream inputStream = connect(url);
		if (null != inputStream) {
			SAXBuilder parser = new SAXBuilder();
			Document doc = parser.build(connect(url));

			Element root = doc.getRootElement();

			// check for exception
			Element message = root.getChild("status");
			if (message != null) {
				throw new Exception(message.getAttributeValue("message"));
			}

			for (Object obj : root.getChildren("geoname")) {
				Element toponymElement = (Element) obj;
				Toponym toponym = getToponymFromElement(toponymElement);
				searchResult.add(toponym);
			}
		}
		return searchResult;
	}

	/**
	 * Open the connection to the server.
	 *
	 * @param url the url to connect to
	 * @return returns the input stream for the connection
	 * @throws IOException cannot get result
	 */
	private InputStream connect(String url) throws IOException {
		URLConnection conn = new URL(URL_BASE + url).openConnection();
		conn.setConnectTimeout(CONNECT_TIMEOUT);
		conn.setReadTimeout(READ_TIMEOUT);
		conn.setRequestProperty("User-Agent", USER_AGENT);
		return conn.getInputStream();
	}

	private Toponym getToponymFromElement(Element toponymElement) {
		Toponym toponym = new Toponym();

		toponym.setName(toponymElement.getChildText("name"));
		toponym.setAlternateNames(toponymElement.getChildText("alternateNames"));
		toponym.setLatitude(Double.parseDouble(toponymElement.getChildText("lat")));
		toponym.setLongitude(Double.parseDouble(toponymElement.getChildText("lng")));

		toponym.setCountryCode(toponymElement.getChildText("countryCode"));
		toponym.setCountryName(toponymElement.getChildText("countryName"));

		toponym.setFeatureClass(FeatureClass.fromValue(toponymElement.getChildText("fcl")));
		toponym.setFeatureCode(toponymElement.getChildText("fcode"));

		toponym.setFeatureClassName(toponymElement.getChildText("fclName"));
		toponym.setFeatureCodeName(toponymElement.getChildText("fCodeName"));

		return toponym;
	}

}
