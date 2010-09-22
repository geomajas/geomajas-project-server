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

package org.geomajas.gwt.client.map.workflow;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.configuration.client.ClientLayerInfo;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.configuration.LayerInfo;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.workflow.activity.Activity;
import org.geomajas.gwt.client.map.workflow.activity.ValidationActivity;
import org.geomajas.gwt.client.spatial.geometry.GeometryFactory;
import org.geomajas.gwt.client.spatial.geometry.LinearRing;
import org.geomajas.gwt.client.spatial.geometry.Polygon;
import org.geomajas.layer.LayerType;
import org.junit.Test;

import com.vividsolutions.jts.util.Assert;

/**
 * Testcase for the SequenceProcessor implementation of the workflow.
 *
 * @author Pieter De Graef
 */
public class TestSequenceProcessor {

	private MapModel mapModel;

	public TestSequenceProcessor() {
		mapModel = new MapModel("foobar");
		ClientMapInfo info = new ClientMapInfo();
		info.setCrs("EPSG:4326");
		info.setInitialBounds(new Bbox(0, 0, 180, 180));

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

		mapModel.initialize(info);
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
			Assert.shouldNeverReachHere();
		}
	}
}
