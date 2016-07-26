/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.service;

import org.geomajas.annotation.Api;

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
