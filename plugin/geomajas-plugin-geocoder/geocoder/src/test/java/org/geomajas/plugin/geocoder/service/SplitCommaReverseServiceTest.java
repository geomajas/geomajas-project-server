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

package org.geomajas.plugin.geocoder.service;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Test for SplitCommaReverseService.
 *
 * @author Joachim Van der Auwera
 */
public class SplitCommaReverseServiceTest {


	private static final String FULL = "Gaston Crommenlaan, Gent, Be";
	private static final String PART0 = "Be";
	private static final String PART1 = "Gent";
	private static final String PART2 = "Gaston Crommenlaan";

	@Test
	public void testSplit() throws Exception {
		SplitCommaReverseService service = new SplitCommaReverseService();
		List<String> res = service.split(FULL);
		Assert.assertNotNull(res);
		Assert.assertEquals(3, res.size());
		Assert.assertEquals(PART0, res.get(0));
		Assert.assertEquals(PART1, res.get(1));
		Assert.assertEquals(PART2, res.get(2));
	}

	@Test
	public void testSplit2() throws Exception {
		SplitCommaReverseService service = new SplitCommaReverseService();
		List<String> res = service.split("");
		Assert.assertNotNull(res);
		Assert.assertEquals(0, res.size());
		res = service.split("x, , y");
		Assert.assertEquals(2, res.size());
		Assert.assertEquals("y", res.get(0));
		Assert.assertEquals("x", res.get(1));
		res = service.split(" , ");
		Assert.assertNotNull(res);
		Assert.assertEquals(0, res.size());
	}

	@Test
	public void testCombine() throws Exception {
		SplitCommaReverseService service = new SplitCommaReverseService();
		List<String> list = new ArrayList<String>();
		list.add(PART0);
		list.add(PART1);
		list.add(PART2);
		Assert.assertEquals(FULL, service.combine(list));
	}

	@Test
	public void testCombine2() throws Exception {
		SplitCommaReverseService service = new SplitCommaReverseService();
		List<String> list = new ArrayList<String>();
		Assert.assertEquals("", service.combine(list));
	}
}
