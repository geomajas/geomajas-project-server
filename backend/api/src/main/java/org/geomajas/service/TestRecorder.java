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

package org.geomajas.service;

import org.geomajas.global.Api;

/**
 * Recorder to allow easier testing of internal workings.
 *
 * @author Joachim Van der Auwera
 * @since 1.8.0
 */
@Api(allMethods = true)
public interface TestRecorder {

	/**
	 * Record a message. Messages are kept in groups which can be tested. Null messages are not recorded.
	 *
	 * @param group group to add message for
	 * @param message message to record
	 */
	void record(Object group, String message);

	/**
	 * Clear all recorded messages.
	 */
	void clear();

	/**
	 * Verify that the messages for a group match.
	 * Returns a message with the problem or the empty string when everything matches.
	 * <p/>
	 * Expected to use in a test like <code>Assert.assertEquals("", recorder.matches(GROUP, "bla");</code>
	 *
	 * @param group group to test messages for
	 * @param messages messages to compare
	 * @return emptry string on match or message about problem
	 */
	String matches(Object group, String... messages);
}
