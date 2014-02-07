/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.rasterizing.command.dto;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.geometry.Geometry;
import org.geomajas.layer.LayerType;
import org.geomajas.sld.UserStyleInfo;

/**
 * Metadata DTO class that represents a client layer with arbitrary geometries in world space. This layer has no
 * server-side equivalent.
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@Api(allMethods = true)
public class ClientGeometryLayerInfo extends ClientLayerInfo {

	private static final long serialVersionUID = 100L;

	private List<Geometry> geometries = new ArrayList<Geometry>();

	private UserStyleInfo style;

	// default showing
	private boolean showing = true;
	
	private LayerType layerType;

	/**
	 * Returns the style of the geometries.
	 * 
	 * @return the feature style
	 */
	public UserStyleInfo getStyle() {
		return style;
	}

	/**
	 * Sets the style of the geometries.
	 * 
	 * @param style
	 *            the new feature style to apply.
	 */
	public void setStyle(UserStyleInfo style) {
		this.style = style;
	}

	/**
	 * Returns the list of geometries to be rendered.
	 * 
	 * @return the list of geometries
	 */
	public List<Geometry> getGeometries() {
		return geometries;
	}

	/**
	 * Sets the list of geometries to be rendered.
	 * 
	 * @param geometries
	 *            the list of geometries
	 */
	public void setGeometries(List<Geometry> geometries) {
		this.geometries = geometries;
	}

	/**
	 * Get the showing status of this layer. If true, the layer will be rendered, if false not.
	 * 
	 * @return The showing status of this layer
	 */
	public boolean isShowing() {
		return showing;
	}

	/**
	 * Sets the showing status of this layer. If true, the layer will be rendered, if false not.
	 * 
	 * @param showing
	 *            showing status of this layer
	 */
	public void setShowing(boolean showing) {
		this.showing = showing;
	}
	
	/**
	 * Set the layer type of this layer's geometries.
	 * 
	 * @param layerType layer type
	 */
	public void setLayerType(LayerType layerType) {
		this.layerType = layerType;
	}

	/**
	 * Get the type of this layer's geometries.
	 * 
	 * @return layer type
	 */
	public LayerType getLayerType() {
		return layerType;
	}

}
