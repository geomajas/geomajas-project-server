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
 * Sample that shows how to enable/disable maxBounds.
 * </p>
 * 
 * @author Frank Wynants
 */
public class MaxBoundsToggleSample extends SamplePanel {

	private boolean isBelgiumBoundsEnabled;

	private Bbox maxBounds;

	public static final String TITLE = "MaxBoundsToggle";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new MaxBoundsToggleSample();
		}
	};

	public Canvas getViewPanel() {
		final Bbox belgiumBounds = new Bbox(135977.5229612165, 6242678.930647222, 679887.6148060877, 625824.2623034349);

		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();
		layout.setMembersMargin(10);

		HLayout mapLayout = new HLayout();
		mapLayout.setShowEdges(true);
		mapLayout.setHeight("60%");

		// Map with ID mapOsm is defined in the XML configuration. (mapOsm.xml)
		final MapWidget map = new MapWidget("mapOsm", "gwt-samples");
		map.setController(new PanController(map));
		mapLayout.addMember(map);

		HLayout buttonLayout = new HLayout();
		buttonLayout.setMembersMargin(10);

		// Create a button to toggle bounds between the whole world an Belgium
		final IButton butToggleMaxBounds = new IButton(I18nProvider.getSampleMessages().toggleMaxBoundsBelgium());
		butToggleMaxBounds.setWidth100();
		buttonLayout.addMember(butToggleMaxBounds);

		butToggleMaxBounds.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				if (isBelgiumBoundsEnabled) { // switch to whole world bounds
					isBelgiumBoundsEnabled = false;
					map.getMapModel().getMapView().setMaxBounds(maxBounds);
					butToggleMaxBounds.setTitle(I18nProvider.getSampleMessages().toggleMaxBoundsBelgium());
				} else { // switch to Belgium bounds
					if (maxBounds == null) {
						// First we save the World bounds:
						maxBounds = map.getMapModel().getMapView().getMaxBounds();
					}
					
					map.getMapModel().getMapView().setMaxBounds(belgiumBounds);
					// force a new scale level so we go inside the bounding box of Belgium
					map.getMapModel().getMapView().applyBounds(belgiumBounds, ZoomOption.LEVEL_FIT);
					isBelgiumBoundsEnabled = true;
					butToggleMaxBounds.setTitle(I18nProvider.getSampleMessages().toggleMaxBoundsWorld());
				}
			}
		});

		layout.addMember(mapLayout);
		layout.addMember(buttonLayout);

		return layout;
	}

	public String getDescription() {
		return I18nProvider.getSampleMessages().maxBoundsToggleDescription();
	}

	public String getSourceFileName() {
		return "classpath:org/geomajas/example/gwt/client/samples/mapwidget/MaxBoundsToggleSample.txt";
	}

	public String[] getConfigurationFiles() {
		return new String[] { "WEB-INF/layerOsm.xml",
				"WEB-INF/mapOsm.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
