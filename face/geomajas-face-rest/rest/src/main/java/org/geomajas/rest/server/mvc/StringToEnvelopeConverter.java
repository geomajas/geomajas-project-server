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
