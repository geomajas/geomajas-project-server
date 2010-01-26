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

package org.geomajas.internal.rendering.writers.svg;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.Point;
import org.geomajas.internal.layer.feature.VectorFeature;
import org.geomajas.internal.rendering.writers.GraphicsWriter;
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
public class SvgFeatureScreenWriter implements GraphicsWriter {

	private final Logger log = LoggerFactory.getLogger(SvgFeatureScreenWriter.class);

	private GeometryCoordinateSequenceTransformer transformer;

	public SvgFeatureScreenWriter(GeometryCoordinateSequenceTransformer transformer) {
		this.transformer = transformer;
	}

	public void writeObject(Object object, GraphicsDocument document, boolean asChild) throws RenderException {
		try {
			VectorFeature feature = (VectorFeature) object;
			Geometry geom = feature.getGeometry();
			if (feature.isClipped()) {
				geom = feature.getClippedGeometry();
			}
			geom = transformer.transform(geom);

			if (geom instanceof Point || geom instanceof MultiPoint) {
				// write the enclosing group
				document.writeElement("g", asChild);
				document.writeAttribute("id", feature.getId());
				// document.writeAttribute("style", feature.getCssStyle());
				// write the point
				document.writeObject(geom, true);
				document.writeId(feature.getId());
				String styleId =
						feature.getLayer().getLayerInfo().getId() + "." + feature.getStyleInfo().getId() + ".style";
				document.writeAttribute("xlink:href", "#" + styleId);
				document.closeElement();
			} else {
				document.writeObject(geom, asChild);
				document.writeAttribute("id", feature.getId());
			}
		} catch (TransformException e) {
			log.warn("could not render feature");
		}
	}
}