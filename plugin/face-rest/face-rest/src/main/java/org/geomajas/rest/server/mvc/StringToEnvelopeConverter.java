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
package org.geomajas.rest.server.mvc;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Converts a comma-separated string to an envelope.
 * 
 * @author Jan De Moerloose
 * 
 */
@Component
public class StringToEnvelopeConverter implements Converter<String, Envelope> {

	public Envelope convert(String source) {
		if (source == null) {
			return null;
		} else {
			String[] args = source.trim().split("[\\s,]+");
			if (args.length != 4) {
				throw new IllegalArgumentException("Cannot parse envelope from " + source
						+ ", expected format is \"xmin,ymin,xmax,ymax\"");
			} else {
				double[] coords = new double[4];
				try {
					for (int i = 0; i < coords.length; i++) {
						coords[i] = Double.parseDouble(args[i]);
					}
					return new Envelope(coords[0], coords[1], coords[2], coords[3]);
				} catch (NumberFormatException nfe) {
					throw new IllegalArgumentException("Cannot parse envelope from " + source
							+ ", expected format is \"xmin,ymin,xmax,ymax\"", nfe);
				}
			}
		}
	}
}
