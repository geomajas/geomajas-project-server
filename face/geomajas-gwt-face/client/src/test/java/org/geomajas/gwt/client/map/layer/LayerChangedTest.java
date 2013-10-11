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

package org.geomajas.gwt.client.map.layer;

import com.google.gwt.junit.GWTMockUtilities;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.event.LayerChangedHandler;
import org.geomajas.gwt.client.map.event.LayerFilteredEvent;
import org.geomajas.gwt.client.map.event.LayerFilteredHandler;
import org.geomajas.gwt.client.map.event.LayerLabeledEvent;
import org.geomajas.gwt.client.map.event.LayerShownEvent;
import org.geomajas.layer.LayerType;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the handling of the {@link LayerShownEvent} and {@link LayerLabeledEvent}.
 *
 * @author Frank Wynants
 */
public class LayerChangedTest {

	private int visibleCount;
	private int labelCount;
	private int filterCount;
	private VectorLayer vLayer;

	@Before
	public void setUp() {
		ClientMapInfo info = new ClientMapInfo();
		info.setCrs("EPSG:4326");
		info.setInitialBounds(new Bbox(0, 0, 180, 180));
		MapModel mapModel = new MapModel(info);
		ClientVectorLayerInfo vLayerInfo = new ClientVectorLayerInfo();
		VectorLayerInfo serverInfo = new VectorLayerInfo();
		serverInfo.setLayerType(LayerType.POLYGON);
		vLayerInfo.setLayerInfo(serverInfo);
		vLayerInfo.setMaxExtent(new Bbox(0, 0, 180, 180));
		vLayerInfo.setServerLayerId("test");
		vLayer = new VectorLayer(mapModel, vLayerInfo);

		visibleCount = 0;
		labelCount = 0;
		filterCount = 0;

		vLayer.addLayerChangedHandler(new LayerChangedHandler() {

			public void onVisibleChange(LayerShownEvent event) {
				visibleCount++;
			}

			public void onLabelChange(LayerLabeledEvent event) {
				labelCount++;
			}

		});

		vLayer.addLayerFilteredHandler(new LayerFilteredHandler() {

			public void onFilterChange(LayerFilteredEvent event) {
				filterCount++;
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

	@Test
	public void testLayerFiltered() {
		Assert.assertEquals(0, filterCount);
		vLayer.setFilter("filter1");
		Assert.assertEquals(1, filterCount);
		vLayer.setFilter("filter2");
		Assert.assertEquals(2, filterCount);
		vLayer.setFilter("filter2");
		Assert.assertEquals(2, filterCount);
	}
}