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

import org.geomajas.widget.utility.common.client.action.AbstractButtonAction;

import com.google.gwt.event.dom.client.ClickEvent;
import com.smartgwt.client.types.AnimationEffect;
import com.smartgwt.client.widgets.layout.VStack;

/**
 * Opens a drop-down panel beneath a button.
 * 
 * @author Emiel Ackermann
 */
public class DropDownButtonAction extends AbstractButtonAction {

	private VStack dropDownPanel;

	/**
	 * 
	 * @param icon
	 * @param title
	 * @param tooltip
	 */
	public DropDownButtonAction(String icon, String title, String tooltip) {
		super(icon, title, tooltip);
	}
	
	public void onClick(ClickEvent event) {
		dropDownPanel.animateShow(AnimationEffect.SLIDE);
	}

	public void setDropDownPanel(VStack dropDownPanel) {
		this.dropDownPanel = dropDownPanel;
	}

	public VStack getDropDownPanel() {
		return dropDownPanel;
	}
}
