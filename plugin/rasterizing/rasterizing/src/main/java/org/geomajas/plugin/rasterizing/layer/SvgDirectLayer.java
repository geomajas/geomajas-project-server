/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.rasterizing.layer;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import org.apache.batik.gvt.GraphicsNode;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.DirectLayer;
import org.geotools.map.MapContent;
import org.geotools.map.MapViewport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Geotools layer responsible for rendering svg. Svg is rendered using batik. A transformation from HTML to screen space
 * is required.
 * 
 * @author Jan De Moerloose
 * 
 */
public class SvgDirectLayer extends DirectLayer {

	private GraphicsNode graphicsNode;

	private AffineTransform htmlToScreen;

	private final Logger log = LoggerFactory.getLogger(SvgDirectLayer.class);

	@Override
	public void draw(Graphics2D graphics, MapContent map, MapViewport viewport) {
		graphics.transform(htmlToScreen);
		graphicsNode.paint(graphics);
	}

	/**
	 * Sets the transformation between HTML coordinates and screen coordinates. The transformation is applied to the
	 * coordinates in HTML space, this not the same as SVG user space ! First of all, the x and y attributes of the SVG
	 * element are ignored, so HTML coordinate space has the upper-left corner of the SVG element as origin and the
	 * width/height of the SVG element as measure of scale. The y-axis points downward. The viewbox has no influence on
	 * the HTML coordinate system but determines the transformation between user space and HTML space.
	 * 
	 * @param svgToScreen
	 */
	public void setHtmlToScreen(AffineTransform htmlToScreen) {
		this.htmlToScreen = htmlToScreen;
	}

	@Override
	public ReferencedEnvelope getBounds() {
		return null;
	}

	public GraphicsNode getGraphicsNode() {
		return graphicsNode;
	}

	public void setGraphicsNode(GraphicsNode graphicsNode) {
		this.graphicsNode = graphicsNode;
	}

}
