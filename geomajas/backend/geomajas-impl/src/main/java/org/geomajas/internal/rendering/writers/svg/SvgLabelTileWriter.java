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

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import org.geomajas.configuration.StyleInfo;
import org.geomajas.internal.layer.feature.InternalFeatureImpl;
import org.geomajas.internal.layer.tile.InternalVectorTile;
import org.geomajas.internal.rendering.writers.GraphicsWriter;
import org.geomajas.rendering.GraphicsDocument;
import org.geomajas.rendering.RenderException;
import org.geomajas.rendering.style.StyleFilter;
import org.geomajas.service.GeoService;
import org.geotools.geometry.jts.GeometryCoordinateSequenceTransformer;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ???
 *
 * @author check subversion
 */
public class SvgLabelTileWriter implements GraphicsWriter {

	private final Logger log = LoggerFactory.getLogger(SvgFeatureScreenWriter.class);

	private GeometryFactory factory;

	private GeometryCoordinateSequenceTransformer transformer;

	private StyleInfo bgStyle;

	private GeoService geoService;

	public SvgLabelTileWriter(GeometryCoordinateSequenceTransformer transformer, StyleInfo bgStyle,
			GeoService geoService) {
		this.transformer = transformer;
		this.bgStyle = bgStyle;
		this.geoService = geoService;
		this.factory = new GeometryFactory();
	}

	public void writeObject(Object object, GraphicsDocument document, boolean asChild) throws RenderException {
		InternalVectorTile tile = (InternalVectorTile) object;
		document.writeElement("g", asChild);
		document.writeId("labels." + tile.getCode().toString());
		for (org.geomajas.layer.feature.InternalFeature f : tile.getFeatures()) {
			InternalFeatureImpl feature = (InternalFeatureImpl) f;
			if (!feature.getStyleInfo().equals(StyleFilter.DEFAULT_STYLE_ID + "")) {
				Coordinate pos = geoService.calcDefaultLabelPosition(feature);
				if (pos == null) {
					continue;
				}
				com.vividsolutions.jts.geom.Point p = factory.createPoint(pos);
				com.vividsolutions.jts.geom.Point labelPos;
				try {
					String labelString = feature.getLabel();
					labelPos = (com.vividsolutions.jts.geom.Point) transformer.transform(p);
					boolean createChild = true;

					// Background:
					if (bgStyle != null && labelString != null && labelString.length() > 0) {
						// We assume font-size = 12 !!!!
						int width = labelString.length() * 8 + 10;
						int height = 12;
						document.writeElement("rect", createChild);
						document.writeAttribute("id", feature.getId() + ".lblBG");
						document.writeAttribute("x", labelPos.getX() - (width / 2));
						document.writeAttribute("y", labelPos.getY() - (height - 2));
						document.writeAttribute("width", width);
						document.writeAttribute("height", height);
						document.writeAttribute("style", "fill: " + bgStyle.getFillColor()
								+ "; fill-opacity: " + bgStyle.getFillOpacity() + "; stroke: "
								+ bgStyle.getStrokeColor() + "; stroke-opacity: "
								+ bgStyle.getStrokeOpacity() + "; stroke-width: " + bgStyle.getStrokeWidth()
								+ ";");
						createChild = false;
					}

					// Text:
					document.writeElement("text", createChild);
					document.writeAttribute("id", feature.getId() + ".lblTXT");
					document.writeAttribute("x", labelPos.getX());
					document.writeAttribute("y", labelPos.getY());
					// TODO: config option, center label
					document.writeAttribute("text-anchor", "middle");

					if (labelString == null) {
						document.closeElement();
						continue;
					}
					document.writeTextNode(labelString);
					document.closeElement();
				} catch (TransformException e) {
					log.warn("Label for " + feature.getId() + " could not be written!");
				}
			}
		}
		document.closeElement();
	}
}