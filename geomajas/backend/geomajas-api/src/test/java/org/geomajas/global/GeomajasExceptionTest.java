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
package org.geomajas.global;

import junit.framework.TestCase;

import java.util.Locale;
import java.util.MissingResourceException;

/**
 * Tests to verify correct functioning of GeomajasException
 * 
 * @author Joachim Van der Auwera
 */
public class GeomajasExceptionTest extends TestCase {

	public void testTranslation() {
		Locale dutch = new Locale("nl");
		Locale.setDefault(dutch);
		GeomajasException exc = new GeomajasException(ExceptionCode.TEST, "TEST");
		assertEquals(-1, exc.getExceptionCode());
		assertEquals("Test foutboodschap met TEST parameter", exc.getMessage());
		exc = new GeomajasException(-2, "TEST");
		assertEquals(-2, exc.getExceptionCode());
		assertEquals("Test foutboodschap met vertaalde parameter", exc.getMessage());
	}

	public void testAddMissing() {
		Locale dutch = new Locale("nl");
		Locale.setDefault(dutch);
		GeomajasException exc = new GeomajasException(-1, "TEST", "too much");
		assertTrue(exc.getMessage().contains("too much"));
	}

	public void testEnglishOnly() {
		Locale dutch = new Locale("nl");
		Locale.setDefault(dutch);
		GeomajasException exc = new GeomajasException(-3);
		assertEquals("English only", exc.getMessage());
	}

	public void testUnknownLanguage() {
		Locale unknown = new Locale("xx");
		Locale.setDefault(unknown);
		GeomajasException exc = new GeomajasException(-1, "TEST");
		assertEquals("Test exception message with TEST parameter", exc.getMessage());
	}

	public void testDifferentResourceBundle() {
		TestException exc = new TestException("org.geomajas.global.GeomajasExceptionTest");
		assertEquals("Test 123", exc.getMessage());
	}

	public void testMissingDifferentResourceBundle() {
		try {
			TestException exc = new TestException("test.bad.bundlename");
			exc.getMessage();
			fail("should have thrown MissingResourceException");
			// if we come here it means the getResourceBundleName() method was not used
		} catch (MissingResourceException e) {
			// this is correct as the custom bundle does not exist
		}
	}

	// ------------------------------------------------------------------

	private static class TestException extends GeomajasException {
		private static final long serialVersionUID = 1L;
		String bundleName;

		public TestException(String bundleName) {
			super(123);
			this.bundleName = bundleName;
		}

		@Override
		public String getResourceBundleName() {
			return bundleName;
		}
	}
}
