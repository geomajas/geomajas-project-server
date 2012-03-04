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

import org.geomajas.gwt.client.controller.editing.EditController.EditMode;
import org.geomajas.gwt.client.widget.MapWidget;

import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseWheelEvent;

/**
 * The parent drawing controller. It delegates to the correct child drawing controller if there is one. Each type of
 * geometry (line, polygon, point, ...) has a specific child editing controller.
 * 
 * @author Bruce Palmkoeck
 */
public class ParentDrawController extends AbstractFreeDrawingController {

	/** The real controller responsible for drawing. */
	private AbstractFreeDrawingController controller;

	protected ParentDrawController(MapWidget mapWidget, GeometryDrawHandler handler) {
		super(mapWidget, null, handler);
	}

	@Override
	public void cleanup() {
		if (controller != null) {
			controller.cleanup();
			setController(null);
		}
	}
	@Override
	public boolean isBusy() {
		return false;
	}

	// -------------------------------------------------------------------------
	// Event handler functions delegating to child controller:
	// -------------------------------------------------------------------------

	public void onMouseDown(MouseDownEvent event) {
		if (controller != null) {
			controller.onMouseDown(event);
		}
	}

	public void onMouseUp(MouseUpEvent event) {
		if (controller != null) {
			controller.onMouseUp(event);
		}
	}

	public void onMouseMove(MouseMoveEvent event) {
		if (controller != null) {
			controller.onMouseMove(event);
		}
	}

	public void onMouseOut(MouseOutEvent event) {
		if (controller != null) {
			controller.onMouseOut(event);
		}

	}

	public void onMouseOver(MouseOverEvent event) {
		if (controller != null) {
			controller.onMouseOver(event);
		}
	}

	public void onMouseWheel(MouseWheelEvent event) {
		if (controller != null) {
			controller.onMouseWheel(event);
		}
	}

	public void onDoubleClick(DoubleClickEvent event) {
		if (controller != null) {
			controller.onDoubleClick(event);
		}
	}

	// -------------------------------------------------------------------------
	// Getters and setters:
	// -------------------------------------------------------------------------

	public void setEditMode(EditMode currentMode) {
		super.setEditMode(currentMode);
		if (controller != null) {
			controller.setEditMode(currentMode);
		}
	}

	public AbstractFreeDrawingController getController() {
		return controller;
	}

	/**
	 * Set a new child draw controller. If there was a previous child controller, then it's <code>onDeactivate</code>
	 * method will be called first. Likewise on the new controller, the <code>onActivate</code> will be called.
	 * 
	 * @param controller
	 *            The new child draw controller. A controller usually masters drawing for one type of geometry. This
	 *            parameter can also be <code>null</code>, when no child draw controller is required anymore. If this
	 *            parameter is <code>null</code>, then this controller <code>onActivate</code> method is called.
	 */
	public void setController(AbstractFreeDrawingController controller) {
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

}
