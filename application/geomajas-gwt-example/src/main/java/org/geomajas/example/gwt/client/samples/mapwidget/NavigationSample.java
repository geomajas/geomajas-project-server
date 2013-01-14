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

package org.geomajas.example.gwt.client.samples.mapwidget;

import org.geomajas.example.gwt.client.samples.base.SamplePanel;
import org.geomajas.example.gwt.client.samples.base.SamplePanelFactory;
import org.geomajas.example.gwt.client.samples.i18n.I18nProvider;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.controller.PanController;
import org.geomajas.gwt.client.map.MapView.ZoomOption;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.widget.MapWidget;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * <p>
 * Sample that shows a map with an OpenStreetMap layer and a list of navigation buttons, testing the MapView.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class NavigationSample extends SamplePanel {

	public static final String TITLE = "MapViewNavigation";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new NavigationSample();
		}
	};

	public Canvas getViewPanel() {
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();
		layout.setMembersMargin(10);

		// Create a 3-column layout for buttons:
		HLayout hLayout = new HLayout();
		hLayout.setMembersMargin(15);
		hLayout.setPadding(5);
		VLayout firstColumn = new VLayout();
		firstColumn.setMembersMargin(5);
		VLayout secondColumn = new VLayout();
		secondColumn.setMembersMargin(5);
		VLayout thirdColumn = new VLayout();
		thirdColumn.setMembersMargin(5);

		// Map with ID mapOsm is defined in the XML configuration. (mapOsm.xml)
		final MapWidget map = new MapWidget("mapOsm", "gwt-samples");

		// Set a panning controller on the map:
		map.setController(new PanController(map));

		VLayout mapLayout = new VLayout();
		mapLayout.setShowEdges(true);
		mapLayout.setHeight("60%");
		mapLayout.addMember(map);

		// Create a button that centers the map to (0,0):
		IButton centerBTN = new IButton(I18nProvider.getSampleMessages().navigationBtnPosition());
		centerBTN.setWidth100();
		centerBTN.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				map.getMapModel().getMapView().setCenterPosition(new Coordinate(-10000000, 2000000));
			}
		});

		// Create a button that translate the map:
		IButton translateBTN = new IButton(I18nProvider.getSampleMessages().navigationBtnTranslate());
		translateBTN.setWidth100();
		translateBTN.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				map.getMapModel().getMapView().translate(1000000, -500000);
			}
		});

		// Create a button that applies a bounding box to zoom to:
		IButton bboxBTN = new IButton(I18nProvider.getSampleMessages().navigationBtnBbox());
		bboxBTN.setWidth100();
		bboxBTN.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				map.getMapModel().getMapView().applyBounds(new Bbox(1200000, 4600000, 1400000, 1400000),
						ZoomOption.EXACT);
			}
		});

		// Create a button that zooms out:
		IButton zoomOutBTN = new IButton(I18nProvider.getSampleMessages().navigationBtnZoomOut());
		zoomOutBTN.setWidth100();
		zoomOutBTN.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				map.getMapModel().getMapView().scale(0.5, ZoomOption.LEVEL_CHANGE);
			}
		});

		// Create a button that zooms in:
		IButton zoomInBTN = new IButton(I18nProvider.getSampleMessages().navigationBtnZoomIn());
		zoomInBTN.setWidth100();
		zoomInBTN.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				map.getMapModel().getMapView().scale(2, ZoomOption.LEVEL_CHANGE);
			}
		});

		firstColumn.addMember(zoomOutBTN);
		firstColumn.addMember(zoomInBTN);

		secondColumn.addMember(centerBTN);
		secondColumn.addMember(translateBTN);

		thirdColumn.addMember(bboxBTN);

		hLayout.addMember(firstColumn);
		hLayout.addMember(secondColumn);
		hLayout.addMember(thirdColumn);

		layout.addMember(mapLayout);
		layout.addMember(hLayout);

		return layout;
	}

	public String getDescription() {
		return I18nProvider.getSampleMessages().navigationDescription();
	}

	public String getSourceFileName() {
		return "classpath:org/geomajas/example/gwt/client/samples/mapwidget/NavigationSample.txt";
	}

	public String[] getConfigurationFiles() {
		return new String[] { "WEB-INF/layerOsm.xml", "WEB-INF/mapOsm.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
