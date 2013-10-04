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

package org.geomajas.gwt2.client.widget;

import org.geomajas.gwt2.client.map.MapPresenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.ui.Composite;

/**
 * Simple base implementation of a widget that can be placed on top of the map. It provides a handler to stop events
 * from bubbling to the actual map controllers, which might be handy. Of course, you don't have to use this, as any
 * widget can be placed on the map.
 * 
 * @author Pieter De Graef
 */
public abstract class AbstractMapWidget extends Composite {

	protected final MapPresenter mapPresenter;

	public AbstractMapWidget(MapPresenter mapPresenter) {
		this.mapPresenter = mapPresenter;
	}

	// ------------------------------------------------------------------------
	// Getters and setters:
	// ------------------------------------------------------------------------

	/**
	 * Combination of different handlers with a single goal: stop all the events from propagating to the map. This is
	 * meant to be used for clickable widgets.
	 * 
	 * @author Pieter De Graef
	 */
	public class StopPropagationHandler implements MouseDownHandler, MouseUpHandler, ClickHandler, DoubleClickHandler {

		public void onDoubleClick(DoubleClickEvent event) {
			event.stopPropagation();
		}

		public void onClick(ClickEvent event) {
			event.stopPropagation();
		}

		public void onMouseDown(MouseDownEvent event) {
			event.stopPropagation();
			event.preventDefault();
		}

		public void onMouseUp(MouseUpEvent event) {
			event.stopPropagation();
		}
	}
}