package org.geomajas.plugin.rasterizing.gwt.example.client;

import org.geomajas.gwt.example.base.SampleTreeNode;
import org.geomajas.gwt.example.base.SampleTreeNodeRegistry;
import org.geomajas.plugin.rasterizing.gwt.example.client.i18n.RasterizingMessages;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;


public class RasterizingExample implements EntryPoint {

	public static final RasterizingMessages MESSAGES = GWT.create(RasterizingMessages.class);

	public void onModuleLoad() {

//		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.treeGroupPlugins(),
//				"[ISOMORPHIC]/geomajas/silk/plugin.png", "Plugins", "topLevel"));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.rasterizingTitle(),
				"[ISOMORPHIC]/geomajas/osgeo/layer-raster.png", RasterizingPanel.TITLE, "Plugins",
				RasterizingPanel.FACTORY));
	}
}
