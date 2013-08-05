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

package org.geomajas.smartgwt.example.client.sample.attribute;

import org.geomajas.command.dto.SearchFeatureRequest;
import org.geomajas.command.dto.SearchFeatureResponse;
import org.geomajas.global.GeomajasConstant;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.smartgwt.client.controller.SelectionController;
import org.geomajas.smartgwt.client.map.feature.Feature;
import org.geomajas.smartgwt.client.map.layer.VectorLayer;
import org.geomajas.smartgwt.client.widget.FeatureListGrid;
import org.geomajas.smartgwt.client.widget.MapWidget;
import org.geomajas.smartgwt.example.base.SamplePanel;
import org.geomajas.smartgwt.example.base.SamplePanelFactory;
import org.geomajas.smartgwt.example.client.sample.i18n.SampleMessages;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * <p>
 * Sample that shows a FeatureListGrid widget and some features in it.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class FeatureListGridSample extends SamplePanel {

	private static final SampleMessages MESSAGES = GWT.create(SampleMessages.class);

	public static final String TITLE = "FeatureListGridSample";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new FeatureListGridSample();
		}
	};

	public Canvas getViewPanel() {
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();
		layout.setMembersMargin(10);

		// switch off lazy loading, we want to get everything
		GwtCommandDispatcher.getInstance().setUseLazyLoading(false);

		VLayout mapLayout = new VLayout();
		mapLayout.setShowEdges(true);

		// Map with ID featureListGridMap is defined in the XML configuration.
		final MapWidget map = new MapWidget("mapFeatureListGrid", "gwtExample");
		map.setController(new SelectionController(map, 1000, 0.7f, false, 5));
		mapLayout.addMember(map);

		// Create a layout with a FeatureListGrid in it:
		final FeatureListGrid grid = new FeatureListGrid(map.getMapModel());
		grid.setShowEdges(true);
		grid.setShowResizeBar(true);

		// Add a trigger to fill the grid when the map has finished loading:
		map.getMapModel().runWhenInitialized(new Runnable() {

			public void run() {
				final VectorLayer layer = map.getMapModel().getVectorLayer("clientLayerCountries110mGrid");
				grid.setLayer(layer);
				SearchFeatureRequest searchFeatureRequest = new SearchFeatureRequest();
				searchFeatureRequest.setCrs(map.getMapModel().getCrs());
				searchFeatureRequest.setFeatureIncludes(GeomajasConstant.FEATURE_INCLUDE_ATTRIBUTES);
				searchFeatureRequest.setLayerId("layerCountries110m");
				GwtCommand searchCommand = new GwtCommand(SearchFeatureRequest.COMMAND);
				searchCommand.setCommandRequest(searchFeatureRequest);

				GwtCommandDispatcher.getInstance().execute(searchCommand,
						new AbstractCommandCallback<SearchFeatureResponse>() {

							public void execute(SearchFeatureResponse response) {
								for (org.geomajas.layer.feature.Feature feature : response.getFeatures()) {
									grid.addFeature(new Feature(feature, layer));
								}
							}
						});
			}
		});

		layout.addMember(grid);
		layout.addMember(mapLayout);

		return layout;
	}

	public String getDescription() {
		return MESSAGES.fltDescription();
	}

	public String[] getConfigurationFiles() {
		return new String[] { "classpath:org/geomajas/smartgwt/example/context/mapFeatureListGrid.xml",
				"classpath:org/geomajas/smartgwt/example/base/layerCountries110m.xml",
				"classpath:org/geomajas/smartgwt/example/base/layerWmsBluemarble.xml", };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
