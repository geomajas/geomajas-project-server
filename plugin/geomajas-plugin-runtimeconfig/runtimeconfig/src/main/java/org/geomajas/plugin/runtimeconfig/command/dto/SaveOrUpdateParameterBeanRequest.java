/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.runtimeconfig.command.dto;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.command.CommandRequest;
import org.geomajas.configuration.IsInfo;
import org.geomajas.plugin.runtimeconfig.dto.parameter.ListParameterDto;
import org.geomajas.plugin.runtimeconfig.dto.parameter.ObjectParameterDto;
import org.geomajas.plugin.runtimeconfig.dto.parameter.ParameterDto;
import org.geomajas.plugin.runtimeconfig.dto.parameter.StringParameterDto;

/**
 * Request object for {@link org.geomajas.configurator.command.configurator.SaveOrUpdateBeanConfigurationCommand}.
 * 
 * @author Jan De Moerloose
 */
public class SaveOrUpdateParameterBeanRequest implements CommandRequest {

	public static final  String COMMAND = "runtimeconfig.SaveOrUpdateParameterBean";

	private static final long serialVersionUID = 100L;

	private List<ParameterDto> parameters = new ArrayList<ParameterDto>();

	public List<ParameterDto> getParameters() {
		return parameters;
	}
	
	public void setParameters(List<ParameterDto> parameters) {
		this.parameters = parameters;
	}

	public void addStringParameter(String name, String value) {
		parameters.add(new StringParameterDto(name, value));
	}
	
	public void addObjectParameter(String name, IsInfo value) {
		parameters.add(new ObjectParameterDto(name, value));
	}

	public void addListParameter(String name, List<? extends IsInfo> value) {
		parameters.add(new ListParameterDto(name, value));
	}

	public void addParameter(ParameterDto<?> parameter) {
		parameters.add(parameter);
	}

}