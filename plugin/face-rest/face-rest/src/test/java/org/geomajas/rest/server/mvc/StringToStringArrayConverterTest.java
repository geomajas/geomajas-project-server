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

package org.geomajas.rest.server.mvc;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test for {@link org.geomajas.rest.server.mvc.StringToStringArrayConverter}.
 */
public class StringToStringArrayConverterTest {

	@Test
	public void testSplitWithoutWhitespace() {
		StringToStringArrayConverter converter= new StringToStringArrayConverter();
		String[] result = converter.convert("a,b,c");
		Assert.assertArrayEquals(new String[]{"a","b","c"}, result);
	}
	
	@Test
	public void testSplitWithWhitespace() {
		StringToStringArrayConverter converter= new StringToStringArrayConverter();
		String[] result = converter.convert(" \n  a,     b\t,\nc   \n \t ");
		Assert.assertArrayEquals(new String[]{"a","b","c"}, result);
	}
	
	@Test
	public void testRegExSplitter() {
		StringToStringArrayConverter converter= new StringToStringArrayConverter();
		converter.setRegexSplitter("[\\s\\|]+");
		String[] result = converter.convert(" \n  a|     b\t|\nc   \n \t ");
		Assert.assertArrayEquals(new String[]{"a","b","c"}, result);
	}
}
