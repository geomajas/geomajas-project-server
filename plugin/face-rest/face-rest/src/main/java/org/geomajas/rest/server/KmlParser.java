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

import org.geotools.kml.KML;
import org.geotools.kml.KMLConfiguration;
import org.geotools.xml.Encoder;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Parses and produces GeoJSON.
 * 
 * @author Oliver May
 *
 */
public class KmlParser {

	public Object read(Object input) throws IOException {
		return null;
	}

	public void write(Object obj, OutputStream output) throws IOException {
		Encoder encoder = new Encoder(new KMLConfiguration());
		encoder.setIndenting(true);
		encoder.encode(obj, KML.kml, output);
	}

}
