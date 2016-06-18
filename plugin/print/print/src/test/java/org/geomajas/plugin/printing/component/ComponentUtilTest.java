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

package org.geomajas.plugin.printing.component;

import junit.framework.Assert;
import org.apache.commons.lang.LocaleUtils;
import org.junit.Test;

import java.util.Locale;
import java.util.ResourceBundle;

public class ComponentUtilTest {

	@Test
	public void testGetResourceBundleEn() throws Exception {
		String locale = "en";
		ResourceBundle resourceBundle = ComponentUtil.getCurrentResourceBundle(locale);
		Assert.assertEquals(locale.toLowerCase(), resourceBundle.getLocale().toString().toLowerCase());
	}

	@Test
	public void testGetResourceBundleNl() throws Exception {
		String locale = "nl";
		ResourceBundle resourceBundle = ComponentUtil.getCurrentResourceBundle(locale);
		Assert.assertEquals(locale.toLowerCase(), resourceBundle.getLocale().toString().toLowerCase());
	}

	@Test
	public void testGetResourceBundleFallbackOnLanguage() throws Exception {
		String locale = "en_GB";
		ResourceBundle resourceBundle = ComponentUtil.getCurrentResourceBundle(locale);
		Assert.assertEquals("en", resourceBundle.getLocale().toString().toLowerCase());
	}

	@Test
	public void testGetResourceBundleEnUs() throws Exception {
		String locale = "en_US";
		ResourceBundle resourceBundle = ComponentUtil.getCurrentResourceBundle(locale);
		Assert.assertEquals(locale.toLowerCase(), resourceBundle.getLocale().toString().toLowerCase());
	}

	@Test
	public void testGetResourceBundleUnknown() throws Exception {
		String locale = "xyz";
		String[] defaultLocaleArray = new String[] {"en", "nl"};
		for (String defaultLocale : defaultLocaleArray) {
			Locale.setDefault(LocaleUtils.toLocale(defaultLocale));
			ResourceBundle resourceBundle = ComponentUtil.getCurrentResourceBundle(locale);
			Assert.assertEquals(defaultLocale.toLowerCase(), resourceBundle.getLocale().toString().toLowerCase());
		}
	}
}
