/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.service.impl;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test for {@link StaticDispatcherUrlService}.
 *
 * @author Joachim Van der Auwera
 */
public class StaticDispatcherUrlServiceTest {

	private static final String TEST = "http://test/url";

	@Test
	public void testStaticDispatcherUrlService() {
		StaticDispatcherUrlService sdus = new StaticDispatcherUrlService();
		sdus.setDispatcherUrl(TEST);
		Assert.assertEquals(TEST, sdus.getDispatcherUrl());
	}
}
