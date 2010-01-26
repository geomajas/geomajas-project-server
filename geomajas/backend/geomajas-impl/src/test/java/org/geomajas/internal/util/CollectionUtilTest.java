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

package org.geomajas.internal.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the ColletionUtil class
 *
 * @author check subversion
 */
public class CollectionUtilTest {

	@Test
	public void testMapToString() throws Exception {
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("Test", "test");

		assertEquals("Test=test\n", CollectionUtil.mapToString(map));
	}

	@Test
	public void testStringToMap() throws Exception {
		String str = "Test=test\n";

		Map<String, String> map = CollectionUtil.stringToMap(str);
		assertEquals("test", map.get("Test"));
	}

	@Test
	public void testStringToList() throws Exception {
		String str = "one,two,,four";

		List<String> res = CollectionUtil.stringToList(str);
		assertTrue("size", 3 == res.size());
		assertEquals("same", "two", res.get(1));
		assertEquals("same2", "four", res.get(2));
	}

	@Test
	public void testListToString() throws Exception {
		String expected = "one,two,,four";
		List<String> lst = new ArrayList<String>(4);
		lst.add("one");
		lst.add("two");
		lst.add("");
		lst.add("four");

		String res = CollectionUtil.listToString(lst);
		assertEquals(expected, res);
	}

}
