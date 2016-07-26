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
package org.geomajas.plugin.runtimeconfig.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.plugin.runtimeconfig.RuntimeConfigException;
import org.geomajas.plugin.runtimeconfig.service.BeanFactory.Priority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.stereotype.Component;

/**
 * Default implementation of {@link BeanFactoryService}.
 * 
 * @author Jan De Moerloose
 * 
 */
@Component
public class BeanFactoryServiceImpl implements BeanFactoryService {

	@Autowired(required = false)
	private Map<String, BeanFactory> factories = new HashMap<String, BeanFactory>();

	public List<BeanDefinitionHolder> createBeans(Map<String, Object> parameters) throws RuntimeConfigException {
		Priority bestPriority = Priority.NONE;
		BeanFactory bestFactory = null;
		for (BeanFactory factory : factories.values()) {
			if (factory.getPriority(parameters).compareTo(bestPriority) > 0) {
				bestPriority = factory.getPriority(parameters);
				bestFactory = factory;
			}
		}
		if (bestPriority != Priority.NONE) {
			return bestFactory.createBeans(parameters);
		} else {
			return Collections.emptyList();
		}
	}

}
