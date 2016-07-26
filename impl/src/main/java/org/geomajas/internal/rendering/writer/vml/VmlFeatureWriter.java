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

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.ImageInfo;
import org.geomajas.configuration.RectInfo;
import org.geomajas.configuration.SymbolInfo;
import org.geomajas.internal.rendering.writer.GraphicsWriter;
import org.geomajas.layer.LayerType;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.rendering.GraphicsDocument;
import org.geomajas.rendering.RenderException;
import org.geotools.geometry.jts.GeometryCoordinateSequenceTransformer;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

/**
 * Vml writer for features.
 * 
 * @author Pieter De Graef
 * @author Jan De Moerloose
 */
public class VmlFeatureWriter implements GraphicsWriter {

	private final Logger log = LoggerFactory.getLogger(VmlFeatureWriter.class);

	private final GeometryCoordinateSequenceTransformer transformer;

	private final int coordWidth;

	private final int coordHeight;

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
			if (isPointLike(feature)) {
				if (feature.getStyleInfo().getSymbol() != null) {
					SymbolInfo info = feature.getStyleInfo().getSymbol();
					for (Coordinate coordinate : geom.getCoordinates()) {
						if (info.getRect() != null) {
							writeRectangle(document, coordinate, feature, info.getRect());
						} else if (info.getCircle() != null) {
							RectInfo rectInfo = new RectInfo();
							float diameter = info.getCircle().getR() * 2;
							rectInfo.setW(diameter);
							rectInfo.setH(diameter);
							writeRectangle(document, coordinate, feature, rectInfo);
						} else if (info.getImage() != null) {
							writeImage(document, coordinate, feature, info.getImage());
						}
					}
				}
			} else {
				document.writeObject(geom, asChild);
				document.writeAttribute("style", "WIDTH: 100%; HEIGHT: 100%");
				document.writeAttribute("coordsize", coordWidth + "," + coordHeight);
				document.writeAttribute("type", "#" + feature.getStyleInfo().getStyleId());
				document.writeAttribute("id", feature.getId());
			}
		} catch (TransformException e) {
			log.warn("could not render feature");
		}
	}
	
	private void writeImage(GraphicsDocument document, Coordinate coordinate, InternalFeature feature,
			ImageInfo imageInfo) throws RenderException {
		document.writeElement("vml:image", true);
		document.writeAttribute("id", feature.getId());
		int width = imageInfo.getWidth();
		int height = imageInfo.getHeight();
		int left = (int) coordinate.x - width / 2;
		int top = (int) coordinate.y - height / 2;
		document.writeAttribute("style", "WIDTH: " + width + "px; HEIGHT: " + height + "px;TOP: " + top + "px; LEFT: "
				+ left + "px;");
		document.writeAttribute("src", imageInfo.getHref());
		document.closeElement();
	}

	private void writeRectangle(GraphicsDocument document, Coordinate coordinate, InternalFeature feature,
			RectInfo rectInfo) throws RenderException {
		document.writeElement("vml:rect", true);
		document.writeAttribute("id", feature.getId());
		int width = (int) rectInfo.getW();
		int height = (int) rectInfo.getH();
		int left = (int) coordinate.x - width / 2;
		int top = (int) coordinate.y - height / 2;
		document.writeAttribute("style", "WIDTH: " + width + "px; HEIGHT: " + height + "px;TOP: " + top + "px; LEFT: "
				+ left + "px;");
		FeatureStyleInfo style = feature.getStyleInfo();
		document.writeAttribute("fillcolor", style.getFillColor());
		document.writeAttribute("strokecolor", style.getStrokeColor());
		document.writeAttribute("strokeweight", style.getStrokeWidth());

		// Rect-fill element:
		document.writeElement("vml:fill", true);
		document.writeAttribute("opacity", Float.toString(style.getFillOpacity()));
		document.closeElement();

		// Rect-stroke element:
		document.writeElement("vml:stroke", true);
		document.writeAttribute("opacity", Float.toString(style.getStrokeOpacity()));
		document.closeElement();

		// Rect element
		document.closeElement();
	}

	private boolean isPointLike(InternalFeature feature) {
		return feature.getLayer().getLayerInfo().getLayerType() == LayerType.POINT
				|| feature.getLayer().getLayerInfo().getLayerType() == LayerType.MULTIPOINT;
	}
}