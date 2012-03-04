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

import org.geomajas.configuration.CircleInfo;
import org.geomajas.configuration.SymbolInfo;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.controller.editing.EditController.EditMode;
import org.geomajas.gwt.client.gfx.paintable.GfxGeometry;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.map.feature.TransactionGeomIndex;
import org.geomajas.gwt.client.spatial.geometry.GeometryFactory;
import org.geomajas.gwt.client.spatial.geometry.Point;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.MapWidget.RenderGroup;
import org.geomajas.gwt.client.widget.MapWidget.RenderStatus;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.user.client.Event;

/**
 * Drawing controller for Point geometries.
 * 
 * @author Bruce Palmkoeck
 */
public class PointDrawController extends AbstractFreeDrawingController {

	protected TransactionGeomIndex index;

	private String dragTargetId;

	private GfxGeometry tempPoint;

	private ShapeStyle drawStyle;

	private SymbolInfo symbolStyle;

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------

	public PointDrawController(MapWidget mapWidget, AbstractFreeDrawingController parent, GeometryDrawHandler handler) {
		super(mapWidget, parent, handler);
		factory = new GeometryFactory(mapWidget.getMapModel().getSrid(), mapWidget.getMapModel().getPrecision());
		geometry = factory.createPoint(new Coordinate());
		drawStyle = new ShapeStyle("#FF7F00", 0.3f, "#FF7F00", 1, 2);
		symbolStyle = new SymbolInfo();
		CircleInfo c = new CircleInfo();
		symbolStyle.setCircle(c);
		c.setR(8);
	}

	// -------------------------------------------------------------------------
	// DrawController implementation:
	// -------------------------------------------------------------------------

	public void cleanup() {
		removeTempPoint();
	}

	public boolean isBusy() {
		// busy when inserting or dragging has started
		return getEditMode() == EditMode.INSERT_MODE || (getEditMode() == EditMode.DRAG_MODE && dragTargetId != null);
	}

	// -------------------------------------------------------------------------
	// MapController implementation:
	// -------------------------------------------------------------------------

	public void onMouseDown(MouseDownEvent event) {
		Coordinate newCoords = getWorldPosition(event);
		geometry = factory.createPoint(newCoords);
	}

	public void onMouseUp(MouseUpEvent event) {
		if (event.getNativeButton() != Event.BUTTON_RIGHT) {
			removeTempPoint();
			createTempPoint();
			handler.onDraw(getGeometry());
		}
	}

	// Getters and setters:

	public void setEditMode(EditMode editMode) {
		super.setEditMode(editMode);
		if (editMode == EditMode.DRAG_MODE) {
			removeTempPoint();
		}
	}

	// Private methods:

	private void createTempPoint() {
		tempPoint = new GfxGeometry("PointDrawController.updatePoint");
		tempPoint.setStyle(drawStyle);
		tempPoint.setSymbolInfo(symbolStyle);

		Coordinate coords = getTransformer().worldToPan(geometry.getCoordinate());
		Point point = (Point) geometry.getGeometryFactory().createPoint(coords);
		tempPoint.setGeometry(point);
		mapWidget.render(tempPoint, RenderGroup.VECTOR, RenderStatus.ALL);
	}

	private void removeTempPoint() {
		if (tempPoint != null) {
			mapWidget.render(tempPoint, RenderGroup.VECTOR, RenderStatus.DELETE);
			tempPoint = null;
		}
	}
}
