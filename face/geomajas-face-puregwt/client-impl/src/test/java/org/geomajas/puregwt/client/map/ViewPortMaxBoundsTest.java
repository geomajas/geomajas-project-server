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

import javax.annotation.PostConstruct;

import junit.framework.Assert;

import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.puregwt.client.GeomajasTestModule;
import org.geomajas.testdata.ReloadContext;
import org.geomajas.testdata.ReloadContextTestExecutionListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.web.bindery.event.shared.EventBus;

@TestExecutionListeners(listeners = { ReloadContextTestExecutionListener.class,
		DependencyInjectionTestExecutionListener.class })
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml", "viewPortContext.xml",
		"mapViewPortBeans.xml", "mapBeansNoResolutions.xml", "layerViewPortBeans.xml" })
@ReloadContext
public class ViewPortMaxBoundsTest {

	private static final Injector INJECTOR = Guice.createInjector(new GeomajasTestModule());

	@Autowired
	@Qualifier(value = "mapViewPortBeans")
	private ClientMapInfo mapInfo;

	private MapEventBus eventBus;

	private ViewPort viewPort;

	@PostConstruct
	public void initialize() {
		eventBus = new MapEventBusImpl(this, INJECTOR.getInstance(EventBus.class));
		viewPort = INJECTOR.getInstance(ViewPort.class);
		viewPort.initialize(mapInfo, eventBus);
		viewPort.setMapSize(1000, 1000);
	}

	@Test
	@ReloadContext
	public void testInitialBounds() {
		viewPort.initialize(mapInfo, eventBus);
		Bbox maxBounds = viewPort.getMaximumBounds();
		Assert.assertEquals(-100.0, maxBounds.getX());
		Assert.assertEquals(-100.0, maxBounds.getY());
		Assert.assertEquals(100.0, maxBounds.getMaxX());
		Assert.assertEquals(100.0, maxBounds.getMaxY());
	}

	@Test
	@ReloadContext
	public void testSetMaxBounds() {
		mapInfo.setMaxBounds(new org.geomajas.geometry.Bbox(0, 0, 10, 10));
		viewPort.initialize(mapInfo, eventBus);
		Bbox maxBounds = viewPort.getMaximumBounds();
		Assert.assertEquals(0.0, maxBounds.getX());
		Assert.assertEquals(0.0, maxBounds.getY());
		Assert.assertEquals(10.0, maxBounds.getMaxX());
		Assert.assertEquals(10.0, maxBounds.getMaxY());
	}

	@Test
	@ReloadContext
	public void testLayerUnion() {
		mapInfo.setMaxBounds(org.geomajas.geometry.Bbox.ALL);
		mapInfo.getLayers().get(0).setMaxExtent(new org.geomajas.geometry.Bbox(0, 0, 500, 500));
		viewPort.initialize(mapInfo, eventBus);
		Bbox maxBounds = viewPort.getMaximumBounds();
		Assert.assertEquals(-100.0, maxBounds.getX());
		Assert.assertEquals(-100.0, maxBounds.getY());
		Assert.assertEquals(500.0, maxBounds.getMaxX());
		Assert.assertEquals(500.0, maxBounds.getMaxY());
	}
}