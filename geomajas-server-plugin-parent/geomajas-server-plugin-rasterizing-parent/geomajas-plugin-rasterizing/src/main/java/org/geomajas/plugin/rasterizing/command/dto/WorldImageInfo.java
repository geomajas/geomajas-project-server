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

import org.geomajas.geometry.Bbox;

/**
 * Metadata DTO class that carries extra metadata information about a world image.
 * 
 * @author Jan De Moerloose
 * @since 1.2.0
 */
public class WorldImageInfo extends WorldPaintableInfo {

	private static final long serialVersionUID = 120L;

	private Bbox bbox;

	private String url;

	public Bbox getBbox() {
		return bbox;
	}

	public void setBbox(Bbox bbox) {
		this.bbox = bbox;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
