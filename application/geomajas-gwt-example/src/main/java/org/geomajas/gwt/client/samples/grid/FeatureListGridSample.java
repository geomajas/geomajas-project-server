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

package org.geomajas.gwt.client.samples.grid;

import java.util.List;

import com.google.gwt.core.client.GWT;
import org.geomajas.global.GeomajasConstant;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.controller.PanController;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.cache.tile.TileFunction;
import org.geomajas.gwt.client.map.cache.tile.VectorTile;
import org.geomajas.gwt.client.map.event.MapModelEvent;
import org.geomajas.gwt.client.map.event.MapModelHandler;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.feature.LazyLoadCallback;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.samples.base.SamplePanel;
import org.geomajas.gwt.client.samples.base.SamplePanelFactory;
import org.geomajas.gwt.client.samples.i18n.I18nProvider;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.widget.FeatureListGrid;
import org.geomajas.gwt.client.widget.MapWidget;

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

		// Map with ID osmFeatureInfoMap is defined in the XML configuration.
		final MapWidget map = new MapWidget("africanCountriesMap", "gwt-samples");
		map.setController(new PanController(map));
		mapLayout.addMember(map);

		// Create a layout with a FeatureListGrid in it:
		final FeatureListGrid grid = new FeatureListGrid(map.getMapModel());
		grid.setShowEdges(true);

		// Add a trigger to fill the grid when the map has finished loading:
		map.getMapModel().addMapModelHandler(new MapModelHandler() {

			public void onMapModelChange(MapModelEvent event) {
				GWT.log("+++ map model changed", null);
				MapModel mapModel = map.getMapModel();
				VectorLayer layer = (VectorLayer) mapModel.getLayer("mflgCountriesLayer");
				grid.setLayer(layer);
				Bbox bounds = mapModel.getMapView().getBounds(); 
				layer.getFeatureStore().queryAndSync(bounds, null, null,
						new TileFunction<VectorTile>() {

							public void execute(VectorTile tile) {
								tile.getFeatures(GeomajasConstant.FEATURE_INCLUDE_ALL, new LazyLoadCallback() {

									public void execute(List<Feature> response) {
										for (Feature feature : response) {
											grid.addFeature(feature);
										}
									}
								});
							}
						});
			}
		});

		layout.addMember(grid);
		layout.addMember(mapLayout);

		return layout;
	}

	public String getDescription() {
		return I18nProvider.getSampleMessages().fltDescription();
	}

	public String getSourceFileName() {
		return "classpath:org/geomajas/gwt/client/samples/grid/FeatureListGridSample.txt";
	}

	public String[] getConfigurationFiles() {
		return new String[] { "classpath:org/geomajas/gwt/samples/grid/mapFeatureListGrid.xml",
				"classpath:org/geomajas/gwt/samples/shapeinmem/layerCountries.xml",
				"classpath:org/geomajas/gwt/samples/mapwidget/layerWmsBluemarble.xml",
		};
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
