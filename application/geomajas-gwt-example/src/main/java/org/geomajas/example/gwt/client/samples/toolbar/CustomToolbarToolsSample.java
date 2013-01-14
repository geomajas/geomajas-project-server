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

		final MapWidget map = new MapWidget("mapOsm", "gwt-samples");
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
		return "classpath:org/geomajas/example/gwt/client/samples/toolbar/CustomToolbarToolsSample.txt";
	}

	public String[] getConfigurationFiles() {
		return new String[] { "WEB-INF/layerOsm.xml",
				"WEB-INF/mapOsm.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
