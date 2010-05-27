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

package org.geomajas.internal.rendering.writer.svg;

import java.awt.geom.Rectangle2D;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.FontStyleInfo;
import org.geomajas.configuration.LabelStyleInfo;
import org.geomajas.internal.rendering.writer.GraphicsWriter;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.layer.tile.InternalTile;
import org.geomajas.rendering.GraphicsDocument;
import org.geomajas.rendering.RenderException;
import org.geomajas.service.GeoService;
import org.geomajas.service.TextService;
import org.geotools.geometry.jts.GeometryCoordinateSequenceTransformer;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * Writer for tile labels.
 * 
 * @author Jan De Moerloose
 * @author Pieter De Graef
 */
public class SvgLabelTileWriter implements GraphicsWriter {

	private final Logger log = LoggerFactory.getLogger(SvgLabelTileWriter.class);

	private GeometryFactory factory;

	private GeometryCoordinateSequenceTransformer transformer;

	private LabelStyleInfo labelStyle;

	private GeoService geoService;

	private TextService textService;

	public SvgLabelTileWriter(GeometryCoordinateSequenceTransformer transformer, LabelStyleInfo labelStyle,
			GeoService geoService, TextService textService) {
		this.transformer = transformer;
		this.labelStyle = labelStyle;
		this.geoService = geoService;
		this.textService = textService;
		this.factory = new GeometryFactory();
	}

	public void writeObject(Object object, GraphicsDocument document, boolean asChild) throws RenderException {
		InternalTile tile = (InternalTile) object;
		FeatureStyleInfo bgStyle = labelStyle.getBackgroundStyle();
		document.writeElement("g", asChild);
		document.writeId("labels." + tile.getCode().toString());
		for (InternalFeature feature : tile.getFeatures()) {
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

				Rectangle2D textBox = textService.getStringBounds(labelString, labelStyle.getFontStyle());
				document.writeElement("rect", createChild);
				document.writeAttribute("id", feature.getId() + ".lblBG");
				document.writeAttribute("x", labelPos.getX() - ((int) textBox.getWidth() / 2));
				document.writeAttribute("y", labelPos.getY() - ((int) textBox.getHeight()));
				document.writeAttribute("width", (int) textBox.getWidth());
				document.writeAttribute("height", (int) textBox.getHeight());
				document.writeAttribute("style", getCssStyle(bgStyle));
				createChild = false;

				// Text:
				document.writeElement("text", createChild);
				document.writeAttribute("id", feature.getId() + ".lblTXT");
				document.writeAttribute("x", labelPos.getX());
				// pull up baseline position to accommodate for descent
				document.writeAttribute("y", labelPos.getY() - (int) textBox.getMaxY());
				// TODO: config option, center label
				document.writeAttribute("text-anchor", "middle");
				document.writeAttribute("style", getCssStyle(labelStyle.getFontStyle()));

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
		document.closeElement();
	}
	
	private String getCssStyle(FontStyleInfo style) {
		String css = "";
		if (style.getColor() != null && !"".equals(style.getColor())) {
			css += "fill:" + style.getColor() + ";";
		}
		if (style.getFamily() != null && !"".equals(style.getFamily())) {
			css += "font-family:" + style.getFamily() + ";";
		}
		if (style.getStyle() != null && !"".equals(style.getStyle())) {
			css += "font-style:" + style.getStyle() + ";";
		}
		if (style.getWeight() != null && !"".equals(style.getWeight())) {
			css += "font-weight:" + style.getWeight() + ";";
		}
		if (style.getSize() >= 0) {
			css += "font-size:" + style.getSize() + "px;";
		}
		return css;
	}


	private String getCssStyle(FeatureStyleInfo style) {
		String css = "";
		if (style.getFillColor() != null && !"".equals(style.getFillColor())) {
			css += "fill:" + style.getFillColor() + ";";
		}
		if (style.getFillOpacity() != -1) {
			css += "fill-opacity:" + style.getFillOpacity() + ";";
		}
		if (style.getStrokeColor() != null && !"".equals(style.getStrokeColor())) {
			css += "stroke:" + style.getStrokeColor() + ";";
		}
		if (style.getStrokeOpacity() != -1) {
			css += "stroke-opacity:" + style.getStrokeOpacity() + ";";
		}
		if (style.getStrokeWidth() >= 0) {
			css += "stroke-width:" + style.getStrokeWidth() + ";";
		}
		return css;
	}
	
}