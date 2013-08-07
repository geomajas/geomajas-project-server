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

package org.geomajas.plugin.geocoder.gwt.example.client;

import com.google.gwt.core.client.GWT;
import org.geomajas.smartgwt.example.base.SampleTreeNode;
import org.geomajas.smartgwt.example.base.SampleTreeNodeRegistry;

import com.google.gwt.core.client.EntryPoint;
import org.geomajas.plugin.geocoder.gwt.example.client.i18n.GeocoderMessages;

/**
 * Entry point and main class for GWT application. This class defines the layout and functionality of this application.
 *
 * @author Pieter De Graef
 */
public class GeocoderExample implements EntryPoint {

	public static final GeocoderMessages MESSAGES = GWT.create(GeocoderMessages.class);

	public void onModuleLoad() {

		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.treeGroupPlugins(),
				"[ISOMORPHIC]/geomajas/silk/plugin.png", "Plugins", "topLevel"));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.geocoderTitle(),
				"[ISOMORPHIC]/geomajas/osgeo/layer-raster.png", GeocoderPanel.TITLE, "Plugins",
				GeocoderPanel.FACTORY));
	}
}
