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

package org.geomajas.gwt.client.controller.editing;

import org.geomajas.gwt.client.action.menu.CancelEditingAction;
import org.geomajas.gwt.client.action.menu.SaveEditingAction;
import org.geomajas.gwt.client.action.menu.ToggleGeometricInfoAction;
import org.geomajas.gwt.client.action.menu.UndoOperationAction;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.feature.FeatureTransaction;
import org.geomajas.gwt.client.map.feature.TransactionGeomIndex;
import org.geomajas.gwt.client.map.feature.TransactionGeomIndexUtil;
import org.geomajas.gwt.client.map.feature.operation.AddCoordinateOp;
import org.geomajas.gwt.client.map.feature.operation.FeatureOperation;
import org.geomajas.gwt.client.map.feature.operation.SetCoordinateOp;
import org.geomajas.gwt.client.widget.MapWidget;

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
				dragTransaction = (FeatureTransaction) featureTransaction.clone();
				mapWidget.render(featureTransaction, "delete");
				mapWidget.render(dragTransaction, "all");
			}
		}
	}

	public void onMouseMove(MouseMoveEvent event) {
		if (getFeatureTransaction() != null && parent.getEditMode() == EditMode.DRAG_MODE && dragTargetId != null) {
			TransactionGeomIndex index = TransactionGeomIndexUtil.getIndex(dragTargetId);

			Feature feature = dragTransaction.getNewFeatures()[index.getFeatureIndex()];
			FeatureOperation op = new SetCoordinateOp(index, getWorldPosition(event));
			op.execute(feature);

			mapWidget.render(dragTransaction, "delete");
			mapWidget.render(dragTransaction, "all");
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

				mapWidget.render(featureTransaction, "delete");
				mapWidget.render(featureTransaction, "all");
				updateGeometricInfo();
			} else if (featureTransaction != null && parent.getEditMode() == EditMode.DRAG_MODE
					&& dragTargetId != null) {
				// Done dragging a point:
				TransactionGeomIndex index = TransactionGeomIndexUtil.getIndex(dragTargetId);
				// TODO: snap ???
				FeatureOperation op = new SetCoordinateOp(index, getWorldPosition(event));
				featureTransaction.execute(op);
				if (dragTransaction != null) {
					mapWidget.render(dragTransaction, "delete");
					dragTransaction = null;
				}
				mapWidget.render(featureTransaction, "all");
				dragTargetId = null;
				updateGeometricInfo();
			}
		}
	}
}
