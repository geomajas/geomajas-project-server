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

import org.geomajas.smartgwt.client.action.menu.AttributesAction;
import org.geomajas.smartgwt.client.action.menu.CancelEditingAction;
import org.geomajas.smartgwt.client.action.menu.SaveEditingAction;
import org.geomajas.smartgwt.client.action.menu.ToggleGeometricInfoAction;
import org.geomajas.smartgwt.client.action.menu.UndoOperationAction;
import org.geomajas.smartgwt.client.map.feature.Feature;
import org.geomajas.smartgwt.client.map.feature.FeatureTransaction;
import org.geomajas.smartgwt.client.map.feature.TransactionGeomIndex;
import org.geomajas.smartgwt.client.map.feature.TransactionGeomIndexUtil;
import org.geomajas.smartgwt.client.map.feature.operation.AddCoordinateOp;
import org.geomajas.smartgwt.client.map.feature.operation.FeatureOperation;
import org.geomajas.smartgwt.client.map.feature.operation.SetCoordinateOp;
import org.geomajas.smartgwt.client.widget.MapWidget;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.user.client.Event;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItemSeparator;

/**
 * Editing controller for Point geometries.
 * 
 * @author Pieter De Graef
 */
public class PointEditController extends EditController {

	private TransactionGeomIndex index;

	private String dragTargetId;

	private FeatureTransaction dragTransaction;

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------

	public PointEditController(MapWidget mapWidget, EditController parent) {
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
			menu.addItem(new MenuItemSeparator());
			menu.addItem(new ToggleGeometricInfoAction(this));
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
			if (TransactionGeomIndexUtil.isDraggable(targetId)) {
				dragTargetId = targetId;
				if (dragTransaction == null) {
					dragTransaction = (FeatureTransaction) featureTransaction.clone();
				}
				mapWidget.render(featureTransaction, MapWidget.RenderGroup.VECTOR, MapWidget.RenderStatus.DELETE);
				mapWidget.render(dragTransaction, MapWidget.RenderGroup.VECTOR, MapWidget.RenderStatus.ALL);
			}
		}
	}

	public void onMouseMove(MouseMoveEvent event) {
		if (getFeatureTransaction() != null && parent.getEditMode() == EditMode.DRAG_MODE && dragTargetId != null) {
			TransactionGeomIndex index = TransactionGeomIndexUtil.getIndex(dragTargetId);

			Feature feature = dragTransaction.getNewFeatures()[index.getFeatureIndex()];
			FeatureOperation op = new SetCoordinateOp(index, getWorldPosition(event));
			op.execute(feature);

			mapWidget.render(dragTransaction, MapWidget.RenderGroup.VECTOR, MapWidget.RenderStatus.DELETE);
			mapWidget.render(dragTransaction, MapWidget.RenderGroup.VECTOR, MapWidget.RenderStatus.ALL);
		}
	}

	public void onMouseUp(MouseUpEvent event) {
		if (event.getNativeButton() != Event.BUTTON_RIGHT) {
			FeatureTransaction featureTransaction = getFeatureTransaction();
			if (featureTransaction != null && parent.getEditMode() == EditMode.INSERT_MODE) {
				// The creation of a new point:
				FeatureOperation op = new AddCoordinateOp(getGeometryIndex(), getWorldPosition(event));
				featureTransaction.execute(op);
				parent.setEditMode(EditMode.DRAG_MODE);

				mapWidget.render(featureTransaction, MapWidget.RenderGroup.VECTOR, MapWidget.RenderStatus.DELETE);
				mapWidget.render(featureTransaction, MapWidget.RenderGroup.VECTOR, MapWidget.RenderStatus.ALL);
				updateGeometricInfo();
			} else if (featureTransaction != null && parent.getEditMode() == EditMode.DRAG_MODE
					&& dragTargetId != null) {
				// Done dragging a point:
				TransactionGeomIndex index = TransactionGeomIndexUtil.getIndex(dragTargetId);
				// TODO: snap ???
				FeatureOperation op = new SetCoordinateOp(index, getWorldPosition(event));
				featureTransaction.execute(op);
				if (dragTransaction != null) {
					mapWidget.render(dragTransaction, MapWidget.RenderGroup.VECTOR, MapWidget.RenderStatus.DELETE);
					dragTransaction = null;
				}
				mapWidget.render(featureTransaction, MapWidget.RenderGroup.VECTOR, MapWidget.RenderStatus.ALL);
				dragTargetId = null;
				updateGeometricInfo();
			}
		}
	}
}
