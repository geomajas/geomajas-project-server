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
