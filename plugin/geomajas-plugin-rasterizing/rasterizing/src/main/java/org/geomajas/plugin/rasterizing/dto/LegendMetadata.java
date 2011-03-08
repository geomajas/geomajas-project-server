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

import org.geomajas.configuration.FontStyleInfo;

/**
 * Metadata DTO class that carries sufficient information to render a complete legend.
 * 
 * @author Jan De Moerloose
 * 
 */
public class LegendMetadata {

	private FontStyleInfo font;

	private String title;

	private MapMetadata mapMetadata;

	public FontStyleInfo getFont() {
		return font;
	}

	public void setFont(FontStyleInfo font) {
		this.font = font;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public MapMetadata getMapMetadata() {
		return mapMetadata;
	}

	public void setMapMetadata(MapMetadata mapMetadata) {
		this.mapMetadata = mapMetadata;
	}

}
