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
package org.geomajas.widget.utility.gwt.client.action;

import com.smartgwt.client.widgets.events.ClickEvent;


/**
 * A basic implementation of a RadioAction.
 * 
 * @author Kristof Heirwegh
 * 
 */
public abstract class AbstractRadioAction extends AbstractButtonAction implements RadioAction {

	private String radioGroup = "default-radio-group";

	public AbstractRadioAction() {
		super();
	}

	public AbstractRadioAction(String icon, String title, String tooltip) {
		super(icon, title, tooltip);
	}

	public AbstractRadioAction(String icon, String title, String tooltip, String radioGroup) {
		super(icon, title, tooltip);
		this.radioGroup = radioGroup;
	}

	public String getRadioGroup() {
		return radioGroup;
	}

	public void setRadioGroup(String radioGroup) {
		this.radioGroup = radioGroup;
	}

	public void onClick(ClickEvent event) {
		// this won't be called so no point in overriding
	}
}
