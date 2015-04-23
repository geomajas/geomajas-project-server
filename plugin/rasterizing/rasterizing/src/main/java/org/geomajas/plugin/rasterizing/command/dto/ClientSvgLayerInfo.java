/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.rasterizing.command.dto;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.geometry.Bbox;

/**
 * Metadata DTO class that represents a client layer with arbitrary svg content. This layer has no server-side
 * equivalent.
 * 
 * @author Jan De Moerloose
 * @since 1.3.0
 */
@Api(allMethods = true)
public class ClientSvgLayerInfo extends ClientLayerInfo {

	private static final long serialVersionUID = 130L;

	private String svgContent;

	private Bbox viewBoxWorldBounds;

	private Bbox viewBoxScreenBounds;

	// default showing
	private boolean showing = true;

	/**
	 * Get the showing status of this layer. If true, the layer will be rendered, if false not.
	 * 
	 * @return The showing status of this layer
	 */
	public boolean isShowing() {
		return showing;
	}

	/**
	 * Sets the showing status of this layer. If true, the layer will be rendered, if false not.
	 * 
	 * @param showing
	 *            showing status of this layer
	 */
	public void setShowing(boolean showing) {
		this.showing = showing;
	}

	/**
	 * Get the SVG content to render. Root element must be SVG tag.
	 * 
	 * @return the content
	 */
	public String getSvgContent() {
		return svgContent;
	}

	/**
	 * Set the SVG content to render.
	 * 
	 * @see #getSvgContent()
	 * @param svgContent
	 *            content
	 */
	public void setSvgContent(String svgContent) {
		this.svgContent = svgContent;
	}

	/**
	 * Get the world bounds corresponding to the view box of the SVG.
	 * 
	 * @return the world bounds
	 */
	public Bbox getViewBoxWorldBounds() {
		return viewBoxWorldBounds;
	}

	
	/**
	 * Set the world bounds corresponding to the view box of the SVG.
	 * @param viewBoxWorldBounds
	 */
	public void setViewBoxWorldBounds(Bbox viewBoxWorldBounds) {
		this.viewBoxWorldBounds = viewBoxWorldBounds;
	}

	
	/**
	 * Get the bounds of the view box of the SVG (world bounds converted to screen space).
	 * 
	 * @return the world bounds
	 */
	public Bbox getViewBoxScreenBounds() {
		return viewBoxScreenBounds;
	}

	
	/**
	 * Set the bounds of the view box of the SVG (world bounds converted to screen space).
	 * @param viewBoxWorldBounds
	 */
	public void setViewBoxScreenBounds(Bbox viewBoxScreenBounds) {
		this.viewBoxScreenBounds = viewBoxScreenBounds;
	}


}
