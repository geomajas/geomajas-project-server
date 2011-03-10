/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.rasterizing.dto;

import java.io.Serializable;

import org.geomajas.configuration.FontStyleInfo;
import org.geomajas.configuration.client.ClientMapInfo;

/**
 * Metadata DTO class that carries sufficient information to render a complete legend.
 * 
 * @author Jan De Moerloose
 * 
 */
public class LegendRasterizingInfo implements Serializable {

	private FontStyleInfo font;

	private String title;

	private ClientMapInfo mapInfo;

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
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Return the map for which this legend applies.
	 * 
	 * @return the map
	 */
	public ClientMapInfo getMapInfo() {
		return mapInfo;
	}

	/**
	 * Sets the map for which this legend applies.
	 * 
	 * @param mapInfo
	 *            the map
	 */
	public void setMapInfo(ClientMapInfo mapInfo) {
		this.mapInfo = mapInfo;
	}

}
