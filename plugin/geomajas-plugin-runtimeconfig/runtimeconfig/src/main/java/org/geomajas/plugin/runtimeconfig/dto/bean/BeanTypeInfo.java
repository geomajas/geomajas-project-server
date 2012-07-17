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
package org.geomajas.plugin.runtimeconfig.dto.bean;

import java.io.Serializable;

/**
 * DTO object that contains bean type meta-data.
 * 
 * @author Jan De Moerloose
 */
public class BeanTypeInfo implements Serializable {

	private static final long serialVersionUID = 100L;

	private String className;

	public BeanTypeInfo() {
	}

	public BeanTypeInfo(String className) {
		this.className = className;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
}
