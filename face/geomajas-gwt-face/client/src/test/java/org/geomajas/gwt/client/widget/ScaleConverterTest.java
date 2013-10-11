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

package org.geomajas.gwt.client.widget;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Test for {@link ScaleConverter}.
 *
 * @author Joachim Van der Auwera
 */
public class ScaleConverterTest {

	@Test
	public void testRoundPrecision() {
		Assert.assertEquals(12345678, ScaleConverter.round(12345678, 0, 0));
		Assert.assertEquals(12345680, ScaleConverter.round(12345678, 10, 0));
		Assert.assertEquals(12345680, ScaleConverter.round(12345678, 20, 0));
		Assert.assertEquals(12345700, ScaleConverter.round(12345678, 100, 0));
		Assert.assertEquals(12346000, ScaleConverter.round(12345678, 1000, 0));
		Assert.assertEquals(12350000, ScaleConverter.round(12345678, 10000, 0));
		Assert.assertEquals(87654321, ScaleConverter.round(87654321, 0, 0));
		Assert.assertEquals(87654320, ScaleConverter.round(87654321, 10, 0));
		Assert.assertEquals(87654320, ScaleConverter.round(87654321, 20, 0));
		Assert.assertEquals(87654300, ScaleConverter.round(87654321, 100, 0));
		Assert.assertEquals(87654000, ScaleConverter.round(87654321, 1000, 0));
		Assert.assertEquals(87650000, ScaleConverter.round(87654321, 10000, 0));
	}

	@Test
	public void testRoundSignificantDigits() {
		Assert.assertEquals(12345678, ScaleConverter.round(12345678, 0, 0));
		Assert.assertEquals(10000000, ScaleConverter.round(12345678, 0, 1));
		Assert.assertEquals(12000000, ScaleConverter.round(12345678, 0, 2));
		Assert.assertEquals(12300000, ScaleConverter.round(12345678, 0, 3));
		Assert.assertEquals(12350000, ScaleConverter.round(12345678, 0, 4));
		Assert.assertEquals(12346000, ScaleConverter.round(12345678, 0, 5));
		Assert.assertEquals(12345700, ScaleConverter.round(12345678, 0, 6));
	}
}
