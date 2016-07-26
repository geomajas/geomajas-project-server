/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.internal.rendering.writer.vml;

import java.awt.geom.Rectangle2D;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.FontStyleInfo;
import org.geomajas.configuration.LabelStyleInfo;
import org.geomajas.internal.layer.feature.InternalFeatureImpl;
import org.geomajas.internal.layer.tile.InternalTileImpl;
import org.geomajas.internal.rendering.writer.GraphicsWriter;
import org.geomajas.layer.feature.InternalFeature;
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
public class VmlLabelTileWriter implements GraphicsWriter {

	private final Logger log = LoggerFactory.getLogger(VmlLabelTileWriter.class);

	private final GeometryFactory factory;

	private final GeometryCoordinateSequenceTransformer transformer;

	private final int coordWidth;

	private final int coordHeight;

	private final LabelStyleInfo labelStyle;

	private final GeoService geoService;

	private final TextService textService;

	public VmlLabelTileWriter(int coordWidth, int coordHeight, GeometryCoordinateSequenceTransformer transformer,
			LabelStyleInfo labelStyle, GeoService geoService, TextService textService) {
		this.coordWidth = coordWidth;
		this.coordHeight = coordHeight;
		this.labelStyle = labelStyle;
		this.transformer = transformer;
		this.geoService = geoService;
		this.textService = textService;
		factory = new GeometryFactory();
	}

	public void writeObject(Object object, GraphicsDocument document, boolean asChild) throws RenderException {
		InternalTileImpl tile = (InternalTileImpl) object;
		FeatureStyleInfo bgStyle = labelStyle.getBackgroundStyle();

		document.writeElement("vml:group", asChild);
		document.writeId("labels." + tile.getCode().toString());
		document.writeAttribute("coordsize", coordWidth + "," + coordHeight);
		document.writeAttribute("style", "WIDTH: " + coordWidth + "; HEIGHT: " + coordHeight);

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
				String labelString = feature.getLabel();

				// If the attribute has no value, continue with the next:
				if (labelString == null || labelString.length() == 0) {
					document.closeElement();
					continue;
				}

				// Calculate label width, left and top:
				Rectangle2D textBox = textService.getStringBounds(labelString, labelStyle.getFontStyle());
				int boxWidth = (int) textBox.getWidth() + 8; // TODO: check why not wide enough !!!
				int boxHeight = (int) textBox.getHeight() + 2;
				int left = ((int) labelPos.getX()) - boxWidth / 2;
				int top = ((int) labelPos.getY()) - boxHeight / 2;

				// Group for an individual label (vml:group):
				document.writeElement("vml:group", true);
				document.writeAttribute("style", "LEFT: " + left + "px; TOP: " + top + "px; WIDTH: " + boxWidth
						+ "px; HEIGHT: " + boxHeight + "px; position:absolute;");
				document.writeAttribute("coordsize", boxWidth + " " + boxHeight);

				// First we draw the rectangle:
				document.writeElement("vml:rect", true);
				document.writeAttribute("id", feature.getId() + ".label");
				document.writeAttribute("style", "WIDTH: " + boxWidth + "px; HEIGHT: " + boxHeight + "px;");
				document.writeAttribute("fillcolor", bgStyle.getFillColor());
				document.writeAttribute("strokecolor", bgStyle.getStrokeColor());
				document.writeAttribute("strokeweight", bgStyle.getStrokeWidth());

				// Rect-fill element:
				document.writeElement("vml:fill", true);
				document.writeAttribute("opacity", Float.toString(bgStyle.getFillOpacity()));
				document.closeElement();

				// Rect-stroke element:
				document.writeElement("vml:stroke", true);
				document.writeAttribute("opacity", Float.toString(bgStyle.getStrokeOpacity()));
				document.closeElement();

				// Then the label-text:
				document.writeElement("vml:textbox", true);
				document.writeAttribute("id", feature.getId() + ".text");
				document.writeAttribute("v-text-anchor", "middle");
				document.writeAttribute("inset", "0px, 0px, 0px, 0px");
				document.writeAttribute("style", getCssStyle(labelStyle.getFontStyle())
						+ "; text-align:center; WIDTH: " + boxWidth + "px; HEIGHT: " + boxHeight + "px;");
				document.writeAttribute("fillcolor", labelStyle.getFontStyle().getColor());
				if (labelStyle.getFontStyle().getOpacity() > 0) {
					document.writeElement("vml:fill", true);
					document.writeAttribute("opacity", Float.toString(labelStyle.getFontStyle().getOpacity()));
					document.closeElement();
				}
				// document.writeTextNode(labelString.replaceAll(" ", "&nbsp;"));
				document.writeTextNode(labelString);
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

	private String getCssStyle(FontStyleInfo style) {
		StringBuilder css = new StringBuilder();
		if (style.getColor() != null && !"".equals(style.getColor())) {
			css.append("color:");
			css.append(style.getColor());
			css.append(";");
		}
		if (style.getFamily() != null && !"".equals(style.getFamily())) {
			css.append("font-family:");
			css.append(style.getFamily());
			css.append(";");
		}
		if (style.getStyle() != null && !"".equals(style.getStyle())) {
			css.append("font-style:");
			css.append(style.getStyle());
			css.append(";");
		}
		if (style.getWeight() != null && !"".equals(style.getWeight())) {
			css.append("font-weight:");
			css.append(style.getWeight());
			css.append(";");
		}
		if (style.getSize() >= 0) {
			css.append("font-size:");
			css.append(style.getSize());
			css.append("px;");
		}
		return css.toString();
	}

}