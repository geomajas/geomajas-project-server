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

package org.geomajas.puregwt.client.widget;

import java.util.List;

import org.geomajas.puregwt.client.gfx.HtmlContainer;
import org.geomajas.puregwt.client.gfx.VectorContainer;
import org.geomajas.puregwt.client.map.MapPresenterImpl.MapWidget;

import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Test implementation of a MapWidget. The GIN/Guice module in test is configured to use this implementation instead of
 * the real MapWidgetImpl.
 * 
 * @author Pieter De Graef
 */
public class MapWidgetTestImpl implements MapWidget {

	public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
		return null;
	}

	public void fireEvent(GwtEvent<?> event) {
	}

	public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
		return null;
	}

	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
		return null;
	}

	public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
		return null;
	}

	public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
		return null;
	}

	public HandlerRegistration addMouseWheelHandler(MouseWheelHandler handler) {
		return null;
	}

	public HandlerRegistration addDoubleClickHandler(DoubleClickHandler handler) {
		return null;
	}

	public Widget asWidget() {
		return new Widget();
	}

	public HtmlContainer getMapHtmlContainer() {
		return null;
	}

	public VectorContainer getMapVectorContainer() {
		return null;
	}

	public List<VectorContainer> getWorldVectorContainers() {
		return null;
	}

	public VectorContainer getNewScreenContainer() {
		return null;
	}

	public VectorContainer getNewWorldContainer() {
		return null;
	}

	public boolean removeVectorContainer(VectorContainer container) {
		return false;
	}

	public boolean bringToFront(VectorContainer container) {
		return false;
	}

	public void onResize() {
	}

	public AbsolutePanel getMapGadgetContainer() {
		// TODO Auto-generated method stub
		return null;
	}
}