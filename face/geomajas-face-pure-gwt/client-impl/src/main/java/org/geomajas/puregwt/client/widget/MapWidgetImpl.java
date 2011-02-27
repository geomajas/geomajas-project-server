/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.puregwt.client.widget;

import org.geomajas.puregwt.client.map.MapPresenterImpl.MapWidget;
import org.geomajas.puregwt.client.map.gfx.HtmlContainer;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.Widget;

/**
 * ...
 * 
 * @author Pieter De Graef
 */
public class MapWidgetImpl extends FocusWidget implements MapWidget {

	private HtmlContainer htmlContainer;

	private Element clipElement;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	// @Inject
	public MapWidgetImpl() {
		Element rootNode = DOM.createElement("div");
		setElement(rootNode);

		// Create an extra element for clipping (overflow=hidden). Can't be done on the top level, because the top level
		// can not have an absolute position (that's up to GWT).
		clipElement = DOM.createElement("div");
		DOM.setStyleAttribute(clipElement, "overflow", "hidden");
		DOM.setStyleAttribute(clipElement, "position", "absolute");
		DOM.appendChild(getElement(), clipElement);

		// Attach an HtmlContainer inside the clipping area (used for rendering layers):
		htmlContainer = new HtmlContainer();
		htmlContainer.setParent(this);
		DOM.appendChild(clipElement, htmlContainer.getElement());

		// Firefox and Chrome allow for DnD of images. This default behavior is not wanted.
		addMouseDownHandler(new MouseDownHandler() {

			public void onMouseDown(MouseDownEvent event) {
				event.preventDefault();
			}
		});
		addMouseMoveHandler(new MouseMoveHandler() {

			public void onMouseMove(MouseMoveEvent event) {
				event.preventDefault();
			}
		});
	}

	public Widget asWidget() {
		return this;
	}

	public HtmlContainer getHtmlContainer() {
		return htmlContainer;
	}

	// ------------------------------------------------------------------------
	// Overriding resize methods:
	// ------------------------------------------------------------------------

	public void setPixelSize(int width, int height) {
		DOM.setStyleAttribute(clipElement, "width", width + "px");
		DOM.setStyleAttribute(clipElement, "height", height + "px");
		htmlContainer.setPixelSize(width, height);
		super.setPixelSize(width, height);
	}

	public void setSize(String width, String height) {
		DOM.setStyleAttribute(clipElement, "width", width);
		DOM.setStyleAttribute(clipElement, "height", height);
		htmlContainer.setSize(width, height);
		super.setSize(width, height);
	}

	public void setWidth(String width) {
		DOM.setStyleAttribute(clipElement, "width", width);
		htmlContainer.setWidth(width);
		super.setWidth(width);
	}

	public void setHeight(String height) {
		DOM.setStyleAttribute(clipElement, "height", height);
		htmlContainer.setHeight(height);
		super.setHeight(height);
	}
}