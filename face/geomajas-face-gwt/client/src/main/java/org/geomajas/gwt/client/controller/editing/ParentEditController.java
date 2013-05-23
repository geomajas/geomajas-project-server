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

import org.geomajas.gwt.client.action.menu.DeleteFeatureAction;
import org.geomajas.gwt.client.action.menu.DeselectAllAction;
import org.geomajas.gwt.client.action.menu.EditFeatureAction;
import org.geomajas.gwt.client.action.menu.NewFeatureAction;
import org.geomajas.gwt.client.action.menu.ToggleSelectionAction;
import org.geomajas.gwt.client.controller.PanController;
import org.geomajas.gwt.client.map.feature.TransactionGeomIndex;
import org.geomajas.gwt.client.widget.MapWidget;

import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.user.client.Event;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuItemSeparator;

/**
 * The parent editing controller. It delegates to the correct child editing controller if there is one. This controller
 * has a context menu that allows you to create, update or delete features on the map. By actually clicking the create
 * or update actions in the context menu, a child controller will be set on this parent that is perfectly suited for the
 * job. Each type of geometry (line, polygon, point, ...) has a specific child editing controller.
 * 
 * @author Pieter De Graef
 */
public class ParentEditController extends EditController {

	/** The real controller responsible for editing. */
	private EditController controller;
	
	private PanController panController;
	
	private TransactionGeomIndex geometryIndex;

	private int pixelTolerance = -1;

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------

	/**
	 * Constructor.
	 *
	 * @param mapWidget map widget
	 */
	public ParentEditController(MapWidget mapWidget) {
		super(mapWidget, null);
		panController = new PanController(mapWidget);
		panController.setShowCursorOnMove(true);
	}

	// -------------------------------------------------------------------------
	// MapController implementation:
	// -------------------------------------------------------------------------

	@Override
	public void onActivate() {
		menu = getContextMenu();
		mapWidget.setContextMenu(menu);
	}

	@Override
	public void onDeactivate() {
		if (menu != null) {
			menu.destroy();
			menu = null;
			mapWidget.setContextMenu(null);
		}
		if (controller != null) {
			controller.onDeactivate();
		}
	}

	// -------------------------------------------------------------------------
	// EditController implementation:
	// -------------------------------------------------------------------------

	@Override
	public Menu getContextMenu() {
		if (menu == null) {
			menu = new Menu();
			menu.addItem(new NewFeatureAction(mapWidget, this));
			menu.addItem(new EditFeatureAction(mapWidget, this));
			menu.addItem(new DeleteFeatureAction(mapWidget, this));
			menu.addItem(new MenuItemSeparator());
			menu.addItem(new DeselectAllAction(mapWidget.getMapModel()));
		}
		return menu;
	}

	@Override
	public TransactionGeomIndex getGeometryIndex() {
		return geometryIndex;
	}

	@Override
	public void setGeometryIndex(TransactionGeomIndex geometryIndex) {
		if (controller != null) {
			controller.setGeometryIndex(geometryIndex);
		}
		this.geometryIndex = geometryIndex;
	}

	@Override
	public void cleanup() {
		if (controller != null) {
			controller.cleanup();
			setController(null);
		}
	}

	@Override
	public void showGeometricInfo() {
		if (controller != null) {
			controller.showGeometricInfo();
		}
	}

	@Override
	public void updateGeometricInfo() {
		if (controller != null) {
			controller.updateGeometricInfo();
		}
	}

	@Override
	public void hideGeometricInfo() {
		if (controller != null) {
			controller.hideGeometricInfo();
		}
	}

	@Override
	public boolean isBusy() {
		// busy when inserting or dragging has started
		return controller != null && controller.isBusy();
	}


	// -------------------------------------------------------------------------
	// Event handler functions delegating to child controller:
	// -------------------------------------------------------------------------

	@Override
	public void onMouseDown(MouseDownEvent event) {
		if (controller != null) {
			controller.onMouseDown(event);
			if (!controller.isBusy()) {
				panController.onMouseDown(event);
			}
		} else {
			panController.onMouseDown(event);
		}
	}

	@Override
	public void onMouseUp(MouseUpEvent event) {
		if (controller != null) {
			controller.onMouseUp(event);
			if (!controller.isBusy()) {
				panController.onMouseUp(event);
			}
		} else {
			if (event.getNativeButton() != Event.BUTTON_RIGHT) {
				boolean moving = panController.isMoving();
				panController.onMouseUp(event);
				if (!moving) {
					// Check if we can toggle selection on a feature:
					ToggleSelectionAction action = new ToggleSelectionAction(mapWidget, pixelTolerance);
					action.toggle(getScreenPosition(event), true);
				}
			}
		}
	}

	@Override
	public void onMouseMove(MouseMoveEvent event) {
		if (controller != null) {
			controller.onMouseMove(event);
			if (!controller.isBusy()) {
				panController.onMouseMove(event);
			}
		} else {
			panController.onMouseMove(event);
		}
	}

	@Override
	public void onMouseOut(MouseOutEvent event) {
		if (controller != null) {
			controller.onMouseOut(event);
			if (!controller.isBusy()) {
				panController.onMouseOut(event);
			}
		} else {
			panController.onMouseOut(event);
		}
	}

	@Override
	public void onMouseOver(MouseOverEvent event) {
		if (controller != null) {
			controller.onMouseOver(event);
		}
	}

	@Override
	public void onMouseWheel(MouseWheelEvent event) {
		if (controller != null) {
			controller.onMouseWheel(event);
		}
	}

	@Override
	public void onDoubleClick(DoubleClickEvent event) {
		if (controller != null) {
			controller.onDoubleClick(event);
			if (!controller.isBusy()) {
				panController.onDoubleClick(event);
			}
		} else {
			panController.onDoubleClick(event);
		}
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	@Override
	public void setEditMode(EditMode currentMode) {
		super.setEditMode(currentMode);
		if (controller != null) {
			controller.setEditMode(currentMode);
		}
	}

	public EditController getController() {
		return controller;
	}

	/**
	 * Set a new child edit controller. If there was a previous child controller, then it's <code>onDeactivate</code>
	 * method will be called first. Likewise on the new controller, the <code>onActivate</code> will be called.
	 * 
	 * @param controller
	 *            The new child edit controller. A controller usually masters editing for one type of geometry. This
	 *            parameter can also be <code>null</code>, when no child edit controller is required anymore. If this
	 *            parameter is <code>null</code>, then this controller <code>onActivate</code> method is called.
	 */
	public void setController(EditController controller) {
		if (this.controller != null) {
			this.controller.onDeactivate();
		}
		this.controller = controller;
		if (controller != null) {
			controller.setMaxBoundsDisplayed(isMaxBoundsDisplayed());
			controller.onActivate();
		} else {
			onActivate();
		}
	}

	/**
	 * Set the pixel tolerance which is used to select objects.
	 *
	 * @param pixelTolerance pixel tolerance
	 */
	public void setPixelTolerance(int pixelTolerance) {
		this.pixelTolerance = pixelTolerance;
	}
}
