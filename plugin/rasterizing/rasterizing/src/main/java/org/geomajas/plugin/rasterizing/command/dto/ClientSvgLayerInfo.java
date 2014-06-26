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

	private int width;

	private int height;

	private Bbox bounds;

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
	 * @param showing showing status of this layer
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
	 * @param svgContent content
	 */
	public void setSvgContent(String svgContent) {
		this.svgContent = svgContent;
	}

	/**
	 * Get the width in pixels of the SVG element.
	 * 
	 * @return
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Set the width in pixels of the SVG element.
	 * 
	 * @param width
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * Get the height in pixels of the SVG element.
	 * 
	 * @return height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Set the height in pixels of the SVG element.
	 * 
	 * @param height
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * Get the bounds of the SVG element in world space.
	 * 
	 * @return the bounds
	 */
	public Bbox getBounds() {
		return bounds;
	}

	/**
	 * Set the bounds of the SVG element in world space.
	 * 
	 * @param bounds
	 */
	public void setBounds(Bbox bounds) {
		this.bounds = bounds;
	}

}
