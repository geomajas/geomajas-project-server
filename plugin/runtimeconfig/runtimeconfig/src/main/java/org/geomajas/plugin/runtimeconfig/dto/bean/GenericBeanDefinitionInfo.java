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
package org.geomajas.plugin.runtimeconfig.dto.bean;

import java.util.Map;


/**
 * Data transfer object for a Spring generic bean definition. Holds the bean's class-name, properties, and referencing
 * beans.
 * 
 * @author Pieter De Graef
 * @author Jan De Moerloose
 */
public class GenericBeanDefinitionInfo implements BeanDefinitionInfo {

	private static final long serialVersionUID = 100L;

	private String className;

	private Map<String, BeanMetadataElementInfo> propertyValues;

	// ------------------------------------------------------------------------
	// Getters and setters:
	// ------------------------------------------------------------------------

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Map<String, BeanMetadataElementInfo> getPropertyValues() {
		return propertyValues;
	}

	public void setPropertyValues(Map<String, BeanMetadataElementInfo> propertyValues) {
		this.propertyValues = propertyValues;
	}

	public String toString() {
		return "[BeanDefinitionInfo,{className=" + getClassName() + ",props={" + getPropertyValues() + "}]";

	}
}
