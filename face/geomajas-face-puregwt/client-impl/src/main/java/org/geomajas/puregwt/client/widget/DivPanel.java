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

import com.google.gwt.dom.client.Node;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Jan De Moerloose
 *
 */
public class DivPanel extends Panel {

	private List<IsWidget> children = new ArrayList<IsWidget>();

	public DivPanel() {
		Element element = DOM.createElement("div");
		setElement(element);
	}

	public int getChildCount() {
		return children.size();
	}

	public IsWidget getChild(int index) {
		if (index < children.size()) {
			return children.get(index);
		}
		return null;
	}

	public void insertBefore(IsWidget child, int beforeIndex) {
		if (beforeIndex >= children.size()) {
			add(child);
			return;
		}
		Node beforeNode = getElement().getChild(beforeIndex);
		getElement().insertBefore(child.asWidget().getElement(), beforeNode);

		List<IsWidget> newChildList = new ArrayList<IsWidget>();
		for (int i = 0; i < children.size(); i++) {
			if (i == beforeIndex) {
				newChildList.add(child);
			}
			newChildList.add(children.get(i));
		}
		children = newChildList;
		adopt(child.asWidget());
	}

	@Override
	public Iterator<Widget> iterator() {
		List<Widget> widgets = new ArrayList<Widget>();
		for (IsWidget child : children) {
			widgets.add(child.asWidget());
		}
		return widgets.iterator();
	}

	public void add(IsWidget child) {
		children.add(child);
		getElement().appendChild(child.asWidget().getElement());
		adopt(child.asWidget());
	}

	public void add(Widget child) {
		getElement().appendChild(child.getElement());
		children.add(child);
		adopt(child);
	}

	@Override
	public boolean remove(IsWidget child) {
		int index = getIndex(child);
		if (index >= 0) {
			orphan(child.asWidget());
			getElement().removeChild(getElement().getChild(index));
			children.remove(index);
			return true;
		}
		return false;
	}

	@Override
	public boolean remove(Widget child) {
		throw new UnsupportedOperationException("Use remove(IsWidget) instead");
	}

	private int getIndex(IsWidget child) {
		for (int i = 0; i < children.size(); i++) {
			IsWidget img = children.get(i);
			if (img.equals(child)) {
				return i;
			}
		}
		return -1;
	}

}
