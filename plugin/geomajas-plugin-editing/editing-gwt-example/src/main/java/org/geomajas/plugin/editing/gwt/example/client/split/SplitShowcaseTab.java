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

package org.geomajas.plugin.editing.gwt.example.client.split;

import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.editing.client.split.GeometrySplitService;
import org.geomajas.plugin.editing.gwt.client.GeometryEditor;
import org.geomajas.plugin.editing.gwt.example.client.widget.RedoBtn;
import org.geomajas.plugin.editing.gwt.example.client.widget.UndoBtn;

import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * Tab wherein the splitting showcase takes place.
 * 
 * @author Pieter De Graef
 */
public class SplitShowcaseTab extends Tab {

	private final MapWidget mapWidget;

	private final GeometryEditor editor;

	private final GeometrySplitService service;

	public SplitShowcaseTab() {
		super("Splitting Geometries");
		mapWidget = new MapWidget("mapSplitting", "app");
		editor = new GeometryEditor(mapWidget);
		service = new GeometrySplitService(editor.getService());

		VLayout layout = new VLayout();
		layout.setSize("100%", "100%");
		ToolStrip toolStrip = new ToolStrip();
		toolStrip.setWidth100();
		toolStrip.setHeight(36);
		toolStrip.addButton(new SplitCountryButton(service, mapWidget));
		toolStrip.addSeparator();

		ExecuteSplitButton splitBtn = new ExecuteSplitButton(mapWidget, service);
		toolStrip.addButton(splitBtn);

		// Add buttons to help the editing process:
		toolStrip.addButton(new CancelSplitProcessButton(service));

		UndoBtn undoBtn = new UndoBtn(editor.getService());
		toolStrip.addButton(undoBtn);

		RedoBtn redoBtn = new RedoBtn(editor.getService());
		toolStrip.addButton(redoBtn);

		layout.addMember(toolStrip);
		layout.addMember(mapWidget);
		setPane(layout);
	}
}