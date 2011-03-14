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

import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.puregwt.client.event.EventBus;
import org.geomajas.puregwt.client.event.EventBusImpl;
import org.geomajas.puregwt.client.map.event.LayerAddedEvent;
import org.geomajas.puregwt.client.map.event.LayerRemovedEvent;
import org.geomajas.puregwt.client.map.event.MapCompositionHandler;
import org.geomajas.puregwt.client.map.layer.Layer;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Tests for the layersModelImpl class to see if it correctly implements all layersModel methods.
 * 
 * @author Pieter De Graef
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml", "applicationContext.xml", "mapOsm.xml",
		"layerOsm1.xml", "layerOsm2.xml", "layerOsm3.xml", "layerOsm4.xml" })
public class LayersModelTest {

	@Autowired
	@Qualifier(value = "mapTest")
	private ClientMapInfo mapInfo;

	private EventBus eventBus = new EventBusImpl();

	private int layerCount;

	@Test
	public void testInitialize() {
		LayersModel layersModel = new LayersModelImpl(eventBus);
		final MapCompositionHandler layerCounter = new MapCompositionHandler() {

			public void onLayerAdded(LayerAddedEvent event) {
				layerCount++;
			}

			public void onLayerRemoved(LayerRemovedEvent event) {
			}
		};
		eventBus.addHandler(MapCompositionHandler.TYPE, layerCounter);
		layersModel.initialize(mapInfo, new ViewPortImpl(eventBus));
		Assert.assertEquals(3, layerCount);
		Assert.assertEquals(3, layersModel.getLayerCount());
	}

	@Test
	public void testLayerSelection() {
		LayersModel layersModel = new LayersModelImpl(eventBus);
		layersModel.initialize(mapInfo, new ViewPortImpl(eventBus));
		Layer<?> layer1 = layersModel.getLayer("osm1Layer");
		Layer<?> layer2 = layersModel.getLayer("osm2Layer");

		layer1.setSelected(true);
		Assert.assertEquals(layer1, layersModel.getSelectedLayer());

		layer2.setSelected(true);
		Assert.assertEquals(layer2, layersModel.getSelectedLayer());
		Assert.assertEquals(false, layer1.isSelected());

		layer2.setSelected(false);
		Assert.assertEquals(null, layersModel.getSelectedLayer());
	}

	@Test
	public void testGetLayer() {
		LayersModel layersModel = new LayersModelImpl(eventBus);
		layersModel.initialize(mapInfo, new ViewPortImpl(eventBus));

		Assert.assertEquals(layersModel.getLayer(0), layersModel.getLayer("osm1Layer"));
		Assert.assertEquals(layersModel.getLayer(1), layersModel.getLayer("osm2Layer"));
		Assert.assertEquals(layersModel.getLayer(2), layersModel.getLayer("osm3Layer"));
	}

	@Test
	public void testLayerPosition() {
		LayersModel layersModel = new LayersModelImpl(eventBus);
		layersModel.initialize(mapInfo, new ViewPortImpl(eventBus));

		Assert.assertEquals(0, layersModel.getLayerPosition(layersModel.getLayer(0)));
		Assert.assertEquals(1, layersModel.getLayerPosition(layersModel.getLayer(1)));
		Assert.assertEquals(2, layersModel.getLayerPosition(layersModel.getLayer(2)));

		Assert.assertEquals(0, layersModel.getLayerPosition(layersModel.getLayer("osm1Layer")));
		Assert.assertEquals(1, layersModel.getLayerPosition(layersModel.getLayer("osm2Layer")));
		Assert.assertEquals(2, layersModel.getLayerPosition(layersModel.getLayer("osm3Layer")));
	}

	@Test
	public void testMoveLayerDown() {
		LayersModel layersModel = new LayersModelImpl(eventBus);
		layersModel.initialize(mapInfo, new ViewPortImpl(eventBus));

		Layer<?> layer1 = layersModel.getLayer("osm1Layer");
		Layer<?> layer2 = layersModel.getLayer("osm2Layer");
		Layer<?> layer3 = layersModel.getLayer("osm3Layer");

		layersModel.moveLayerDown(layer1); // Expect no changes.
		Assert.assertEquals(0, layersModel.getLayerPosition(layer1));
		Assert.assertEquals(1, layersModel.getLayerPosition(layer2));
		Assert.assertEquals(2, layersModel.getLayerPosition(layer3));

		layersModel.moveLayerDown(layer3);
		Assert.assertEquals(0, layersModel.getLayerPosition(layer1));
		Assert.assertEquals(2, layersModel.getLayerPosition(layer2));
		Assert.assertEquals(1, layersModel.getLayerPosition(layer3));
	}

	@Test
	public void testMoveLayerUp() {
		LayersModel layersModel = new LayersModelImpl(eventBus);
		layersModel.initialize(mapInfo, new ViewPortImpl(eventBus));

		Layer<?> layer1 = layersModel.getLayer("osm1Layer");
		Layer<?> layer2 = layersModel.getLayer("osm2Layer");
		Layer<?> layer3 = layersModel.getLayer("osm3Layer");

		layersModel.moveLayerUp(layer3); // Expect no changes.
		Assert.assertEquals(0, layersModel.getLayerPosition(layer1));
		Assert.assertEquals(1, layersModel.getLayerPosition(layer2));
		Assert.assertEquals(2, layersModel.getLayerPosition(layer3));

		layersModel.moveLayerUp(layer1);
		Assert.assertEquals(1, layersModel.getLayerPosition(layer1));
		Assert.assertEquals(0, layersModel.getLayerPosition(layer2));
		Assert.assertEquals(2, layersModel.getLayerPosition(layer3));
	}

	@Test
	public void testMoveLayer() {
		LayersModel layersModel = new LayersModelImpl(eventBus);
		layersModel.initialize(mapInfo, new ViewPortImpl(eventBus));

		Layer<?> layer1 = layersModel.getLayer("osm1Layer");
		Layer<?> layer2 = layersModel.getLayer("osm2Layer");
		Layer<?> layer3 = layersModel.getLayer("osm3Layer");

		layersModel.moveLayer(layer1, -1); // Expect no changes.
		Assert.assertEquals(0, layersModel.getLayerPosition(layer1));
		Assert.assertEquals(1, layersModel.getLayerPosition(layer2));
		Assert.assertEquals(2, layersModel.getLayerPosition(layer3));
		
		layersModel.moveLayer(layer2, -1);
		Assert.assertEquals(0, layersModel.getLayerPosition(layer2));
		Assert.assertEquals(1, layersModel.getLayerPosition(layer1));
		Assert.assertEquals(2, layersModel.getLayerPosition(layer3));
		
		layersModel.moveLayer(layer2, 2);
		Assert.assertEquals(0, layersModel.getLayerPosition(layer1));
		Assert.assertEquals(1, layersModel.getLayerPosition(layer3));
		Assert.assertEquals(2, layersModel.getLayerPosition(layer2));

		layersModel.moveLayer(layer2, 200);
		Assert.assertEquals(0, layersModel.getLayerPosition(layer1));
		Assert.assertEquals(1, layersModel.getLayerPosition(layer3));
		Assert.assertEquals(2, layersModel.getLayerPosition(layer2));

		layersModel.moveLayer(layer3, 0);
		Assert.assertEquals(0, layersModel.getLayerPosition(layer3));
		Assert.assertEquals(1, layersModel.getLayerPosition(layer1));
		Assert.assertEquals(2, layersModel.getLayerPosition(layer2));
	}
}