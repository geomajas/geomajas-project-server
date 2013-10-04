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

package org.geomajas.gwt2.client.gfx;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.vaadin.gwtgraphics.client.Transparent;

import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Default implementation of {@link TransformableWidgetContainer}.
 * 
 * @author Jan De Moerloose
 * 
 */
public class TransformableWidgetContainerImpl extends Panel implements TransformableWidgetContainer, Transparent {

	private List<TransformableWidget> children = new ArrayList<TransformableWidget>();

	private double deltaX;

	private double deltaY;

	private double scaleX = -1;

	private double scaleY = -1;

	private boolean setPanOrigin;

	private double panX;

	private double panY;

	public TransformableWidgetContainerImpl() {
		Element element = DOM.createElement("div");
		setElement(element);
		getElement().getStyle().setPosition(Position.ABSOLUTE);
	}

	@Override
	public Widget asWidget() {
		return this;
	}

	@Override
	public void setTranslation(double deltaX, double deltaY) {
		this.deltaX = deltaX;
		this.deltaY = deltaY;
		if (setPanOrigin) {
			this.panX = deltaX;
			this.panY = deltaY;
			setPanOrigin = false;
		}
		renderTransformed();
	}

	@Override
	public void setScale(double scaleX, double scaleY) {
		if (this.scaleX != scaleX || this.scaleY != scaleY) {
			this.scaleX = scaleX;
			this.scaleY = scaleY;
			setPanOrigin = true;
			renderTransformed();
		}
	}

	@Override
	public void setFixedSize(boolean fixedSize) {
	}

	@Override
	public boolean isFixedSize() {
		return false;
	}

	@Override
	public void add(TransformableWidget child) {
		children.add(child);
		getElement().appendChild(child.asWidget().getElement());
		adopt(child.asWidget());
		renderTransformed();
	}

	@Override
	public void insert(TransformableWidget child, int beforeIndex) {
		if (beforeIndex >= children.size()) {
			add(child);
			return;
		}
		Node beforeNode = getElement().getChild(beforeIndex);
		getElement().insertBefore(child.asWidget().getElement(), beforeNode);

		List<TransformableWidget> newChildList = new ArrayList<TransformableWidget>();
		for (int i = 0; i < children.size(); i++) {
			if (i == beforeIndex) {
				newChildList.add(child);
			}
			newChildList.add(children.get(i));
		}
		children = newChildList;
		adopt(child.asWidget());
		renderTransformed();
	}

	@Override
	public boolean remove(Widget child) {
		throw new UnsupportedOperationException("Use remove(TransformableWidget) instead");
	}

	@Override
	public boolean remove(TransformableWidget child) {
		int index = indexOf(child);
		if (index >= 0) {
			orphan(child.asWidget());
			getElement().removeChild(getElement().getChild(index));
			children.remove(index);
			return true;
		}
		return false;
	}

	@Override
	public Iterator<Widget> iterator() {
		List<Widget> widgets = new ArrayList<Widget>();
		for (IsWidget child : children) {
			widgets.add(child.asWidget());
		}
		return widgets.iterator();
	}

	@Override
	public int indexOf(TransformableWidget child) {
		for (int i = 0; i < children.size(); i++) {
			IsWidget img = children.get(i);
			if (img.equals(child)) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public void bringToFront(TransformableWidget child) {
		if (remove(child)) {
			add(child);
		}
	}

	@Override
	public int getChildCount() {
		return children.size();
	}

	@Override
	public TransformableWidget getChild(int index) {
		return children.get(index);
	}

	public void clear() {
		for (int i = getChildCount() - 1; i >= 0; i--) {
			remove(getChild(i));
		}
	}

	private void renderTransformed() {
		getElement().getStyle().setLeft(deltaX - panX, Unit.PX);
		getElement().getStyle().setTop(deltaY - panY, Unit.PX);
		for (int i = 0; i < getChildCount(); i++) {
			TransformableWidget w = (TransformableWidget) getChild(i);
			w.setScale(scaleX, scaleY);
			w.setTranslation(panX, panY);
		}

	}

	@Override
	public void setOpacity(double opacity) {
		getElement().getStyle().setOpacity(opacity);
	}

}
