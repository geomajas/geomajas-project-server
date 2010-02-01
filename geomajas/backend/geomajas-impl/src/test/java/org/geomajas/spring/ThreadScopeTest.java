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

package org.geomajas.spring;

import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
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
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml", "/org/geomajas/spring/moreContext.xml"})
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

		Thread thread = new Thread(new Runnable(){
			public void run() {
				ThreadedService ts = applicationContext.getBean(BEAN_NAME, ThreadedService.class);
				Assert.assertNull(ts.getValue());
				ts.setValue(VALUE_OTHER);
				ts = applicationContext.getBean(BEAN_NAME, ThreadedService.class);
				Assert.assertEquals(VALUE_OTHER, ts.getValue());
				//To change body of implemented methods use File | Settings | File Templates.
			}
		});
		thread.start();
		thread.join();

		// now verify that we can clear the thread data
		ts = applicationContext.getBean(BEAN_NAME, ThreadedService.class);
		Assert.assertEquals(VALUE_FIRST, ts.getValue());
		ThreadScopeContextHolder.getContext().remove(BEAN_NAME);
		ts = applicationContext.getBean(BEAN_NAME, ThreadedService.class);
		Assert.assertNull(ts.getValue());
	}
}
