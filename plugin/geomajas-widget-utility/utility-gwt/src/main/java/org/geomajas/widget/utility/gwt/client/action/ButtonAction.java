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

package org.geomajas.widget.utility.gwt.client.action;

import org.geomajas.gwt.client.action.toolbar.parameter.ButtonLayoutStyle;

import com.smartgwt.client.widgets.events.ClickHandler;


/**
 * <p>
 * Definition of an 'action' that can be attached to a button. Such actions are used for example within
 * <code>Ribbon</code> widgets. It implements the {@link ClickHandler} interface, which will trigger the actual action
 * execution and it provides an icon, title and tooltip for buttons.
 * </p>
 * <p>
 * By itself this class is nothing, it always needs to be used within some sort of button.
 * </p>
 * 
 * @author Pieter De Graef
 */
public interface ButtonAction extends ClickHandler {

	/**
	 * Add configuration key/value pair.
	 * 
	 * @param key
	 *            parameter key
	 * @param value
	 *            parameter value
	 */
	void configure(String key, String value);

	/**
	 * Get the icon to be used on the button.
	 * 
	 * @return The icon to be used on the button.
	 */
	String getIcon();

	/**
	 * Determine which icon should be used on the button.
	 * 
	 * @param icon
	 *            The new icon reference.
	 */
	void setIcon(String icon);

	/**
	 * Get the title to be displayed on the button.
	 * 
	 * @return The title to be displayed on the button.
	 */
	String getTitle();

	/**
	 * Set the title to be displayed on the button.
	 * 
	 * @param title
	 *            The new title to be displayed on the button.
	 */
	void setTitle(String title);

	/**
	 * Get the tooltip one sees when hovering over the button.
	 * 
	 * @return The tooltip one sees when hovering over the button.
	 */
	String getTooltip();

	/**
	 * Set the tooltip one sees when hovering over the button.
	 * 
	 * @param tooltip
	 *            The new tooltip.
	 */
	void setTooltip(String tooltip);
	
	/**
	 * Get the button {@link ButtonLayoutStyle} which determines the gui in a RibbonColumn.
	 * 
	 * @return The button {@link ButtonLayoutStyle} which determines the gui.
	 */
	ButtonLayoutStyle getButtonLayoutStyle();

	/**
	 * Set the button {@link ButtonLayoutStyle} which determines the gui in a RibbonColumn.
	 * 
	 * @param buttonLayoutStyle
	 *           The button {@link ButtonLayoutStyle} which determines the gui.
	 */
	void setButtonLayoutStyle(ButtonLayoutStyle buttonLayoutStyle);
}
