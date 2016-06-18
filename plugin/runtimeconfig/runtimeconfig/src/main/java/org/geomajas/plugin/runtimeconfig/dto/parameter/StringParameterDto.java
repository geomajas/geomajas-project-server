/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.runtimeconfig.dto.parameter;


/**
 * Bean factory parameter that wraps a {@link String} object.
 * 
 * @author Jan De Moerloose
 * 
 */
public class StringParameterDto implements ParameterDto<String> {

	private String value;

	private String name;

	public StringParameterDto() {
	}

	public StringParameterDto(String name, String value) {
		this.value = value;
		this.name = name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
