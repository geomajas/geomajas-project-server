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

package org.geomajas.widget.utility.gwt.example.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import org.geomajas.configuration.client.ClientToolInfo;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.example.base.SampleTreeNode;
import org.geomajas.gwt.example.base.SampleTreeNodeRegistry;
import org.geomajas.widget.utility.common.client.ribbon.RibbonColumn;
import org.geomajas.widget.utility.gwt.client.ribbon.RibbonButton;
import org.geomajas.widget.utility.gwt.client.ribbon.RibbonColumnRegistry;
import org.geomajas.widget.utility.gwt.example.client.i18n.WidgetUtilityMessages;

import java.util.List;

/**
 * Entry point and main class for GWT application. This class defines the layout and functionality of this application.
 *
 * @author Joachim Van der Auwera
 */
public class WidgetUtilityExample implements EntryPoint {

	public static final WidgetUtilityMessages MESSAGES = GWT.create(WidgetUtilityMessages.class);

	public void onModuleLoad() {
		// Add my custom ribbon columns type to the registry:

		RibbonColumnRegistry.put("MyCustomColumn", new RibbonColumnRegistry.RibbonColumnCreator() {

			public RibbonColumn create(List<ClientToolInfo> tools, MapWidget mapWidget) {
				return new MyCustomRibbonColumn(mapWidget);
			}
		});
		final RibbonButton theAnswer = new RibbonButton(new TheAnswerAction());
		RibbonColumnRegistry.put(TheAnswerAction.IDENTIFIER, new RibbonColumnRegistry.RibbonColumnCreator() {
			public RibbonColumn create(List<ClientToolInfo> tools, MapWidget mapWidget) {
				return theAnswer;
			}
		});
		RibbonColumnRegistry.put(ChangeStateAction.IDENTIFIER, new RibbonColumnRegistry.RibbonColumnCreator() {
			public RibbonColumn create(List<ClientToolInfo> tools, MapWidget mapWidget) {
				return new RibbonButton(new ChangeStateAction(theAnswer));
			}
		});

		// add samples in the tree with examples

		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.treeGroupUtilityWidgets(),
				"[ISOMORPHIC]/geomajas/silk/plugin.png", "UtilityWidgets", "topLevel"));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.ribbonTitle(),
				"[ISOMORPHIC]/geomajas/osgeo/layer-raster.png", RibbonPanel.TITLE, "UtilityWidgets",
				RibbonPanel.FACTORY));
	}
}

