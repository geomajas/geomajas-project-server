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
import java.util.ArrayList;
import java.util.List;

/**
 * Information about a vector layer.
 * 
 * @author Joachim Van der Auwera
 */
public class VectorLayerInfo extends LayerInfo implements Serializable, Cloneable {

	private static final long serialVersionUID = 151L;

	private LabelAttributeInfo labelAttribute;

	private String filter;

	// @todo should be removed, should be handled either through testing of capabilities (interfaces?) or policies
	private EditPermissionType editPermissions;

	private FeatureInfo featureInfo;

	private List<SnappingRuleInfo> snappingRules;

	private List<StyleInfo> styleDefinitions;

	public LabelAttributeInfo getLabelAttribute() {
		return labelAttribute;
	}

	public void setLabelAttribute(LabelAttributeInfo label) {
		this.labelAttribute = label;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public EditPermissionType getEditPermissions() {
		return editPermissions;
	}

	public void setEditPermissions(EditPermissionType editPermissions) {
		this.editPermissions = editPermissions;
	}

	public FeatureInfo getFeatureInfo() {
		return featureInfo;
	}

	public void setFeatureInfo(FeatureInfo featureInfo) {
		this.featureInfo = featureInfo;
	}

	public List<SnappingRuleInfo> getSnappingRules() {
		if (null == snappingRules) {
			snappingRules = new ArrayList<SnappingRuleInfo>();
		}
		return snappingRules;
	}

	public void setSnappingRules(List<SnappingRuleInfo> snappingRules) {
		this.snappingRules = snappingRules;
	}

	public List<StyleInfo> getStyleDefinitions() {
		if (null == styleDefinitions) {
			styleDefinitions = new ArrayList<StyleInfo>();
		}
		return styleDefinitions;
	}

	public void setStyleDefinitions(List<StyleInfo> styleDefinitions) {
		this.styleDefinitions = styleDefinitions;
	}

	public VectorLayerInfo clone() {
		/*
		 * this is what the method should look like, but GWT cannot handle this (grmbl) return (VectorLayerInfo)
		 * super.clone();
		 */
		VectorLayerInfo res = (VectorLayerInfo) super.clone(new VectorLayerInfo());
		res.setLabelAttribute(labelAttribute);
		res.setFilter(filter);
		res.setEditPermissions(editPermissions);
		res.setFeatureInfo(featureInfo);
		res.setSnappingRules(snappingRules);
		res.setStyleDefinitions(styleDefinitions);
		return res;
	}

}
