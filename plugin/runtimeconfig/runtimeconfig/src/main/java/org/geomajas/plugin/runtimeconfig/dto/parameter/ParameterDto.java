/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.runtimeconfig.dto.parameter;

import java.io.Serializable;

/**
 * DTO object for a bean factory parameter.
 * 
 * @author Jan De Moerloose
 * 
 * @param <T>
 */
public interface ParameterDto<T> extends Serializable {

	void setName(String name);

	String getName();

	void setValue(T value);

	T getValue();
}
