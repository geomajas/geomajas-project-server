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

package org.geomajas.gwt.client.controller.editing;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.action.menu.AttributesAction;
import org.geomajas.gwt.client.action.menu.CancelEditingAction;
import org.geomajas.gwt.client.action.menu.InsertPointAction;
import org.geomajas.gwt.client.action.menu.RemovePointAction;
import org.geomajas.gwt.client.action.menu.SaveEditingAction;
import org.geomajas.gwt.client.action.menu.ToggleEditModeAction;
import org.geomajas.gwt.client.action.menu.ToggleGeometricInfoAction;
import org.geomajas.gwt.client.action.menu.ToggleSnappingAction;
import org.geomajas.gwt.client.action.menu.UndoOperationAction;
import org.geomajas.gwt.client.gfx.paintable.GfxGeometry;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.feature.FeatureTransaction;
import org.geomajas.gwt.client.map.feature.TransactionGeomIndex;
import org.geomajas.gwt.client.map.feature.TransactionGeomIndexUtil;
import org.geomajas.gwt.client.map.feature.operation.AddCoordinateOp;
import org.geomajas.gwt.client.map.feature.operation.FeatureOperation;
import org.geomajas.gwt.client.map.feature.operation.SetCoordinateOp;
import org.geomajas.gwt.client.spatial.geometry.LineString;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.MapWidget.RenderGroup;
import org.geomajas.gwt.client.widget.MapWidget.RenderStatus;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.user.client.Event;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItemSeparator;

/**
 * Editing controller for LineString geometries.
 * 
 * @author Pieter De Graef
 */
public class LineStringEditController extends EditController {

	protected TransactionGeomIndex index;

	private String dragTargetId;

	private FeatureTransaction dragTransaction;

	private GfxGeometry tempLine;

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------

	public LineStringEditController(MapWidget mapWidget, EditController parent) {
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
		}
		return index;
	}

	public void setGeometryIndex(TransactionGeomIndex geometryIndex) {
	}

	public void cleanup() {
		removeTempLine();
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
				mapWidget.render(featureTransaction, RenderGroup.VECTOR, RenderStatus.DELETE);
				mapWidget.render(dragTransaction, RenderGroup.VECTOR, RenderStatus.ALL);
				createTempLine(featureTransaction, event);
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

			mapWidget.render(dragTransaction, RenderGroup.VECTOR, RenderStatus.DELETE);
			mapWidget.render(dragTransaction, RenderGroup.VECTOR, RenderStatus.ALL);
		} else if (featureTransaction != null && parent.getEditMode() == EditMode.INSERT_MODE) {
			updateTempLine(featureTransaction, event);
		}
	}

	public void onMouseUp(MouseUpEvent event) {
		if (event.getNativeButton() != Event.BUTTON_RIGHT) {
			FeatureTransaction featureTransaction = getFeatureTransaction();
			if (featureTransaction != null && parent.getEditMode() == EditMode.INSERT_MODE) {
				// The creation of a new point:
				FeatureOperation op = new AddCoordinateOp(getGeometryIndex(), getWorldPosition(event));
				featureTransaction.execute(op);
				mapWidget.render(featureTransaction, RenderGroup.VECTOR, RenderStatus.DELETE);
				mapWidget.render(featureTransaction, RenderGroup.VECTOR, RenderStatus.ALL);
				updateGeometricInfo();
			} else if (featureTransaction != null && parent.getEditMode() == EditMode.DRAG_MODE &&
					dragTargetId != null) {
				// Done dragging a point:
				TransactionGeomIndex index = TransactionGeomIndexUtil.getIndex(dragTargetId);
				// TODO: check validity
				FeatureOperation op = new SetCoordinateOp(index, getWorldPosition(event));
				featureTransaction.execute(op);
				if (dragTransaction != null) {
					mapWidget.render(dragTransaction, RenderGroup.VECTOR, RenderStatus.DELETE);
					dragTransaction = null;
				}
				mapWidget.render(featureTransaction, RenderGroup.VECTOR, RenderStatus.ALL);
				dragTargetId = null;
				removeTempLine();
				updateGeometricInfo();
			}
		}
	}

	// Getters and setters:

	public void setEditMode(EditMode editMode) {
		super.setEditMode(editMode);
		if (editMode == EditMode.DRAG_MODE) {
			removeTempLine();
		}
	}

	// Private methods:

	private void createTempLine(FeatureTransaction featureTransaction, MouseEvent<?> event) {
		if (featureTransaction.getNewFeatures() != null && featureTransaction.getNewFeatures().length > 0
				&& tempLine == null) {
			Coordinate position = getPanPosition(event);
			LineString lineString = getGeometryIndex().getGeometry(featureTransaction).getGeometryFactory()
					.createLineString(new Coordinate[] { position, position });
			tempLine = new GfxGeometry("LineStringEditController.updateLine");
			tempLine.setGeometry(lineString);
			tempLine.setStyle(new ShapeStyle("#FFFFFF", 0, "#FF3322", 1, 1));
			mapWidget.render(tempLine, RenderGroup.VECTOR, RenderStatus.ALL);
		}
	}

	private void updateTempLine(FeatureTransaction featureTransaction, MouseEvent<?> event) {
		if (featureTransaction.getNewFeatures() != null && featureTransaction.getNewFeatures().length > 0) {
			if (tempLine == null) {
				createTempLine(featureTransaction, event);
			}
			Coordinate[] coordinates = getGeometryIndex().getGeometry(featureTransaction).getCoordinates();
			if (coordinates != null && coordinates.length > 0) {
				Coordinate lastCoordinate = getTransformer().worldToPan(coordinates[coordinates.length - 1]);
				LineString lineString = featureTransaction.getNewFeatures()[0].getGeometry().getGeometryFactory()
						.createLineString(new Coordinate[] { lastCoordinate, getPanPosition(event) });
				tempLine.setGeometry(lineString);
				mapWidget.render(tempLine, RenderGroup.VECTOR, RenderStatus.ALL);
			}
		}
	}

	private void removeTempLine() {
		if (tempLine != null) {
			mapWidget.render(tempLine, RenderGroup.VECTOR, RenderStatus.DELETE);
			tempLine = null;
		}
	}
}
