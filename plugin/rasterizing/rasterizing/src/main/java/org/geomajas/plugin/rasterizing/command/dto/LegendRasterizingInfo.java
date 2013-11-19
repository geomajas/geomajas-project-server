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

import java.io.Serializable;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.FontStyleInfo;

/**
 * Metadata DTO class that carries sufficient information to render a complete legend.
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@Api(allMethods = true)
public class LegendRasterizingInfo implements Serializable {

	private static final long serialVersionUID = 100L;

	private FontStyleInfo font;

	private String title;

	private int width = -1;

	private int height = -1;

	/**
	 * Returns the font style of this legend.
	 * 
	 * @return the font style
	 */
	public FontStyleInfo getFont() {
		return font;
	}

	/**
	 * Sets the font style of this legend.
	 * 
	 * @param font
	 *            the font style
	 */
	public void setFont(FontStyleInfo font) {
		this.font = font;
	}

	/**
	 * Returns the title of this legend.
	 * 
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title of this legend.
	 * 
	 * @param title legend title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	
	/**
	 * Returns the width of this legend.
	 * 
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	
	/**
	 * Sets the width of this legend.
	 * 
	 * @param width legend width
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	
	/**
	 * Returns the height of this legend.
	 * 
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	
	/**
	 * Sets the height of this legend.
	 * 
	 * @param height legend height
	 */
	public void setHeight(int height) {
		this.height = height;
	}
	
}
