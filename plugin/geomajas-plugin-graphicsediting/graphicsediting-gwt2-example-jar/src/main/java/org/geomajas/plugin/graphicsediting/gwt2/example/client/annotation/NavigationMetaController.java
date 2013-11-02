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
package org.geomajas.plugin.graphicsediting.gwt2.example.client.annotation;

import org.geomajas.graphics.client.controller.MetaController;
import org.geomajas.graphics.client.object.GraphicsObject;
import org.geomajas.graphics.client.service.GraphicsService;
import org.geomajas.gwt2.client.map.MapPresenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseWheelEvent;

/**
 * Extension of {@link MetaController}.
 * 
 * @author Jan De Moerloose
 * 
 */
public class NavigationMetaController extends MetaController {

	private IntervalNavigationController navigationController;
	
	private NoActionMapController noController;
	
	private boolean objectActive;

	private MapPresenter mapPresenter;

	public NavigationMetaController(GraphicsService service, MapPresenter mapPresenter) {
		super(service);
		navigationController = new IntervalNavigationController();
		navigationController.onActivate(mapPresenter);
		noController = new NoActionMapController();
		noController.onActivate(mapPresenter);
		this.mapPresenter = mapPresenter;
		getObjectContainer().getBackGround().addMouseDownHandler(this);
	}

	
	@Override
	public void setActive(boolean active) {
		if (active != this.active) {
			this.active = active;
			if (active) {
				// for activation of objects
				for (GraphicsObject object : getObjectContainer().getObjects()) {
					register(object.asObject().addMouseDownHandler(this));
				}
				// for deactivating
				register(getObjectContainer().getBackGround().addMouseDownHandler(this));
				// for preventing events to bubble up
				register(getObjectContainer().addMouseDownHandler(this));
				register(getObjectContainer().addMouseUpHandler(this));
				register(getObjectContainer().addMouseMoveHandler(this));
				register(getObjectContainer().addMouseOverHandler(this));
				register(getObjectContainer().addMouseOutHandler(this));
				register(getObjectContainer().addMouseWheelHandler(this));
				register(getObjectContainer().addClickHandler(this));
				register(getObjectContainer().addDoubleClickHandler(this));
			} else {
				deactivateAllControllers();
				objectActive = false;
				unregister();
			}
			mapPresenter.setMapController(active ? noController : navigationController);
		}
		navigationController.stopDragging();
	}
	
	public boolean isObjectActive() {
		return objectActive;
	}

	@Override
	public void onClick(ClickEvent event) {
		if (isActive()) {
			// prevent trickle through to map controller
			event.stopPropagation();
			event.preventDefault();
		}
	}

	@Override
	public void onDoubleClick(DoubleClickEvent event) {
		if (isActive()) {
			// prevent trickle through to map controller
			event.stopPropagation();
			event.preventDefault();
		}
	}

	@Override
	public void onMouseDown(MouseDownEvent event) {
		if (isActive()) {
			if (!isObjectActive() && getVectorObjects().contains(event.getSource())) {
				// activate controllers for this object
				for (GraphicsObject object : getObjectContainer().getObjects()) {
					if (object.asObject() == event.getSource()) {
						activateControllersForObject(object, event);
						objectActive = true;
						break;
					}
				}
			} else if (event.getSource() == getObjectContainer().getBackGround()) {
				deactivateAllControllers();
				objectActive = false;
				navigationController.onMouseDown(event);
			}
			// prevent trickle through to map controller
			event.stopPropagation();
			event.preventDefault();
		}
	}

	@Override
	public void onMouseMove(MouseMoveEvent event) {
		if (isActive()) {
			if (!isObjectActive()) {
				navigationController.onMouseMove(event);
			}
			// prevent trickle through to map controller
			event.stopPropagation();
			event.preventDefault();
		}
	}

	@Override
	public void onMouseUp(MouseUpEvent event) {
		if (isActive()) {
			if (!isObjectActive()) {
				navigationController.onMouseUp(event);
			}
			// prevent trickle through to map controller
			event.stopPropagation();
			event.preventDefault();
		}
	}

	@Override
	public void onMouseOut(MouseOutEvent event) {
		if (isActive()) {
			if (!isObjectActive()) {
				navigationController.onMouseOut(event);
			}
			// prevent trickle through to map controller
			event.stopPropagation();
			event.preventDefault();
		}
	}

	@Override
	public void onMouseWheel(MouseWheelEvent event) {
		if (isActive()) {
			if (!isObjectActive()) {
				navigationController.onMouseWheel(event);
			}
			// prevent trickle through to map controller
			event.stopPropagation();
			event.preventDefault();
		}
	}

}
