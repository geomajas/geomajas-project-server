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
package org.geomajas.configuration;

import javax.validation.constraints.NotNull;

import org.geomajas.annotation.Api;

/**
 * Representation of a generic parameter for the configuration.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class Parameter implements IsInfo {

	private static final long serialVersionUID = 151L;
	@NotNull
	private String name;
	@NotNull
	private String value;
	
	/**
	 * Construct a parameter with null name and value.
	 */
	public Parameter() {
	}

	/**
	 * Construct a parameter with the specified name and value.
	 * 
	 * @param name the name
	 * @param value the value
	 * @since 1.11.0
	 */
	public Parameter(String name, String value) {
		this.name = name;
		this.value = value;
	}

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
