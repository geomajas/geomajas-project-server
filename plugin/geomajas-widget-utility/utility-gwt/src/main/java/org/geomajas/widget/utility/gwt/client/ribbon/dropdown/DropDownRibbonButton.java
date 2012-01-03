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
		dropDownPanel.hide();
		action.setDropDownPanel(dropDownPanel);
	}
	
	public DropDownRibbonButton(final DropDownButtonAction action, int iconSize, TitleAlignment titleAlignment) {
		this(action);
		setIconSize(iconSize);
		setTitleAlignment(titleAlignment);
	}
	
	@Override
	public void configure(String key, String value) {
		if ("title".equals(key)) {
			setTitle(value);
		} else if ("icon".equals(key)) {
			setIcon(value);
		} else if ("toolTip".equals(key)) {
			setTooltip(value);
		} else if ("panelWidth".equals(key)) {
			dropDownPanel.setWidth(Integer.parseInt(value));
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

	@Override
	public Widget asWidget() {
		return this;
	}

	@Override
	public void setShowTitles(boolean showTitles) {
		super.setShowTitles(showTitles);
	}

	@Override
	public boolean isShowTitles() {
		return super.isShowTitles();
	}

	@Override
	public boolean isEnabled() {
		return !isDisabled();
	}

	@Override
	public void setEnabled(boolean enabled) {
		setDisabled(!enabled);
	}

	public DropDownPanel getPanel() {
		return dropDownPanel;
	}
}