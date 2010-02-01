/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.extension.command.dto;

import org.geomajas.configuration.StyleInfo;
import org.geomajas.geometry.Coordinate;
import org.geomajas.layer.tile.TileCode;
import org.geomajas.layer.tile.TileMetadata;

/**
 * Request object for {@link org.geomajas.extension.command.render.GetRenderedTileCommand}.
 *
 * @author Joachim Van der Auwera
 */
public class GetRenderedTileRequest extends LayerIdCommandRequest implements TileMetadata {

	private static final long serialVersionUID = 151L;

	private TileCode code;

	private double scale;

	private Coordinate panOrigin;

	private String filter;

	private String crs;

	private String renderer;

	private StyleInfo[] styleDefs;

	private boolean paintGeometries;

	private boolean paintLabels;

	public TileCode getCode() {
		return code;
	}

	public void setCode(TileCode code) {
		this.code = code;
	}

	public double getScale() {
		return scale;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}

	public Coordinate getPanOrigin() {
		return panOrigin;
	}

	public void setPanOrigin(Coordinate panOrigin) {
		this.panOrigin = panOrigin;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	/**
	 * Get the coordinate reference space which should be used for the returned geometries.
	 *
	 * @return crs
	 */
	public String getCrs() {
		return crs;
	}

	/**
	 * Set the coordinate reference space which should be used for the returned geometries.
	 *
	 * @param crs crs
	 */
	public void setCrs(String crs) {
		this.crs = crs;
	}

	public String getRenderer() {
		return renderer;
	}

	public void setRenderer(String renderer) {
		this.renderer = renderer;
	}

	public StyleInfo[] getStyleDefs() {
		return styleDefs;
	}

	public void setStyleDefs(StyleInfo[] styleDefs) {
		this.styleDefs = styleDefs;
	}

	public boolean isPaintGeometries() {
		return paintGeometries;
	}

	public void setPaintGeometries(boolean paintGeometries) {
		this.paintGeometries = paintGeometries;
	}

	public boolean isPaintLabels() {
		return paintLabels;
	}

	public void setPaintLabels(boolean paintLabels) {
		this.paintLabels = paintLabels;
	}
}
