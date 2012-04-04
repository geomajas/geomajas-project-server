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

package org.geomajas.plugin.editing.jsapi.example.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import org.geomajas.plugin.editing.dto.GeometrySplitRequest;
import org.geomajas.plugin.editing.dto.GeometrySplitResponse;
import org.geomajas.plugin.editing.jsapi.example.dto.GetCentroidRequest;
import org.geomajas.plugin.editing.jsapi.example.dto.GetCentroidResponse;

import com.google.gwt.event.shared.HandlerRegistration;

/**
 * Class for activating and deactivating labels that show areas during splitting.
 * 
 * @author Pieter De Graef
 */
public class ShowSplitAreas {

	private final MapWidget mapWidget;

	private final GeometrySplitService service;

	private final List<Coordinate> centroids;

	private final List<Rectangle> labelBgs;

	private final List<Text> labelTxts;

	private final List<HandlerRegistration> registrations;

	// Getter/setter fields, used in label display:

	private int labelWidth = 120;

	private int labelHeight = 18;

	private String labelTxt = "Area:";

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	public ShowSplitAreas() {
		this(null, null);
	}

	public ShowSplitAreas(MapWidget mapWidget, GeometrySplitService service) {
		this.mapWidget = mapWidget;
		this.service = service;
		centroids = new ArrayList<Coordinate>();
		labelBgs = new ArrayList<Rectangle>();
		labelTxts = new ArrayList<Text>();
		registrations = new ArrayList<HandlerRegistration>();
	}

	// ------------------------------------------------------------------------
	// Public methods:
	// ------------------------------------------------------------------------

	public void activate() {
		deactivate();

		registrations.add(service.getGeometryEditService().addGeometryEditStartHandler(new GeometryEditStartHandler() {

			public void onGeometryEditStart(GeometryEditStartEvent event) {
				cleanup();
			}
		}));
		registrations.add(service.getGeometryEditService().addGeometryEditStopHandler(new GeometryEditStopHandler() {

			public void onGeometryEditStop(GeometryEditStopEvent event) {
				cleanup();
			}
		}));
		registrations.add(service.getGeometryEditService().addGeometryEditShapeChangedHandler(
				new GeometryEditShapeChangedHandler() {

					public void onGeometryShapeChanged(GeometryEditShapeChangedEvent event) {
						fetchGeometries();
					}
				}));
		registrations.add(mapWidget.getMapModel().getMapView().addMapViewChangedHandler(new MapViewChangedHandler() {

			public void onMapViewChanged(MapViewChangedEvent event) {
				for (int i = 0; i < centroids.size(); i++) {
					Coordinate centroid = centroids.get(i);
					Coordinate position = mapWidget.getMapModel().getMapView().getWorldViewTransformer()
							.worldToView(centroid);
					int x = (int) (position.getX() - labelWidth / 2);
					int y = (int) (position.getY() - labelHeight / 2);

					Rectangle rectangle = labelBgs.get(i);
					rectangle.setBounds(new Bbox(x, y, labelWidth, labelHeight));
					mapWidget.render(rectangle, RenderGroup.SCREEN, RenderStatus.UPDATE);

					Text text = labelTxts.get(i);
					text.setPosition(new Coordinate(x + 4, y + 2));
					mapWidget.render(text, RenderGroup.SCREEN, RenderStatus.UPDATE);
				}
			}
		}));
	}

	public void deactivate() {
		cleanup();

		for (HandlerRegistration registration : registrations) {
			registration.removeHandler();
		}
		registrations.clear();
	}

	public boolean isActivated() {
		return registrations.size() > 0;
	}

	// ------------------------------------------------------------------------
	// Getters and setters:
	// ------------------------------------------------------------------------

	public int getLabelWidth() {
		return labelWidth;
	}

	public int getLabelHeight() {
		return labelHeight;
	}

	public String getLabelTxt() {
		return labelTxt;
	}

	public void setLabelWidth(int labelWidth) {
		this.labelWidth = labelWidth;
	}

	public void setLabelHeight(int labelHeight) {
		this.labelHeight = labelHeight;
	}

	public void setLabelTxt(String labelTxt) {
		this.labelTxt = labelTxt;
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

	private void showAreas(Map<Geometry, Coordinate> geometries) {
		cleanup();
		int i = 0;
		for (org.geomajas.geometry.Geometry geom : geometries.keySet()) {
			org.geomajas.gwt.client.spatial.geometry.Geometry geometry = GeometryConverter.toGwt(geom);
			Coordinate centroid = geometries.get(geom);
			centroids.add(centroid);

			Coordinate position = mapWidget.getMapModel().getMapView().getWorldViewTransformer().worldToView(centroid);
			int x = (int) (position.getX() - labelWidth / 2);
			int y = (int) (position.getY() - labelHeight / 2);

			Rectangle rectangle = new Rectangle("area-geom-" + i + "-bg");
			rectangle.setBounds(new Bbox(x, y, labelWidth, labelHeight));
			rectangle.setStyle(new ShapeStyle("#FFFFFF", 0.9f, "#000000", 0.9f, 1));
			mapWidget.render(rectangle, RenderGroup.SCREEN, RenderStatus.ALL);
			labelBgs.add(rectangle);

			Text text = new Text("area-geom-" + i + "-txt");
			text.setPosition(new Coordinate(x + 4, y + 2));
			text.setStyle(new FontStyle("#000000", 12, "Arial", "normal", "normal"));
			String txt = labelTxt + " " + DistanceFormat.asMapArea(mapWidget, geometry.getArea());
			txt = txt.replaceAll("&sup2;", "²");
			text.setContent(txt);
			mapWidget.render(text, RenderGroup.SCREEN, RenderStatus.ALL);
			labelTxts.add(text);

			i++;
		}
	}

	private void fetchGeometries() {
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
					//showAreas(response.getGeometries());
					fetchCentroids(response.getGeometries());
				}
			});
		}
	}

	private void fetchCentroids(List<org.geomajas.geometry.Geometry> geometries) {
		GetCentroidRequest request = new GetCentroidRequest();
		request.setGeometries(geometries);
		GwtCommand command = new GwtCommand(GetCentroidRequest.COMMAND);
		command.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(command, new AbstractCommandCallback<GetCentroidResponse>() {

			public void execute(GetCentroidResponse response) {
				showAreas(response.getCentroids());
			}
		});
	}
}