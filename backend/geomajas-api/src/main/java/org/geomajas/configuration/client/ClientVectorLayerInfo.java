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

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.geomajas.configuration.FeatureInfo;
import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.configuration.SnappingRuleInfo;
import org.geomajas.global.Api;

/**
 * Client side vector layer metadata.
 * 
 * @author Jan De Moerloose
 */
@Api(allMethods = true)
public class ClientVectorLayerInfo extends ClientLayerInfo {

	@NotNull
	private NamedStyleInfo namedStyleInfo;

	private boolean creatable;

	private boolean updatable;

	private boolean deletable;

	private List<SnappingRuleInfo> snappingRules = new ArrayList<SnappingRuleInfo>();

	private FeatureInfo featureInfo;

	/**
	 * Returns the style used by this layer. If null, the first server-defined style will be used, if only the name of
	 * the style is defined, the server-defined style with the same name will be used.
	 * 
	 * @return the style info
	 */
	public NamedStyleInfo getNamedStyleInfo() {
		return namedStyleInfo;
	}

	/**
	 * Sets the style used by this layer. If null, the first server-defined style will be used, if only the name of the
	 * style is defined, the server-defined style with the same name will be used.
	 * 
	 * @param namedStyleInfo
	 *            the style info
	 */
	public void setNamedStyleInfo(NamedStyleInfo namedStyleInfo) {
		this.namedStyleInfo = namedStyleInfo;
	}

	/**
	 * Returns the feature info for this layer.
	 * 
	 * @return the feature info for this layer
	 */
	public FeatureInfo getFeatureInfo() {
		return featureInfo;
	}

	/**
	 * Sets the feature info for this layer. This property is auto-set by Spring based on server layer info. It should
	 * only be set if the user wants to override the feature info as defined in the server layer info. This could be the
	 * case if one wants less attributes or different labels for the attributes or a different set of constraints,
	 * etc... Attention : the code does not currently validate if such overriding is compatible with the server layer !
	 * 
	 * @param featureInfo feature info
	 */
	public void setFeatureInfo(FeatureInfo featureInfo) {
		this.featureInfo = featureInfo;
	}

	/**
	 * Returns the snapping rules for this layer.
	 * 
	 * @return the snapping rules for this layer
	 */
	public List<SnappingRuleInfo> getSnappingRules() {
		return snappingRules;
	}

	/**
	 * Sets the snapping rules for this layer.
	 * @param snappingRules the snapping rules for this layer
	 */
	public void setSnappingRules(List<SnappingRuleInfo> snappingRules) {
		this.snappingRules = snappingRules;
	}

	/**
	 * Is the logged in user allowed to create new features?
	 * 
	 * @return true when creating new features is allowed
	 */
	public boolean isCreatable() {
		return creatable;
	}

	/**
	 * Set whether the logged in user is allowed to create new features.
	 * <p/>
	 * This should not be set in configuration, it is set in the GetConfigurationCommand based on security settings.
	 * 
	 * @param creatable
	 *            true when creating new features is allowed
	 */
	public void setCreatable(boolean creatable) {
		this.creatable = creatable;
	}

	/**
	 * Is the logged in user allowed to edit some features?
	 * 
	 * @return true when update is allowed for some features
	 */
	public boolean isUpdatable() {
		return updatable;
	}

	/**
	 * Set whether the logged in user is allowed to edit/update some features.
	 * <p/>
	 * This should not be set in configuration, it is set in the GetConfigurationCommand based on security settings.
	 * 
	 * @param editable
	 *            true when edit/update is allowed for some features
	 */
	public void setUpdatable(boolean editable) {
		this.updatable = editable;
	}

	/**
	 * Is the logged in user allowed to delete (some) features?
	 * 
	 * @return true when delete is allowed
	 */
	public boolean isDeletable() {
		return deletable;
	}

	/**
	 * Set whether the logged in user is allowed to delete (some) features.
	 * <p/>
	 * This should not be set in configuration, it is set in the GetConfigurationCommand based on security settings.
	 * 
	 * @param deletable
	 *            true when deleting is allowed
	 */
	public void setDeletable(boolean deletable) {
		this.deletable = deletable;
	}

}
