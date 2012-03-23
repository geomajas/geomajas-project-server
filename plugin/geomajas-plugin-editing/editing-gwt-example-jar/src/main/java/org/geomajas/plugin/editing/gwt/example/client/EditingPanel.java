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

package org.geomajas.plugin.editing.gwt.example.client;

import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.example.base.SamplePanel;
import org.geomajas.gwt.example.base.SamplePanelFactory;
import org.geomajas.plugin.editing.gwt.client.GeometryEditor;
import org.geomajas.plugin.editing.gwt.client.GeometryEditorImpl;
import org.geomajas.plugin.editing.gwt.example.client.i18n.EditingMessages;
import org.geomajas.plugin.editing.gwt.example.client.widget.MenuBar;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Entry point and main class for GWT application. This class defines the layout and functionality of this application.
 *
 * @author Pieter De Graef
 */
public class EditingPanel extends SamplePanel {

	public static final String TITLE = "gepEditing";

	public static final EditingMessages MESSAGES = GWT.create(EditingMessages.class);

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new EditingPanel();
		}
	};

	/** {@inheritDoc} */
	public Canvas getViewPanel() {
		final MapWidget map = new MapWidget("mapGepEditing", "appEditing");
		final GeometryEditor editor = new GeometryEditorImpl(map);

		VLayout layout = new VLayout();
		MenuBar editingToolStrip = new MenuBar(editor);
		layout.addMember(editingToolStrip);
		layout.addMember(map);
		layout.setHeight("100%");

		return layout;
	}

	/** {@inheritDoc} */
	public String getDescription() {
		return MESSAGES.editingDescription();
	}

	/** {@inheritDoc} */
	public String[] getConfigurationFiles() {
		return new String[] {"classpath:org/geomajas/plugin/editing/gwt/example/context/appEditing.xml",
				"classpath:org/geomajas/plugin/editing/gwt/example/context/clientLayerCountries.xml",
				"classpath:org/geomajas/plugin/editing/gwt/example/context/clientLayerOsm.xml",
				"classpath:org/geomajas/plugin/editing/gwt/example/context/mapEditing.xml",
				"classpath:org/geomajas/gwt/example/base/layerOsm.xml",
				"classpath:org/geomajas/gwt/example/base/layerCountries.xml"};
	}

	/** {@inheritDoc} */
	public String ensureUserLoggedIn() {
		return "luc";
	}

}