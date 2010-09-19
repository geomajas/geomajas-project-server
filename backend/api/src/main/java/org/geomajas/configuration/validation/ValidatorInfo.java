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
package org.geomajas.configuration.validation;

import org.geomajas.global.Api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Validator configuration information.
 * 
 * @author Joachim Van der Auwera
 * @author Jan De Moerloose
 * @since 1.6.0
 */
@Api(allMethods = true)
public class ValidatorInfo implements Serializable {

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
