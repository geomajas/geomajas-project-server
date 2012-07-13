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
package org.geomajas.plugin.admin.dto.parameter;

import org.geomajas.configuration.IsInfo;

/**
 * Bean factory parameter that wraps a {@link IsInfo} object.
 * 
 * @author Jan De Moerloose
 * 
 */
public class ObjectParameterDto implements ParameterDto<IsInfo> {

	private IsInfo value;

	private String name;

	public ObjectParameterDto() {

	}

	public ObjectParameterDto(String name, IsInfo value) {
		this.value = value;
		this.name = name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setValue(IsInfo value) {
		this.value = value;
	}

	public IsInfo getValue() {
		return value;
	}

}