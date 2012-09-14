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
package org.geomajas.plugin.deskmanager.client.gwt.geodesk.widget.featureinfo;

import org.geomajas.plugin.deskmanager.client.gwt.geodesk.i18n.GeodeskMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickHandler;

/**
 * Widget class: button widget (i18n) to zoom to a feature on the map. To be useful, the user must specify a
 * clickHandler.
 * 
 * Note that button is implemented via an {@link IButton} member.
 * 
 * @author BuyleA
 * 
 */
public class ZoomButton {

	private static final GeodeskMessages MESSAGES = GWT.create(GeodeskMessages.class);

	private static final String BTN_ZOOM_SELECTION_IMG = "[ISOMORPHIC]/geomajas/osgeo/zoom-selection.png";

	private IButton button;

	private HandlerRegistration handlerRegistration;

	/**
	 * Constructor.
	 * 
	 * @param clickHandler ClickHandler for button
	 */
	public ZoomButton(ClickHandler clickHandler) {
		button = new IButton();
		button.setIcon(BTN_ZOOM_SELECTION_IMG);
		button.setShowDisabledIcon(false);

		button.setTitle(MESSAGES.zoomFeatureButtonTitle());
		button.setTooltip(MESSAGES.zoomFeatureButtonTooltip());
		button.setOverflow(Overflow.VISIBLE);
		button.setAutoWidth();

		setClickHandler(clickHandler);

	}

	/**
	 * Default Constructor. To be useful, the user must later specify the clickHandler via {@link setClickHandler}.
	 */
	public ZoomButton() {
		new ZoomButton((ClickHandler) null);
	}

	/**
	 * Specify the clickHandler.
	 * 
	 * @param clickHandler clickHandler ClickHandler for button
	 */
	public void setClickHandler(ClickHandler clickHandler) {

		if (null != handlerRegistration) {
			handlerRegistration.removeHandler();
		}
		if (null != clickHandler) {
			handlerRegistration = button.addClickHandler(clickHandler);
		}
	}

	/**
	 * Return the canvas for the button used to realize the widget.
	 * 
	 * @return Canvas for the button used to realize the widget
	 */
	public Canvas getCanvas() {
		return button;
	}

}
