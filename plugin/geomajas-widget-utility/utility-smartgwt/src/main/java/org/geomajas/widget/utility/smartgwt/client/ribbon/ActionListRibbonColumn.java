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

package org.geomajas.widget.utility.smartgwt.client.ribbon;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.widget.utility.client.action.ButtonAction;
import org.geomajas.widget.utility.client.ribbon.RibbonColumn;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.layout.VStack;

/**
 * RibbonColumn implementation that displays a vertical list of {@link RibbonButton}s. Actions such as title alignment
 * and toggling of titles will be applied on all buttons in the list.
 * 
 * @author Pieter De Graef
 */
public class ActionListRibbonColumn extends VStack implements RibbonColumn {

	private List<RibbonButton> buttons;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	/**
	 * Initialize this vertical button list by immediately providing the full list of {@link ButtonAction}s.
	 * 
	 * @param actionList
	 *            The list of {@link ButtonAction}s to translate into a vertical list of {@link RibbonButton}s.
	 */
	public ActionListRibbonColumn(List<ButtonAction> actionList) {
		this(actionList, 16);
	}

	/**
	 * Initialize this vertical button list by immediately providing the full list of {@link ButtonAction}s.
	 * 
	 * @param actionList
	 *            The list of {@link ButtonAction}s to translate into a vertical list of {@link RibbonButton}s.
	 * @param iconSize
	 *            The width and height of the icons used in the vertical list. Expressed in pixels.
	 */
	public ActionListRibbonColumn(List<ButtonAction> actionList, int iconSize) {
		buttons = new ArrayList<RibbonButton>(actionList.size());
		for (ButtonAction action : actionList) {
			RibbonButton button = new RibbonButton(action, iconSize, TitleAlignment.RIGHT);
			button.setHeight(20);
			addMember(button);
			buttons.add(button);
		}
	}

	// ------------------------------------------------------------------------
	// Class specific methods:
	// ------------------------------------------------------------------------

	public void setButtonBaseStyle(String buttonBaseStyle) {
		for (RibbonButton button : buttons) {
			button.setBaseStyle(buttonBaseStyle);
		}
	}

	// ------------------------------------------------------------------------
	// RibbonColumn implementation:
	// ------------------------------------------------------------------------

	/**
	 * Returns the vertical layout that holds all buttons.
	 * 
	 * @return The vertical layout that holds all buttons.
	 */
	public Widget asWidget() {
		return this;
	}

	/**
	 * Determine whether or not to display all titles on all buttons.
	 * 
	 * @param showTitles
	 *            The new value. Applying this new value will immediately trigger the GUI to redraw.
	 */
	public void setShowTitles(boolean showTitles) {
		for (RibbonButton button : buttons) {
			button.setShowTitles(showTitles);
		}
	}

	/**
	 * See whether or not the titles on the buttons are currently visible.
	 * 
	 * @return Return whether or not the titles on the buttons are currently visible.
	 */
	public boolean isShowTitles() {
		if (buttons.size() > 0) {
			return buttons.get(0).isShowTitles();
		}
		return false;
	}

	/**
	 * Determine the alignment (BOTTOM, RIGHT) for the titles on all buttons.
	 * 
	 * @param titleAlignment
	 *            The new value. Applying this new value will immediately trigger the GUI to redraw.
	 */
	public void setTitleAlignment(TitleAlignment titleAlignment) {
		for (RibbonButton button : buttons) {
			button.setTitleAlignment(titleAlignment);
		}
	}

	/**
	 * Get the current value for the title alignment (BOTTOM, RIGHT).
	 * 
	 * @return The current value for the title alignment (BOTTOM, RIGHT).
	 */
	public TitleAlignment getTitleAlignment() {
		if (buttons.size() > 0) {
			return buttons.get(0).getTitleAlignment();
		}
		return TitleAlignment.BOTTOM;
	}

	/**
	 * Is the ribbonColumn enabled?
	 * 
	 * @return true if column is enabled
	 */
	public boolean isEnabled() {
		return !isDisabled();
	}

	/**
	 * Set the enabled state of the RibbonColumn.
	 * 
	 * @param enabled
	 *            The enabled state
	 */
	public void setEnabled(boolean enabled) {
		setDisabled(!enabled);
	}

	/**
	 * Add configuration key/value pair. This pair will be applied on all actions within this list.
	 * 
	 * @param key
	 *            parameter key
	 * @param value
	 *            parameter value
	 */
	public void configure(String key, String value) {
		for (RibbonButton button : buttons) {
			button.configure(key, value);
		}
	}
}