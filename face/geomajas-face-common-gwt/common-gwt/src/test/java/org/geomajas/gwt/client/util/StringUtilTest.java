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

package org.geomajas.gwt.client.util;

import java.util.Arrays;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Tests the StringUtil.
 * 
 * @author Jan De Moerloose
 *
 */
public class StringUtilTest {

	@Test
	public void testJoin() {
		String[] two = new String[]{"a","b"};
		Assert.assertEquals("a.b", StringUtil.join(Arrays.asList(two), "."));
		
		String[] one = new String[]{"a"};
		Assert.assertEquals("a", StringUtil.join(Arrays.asList(one), "."));
		
		String[] none = new String[]{};
		Assert.assertEquals("", StringUtil.join(Arrays.asList(none), "."));
	}
	
	@Test
	public void testGetExtension() {
		String many = "a.b.c.doc";
		Assert.assertEquals("doc", StringUtil.getExtension(many));
		String one = "a.doc";
		Assert.assertEquals("doc", StringUtil.getExtension(one));
		String none = "a";
		Assert.assertEquals(null, StringUtil.getExtension(none));
	}

}
