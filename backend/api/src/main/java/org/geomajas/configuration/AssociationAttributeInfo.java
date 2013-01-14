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
package org.geomajas.configuration;

import javax.validation.constraints.NotNull;

import org.geomajas.annotation.Api;

/**
 * Linked attribute configuration information.
 * 
 * @author Jan De Moerloose
 * @since 1.6.0
 */
@Api(allMethods = true)
public class AssociationAttributeInfo extends AbstractEditableAttributeInfo implements IsInfo {

	private static final long serialVersionUID = 151L;

	@NotNull
	private AssociationType type;

	@NotNull
	private FeatureInfo feature;

	/**
	 * returns the type of the association (many-to-one, one-to-many).
	 * 
	 * @return association type
	 */
	public AssociationType getType() {
		return type;
	}

	/**
	 * Set the type of the association (many-to-one, one-to-many).
	 *
	 * @param type association type
	 */
	public void setType(AssociationType type) {
		this.type = type;
	}

	/**
	 * Get the feature information of this attribute (represented as feature).
	 * 
	 * @return feature information
	 */
	public FeatureInfo getFeature() {
		return feature;
	}

	/**
	 * Set the feature information.
	 *
	 * @param featureInfo feature info
	 */
	public void setFeature(FeatureInfo featureInfo) {
		this.feature = featureInfo;
	}
}
