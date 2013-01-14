/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.configuration.client;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.FeatureInfo;
import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.configuration.SnappingRuleInfo;

/**
 * Client side vector layer metadata.
 * 
 * @author Jan De Moerloose
 * @since 1.6.0
 */
@Api(allMethods = true)
public class ClientVectorLayerInfo extends ClientLayerInfo {

	private static final long serialVersionUID = 160L;

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
