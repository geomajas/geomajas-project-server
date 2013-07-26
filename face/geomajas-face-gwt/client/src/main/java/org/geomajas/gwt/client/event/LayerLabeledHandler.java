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
package org.geomajas.gwt.client.event;

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
public interface LayerLabeledHandler extends EventHandler {

	/**
	 * The type of the handler.
	 */
	Type<LayerLabeledHandler> TYPE = new Type<LayerLabeledHandler>();

	/**
	 * Called when labels are shown on the layer. This event gives you no doubt, if this method is called,you can see
	 * the labels with your own eyes.
	 * 
	 * @param event
	 *            The event that signals the labels are currently visible.
	 */
	void onLabelShow(LayerLabelShowEvent event);

	/**
	 * Called when labels for a layer have become invisible. This can be due to the fact that is as marked as invisible,
	 * or due to the fact that the layer itself became invisible for some reason.
	 * 
	 * @param event
	 *            The event that signals the labels are currently invisible.
	 */
	void onLabelHide(LayerLabelHideEvent event);

	/**
	 * <p>
	 * Called when the labels of a layer have been marked as visible or invisible. Note that when labels have been
	 * marked as invisible at a moment when they where actually visible, than you can expect a call to
	 * <code>onLabelHide</code> shortly.
	 * </p>
	 * <p>
	 * On the other hand marking labels as visible does not necessarily mean that they will become visible. For labels
	 * to becomes visible, they must be invisible and their layer must be visible. Only if those requirements are met
	 * will the labels truly become visible and can you expect a call to <code>onLabelShow</code> to follow this method.
	 * </p>
	 * 
	 * @param event
	 *            The event that signals the labels have been marked as visible or invisible. The event contain the
	 *            layer, so ask him which it is.
	 */
	void onLabelMarked(LayerLabelMarkedEvent event);
}