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

import com.google.gwt.junit.client.GWTTestCase;

/**
 * Tests for the MapModelImpl class to see if it correctly implements all MapModel methods.
 * 
 * @author Pieter De Graef
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml", "applicationContext.xml", "mapOsm.xml",
		"layerOsm1.xml", "layerOsm2.xml", "layerOsm3.xml", "layerOsm4.xml" })
public class MapModelTest extends GWTTestCase {

	@Autowired
	@Qualifier(value = "mapTest")
	private ClientMapInfo mapInfo;

	private int layerCount;

	public String getModuleName() {
		return "org.geomajas.puregwt.GeomajasClientImpl";
	}

	@Test
	public void testInitialize() {
		MapModel mapModel = new MapModelImpl();
		final MapCompositionHandler layerCounter = new MapCompositionHandler() {

			public void onLayerAdded(LayerAddedEvent event) {
				layerCount++;
			}

			public void onLayerRemoved(LayerRemovedEvent event) {
			}
		};
		mapModel.getEventBus().addHandler(MapCompositionHandler.TYPE, layerCounter);
		mapModel.initialize(mapInfo, 640, 480);
		Assert.assertEquals(3, layerCount);
		Assert.assertEquals(3, mapModel.getLayerCount());
		Assert.assertEquals("EPSG:900913", mapModel.getEpsg());
		Assert.assertEquals(900913, mapModel.getSrid());
	}

	@Test
	public void testLayerSelection() {
		MapModel mapModel = new MapModelImpl();
		mapModel.initialize(mapInfo, 640, 480);
		Layer<?> layer1 = mapModel.getLayer("osm1Layer");
		Layer<?> layer2 = mapModel.getLayer("osm2Layer");

		layer1.setSelected(true);
		Assert.assertEquals(layer1, mapModel.getSelectedLayer());

		layer2.setSelected(true);
		Assert.assertEquals(layer2, mapModel.getSelectedLayer());
		Assert.assertEquals(false, layer1.isSelected());

		layer2.setSelected(false);
		Assert.assertEquals(null, mapModel.getSelectedLayer());
	}

	@Test
	public void testGetLayer() {
		MapModel mapModel = new MapModelImpl();
		mapModel.initialize(mapInfo, 640, 480);

		Assert.assertEquals(mapModel.getLayer(0), mapModel.getLayer("osm1Layer"));
		Assert.assertEquals(mapModel.getLayer(1), mapModel.getLayer("osm2Layer"));
		Assert.assertEquals(mapModel.getLayer(2), mapModel.getLayer("osm3Layer"));
	}

	@Test
	public void testLayerPosition() {
		MapModel mapModel = new MapModelImpl();
		mapModel.initialize(mapInfo, 640, 480);

		Assert.assertEquals(0, mapModel.getLayerPosition(mapModel.getLayer(0)));
		Assert.assertEquals(1, mapModel.getLayerPosition(mapModel.getLayer(1)));
		Assert.assertEquals(2, mapModel.getLayerPosition(mapModel.getLayer(2)));

		Assert.assertEquals(0, mapModel.getLayerPosition(mapModel.getLayer("osm1Layer")));
		Assert.assertEquals(1, mapModel.getLayerPosition(mapModel.getLayer("osm2Layer")));
		Assert.assertEquals(2, mapModel.getLayerPosition(mapModel.getLayer("osm3Layer")));
	}

	@Test
	public void testMoveLayerDown() {
		MapModel mapModel = new MapModelImpl();
		mapModel.initialize(mapInfo, 640, 480);

		Layer<?> layer1 = mapModel.getLayer("osm1Layer");
		Layer<?> layer2 = mapModel.getLayer("osm2Layer");
		Layer<?> layer3 = mapModel.getLayer("osm3Layer");

		mapModel.moveLayerDown(layer1); // Expect no changes.
		Assert.assertEquals(0, mapModel.getLayerPosition(layer1));
		Assert.assertEquals(1, mapModel.getLayerPosition(layer2));
		Assert.assertEquals(2, mapModel.getLayerPosition(layer3));

		mapModel.moveLayerDown(layer3);
		Assert.assertEquals(0, mapModel.getLayerPosition(layer1));
		Assert.assertEquals(2, mapModel.getLayerPosition(layer2));
		Assert.assertEquals(1, mapModel.getLayerPosition(layer3));
	}

	@Test
	public void testMoveLayerUp() {
		MapModel mapModel = new MapModelImpl();
		mapModel.initialize(mapInfo, 640, 480);

		Layer<?> layer1 = mapModel.getLayer("osm1Layer");
		Layer<?> layer2 = mapModel.getLayer("osm2Layer");
		Layer<?> layer3 = mapModel.getLayer("osm3Layer");

		mapModel.moveLayerUp(layer3); // Expect no changes.
		Assert.assertEquals(0, mapModel.getLayerPosition(layer1));
		Assert.assertEquals(1, mapModel.getLayerPosition(layer2));
		Assert.assertEquals(2, mapModel.getLayerPosition(layer3));

		mapModel.moveLayerUp(layer1);
		Assert.assertEquals(1, mapModel.getLayerPosition(layer1));
		Assert.assertEquals(0, mapModel.getLayerPosition(layer2));
		Assert.assertEquals(2, mapModel.getLayerPosition(layer3));
	}

	@Test
	public void testMoveLayer() {
		MapModel mapModel = new MapModelImpl();
		mapModel.initialize(mapInfo, 640, 480);

		Layer<?> layer1 = mapModel.getLayer("osm1Layer");
		Layer<?> layer2 = mapModel.getLayer("osm2Layer");
		Layer<?> layer3 = mapModel.getLayer("osm3Layer");

		mapModel.moveLayer(layer1, -1); // Expect no changes.
		Assert.assertEquals(0, mapModel.getLayerPosition(layer1));
		Assert.assertEquals(1, mapModel.getLayerPosition(layer2));
		Assert.assertEquals(2, mapModel.getLayerPosition(layer3));
		
		mapModel.moveLayer(layer2, -1);
		Assert.assertEquals(0, mapModel.getLayerPosition(layer2));
		Assert.assertEquals(1, mapModel.getLayerPosition(layer1));
		Assert.assertEquals(2, mapModel.getLayerPosition(layer3));
		
		mapModel.moveLayer(layer2, 2);
		Assert.assertEquals(0, mapModel.getLayerPosition(layer1));
		Assert.assertEquals(1, mapModel.getLayerPosition(layer3));
		Assert.assertEquals(2, mapModel.getLayerPosition(layer2));

		mapModel.moveLayer(layer2, 200);
		Assert.assertEquals(0, mapModel.getLayerPosition(layer1));
		Assert.assertEquals(1, mapModel.getLayerPosition(layer3));
		Assert.assertEquals(2, mapModel.getLayerPosition(layer2));

		mapModel.moveLayer(layer3, 0);
		Assert.assertEquals(0, mapModel.getLayerPosition(layer3));
		Assert.assertEquals(1, mapModel.getLayerPosition(layer1));
		Assert.assertEquals(2, mapModel.getLayerPosition(layer2));
	}
}