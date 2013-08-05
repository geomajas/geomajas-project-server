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

package org.geomajas.smartgwt.client.map.workflow;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.smartgwt.client.map.MapModel;
import org.geomajas.smartgwt.client.map.feature.Feature;
import org.geomajas.smartgwt.client.map.workflow.*;
import org.geomajas.smartgwt.client.map.workflow.activity.Activity;
import org.geomajas.smartgwt.client.map.workflow.activity.ValidationActivity;
import org.geomajas.smartgwt.client.spatial.geometry.GeometryFactory;
import org.geomajas.smartgwt.client.spatial.geometry.LinearRing;
import org.geomajas.smartgwt.client.spatial.geometry.Polygon;
import org.geomajas.layer.LayerType;
import org.junit.Assert;
import org.junit.Test;

/**
 * Testcase for the SequenceProcessor implementation of the workflow.
 *
 * @author Pieter De Graef
 */
public class TestSequenceProcessor {

	private MapModel mapModel;

	public TestSequenceProcessor() {
		ClientMapInfo info = new ClientMapInfo();
		info.setCrs("EPSG:4326");
		info.setInitialBounds(new Bbox(0, 0, 180, 180));
		mapModel = new MapModel(info);

		ClientVectorLayerInfo layerInfo = new ClientVectorLayerInfo();
		VectorLayerInfo serverInfo = new VectorLayerInfo();
		serverInfo.setLayerType(LayerType.POLYGON);
		layerInfo.setLayerInfo(serverInfo);
		layerInfo.setMaxExtent(new Bbox(0, 0, 180, 180));
		layerInfo.setServerLayerId("test");
		layerInfo.setId("testLayer");
		List<ClientLayerInfo> layers = new ArrayList<ClientLayerInfo>();
		layers.add(layerInfo);
		info.setLayers(layers);

		//mapModel.initialize("bla", info);
		mapModel.selectLayer(mapModel.getLayer("testLayer"));

		Feature feature = new Feature();
		GeometryFactory factory = new GeometryFactory(4326, -1);
		LinearRing invalidRing = factory.createLinearRing(new Coordinate[] {new Coordinate(10, 10),
				new Coordinate(20, 10), new Coordinate(15, 20)/*, new Coordinate(15, 5)*/});
		Polygon inValidPolygon = factory.createPolygon(invalidRing, null);
		feature.setGeometry(inValidPolygon);
		mapModel.getFeatureEditor().startEditing(new Feature[] {feature}, new Feature[] {feature});
	}

	@Test
	public void testSimpleActivities() {
		List<Activity> activities = new ArrayList<Activity>();
		activities.add(new ValidationActivity());
		WorkflowProcessor processor = new SequenceProcessor(new MapModelWorkflowContext());
		processor.setDefaultErrorHandler(new ExpectErrorHandler());
		processor.setActivities(activities);
		processor.doActivities(mapModel);
	}

	private class ExpectErrorHandler implements WorkflowErrorHandler {

		public void handleError(WorkflowContext context, Throwable throwable) {
			Assert.fail();
		}
	}
}
