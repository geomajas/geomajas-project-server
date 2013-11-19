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

	private int viewBoxWidth;

	private int viewBoxHeight;

	private Bbox viewBoxBounds;

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
	 * Get the view box width of the SVG.
	 * 
	 * @return width in pixels
	 */
	public int getViewBoxWidth() {
		return viewBoxWidth;
	}

	/**
	 * Set the viewbox width.
	 * 
	 * @see #getViewBoxWidth()
	 * @param viewBoxWidth
	 */
	public void setViewBoxWidth(int viewBoxWidth) {
		this.viewBoxWidth = viewBoxWidth;
	}

	/**
	 * Get the view box height of the SVG.
	 * 
	 * @return height in pixels
	 */
	public int getViewBoxHeight() {
		return viewBoxHeight;
	}

	/**
	 * Set the viewbox height.
	 * 
	 * @see #getViewBoxHeight()
	 * @param viewBoxHeight
	 */
	public void setViewBoxHeight(int viewBoxHeight) {
		this.viewBoxHeight = viewBoxHeight;
	}

	/**
	 * Get the bounding box of the view box in world coordinates.
	 * 
	 * @return the bounding box.
	 */
	public Bbox getViewBoxBounds() {
		return viewBoxBounds;
	}

	/**
	 * Set the bounding box of the view box in world coordinates.
	 * 
	 * @see #getViewBoxBounds()
	 * @param viewBoxBounds
	 */
	public void setViewBoxBounds(Bbox viewBoxBounds) {
		this.viewBoxBounds = viewBoxBounds;
	}

}
