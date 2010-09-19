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
package org.geomajas.configuration;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.geomajas.geometry.Bbox;
import org.geomajas.global.Api;
import org.geomajas.layer.LayerType;

/**
 * Layer configuration info.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class LayerInfo implements Serializable {

	private static final long serialVersionUID = 151L;
	@NotNull
	private LayerType layerType;
	@NotNull
	private String crs;
	private Bbox maxExtent = Bbox.ALL;

	/**
	 * Get layer type, indicating whether it is a raster layer or which type of geometries which are contained in the
	 * layer if it is a vector layer.
	 *
	 * @return layer type
	 */
	public LayerType getLayerType() {
		return layerType;
	}

	/**
	 * Set layer type. This indicates either that it is a raster layer, or the type of geometry when it is a vector
	 * layer.
	 *
	 * @param layerType layer type
	 */
	public void setLayerType(LayerType layerType) {
		this.layerType = layerType;
	}

	/**
	 * Get the CRS code which is used for expressing the coordinated in this layer.
	 *
	 * @return CRS code for this layer
	 */
	public String getCrs() {
		return crs;
	}

	/**
	 * Set the CRS code which is used for expressing the coordinated in this layer.
	 *
	 * @param crs CRS code for this layer
	 */
	public void setCrs(String crs) {
		this.crs = crs;
	}

	/**
	 * Get the maximum extent for the layer, the bounding box for the visible/usable area for this layer.
	 * <p/>
	 * Note that this may need to be limited some more based on security configuration.
	 *
	 * @return maximum extent for layer
	 */
	public Bbox getMaxExtent() {
		return maxExtent;
	}

	/**
	 * Set the maximum extent for the layer, the bounding box for the visible/usable area for this layer.
	 * <p/>
	 * Note that this is the largest area for any user. It may be limited based on logged in user.
	 *
	 * @param maxExtent maximum extent for layer
	 */
	public void setMaxExtent(Bbox maxExtent) {
		this.maxExtent = maxExtent;
	}

}
