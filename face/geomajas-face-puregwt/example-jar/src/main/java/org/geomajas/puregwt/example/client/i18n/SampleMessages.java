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

package org.geomajas.puregwt.example.client.i18n;

import com.google.gwt.i18n.client.Messages;

/**
 * Specific messages for the many samples.
 * 
 * @author Pieter De Graef
 */
public interface SampleMessages extends Messages {

	// ------------------------------------------------------------------------
	// Category: general samples
	// ------------------------------------------------------------------------

	String generalNavOptionTitle();

	String generalNavOptionShort();

	String generalNavOptionDescription();

	String generalResizeMapTitle();

	String generalResizeMapShort();

	String generalResizeMapDescription();

	String generalMapFillTitle();

	String generalMapFillShort();

	String generalMapFillDescription();

	String generalVpEventTitle();

	String generalVpEventShort();

	String generalVpEventDescription();
	
	String generalServerExceptionTitle();
	
	String generalServerExceptionShort();
	
	String generalServerExceptionDescription();

	String generalListnerTitle();

	String generalListnerShort();

	String generalListnerDescription();

	String listenerCoordinates();

	String listenerScreenPosition();

	String listenerWorldPosition();

	String listenerEvent();

	// ------------------------------------------------------------------------
	// Category: Layers
	// ------------------------------------------------------------------------

	String layerAddRemoveTitle();

	String layerAddRemoveShort();

	String layerAddRemoveDescription();

	String layerOrderTitle();

	String layerOrderShort();

	String layerOrderDescription();

	String layerVisibilityTitle();

	String layerVisibilityShort();

	String layerVisibilityDescription();

	String layerRefreshTitle();

	String layerRefreshShort();

	String layerRefreshDescription();

	String layerOpacityTitle();

	String layerOpacityShort();

	String layerOpacityDescription();

	// ------------------------------------------------------------------------
	// Category: Features
	// ------------------------------------------------------------------------

	String featureSelectionTitle();

	String featureSelectionShort();

	String featureSelectionDescription();

	// ------------------------------------------------------------------------
	// Category: Rendering
	// ------------------------------------------------------------------------

	String renderingInteractionTitle();

	String renderingInteractionShort();

	String renderingInteractionDescription();

	String renderingScreenSpaceTitle();

	String renderingScreenSpaceShort();

	String renderingScreenSpaceDescription();

	String renderingWorldSpaceTitle();

	String renderingWorldSpaceShort();

	String renderingWorldSpaceDescription();

	String renderingWorldSpaceFixedTitle();

	String renderingWorldSpaceFixedShort();

	String renderingWorldSpaceFixedDescription();
	
	String renderingMissingCanvas();

	String renderingCanvasTitle();

	String renderingCanvasShort();

	String renderingCanvasDescription();
}