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

package org.geomajas.puregwt.client.map.layer;

import junit.framework.Assert;

import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.puregwt.client.GeomajasTestModule;
import org.geomajas.puregwt.client.map.MapEventBus;
import org.geomajas.puregwt.client.map.MapEventBusImpl;
import org.geomajas.puregwt.client.map.ViewPort;
import org.geomajas.puregwt.client.service.EndPointService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.web.bindery.event.shared.EventBus;

/**
 * Test-cases for the {@link VectorServerLayerImpl} class.
 * 
 * @author Pieter De Graef
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml", "layerBeans1.xml", "mapBeans.xml" })
public class VectorLayerTest {

	private static final Injector INJECTOR = Guice.createInjector(new GeomajasTestModule());

	@Autowired
	@Qualifier(value = "mapBeans")
	private ClientMapInfo mapInfo;

	private ClientVectorLayerInfo layerInfo;

	private MapEventBus eventBus;

	private ViewPort viewPort;
	
	private EndPointService endPointService;

	@Before
	public void checkLayerOrder() {
		eventBus = new MapEventBusImpl(this, INJECTOR.getInstance(EventBus.class));
		viewPort = INJECTOR.getInstance(ViewPort.class);
		viewPort.initialize(mapInfo, eventBus);
		viewPort.setMapSize(1000, 1000);
		endPointService  = INJECTOR.getInstance(EndPointService.class);
		layerInfo = (ClientVectorLayerInfo) mapInfo.getLayers().get(0);
	}

	@Test
	public void testServerLayerId() {
		VectorServerLayerImpl layer = new VectorServerLayerImpl(layerInfo, viewPort, eventBus, endPointService);
		Assert.assertEquals(layerInfo.getServerLayerId(), layer.getServerLayerId());
	}

	@Test
	public void testTitle() {
		VectorServerLayerImpl layer = new VectorServerLayerImpl(layerInfo, viewPort, eventBus, endPointService);
		Assert.assertEquals(layerInfo.getLabel(), layer.getTitle());
	}

	@Test
	public void testLayerInfo() {
		VectorServerLayerImpl layer = new VectorServerLayerImpl(layerInfo, viewPort, eventBus, endPointService);
		Assert.assertEquals(layerInfo, layer.getLayerInfo());
	}

	@Test
	public void testSelection() {
		VectorServerLayerImpl layer = new VectorServerLayerImpl(layerInfo, viewPort, eventBus, endPointService);
		Assert.assertFalse(layer.isSelected());
		layer.setSelected(true);
		Assert.assertTrue(layer.isSelected());
		layer.setSelected(false);
		Assert.assertFalse(layer.isSelected());
	}

	@Test
	public void testMarkedAsVisible() {
		VectorServerLayerImpl layer = new VectorServerLayerImpl(layerInfo, viewPort, eventBus, endPointService);
		Assert.assertTrue(layer.isMarkedAsVisible());
		layer.setMarkedAsVisible(false);
		Assert.assertFalse(layer.isMarkedAsVisible());
		layer.setMarkedAsVisible(true);
		Assert.assertTrue(layer.isMarkedAsVisible());
	}

	@Test
	public void testShowing() {
		VectorServerLayerImpl layer = new VectorServerLayerImpl(layerInfo, viewPort, eventBus, endPointService);

		// Scale between 6 and 20 is OK:
		viewPort.applyScale(viewPort.getZoomStrategy().getZoomStepScale(0)); // 32 -> NOK
		Assert.assertFalse(layer.isShowing());

		viewPort.applyScale(viewPort.getZoomStrategy().getZoomStepScale(1)); // 16 -> OK
		Assert.assertTrue(layer.isShowing());

		viewPort.applyScale(viewPort.getZoomStrategy().getZoomStepScale(2)); // 8 -> OK
		Assert.assertTrue(layer.isShowing());

		viewPort.applyScale(viewPort.getZoomStrategy().getZoomStepScale(3)); // 4 -> NOK
		Assert.assertFalse(layer.isShowing());

		viewPort.applyScale(viewPort.getZoomStrategy().getZoomStepScale(4)); // 2 -> NOK
		Assert.assertFalse(layer.isShowing());

		viewPort.applyScale(viewPort.getZoomStrategy().getZoomStepScale(5)); // 1 -> NOK
		Assert.assertFalse(layer.isShowing());

		// Mark as invisible:
		layer.setMarkedAsVisible(false);
		viewPort.applyScale(viewPort.getZoomStrategy().getZoomStepScale(0)); // 32 -> NOK
		Assert.assertFalse(layer.isShowing());

		viewPort.applyScale(viewPort.getZoomStrategy().getZoomStepScale(1)); // 16 -> NOK
		Assert.assertFalse(layer.isShowing());
	}

	@Test
	public void testFilter() {
		VectorServerLayerImpl layer = new VectorServerLayerImpl(layerInfo, viewPort, eventBus, endPointService);
		Assert.assertNull(layer.getFilter());
		String filter = "Look at me, mom!";
		layer.setFilter(filter);
		Assert.assertEquals(filter, layer.getFilter());
	}
}