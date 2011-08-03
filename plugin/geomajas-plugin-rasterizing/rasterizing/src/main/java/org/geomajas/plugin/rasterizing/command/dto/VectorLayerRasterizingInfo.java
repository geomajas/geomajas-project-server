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
package org.geomajas.plugin.rasterizing.command.dto;

import java.util.Map;

import org.geomajas.annotations.Api;
import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.configuration.client.ClientUserDataInfo;
import org.geomajas.configuration.client.ClientWidgetInfo;

/**
 * Metadata DTO class that carries extra metadata information to render a vector layer.
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */
@Api(allMethods = true)
public class VectorLayerRasterizingInfo implements ClientWidgetInfo, RasterizingConstants {

	private static final long serialVersionUID = 100L;

	private NamedStyleInfo style;

	private FeatureStyleInfo selectionStyle;

	private String[] selectedFeatureIds;

	private Map<String, ClientUserDataInfo> userData;

	private String filter;

	// default paint labels
	private boolean paintLabels = true;

	// default paint geometries
	private boolean paintGeometries = true;

	// default showing
	private boolean showing = true;

	/**
	 * Returns the style to be applied to this layer.
	 * 
	 * @return the style to be applied to this layer
	 */
	public NamedStyleInfo getStyle() {
		return style;
	}

	/**
	 * Sets the style to be applied to this layer.
	 * 
	 * @param style
	 *            the style to be applied to this layer
	 */
	public void setStyle(NamedStyleInfo style) {
		this.style = style;
	}

	/**
	 * Returns the map of custom user data for this layer.
	 * 
	 * @return the map of custom user data
	 */
	public Map<String, ClientUserDataInfo> getUserData() {
		return userData;
	}

	/**
	 * Sets the map of custom user data for this layer.
	 * 
	 * @param userData
	 *            map of custom user data
	 */
	public void setUserData(Map<String, ClientUserDataInfo> userData) {
		this.userData = userData;
	}

	/**
	 * Returns the filter to be applied on the features before rendering.
	 * 
	 * @return filter to be applied
	 */
	public String getFilter() {
		return filter;
	}

	/**
	 * Sets the filter to be applied on the features before rendering.
	 * 
	 * @param filter
	 *            filter to be applied
	 */
	public void setFilter(String filter) {
		this.filter = filter;
	}

	/**
	 * returns true if feature labels have to be painted.
	 * 
	 * @return true if labels have to be painted, false otherwise.
	 */
	public boolean isPaintLabels() {
		return paintLabels;
	}

	/**
	 * Sets whether or not labels have to be painted.
	 * 
	 * @param paintLabels
	 *            true if labels have to be painted, false otherwise.
	 */
	public void setPaintLabels(boolean paintLabels) {
		this.paintLabels = paintLabels;
	}

	/**
	 * Sets whether or not geometries have to be painted.
	 * 
	 * @return true if geometries have to be painted, false otherwise.
	 */
	public boolean isPaintGeometries() {
		return paintGeometries;
	}

	/**
	 * Set to true to paint the geometries of this layer.
	 * 
	 * @param paintGeometries
	 *            true if geometries should be painted, false otherwise.
	 */
	public void setPaintGeometries(boolean paintGeometries) {
		this.paintGeometries = paintGeometries;
	}

	/**
	 * returns the selection style. This style has to be merged with the actual style.
	 * 
	 * @return the selection style
	 */
	public FeatureStyleInfo getSelectionStyle() {
		return selectionStyle;
	}

	/**
	 * Sets the selection style for this layer. This style has to be merged with the actual style.
	 * 
	 * @param selectionStyle
	 *            the selection style
	 */
	public void setSelectionStyle(FeatureStyleInfo selectionStyle) {
		this.selectionStyle = selectionStyle;
	}

	/**
	 * Returns the selected features of this layer.
	 * 
	 * @return array of feature ids
	 */
	public String[] getSelectedFeatureIds() {
		return selectedFeatureIds;
	}

	/**
	 * Sets the selected features of this layer.
	 * 
	 * @param selectedFeatureIds
	 *            array of feature ids
	 */
	public void setSelectedFeatureIds(String[] selectedFeatureIds) {
		this.selectedFeatureIds = selectedFeatureIds;
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

}
