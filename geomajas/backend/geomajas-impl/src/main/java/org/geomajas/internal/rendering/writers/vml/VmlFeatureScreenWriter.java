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
import org.geomajas.internal.layer.feature.InternalFeatureImpl;
import org.geomajas.internal.rendering.writers.GraphicsWriter;
import org.geomajas.internal.rendering.writers.svg.SvgFeatureScreenWriter;
import org.geomajas.rendering.GraphicsDocument;
import org.geomajas.rendering.RenderException;
import org.geotools.geometry.jts.GeometryCoordinateSequenceTransformer;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ???
 *
 * @author check subversion
 */
public class VmlFeatureScreenWriter implements GraphicsWriter {

	private final Logger log = LoggerFactory.getLogger(SvgFeatureScreenWriter.class);

	private GeometryCoordinateSequenceTransformer transformer;

	private int coordWidth;

	private int coordHeight;

	public VmlFeatureScreenWriter(GeometryCoordinateSequenceTransformer transformer, int coordWidth,
			int coordHeight) {
		this.transformer = transformer;
		this.coordWidth = coordWidth;
		this.coordHeight = coordHeight;
	}

	public void writeObject(Object object, GraphicsDocument document, boolean asChild) throws RenderException {
		try {
			InternalFeatureImpl feature = (InternalFeatureImpl) object;
			Geometry geom = feature.getGeometry();
			if (feature.isClipped()) {
				geom = feature.getClippedGeometry();
			}
			geom = transformer.transform(geom);
			String styleId = feature.getLayer().getLayerInfo().getId() + "." + feature.getStyleInfo().getIndex()
					+ ".style";

			document.writeObject(geom, asChild);
			document.writeAttribute("style", "WIDTH: 100%; HEIGHT: 100%");
			document.writeAttribute("coordsize", coordWidth + "," + coordHeight);
			document.writeAttribute("type", "#" + styleId);
			document.writeAttribute("id", feature.getId());

		} catch (TransformException e) {
			log.warn("could not render feature");
		}
	}
}