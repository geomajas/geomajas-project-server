/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
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
