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

import org.geomajas.project.profiling.jmx.GroupData;

/**
 * Set of profiling data for a specific group.
 *
 * @author Joachim Van der Auwera
 */
public class GroupContainer extends OneContainer implements GroupData {

	private static final long serialVersionUID = 100;
	
	private final String group;

	/**
	 * Construct container for given group, number of invocations and total run time.
	 *
	 * @param group group name
	 * @param invocationCount invocation count
	 * @param totalRunTime total run time
	 */
	public GroupContainer(String group, long invocationCount, long totalRunTime) {
		super(invocationCount, totalRunTime);
		this.group = group;
	}

	/** {@inheritDoc} */
	public String getGroup() {
		return group;
	}

	@Override
	public String toString() {
		return "GroupContainer{" +
				"group='" + group + "\', " + super.toString() +
				'}';
	}
}
