/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.gwt.client.map;

import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.gwt.client.map.event.FeatureDeselectedEvent;
import org.geomajas.gwt.client.map.event.FeatureSelectedEvent;
import org.geomajas.gwt.client.map.event.FeatureSelectionHandler;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.layer.VectorLayer;
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
		mapModel = new MapModel("test");

		VectorLayerInfo serverLayerInfo1 = new VectorLayerInfo();
		serverLayerInfo1.setId("layer1");
		ClientVectorLayerInfo layerInfo1 = new ClientVectorLayerInfo();
		layerInfo1.setLayerInfo(serverLayerInfo1);
		layerInfo1.setMaxExtent(new Bbox(0, 0, 200, 100));
		layer1 = new VectorLayer(mapModel, layerInfo1);

		VectorLayerInfo serverLayerInfo2 = new VectorLayerInfo();
		serverLayerInfo2.setId("layer2");
		ClientVectorLayerInfo layerInfo2 = new ClientVectorLayerInfo();
		layerInfo2.setLayerInfo(serverLayerInfo2);
		layerInfo2.setMaxExtent(new Bbox(0, 0, 250, 125));
		layer2 = new VectorLayer(mapModel, layerInfo2);

		mapModel.getLayers().add(layer1);
		mapModel.getLayers().add(layer2);

		selectedCount = 0;
		deselectedCount = 0;
		lastFeatureId = null;
		mapModel.addFeatureSelectionHandler(new FeatureSelectionHandler() {
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
		dto1.setId("layer1.feat1");
		Feature feature1 = new Feature(dto1, layer1);

		org.geomajas.layer.feature.Feature dto2 = new org.geomajas.layer.feature.Feature();
		dto2.setId("layer1.feat2");
		Feature feature2 = new Feature(dto2, layer1);

		Assert.assertFalse(feature1.isSelected());
		Assert.assertFalse(feature2.isSelected());
		Assert.assertEquals(0, selectedCount);
		Assert.assertEquals(0, deselectedCount);
		Assert.assertNull(lastFeatureId);

		mapModel.selectFeature(feature1);
		Assert.assertTrue(feature1.isSelected());
		Assert.assertFalse(feature2.isSelected());
		Assert.assertEquals(1, selectedCount);
		Assert.assertEquals(0, deselectedCount);
		Assert.assertEquals("layer1.feat1", lastFeatureId);
		lastFeatureId = null;

		mapModel.deselectFeature(feature2);
		Assert.assertTrue(feature1.isSelected());
		Assert.assertFalse(feature2.isSelected());
		Assert.assertEquals(1, selectedCount);
		Assert.assertEquals(0, deselectedCount);
		Assert.assertNull(lastFeatureId);

		mapModel.deselectFeature(feature1);
		Assert.assertFalse(feature1.isSelected());
		Assert.assertFalse(feature2.isSelected());
		Assert.assertEquals(1, selectedCount);
		Assert.assertEquals(1, deselectedCount);
		Assert.assertEquals("layer1.feat1", lastFeatureId);
	}

	@Test
	public void testDeselectLayer() {
		org.geomajas.layer.feature.Feature dto1 = new org.geomajas.layer.feature.Feature();
		dto1.setId("layer1.feat1");
		Feature feature1 = new Feature(dto1, layer1);

		org.geomajas.layer.feature.Feature dto2 = new org.geomajas.layer.feature.Feature();
		dto2.setId("layer1.feat2");
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

		mapModel.selectFeature(feature1);
		mapModel.selectFeature(feature2);
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
