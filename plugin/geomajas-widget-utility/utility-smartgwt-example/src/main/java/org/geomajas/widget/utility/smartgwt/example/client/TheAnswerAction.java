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

package org.geomajas.widget.utility.smartgwt.example.client;

import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.widget.utility.client.action.AbstractButtonAction;

import com.google.gwt.event.dom.client.ClickEvent;
import com.smartgwt.client.util.SC;

/**
 * Show the use of an AbstractButtonAction, as well as configuration parameter (enabled).
 * 
 * @author Kristof Heirwegh
 */
public class TheAnswerAction extends AbstractButtonAction {

	public static final String IDENTIFIER = "TheAnswerAction";
	
	public TheAnswerAction() {
		super(WidgetLayout.iconHelpContents, "The Answer",
				"Answer to the Ultimate Question of Life, the Universe, and Everything");
	}

	public void onClick(ClickEvent event) {
		SC.say("42");
	}
}
