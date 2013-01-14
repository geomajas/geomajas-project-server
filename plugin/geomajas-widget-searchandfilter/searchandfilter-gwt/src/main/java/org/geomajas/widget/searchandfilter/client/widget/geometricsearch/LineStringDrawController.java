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

package org.geomajas.widget.searchandfilter.client.widget.geometricsearch;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.controller.editing.EditController.EditMode;
import org.geomajas.gwt.client.gfx.paintable.GfxGeometry;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.spatial.geometry.Geometry;
import org.geomajas.gwt.client.spatial.geometry.LineString;
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
 * Drawing controller for LineString geometries.
 * 
 * @author Bruce Palmkoeck
 */
public class LineStringDrawController extends AbstractFreeDrawingController {

	private GfxGeometry tempLine;

	private GfxGeometry tempLineEnd;

	private ShapeStyle drawStyle = new ShapeStyle("#FF7F00", 0f, "#FF7F00", 1, 2);

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------

	public LineStringDrawController(MapWidget mapWidget, AbstractFreeDrawingController parent,
			GeometryDrawHandler handler) {
	super(mapWidget, parent, handler);
		geometry = factory.createLineString(new Coordinate[0]);
	}

	// -------------------------------------------------------------------------
	// DrawController implementation:
	// -------------------------------------------------------------------------

	public void cleanup() {
		removeTempLine();
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
			createTempLine(geometry);
		}
	}

	public void onMouseMove(MouseMoveEvent event) {
		updateTempLine(geometry, event);
	}

	public void onMouseUp(MouseUpEvent event) {
		if (event.getNativeButton() != Event.BUTTON_RIGHT) {
			Coordinate[] newCoords;
			int length;
			length = geometry.getCoordinates().length;
			Coordinate[] oldCoords = geometry.getCoordinates();
			newCoords = new Coordinate[length + 1];
			System.arraycopy(oldCoords, 0, newCoords, 0, length);

			newCoords[length] = getWorldPosition(event);
			geometry = factory.createLineString(newCoords);
			createTempLine(geometry);
		}
	}

	// Getters and setters:

	@Override
	public void onDoubleClick(DoubleClickEvent event) {
		if (event.getNativeButton() != Event.BUTTON_RIGHT) {
			handler.onDraw(geometry);
		}
	}

	public void setEditMode(EditMode editMode) {
		super.setEditMode(editMode);
		if (editMode == EditMode.DRAG_MODE) {
			removeTempLine();
		}
	}

	// Private methods:

	private void createTempLine(Geometry geometry) {
		if (tempLine == null) {
			tempLine = new GfxGeometry("LineStringDrawController.updateLine");
			tempLine.setStyle(drawStyle);

			tempLineEnd = new GfxGeometry("LineStringDrawController.updateLine");
			tempLineEnd.setStyle(drawStyle);
		}

		Coordinate[] srCoords = new Coordinate[geometry.getCoordinates().length];
		Coordinate[] worldCoords = geometry.getCoordinates();
		for (int i = 0; i < srCoords.length; i++) {
			srCoords[i] = getTransformer().worldToPan(worldCoords[i]);
		}

		LineString lineString = geometry.getGeometryFactory().createLineString(srCoords);
		tempLine.setGeometry(lineString);
	}

	private void updateTempLine(Geometry geometry, MouseEvent<?> event) {
		if (tempLine == null) {
			createTempLine(geometry);
		}

		Coordinate[] oldCoords = tempLine.getGeometry().getCoordinates();
		if (oldCoords != null && oldCoords.length > 0) {
			Coordinate[] newCoords = new Coordinate[oldCoords.length + 1];
			System.arraycopy(oldCoords, 0, newCoords, 0, oldCoords.length);
			newCoords[oldCoords.length] = getPanPosition(event);

			LineString lineString = geometry.getGeometryFactory().createLineString(newCoords);
			tempLineEnd.setGeometry(lineString);
			mapWidget.render(tempLineEnd, RenderGroup.VECTOR, RenderStatus.UPDATE);
		}
	}

	private void removeTempLine() {
		if (tempLine != null) {
			mapWidget.render(tempLine, RenderGroup.VECTOR, RenderStatus.DELETE);
			tempLine = null;
		}
		if (tempLineEnd != null) {
			mapWidget.render(tempLineEnd, RenderGroup.VECTOR, RenderStatus.DELETE);
			tempLineEnd = null;
		}
	}
}
