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
