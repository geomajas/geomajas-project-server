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
package org.geomajas.configuration.client;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.geomajas.configuration.LayerInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.layer.LayerType;

/**
 * Client side layer info.
 * 
 * @author Jan De Moerloose s
 */
public class ClientLayerInfo implements Serializable {

	@NotNull
	private String label;

	private boolean visible;

	private double viewScaleMin;

	private double viewScaleMax = Double.MAX_VALUE;

	@NotNull
	private LayerInfo layerInfo;
	
	private Bbox maxExtent;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public double getViewScaleMin() {
		return viewScaleMin;
	}

	public void setViewScaleMin(double viewScaleMin) {
		this.viewScaleMin = viewScaleMin;
	}

	public double getViewScaleMax() {
		return viewScaleMax;
	}

	public void setViewScaleMax(double viewScaleMax) {
		this.viewScaleMax = viewScaleMax;
	}
	
	public Bbox getMaxExtent() {
		return maxExtent;
	}
	
	public void setMaxExtent(Bbox maxExtent) {
		this.maxExtent = maxExtent;
	}

	public LayerInfo getLayerInfo() {
		return layerInfo;
	}

	public void setLayerInfo(LayerInfo layerInfo) {
		this.layerInfo = layerInfo;
	}

	public String getId() {
		return layerInfo.getId();
	}

	public LayerType getLayerType() {
		return layerInfo.getLayerType();
	}

	public String getCrs() {
		return layerInfo.getCrs();
	}

	public int getMaxTileLevel() {
		return layerInfo.getMaxTileLevel();
	}


}
