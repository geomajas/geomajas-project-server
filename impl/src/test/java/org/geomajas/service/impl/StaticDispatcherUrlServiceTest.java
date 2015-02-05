/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
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

	private static final String LOCAL = "http://localhost:8080/url";

	private static final String EXT1 = "http://test/url/something";

	private static final String LOCAL1 = "http://localhost:8080/url/something";

	private static final String BAD = "http://other/url/something";

	@Test
	public void testStaticDispatcherUrlService() {
		StaticDispatcherUrlService sdus = new StaticDispatcherUrlService();
		sdus.setDispatcherUrl(TEST);
		Assert.assertEquals(TEST, sdus.getDispatcherUrl());
	}
	
	@Test
	public void testLocalize() {
		StaticDispatcherUrlService sdus = new StaticDispatcherUrlService();
		sdus.setDispatcherUrl(TEST);
		sdus.setLocalDispatcherUrl(LOCAL);
		Assert.assertEquals(LOCAL, sdus.getLocalDispatcherUrl());
		Assert.assertEquals(LOCAL1, sdus.localize(EXT1));
		Assert.assertEquals(BAD, sdus.localize(BAD));
	}
}
