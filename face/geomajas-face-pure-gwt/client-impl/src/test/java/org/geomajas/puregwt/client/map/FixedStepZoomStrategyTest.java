/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.puregwt.client.map;

import java.util.ArrayList;

import javax.annotation.PostConstruct;

import junit.framework.Assert;

import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ScaleInfo;
import org.geomajas.puregwt.client.map.ZoomStrategy.ZoomOption;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test case that tests the FreeForAll ZoomStrategy.
 * 
 * @author Pieter De Graef
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml", "viewPortContext.xml",
		"mapViewPortBeans.xml", "mapBeansNoResolutions.xml", "layerViewPortBeans.xml" })
public class FixedStepZoomStrategyTest {

	private static final double[] SCALES = new double[] { 8.0, 4.0, 2.0, 1.0 };

	@Autowired
	@Qualifier(value = "mapViewPortBeans")
	private ClientMapInfo mapInfo;

	private ZoomStrategy zoomStrategy;

	@PostConstruct
	public void initialize() {
		zoomStrategy = new FixedStepZoomStrategy(mapInfo, mapInfo.getMaxBounds());
		zoomStrategy.setMapSize(100, 100);
	}

	@Test
	public void testMaximumScale() {
		Assert.assertEquals(SCALES[0], zoomStrategy.getMaximumScale());
	}

	@Test
	public void testMinimumScale() {
		// Should be the minimum ratio (hor/vert) of the map width/height divided by the maxbounds width/height.
		Assert.assertEquals(SCALES[3], zoomStrategy.getMinimumScale());

		// Change map size - this does NOT change the minimum scale:
		zoomStrategy.setMapSize(120, 120);
		Assert.assertEquals(SCALES[3], zoomStrategy.getMinimumScale());

		// Reset map size:
		zoomStrategy.setMapSize(100, 100);
	}

	@Test
	public void testZoomStepCount() {
		// Allowed scales: 0.5, 1, 2, 4. Count = 4.
		Assert.assertEquals(4, zoomStrategy.getZoomStepCount());

		// Change map size. Allowed scales remain the same...
		zoomStrategy.setMapSize(120, 120);
		Assert.assertEquals(4, zoomStrategy.getZoomStepCount());

		// Reset map size:
		zoomStrategy.setMapSize(100, 100);
	}

	@Test
	public void testZoomStepScale() {
		try {
			zoomStrategy.getZoomStepScale(-1);
			Assert.fail(); // We should not get here...
		} catch (Exception e) {
			// Test passed!
		}

		try {
			zoomStrategy.getZoomStepScale(Integer.MAX_VALUE);
			Assert.fail(); // We should not get here...
		} catch (Exception e) {
			// Test passed!
		}

		Assert.assertEquals(SCALES[0], zoomStrategy.getZoomStepScale(0));
		Assert.assertEquals(SCALES[1], zoomStrategy.getZoomStepScale(1));
		Assert.assertEquals(SCALES[2], zoomStrategy.getZoomStepScale(2));
		Assert.assertEquals(SCALES[3], zoomStrategy.getZoomStepScale(3));

		// Change map size. Allowed scales: 0.6, 1.2, 2.4 (4.8 is larger than maximum allowed scale).
		zoomStrategy.setMapSize(120, 120);
		Assert.assertEquals(SCALES[0], zoomStrategy.getZoomStepScale(0));
		Assert.assertEquals(SCALES[1], zoomStrategy.getZoomStepScale(1));
		Assert.assertEquals(SCALES[2], zoomStrategy.getZoomStepScale(2));
		Assert.assertEquals(SCALES[3], zoomStrategy.getZoomStepScale(3));

		// Reset map size:
		zoomStrategy.setMapSize(100, 100);
	}

	@Test
	public void testZoomStepIndex() {
		for (int i = 0; i < 4; i++) {
			Assert.assertEquals(i, zoomStrategy.getZoomStepIndex(SCALES[i]));
		}
		Assert.assertEquals(0, zoomStrategy.getZoomStepIndex(8.5));
		Assert.assertEquals(0, zoomStrategy.getZoomStepIndex(7.0));
		Assert.assertEquals(1, zoomStrategy.getZoomStepIndex(4.1));
		Assert.assertEquals(1, zoomStrategy.getZoomStepIndex(3.9));
		Assert.assertEquals(2, zoomStrategy.getZoomStepIndex(3.0)); // Perfectly half-way. Prefer the zoomed out step.
		Assert.assertEquals(2, zoomStrategy.getZoomStepIndex(2.9));
		Assert.assertEquals(2, zoomStrategy.getZoomStepIndex(2.1));
		Assert.assertEquals(3, zoomStrategy.getZoomStepIndex(1.1));
		Assert.assertEquals(3, zoomStrategy.getZoomStepIndex(0.9));
		Assert.assertEquals(3, zoomStrategy.getZoomStepIndex(-1.0));
	}

	@Test
	public void testCheckScale() {
		Assert.assertEquals(SCALES[3], zoomStrategy.checkScale(0, ZoomOption.LEVEL_FIT));
		Assert.assertEquals(SCALES[3], zoomStrategy.checkScale(SCALES[3] - 0.001, ZoomOption.LEVEL_FIT));
		Assert.assertEquals(SCALES[3], zoomStrategy.checkScale(SCALES[2] - 0.001, ZoomOption.LEVEL_FIT));
		Assert.assertEquals(SCALES[2], zoomStrategy.checkScale(SCALES[1] - 0.001, ZoomOption.LEVEL_FIT));
		Assert.assertEquals(SCALES[1], zoomStrategy.checkScale(SCALES[0] - 0.001, ZoomOption.LEVEL_FIT));
		
		Assert.assertEquals(SCALES[3], zoomStrategy.checkScale(0, ZoomOption.LEVEL_CLOSEST));
		Assert.assertEquals(SCALES[3], zoomStrategy.checkScale(SCALES[3] - 0.001, ZoomOption.LEVEL_CLOSEST));
		Assert.assertEquals(SCALES[2], zoomStrategy.checkScale(SCALES[2] - 0.001, ZoomOption.LEVEL_CLOSEST));
		Assert.assertEquals(SCALES[1], zoomStrategy.checkScale(SCALES[1] - 0.001, ZoomOption.LEVEL_CLOSEST));
		Assert.assertEquals(SCALES[0], zoomStrategy.checkScale(SCALES[0] - 0.001, ZoomOption.LEVEL_CLOSEST));
	}

	@Test
	@DirtiesContext
	public void testFaultyConfiguration() {
		// Null value for scale levels:
		mapInfo.getScaleConfiguration().setZoomLevels(null);
		try {
			zoomStrategy = new FixedStepZoomStrategy(mapInfo, mapInfo.getMaxBounds());
			Assert.fail();
		} catch (IllegalArgumentException e) {
			// Expected exception.
		}

		// Empty list of scale levels:
		mapInfo.getScaleConfiguration().setZoomLevels(new ArrayList<ScaleInfo>());
		try {
			zoomStrategy = new FixedStepZoomStrategy(mapInfo, mapInfo.getMaxBounds());
			Assert.fail();
		} catch (IllegalArgumentException e) {
			// Expected exception.
		}
	}
}