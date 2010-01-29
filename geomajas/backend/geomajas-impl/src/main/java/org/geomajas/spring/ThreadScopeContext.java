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

import java.util.HashMap;
import java.util.Map;

/**
 * Object in which the thread scoped beans are stored (thread specific instances are obtained using
 * {@link ThreadScopeContextHolder}.
 *
 * @author Joachim Van der Auwera
 */
public class ThreadScopeContext {

	protected final Map<String, Bean> beans = new HashMap<String, Bean>();

	/**
	 * Get a bean value from the context.
	 *
	 * @param name bean name
	 * @return bean value or null
	 */
	public Object getBean(String name) {
		Bean bean = beans.get(name);
		if (null == bean) {
			return null;
		}
		return bean.object;
	}

	/**
	 * Set a bean in the context.
	 *
	 * @param name bean name
	 * @param object bean value
	 */
	public void setBean(String name, Object object) {
		Bean bean = beans.get(name);
		if (null == bean) {
			bean = new Bean();
			beans.put(name, bean);
		}
		bean.object = object;
	}

	/**
	 * Remove a bean from the context, calling the destruction callback if any.
	 *
	 * @param name bean name
	 * @return previous value
	 */
	public Object remove(String name) {
		Bean bean = beans.get(name);
		if (null != bean) {
			beans.remove(name);
			bean.destructionCallback.run();
			return bean.object;
		}
		return null;
	}

	/**
	 * Register the given callback as to be executed after request completion.
	 *
	 * @param name The name of the bean.
	 * @param callback The callback of the bean to be executed for destruction.
	 */
	public void registerDestructionCallback(String name, Runnable callback) {
		Bean bean = beans.get(name);
		if (null == bean) {
			bean = new Bean();
			beans.put(name, bean);
		}
		bean.destructionCallback = callback;
	}

	/** Clear all beans and call the destruction callback. */
	public void clear() {
		for (Bean bean : beans.values()) {
			if (null != bean.destructionCallback) {
				bean.destructionCallback.run();
			}
		}
		beans.clear();
	}

	/** Private class storing bean name and destructor callback. */
	private class Bean {

		private Object object;
		private Runnable destructionCallback;

		public Object getObject() {
			return object;
		}

		public void setObject(Object object) {
			this.object = object;
		}

		public Runnable getDestructionCallback() {
			return destructionCallback;
		}

		public void setDestructionCallback(Runnable destructionCallback) {
			this.destructionCallback = destructionCallback;
		}
	}
}
