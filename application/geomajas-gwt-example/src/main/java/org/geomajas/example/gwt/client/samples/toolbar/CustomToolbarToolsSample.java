/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.example.gwt.client.samples.toolbar;

import org.geomajas.example.gwt.client.samples.base.SamplePanel;
import org.geomajas.example.gwt.client.samples.base.SamplePanelFactory;
import org.geomajas.example.gwt.client.samples.i18n.I18nProvider;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.action.ToolbarModalAction;
import org.geomajas.gwt.client.controller.AbstractGraphicsController;
import org.geomajas.gwt.client.controller.GraphicsController;
import org.geomajas.gwt.client.controller.PanController;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.Toolbar;

import com.google.gwt.event.dom.client.MouseUpEvent;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * <p>
 * Sample that shows how a custom tools can be added to a toolbar.
 * </p>
 * 
 * @author Frank Wynants
 */
public class CustomToolbarToolsSample extends SamplePanel {

	public static final String TITLE = "CustomToolbarTools";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new CustomToolbarToolsSample();
		}
	};

	/**
	 * @return The viewPanel Canvas
	 */
	public Canvas getViewPanel() {
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();

		final MapWidget map = new MapWidget("osmMap", "gwt-samples");
		map.setController(new PanController(map));

		final Toolbar toolbar = new Toolbar(map);
		toolbar.setButtonSize(Toolbar.BUTTON_SIZE_BIG);

		// Create a custom controller that will be enabled/disabled by a button in the toolbar:
		final GraphicsController customController = new AbstractGraphicsController(map) {

			public void onMouseUp(MouseUpEvent event) {
				Coordinate screenPosition = getScreenPosition(event);
				Coordinate worldPosition = getWorldPosition(event);
				SC.say(I18nProvider.getSampleMessages().customControllerScreenCoordinates() + " = " + screenPosition
						+ "<br/>" + I18nProvider.getSampleMessages().customControllerWorldCoordinates() + " = "
						+ worldPosition);
			}
		};

		// Add the customController to the toolbar using a custom ToolbarModalAction button
		toolbar.addModalButton(new ToolbarModalAction("[ISOMORPHIC]/geomajas/widget/target.gif", I18nProvider
				.getSampleMessages().customToolbarToolsTooltip()) {

			@Override
			public void onSelect(ClickEvent event) {
				map.setController(customController);
			}

			@Override
			public void onDeselect(ClickEvent event) {
				map.setController(null);
			}
		});

		layout.addMember(toolbar);
		layout.addMember(map);

		return layout;
	}

	public String getDescription() {
		return I18nProvider.getSampleMessages().customToolbarToolsDescription();
	}

	public String getSourceFileName() {
		return "classpath:org/geomajas/gwt/client/samples/toolbar/CustomToolbarToolsSample.txt";
	}

	public String[] getConfigurationFiles() {
		return new String[] { "classpath:org/geomajas/gwt/samples/mapwidget/layerOsm.xml",
				"classpath:org/geomajas/gwt/samples/mapwidget/mapOsm.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
