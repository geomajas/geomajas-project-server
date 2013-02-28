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

import org.geomajas.annotation.Api;

/**
 * Implement this class and register in {@link ProfilingDriver} to register your profiling information.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface ProfilingListener {

	/**
	 * Register a duration in milliseconds for running a JDBC method.
	 *
	 * @param group indication of type of command.
	 * @param durationMillis duration in milliseconds
	 */
	void register(String group, long durationMillis);

}
