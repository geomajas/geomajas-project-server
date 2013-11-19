/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.internal.service;

import junit.framework.Assert;

import org.geomajas.service.TestRecorder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test for {@link RealTestRecorder}.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/spring/testRecorder.xml"})
public class RealTestRecorderTest {

	@Autowired
	private TestRecorder recorder;

	@Test
	public void testRecorder() throws Exception {
		recorder.clear();
		recorder.record("bla", "something");
		recorder.record("bla", null);
		recorder.record("bla", "more");
		Assert.assertEquals("", recorder.matches("bla", "something", "more"));
		recorder.record(null, "bla");
		Assert.assertEquals("", recorder.matches("bla", "something", "more"));
		Assert.assertEquals("", recorder.matches(null, "bla"));
		Assert.assertEquals("", recorder.matches("null", "bla"));

		recorder.clear();
		Assert.assertEquals("", recorder.matches("bla"));
		Assert.assertNotSame("", recorder.matches("bla", "something", "more"));
		Assert.assertEquals("no messages for group, expected [something, more]",
				recorder.matches("bla", "something", "more"));
		recorder.record("bla", "something");
		Assert.assertEquals("too little recorded messages, only 1 available, [something]",
				recorder.matches("bla", "something", "more"));
		Assert.assertEquals("match failed at position 0, requested [bla] got [something], [something]",
				recorder.matches("bla", "bla"));
		recorder.record("bla", "more");
		recorder.record("bla", "and more");
		Assert.assertEquals("more recorded messages then tested, first 2 ok, [something, more, and more]",
				recorder.matches("bla", "something", "more"));
	}
}
