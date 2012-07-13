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
package org.geomajas.plugin.admin.service;

import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.plugin.admin.AdminException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.core.io.Resource;

/**
 * Service for configuration of the application context.
 * 
 * @author Jan De Moerloose
 * 
 * @since 0.0.0 
 */
@Api(allMethods = true)
public interface ContextConfiguratorService {

	/**
	 * Configure a bean by adding the bean definition and instantiating the bean if it is a singleton.
	 * 
	 * @param beanName the name of the bean
	 * @param beanDefinition the bean definition
	 * @throws AdminException oops
	 */
	void configureBeanDefinition(String beanName, BeanDefinition beanDefinition) throws AdminException;

	/**
	 * Configure a list of beans by adding the bean definitions and instantiating the beans if they are singletons.
	 * 
	 * @param holders list of bean holders
	 * @throws AdminException oops
	 */
	void configureBeanDefinitions(List<BeanDefinitionHolder> holders) throws AdminException;

	/**
	 * Configure all beans in the specified context location.
	 * 
	 * @param location location of context XML file.
	 * @throws AdminException oops
	 */
	void configureBeanDefinitions(String location) throws AdminException;

	/**
	 * Remove a bean definition from the application context.
	 * 
	 * @param beanName the name of the bean
	 * @throws AdminException oops
	 */
	void removeBeanDefinition(String beanName) throws AdminException;

	/**
	 * Set the list of class names that support rewiring. Beans of these classes will be rewired when the context
	 * changes.
	 * 
	 * @param rewireClassNames
	 */
	void setRewireClassNames(String[] rewireClassNames);

	/**
	 * Set the list of context locations for the rewiring context. This is the application context that is used to
	 * initialise the context.
	 * 
	 * @param rewireContextLocations
	 */
	void setRewireContextLocations(Resource[] rewireContextLocations);

}
