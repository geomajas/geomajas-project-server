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

package org.geomajas.puregwt.client.general;

import org.geomajas.puregwt.client.ContentPanel;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

/**
 * Introduction page.
 * 
 * @author Pieter De Graef
 */
public class IntroductionContentPanel extends ContentPanel {

	public IntroductionContentPanel() {
		super(null);
	}

	public String getTitle() {
		return "Geomajas PureGWT face";
	}

	public String getDescription() {
		return "This is the introduction page for the Geomajas PureGWT face. Feel free to explore all " +
				"features this demo application provides.";
	}

	public Widget getContentWidget() {
		return new HTML("Please give Geomajas a try and let the world know how wonderful it is.");
	}
}