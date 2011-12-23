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

package org.geomajas.widget.utility.gwt.client.ribbon.dropdown;

import org.geomajas.widget.utility.gwt.client.action.DropDownButtonAction;
import org.geomajas.widget.utility.gwt.client.action.ToolbarButtonAction;
import org.geomajas.widget.utility.gwt.client.ribbon.RibbonButton;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.Positioning;

/**
 * RibbonColumn implementation that displays a button, which opens a drop-down panel with more buttons.
 * 
 * @author Emiel Ackermann
 */
public class DropDownRibbonButton extends RibbonButton {
	
	private DropDownPanel dropDownPanel;
		
	public DropDownRibbonButton(DropDownButtonAction action) {
		super(new ToolbarButtonAction(action));
		dropDownPanel = new DropDownPanel(this);
		dropDownPanel.setOverflow(Overflow.VISIBLE);
		dropDownPanel.setAutoHeight();
		dropDownPanel.setAutoWidth();
		dropDownPanel.setPosition(Positioning.RELATIVE);
		dropDownPanel.hide();
		action.setDropDownPanel(dropDownPanel);
	}
	
	public DropDownRibbonButton(final DropDownButtonAction action, int iconSize, TitleAlignment titleAlignment) {
		this(action);
		setIconSize(iconSize);
		setTitleAlignment(titleAlignment);
	}
	
	/**
<<<<<<< .mine
	 * Add configuration key/value pair. The "style" value will trigger 
	 * the conversion of the actions into buttons on the {@link DropDownPanel}.
=======
	 * Add configuration key/value pair. The "style" value will trigger the conversion of the actions into buttons on
	 * the {@link DropDownPanel}.
>>>>>>> .r9525
	 * 
	 * @param key parameter key
	 * @param value parameter value
	 */
	public void configure(String key, String value) {
		if ("title".equals(key)) {
			setTitle(value);
		} else if ("icon".equals(key)) {
			setIcon(value);
		} else if ("toolTip".equals(key)) {
			setTooltip(value);
		} 
	}

	// ------------------------------------------------------------------------
	// Class specific methods:
	// ------------------------------------------------------------------------
	public void setButtonBaseStyle(String baseStyle) {
		this.setBaseStyle(baseStyle);
		dropDownPanel.setStyleName(this.getStyleName().replace("Button", "DropDownPanel"));
	}

	// ------------------------------------------------------------------------
	// RibbonColumn implementation:
	// ------------------------------------------------------------------------

	/**
	 * Returns the vertical layout that holds the drop-down button.
	 * 
	 * @return The vertical layout that holds the drop-down button.
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
		super.setShowTitles(showTitles);
	}

	/**
	 * See whether or not the titles on the buttons are currently visible.
	 * 
	 * @return Return whether or not the titles on the buttons are currently visible.
	 */
	public boolean isShowTitles() {
		return super.isShowTitles();
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

	public DropDownPanel getPanel() {
		return dropDownPanel;
	}
}