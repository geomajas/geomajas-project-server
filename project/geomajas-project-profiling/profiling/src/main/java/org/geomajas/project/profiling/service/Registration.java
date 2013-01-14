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

import com.lmax.disruptor.EventFactory;

/**
 * Value as used by the disruptor for future aggregation.
 *
 * @author Joachim Van der Auwera
 */
public class Registration {

	/** Object factory to pre-fill the ring buffer. */
	public static final EventFactory<Registration> FACTORY = new EventFactory<Registration>() {
		public Registration newInstance() {
			return new Registration();
		}
	};

	private String group;
 	private long duration;

	/**
	 * Get group for registration.
	 *
	 * @return group name
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * Set group for registration.
	 *
	 * @param group group name
	 */
	public void setGroup(String group) {
		this.group = group;
	}

	/**
	 * Get duration for registration.
	 *
	 * @return duration duration
	 */
	public long getDuration() {
		return duration;
	}

	/**
	 * Set duration for registration.
	 *
	 * @param duration duration
	 */
	public void setDuration(long duration) {
		this.duration = duration;
	}
}
