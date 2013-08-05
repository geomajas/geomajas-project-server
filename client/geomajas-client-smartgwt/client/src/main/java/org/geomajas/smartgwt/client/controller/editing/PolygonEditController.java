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

package org.geomajas.smartgwt.client.controller.editing;

import org.geomajas.geometry.Coordinate;
import org.geomajas.smartgwt.client.action.menu.AttributesAction;
import org.geomajas.smartgwt.client.action.menu.CancelEditingAction;
import org.geomajas.smartgwt.client.action.menu.InsertPointAction;
import org.geomajas.smartgwt.client.action.menu.InsertRingAction;
import org.geomajas.smartgwt.client.action.menu.RemovePointAction;
import org.geomajas.smartgwt.client.action.menu.RemoveRingAction;
import org.geomajas.smartgwt.client.action.menu.SaveEditingAction;
import org.geomajas.smartgwt.client.action.menu.ToggleEditModeAction;
import org.geomajas.smartgwt.client.action.menu.ToggleGeometricInfoAction;
import org.geomajas.smartgwt.client.action.menu.ToggleSnappingAction;
import org.geomajas.smartgwt.client.action.menu.UndoOperationAction;
import org.geomajas.smartgwt.client.gfx.paintable.GfxGeometry;
import org.geomajas.smartgwt.client.gfx.style.ShapeStyle;
import org.geomajas.smartgwt.client.map.feature.Feature;
import org.geomajas.smartgwt.client.map.feature.FeatureTransaction;
import org.geomajas.smartgwt.client.map.feature.TransactionGeomIndex;
import org.geomajas.smartgwt.client.map.feature.TransactionGeomIndexUtil;
import org.geomajas.smartgwt.client.map.feature.operation.AddCoordinateOp;
import org.geomajas.smartgwt.client.map.feature.operation.FeatureOperation;
import org.geomajas.smartgwt.client.map.feature.operation.SetCoordinateOp;
import org.geomajas.smartgwt.client.spatial.geometry.Geometry;
import org.geomajas.smartgwt.client.spatial.geometry.LineString;
import org.geomajas.smartgwt.client.spatial.geometry.LinearRing;
import org.geomajas.smartgwt.client.spatial.geometry.Polygon;
import org.geomajas.smartgwt.client.widget.MapWidget;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.user.client.Event;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItemSeparator;

/**
 * Editing controller for Polygon geometries.
 * 
 * @author Pieter De Graef
 */
public class PolygonEditController extends EditController {

	protected String dragTargetId;

	protected FeatureTransaction dragTransaction;

	protected GfxGeometry tempLine1;

	protected GfxGeometry tempLine2;

	protected TransactionGeomIndex index;

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------

	public PolygonEditController(MapWidget mapWidget, EditController parent) {
		super(mapWidget, parent);
	}

	// -------------------------------------------------------------------------
	// EditController implementation:
	// -------------------------------------------------------------------------

	public Menu getContextMenu() {
		if (menu == null) {
			menu = new Menu();
			menu.addItem(new AttributesAction(mapWidget));
			menu.addItem(new MenuItemSeparator());
			menu.addItem(new UndoOperationAction(mapWidget, this));
			menu.addItem(new CancelEditingAction(mapWidget, (ParentEditController) parent));
			menu.addItem(new SaveEditingAction(mapWidget, (ParentEditController) parent));
			menu.addItem(new ToggleEditModeAction((ParentEditController) parent));
			menu.addItem(new MenuItemSeparator());
			menu.addItem(new InsertPointAction(mapWidget));
			menu.addItem(new RemovePointAction(mapWidget));
			menu.addItem(new MenuItemSeparator());
			menu.addItem(new InsertRingAction(mapWidget, (ParentEditController) parent));
			menu.addItem(new RemoveRingAction(mapWidget));
			menu.addItem(new MenuItemSeparator());
			menu.addItem(new ToggleGeometricInfoAction(this));
			menu.addItem(new ToggleSnappingAction(getFeatureTransaction().getLayer(), this));
		}
		return menu;
	}

	public TransactionGeomIndex getGeometryIndex() {
		if (index == null) {
			index = new TransactionGeomIndex();
			index.setFeatureIndex(0);
			index.setCoordinateIndex(0);
			index.setExteriorRing(true);
		}
		return index;
	}

	public void setGeometryIndex(TransactionGeomIndex geometryIndex) {
		index = geometryIndex;
	}

	public void cleanup() {
		removeTempLines();
	}

	public boolean isBusy() {
		// busy when inserting or dragging has started
		return getEditMode() == EditMode.INSERT_MODE || (getEditMode() == EditMode.DRAG_MODE && dragTargetId != null);
	}

	// -------------------------------------------------------------------------
	// MapController implementation:
	// -------------------------------------------------------------------------

	public void onMouseDown(MouseDownEvent event) {
		FeatureTransaction featureTransaction = getFeatureTransaction();
		if (featureTransaction != null && parent.getEditMode() == EditMode.DRAG_MODE
				&& event.getNativeButton() != Event.BUTTON_RIGHT) {
			String targetId = getTargetId(event);
			if (TransactionGeomIndexUtil.isVertex(targetId)) {
				dragTargetId = targetId;
				if (dragTransaction == null) {
					dragTransaction = (FeatureTransaction) featureTransaction.clone();
				}
				mapWidget.render(featureTransaction, MapWidget.RenderGroup.VECTOR, MapWidget.RenderStatus.DELETE);
				mapWidget.render(dragTransaction, MapWidget.RenderGroup.VECTOR, MapWidget.RenderStatus.ALL);
				createTempLines(featureTransaction, event);
			}
		}
	}

	public void onMouseMove(MouseMoveEvent event) {
		FeatureTransaction featureTransaction = getFeatureTransaction();
		if (featureTransaction != null && parent.getEditMode() == EditMode.DRAG_MODE && dragTargetId != null) {
			TransactionGeomIndex index = TransactionGeomIndexUtil.getIndex(dragTargetId);

			Feature feature = dragTransaction.getNewFeatures()[index.getFeatureIndex()];
			FeatureOperation op = new SetCoordinateOp(index, getWorldPosition(event));
			op.execute(feature);

			mapWidget.render(dragTransaction, MapWidget.RenderGroup.VECTOR, MapWidget.RenderStatus.DELETE);
			mapWidget.render(dragTransaction, MapWidget.RenderGroup.VECTOR, MapWidget.RenderStatus.ALL);
		} else if (featureTransaction != null && parent.getEditMode() == EditMode.INSERT_MODE) {
			updateTempLines(featureTransaction, event);
		}
	}

	public void onMouseUp(MouseUpEvent event) {
		if (event.getNativeButton() != Event.BUTTON_RIGHT) {
			FeatureTransaction featureTransaction = getFeatureTransaction();
			if (featureTransaction != null && parent.getEditMode() == EditMode.INSERT_MODE) {
				// The creation of a new point:
				FeatureOperation op = new AddCoordinateOp(getGeometryIndex(), getWorldPosition(event));
				featureTransaction.execute(op);
				mapWidget.render(featureTransaction, MapWidget.RenderGroup.VECTOR, MapWidget.RenderStatus.DELETE);
				mapWidget.render(featureTransaction, MapWidget.RenderGroup.VECTOR, MapWidget.RenderStatus.ALL);
				updateGeometricInfo();
			} else if (featureTransaction != null && parent.getEditMode() == EditMode.DRAG_MODE
					&& dragTargetId != null) {
				// Done dragging a point:
				TransactionGeomIndex index = TransactionGeomIndexUtil.getIndex(dragTargetId);
				// TODO: check validity
				FeatureOperation op = new SetCoordinateOp(index, getWorldPosition(event));
				featureTransaction.execute(op);
				if (dragTransaction != null) {
					mapWidget.render(dragTransaction, MapWidget.RenderGroup.VECTOR, MapWidget.RenderStatus.DELETE);
					dragTransaction = null;
				}
				mapWidget.render(featureTransaction, MapWidget.RenderGroup.VECTOR, MapWidget.RenderStatus.ALL);
				dragTargetId = null;
				removeTempLines();
				updateGeometricInfo();
			}
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

	protected void createTempLines(FeatureTransaction featureTransaction, MouseEvent<?> event) {
		if (featureTransaction.getNewFeatures() != null && featureTransaction.getNewFeatures().length > 0
				&& tempLine1 == null) {
			Coordinate position = getPanPosition(event);
			Geometry geom = getGeometryIndex().getGeometry(featureTransaction);
			LineString lineString = geom.getGeometryFactory().createLineString(new Coordinate[] { position, position });
			tempLine1 = new GfxGeometry("LineStringEditController.updateLine1");
			tempLine1.setGeometry(lineString);
			tempLine1.setStyle(new ShapeStyle("#FFFFFF", 0, "#FF3322", 1, 1));
			mapWidget.render(tempLine1, MapWidget.RenderGroup.VECTOR, MapWidget.RenderStatus.ALL);
			tempLine2 = new GfxGeometry("LineStringEditController.updateLine2");
			tempLine2.setGeometry(lineString);
			tempLine2.setStyle(new ShapeStyle("#FFFFFF", 0, "#FF3322", 1, 1));
			mapWidget.render(tempLine2, MapWidget.RenderGroup.VECTOR, MapWidget.RenderStatus.ALL);
		}
	}

	protected void updateTempLines(FeatureTransaction featureTransaction, MouseEvent<?> event) {
		if (featureTransaction.getNewFeatures() != null && featureTransaction.getNewFeatures().length > 0) {
			if (tempLine1 == null) {
				createTempLines(featureTransaction, event);
			}
			Polygon polygon = (Polygon) getGeometryIndex().getGeometry(featureTransaction);
			LinearRing ring = getGeometryIndex().getLinearRing(polygon);
			if (ring != null) {
				Coordinate[] coordinates = ring.getCoordinates();
				if (coordinates != null && coordinates.length > 0) {
					Coordinate lastCoordinate = coordinates[coordinates.length - 2];
					LineString lineString1 = featureTransaction.getNewFeatures()[index.getFeatureIndex()].getGeometry()
							.getGeometryFactory().createLineString(
									new Coordinate[] {getTransformer().worldToPan(lastCoordinate),
											getPanPosition(event)});
					tempLine1.setGeometry(lineString1);
					mapWidget.render(tempLine1, MapWidget.RenderGroup.VECTOR, MapWidget.RenderStatus.ALL);

					LineString lineString2 = featureTransaction.getNewFeatures()[index.getFeatureIndex()].getGeometry()
							.getGeometryFactory().createLineString(
									new Coordinate[] {getTransformer().worldToPan(coordinates[0]),
											getPanPosition(event)});
					tempLine2.setGeometry(lineString2);
					mapWidget.render(tempLine2, MapWidget.RenderGroup.VECTOR, MapWidget.RenderStatus.ALL);
				}
			}
		}
	}

	protected void removeTempLines() {
		if (tempLine1 != null) {
			mapWidget.render(tempLine1, MapWidget.RenderGroup.VECTOR, MapWidget.RenderStatus.DELETE);
			tempLine1 = null;
		}
		if (tempLine2 != null) {
			mapWidget.render(tempLine2, MapWidget.RenderGroup.VECTOR, MapWidget.RenderStatus.DELETE);
			tempLine2 = null;
		}
	}
}
