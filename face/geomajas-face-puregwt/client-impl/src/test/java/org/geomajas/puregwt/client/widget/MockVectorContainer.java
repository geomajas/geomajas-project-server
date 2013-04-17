package org.geomajas.puregwt.client.widget;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.puregwt.client.gfx.VectorContainer;
import org.vaadin.gwtgraphics.client.VectorObject;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;

public class MockVectorContainer implements VectorContainer {

	private List<VectorObject> children = new ArrayList<VectorObject>();

	private boolean visible;

	public VectorObject add(VectorObject vo) {
		children.add(vo);
		return vo;
	}

	public VectorObject insert(VectorObject vo, int beforeIndex) {
		if (beforeIndex < 0 || beforeIndex > getVectorObjectCount()) {
			throw new IndexOutOfBoundsException();
		}

		if (children.contains(vo)) {
			children.remove(vo);
		}

		children.add(beforeIndex, vo);
		return vo;
	}

	public VectorObject remove(VectorObject vo) {
		children.remove(vo);
		return vo;
	}

	public VectorObject bringToFront(VectorObject vo) {
		return vo;
	}

	public void clear() {
		children.clear();
	}

	public int getVectorObjectCount() {
		return children.size();
	}

	public VectorObject getVectorObject(int index) {
		return children.get(index);
	}

	public void setTranslation(double deltaX, double deltaY) {
	}

	public void setScale(double scaleX, double scaleY) {
	}

	public Widget asWidget() {
		return null;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setOpacity(double opacity) {
	}

	public void setFixedSize(boolean fixedSize) {
	}

	public boolean isFixedSize() {
		return false;
	}

	@Override
	public VectorObject moveToBack(VectorObject vo) {
		return vo;
	}

	@Override
	public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
		return null;
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return null;
	}

	@Override
	public HandlerRegistration addDoubleClickHandler(DoubleClickHandler handler) {
		return null;
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
	}

	@Override
	public HandlerRegistration addMouseWheelHandler(MouseWheelHandler handler) {
		return null;
	}

	@Override
	public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
		return null;
	}

	@Override
	public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
		return null;
	}

	@Override
	public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {

		return null;
	}

	@Override
	public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {

		return null;
	}
	
	
}