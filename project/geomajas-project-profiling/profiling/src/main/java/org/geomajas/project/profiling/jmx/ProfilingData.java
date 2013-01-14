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

package org.geomajas.project.profiling.jmx;

import org.geomajas.annotation.Api;

/**
 * Profiling information.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface ProfilingData {

	/**
	 * Get number of invocations.
	 *
	 * @return number of invocations
	 */
	long getInvocationCount();

	/**
	 * Get total time spent for the invocations.
	 *
	 * @return total run time
	 */
	long getTotalRunTime();

	/**
	 * Get average run time per invocation.
	 *
	 * @return average run time
	 */
	double getAverageRunTime();

}
