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
import javax.validation.constraints.Null;

import org.geomajas.configuration.LayerInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.layer.LayerType;

/**
 * Abstract class that contains the metadata properties that are common to all client layers. The coupling with the
 * server layer is established via the <code>layerInfo</code> field.
 * 
 * @author Jan De Moerloose
 */
public abstract class ClientLayerInfo implements Serializable {

	@NotNull
	private String label;

	private boolean visible;

	private double viewScaleMin;

	private double viewScaleMax = Double.MAX_VALUE;

	@Null
	private LayerInfo layerInfo;

	private Bbox maxExtent;

	private String id;

	@NotNull
	private String serverLayerId;

	/**
	 * Get the unique id of this layer.
	 * 
	 * @return the unique id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Set the unique id of this layer (auto-copied from Spring context).
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Get the layer id of the server layer.
	 * 
	 * @return layer id
	 */
	public String getServerLayerId() {
		return serverLayerId;
	}

	/**
	 * Set the server layer id. This should be the name of an existing {@link org.geomajas.layer.Layer} bean.
	 * 
	 * @param id
	 */
	public void setServerLayerId(String serverLayerId) {
		this.serverLayerId = serverLayerId;
	}

	/**
	 * Get the user label of this layer.
	 * 
	 * @return layer id
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Set the user label of this layer.
	 * 
	 * @param label
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Is this layer be visible at startup ?
	 * 
	 * @return true if visible
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * Set this layer visible at startup.
	 * 
	 * @param visible
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * Get the minimum scale for which this layer should be visible (maximum zoom out).
	 * 
	 * @return minimum scale (pix/map unit)
	 */
	public double getViewScaleMin() {
		return viewScaleMin;
	}

	/**
	 * Set the minimum scale for which this layer should be visible (maximum zoom out).
	 * 
	 * @param minimum
	 *            scale (pix/map unit)
	 */
	public void setViewScaleMin(double viewScaleMin) {
		this.viewScaleMin = viewScaleMin;
	}

	/**
	 * Get the maximum scale for which this layer should be visible (maximum zoom in).
	 * 
	 * @return maximum scale (pix/map unit)
	 */
	public double getViewScaleMax() {
		return viewScaleMax;
	}

	/**
	 * Set the maximum scale for which this layer should be visible (maximum zoom in).
	 * 
	 * @param maximum
	 *            scale (pix/map unit)
	 */
	public void setViewScaleMax(double viewScaleMax) {
		this.viewScaleMax = viewScaleMax;
	}

	/**
	 * Get the maximum visible extent of this layer (in map space).
	 * 
	 * @return maximum visible extent
	 */
	public Bbox getMaxExtent() {
		return maxExtent;
	}

	/**
	 * Set the maximum visible extent of this layer in map space (auto-set by Spring).
	 * 
	 * @param maximum
	 *            visible extent
	 */
	public void setMaxExtent(Bbox maxExtent) {
		this.maxExtent = maxExtent;
	}

	/**
	 * Get the layer information of this layer.
	 * 
	 * @return layer information
	 */
	public LayerInfo getLayerInfo() {
		return layerInfo;
	}

	/**
	 * Set the layer information of this layer (auto-copied from Spring context).
	 * 
	 * @param layer
	 *            information
	 */
	public void setLayerInfo(LayerInfo layerInfo) {
		this.layerInfo = layerInfo;
	}

	/**
	 * Get the type of this layer (same as server layer).
	 * 
	 * @return layer type
	 */
	public LayerType getLayerType() {
		return layerInfo.getLayerType();
	}

	/**
	 * Get the crs of this layer (same as server layer).
	 * 
	 * @return crs
	 */
	public String getCrs() {
		return layerInfo.getCrs();
	}

}
