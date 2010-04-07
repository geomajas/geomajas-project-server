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
		Assert.assertEquals("&lt;test&gt;black &amp; white&lt;/test&gt;", list.get(0));
	}
}
