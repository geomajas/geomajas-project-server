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

package org.geomajas.plugin.editing.gwt.example.client.merge;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.Canvas;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.example.base.SamplePanel;
import org.geomajas.gwt.example.base.SamplePanelFactory;
import org.geomajas.plugin.editing.client.merge.GeometryMergeService;

import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import org.geomajas.plugin.editing.gwt.example.client.i18n.EditingMessages;

/**
 * Showcase for the merging of geometries.
 * 
 * @author Pieter De Graef
 */
public class MergePanel extends SamplePanel {

	public static final String TITLE = "gepMerge";

	public static final EditingMessages MESSAGES = GWT.create(EditingMessages.class);

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new MergePanel();
		}
	};

	@Override
	public Canvas getViewPanel() {
		MapWidget mapWidget = new MapWidget("mapGepMerging", "appEditing");
		GeometryMergeService mergingService = new GeometryMergeService();
		mergingService.setPrecision(1);

		VLayout layout = new VLayout();
		layout.setSize("100%", "100%");
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setMembersMargin(2);
		toolStrip.setWidth("100%");
		layout.addMember(toolStrip);
		layout.addMember(mapWidget);

		StartMergeProcessButton startBtn = new StartMergeProcessButton(mapWidget, mergingService);
		ExecuteMergeButton stopBtn = new ExecuteMergeButton(mapWidget, mergingService);
		CancelMergeProcessButton cancelBtn = new CancelMergeProcessButton(mapWidget, mergingService);
		toolStrip.addButton(startBtn);
		toolStrip.addButton(stopBtn);
		toolStrip.addButton(cancelBtn);

		return layout;
	}

	@Override
	public String getDescription() {
		return MESSAGES.mergeDescription();
	}

	@Override
	public String[] getConfigurationFiles() {
		return new String[]{"classpath:org/geomajas/plugin/editing/gwt/example/context/appEditing.xml",
				"classpath:org/geomajas/plugin/editing/gwt/example/context/clientLayerCountries.xml",
				"classpath:org/geomajas/plugin/editing/gwt/example/context/clientLayerOsm.xml",
				"classpath:org/geomajas/plugin/editing/gwt/example/context/mapMerging.xml",
				"classpath:org/geomajas/gwt/example/base/layerOsm.xml",
				"classpath:org/geomajas/gwt/example/base/layerCountries.xml"};
	}

	@Override
	public String ensureUserLoggedIn() {
		return "luc";
	}

}