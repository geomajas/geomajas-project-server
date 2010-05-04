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
package org.geomajas.layer.hibernate;

import java.sql.Connection;
import java.sql.Types;

import org.hibernatespatial.AbstractDBGeometryType;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;

/**
 * WKT based geometry type for HSQL.
 *  
 * @author Jan De Moerloose
 */
public class HSQLGeometryUserType extends AbstractDBGeometryType {

	private static final int SRIDLEN = 4;

	private static final int[] GEOMETRY_TYPES = new int[] { Types.VARCHAR };

	public int[] sqlTypes() {
		return GEOMETRY_TYPES;
	}

	/**
	 * Converts the native geometry object to a JTS <code>Geometry</code>.
	 * 
	 * @param object
	 *            native database geometry object (depends on the JDBC spatial
	 *            extension of the database)
	 * @return JTS geometry corresponding to geomObj.
	 */
	public Geometry convert2JTS(Object object) {
		if (object == null) {
			return null;
		}
		String data = (String) object;
		int srid = Integer.parseInt(data.substring(0, SRIDLEN - 1));
		Geometry geom = null;
		try {
			WKTReader reader = new WKTReader();
			geom = reader.read(data.substring(SRIDLEN + 1));
		} catch (Exception e) {
			throw new RuntimeException("Couldn't parse incoming wkt geometry.");
		}
		geom.setSRID(srid);
		return geom;
	}

	/**
	 * Converts a JTS <code>Geometry</code> to a native geometry object.
	 * 
	 * @param jtsGeom
	 *            JTS Geometry to convert
	 * @param connection
	 *            the current database connection
	 * @return native database geometry object corresponding to jtsGeom.
	 */
	public Object conv2DBGeometry(Geometry jtsGeom, Connection connection) {
		int srid = jtsGeom.getSRID();
		WKTWriter writer = new WKTWriter();
		String wkt = writer.write(jtsGeom);
		return srid + "|" + wkt;
	}

}
