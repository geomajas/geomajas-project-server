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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.geomajas.configuration.Parameter;
import org.geomajas.global.Api;

/**
 * Representation of a tool.
 * 
 * @author Joachim Van der Auwera
 */
@Api(allMethods = true)
public class ClientToolInfo implements Serializable {

	private static final long serialVersionUID = 151L;

	@NotNull
	private String id;

	private List<Parameter> parameters = new ArrayList<Parameter>();

	/**
	 * Get the list of parameters for this tool.
	 * 
	 * @return list of {@link Parameter}
	 */
	public List<Parameter> getParameters() {
		return parameters;
	}

	/**
	 * Set the list of parameters for this tool.
	 * 
	 * @param value
	 *            list of {@link Parameter}
	 */
	public void setParameters(List<Parameter> value) {
		parameters = value;
	}

	/**
	 * Get the tool id.
	 * 
	 * @return tool id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Set the tool id.
	 * 
	 * @param value
	 *            tool id
	 */
	public void setId(String value) {
		this.id = value;
	}

}
