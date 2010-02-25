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

package org.geomajas.layer.tile;

import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.geometry.Coordinate;

/**
 * <p>
 * The collected meta-data that uniquely define a tile's rendering.
 * </p>
 *
 * @author Pieter De Graef
 */
public interface TileMetadata {

	String PARAM_SVG_RENDERER = "SVG";

	String PARAM_VML_RENDERER = "VML";

	/**
	 * Get layer id.
	 *
	 * @return layer id
	 */
	String getLayerId();

	/**
	 * Set layer id.
	 *
	 * @param layerId layer id
	 */
	void setLayerId(String layerId);

	/**
	 * Get crs code.
	 *
	 * @return crs
	 */
	String getCrs();

	/**
	 * Set crs code.
	 *
	 * @param crs crs
	 */
	void setCrs(String crs);

	TileCode getCode();

	void setCode(TileCode code);

	double getScale();

	void setScale(double scale);

	Coordinate getPanOrigin();

	void setPanOrigin(Coordinate panOrigin);

	String getRenderer();

	void setRenderer(String renderer);

	String getFilter();

	void setFilter(String filter);

	NamedStyleInfo getStyleInfo();

	void setStyleInfo(NamedStyleInfo styleInfo);

	boolean isPaintGeometries();

	void setPaintGeometries(boolean paintGeometries);

	boolean isPaintLabels();

	void setPaintLabels(boolean paintLabels);

}
