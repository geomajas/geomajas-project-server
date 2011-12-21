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

package org.geomajas.plugin.editing.gwt.example.client.merge;

import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.editing.client.merge.GeometryMergeService;

import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * Showcase for the merging of geometries.
 * 
 * @author Pieter De Graef
 */
public class MergeShowcaseTab extends Tab {

	private final MapWidget mapWidget;

	private final GeometryMergeService mergingService;

	public MergeShowcaseTab() {
		super("Merging geometries");
		mapWidget = new MapWidget("mapMerging", "app");
		mergingService = new GeometryMergeService();
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

		setPane(layout);
	}
}