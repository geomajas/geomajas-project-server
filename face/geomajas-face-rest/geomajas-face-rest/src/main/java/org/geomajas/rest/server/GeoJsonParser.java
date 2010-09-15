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
package org.geomajas.rest.server;

import java.io.IOException;

import org.geotools.feature.FeatureCollection;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Parses and produces GeoJSON.
 * 
 * @author Jan De Moerloose
 *
 */
public class GeoJsonParser {

	private GeometryJSON gjson = new GeometryJSON();

	private  FeatureJSON fjson = new FeatureJSON();

	public Object read(Object input) throws IOException {
		throw new UnsupportedOperationException();
	}

	public void write(Object obj, Object output) throws IOException {
		if (obj instanceof Geometry) {
			gjson.write((Geometry) obj, output);
		} else if (obj instanceof Feature || obj instanceof CoordinateReferenceSystem
				|| obj instanceof FeatureCollection) {
			if (obj instanceof SimpleFeature) {
				fjson.writeFeature((SimpleFeature) obj, output);
			} else if (obj instanceof FeatureCollection) {
				fjson.writeFeatureCollection((FeatureCollection) obj, output);
			} else if (obj instanceof CoordinateReferenceSystem) {
				fjson.writeCRS((CoordinateReferenceSystem) obj, output);
			} else {
				throw new IllegalArgumentException("Unable able to encode object of type " + obj.getClass());
			}

		}
	}

}
