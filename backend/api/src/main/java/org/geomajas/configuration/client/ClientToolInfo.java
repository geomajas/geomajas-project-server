/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.configuration.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.Parameter;

/**
 * Representation of a tool.
 * 
 * @author Joachim Van der Auwera
 * @since 1.6.0
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
