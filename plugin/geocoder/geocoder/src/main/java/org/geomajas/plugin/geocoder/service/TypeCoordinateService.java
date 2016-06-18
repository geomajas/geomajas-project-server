/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.geocoder.service;

import com.vividsolutions.jts.geom.Coordinate;
import org.geomajas.annotation.Api;
import org.geomajas.geometry.Crs;
import org.geomajas.global.GeomajasException;
import org.geomajas.layer.LayerException;
import org.geomajas.plugin.geocoder.api.GeocoderService;
import org.geomajas.plugin.geocoder.api.GetLocationResult;
import org.geomajas.service.GeoService;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Geocoder service which jumps to the typed coordinate. The ordinates should be separated by whitespace.
 * The default CRS for the coordinate can be configured. An alternate CRS can be chosen using " crs:" followed by the
 * CRS at the end of the string.
 *
 * @author Joachim Van der Auwera
 * @since 1.2.0
 */
@Api
public class TypeCoordinateService implements GeocoderService {

	private static final String CRS_PREFIX = "crs:";

	private final Logger log = LoggerFactory.getLogger(TypeCoordinateService.class);

	@Autowired
	private GeoService geoService;

	private String defaultCrs = "EPSG:4326";
	private Crs crs;

	private String name = "TypeCoordinate";

	@PostConstruct
	protected void postConstruct() throws LayerException {
		crs = geoService.getCrs2(defaultCrs);
	}

	/**
	 * Set the default CRS which is used for the coordinates. When not set it defaults to EPSG:4326.
	 *
	 * @param defaultCrs default CRS
	 */
	public void setDefaultCrs(String defaultCrs) {
		this.defaultCrs = defaultCrs;
	}

	@Override
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

	@Override
	public CoordinateReferenceSystem getCrs() {
		return crs;
	}

	@Override
	public GetLocationResult[] getLocation(List<String> location, int maxAlternatives, Locale locale) {
		if (null == locale) {
			locale = Locale.US;
		}
		GetLocationResult result = new GetLocationResult();
		if (location.size() > 0) {
			String[] parts = location.get(0).split("\\s+");
			if (parts.length >= 2) {
				NumberFormat format = NumberFormat.getInstance(locale);
				try {
					Double x = format.parse(parts[0]).doubleValue();
					Double y = format.parse(parts[1]).doubleValue();
					Coordinate coordinate = new Coordinate(x, y);

					String canonicalCrs = "";
					if (parts.length > 2) {
						String crsPart = parts[2];
						if (crsPart.startsWith(CRS_PREFIX)) {
							crsPart = crsPart.substring(CRS_PREFIX.length());
							try {
								coordinate = geoService.transform(coordinate, crsPart, defaultCrs);
								canonicalCrs = " " + CRS_PREFIX + crsPart;
							} catch (GeomajasException le) {
								log.debug("Crs conversion from " + crsPart + " tot " + defaultCrs + " failed, " +
										le.getMessage());
							}
						}
					}
					result.setCoordinate(coordinate);
					List<String> canonical = new ArrayList<String>();
					canonical.add(parts[0] + " " + parts[1] + canonicalCrs);
					result.setCanonicalStrings(canonical);
					return new GetLocationResult[] { result };
				} catch (ParseException pe) {
					log.debug("Could not parse " + location.get(0) + ", " + pe.getMessage());
				}
			}
		}
		return new GetLocationResult[0];
	}
}
