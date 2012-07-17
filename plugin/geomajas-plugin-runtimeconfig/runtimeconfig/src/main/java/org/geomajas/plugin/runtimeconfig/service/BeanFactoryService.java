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
package org.geomajas.plugin.runtimeconfig.service;

import java.util.List;
import java.util.Map;

import org.geomajas.plugin.runtimeconfig.RuntimeConfigException;
import org.springframework.beans.factory.config.BeanDefinitionHolder;

/**
 * Service that creates bean definitions based on a set of parameters.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface BeanFactoryService {

	/**
	 * Create a list of bean definitions for the specified set of parameters.
	 * 
	 * @param parameters
	 * @return
	 * @throws RuntimeConfigException
	 */
	List<BeanDefinitionHolder> createBeans(Map<String, Object> parameters) throws RuntimeConfigException;

}
