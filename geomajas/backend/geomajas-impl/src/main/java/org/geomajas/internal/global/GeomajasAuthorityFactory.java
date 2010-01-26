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
package org.geomajas.internal.global;

import org.geotools.referencing.crs.EPSGCRSAuthorityFactory;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CRSFactory;

import java.net.URL;

/**
 * Pulls in Google Mercator CRS. TODO: EPSGCRSAuthorityFactory is deprecated !!!
 *
 * @author Jan De Moerloose
 *
 */
public class GeomajasAuthorityFactory extends EPSGCRSAuthorityFactory {

	public GeomajasAuthorityFactory() {
		super();
		addGoogle();
	}

	public GeomajasAuthorityFactory(CRSFactory arg0, URL arg1) throws FactoryException {
		super(arg0, arg1);
		addGoogle();
	}

	public GeomajasAuthorityFactory(CRSFactory arg0) {
		super(arg0);
		addGoogle();
	}

	private void addGoogle() {
		epsg.put("900913", "PROJCS[\"Google Mercator\", " + "GEOGCS[\"WGS 84\", "
				+ "DATUM[\"World Geodetic System 1984\", "
				+ "SPHEROID[\"WGS 84\", 6378137.0, 298.257223563, AUTHORITY[\"EPSG\",\"7030\"]], "
				+ "AUTHORITY[\"EPSG\",\"6326\"]], "
				+ "PRIMEM[\"Greenwich\", 0.0, AUTHORITY[\"EPSG\",\"8901\"]], "
				+ "UNIT[\"degree\", 0.017453292519943295], " + "AXIS[\"Geodetic latitude\", NORTH], "
				+ "AXIS[\"Geodetic longitude\", EAST], " + "AUTHORITY[\"EPSG\",\"4326\"]],  "
				+ "PROJECTION[\"Mercator (1SP)\", AUTHORITY[\"EPSG\",\"9804\"]], "
				+ "PARAMETER[\"semi_major\", 6378137.0], " + "PARAMETER[\"semi_minor\", 6378137.0], "
				+ "PARAMETER[\"latitude_of_origin\", 0.0], " + "PARAMETER[\"central_meridian\", 0.0], "
				+ "PARAMETER[\"scale_factor\", 1.0],  " + "PARAMETER[\"false_easting\", 0.0],  "
				+ "PARAMETER[\"false_northing\", 0.0],  " + "UNIT[\"m\", 1.0],  "
				+ "AXIS[\"Easting\", EAST],  " + "AXIS[\"Northing\", NORTH], "
				+ "AUTHORITY[\"EPSG\",\"900913\"]]");
	}

}
