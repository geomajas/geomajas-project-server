/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.rasterizing.layer;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;

import org.apache.batik.gvt.GraphicsNode;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.DirectLayer;
import org.geotools.map.MapContent;
import org.geotools.map.MapViewport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Geotools layer responsible for rendering svg (defined in world space).
 * 
 * @author Jan De Moerloose
 * 
 */
public class SvgDirectLayer extends DirectLayer {

	private GraphicsNode graphicsNode;

	private ReferencedEnvelope svgWorldBounds;

	private Rectangle svgScreenBounds;

	private MapContent mapContent;

	private final Logger log = LoggerFactory.getLogger(SvgDirectLayer.class);

	public SvgDirectLayer(MapContent mapContent) {
		this.mapContent = mapContent;
	}

	@Override
	public void draw(Graphics2D graphics, MapContent map, MapViewport viewport) {
		try {
			AffineTransform svgToScreen = getSvgToScreen();
			graphics.transform(svgToScreen);
		} catch (NoninvertibleTransformException e) {
			log.error("Could not draw svg layer");
		}
		graphicsNode.paint(graphics);
	}

	private AffineTransform getSvgToScreen() throws NoninvertibleTransformException {
		AffineTransform svgToWorld = getSvgToWorld();
		AffineTransform worldToScreen = (AffineTransform) mapContent.getViewport().getWorldToScreen().clone();
		worldToScreen.concatenate(svgToWorld);
		return worldToScreen;
	}

	private AffineTransform getSvgToWorld() {
		MapContent m = new MapContent();
		m.getViewport().setBounds(svgWorldBounds);
		m.getViewport().setScreenArea(svgScreenBounds);
		return m.getViewport().getScreenToWorld();
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

	public ReferencedEnvelope getSvgWorldBounds() {
		return svgWorldBounds;
	}

	public void setSvgWorldBounds(ReferencedEnvelope svgWorldBounds) {
		this.svgWorldBounds = svgWorldBounds;
	}

	public Rectangle getSvgScreenBounds() {
		return svgScreenBounds;
	}

	public void setSvgScreenBounds(Rectangle svgScreenBounds) {
		this.svgScreenBounds = svgScreenBounds;
	}

}
