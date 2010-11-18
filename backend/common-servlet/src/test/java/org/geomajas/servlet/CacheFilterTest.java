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

package org.geomajas.servlet;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test for {@link CacheFilter}.
 *
 * @author Joachim Van der Auwera
 */
public class CacheFilterTest {

	@Test
	public void testShouldCache() {
		CacheFilter cf = new CacheFilter();
		Assert.assertTrue(cf.shouldCache("/blabla.jpg"));
		//Assert.assertTrue(cf.shouldCache("/blabla.jpeg"));
		Assert.assertTrue(cf.shouldCache("/blabla.png"));
		Assert.assertTrue(cf.shouldCache("/blabla.gif"));
		Assert.assertTrue(cf.shouldCache("/blabla.css"));
		Assert.assertTrue(cf.shouldCache("/blabla.js"));
		Assert.assertTrue(cf.shouldCache("/blabla.html"));
		Assert.assertTrue(cf.shouldCache("/bla.cache.xx"));
		Assert.assertFalse(cf.shouldCache("/blabla.xx"));
		Assert.assertTrue(cf.shouldCache("/a/blabla.jpg"));
		//Assert.assertTrue(cf.shouldCache("/a/blabla.jpeg"));
		Assert.assertTrue(cf.shouldCache("/a/blabla.png"));
		Assert.assertTrue(cf.shouldCache("/a/blabla.gif"));
		Assert.assertTrue(cf.shouldCache("/a/blabla.css"));
		Assert.assertTrue(cf.shouldCache("/a/blabla.js"));
		Assert.assertTrue(cf.shouldCache("/a/blabla.html"));
		Assert.assertTrue(cf.shouldCache("/a/bla.cache.xx"));
		Assert.assertFalse(cf.shouldCache("/a/blabla.xx"));
		/*
		Assert.assertTrue(cf.shouldCache("/blabla.JPG"));
		Assert.assertTrue(cf.shouldCache("/blabla.JPEG"));
		Assert.assertTrue(cf.shouldCache("/blabla.PnG"));
		Assert.assertTrue(cf.shouldCache("/blabla.GIF"));
		Assert.assertTrue(cf.shouldCache("/blabla.CSS"));
		Assert.assertTrue(cf.shouldCache("/blabla.JS"));
		Assert.assertTrue(cf.shouldCache("/blabla.HTML"));
		Assert.assertTrue(cf.shouldCache("/bla.cache.XX"));
		Assert.assertTrue(cf.shouldCache("/bla.CACHE.xx"));
		Assert.assertFalse(cf.shouldCache("/blabla.XX"));
		Assert.assertTrue(cf.shouldCache("/a/blabla.JPG"));
		Assert.assertTrue(cf.shouldCache("/a/blabla.PNG"));
		Assert.assertTrue(cf.shouldCache("/a/blabla.GIF"));
		Assert.assertTrue(cf.shouldCache("/a/blabla.CSS"));
		Assert.assertTrue(cf.shouldCache("/a/blabla.JS"));
		Assert.assertTrue(cf.shouldCache("/a/blabla.HTML"));
		Assert.assertTrue(cf.shouldCache("/a/bla.cache.XX"));
		Assert.assertTrue(cf.shouldCache("/a/bla.CACHE.xx"));
		Assert.assertFalse(cf.shouldCache("/a/blabla.XX"));
		*/
	}

	@Test
	public void testShouldNotCache() {
		CacheFilter cf = new CacheFilter();
		Assert.assertTrue(cf.shouldNotCache("/bla.nocache.jpg"));
		//Assert.assertTrue(cf.shouldNotCache("/bla.nocache.jpeg"));
		Assert.assertFalse(cf.shouldNotCache("/blabla.jpg"));
		Assert.assertFalse(cf.shouldNotCache("/blabla.jpeg"));
		Assert.assertFalse(cf.shouldNotCache("/blabla.html"));
		Assert.assertTrue(cf.shouldNotCache("/bla.nocache.jpg"));
		Assert.assertTrue(cf.shouldNotCache("/bla.nocache.jpeg"));
		Assert.assertFalse(cf.shouldNotCache("/blabla.JPG"));
		Assert.assertFalse(cf.shouldNotCache("/blabla.JPEG"));
		Assert.assertFalse(cf.shouldNotCache("/blabla.HTML"));
		//Assert.assertTrue(cf.shouldNotCache("/bla.NOCACHE.jpg"));
		Assert.assertTrue(cf.shouldNotCache("/bla.nocache.JPG"));
		//Assert.assertTrue(cf.shouldNotCache("/bla.NOCACHE.jpeg"));
		Assert.assertTrue(cf.shouldNotCache("/bla.nocache.JPeG"));
	}

	@Test
	public void testShouldCompress() {
		CacheFilter cf = new CacheFilter();
		Assert.assertTrue(cf.shouldCompress("/blabla.js"));
		Assert.assertTrue(cf.shouldCompress("/blabla.html"));
		Assert.assertTrue(cf.shouldCompress("/blabla.css"));
		Assert.assertFalse(cf.shouldCompress("/blabla.jpg"));
		Assert.assertFalse(cf.shouldCompress("/blabla.jpeg"));
		Assert.assertFalse(cf.shouldCompress("/blabla.gif"));
		Assert.assertFalse(cf.shouldCompress("/blabla.png"));
		/*
		Assert.assertTrue(cf.shouldCompress("/blabla.JS"));
		Assert.assertTrue(cf.shouldCompress("/blabla.HtML"));
		Assert.assertTrue(cf.shouldCompress("/blabla.CsS"));
		Assert.assertFalse(cf.shouldCompress("/blabla.JPG"));
		Assert.assertFalse(cf.shouldCompress("/blabla.JPeG"));
		Assert.assertFalse(cf.shouldCompress("/blabla.GIF"));
		Assert.assertFalse(cf.shouldCompress("/blabla.PNG"));
		*/
	}

}
