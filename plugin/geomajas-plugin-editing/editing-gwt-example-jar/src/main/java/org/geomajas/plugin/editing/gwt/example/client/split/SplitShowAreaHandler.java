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

package org.geomajas.plugin.editing.gwt.example.client.split;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.command.dto.GeometrySplitRequest;
import org.geomajas.command.dto.GeometrySplitResponse;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.gfx.paintable.Rectangle;
import org.geomajas.gwt.client.gfx.paintable.Text;
import org.geomajas.gwt.client.gfx.style.FontStyle;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.map.event.MapViewChangedEvent;
import org.geomajas.gwt.client.map.event.MapViewChangedHandler;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.util.DistanceFormat;
import org.geomajas.gwt.client.util.GeometryConverter;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.MapWidget.RenderGroup;
import org.geomajas.gwt.client.widget.MapWidget.RenderStatus;
import org.geomajas.plugin.editing.client.event.GeometryEditShapeChangedEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditShapeChangedHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditStartEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditStartHandler;
import org.geomajas.plugin.editing.client.event.GeometryEditStopEvent;
import org.geomajas.plugin.editing.client.event.GeometryEditStopHandler;
import org.geomajas.plugin.editing.client.split.GeometrySplitService;

/**
 * Handler for drawing labels with areas for each part while splitting.
 * 
 * @author Pieter De Graef
 */
public class SplitShowAreaHandler implements GeometryEditStartHandler, GeometryEditStopHandler,
		GeometryEditShapeChangedHandler {

	private static final int WIDTH = 120;

	private static final int HEIGHT = 18;

	private final MapWidget mapWidget;

	private final GeometrySplitService service;

	private final List<Coordinate> centroids;

	private final List<Rectangle> labelBgs;

	private final List<Text> labelTxts;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	public SplitShowAreaHandler(final MapWidget mapWidget, GeometrySplitService service) {
		this.mapWidget = mapWidget;
		this.service = service;
		centroids = new ArrayList<Coordinate>();
		labelBgs = new ArrayList<Rectangle>();
		labelTxts = new ArrayList<Text>();

		service.getGeometryEditService().addGeometryEditStartHandler(this);
		service.getGeometryEditService().addGeometryEditStopHandler(this);
		service.getGeometryEditService().addGeometryEditShapeChangedHandler(this);

		mapWidget.getMapModel().getMapView().addMapViewChangedHandler(new MapViewChangedHandler() {

			public void onMapViewChanged(MapViewChangedEvent event) {
				for (int i = 0; i < centroids.size(); i++) {
					Coordinate centroid = centroids.get(i);
					Coordinate position = mapWidget.getMapModel().getMapView().getWorldViewTransformer()
							.worldToView(centroid);
					int x = (int) (position.getX() - WIDTH / 2);
					int y = (int) (position.getY() - HEIGHT / 2);

					Rectangle rectangle = labelBgs.get(i);
					rectangle.setBounds(new Bbox(x, y, WIDTH, HEIGHT));
					mapWidget.render(rectangle, RenderGroup.SCREEN, RenderStatus.UPDATE);

					Text text = labelTxts.get(i);
					text.setPosition(new Coordinate(x + 4, y + 2));
					mapWidget.render(text, RenderGroup.SCREEN, RenderStatus.UPDATE);
				}
			}
		});
	}

	// ------------------------------------------------------------------------
	// Public methods:
	// ------------------------------------------------------------------------

	public void onGeometryShapeChanged(GeometryEditShapeChangedEvent event) {
		fetch();
	}

	public void onGeometryEditStop(GeometryEditStopEvent event) {
		cleanup();
	}

	public void onGeometryEditStart(GeometryEditStartEvent event) {
		cleanup();
	}

	// ------------------------------------------------------------------------
	// Private methods:
	// ------------------------------------------------------------------------

	private void cleanup() {
		for (Rectangle rectangle : labelBgs) {
			mapWidget.render(rectangle, RenderGroup.SCREEN, RenderStatus.DELETE);
		}
		labelBgs.clear();

		for (Text text : labelTxts) {
			mapWidget.render(text, RenderGroup.SCREEN, RenderStatus.DELETE);
		}
		labelTxts.clear();

		centroids.clear();
	}

	private void showAreas(List<Geometry> geometries) {
		cleanup();
		for (int i = 0; i < geometries.size(); i++) {
			org.geomajas.gwt.client.spatial.geometry.Geometry geometry = GeometryConverter.toGwt(geometries.get(i));
			Coordinate centroid = geometry.getCentroid();
			Coordinate position = mapWidget.getMapModel().getMapView().getWorldViewTransformer().worldToView(centroid);
			centroids.add(centroid);

			int x = (int) (position.getX() - WIDTH / 2);
			int y = (int) (position.getY() - HEIGHT / 2);
			Rectangle rectangle = new Rectangle("area-geom-" + i + "-bg");
			rectangle.setBounds(new Bbox(x, y, WIDTH, HEIGHT));
			rectangle.setStyle(new ShapeStyle("#FFFFFF", 0.9f, "#000000", 0.9f, 1));
			mapWidget.render(rectangle, RenderGroup.SCREEN, RenderStatus.ALL);
			labelBgs.add(rectangle);

			Text text = new Text("area-geom-" + i + "-txt");
			text.setPosition(new Coordinate(x + 4, y + 2));
			text.setStyle(new FontStyle("#000000", 12, "Arial", "normal", "normal"));
			String txt = "Area: " + DistanceFormat.asMapArea(mapWidget, geometry.getArea());
			txt = txt.replaceAll("&sup2;", "²");
			text.setContent(txt);
			mapWidget.render(text, RenderGroup.SCREEN, RenderStatus.ALL);
			labelTxts.add(text);
		}
	}

	private void fetch() {
		Geometry geometry = service.getGeometry();
		Geometry splitLine = service.getGeometryEditService().getGeometry();
		if (splitLine.getCoordinates() != null && splitLine.getCoordinates().length > 1) {
			geometry.setPrecision(-1);
			splitLine.setPrecision(-1);

			GeometrySplitRequest request = new GeometrySplitRequest(geometry, splitLine);
			GwtCommand command = new GwtCommand(GeometrySplitRequest.COMMAND);
			command.setCommandRequest(request);
			GwtCommandDispatcher.getInstance().execute(command, new AbstractCommandCallback<GeometrySplitResponse>() {

				public void execute(GeometrySplitResponse response) {
					showAreas(response.getGeometries());
				}
			});
		}
	}
}