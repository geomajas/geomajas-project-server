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

package org.geomajas.configuration.client;

import junit.framework.Assert;
import org.junit.Test;

/**
 * Tests for ScaleInfo.
 *
 * @author Joachim Van der Auwera
 */
public class ScaleInfoTest {

	private static final double DELTA = 1e-50;

	@Test
	public void testNoZeroPixelsPerUnit() {
		ScaleInfo scaleInfo;
		scaleInfo = new ScaleInfo();
		Assert.assertTrue(scaleInfo.getPixelPerUnit() > 0);
		scaleInfo = new ScaleInfo(0);
		Assert.assertTrue(scaleInfo.getPixelPerUnit() > 0);
	}

	@Test
	public void testPixelsPerUnitRange() {
		ScaleInfo scaleInfo;
		scaleInfo = new ScaleInfo(-100);
		Assert.assertEquals(ScaleInfo.MINIMUM_PIXEL_PER_UNIT, scaleInfo.getPixelPerUnit(), DELTA);
		scaleInfo = new ScaleInfo(1e100);
		Assert.assertEquals(ScaleInfo.MAXIMUM_PIXEL_PER_UNIT, scaleInfo.getPixelPerUnit(), DELTA);
	}
}
