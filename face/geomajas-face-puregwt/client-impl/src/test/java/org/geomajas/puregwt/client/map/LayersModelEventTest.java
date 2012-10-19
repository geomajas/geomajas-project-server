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
import java.util.List;

import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.puregwt.client.GeomajasTestModule;
import org.geomajas.puregwt.client.event.LayerAddedEvent;
import org.geomajas.puregwt.client.event.LayerDeselectedEvent;
import org.geomajas.puregwt.client.event.LayerOrderChangedEvent;
import org.geomajas.puregwt.client.event.LayerOrderChangedHandler;
import org.geomajas.puregwt.client.event.LayerRemovedEvent;
import org.geomajas.puregwt.client.event.LayerSelectedEvent;
import org.geomajas.puregwt.client.event.LayerSelectionHandler;
import org.geomajas.puregwt.client.event.MapCompositionHandler;
import org.geomajas.puregwt.client.map.layer.Layer;
import org.geomajas.testdata.ReloadContext;
import org.geomajas.testdata.ReloadContextTestExecutionListener;
import org.geomajas.testdata.ReloadContext.ClassMode;
import org.junit.Assert;
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
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * Tests for the layersModelImpl class to see if it fires the correct events.
 * 
 * @author Pieter De Graef
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml", "beansContext.xml", "mapBeans.xml",
		"layerBeans1.xml", "layerBeans2.xml", "layerBeans3.xml" })
@TestExecutionListeners(listeners = { ReloadContextTestExecutionListener.class,
		DependencyInjectionTestExecutionListener.class })
@ReloadContext(classMode=ClassMode.BEFORE_EACH_TEST_METHOD)
public class LayersModelEventTest {

	private static final Injector INJECTOR = Guice.createInjector(new GeomajasTestModule());

	private static final String LAYER1 = "beans1Layer";

	private static final String LAYER2 = "beans2Layer";

	private static final String LAYER3 = "beans3Layer";

	@Autowired
	@Qualifier(value = "mapBeans")
	private ClientMapInfo mapInfo;

	private MapEventBus eventBus;

	private ViewPort viewPort;

	private int layerCount;

	private String selectId;

	private String deselectId;

	private int fromIndex;

	private int toIndex;

	@Before
	public void checkLayerOrder() {
		eventBus = new MapEventBusImpl(this, INJECTOR.getInstance(EventBus.class));
		viewPort = INJECTOR.getInstance(ViewPort.class);

		List<ClientLayerInfo> layers = new ArrayList<ClientLayerInfo>();
		for (int i = 1; i < 4; i++) {
			for (ClientLayerInfo layerInfo : mapInfo.getLayers()) {
				if (("beans" + i + "Layer").equals(layerInfo.getId())) {
					layers.add(layerInfo);
				}
			}
		}
		mapInfo.setLayers(layers);
	}

	@Test
	public void testInitialize() {
		LayersModel layersModel = INJECTOR.getInstance(LayersModel.class);
		final MapCompositionHandler layerCounter = new MapCompositionHandler() {

			public void onLayerAdded(LayerAddedEvent event) {
				layerCount++;
			}

			public void onLayerRemoved(LayerRemovedEvent event) {
				layerCount--;
			}
		};
		eventBus.addMapCompositionHandler(layerCounter);
		layersModel.initialize(mapInfo, viewPort, eventBus);
		Assert.assertEquals(3, layerCount);
		Assert.assertEquals(3, layersModel.getLayerCount());
	}

	@Test
	public void testLayerSelection() {
		LayersModel layersModel = INJECTOR.getInstance(LayersModel.class);
		layersModel.initialize(mapInfo, viewPort, eventBus);
		Layer<?> layer1 = layersModel.getLayer(LAYER1);
		Layer<?> layer2 = layersModel.getLayer(LAYER2);

		selectId = null;
		deselectId = null;

		HandlerRegistration reg = eventBus.addLayerSelectionHandler(new LayerSelectionHandler() {

			public void onSelectLayer(LayerSelectedEvent event) {
				selectId = event.getLayer().getId();
			}

			public void onDeselectLayer(LayerDeselectedEvent event) {
				deselectId = event.getLayer().getId();
			}
		});

		layer1.setSelected(true);
		Assert.assertEquals(LAYER1, selectId);
		Assert.assertNull(deselectId);

		layer2.setSelected(true);
		Assert.assertEquals(LAYER1, deselectId);
		Assert.assertEquals(LAYER2, selectId);

		layer2.setSelected(false);
		Assert.assertEquals(LAYER2, deselectId);
		Assert.assertEquals(LAYER2, selectId);

		reg.removeHandler();
	}

	@Test
	public void testMoveLayerDown() {
		LayersModel layersModel = INJECTOR.getInstance(LayersModel.class);
		layersModel.initialize(mapInfo, viewPort, eventBus);
		Layer<?> layer1 = layersModel.getLayer(LAYER1);
		Layer<?> layer3 = layersModel.getLayer(LAYER3);

		fromIndex = 342;
		toIndex = 342;

		HandlerRegistration reg = eventBus.addLayerOrderChangedHandler(new LayerOrderChangedHandler() {

			public void onLayerOrderChanged(LayerOrderChangedEvent event) {
				fromIndex = event.getFromIndex();
				toIndex = event.getToIndex();
			}
		});

		layersModel.moveLayerDown(layer1); // Expect no changes, and so no event.
		Assert.assertEquals(342, fromIndex);
		Assert.assertEquals(342, toIndex);

		layersModel.moveLayerDown(layer3);
		Assert.assertEquals(2, fromIndex);
		Assert.assertEquals(1, toIndex);

		reg.removeHandler();
	}

	@Test
	public void testMoveLayerUp() {
		LayersModel layersModel = INJECTOR.getInstance(LayersModel.class);
		layersModel.initialize(mapInfo, viewPort, eventBus);
		Layer<?> layer1 = layersModel.getLayer(LAYER1);
		Layer<?> layer3 = layersModel.getLayer(LAYER3);

		fromIndex = 342;
		toIndex = 342;

		HandlerRegistration reg = eventBus.addLayerOrderChangedHandler(new LayerOrderChangedHandler() {

			public void onLayerOrderChanged(LayerOrderChangedEvent event) {
				fromIndex = event.getFromIndex();
				toIndex = event.getToIndex();
			}
		});

		layersModel.moveLayerUp(layer3); // Expect no changes, and so no event.
		Assert.assertEquals(342, fromIndex);
		Assert.assertEquals(342, toIndex);

		layersModel.moveLayerUp(layer1);
		Assert.assertEquals(0, fromIndex);
		Assert.assertEquals(1, toIndex);

		reg.removeHandler();
	}

	@Test
	public void testMoveLayer() {
		LayersModel layersModel = INJECTOR.getInstance(LayersModel.class);
		layersModel.initialize(mapInfo, viewPort, eventBus);

		Layer<?> layer1 = layersModel.getLayer(LAYER1);
		Layer<?> layer2 = layersModel.getLayer(LAYER2);
		Layer<?> layer3 = layersModel.getLayer(LAYER3);

		fromIndex = 342;
		toIndex = 342;

		HandlerRegistration reg = eventBus.addLayerOrderChangedHandler(new LayerOrderChangedHandler() {

			public void onLayerOrderChanged(LayerOrderChangedEvent event) {
				fromIndex = event.getFromIndex();
				toIndex = event.getToIndex();
			}
		});

		layersModel.moveLayer(layer1, -1); // Expect no changes, and so no event.
		Assert.assertEquals(342, fromIndex);
		Assert.assertEquals(342, toIndex);

		layersModel.moveLayer(layer2, -1);
		Assert.assertEquals(1, fromIndex);
		Assert.assertEquals(0, toIndex);

		layersModel.moveLayer(layer2, 2);
		Assert.assertEquals(0, fromIndex);
		Assert.assertEquals(2, toIndex);

		layersModel.moveLayer(layer2, 200); // Expect no changes.
		Assert.assertEquals(0, fromIndex);
		Assert.assertEquals(2, toIndex);

		layersModel.moveLayer(layer3, 0);
		Assert.assertEquals(1, fromIndex);
		Assert.assertEquals(0, toIndex);

		fromIndex = 342;
		toIndex = 342;

		// Corner case - move to same position. We don't expect an event.
		layersModel.moveLayer(layer3, 0);
		Assert.assertEquals(342, fromIndex);
		Assert.assertEquals(342, toIndex);

		reg.removeHandler();
	}

	@Test
	public void testLayerRemoval() {
		LayersModel layersModel = INJECTOR.getInstance(LayersModel.class);
		layersModel.initialize(mapInfo, viewPort, eventBus);

		layerCount = layersModel.getLayerCount();
		Assert.assertEquals(3, layerCount);

		final MapCompositionHandler layerCounter = new MapCompositionHandler() {

			public void onLayerAdded(LayerAddedEvent event) {
				layerCount++;
			}

			public void onLayerRemoved(LayerRemovedEvent event) {
				layerCount--;
			}
		};
		HandlerRegistration reg = eventBus.addMapCompositionHandler(layerCounter);

		layersModel.removeLayer(LAYER1);
		Assert.assertEquals(2, layerCount);
		layersModel.removeLayer(LAYER3);
		Assert.assertEquals(1, layerCount);
		layersModel.removeLayer(LAYER2);
		Assert.assertEquals(0, layerCount);

		// Corner cases:
		Assert.assertFalse(layersModel.removeLayer("this-layer-does-not-exist"));
		try {
			layersModel.removeLayer(null);
			Assert.fail();
		} catch (Exception e) {
			// Test passed.
		}

		reg.removeHandler();
	}
}