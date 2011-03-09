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

import java.util.Map;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.configuration.client.ClientUserDataInfo;
import org.geomajas.layer.LayerType;

/**
 * Metadata DTO class that carries sufficient information to render a vector layer.
 * 
 * @author Jan De Moerloose
 * 
 */
public class VectorLayerMetadata implements LayerMetadata {

	private String layerId;

	private LayerType layertype;

	private NamedStyleInfo style;

	private FeatureStyleInfo selectionStyle;

	private String[] selectedFeatureIds;

	private Map<String, ClientUserDataInfo> userData;

	private String filter;

	private boolean paintLabels = true;

	private boolean paintGeometries = true;

	public String getLayerId() {
		return layerId;
	}

	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}

	public LayerType getLayertype() {
		return layertype;
	}

	public void setLayertype(LayerType layertype) {
		this.layertype = layertype;
	}

	public NamedStyleInfo getStyle() {
		return style;
	}

	public void setStyle(NamedStyleInfo style) {
		this.style = style;
	}

	public Map<String, ClientUserDataInfo> getUserData() {
		return userData;
	}

	public void setUserData(Map<String, ClientUserDataInfo> userData) {
		this.userData = userData;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public boolean isPaintLabels() {
		return paintLabels;
	}

	public void setPaintLabels(boolean paintLabels) {
		this.paintLabels = paintLabels;
	}

	public boolean isPaintGeometries() {
		return paintGeometries;
	}

	public void setPaintGeometries(boolean paintGeometries) {
		this.paintGeometries = paintGeometries;
	}

	public FeatureStyleInfo getSelectionStyle() {
		return selectionStyle;
	}

	public void setSelectionStyle(FeatureStyleInfo selectionStyle) {
		this.selectionStyle = selectionStyle;
	}

	public String[] getSelectedFeatureIds() {
		return selectedFeatureIds;
	}

	public void setSelectedFeatureIds(String[] selectedFeatureIds) {
		this.selectedFeatureIds = selectedFeatureIds;
	}

}
