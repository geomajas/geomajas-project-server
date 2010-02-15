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

package org.geomajas.gwt.client.map.layer;

import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.event.LayerChangedHandler;
import org.geomajas.gwt.client.map.event.LayerLabeledEvent;
import org.geomajas.gwt.client.map.event.LayerShownEvent;
import org.geomajas.layer.LayerType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the handling of the {@link LayerShownEvent} and {@link LayerLabeledEvent}
 *
 * @author Frank Wynants
 */
public class LayerChangedTest {

	private MapModel mapModel;
	private ClientVectorLayerInfo vLayerInfo;
	private int visibleCount;
	private int labelCount;
	private VectorLayer vLayer;

	@Before
	public void setUp() {
		mapModel = new MapModel("test");
		vLayerInfo = new ClientVectorLayerInfo();
		VectorLayerInfo serverInfo = new VectorLayerInfo();
		serverInfo.setLayerType(LayerType.POLYGON);
		serverInfo.setId("test");
		vLayerInfo.setLayerInfo(serverInfo);
		vLayerInfo.setMaxExtent(new Bbox(0, 0, 180, 180));
		vLayer = new VectorLayer(mapModel, vLayerInfo);

		visibleCount = 0;
		labelCount = 0;

		vLayer.addLayerChangedHandler(new LayerChangedHandler() {

			public void onVisibleChange(LayerShownEvent event) {
				visibleCount++;
			}

			public void onLabelChange(LayerLabeledEvent event) {
				labelCount++;
			}
		});
	}

	@Test
	public void testLayerLabels() {
		//impossible to assert on vLayer.isLabeled because the isShowing is always false
		Assert.assertEquals(0, labelCount);
		vLayer.setLabeled(false);
		Assert.assertEquals(1, labelCount);
		vLayer.setLabeled(true);
		Assert.assertEquals(2, labelCount);
	}

	@Test
	public void testLayerVisible() {
		Assert.assertEquals(0, visibleCount);
		vLayer.setVisible(true);
		Assert.assertEquals(vLayer.isVisible(), true);
		Assert.assertEquals(1, visibleCount);
		vLayer.setVisible(false);
		Assert.assertEquals(vLayer.isVisible(), false);
		Assert.assertEquals(2, visibleCount);
	}
}