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

package org.geomajas.gwt.example.client.sample.toolbar;

import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.action.ToolbarModalAction;
import org.geomajas.gwt.client.controller.AbstractGraphicsController;
import org.geomajas.gwt.client.controller.GraphicsController;
import org.geomajas.gwt.client.controller.PanController;
import org.geomajas.gwt.client.map.RenderSpace;
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.Toolbar;
import org.geomajas.gwt.example.base.SamplePanel;
import org.geomajas.gwt.example.base.SamplePanelFactory;
import org.geomajas.gwt.example.client.sample.i18n.SampleMessages;

import com.google.gwt.core.client.GWT;
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

	private static final SampleMessages MESSAGES = GWT.create(SampleMessages.class);

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

		final MapWidget map = new MapWidget("mapCustomToolbarTools", "gwtExample");
		map.setController(new PanController(map));

		final Toolbar toolbar = new Toolbar(map);
		toolbar.setButtonSize(WidgetLayout.toolbarLargeButtonSize);

		// Create a custom controller that will be enabled/disabled by a button in the toolbar:
		final GraphicsController customController = new AbstractGraphicsController(map) {

			public void onMouseUp(MouseUpEvent event) {
				Coordinate screenPosition = getLocation(event, RenderSpace.SCREEN);
				Coordinate worldPosition = getLocation(event, RenderSpace.WORLD);
				SC.say(MESSAGES.customControllerScreenCoordinates() + " = " + screenPosition
						+ "<br/>" + MESSAGES.customControllerWorldCoordinates() + " = "
						+ worldPosition);
			}
		};

		map.getMapModel().runWhenInitialized(new Runnable() {

			public void run() {
				// Add the customController to the toolbar using a custom ToolbarModalAction button
				toolbar.addModalButton(new ToolbarModalAction("[ISOMORPHIC]/geomajas/widget/target.gif",
						MESSAGES.customToolbarToolsTooltip()) {

					@Override
					public void onSelect(ClickEvent event) {
						map.setController(customController);
					}

					@Override
					public void onDeselect(ClickEvent event) {
						map.setController(null);
					}
				});
			}
		});


		layout.addMember(toolbar);
		layout.addMember(map);

		return layout;
	}

	public String getDescription() {
		return MESSAGES.customToolbarToolsDescription();
	}

	public String[] getConfigurationFiles() {
		return new String[] {
				"classpath:org/geomajas/gwt/example/context/mapCustomToolbarTools.xml",
				"classpath:org/geomajas/gwt/example/base/layerOsm.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
