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
package org.geomajas.plugin.runtimeconfig.dto.bean;

/**
 * Data transfer object for a Spring bean definition.
 * 
 * @author Jan De Moerloose
 */
public interface BeanDefinitionInfo extends BeanMetadataElementInfo {

	/**
	 * Returns the class name of the bean.
	 * 
	 * @return the bean's class name
	 */
	String getClassName();

	/**
	 * Sets the class name of this bean.
	 * 
	 * @param className the bean's class name
	 */
	void setClassName(String className);
}
