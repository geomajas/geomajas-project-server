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

package org.geomajas.widget.utility.gwt.client.ribbon;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.widget.utility.common.client.action.ButtonAction;
import org.geomajas.widget.utility.common.client.ribbon.RibbonColumn;

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

	/** {@inheritDoc} */
	public void setButtonBaseStyle(String buttonBaseStyle) {
		for (RibbonButton button : buttons) {
			button.setBaseStyle(buttonBaseStyle);
		}
	}

	// ------------------------------------------------------------------------
	// RibbonColumn implementation:
	// ------------------------------------------------------------------------

	/** {@inheritDoc} */
	public Widget asWidget() {
		return this;
	}

	/** {@inheritDoc} */
	public void setShowTitles(boolean showTitles) {
		for (RibbonButton button : buttons) {
			button.setShowTitles(showTitles);
		}
	}

	/** {@inheritDoc} */
	public boolean isShowTitles() {
		return buttons.size() > 0 && buttons.get(0).isShowTitles();
	}

	/** {@inheritDoc} */
	public void setTitleAlignment(TitleAlignment titleAlignment) {
		for (RibbonButton button : buttons) {
			button.setTitleAlignment(titleAlignment);
		}
	}

	/** {@inheritDoc} */
	public TitleAlignment getTitleAlignment() {
		if (buttons.size() > 0) {
			return buttons.get(0).getTitleAlignment();
		}
		return TitleAlignment.BOTTOM;
	}

	/** {@inheritDoc} */
	public boolean isEnabled() {
		return !isDisabled();
	}

	/** {@inheritDoc} */
	public void setEnabled(boolean enabled) {
		setDisabled(!enabled);
	}

	/** {@inheritDoc} */
	public void configure(String key, String value) {
		for (RibbonButton button : buttons) {
			button.configure(key, value);
		}
	}
}