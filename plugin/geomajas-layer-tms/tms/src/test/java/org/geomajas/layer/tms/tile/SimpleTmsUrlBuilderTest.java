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

package org.geomajas.layer.tms.tile;

import junit.framework.Assert;

import org.geomajas.layer.tile.TileCode;
import org.junit.Test;

/**
 * Test cases to test the simple URL builder for TMS.
 * 
 * @author Pieter De Graef
 */
public class SimpleTmsUrlBuilderTest {

	private static final String BASE_TMS_URL = "http://www.geomajas.org/tms/some_layer";

	private static final String EXTENSION = "png";

	private static final String EXPECTED = "http://www.geomajas.org/tms/some_layer/1/2/3.png";

	@Test
	public void testBuildUrl1() {
		TileUrlBuilder builder = new SimpleTmsUrlBuilder(EXTENSION);
		String url = builder.buildUrl(new TileCode(1, 2, 3), BASE_TMS_URL);
		Assert.assertEquals(EXPECTED, url);
	}

	@Test
	public void testBuildUrl2() {
		TileUrlBuilder builder = new SimpleTmsUrlBuilder(EXTENSION);
		String url = builder.buildUrl(new TileCode(1, 2, 3), BASE_TMS_URL + "/");
		Assert.assertEquals(EXPECTED, url);
	}

	@Test
	public void testBuildUrl3() {
		TileUrlBuilder builder = new SimpleTmsUrlBuilder("." + EXTENSION);
		String url = builder.buildUrl(new TileCode(1, 2, 3), BASE_TMS_URL + "/");
		Assert.assertEquals(EXPECTED, url);
	}

	@Test
	public void testBuildUrlCornerCases() {
		try {
			new SimpleTmsUrlBuilder(null);
			Assert.fail();
		} catch (IllegalStateException e) {
			// As expected...
		}
		try {
			TileUrlBuilder builder = new SimpleTmsUrlBuilder("." + EXTENSION);
			builder.buildUrl(null, "");
			Assert.fail();
		} catch (NullPointerException e) {
			// As expected...
		}
		try {
			TileUrlBuilder builder = new SimpleTmsUrlBuilder("." + EXTENSION);
			builder.buildUrl(new TileCode(), null);
			Assert.fail();
		} catch (NullPointerException e) {
			// As expected...
		}
	}
}