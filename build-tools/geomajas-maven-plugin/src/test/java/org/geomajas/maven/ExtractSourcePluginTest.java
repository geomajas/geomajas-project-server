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

package org.geomajas.maven;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests for ExtractSourcePlugin
 *
 * @author Joachim Van der Auwera
 */
public class ExtractSourcePluginTest {

	@Test
	public void testPrepareLinesTabs() throws Exception {
		List<String> list  = new ArrayList<String>();
		list.add("some\ttest");
		ExtractSourcePlugin esp = new ExtractSourcePlugin();
		esp.prepareLines(list);
		Assert.assertEquals("some    test", list.get(0));
	}

	@Test
	public void testPrepareLinesFixIndent() throws Exception {
		List<String> list  = new ArrayList<String>();
		list.add("   line {");
		list.add("      blabla");
		list.add("   }");
		ExtractSourcePlugin esp = new ExtractSourcePlugin();
		esp.prepareLines(list);
		Assert.assertEquals("line {", list.get(0));
		Assert.assertEquals("   blabla", list.get(1));
		Assert.assertEquals("}", list.get(2));
	}

	@Test
	public void testPrepareLinesXmlEscape() throws Exception {
		List<String> list  = new ArrayList<String>();
		list.add("<test>black & white</test>");
		ExtractSourcePlugin esp = new ExtractSourcePlugin();
		esp.prepareLines(list);
		// should not replace, block will be stored in CDATA
		//Assert.assertEquals("&lt;test&gt;black &amp; white&lt;/test&gt;", list.get(0));
		Assert.assertEquals("<test>black & white</test>", list.get(0));
	}
}
