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
public class HsqlGeometryUserType extends AbstractDBGeometryType {

	private static final long serialVersionUID = 180L;

	private static final int[] GEOMETRY_TYPES = new int[] { Types.CLOB };

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
		int srid = Integer.parseInt(data.substring(0, data.indexOf("|")));
		Geometry geom;
		try {
			WKTReader reader = new WKTReader();
			geom = reader.read(data.substring(data.indexOf("|") + 1));
		} catch (Exception e) { // NOSONAR
			throw new RuntimeException("Couldn't parse incoming wkt geometry.", e);
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
