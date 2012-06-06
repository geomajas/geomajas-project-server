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

package org.geomajas.widget.featureinfo.gwt.example.client;

import com.google.gwt.core.client.GWT;
import org.geomajas.gwt.example.base.SampleTreeNode;
import org.geomajas.gwt.example.base.SampleTreeNodeRegistry;

import com.google.gwt.core.client.EntryPoint;
import org.geomajas.widget.featureinfo.gwt.example.client.i18n.ApplicationMessages;

/**
 * Entry point and main class for GWT application. This class defines the layout and functionality of this application.
 *
 * @author Wout Swartenbroekx
 */
public class FeatureinfoExample implements EntryPoint {

	private static final ApplicationMessages MESSAGES = GWT.create(ApplicationMessages.class);

	public void onModuleLoad() {

		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode("Plug-ins",
				"[ISOMORPHIC]/geomajas/silk/plugin.png", "Plugins", "topLevel"));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.applicationTitle("Feature info"),
				"[ISOMORPHIC]/geomajas/osgeo/layer-raster.png", FeatureinfoPanel.TITLE, "Plugins",
				FeatureinfoPanel.FACTORY));
	}
}
