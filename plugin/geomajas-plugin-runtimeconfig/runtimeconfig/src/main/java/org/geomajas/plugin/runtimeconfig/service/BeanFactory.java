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
import java.util.Map;

import org.geomajas.annotation.ExpectAlternatives;
import org.geomajas.plugin.admin.AdminException;
import org.springframework.beans.factory.config.BeanDefinitionHolder;

/**
 * Factory for creating {@link BeanDefinitionHolder} objects from a map of parameters. All factories declared in the
 * application context will be discovered through autowiring. The factory for which {@link #getPriority(Map)} returns
 * the highest priority level for a given map of parameters will be used. Factories should return {@link Priority.NONE}
 * to be skipped, {@link Priority.DEFAULT} if they provide a default implementation or {@link Priority.HIGH} if they
 * override the default implementation.
 * 
 * @author Jan De Moerloose
 * 
 */
@ExpectAlternatives
public interface BeanFactory extends Rewirable {

	/**
	 * Name (id) of the bean [type= String].
	 */
	String BEAN_NAME = "beanName";

	/**
	 * Bean class name [type= String].
	 */
	String CLASS_NAME = "className";

	/**
	 * Referenced beans [type = Collection of NamedObject].
	 */
	String BEAN_REFS = "beanRefs";

	/**
	 * Priority of a factory (NONE=skip, DEFAULT=default, HIGH=application specific).
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	enum Priority {
		NONE, DEFAULT, HIGH;

		public Priority and(Priority other) {
			switch (other) {
				case DEFAULT:
					return this == HIGH ? DEFAULT : this;
				case HIGH:
					return this;
				case NONE:
				default:
					return NONE;
			}
		}
	}

	/**
	 * Get the priority of this factory for the parameters passed.
	 * 
	 * @param parameters
	 * @return
	 */
	Priority getPriority(Map<String, Object> parameters);

	/**
	 * Create a list of bean definitions for the specified set of parameters.
	 * 
	 * @param parameters
	 * @return
	 * @throws AdminException
	 */
	List<BeanDefinitionHolder> createBeans(Map<String, Object> parameters) throws AdminException;

}
