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

import org.geomajas.global.Api;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

/**
 * Representation of a generic parameter for the configuration.
 *
 * @author Joachim Van der Auwera
 */
@Api(allMethods = true)
public class Parameter implements Serializable {

	private static final long serialVersionUID = 151L;
	@NotNull
	private String name;
	@NotNull
	private String value;

	/**
	 * Get the parameter name.
	 *
	 * @return parameter name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the parameter's name.
	 *
	 * @param name parameter name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the parameter value.
	 *
	 * @return parameter value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Set the parameter value.
	 *
	 * @param value parameter value
	 */
	public void setValue(String value) {
		this.value = value;
	}

}
