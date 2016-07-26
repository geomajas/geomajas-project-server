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

import org.hibernatespatial.SpatialDialect;
import org.hibernatespatial.spi.SpatialDialectProvider;

/**
 * Spatial dialect provider for HSQL.
 * 
 * @author Jan De Moerloose
 */
public class HSQLSpatialDialectProvider implements SpatialDialectProvider {

	public SpatialDialect createSpatialDialect(String dialect) {
		if (dialect.equals(HSQLSpatialDialect.class.getCanonicalName())
				|| "org.hibernate.dialect.HSQLDialect".equals(dialect)
				|| "hsql".equals(dialect)) {
			return new HSQLSpatialDialect();
		} else {
			return null;
		}
	}

	public SpatialDialect getDefaultDialect() {
		return new HSQLSpatialDialect();
	}

	public String[] getSupportedDialects() {
		return new String[] { HSQLSpatialDialect.class.getCanonicalName() };
	}

}
