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
import org.geomajas.geometry.Coordinate;
import org.geomajas.puregwt.client.GeomajasTestModule;
import org.geomajas.testdata.ReloadContext;
import org.geomajas.testdata.ReloadContextTestExecutionListener;
import org.junit.Before;
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

/**
 * Unit test that checks if the ViewPortImpl positions correctly.
 * 
 * @author Pieter De Graef
 */
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class,
		ReloadContextTestExecutionListener.class })
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml", "viewPortContext.xml",
		"mapViewPortBeans.xml", "mapBeansNoResolutions.xml", "layerViewPortBeans.xml" })
@ReloadContext
public class ViewPortPositionTest {

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
		viewPort.setMapSize(200, 200);
	}

	@Before
	public void prepareTest() {
		viewPort.applyBounds(viewPort.getMaximumBounds());
	}

	@Test
	public void testOnMinimumScale() {
		Assert.assertEquals(0.0, viewPort.getPosition().getX());
		Assert.assertEquals(0.0, viewPort.getPosition().getY());

		viewPort.applyPosition(new Coordinate(10, 10));
		Assert.assertEquals(0.0, viewPort.getPosition().getX());
		Assert.assertEquals(0.0, viewPort.getPosition().getY());
	}

	@Test
	public void testOnAverageScale() {
		Assert.assertEquals(0.0, viewPort.getPosition().getX());
		Assert.assertEquals(0.0, viewPort.getPosition().getY());

		viewPort.applyScale(2.0); // Width now becomes 100, so max center = (50,50).
		Assert.assertEquals(0.0, viewPort.getPosition().getX());
		Assert.assertEquals(0.0, viewPort.getPosition().getY());

		viewPort.applyPosition(new Coordinate(10, 10));
		Assert.assertEquals(10.0, viewPort.getPosition().getX());
		Assert.assertEquals(10.0, viewPort.getPosition().getY());

		viewPort.applyPosition(new Coordinate(1000, 1000));
		Assert.assertEquals(50.0, viewPort.getPosition().getX());
		Assert.assertEquals(50.0, viewPort.getPosition().getY());
	}
}