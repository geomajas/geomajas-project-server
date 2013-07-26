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

package org.geomajas.gwt.client.gfx;

import org.geomajas.annotation.Api;
import org.vaadin.gwtgraphics.client.Transformable;
import org.vaadin.gwtgraphics.client.Transparent;
import org.vaadin.gwtgraphics.client.VectorObjectContainer;

import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasDoubleClickHandlers;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * Base container definition for vector object types. This interface is used for rendering vector layers, screen space
 * objects or world space objects.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface VectorContainer extends VectorObjectContainer, HasClickHandlers, HasAllMouseHandlers,
		HasDoubleClickHandlers, Transparent, Transformable, IsWidget {

	/**
	 * Determine container visibility.
	 * 
	 * @param visible
	 *            Set whether or not this container and all it's children should be visible or not.
	 */
	void setVisible(boolean visible);

	/**
	 * Get whether or not this container and all it's children are visible.
	 * 
	 * @return Whether or not this container and all it's children are visible.
	 */
	boolean isVisible();
}