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

package org.geomajas.gwt.example.client.sample.mapwidget;

import com.google.gwt.core.client.GWT;
import org.geomajas.gwt.example.base.SamplePanel;
import org.geomajas.gwt.example.base.SamplePanelFactory;
import org.geomajas.gwt.client.controller.PanController;
import org.geomajas.gwt.client.gfx.style.ShapeStyle;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.OverviewMap;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.gwt.example.client.sample.i18n.SampleMessages;

/**
 * <p>
 * Sample that shows the relation between an overview map and a normal map.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class OverviewMapSample extends SamplePanel {

	private static final SampleMessages MESSAGES = GWT.create(SampleMessages.class);

	public static final String TITLE = "OverviewMap";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new OverviewMapSample();
		}
	};

	public Canvas getViewPanel() {
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();
		layout.setMembersMargin(10);

		// Create a layout for the main map:
		HLayout mapLayout = new HLayout();
		mapLayout.setShowEdges(true);

		// Map with ID osmNavigationToolbarMap is defined in the XML configuration.
		final MapWidget map = new MapWidget("mapOsm", "gwtExample");
		map.setController(new PanController(map));
		mapLayout.addMember(map);

		// Create a layout for the overview map:
		HLayout bottomLayout = new HLayout();
		bottomLayout.setHeight(300);
		bottomLayout.setMembersMargin(10);
		bottomLayout.setAlign(Alignment.CENTER);

		VLayout overviewMapLayout = new VLayout();
		overviewMapLayout.setShowEdges(true);
		overviewMapLayout.setWidth(300);

		// Create an overview map:
		final OverviewMap overviewMap = new OverviewMap("mapOverviewOsm", "gwtExample", map, true, true);
		overviewMapLayout.addMember(overviewMap);

		// Create a layout for a few buttons:
		HLayout buttonLayout = new HLayout();
		buttonLayout.setHeight(20);
		buttonLayout.setAlign(VerticalAlignment.BOTTOM);
		buttonLayout.setMembersMargin(10);

		// ---------------------------------------------------------------------
		// Creating 3 buttons:
		// ---------------------------------------------------------------------

		// Button1: Toggle the rectangle style:
		IButton button1 = new IButton(MESSAGES.overviewMapToggleRectStyle());
		button1.addClickHandler(new ClickHandler() {

			private ShapeStyle nextStyle = new ShapeStyle("#000000", 0.6f, "#000000", 1, 1);

			public void onClick(ClickEvent event) {
				ShapeStyle temp = nextStyle;
				nextStyle = overviewMap.getRectangleStyle();
				overviewMap.setRectangleStyle(temp);
			}
		});
		button1.setWidth100();
		buttonLayout.addMember(button1);

		// Button2: Toggle the maximum extent style:
		IButton button2 = new IButton(MESSAGES.overviewMapToggleExtentStyle());
		button2.addClickHandler(new ClickHandler() {

			private ShapeStyle nextStyle = new ShapeStyle("#FF0000", 0.6f, "#FF0000", 1, 3);

			public void onClick(ClickEvent event) {
				ShapeStyle temp = nextStyle;
				nextStyle = overviewMap.getTargetMaxExtentRectangleStyle();
				overviewMap.setTargetMaxExtentRectangleStyle(temp);
			}
		});
		button2.setWidth100();
		buttonLayout.addMember(button2);

		// Button3: Toggle drawing the maximum extent:
		IButton button3 = new IButton(MESSAGES.overviewMapToggleExtent());
		button3.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				overviewMap.setDrawTargetMaxExtent(!overviewMap.isDrawTargetMaxExtent());
			}
		});
		button3.setWidth100();
		buttonLayout.addMember(button3);

		// ---------------------------------------------------------------------
		// Place the layouts together:
		// ---------------------------------------------------------------------

		bottomLayout.addMember(overviewMapLayout);
		//bottomLayout.addMember(buttonLayout);

		layout.addMember(mapLayout);
		layout.addMember(bottomLayout);
		layout.addMember(buttonLayout);

		return layout;
	}

	public String getDescription() {
		return MESSAGES.overviewMapDescription();
	}

	public String[] getConfigurationFiles() {
		return new String[] {
				"classpath:org/geomajas/gwt/example/context/mapOsm.xml",
				"classpath:org/geomajas/gwt/example/context/mapOverviewOsm.xml",
				"classpath:org/geomajas/gwt/example/base/layerOsm.xml" };

	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
