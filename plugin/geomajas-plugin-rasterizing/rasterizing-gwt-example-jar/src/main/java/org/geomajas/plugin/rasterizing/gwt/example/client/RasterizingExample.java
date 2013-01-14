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
package org.geomajas.plugin.rasterizing.gwt.example.client;

import org.geomajas.gwt.example.base.SampleTreeNode;
import org.geomajas.gwt.example.base.SampleTreeNodeRegistry;
import org.geomajas.plugin.rasterizing.gwt.example.client.i18n.RasterizingMessages;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

/**
 * Entry point of rasterizing example.
 * 
 * @author Jan De Moerloose
 *
 */
public class RasterizingExample implements EntryPoint {

	public static final RasterizingMessages MESSAGES = GWT.create(RasterizingMessages.class);

	public void onModuleLoad() {

		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.rasterizingTitle(),
				"[ISOMORPHIC]/geomajas/osgeo/layer-raster.png", RasterizingPanel.TITLE, "Plugins",
				RasterizingPanel.FACTORY));
	}
}
