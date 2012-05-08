/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.printing.component.dto;

import java.io.Serializable;

import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.annotation.Api;

/**
 * DTO object for VectorLayerComponent.
 * 
 * @author Jan De Moerloose
 * @see org.geomajas.plugin.printing.component.VectorLayerComponent
 * @since 2.0.0
 *
 */
@Api(allMethods = true)
public class VectorLayerComponentInfo extends BaseLayerComponentInfo implements Serializable {

	private static final long serialVersionUID = 200L;

	/** Style for this layer. */
	private NamedStyleInfo styleInfo;

	/** CQL filter */
	private String filter;

	/** Array of selected feature id's for this layer. */
	private String[] selectedFeatureIds = new String[0];

	/** True if labels are visible. */
	private boolean labelsVisible;

	/**
	 * Get style info.
	 *
	 * @return style info
	 */
	public NamedStyleInfo getStyleInfo() {
		return styleInfo;
	}

	/**
	 * Set style info.
	 *
	 * @param styleInfo style info
	 */
	public void setStyleInfo(NamedStyleInfo styleInfo) {
		this.styleInfo = styleInfo;
	}

	/**
	 * Get filter.
	 *
	 * @return filter
	 */
	public String getFilter() {
		return filter;
	}

	/**
	 * Set filter.
	 *
	 * @param filter filter
	 */
	public void setFilter(String filter) {
		this.filter = filter;
	}

	/**
	 * Get selected feature ids.
	 *
	 * @return selected feature ids
	 */
	public String[] getSelectedFeatureIds() {
		return selectedFeatureIds;
	}

	/**
	 * Set ids of selected features.
	 *
	 * @param selectedFeatureIds ids of selected features
	 */
	public void setSelectedFeatureIds(String[] selectedFeatureIds) {
		this.selectedFeatureIds = selectedFeatureIds;
	}

	/**
	 * Should feature labels be displayed?
	 *
	 * @return feature labels visible?
	 */
	public boolean isLabelsVisible() {
		return labelsVisible;
	}

	/**
	 * Set whether feature labels should be displayed.
	 *
	 * @param labelsVisible labels visible?
	 */
	public void setLabelsVisible(boolean labelsVisible) {
		this.labelsVisible = labelsVisible;
	}

}
