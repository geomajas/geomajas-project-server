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

package org.geomajas;

import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Crs converter.
 *
 * @author Pieter De Graef
 */
public class CrsConverter {

	public static void main(String[] args) {
		Envelope env = new Envelope(-20026376.393709917, 20026376.393709917, -20026376.393709917, 20026376.393709917);
		Envelope lonlat = getClientMaxExtent("EPSG:4326", "EPSG:900913", env);
		System.out.println(lonlat);
		
		lonlat = getClientMaxExtent("EPSG:900913", "EPSG:4326", new Envelope(-180, 180, -90, 90));
		System.out.println(lonlat);
	}

	public static Envelope getClientMaxExtent(String mapCrsKey, String layerCrsKey, Envelope serverBbox) {
		try {
			CoordinateReferenceSystem mapCrs = CRS.decode(mapCrsKey);
			CoordinateReferenceSystem layerCrs = CRS.decode(layerCrsKey);
			MathTransform transformer = CRS.findMathTransform(layerCrs, mapCrs);
			return JTS.transform(serverBbox, transformer);
		} catch (FactoryException e) {
			e.printStackTrace();
		} catch (TransformException e) {
			e.printStackTrace();
		}
		return null;
	}

}
