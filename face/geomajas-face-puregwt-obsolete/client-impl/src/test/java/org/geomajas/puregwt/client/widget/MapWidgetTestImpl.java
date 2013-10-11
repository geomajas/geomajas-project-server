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

package org.geomajas.puregwt.client.widget;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.geomajas.puregwt.client.gfx.CanvasContainer;
import org.geomajas.puregwt.client.gfx.HtmlContainer;
import org.geomajas.puregwt.client.gfx.VectorContainer;
import org.geomajas.puregwt.client.map.MapPresenterImpl.MapWidget;
import org.vaadin.gwtgraphics.client.Transformable;

import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.GestureChangeHandler;
import com.google.gwt.event.dom.client.GestureEndHandler;
import com.google.gwt.event.dom.client.GestureStartHandler;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.dom.client.TouchCancelHandler;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.event.dom.client.TouchMoveHandler;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HasWidgets.ForIsWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

/**
 * Test implementation of a MapWidget. The GIN/Guice module in test is configured to use this implementation instead of
 * the real MapWidgetImpl.
 * 
 * @author Pieter De Graef
 */
public class MapWidgetTestImpl implements MapWidget {

	public class GadgetContainer implements ForIsWidget {

		private List<IsWidget> children = new ArrayList<IsWidget>();

		public void add(Widget w) {
		}

		public void clear() {
		}

		public Iterator<Widget> iterator() {
			return null;
		}

		public boolean remove(Widget w) {
			return false;
		}

		public void add(IsWidget w) {
			children.add(w);
		}

		public boolean remove(IsWidget w) {
			return children.remove(w);
		}

	}

	List<VectorContainer> worldContainers = new ArrayList<VectorContainer>();

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
		return null;
	}

	public HtmlContainer getMapHtmlContainer() {
		return null;
	}

	public VectorContainer getMapVectorContainer() {
		return null;
	}

	public List<VectorContainer> getWorldVectorContainers() {
		return worldContainers;
	}

	public VectorContainer getNewScreenContainer() {
		return null;
	}

	public VectorContainer getNewWorldContainer() {
		MockVectorContainer c = new MockVectorContainer();
		worldContainers.add(c);
		return c;
	}

	public boolean removeVectorContainer(VectorContainer container) {
		return false;
	}

	public boolean bringToFront(VectorContainer container) {
		return false;
	}

	public void onResize() {
	}

	public int getHeight() {
		return 500;
	}

	public int getWidth() {
		return 500;
	}

	public void setPixelSize(int width, int height) {

	}

	public void scheduleScale(double xx, double yy, int animationMillis) {

	}

	@Override
	public List<Transformable> getWorldTransformables() {
		return null;
	}

	@Override
	public CanvasContainer getNewWorldCanvas() {
		return null;
	}

	@Override
	public void scheduleTransform(double xx, double yy, double dx, double dy, int animationMillis) {
	}

	@Override
	public AbsolutePanel getWidgetContainer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HandlerRegistration addTouchStartHandler(TouchStartHandler handler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HandlerRegistration addTouchEndHandler(TouchEndHandler handler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HandlerRegistration addTouchCancelHandler(TouchCancelHandler handler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HandlerRegistration addTouchMoveHandler(TouchMoveHandler handler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HandlerRegistration addGestureStartHandler(GestureStartHandler handler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HandlerRegistration addGestureChangeHandler(GestureChangeHandler handler) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HandlerRegistration addGestureEndHandler(GestureEndHandler handler) {
		// TODO Auto-generated method stub
		return null;
	}
}