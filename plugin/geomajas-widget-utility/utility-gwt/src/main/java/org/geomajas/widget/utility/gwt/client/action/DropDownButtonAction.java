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

package org.geomajas.widget.utility.gwt.client.action;

import org.geomajas.gwt.client.action.ToolbarAction;
import org.geomajas.widget.utility.gwt.client.ribbon.dropdown.DropDownPanel;

import com.smartgwt.client.types.AnimationEffect;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.layout.VStack;

/**
 * Opens a drop-down panel beneath a button.
 * 
 * @author Emiel Ackermann
 */
public class DropDownButtonAction extends ToolbarAction {

	private DropDownPanel dropDownPanel;
	
	/**
	 * Title, icon and tool tip of the {@link DropDownRibbonButton} 
	 * are set through {@link Parameter}s of the {@link RibbonColumnInfo}.
	 */
	public DropDownButtonAction() {
		super("", "");
	}

	/**
	 * Set panel which should be displayed in the drop down.
	 *
	 * @param dropDownPanel drop down panel
	 */
	public void setDropDownPanel(DropDownPanel dropDownPanel) {
		this.dropDownPanel = dropDownPanel;
	}

	/**
	 * Get the panel which should be displayed in the drop down.
	 *
	 * @return drop down panel
	 */
	public VStack getDropDownPanel() {
		return dropDownPanel;
	}

	public void onClick(ClickEvent event) {
		if (dropDownPanel.isVisible()) {
			dropDownPanel.hide();
		} else {
			dropDownPanel.animateShow(AnimationEffect.SLIDE);
		}
	}
}
