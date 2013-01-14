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

package org.geomajas.spring;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.stereotype.Component;

/**
 * Thread scope which allows putting data in thread scope and clearing up afterwards.
 *
 * @author Joachim Van der Auwera
 */
@Component
public class ThreadScope implements Scope, DisposableBean {

	public ThreadScope() {
		ThreadScopeContextHolder.clear();
	}

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

	public void destroy() throws Exception {
		ThreadScopeContextHolder.clear();
	}
}
