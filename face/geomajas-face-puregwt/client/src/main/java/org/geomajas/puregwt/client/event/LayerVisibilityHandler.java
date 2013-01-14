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
package org.geomajas.puregwt.client.event;

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;

import com.google.gwt.event.shared.EventHandler;
import com.google.web.bindery.event.shared.Event.Type;

/**
 * Interface for handling layer visibility events.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
@UserImplemented
public interface LayerVisibilityHandler extends EventHandler {

	/**
	 * The type of the handler.
	 */
	Type<LayerVisibilityHandler> TYPE = new Type<LayerVisibilityHandler>();

	/**
	 * <p>
	 * Called when a layer becomes visible on the map. It doesn't matter what caused the layer to become visible (marked
	 * as visible, zoom came into scale range, ...). This method is executed at the moment a layer has become visible.
	 * </p>
	 * <p>
	 * If this method is called, then the method <code>layer.isShowing()</code> must return <code>TRUE</code>.
	 * </p>
	 * 
	 * @param event
	 *            The event that indicates which layer specifically became visible.
	 */
	void onShow(LayerShowEvent event);

	/**
	 * <p>
	 * Called when a layer becomes invisible on the map. It doesn't matter what caused the layer to become invisible
	 * (marked as invisible, zoom out of the scale range, ...). This method is executed when a layer becomes invisible.
	 * </p>
	 * <p>
	 * If this method is called, then the method <code>layer.isShowing()</code> must return <code>FALSE</code>.
	 * </p>
	 * 
	 * @param event
	 *            The event that indicates which layer specifically became invisible.
	 */
	void onHide(LayerHideEvent event);

	/**
	 * <p>
	 * Called when a layer has been marked as visible/invisible. When a layer has been marked as invisible, expect a
	 * call to <code>onHide</code> very soon.
	 * </p>
	 * <p>
	 * But, when a layer has been marked as visible, that does not necessarily mean it will become visible. There are
	 * more requirements that have to be met in order for a layer to become visible: the map's scale must be between the
	 * minimum and maximum allowed scales for the layer. If that requirement has been met as well, expect a call to
	 * <code>onShow</code> shortly.
	 * </p>
	 * 
	 * @param event
	 *            The event that indicates which layer has been marked as visible or invisible (you can ask the layer
	 *            itself).
	 */
	void onVisibilityMarked(LayerVisibilityMarkedEvent event);
}