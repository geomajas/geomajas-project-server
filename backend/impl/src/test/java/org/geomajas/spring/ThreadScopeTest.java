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

package org.geomajas.spring;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.scope.ScopedObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test to verify that thread scoped beans work properly.
 * 
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml", "/org/geomajas/spring/moreContext.xml" })
public class ThreadScopeTest {

	private static final String VALUE_FIRST = "first";

	private static final String VALUE_OTHER = "other";

	private static final String BEAN_NAME = "spring.ThreadedService";

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private ThreadedService threadedService;

	@Test
	public void testThreadScope() throws Exception {
		// verify that set/get works in normal case
		threadedService.setValue(VALUE_FIRST);
		Assert.assertEquals(VALUE_FIRST, threadedService.getValue());

		// assure bean is not treated as prototype
		ThreadedService ts = applicationContext.getBean(BEAN_NAME, ThreadedService.class);
		Assert.assertEquals(VALUE_FIRST, ts.getValue());

		Thread thread = new Thread(new Runnable() {

			public void run() {
				// we are in the thread, now create the autowired class and test:
				testInOtherThread();
			}
		});
		thread.start();
		thread.join();

		// now verify that we can clear the thread data
		Assert.assertEquals(VALUE_FIRST, threadedService.getValue());
		((ScopedObject)threadedService).removeFromScope();
		Assert.assertNull(threadedService.getValue());
	}

	private void testInOtherThread() {
		Assert.assertNull(threadedService.getValue());
		threadedService.setValue(VALUE_OTHER);
		Assert.assertEquals(VALUE_OTHER, threadedService.getValue());
	}
}
