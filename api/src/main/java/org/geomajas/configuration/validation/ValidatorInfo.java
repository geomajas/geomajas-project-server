/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.configuration.validation;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.IsInfo;

/**
 * Validator configuration information.
 * 
 * @author Joachim Van der Auwera
 * @author Jan De Moerloose
 * @since 1.6.0
 */
@Api(allMethods = true)
public class ValidatorInfo implements IsInfo {

	private static final long serialVersionUID = 151L;

	private List<ConstraintInfo> constraints = new ArrayList<ConstraintInfo>();

	private String toolTip;

	private String errorMessage;

	/**
	 * Get constraints which need to be validated.
	 *
	 * @return contraints
	 */
	public List<ConstraintInfo> getConstraints() {
		return constraints;
	}

	/**
	 * Set constraints which need to be validated.
	 *
	 * @param constraints constraints
	 */
	public void setConstraints(List<ConstraintInfo> constraints) {
		this.constraints = constraints;
	}

	/**
	 * Get tooltip for attribute.
	 *
	 * @return tooltip
	 */
	public String getToolTip() {
		return toolTip;
	}

	/**
	 * Set tooltip for attribute.
	 *
	 * @param toolTip tooltip text
	 */
	public void setToolTip(String toolTip) {
		this.toolTip = toolTip;
	}

	/**
	 * Get message to display when validation failed.
	 *
	 * @return error message
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * Set message to display when validation failed.
	 *
	 * @param errorMessage error message
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
