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
package org.geomajas.rest.server;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.kml.KMLConfiguration;
import org.geotools.xml.Parser;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.AttributeDescriptor;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Parses and produces GeoJSON.
 * 
 * @author Oliver May
 *
 */
public class TxtParser {

	public Object read(Object input) throws IOException {
		Parser parser = new Parser(new KMLConfiguration());
//		return parser.parse((InputStream) input);
		return null;
	}

	public void write(SimpleFeatureCollection obj, OutputStream output) throws IOException {
		SimpleFeatureIterator it = obj.features();

		PrintStream printStream = new PrintStream(output);

		while (it.hasNext()) {
			SimpleFeature ft = it.next();
			printStream.print(ft.getID() + " [");
			boolean first = true;
			for (AttributeDescriptor ad : ft.getType().getAttributeDescriptors()) {
				if (!first) {
					printStream.print(", ");
				}
				printStream.print(ad.getLocalName() + ": " + ft.getAttribute(ad.getName()));
				first = false;
			}
			printStream.println("]");
		}

	}

}
