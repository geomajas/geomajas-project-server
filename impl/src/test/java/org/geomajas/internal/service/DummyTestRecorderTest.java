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

package org.geomajas.internal.service;

import junit.framework.Assert;

import org.geomajas.service.TestRecorder;
import org.geomajas.spring.ThreadScopeContextHolder;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test for {@link DummyTestRecorder}.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml"})
public class DummyTestRecorderTest {

	@Autowired
	private TestRecorder recorder;

	@After
	public void clearTestRecorder() {
		recorder.clear();
		ThreadScopeContextHolder.clear();
	}

	@Test
	public void testRecorder() throws Exception {
		recorder.clear();
		recorder.record("bla", "something");
		recorder.record("bla", "more");
		Assert.assertEquals("DummyTestRecorder, cannot test", recorder.matches("bla", "something", "more"));
		recorder.clear();
		Assert.assertEquals("DummyTestRecorder, cannot test", recorder.matches("bla", "something", "more"));
	}
}
