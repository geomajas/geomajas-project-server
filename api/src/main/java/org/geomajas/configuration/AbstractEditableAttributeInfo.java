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

package org.geomajas.configuration;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.validation.ValidatorInfo;

/**
 * Base information which is shared between all editable attributes.
 *
 * @author Joachim Van der Auwera
 * @since 1.10.0 split out of AttributeInfo
 */
@Api(allMethods = true)
public abstract class AbstractEditableAttributeInfo extends AbstractReadOnlyAttributeInfo
		implements EditableAttributeInfo {

	private static final long serialVersionUID = 1100L;

	private ValidatorInfo validator = new ValidatorInfo();
	private boolean editable;

	/** No-arguments constructor needed for GWT. */
	public AbstractEditableAttributeInfo() {
		super();
	}

	/**
	 * Get validator for attribute.
	 *
	 * @return validator
	 */
	public ValidatorInfo getValidator() {
		return validator;
	}

	/**
	 * Set validator for attribute.
	 *
	 * @param validator validator
	 */
	public void setValidator(ValidatorInfo validator) {
		this.validator = validator;
	}

	/**
	 * Indicates whether the layer is capable of of storing values at the data level.
	 * <p/>
	 * It does not give any indication of whether the logged user is allowed to edit the attribute, for that
	 * purpose use the {@link org.geomajas.security.SecurityContext}.
	 *
	 * @return does the layer have to capability of writing this attribute?
	 */
	public boolean isEditable() {
		return editable;
	}

	/**
	 * Set whether the layer has the capability of writing values for this attribute at the data level.
	 * <p/>
	 * It does not give any indication of whether the logged user is allowed to edit the attribute, for that
	 * purpose use the {@link org.geomajas.security.SecurityContext}.
	 *
	 * @param editable "editable" capability for attribute
	 */
	public void setEditable(boolean editable) {
		this.editable = editable;
	}

}