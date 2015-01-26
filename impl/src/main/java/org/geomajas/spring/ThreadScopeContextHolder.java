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

package org.geomajas.spring;

/**
 * Object which holds the different thread scope.
 *
 * @author Joachim Van der Auwera
 */
public final class ThreadScopeContextHolder {

	private static ThreadLocal<ThreadScopeContext> contextHolder =
			new ThreadLocal<ThreadScopeContext>() {
				protected ThreadScopeContext initialValue() {
					return new ThreadScopeContext();
				}
			};

	private ThreadScopeContextHolder() {
		// utility object, not allowed to create instances
	}

	/**
	 * Get the thread specific context.
	 *
	 * @return thread scoped context
	 */
	public static ThreadScopeContext getContext() {
		return contextHolder.get();
	}

	/**
	 * Set the thread specific context.
	 *
	 * @param context thread scoped context
	 */
	public static void setContext(ThreadScopeContext context) {
		ThreadScopeContextHolder.contextHolder.set(context);
	}

	public static void clear() {
		contextHolder =
			new ThreadLocal<ThreadScopeContext>() {
				protected ThreadScopeContext initialValue() {
					return new ThreadScopeContext();
				}
			};
	}
}
