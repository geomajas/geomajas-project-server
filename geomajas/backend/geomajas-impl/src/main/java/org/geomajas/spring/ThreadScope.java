/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.spring;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.stereotype.Component;

/**
 * Thread scope which allows putting data in thread scope and clearing up afterwards.
 *
 * @author Joachim Van der Auwera
 */
@Component
public class ThreadScope implements Scope {

	/**
	 * Get bean for given name in the "thread" scope.
	 *
	 * @param name name of bean
	 * @param factory factory for new instances
	 * @return bean for this scope
	 */
	public Object get(String name, ObjectFactory<?> factory) {
		ThreadScopeContext context = ThreadScopeContextHolder.getContext();

		Object result = context.getBean(name);
		if (null == result) {
			result = factory.getObject();
			context.setBean(name, result);
		}
		return result;
	}

	/**
	 * Removes bean from scope.
	 *
	 * @param name bean name
	 * @return previous value
	 */
	public Object remove(String name) {
		ThreadScopeContext context = ThreadScopeContextHolder.getContext();
		return context.remove(name);
	}

	public void registerDestructionCallback(String name, Runnable callback) {
		ThreadScopeContextHolder.getContext().registerDestructionCallback(name, callback);
	}

	/**
	 * Resolve the contextual object for the given key, if any. E.g. the HttpServletRequest object for key "request".
	 *
	 * @param key key
	 * @return contextual object
	 */
	public Object resolveContextualObject(String key) {
		return null;
	}

	/**
	 * Return the conversation ID for the current underlying scope, if any.
	 * <p/>
	 * In this case, it returns the thread name.
	 *
	 * @return thread name as conversation id
	 */
	public String getConversationId() {
		return Thread.currentThread().getName();
	}

}
