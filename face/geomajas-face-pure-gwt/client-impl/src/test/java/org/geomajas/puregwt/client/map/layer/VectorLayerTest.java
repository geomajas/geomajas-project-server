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

package org.geomajas.puregwt.client.map.layer;

import junit.framework.Assert;

import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.puregwt.client.GeomajasTestModule;
import org.geomajas.puregwt.client.map.ViewPort;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.web.bindery.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Test-cases for the {@link VectorLayer} class.
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

	private EventBus eventBus;

	private ViewPort viewPort;

	@Before
	public void checkLayerOrder() {
		eventBus = new SimpleEventBus();
		viewPort = INJECTOR.getInstance(ViewPort.class);
		viewPort.initialize(mapInfo, eventBus);
		viewPort.setMapSize(1000, 1000);
		layerInfo = (ClientVectorLayerInfo) mapInfo.getLayers().get(0);
	}

	@Test
	public void testServerLayerId() {
		VectorLayer layer = new VectorLayer(layerInfo, viewPort, eventBus);
		Assert.assertEquals(layerInfo.getServerLayerId(), layer.getServerLayerId());
	}

	@Test
	public void testTitle() {
		VectorLayer layer = new VectorLayer(layerInfo, viewPort, eventBus);
		Assert.assertEquals(layerInfo.getLabel(), layer.getTitle());
	}

	@Test
	public void testLayerInfo() {
		VectorLayer layer = new VectorLayer(layerInfo, viewPort, eventBus);
		Assert.assertEquals(layerInfo, layer.getLayerInfo());
	}

	@Test
	public void testSelection() {
		VectorLayer layer = new VectorLayer(layerInfo, viewPort, eventBus);
		Assert.assertFalse(layer.isSelected());
		layer.setSelected(true);
		Assert.assertTrue(layer.isSelected());
		layer.setSelected(false);
		Assert.assertFalse(layer.isSelected());
	}

	@Test
	public void testMarkedAsVisible() {
		VectorLayer layer = new VectorLayer(layerInfo, viewPort, eventBus);
		Assert.assertTrue(layer.isMarkedAsVisible());
		layer.setMarkedAsVisible(false);
		Assert.assertFalse(layer.isMarkedAsVisible());
		layer.setMarkedAsVisible(true);
		Assert.assertTrue(layer.isMarkedAsVisible());
	}

	@Test
	public void testShowing() {
		VectorLayer layer = new VectorLayer(layerInfo, viewPort, eventBus);

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
		VectorLayer layer = new VectorLayer(layerInfo, viewPort, eventBus);
		Assert.assertNull(layer.getFilter());
		String filter = "Look at me, mom!";
		layer.setFilter(filter);
		Assert.assertEquals(filter, layer.getFilter());
	}
}