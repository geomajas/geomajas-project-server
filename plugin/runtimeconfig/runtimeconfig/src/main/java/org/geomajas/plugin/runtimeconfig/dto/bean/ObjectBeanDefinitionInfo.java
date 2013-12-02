/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.runtimeconfig.dto.bean;

import org.geomajas.configuration.IsInfo;

/**
 * 
 * @author Jan De Moerloose
 *
 */
public class ObjectBeanDefinitionInfo implements BeanDefinitionInfo {

	private static final long serialVersionUID = 100L;
	
	private IsInfo object;

	public String getClassName() {
		return object.getClass().getName();
	}

	public void setClassName(String className) {
		// not implemented
	}
	
	public IsInfo getObject() {
		return object;
	}
	
	public void setObject(IsInfo object) {
		this.object = object;
	}

}
