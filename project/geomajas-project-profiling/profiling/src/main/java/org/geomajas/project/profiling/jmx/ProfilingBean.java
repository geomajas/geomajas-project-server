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

import java.util.List;

/**
 * Interface which should be published using JMX to allow getting the profiling info.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface ProfilingBean {

	/**
	 * Clear data to remove old totals.
	 */
	void clear();

	/**
	 * Get totals across all the groups.
	 *
	 * @return profiling data for all groups
	 */
	ProfilingData getTotal();

	/**
	 * Get data for the groups. The groups are sorted alphabetically.
	 *
	 * @return sorted set of data for the individual groups
	 */
	List<GroupData> getGroupData();
}
