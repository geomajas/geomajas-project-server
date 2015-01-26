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
package org.geomajas.plugin.runtimeconfig.dto.parameter;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.configuration.IsInfo;

/**
 * Bean factory parameter that wraps a list of {@link IsInfo} objects.
 * 
 * @author Jan De Moerloose
 * 
 */
public class ListParameterDto implements ParameterDto<List<IsInfo>> {

	private List<IsInfo> value = new ArrayList<IsInfo>();

	private String name;

	public ListParameterDto() {

	}

	public ListParameterDto(String name, List<? extends IsInfo> value) {
		this.name = name;
		this.value.addAll(value);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setValue(List<IsInfo> value) {
		this.value = value;
	}

	public List<IsInfo> getValue() {
		return value;
	}

	public void add(IsInfo parameter) {
		value.add(parameter);
	}

}