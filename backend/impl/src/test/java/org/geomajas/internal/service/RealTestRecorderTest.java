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
		recorder.record("bla", "more");
		Assert.assertEquals("", recorder.matches("bla", "something", "more"));
		recorder.record(null, "bla");
		Assert.assertEquals("", recorder.matches("bla", "something", "more"));
		Assert.assertEquals("", recorder.matches(null, "bla"));
		Assert.assertEquals("", recorder.matches("null", "bla"));

		recorder.clear();
		Assert.assertEquals("", recorder.matches("bla"));
		Assert.assertNotSame("", recorder.matches("bla", "something", "more"));
		Assert.assertEquals("no messages for group", recorder.matches("bla", "something", "more"));
		recorder.record("bla", "something");
		Assert.assertEquals("too little recorded messages, only 1 available",
				recorder.matches("bla", "something", "more"));
		Assert.assertEquals("match failed at position 0, requested [bla] got [something]",
				recorder.matches("bla", "bla"));
		recorder.record("bla", "more");
		recorder.record("bla", "and more");
		Assert.assertEquals("more recorded messages then tested", recorder.matches("bla", "something", "more"));
	}
}
