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
package org.geomajas.widget.searchandfilter.client.widget.geometricsearch;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.controller.editing.EditController.EditMode;
import org.geomajas.gwt.client.gfx.paintable.GfxGeometry;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.spatial.geometry.GeometryFactory;
import org.geomajas.gwt.client.spatial.geometry.LineString;
import org.geomajas.gwt.client.spatial.geometry.LinearRing;
import org.geomajas.gwt.client.spatial.geometry.Polygon;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.MapWidget.RenderGroup;
import org.geomajas.gwt.client.widget.MapWidget.RenderStatus;

import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.user.client.Event;

/**
 * Drawing controller for Polygon geometries.
 * 
 * @author Bruce Palmkoeck
 */
public class PolygonDrawController extends AbstractFreeDrawingController {

	private GfxGeometry tempLine1;

	private GfxGeometry tempLine2;

	private GfxGeometry tempLineEnd;

	private ShapeStyle drawStyleGood = new ShapeStyle("#FF7F00", 0.3f, "#FF7F00", 1, 2);

	private ShapeStyle drawStyleBad = new ShapeStyle("#FF3322", 0.4f, "#FF3322", 1, 2);

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------

	public PolygonDrawController(MapWidget mapWidget, AbstractFreeDrawingController parent, 
			GeometryDrawHandler handler) {
		super(mapWidget, parent, handler);
		factory = new GeometryFactory(mapWidget.getMapModel().getSrid(), mapWidget.getMapModel().getPrecision());
		geometry = factory.createPolygon(null, null);
	}

	// -------------------------------------------------------------------------
	// DrawController implementation:
	// -------------------------------------------------------------------------

	public void cleanup() {
		removeTempAll();
	}

	public boolean isBusy() {
		// busy when inserting or dragging has started
		return getEditMode() == EditMode.INSERT_MODE || getEditMode() == EditMode.DRAG_MODE;
	}

	// -------------------------------------------------------------------------
	// MapController implementation:
	// -------------------------------------------------------------------------

	public void onMouseDown(MouseDownEvent event) {
		if (event.getNativeButton() != Event.BUTTON_RIGHT) {
			createTempLine();
		}
	}

	public void onMouseMove(MouseMoveEvent event) {
		updateTempLine(event);
	}

	public void onMouseUp(MouseUpEvent event) {
		if (event.getNativeButton() != Event.BUTTON_RIGHT) {

			Coordinate[] oldCoords = null;
			Coordinate[] newCoords;
			int length = 0;

			if (geometry.getCoordinates() != null) {
				length = geometry.getCoordinates().length;
				oldCoords = geometry.getCoordinates();
			}
			newCoords = new Coordinate[length + 1];

			if (oldCoords != null) {
				System.arraycopy(oldCoords, 0, newCoords, 0, length);
			}

			LinearRing linearRing;

			if (newCoords.length < 2) {
				newCoords[length] = getWorldPosition(event);
			} else {
				newCoords[length] = newCoords[length - 1];
				newCoords[length - 1] = getWorldPosition(event);
			}

			linearRing = factory.createLinearRing(newCoords);
			geometry = factory.createPolygon(linearRing, null);

			Coordinate[] srCoords = new Coordinate[geometry.getCoordinates().length];
			Coordinate[] worldCoords = geometry.getCoordinates();
			for (int i = 0; i < srCoords.length; i++) {
				srCoords[i] = getTransformer().worldToPan(worldCoords[i]);
			}

			tempLineEnd = new GfxGeometry("LineStringEditController.updateLineEnd");
			tempLineEnd.setStyle(new ShapeStyle("#FF3322", 0.5f, "#FF3322", 1, 1));

			linearRing = geometry.getGeometryFactory().createLinearRing(srCoords);
			Polygon polygon = geometry.getGeometryFactory().createPolygon(linearRing, null);

			if (geometry.isValid()) {
				tempLineEnd.setStyle(drawStyleGood);
			} else {
				tempLineEnd.setStyle(drawStyleBad);
			}

			tempLineEnd.setGeometry(polygon);
			mapWidget.render(tempLineEnd, RenderGroup.VECTOR, RenderStatus.ALL);
			removeTempLines();
		}
	}

	@Override
	public void onDoubleClick(DoubleClickEvent event) {
		if (event.getNativeButton() != Event.BUTTON_RIGHT) {
			handler.onDraw(geometry);
		}
	}

	// Getters and setters:

	public void setEditMode(EditMode editMode) {
		super.setEditMode(editMode);
		if (editMode == EditMode.DRAG_MODE) {
			removeTempLines();
		}
	}

	// Private methods:

	private void createTempLine() {
		if (tempLine1 == null && geometry instanceof Polygon) {
			tempLine1 = new GfxGeometry("LineStringEditController.updateLine1");
			tempLine2 = new GfxGeometry("LineStringEditController.updateLine2");

			if (geometry.isValid()) {
				tempLine1.setStyle(drawStyleGood);
				tempLine2.setStyle(drawStyleGood);

			} else {
				tempLine1.setStyle(drawStyleBad);
				tempLine2.setStyle(drawStyleBad);
			}
		}
	}

	private void updateTempLine(MouseEvent<?> event) {
		if (tempLine1 == null) {
			createTempLine();
		}

		Polygon polygon = (Polygon) geometry;

		if (polygon != null && polygon.getCoordinates() != null && polygon.getCoordinates().length > 0) {

			LinearRing ring = geometry.getGeometryFactory().createLinearRing(polygon.getCoordinates());
			if (ring != null && geometry.getCoordinates().length > 0) {

				Coordinate[] coordinates = ring.getCoordinates();
				Coordinate firstCoordinate = coordinates[0];
				Coordinate lastCoordinate = coordinates[coordinates.length - 2];

				LineString lineString1 = geometry.getGeometryFactory().createLineString(
						new Coordinate[] { getTransformer().worldToPan(lastCoordinate), getPanPosition(event) });
				tempLine1.setGeometry(lineString1);

				LineString lineString2 = geometry.getGeometryFactory().createLineString(
						new Coordinate[] { getTransformer().worldToPan(firstCoordinate), getPanPosition(event) });
				tempLine2.setGeometry(lineString2);

				mapWidget.render(tempLine1, RenderGroup.VECTOR, RenderStatus.ALL);
				mapWidget.render(tempLine2, RenderGroup.VECTOR, RenderStatus.ALL);
			}
		}
	}

	private void removeTempLines() {
		if (tempLine1 != null) {
			mapWidget.render(tempLine1, RenderGroup.VECTOR, RenderStatus.DELETE);
			tempLine1 = null;
		}
		if (tempLine2 != null) {
			mapWidget.render(tempLine2, RenderGroup.VECTOR, RenderStatus.DELETE);
			tempLine2 = null;
		}
	}

	private void removeTempAll() {
		if (tempLine1 != null) {
			mapWidget.render(tempLine1, RenderGroup.VECTOR, RenderStatus.DELETE);
			tempLine1 = null;
		}
		if (tempLine2 != null) {
			mapWidget.render(tempLine2, RenderGroup.VECTOR, RenderStatus.DELETE);
			tempLine2 = null;
		}
		if (tempLineEnd != null) {
			mapWidget.render(tempLineEnd, RenderGroup.VECTOR, RenderStatus.DELETE);
			tempLineEnd = null;
		}
	}
}
