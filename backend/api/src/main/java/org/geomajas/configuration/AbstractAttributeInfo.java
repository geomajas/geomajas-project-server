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

import org.geomajas.annotation.Api;

/**
 * Base information which is shared between all attributes.
 *
 * @author Joachim Van der Auwera
 * @since 1.10.0 split out of AttributeBaseInfo, fixing the naming and removing the editable property
 */
@Api(allMethods = true)
public abstract class AbstractAttributeInfo implements IsInfo {

	private static final long serialVersionUID = 1100L;
	private String name;

	/** No-arguments constructor needed for GWT.*/
	public AbstractAttributeInfo() {
		// NOSONAR do nothing
	}

	/**
	 * Get name of the attribute.
	 * <p/>
	 * This is also the name which is used to identify the attribute at the data level and
	 * is assumed to be unique across the attributes for the feature.
	 * <p/>
	 * It depends on the layer whether an attribute name is case dependent or independent.
	 *
	 * @return attribute name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of the attribute.
	 * <p/>
	 * This is also the name which is used to identify the attribute at the data level and
	 * is assumed to be unique across the attributes for the feature.
	 * <p/>
	 * It depends on the layer whether an attribute name is case dependent or independent.
	 *
	 * @param name attribute name
	 */
	public void setName(String name) {
		this.name = name;
	}

}