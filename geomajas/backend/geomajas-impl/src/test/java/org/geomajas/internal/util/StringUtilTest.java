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

import junit.framework.TestCase;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Tests for the StringUtil class
 *
 * @author Joachim Van der Auwera
 */
public class StringUtilTest extends TestCase {

	Map<String, String> keyValuePairs;

	protected void setUp() throws Exception {
		keyValuePairs = new HashMap<String, String>();
		keyValuePairs.put("language", "Dutch");
		keyValuePairs.put("country", "Belgium");
		keyValuePairs.put("day", "wednesday");
	}

	public void testSubstituteParametersBoundary() {
		assertEquals("", StringUtil.substituteParameters(null, keyValuePairs));
		assertEquals("", StringUtil.substituteParameters("", keyValuePairs));
	}

	public void testSubstituteParametersReplace() {
		assertEquals("Dutch", StringUtil.substituteParameters("${language}", keyValuePairs));
		assertEquals("$Dutch", StringUtil.substituteParameters("$${language}", keyValuePairs));
		assertEquals("Dutch$", StringUtil.substituteParameters("${language}$", keyValuePairs));
		assertEquals("In Belgium some people speak Dutch.",
				StringUtil.substituteParameters("In ${country} some people speak ${language}.", keyValuePairs));
	}

	public void testSubstituteParametersEscape() {
		assertEquals("x\\${language}y", StringUtil.substituteParameters("x\\${language}y", keyValuePairs));
		assertEquals("\\${language}", StringUtil.substituteParameters("\\${language}", keyValuePairs));
		assertEquals(" \\${language}", StringUtil.substituteParameters(" \\${language}", keyValuePairs));
		assertEquals("\\${language} ", StringUtil.substituteParameters("\\${language} ", keyValuePairs));
	}

	public void testSubstituteParametersUnknown() {
		try {
			StringUtil.substituteParameters("${unknown}", keyValuePairs);
			fail("Should throw IllegalArgumentException");
		} catch (IllegalArgumentException iea) {
		}
	}

	public void testSubstituteParametersCase() {
		assertEquals("wednesday", StringUtil.substituteParameters("${day}", keyValuePairs));
		try {
			assertEquals("wednesday", StringUtil.substituteParameters("${Day}", keyValuePairs));
			fail("Should throw IllegalArgumentException");
		} catch (IllegalArgumentException iea) {
		}
	}

	@Test
	public void testSubstituteParameters() throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("parameter", "param1");

		assertEquals("01", "", StringUtil.substituteParameters("", params));
		assertEquals("02", "Testje", StringUtil.substituteParameters("Testje", params));

		assertEquals("04", "", StringUtil.substituteParameters("", params));
		assertEquals("05", "Testje", StringUtil.substituteParameters("Testje", params));
		assertEquals("06", "testje met param1.", StringUtil.substituteParameters("testje met ${parameter}.",
				params));
		assertEquals("07", "param1", StringUtil.substituteParameters("${parameter}", params));
		assertEquals("08", "param1 einde", StringUtil.substituteParameters("${parameter} einde", params));
		assertEquals("09", "begin param1", StringUtil.substituteParameters("begin ${parameter}", params));

		params.put("nogParam", "param2");
		assertEquals("10", "[param1]-[param2]", StringUtil.substituteParameters(
				"[${parameter}]-[${nogParam}]", params));

		assertEquals("11", "$.{parameter}", StringUtil.substituteParameters("$.{parameter}", params));
		assertEquals("12", "{parameter}", StringUtil.substituteParameters("{parameter}", params));
		assertEquals("13", "$parameter}", StringUtil.substituteParameters("$parameter}", params));
		assertEquals("14", "${parameter", StringUtil.substituteParameters("${parameter", params));
	}

	@Test
	public void testSubstituteFormulas() throws Exception {
		assertEquals("01", "", StringUtil.substituteFormulas(""));
		assertEquals("02", "Testje", StringUtil.substituteFormulas("Testje"));

		assertEquals("03", "bla", StringUtil.substituteFormulas("$left:3{blabla}"));
		assertEquals("04", "voor_bla_na", StringUtil.substituteFormulas("voor_$left:3{blabla}_na"));
		assertEquals("05", "blabla", StringUtil.substituteFormulas("$left:99{blabla}"));
		assertEquals("06", "", StringUtil.substituteFormulas("$left:0{blabla}"));

		assertEquals("07", "bla+ble", StringUtil.substituteFormulas("$left:3{blabla}+$left:3{bleh}"));

		assertEquals("08", "", StringUtil.substituteFormulas("$left:3{}"));
	}

	@Test
	public void testPadding() throws Exception {
		assertEquals("0000000123", StringUtil.leftPad("123", 10, '0'));
		assertEquals("1230000000", StringUtil.rightPad("123", 10, '0'));
		assertEquals("12345678910", StringUtil.leftPad("12345678910", 10, '0'));
		assertEquals("12345678910", StringUtil.rightPad("12345678910", 10, '0'));
	}

}
