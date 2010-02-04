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

import org.geomajas.configuration.StyleInfo;
import org.geomajas.internal.layer.feature.InternalFeatureImpl;
import org.geomajas.internal.layer.tile.InternalTileImpl;
import org.geomajas.internal.rendering.writers.GraphicsWriter;
import org.geomajas.internal.rendering.writers.svg.SvgFeatureScreenWriter;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.rendering.GraphicsDocument;
import org.geomajas.rendering.RenderException;
import org.geomajas.service.GeoService;
import org.geotools.geometry.jts.GeometryCoordinateSequenceTransformer;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * ???
 * 
 * @author check subversion
 */
public class VmlLabelTileWriter implements GraphicsWriter {

	private final Logger log = LoggerFactory.getLogger(SvgFeatureScreenWriter.class);

	private static final float FONT_WIDTH = 7.2f;

	private static final int FONT_HEIGHT = 10;

	private GeometryFactory factory;

	private GeometryCoordinateSequenceTransformer transformer;

	private int coordWidth;

	private int coordHeight;

	private StyleInfo bgStyle;

	private GeoService geoService;

	public VmlLabelTileWriter(int coordWidth, int coordHeight, GeometryCoordinateSequenceTransformer transformer,
			StyleInfo bgStyle, GeoService geoService) {
		this.coordWidth = coordWidth;
		this.coordHeight = coordHeight;
		this.bgStyle = bgStyle;
		this.transformer = transformer;
		this.geoService = geoService;
		factory = new GeometryFactory();
	}

	public void writeObject(Object object, GraphicsDocument document, boolean asChild) throws RenderException {
		InternalTileImpl tile = (InternalTileImpl) object;
		document.writeElement("vml:group", asChild);
		document.writeId("labels." + tile.getCode().toString());
		document.writeAttribute("style", "WIDTH: 100%; HEIGHT: 100%");
		document.writeAttribute("coordsize", coordWidth + "," + coordHeight);

		// the shapetype
		document.writeElement("vml:shapetype", true);
		document.writeAttribute("id", "labels." + tile.getCode().toString() + ".style");
		document.writeAttribute("style", "WIDTH: 100%; HEIGHT: 100%");
		document.writeAttribute("style", "VISIBILITY: hidden");
		document.writeAttribute("filled", "f");
		document.writeAttribute("stroked", "f");
		document.writeAttribute("coordsize", coordWidth + "," + coordHeight);
		// up to shapetype
		document.closeElement();

		for (InternalFeature f : tile.getFeatures()) {
			InternalFeatureImpl feature = (InternalFeatureImpl) f;
			Coordinate pos = geoService.calcDefaultLabelPosition(feature);
			if (pos == null) {
				continue;
			}
			com.vividsolutions.jts.geom.Point p = factory.createPoint(pos);
			com.vividsolutions.jts.geom.Point labelPos;
			try {
				labelPos = (com.vividsolutions.jts.geom.Point) transformer.transform(p);
				String label = feature.getLabel();

				// If the attribute has no value, continue with the next:
				if (label == null || label.length() == 0) {
					document.closeElement();
					continue;
				}

				// Calculate label width, left and top:
				int boxWidth = (int) ((label.length()) * FONT_WIDTH) + 2;
				int boxHeight = (int) (FONT_HEIGHT + 4);
				int left = ((int) labelPos.getX()) - boxWidth / 2;
				int top = ((int) labelPos.getY()) - FONT_HEIGHT / 2;

				// Group for an individual label (vml:group):
				document.writeElement("vml:group", true);
				document.writeAttribute("style", "LEFT: " + left + "px; TOP: " + top + "px; WIDTH: " + boxWidth
						+ "px; HEIGHT: " + boxHeight + "px; position:absolute;");
				document.writeAttribute("coordsize", boxWidth + " " + boxHeight);

				// First we draw the rectangle:
				if (bgStyle != null) {
					document.writeElement("vml:rect", true);
					document.writeAttribute("id", feature.getId() + ".label");
					document.writeAttribute("style", "WIDTH: " + boxWidth + "px; HEIGHT: " + boxHeight + "px;");
					document.writeAttribute("fillcolor", bgStyle.getFillColor());
					document.writeAttribute("strokecolor", bgStyle.getStrokeColor());
					document.writeAttribute("strokeweight", bgStyle.getStrokeWidth());

					// Rect-fill element:
					document.writeElement("vml:fill", true);
					document.writeAttribute("opacity", "" + bgStyle.getFillOpacity());
					document.closeElement();

					// Rect-stroke element:
					document.writeElement("vml:stroke", true);
					document.writeAttribute("opacity", "" + bgStyle.getStrokeOpacity());
					document.closeElement();
				}

				// Then the label-text:
				document.writeElement("vml:textbox", true);
				document.writeAttribute("id", feature.getId() + ".text");
				document.writeAttribute("style", "font-family: monospace;font-size: 8pt;");
				document.writeAttribute("inset", "0px, 0px, 0px, 0px");
				document.writeTextNode(label.replaceAll(" ", "&nbsp;"));
				document.closeElement();

				// Close the vml:rect
				document.closeElement();

				// Close the individual label group (vml:group):
				document.closeElement();
			} catch (TransformException e) {
				log.warn("Label for " + feature.getId() + " could not be written!");
			}
		}
		document.closeElement();
	}
}