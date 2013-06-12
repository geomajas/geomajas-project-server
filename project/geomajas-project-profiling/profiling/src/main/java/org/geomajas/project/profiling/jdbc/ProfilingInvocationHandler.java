/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.project.profiling.jdbc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Wrapping JDBC object which can be used to profile the time spent communicating with the database.
 *
 * @author Joachim Van der Auwera
 */
public class ProfilingInvocationHandler implements InvocationHandler {

	private String groupPrefix;
	private Object delegate;

	/**
	 * Constructor.
	 *
	 * @param delegate the "real" prepared statement which is profiled.
	 */
	public ProfilingInvocationHandler(String groupPrefix, Object delegate) {
		this.groupPrefix = groupPrefix;
		this.delegate = delegate;
	}

	@Override
	// CHECKSTYLE THROWS_THROWABLE: OFF
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		long start = System.currentTimeMillis();
		try {
			return method.invoke(delegate, args);
		} finally {
			ProfilingDriver.register(groupPrefix + method.getName(), System.currentTimeMillis() - start);
		}
	}
	// CHECKSTYLE THROWS_THROWABLE: ON
}
