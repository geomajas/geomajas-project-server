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

package org.geomajas.internal.rendering.writers.vml;

import com.vividsolutions.jts.geom.Geometry;
import org.geomajas.internal.rendering.writers.GraphicsWriter;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.rendering.GraphicsDocument;
import org.geomajas.rendering.RenderException;
import org.geotools.geometry.jts.GeometryCoordinateSequenceTransformer;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Vml writer for features.
 * 
 * @author Pieter De Graef
 * @author Jan De Moerloose
 */
public class VmlFeatureWriter implements GraphicsWriter {

	private final Logger log = LoggerFactory.getLogger(VmlFeatureWriter.class);

	private GeometryCoordinateSequenceTransformer transformer;

	private int coordWidth;

	private int coordHeight;

	public VmlFeatureWriter(GeometryCoordinateSequenceTransformer transformer, int coordWidth, int coordHeight) {
		this.transformer = transformer;
		this.coordWidth = coordWidth;
		this.coordHeight = coordHeight;
	}

	public void writeObject(Object object, GraphicsDocument document, boolean asChild) throws RenderException {
		try {
			InternalFeature feature = (InternalFeature) object;
			Geometry geom = feature.getGeometry();
			if (feature.isClipped()) {
				geom = feature.getClippedGeometry();
			}
			geom = transformer.transform(geom);

			document.writeObject(geom, asChild);
			document.writeAttribute("style", "WIDTH: 100%; HEIGHT: 100%");
			document.writeAttribute("coordsize", coordWidth + "," + coordHeight);
			document.writeAttribute("type", "#" + feature.getStyleInfo().getStyleId());
			document.writeAttribute("id", feature.getId());
		} catch (TransformException e) {
			log.warn("could not render feature");
		}
	}
}