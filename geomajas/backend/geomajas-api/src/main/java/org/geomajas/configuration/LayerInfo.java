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

import org.geomajas.geometry.Bbox;
import org.geomajas.layer.LayerType;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

/**
 * Layer configuration info.
 *
 * @author Joachim Van der Auwera
 */
public class LayerInfo implements Serializable {

	private static final long serialVersionUID = 151L;
	@NotNull
	private LayerType layerType;
	@NotNull
	private String crs;
	private Bbox maxExtent = Bbox.ALL;
	private int maxTileLevel;

	public LayerType getLayerType() {
		return layerType;
	}

	public void setLayerType(LayerType layerType) {
		this.layerType = layerType;
	}

	public String getCrs() {
		return crs;
	}

	public void setCrs(String crs) {
		this.crs = crs;
	}

	public Bbox getMaxExtent() {
		return maxExtent;
	}

	public void setMaxExtent(Bbox maxExtent) {
		this.maxExtent = maxExtent;
	}

	public int getMaxTileLevel() {
		return maxTileLevel;
	}

	public void setMaxTileLevel(int maxTileLevel) {
		this.maxTileLevel = maxTileLevel;
	}

}
