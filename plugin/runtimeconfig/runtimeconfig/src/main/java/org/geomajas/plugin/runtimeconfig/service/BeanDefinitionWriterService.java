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
package org.geomajas.plugin.runtimeconfig.service;

import java.util.List;

import org.geomajas.plugin.runtimeconfig.RuntimeConfigException;
import org.springframework.beans.factory.config.BeanDefinitionHolder;

/**
 * Persistence service for Spring bean definitions.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface BeanDefinitionWriterService {

	/**
	 * Persists the specified bean.
	 * 
	 * @param key persistence key, can be used to delete
	 * @param bean bean definition
	 */
	void persist(String key, BeanDefinitionHolder bean) throws RuntimeConfigException;

	/**
	 * Persists the specified beans.
	 * 
	 * @param key persistence key, can be used to delete
	 * @param beans list of bean definitions
	 */
	void persist(String key, List<BeanDefinitionHolder> beans) throws RuntimeConfigException;

	/**
	 * Removes the persisted beans associated with this key.
	 * 
	 * @param key
	 * @throws RuntimeConfigException
	 */
	void delete(String key) throws RuntimeConfigException;

}
