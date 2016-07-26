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

package org.geomajas.plugin.caching.service;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test for {@link CacheContext}.
 *
 * @author Joachim Van der Auwera
 */
public class CacheContextTest {
	private static final String KEY1 = "blabla";
	private static final String KEY2 = "somekey";
	private static final String KEY3 = "another";

	@Test
	public void testCacheContext() {
		CacheContext cc = new CacheContextImpl();

		cc.put(KEY1, new ExtraString("bla"));
		Assert.assertNotNull(cc.get(KEY1));
		Assert.assertNull(cc.get(KEY1, CacheContext.class));
		Assert.assertEquals("bla", cc.get(KEY1, ExtraString.class).getString());
		Assert.assertEquals("bla", cc.get(KEY1, BaseExtraString.class).getString());
		Assert.assertNull(cc.get(KEY2));
	}

	@Test
	public void testCacheContextEquals() {
		CacheContext cc1 = new CacheContextImpl();
		cc1.put(KEY1, this);
		cc1.put(KEY2, KEY1);
		cc1.put(KEY2, KEY2);
		cc1.put(KEY3, 1);
		CacheContext cc2 = new CacheContextImpl();
		cc2.put(KEY1, this);
		cc2.put(KEY2, KEY1);
		cc2.put(KEY2, KEY2);
		cc2.put(KEY3, 1);

		Assert.assertEquals(cc1, cc2);
		Assert.assertEquals(cc1.hashCode(), cc2.hashCode());
		cc2.put(KEY3, null);
		Assert.assertFalse(cc1.equals(cc2));
		Assert.assertFalse(cc1.hashCode() == cc2.hashCode());
		Assert.assertFalse(cc1.equals(null));
		Assert.assertFalse(cc1.equals(Integer.valueOf(123)));
		Assert.assertTrue(cc1.equals(cc1));
	}

	@Test
	public void testCacheContextEquals2() {
		CacheContext cc1 = new CacheContextImpl();
		CacheContext cc2 = new CacheContextImpl();
		Assert.assertEquals(cc1, cc2);
		Assert.assertEquals(cc1.hashCode(), cc2.hashCode());

		cc1.put(KEY1, this);
		cc1.put(KEY2, KEY1);
		cc1.put(KEY2, KEY2);
		cc2.put(KEY1, this);
		cc2.put(KEY2, KEY1);
		cc2.put(KEY2, KEY2);

		Assert.assertEquals(cc1, cc2);
		Assert.assertEquals(cc1.hashCode(), cc2.hashCode());
		cc2.put(KEY3, 1);
		Assert.assertFalse(cc1.equals(cc2));
		Assert.assertFalse(cc1.hashCode() == cc2.hashCode());
	}

	private class BaseExtraString {
		private String str;

		public BaseExtraString(String str) {
			this.str = str;
		}

		public String getString() {
			return str;
		}
	}

	private class ExtraString extends BaseExtraString {
		public ExtraString(String str) {
			super(str);
		}
	}

}
