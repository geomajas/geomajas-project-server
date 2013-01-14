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

package org.geomajas.widget.utility.gwt.example.client;

import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.widget.utility.gwt.client.action.AbstractButtonAction;
import org.geomajas.widget.utility.gwt.client.ribbon.RibbonButton;

import com.smartgwt.client.widgets.events.ClickEvent;

/**
 * Change the enabled/disabled state of a button.
 * 
 * @author Kristof Heirwegh
 */
public class ChangeStateAction extends AbstractButtonAction {

	public static final String IDENTIFIER = "ChangeStateAction";

	private RibbonButton button;

	public ChangeStateAction(RibbonButton rb) {
		super(WidgetLayout.iconRedraw, "Change State", "Change state of button");
		this.button = rb;
	}

	public void onClick(ClickEvent event) {
		button.setEnabled(!button.isEnabled());
	}
}
