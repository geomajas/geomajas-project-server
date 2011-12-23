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
package org.geomajas.puregwt.client.map.gfx;

import org.geomajas.annotation.Api;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * <p>
 * Basic HTML element definition used in map rendering. Note that all HtmlObjects are actually GWT widgets.This class
 * will create an HTML element in the constructor and set as many style info as is provided. Also all HtmlObjects will
 * have an absolute positioning.
 * </p>
 * <p>
 * All getters and setters work immediately on the underlying HTML element for performance reasons. Extensions of this
 * class can represent individual HTML tags such as images, but also groups (DIV). In this case of a group (see
 * HtmlContainer) it is possible to attach multiple other HtmlObjects to create a tree structure.
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api
public interface HtmlObject {

	Element getElement();

	Widget getParent();

	void setParent(Widget parent);

	int getWidth();

	void setWidth(int width);

	int getHeight();

	void setHeight(int height);

	int getLeft();

	void setLeft(int left);

	int getTop();

	void setTop(int top);

	double getOpacity();

	void setOpacity(double opacity);

	void setVisible(boolean visible);

	boolean isVisible();
}