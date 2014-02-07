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
package org.geomajas.rest.server;

import java.io.IOException;

import org.geotools.geojson.GeoJSON;

/**
 * Parses and produces GeoJSON.
 * 
 * @author Jan De Moerloose
 *
 */
public class GeoJsonParser {

	public Object read(Object input) throws IOException {
		return GeoJSON.read(input);
	}

	public void write(Object obj, Object output) throws IOException {
		GeoJSON.write(obj, output);
	}

}
