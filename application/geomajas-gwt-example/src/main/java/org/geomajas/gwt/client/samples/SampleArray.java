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

package org.geomajas.gwt.client.samples;

import org.geomajas.gwt.client.samples.base.SampleTreeNode;
import org.geomajas.gwt.client.samples.controller.ControllerOnElementSample;
import org.geomajas.gwt.client.samples.controller.CustomControllerSample;
import org.geomajas.gwt.client.samples.controller.RectangleControllerSample;
import org.geomajas.gwt.client.samples.i18n.I18nProvider;
import org.geomajas.gwt.client.samples.mapwidget.CrsSample;
import org.geomajas.gwt.client.samples.mapwidget.GeoSample;
import org.geomajas.gwt.client.samples.mapwidget.MaxBoundsToggleSample;
import org.geomajas.gwt.client.samples.mapwidget.NavigationSample;
import org.geomajas.gwt.client.samples.mapwidget.OpenStreetMapSample;
import org.geomajas.gwt.client.samples.mapwidget.PanScaleToggleSample;
import org.geomajas.gwt.client.samples.mapwidget.UnitTypesSample;
import org.geomajas.gwt.client.samples.mapwidget.WmsSample;
import org.geomajas.gwt.client.samples.security.AttributeSecuritySample;
import org.geomajas.gwt.client.samples.security.CommandSecuritySample;
import org.geomajas.gwt.client.samples.security.FilterSecuritySample;
import org.geomajas.gwt.client.samples.security.LayerSecuritySample;
import org.geomajas.gwt.client.samples.security.LoginSample;
import org.geomajas.gwt.client.samples.security.ToolSecuritySample;
import org.geomajas.gwt.client.samples.toolbarAndControllers.CustomToolbarSample;
import org.geomajas.gwt.client.samples.toolbarAndControllers.CustomToolbarToolsSample;
import org.geomajas.gwt.client.samples.toolbarAndControllers.ScaleSelectCustomSample;
import org.geomajas.gwt.client.samples.toolbarAndControllers.ScaleSelectDefaultSample;
import org.geomajas.gwt.client.samples.toolbarAndControllers.ToolbarFeatureInfoSample;
import org.geomajas.gwt.client.samples.toolbarAndControllers.ToolbarMeasureSample;
import org.geomajas.gwt.client.samples.toolbarAndControllers.ToolbarNavigationSample;
import org.geomajas.gwt.client.samples.toolbarAndControllers.ToolbarSelectionSample;

/**
 * <p>
 * List of all test samples available in the <code>TreeGrid</code> on the left side.
 * </p>
 * 
 * @author Pieter De Graef
 */
public final class SampleArray {

	private SampleArray() {
	}

	public static SampleTreeNode[] getSampleArray() {
		return new SampleTreeNode[] {
				// MapWidget samples:
				new SampleTreeNode("MapWidget", "[ISOMORPHIC]/geomajas/example/images/world.png", "MapWidget",
						"topLevel"),
				new SampleTreeNode(I18nProvider.getSampleMessages().osmTitle(),
						"[ISOMORPHIC]/geomajas/layer-raster.png", OpenStreetMapSample.OSM_TITLE, "MapWidget",
						OpenStreetMapSample.FACTORY),
				new SampleTreeNode(I18nProvider.getSampleMessages().wmsTitle(),
						"[ISOMORPHIC]/geomajas/layer-raster.png", WmsSample.WMS_TITLE, "MapWidget", WmsSample.FACTORY),
				new SampleTreeNode(I18nProvider.getSampleMessages().geoTitle(),
						"[ISOMORPHIC]/geomajas/layer-raster.png", GeoSample.GEO_TITLE, "MapWidget", GeoSample.FACTORY),
				new SampleTreeNode(I18nProvider.getSampleMessages().navigationTitle(),
						"[ISOMORPHIC]/geomajas/layer.png", NavigationSample.TITLE, "MapWidget",
						NavigationSample.FACTORY),
				new SampleTreeNode(I18nProvider.getSampleMessages().crsTitle(), "[ISOMORPHIC]/geomajas/layer.png",
						CrsSample.TITLE, "MapWidget", CrsSample.FACTORY),
				new SampleTreeNode(I18nProvider.getSampleMessages().unitTypesTitle(),
						"[ISOMORPHIC]/geomajas/layer.png", UnitTypesSample.TITLE, "MapWidget", UnitTypesSample.FACTORY),
				new SampleTreeNode(I18nProvider.getSampleMessages().maxBoundsToggleTitle(),
						"[ISOMORPHIC]/geomajas/layer.png", MaxBoundsToggleSample.TITLE, "MapWidget",
						MaxBoundsToggleSample.FACTORY),
				new SampleTreeNode(I18nProvider.getSampleMessages().panScaleToggleTitle(),
						"[ISOMORPHIC]/geomajas/layer.png", PanScaleToggleSample.TITLE, "MapWidget",
						PanScaleToggleSample.FACTORY),

				// Map controller:
				new SampleTreeNode(I18nProvider.getSampleMessages().treeGroupMapController(),
						"[ISOMORPHIC]/geomajas/tools.png", "MapController", "topLevel"),

				new SampleTreeNode(I18nProvider.getSampleMessages().customControllerTitle(),
						"[ISOMORPHIC]/geomajas/tools.png", CustomControllerSample.TITLE, "MapController",
						CustomControllerSample.FACTORY),
				new SampleTreeNode(I18nProvider.getSampleMessages().controllerOnElementTitle(),
						"[ISOMORPHIC]/geomajas/tools.png", ControllerOnElementSample.TITLE, "MapController",
						ControllerOnElementSample.FACTORY),
				new SampleTreeNode(I18nProvider.getSampleMessages().rectangleControllerTitle(),
						"[ISOMORPHIC]/geomajas/tools.png", RectangleControllerSample.TITLE, "MapController",
						RectangleControllerSample.FACTORY),

				// Toolbar and controllers:
				new SampleTreeNode(I18nProvider.getSampleMessages().treeGroupToolbarAndControllers(),
						"[ISOMORPHIC]/geomajas/zoom-in.png", "ToolbarAndControllers", "topLevel"),

				new SampleTreeNode(I18nProvider.getSampleMessages().toolbarNavigationTitle(),
						"[ISOMORPHIC]/geomajas/pan.png", ToolbarNavigationSample.TITLE, "ToolbarAndControllers",
						ToolbarNavigationSample.FACTORY),

				new SampleTreeNode(I18nProvider.getSampleMessages().toolbarSelectionTitle(),
						"[ISOMORPHIC]/geomajas/select.png", ToolbarSelectionSample.TITLE, "ToolbarAndControllers",
						ToolbarSelectionSample.FACTORY),

				new SampleTreeNode(I18nProvider.getSampleMessages().toolbarMeasureTitle(),
						"[ISOMORPHIC]/geomajas/length-measure.png", ToolbarMeasureSample.TITLE,
						"ToolbarAndControllers", ToolbarMeasureSample.FACTORY),

				new SampleTreeNode(I18nProvider.getSampleMessages().toolbarFeatureInfoTitle(),
						"[ISOMORPHIC]/geomajas/info.png", ToolbarFeatureInfoSample.TITLE,
						"ToolbarAndControllers", ToolbarFeatureInfoSample.FACTORY),

				new SampleTreeNode(I18nProvider.getSampleMessages().scaleSelectDefaultTitle(),
						"[ISOMORPHIC]/geomajas/tools.png", ScaleSelectDefaultSample.TITLE,
						"ToolbarAndControllers", ScaleSelectDefaultSample.FACTORY),

				new SampleTreeNode(I18nProvider.getSampleMessages().scaleSelectCustomTitle(),
						"[ISOMORPHIC]/geomajas/tools.png", ScaleSelectCustomSample.TITLE,
						"ToolbarAndControllers", ScaleSelectCustomSample.FACTORY),

				new SampleTreeNode(I18nProvider.getSampleMessages().customToolbarToolsTitle(),
						"[ISOMORPHIC]/geomajas/tools.png", CustomToolbarToolsSample.TITLE,
						"ToolbarAndControllers", CustomToolbarToolsSample.FACTORY),
						
				new SampleTreeNode(I18nProvider.getSampleMessages().customToolbarTitle(),
						"[ISOMORPHIC]/geomajas/tools.png", CustomToolbarSample.TITLE,
						"ToolbarAndControllers", CustomToolbarSample.FACTORY),
						
				// Security samples:
				new SampleTreeNode(I18nProvider.getSampleMessages().treeGroupSecurity(),
						"[ISOMORPHIC]/geomajas/silk/key.png", "Security", "topLevel"),
				new SampleTreeNode(I18nProvider.getSampleMessages().loginTitle(),
						"[ISOMORPHIC]/geomajas/springsecurity/key_go.png", LoginSample.LOGIN_TITLE, "Security",
						LoginSample.FACTORY),
				new SampleTreeNode(I18nProvider.getSampleMessages().layerSecurityTitle(),
						"[ISOMORPHIC]/geomajas/springsecurity/key_go.png", LayerSecuritySample.LAYER_SECUTIRY_TITLE,
						"Security", LayerSecuritySample.FACTORY),
				// new SampleTreeNode(I18nProvider.getSampleMessages().featureSecurityTitle(),
				// "[ISOMORPHIC]/geomajas/springsecurity/key_go.png",
				// FeatureSecuritySample.FEATURE_SECUTIRY_TITLE, "Security", FeatureSecuritySample.FACTORY),
				new SampleTreeNode(I18nProvider.getSampleMessages().filterSecurityTitle(),
						"[ISOMORPHIC]/geomajas/springsecurity/key_go.png", FilterSecuritySample.TITLE, "Security",
						FilterSecuritySample.FACTORY),
				new SampleTreeNode(I18nProvider.getSampleMessages().attributeSecurityTitle(),
						"[ISOMORPHIC]/geomajas/springsecurity/key_go.png", AttributeSecuritySample.TITLE, "Security",
						AttributeSecuritySample.FACTORY),
				new SampleTreeNode(I18nProvider.getSampleMessages().commandSecurityTitle(),
						"[ISOMORPHIC]/geomajas/springsecurity/key_go.png", CommandSecuritySample.TITLE, "Security",
						CommandSecuritySample.FACTORY),
				new SampleTreeNode(I18nProvider.getSampleMessages().toolSecurityTitle(),
						"[ISOMORPHIC]/geomajas/springsecurity/key_go.png", ToolSecuritySample.TITLE, "Security",
						ToolSecuritySample.FACTORY) };
	}
}
