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

package org.geomajas.gwt.client.gfx.paintable.mapaddon;

import org.geomajas.configuration.client.UnitType;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class ScaleBarTest {

	@Test
	public void testGetBestFitMetric() throws Exception {
		ScaleBar sb = new ScaleBar("scaleBar", null);
		sb.initialize(UnitType.METRIC, 1.0d, null);

		sb.calculateBestFit(3.779527559055119); // 1:1000 (96 dpi), best fit is 25 meters for 125 pixels
		Assert.assertTrue("should be same units", 25 == sb.getWidthInUnits());
		Assert.assertTrue("should be same units", 94 == sb.getWidthInPixels());

		sb.calculateBestFit(0.003779527559055119); // 1:1000000 (96 dpi)
		Assert.assertTrue("should be same units", 25000 == sb.getWidthInUnits());
		Assert.assertTrue("should be same units", 94 == sb.getWidthInPixels());
	}

	@Test
	public void testGetBestFitEnglish() throws Exception {
		ScaleBar sb = new ScaleBar("scaleBar", null);
		sb.initialize(UnitType.ENGLISH, 1.0d, null);

		sb.calculateBestFit(3.779527559055119); // 1:1000 (96 dpi), best fit is 25 meters for 125 pixels
		Assert.assertFalse("Should be yards", sb.isUnitWidthInMiles());
		Assert.assertTrue("should be same units", 25 == sb.getWidthInUnits());
		Assert.assertTrue("should be same pixels", 86 == sb.getWidthInPixels());

		sb.calculateBestFit(0.003779527559055119); // 1:1000000 (96 dpi)
		Assert.assertTrue("Should be yards", sb.isUnitWidthInMiles());
		Assert.assertTrue("should be same units", 10 == sb.getWidthInUnits());
		Assert.assertTrue("should be same pixels", 61 == sb.getWidthInPixels());
	}

	// gwt formatter doesn't work for unit testing (not initialized)
	@Ignore
	@Test
	public void testformatUnitsMetric() throws Exception {
		ScaleBar sb = new ScaleBar("scaleBar", null);
		sb.initialize(UnitType.METRIC, 1.0d, null);
		sb.calculateBestFit(3.779527559055119);

		Assert.assertEquals("should be same", "25 m", sb.formatUnits(25));
		Assert.assertEquals("should be same", "25.1 km", sb.formatUnits(25100));
	}
}
