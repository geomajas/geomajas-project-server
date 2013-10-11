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

package org.geomajas.gwt.client.map;

import com.google.gwt.junit.GWTMockUtilities;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.gwt.client.map.event.FeatureDeselectedEvent;
import org.geomajas.gwt.client.map.event.FeatureSelectedEvent;
import org.geomajas.gwt.client.map.event.FeatureSelectionHandler;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the handling of the FeatureSelectedEvent and FeatureDeselectedEvent.
 *
 * @author Joachim Van der Auwera
 */
public class MapModelSelectedFeaturesTest {

	private MapModel mapModel;
	private VectorLayer layer1, layer2;
	private int selectedCount, deselectedCount;
	private String lastFeatureId;

	@Before
	public void setUp() {
		ClientMapInfo info = new ClientMapInfo();
		info.setCrs("EPSG:4326");
		info.setInitialBounds(new Bbox(0, 0, 180, 180));
		mapModel = new MapModel(info);

		VectorLayerInfo serverLayerInfo1 = new VectorLayerInfo();
		ClientVectorLayerInfo layerInfo1 = new ClientVectorLayerInfo();
		layerInfo1.setLayerInfo(serverLayerInfo1);
		layerInfo1.setMaxExtent(new Bbox(0, 0, 200, 100));
		layerInfo1.setId("layer1");
		layerInfo1.setServerLayerId("layer1");
		layer1 = new VectorLayer(mapModel, layerInfo1);

		VectorLayerInfo serverLayerInfo2 = new VectorLayerInfo();
		ClientVectorLayerInfo layerInfo2 = new ClientVectorLayerInfo();
		layerInfo2.setLayerInfo(serverLayerInfo2);
		layerInfo2.setMaxExtent(new Bbox(0, 0, 250, 125));
		layerInfo2.setId("layer2");
		layerInfo2.setServerLayerId("layer2");
		layer2 = new VectorLayer(mapModel, layerInfo2);

		mapModel.getLayers().add(layer1);
		mapModel.getLayers().add(layer2);

		selectedCount = 0;
		deselectedCount = 0;
		lastFeatureId = null;
		layer1.addFeatureSelectionHandler(new FeatureSelectionHandler() {
			public void onFeatureSelected(FeatureSelectedEvent event) {
				selectedCount++;
				lastFeatureId = event.getFeature().getId();
			}

			public void onFeatureDeselected(FeatureDeselectedEvent event) {
				deselectedCount++;
				lastFeatureId = event.getFeature().getId();
			}
		});
	}

	@Test
	public void testSelectDeselectFeature() {
		org.geomajas.layer.feature.Feature dto1 = new org.geomajas.layer.feature.Feature();
		dto1.setId("feat1");
		Feature feature1 = new Feature(dto1, layer1);

		org.geomajas.layer.feature.Feature dto2 = new org.geomajas.layer.feature.Feature();
		dto2.setId("feat2");
		Feature feature2 = new Feature(dto2, layer1);

		Assert.assertFalse(feature1.isSelected());
		Assert.assertFalse(feature2.isSelected());
		Assert.assertEquals(0, selectedCount);
		Assert.assertEquals(0, deselectedCount);
		Assert.assertNull(lastFeatureId);

		layer1.selectFeature(feature1);
		Assert.assertTrue(feature1.isSelected());
		Assert.assertFalse(feature2.isSelected());
		Assert.assertEquals(1, selectedCount);
		Assert.assertEquals(0, deselectedCount);
		Assert.assertEquals("feat1", lastFeatureId);
		lastFeatureId = null;

		layer1.selectFeature(feature2);
		Assert.assertTrue(feature1.isSelected());
		Assert.assertTrue(feature2.isSelected());
		Assert.assertEquals(2, selectedCount);
		Assert.assertEquals(0, deselectedCount);
		Assert.assertEquals("feat2", lastFeatureId);

		layer1.deselectFeature(feature1);
		Assert.assertFalse(feature1.isSelected());
		Assert.assertTrue(feature2.isSelected());
		Assert.assertEquals(2, selectedCount);
		Assert.assertEquals(1, deselectedCount);
		Assert.assertEquals("feat1", lastFeatureId);
	}

	@Test
	public void testDeselectLayer() {
		org.geomajas.layer.feature.Feature dto1 = new org.geomajas.layer.feature.Feature();
		dto1.setId("feat1");
		Feature feature1 = new Feature(dto1, layer1);

		org.geomajas.layer.feature.Feature dto2 = new org.geomajas.layer.feature.Feature();
		dto2.setId("feat2");
		Feature feature2 = new Feature(dto2, layer1);

		Assert.assertFalse(feature1.isSelected());
		Assert.assertFalse(feature2.isSelected());
		Assert.assertEquals(0, selectedCount);
		Assert.assertEquals(0, deselectedCount);

		mapModel.selectLayer(layer1);
		Assert.assertFalse(feature1.isSelected());
		Assert.assertFalse(feature2.isSelected());
		Assert.assertEquals(0, selectedCount);
		Assert.assertEquals(0, deselectedCount);

		layer1.selectFeature(feature1);
		layer1.selectFeature(feature2);
		Assert.assertTrue(feature1.isSelected());
		Assert.assertTrue(feature2.isSelected());
		Assert.assertEquals(2, selectedCount);
		Assert.assertEquals(0, deselectedCount);

		mapModel.selectLayer(layer1);
		Assert.assertTrue(feature1.isSelected());
		Assert.assertTrue(feature2.isSelected());
		Assert.assertEquals(2, selectedCount);
		Assert.assertEquals(0, deselectedCount);

		mapModel.selectLayer(layer2);
		Assert.assertTrue(feature1.isSelected());
		Assert.assertTrue(feature2.isSelected());
		Assert.assertEquals(2, selectedCount);
		Assert.assertEquals(0, deselectedCount);
	}
}
