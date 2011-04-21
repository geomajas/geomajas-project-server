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

package org.geomajas.puregwt.client.map;

import javax.annotation.PostConstruct;

import junit.framework.Assert;

import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.puregwt.client.map.event.EventBus;
import org.geomajas.puregwt.client.map.event.EventBusImpl;
import org.geomajas.puregwt.client.spatial.Bbox;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml", "viewPortContext.xml",
		"mapViewPortBeans.xml", "mapBeansNoResolutions.xml", "layerViewPortBeans.xml" })
@DirtiesContext
public class ViewPortMaxBoundsTest {

	@Autowired
	@Qualifier(value = "mapViewPortBeans")
	private ClientMapInfo mapInfo;

	private EventBus eventBus;

	private ViewPort viewPort;

	@PostConstruct
	public void initialize() {
		eventBus = new EventBusImpl();
		viewPort = new ViewPortImpl(eventBus);
		viewPort.setMapSize(1000, 1000);
	}

	@Test
	public void testInitialBounds() {
		viewPort.initialize(mapInfo);
		Bbox maxBounds = viewPort.getMaximumBounds();
		Assert.assertEquals(maxBounds.getX(), -100.0);
		Assert.assertEquals(maxBounds.getY(), -100.0);
		Assert.assertEquals(maxBounds.getMaxX(), 100.0);
		Assert.assertEquals(maxBounds.getMaxY(), 100.0);
	}

	@Test
	public void testSetMaxBounds() {
		mapInfo.setMaxBounds(new org.geomajas.geometry.Bbox(0, 0, 10, 10));
		viewPort.initialize(mapInfo);
		Bbox maxBounds = viewPort.getMaximumBounds();
		Assert.assertEquals(maxBounds.getX(), 0.0);
		Assert.assertEquals(maxBounds.getY(), 0.0);
		Assert.assertEquals(maxBounds.getMaxX(), 10.0);
		Assert.assertEquals(maxBounds.getMaxY(), 10.0);
	}

	@Test
	public void testLayerUnion() {
		mapInfo.setMaxBounds(org.geomajas.geometry.Bbox.ALL);
		mapInfo.getLayers().get(0).setMaxExtent(new org.geomajas.geometry.Bbox(0, 0, 500, 500));
		viewPort.initialize(mapInfo);
		Bbox maxBounds = viewPort.getMaximumBounds();
		Assert.assertEquals(maxBounds.getX(), -100.0);
		Assert.assertEquals(maxBounds.getY(), -100.0);
		Assert.assertEquals(maxBounds.getMaxX(), 500.0);
		Assert.assertEquals(maxBounds.getMaxY(), 500.0);
	}
}