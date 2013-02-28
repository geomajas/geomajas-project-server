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

package org.geomajas.project.profiling.service;

import org.geomajas.project.profiling.jmx.ProfilingData;

import java.io.Serializable;

/**
 * Set of profiling data.
 *
 * @author Joachim Van der Auwera
 */
public class OneContainer implements ProfilingData, Serializable {

	private static final long serialVersionUID = 100;

	private final long invocationCount;
	private final long totalRunTime;

	/**
	 * Construct container for given number of invocations and total run time.
	 *
	 * @param invocationCount invocation count
	 * @param totalRunTime total run time
	 */
	public OneContainer(long invocationCount, long totalRunTime) {
		this.invocationCount = invocationCount;
		this.totalRunTime = totalRunTime;
	}

	/** {@inheritDoc} */
	public long getInvocationCount() {
		return invocationCount;
	}

	/** {@inheritDoc} */
	public long getTotalRunTime() {
		return totalRunTime;
	}

	/** {@inheritDoc} */
	public double getAverageRunTime() {
		if (invocationCount > 0) {
			return ((double) totalRunTime) / invocationCount;
		} else {
			return 0;
		}
	}

	@Override
	public String toString() {
		return "OneContainer{" +
				"invocationCount=" + invocationCount +
				", totalRunTime=" + totalRunTime +
				'}';
	}
}
