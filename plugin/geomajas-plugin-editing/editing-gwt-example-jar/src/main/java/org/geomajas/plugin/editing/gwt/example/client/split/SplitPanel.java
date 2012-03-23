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

package org.geomajas.plugin.editing.gwt.example.client.split;

import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.example.base.SamplePanel;
import org.geomajas.gwt.example.base.SamplePanelFactory;
import org.geomajas.plugin.editing.client.split.GeometrySplitService;
import org.geomajas.plugin.editing.gwt.client.GeometryEditor;
import org.geomajas.plugin.editing.gwt.client.GeometryEditorImpl;
import org.geomajas.plugin.editing.gwt.example.client.i18n.EditingMessages;
import org.geomajas.plugin.editing.gwt.example.client.widget.RedoBtn;
import org.geomajas.plugin.editing.gwt.example.client.widget.UndoBtn;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * Tab wherein the splitting showcase takes place.
 * 
 * @author Pieter De Graef
 */
public class SplitPanel extends SamplePanel {

	public static final String TITLE = "gepSplit";

	public static final EditingMessages MESSAGES = GWT.create(EditingMessages.class);

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new SplitPanel();
		}
	};

	/** {@inheritDoc} */
	public Canvas getViewPanel() {
		MapWidget mapWidget = new MapWidget("mapGepSplitting", "appEditing");
		GeometryEditor editor = new GeometryEditorImpl(mapWidget);
		GeometrySplitService service = new GeometrySplitService(editor.getEditService());

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

		UndoBtn undoBtn = new UndoBtn(editor.getEditService());
		toolStrip.addButton(undoBtn);

		RedoBtn redoBtn = new RedoBtn(editor.getEditService());
		toolStrip.addButton(redoBtn);

		layout.addMember(toolStrip);
		layout.addMember(mapWidget);

		// Add the possibility to calculate area's while drawing the splitting line:
		new SplitShowAreaHandler(mapWidget, service);
		
		return layout;
	}

	/** {@inheritDoc} */
	public String getDescription() {
		return MESSAGES.splitDescription();
	}

	/** {@inheritDoc} */
	public String[] getConfigurationFiles() {
		return new String[]{"classpath:org/geomajas/plugin/editing/gwt/example/context/appEditing.xml",
				"classpath:org/geomajas/plugin/editing/gwt/example/context/clientLayerCountries.xml",
				"classpath:org/geomajas/plugin/editing/gwt/example/context/clientLayerOsm.xml",
				"classpath:org/geomajas/plugin/editing/gwt/example/context/mapSplitting.xml",
				"classpath:org/geomajas/gwt/example/base/layerOsm.xml",
				"classpath:org/geomajas/gwt/example/base/layerCountries.xml"};
	}

	/** {@inheritDoc} */
	public String ensureUserLoggedIn() {
		return "luc";
	}

}