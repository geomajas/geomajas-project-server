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

package org.geomajas.example.gwt.client.samples;

import org.geomajas.example.gwt.client.samples.attribute.AttributeCustomFormSample;
import org.geomajas.example.gwt.client.samples.attribute.AttributeCustomTypeSample;
import org.geomajas.example.gwt.client.samples.attribute.AttributeIncludeInFormSample;
import org.geomajas.example.gwt.client.samples.attribute.AttributeSearchSample;
import org.geomajas.example.gwt.client.samples.attribute.EditAttributeSample;
import org.geomajas.example.gwt.client.samples.attribute.EditableGridSample;
import org.geomajas.example.gwt.client.samples.attribute.FeatureListGridSample;
import org.geomajas.example.gwt.client.samples.attribute.SearchSample;
import org.geomajas.example.gwt.client.samples.base.SampleTreeNode;
import org.geomajas.example.gwt.client.samples.controller.CircleControllerSample;
import org.geomajas.example.gwt.client.samples.controller.ControllerOnElementSample;
import org.geomajas.example.gwt.client.samples.controller.CustomControllerSample;
import org.geomajas.example.gwt.client.samples.controller.FallbackControllerSample;
import org.geomajas.example.gwt.client.samples.controller.MouseMoveListenerSample;
import org.geomajas.example.gwt.client.samples.controller.MultipleListenersSample;
import org.geomajas.example.gwt.client.samples.controller.RectangleControllerSample;
import org.geomajas.example.gwt.client.samples.editing.EditLineLayerSample;
import org.geomajas.example.gwt.client.samples.editing.EditMultiLineLayerSample;
import org.geomajas.example.gwt.client.samples.editing.EditMultiPolygonLayerSample;
import org.geomajas.example.gwt.client.samples.editing.EditPointLayerSample;
import org.geomajas.example.gwt.client.samples.editing.EditPolygonLayerSample;
import org.geomajas.example.gwt.client.samples.general.ServerErrorSample;
import org.geomajas.example.gwt.client.samples.i18n.I18nProvider;
import org.geomajas.example.gwt.client.samples.layer.GeoToolsSample;
import org.geomajas.example.gwt.client.samples.layer.GoogleSample;
import org.geomajas.example.gwt.client.samples.layer.OpenStreetMapSample;
import org.geomajas.example.gwt.client.samples.layer.WmsSample;
import org.geomajas.example.gwt.client.samples.layertree.LayerOrderSample;
import org.geomajas.example.gwt.client.samples.layertree.LayertreeSample;
import org.geomajas.example.gwt.client.samples.layertree.LegendSample;
import org.geomajas.example.gwt.client.samples.mapwidget.CrsSample;
import org.geomajas.example.gwt.client.samples.mapwidget.LayerOpacitySample;
import org.geomajas.example.gwt.client.samples.mapwidget.MaxBoundsToggleSample;
import org.geomajas.example.gwt.client.samples.mapwidget.NavigationSample;
import org.geomajas.example.gwt.client.samples.mapwidget.OverviewMapSample;
import org.geomajas.example.gwt.client.samples.mapwidget.PanScaleToggleSample;
import org.geomajas.example.gwt.client.samples.mapwidget.RenderingSample;
import org.geomajas.example.gwt.client.samples.mapwidget.UnitTypesSample;
import org.geomajas.example.gwt.client.samples.mapwidget.WorldScreenSample;
import org.geomajas.example.gwt.client.samples.plugin.DefaultPrintingSample;
import org.geomajas.example.gwt.client.samples.plugin.GeocoderSample;
import org.geomajas.example.gwt.client.samples.security.AttributeSecuritySample;
import org.geomajas.example.gwt.client.samples.security.CommandSecuritySample;
import org.geomajas.example.gwt.client.samples.security.FilterSecuritySample;
import org.geomajas.example.gwt.client.samples.security.LayerSecuritySample;
import org.geomajas.example.gwt.client.samples.security.LoginSample;
import org.geomajas.example.gwt.client.samples.security.ToolSecuritySample;
import org.geomajas.example.gwt.client.samples.toolbar.CustomToolbarSample;
import org.geomajas.example.gwt.client.samples.toolbar.CustomToolbarToolsSample;
import org.geomajas.example.gwt.client.samples.toolbar.ScaleSelectCustomSample;
import org.geomajas.example.gwt.client.samples.toolbar.ScaleSelectDefaultSample;
import org.geomajas.example.gwt.client.samples.toolbar.ToolbarFeatureInfoSample;
import org.geomajas.example.gwt.client.samples.toolbar.ToolbarMeasureSample;
import org.geomajas.example.gwt.client.samples.toolbar.ToolbarNavigationSample;
import org.geomajas.example.gwt.client.samples.toolbar.ToolbarSelectionSample;
import org.geomajas.gwt.client.util.WidgetLayout;

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
				new SampleTreeNode(I18nProvider.getSampleMessages().treeGroupLayers(),
						"[ISOMORPHIC]/geomajas/osgeo/layer.png", "Layers", "topLevel"),
				new SampleTreeNode(I18nProvider.getSampleMessages().osmTitle(),
						"[ISOMORPHIC]/geomajas/osgeo/layer-raster.png", OpenStreetMapSample.OSM_TITLE, "Layers",
						OpenStreetMapSample.FACTORY),
				new SampleTreeNode(I18nProvider.getSampleMessages().googleTitle(),
						"[ISOMORPHIC]/geomajas/osgeo/layer-raster.png", GoogleSample.TITLE, "Layers",
						GoogleSample.FACTORY),
				new SampleTreeNode(I18nProvider.getSampleMessages().wmsTitle(),
						"[ISOMORPHIC]/geomajas/osgeo/layer-wms.png", WmsSample.WMS_TITLE, "Layers", WmsSample.FACTORY),
				new SampleTreeNode(I18nProvider.getSampleMessages().geoTitle(),
						"[ISOMORPHIC]/geomajas/osgeo/layer-vector.png", GeoToolsSample.TITLE, "Layers",
						GeoToolsSample.FACTORY),

				new SampleTreeNode(I18nProvider.getSampleMessages().treeGroupMap(),
						"[ISOMORPHIC]/geomajas/example/images/silk/world.png", "MapWidget", "topLevel"),
				new SampleTreeNode(I18nProvider.getSampleMessages().navigationTitle(),
						"[ISOMORPHIC]/geomajas/example/images/silk/world.png", NavigationSample.TITLE, "MapWidget",
						NavigationSample.FACTORY),
				new SampleTreeNode(I18nProvider.getSampleMessages().crsTitle(),
						"[ISOMORPHIC]/geomajas/example/images/silk/world.png", CrsSample.TITLE, "MapWidget",
						CrsSample.FACTORY),
				new SampleTreeNode(I18nProvider.getSampleMessages().unitTypesTitle(),
						"[ISOMORPHIC]/geomajas/example/images/silk/world.png", UnitTypesSample.TITLE, "MapWidget",
						UnitTypesSample.FACTORY),
				new SampleTreeNode(I18nProvider.getSampleMessages().maxBoundsToggleTitle(),
						"[ISOMORPHIC]/geomajas/example/images/silk/world.png", MaxBoundsToggleSample.TITLE,
						"MapWidget", MaxBoundsToggleSample.FACTORY),
				new SampleTreeNode(I18nProvider.getSampleMessages().panScaleToggleTitle(),
						"[ISOMORPHIC]/geomajas/example/images/silk/world.png", PanScaleToggleSample.TITLE, "MapWidget",
						PanScaleToggleSample.FACTORY),
				new SampleTreeNode(I18nProvider.getSampleMessages().renderingTitle(),
						"[ISOMORPHIC]/geomajas/osgeo/edit.png", RenderingSample.TITLE, "MapWidget",
						RenderingSample.FACTORY),
				new SampleTreeNode(I18nProvider.getSampleMessages().screenWorldTitle(),
						"[ISOMORPHIC]/geomajas/osgeo/edit.png", WorldScreenSample.TITLE, "MapWidget",
						WorldScreenSample.FACTORY),
				new SampleTreeNode(I18nProvider.getSampleMessages().overviewMapTitle(),
						"[ISOMORPHIC]/geomajas/osgeo/region.png", OverviewMapSample.TITLE, "MapWidget",
						OverviewMapSample.FACTORY),
				new SampleTreeNode(I18nProvider.getSampleMessages().layerOpacityTitle(),
						"[ISOMORPHIC]/geomajas/osgeo/layer-raster.png", LayerOpacitySample.TITLE, "MapWidget",
						LayerOpacitySample.FACTORY),

				// Editing:
				new SampleTreeNode(I18nProvider.getSampleMessages().treeGroupEditing(),
						WidgetLayout.iconEdit, "GeoGraphicEditing", "topLevel"),
				new SampleTreeNode(I18nProvider.getSampleMessages().editPointLayerTitle(),
						WidgetLayout.iconEdit, EditPointLayerSample.TITLE, "GeoGraphicEditing",
						EditPointLayerSample.FACTORY),
				new SampleTreeNode(I18nProvider.getSampleMessages().editLineLayerTitle(),
						WidgetLayout.iconEdit, EditLineLayerSample.TITLE, "GeoGraphicEditing",
						EditLineLayerSample.FACTORY),
				new SampleTreeNode(I18nProvider.getSampleMessages().editPolygonLayerTitle(),
						WidgetLayout.iconEdit, EditPolygonLayerSample.TITLE, "GeoGraphicEditing",
						EditPolygonLayerSample.FACTORY),
				// new SampleTreeNode(I18nProvider.getSampleMessages().editMultiPointLayerTitle(),
				// "[ISOMORPHIC]/geomajas/osgeo/edit.png", EditMultiPointLayerSample.TITLE, "GeoGraphicEditing",
				// EditMultiPointLayerSample.FACTORY),
				new SampleTreeNode(I18nProvider.getSampleMessages().editMultiLineLayerTitle(),
						WidgetLayout.iconEdit, EditMultiLineLayerSample.TITLE, "GeoGraphicEditing",
						EditMultiLineLayerSample.FACTORY),
				new SampleTreeNode(I18nProvider.getSampleMessages().editMultiPolygonLayerTitle(),
						WidgetLayout.iconEdit, EditMultiPolygonLayerSample.TITLE, "GeoGraphicEditing",
						EditMultiPolygonLayerSample.FACTORY),

				// LayerTree & Legend samples:
				new SampleTreeNode(I18nProvider.getSampleMessages().treeGroupLayerTree(),
						"[ISOMORPHIC]/geomajas/osgeo/mapset.png", "Layertree", "topLevel"),
				new SampleTreeNode(I18nProvider.getSampleMessages().layertreeTitle(),
						"[ISOMORPHIC]/geomajas/osgeo/mapset.png", LayertreeSample.TITLE, "Layertree",
						LayertreeSample.FACTORY),
				new SampleTreeNode(I18nProvider.getSampleMessages().legendTitle(),
						"[ISOMORPHIC]/geomajas/osgeo/legend-add.png", LegendSample.TITLE, "Layertree",
						LegendSample.FACTORY),
				new SampleTreeNode(I18nProvider.getSampleMessages().layerOrderTitle(),
						"[ISOMORPHIC]/geomajas/osgeo/mapset.png", LayerOrderSample.TITLE, "Layertree",
						LayerOrderSample.FACTORY),

				// Attribute samples:
				new SampleTreeNode(I18nProvider.getSampleMessages().treeGroupAttributes(),
						WidgetLayout.iconTable, "FeatureListGridGroup", "topLevel"),
				new SampleTreeNode(I18nProvider.getSampleMessages().fltTitle(),
						WidgetLayout.iconTable, FeatureListGridSample.TITLE, "FeatureListGridGroup",
						FeatureListGridSample.FACTORY),
				new SampleTreeNode(I18nProvider.getSampleMessages().searchTitle(),
						WidgetLayout.iconTable, SearchSample.TITLE, "FeatureListGridGroup",
						SearchSample.FACTORY),
				new SampleTreeNode(I18nProvider.getSampleMessages().search2Title(),
						WidgetLayout.iconTable, AttributeSearchSample.TITLE, "FeatureListGridGroup",
						AttributeSearchSample.FACTORY),
				new SampleTreeNode(I18nProvider.getSampleMessages().editableGridTitle(),
						WidgetLayout.iconTable, EditableGridSample.TITLE, "FeatureListGridGroup",
						EditableGridSample.FACTORY),
				new SampleTreeNode(I18nProvider.getSampleMessages().editAttributeTitle(),
						WidgetLayout.iconTable, EditAttributeSample.TITLE, "FeatureListGridGroup",
						EditAttributeSample.FACTORY),
				new SampleTreeNode(I18nProvider.getSampleMessages().attributeIncludeInFormTitle(),
						WidgetLayout.iconTable, AttributeIncludeInFormSample.TITLE,
						"FeatureListGridGroup", AttributeIncludeInFormSample.FACTORY),
				new SampleTreeNode(I18nProvider.getSampleMessages().attributeCustomTypeTitle(),
						WidgetLayout.iconTable, AttributeCustomTypeSample.TITLE,
						"FeatureListGridGroup", AttributeCustomTypeSample.FACTORY),
				new SampleTreeNode(I18nProvider.getSampleMessages().attributeCustomFormTitle(),
						WidgetLayout.iconTable, AttributeCustomFormSample.TITLE,
						"FeatureListGridGroup", AttributeCustomFormSample.FACTORY),

				// Map controller:
				new SampleTreeNode(I18nProvider.getSampleMessages().treeGroupMapController(),
						WidgetLayout.iconTools, "MapController", "topLevel"),

				new SampleTreeNode(I18nProvider.getSampleMessages().customControllerTitle(),
						WidgetLayout.iconTools, CustomControllerSample.TITLE, "MapController",
						CustomControllerSample.FACTORY),
				new SampleTreeNode(I18nProvider.getSampleMessages().controllerOnElementTitle(),
						WidgetLayout.iconTools, ControllerOnElementSample.TITLE, "MapController",
						ControllerOnElementSample.FACTORY),
				new SampleTreeNode(I18nProvider.getSampleMessages().rectangleControllerTitle(),
						WidgetLayout.iconTools, RectangleControllerSample.TITLE, "MapController",
						RectangleControllerSample.FACTORY),
				new SampleTreeNode(I18nProvider.getSampleMessages().circleControllerTitle(),
						WidgetLayout.iconTools, CircleControllerSample.TITLE, "MapController",
						CircleControllerSample.FACTORY),
				new SampleTreeNode(I18nProvider.getSampleMessages().fallbackControllerTitle(),
						WidgetLayout.iconTools, FallbackControllerSample.TITLE, "MapController",
						FallbackControllerSample.FACTORY),
				new SampleTreeNode(I18nProvider.getSampleMessages().mouseMoveListenerTitle(),
						"[ISOMORPHIC]/geomajas/silk/monitor.png", MouseMoveListenerSample.TITLE, "MapController",
						MouseMoveListenerSample.FACTORY),
				new SampleTreeNode(I18nProvider.getSampleMessages().multipleListenersTitle(),
						"[ISOMORPHIC]/geomajas/silk/monitor.png", MultipleListenersSample.TITLE, "MapController",
						MultipleListenersSample.FACTORY),

				// Plug-ins
				new SampleTreeNode(I18nProvider.getSampleMessages().treeGroupPlugins(),
						"[ISOMORPHIC]/geomajas/silk/plugin.png", "Plugins", "topLevel"),
				new SampleTreeNode(I18nProvider.getSampleMessages().defaultPrintControllerTitle(),
						"[ISOMORPHIC]/geomajas/osgeo/print.png", DefaultPrintingSample.TITLE, "Plugins",
						DefaultPrintingSample.FACTORY),
				new SampleTreeNode(I18nProvider.getSampleMessages().geocoderTitle(),
						"[ISOMORPHIC]/geomajas/osgeo/show.png", GeocoderSample.TITLE, "Plugins",
						GeocoderSample.FACTORY),

				// Toolbar and controllers:
				new SampleTreeNode(I18nProvider.getSampleMessages().treeGroupToolbarAndControllers(),
						WidgetLayout.iconZoomIn, "ToolbarAndControllers", "topLevel"),

				new SampleTreeNode(I18nProvider.getSampleMessages().toolbarNavigationTitle(),
						WidgetLayout.iconPan, ToolbarNavigationSample.TITLE, "ToolbarAndControllers",
						ToolbarNavigationSample.FACTORY),

				new SampleTreeNode(I18nProvider.getSampleMessages().toolbarSelectionTitle(),
						"[ISOMORPHIC]/geomajas/osgeo/select.png", ToolbarSelectionSample.TITLE,
						"ToolbarAndControllers", ToolbarSelectionSample.FACTORY),

				new SampleTreeNode(I18nProvider.getSampleMessages().toolbarMeasureTitle(),
						"[ISOMORPHIC]/geomajas/osgeo/length-measure.png", ToolbarMeasureSample.TITLE,
						"ToolbarAndControllers", ToolbarMeasureSample.FACTORY),

				new SampleTreeNode(I18nProvider.getSampleMessages().toolbarFeatureInfoTitle(),
						WidgetLayout.iconInfo, ToolbarFeatureInfoSample.TITLE,
						"ToolbarAndControllers", ToolbarFeatureInfoSample.FACTORY),

				new SampleTreeNode(I18nProvider.getSampleMessages().scaleSelectDefaultTitle(),
						WidgetLayout.iconTools, ScaleSelectDefaultSample.TITLE,
						"ToolbarAndControllers", ScaleSelectDefaultSample.FACTORY),

				new SampleTreeNode(I18nProvider.getSampleMessages().scaleSelectCustomTitle(),
						WidgetLayout.iconTools, ScaleSelectCustomSample.TITLE,
						"ToolbarAndControllers", ScaleSelectCustomSample.FACTORY),

				new SampleTreeNode(I18nProvider.getSampleMessages().customToolbarToolsTitle(),
						WidgetLayout.iconTools, CustomToolbarToolsSample.TITLE,
						"ToolbarAndControllers", CustomToolbarToolsSample.FACTORY),

				new SampleTreeNode(I18nProvider.getSampleMessages().customToolbarTitle(),
						WidgetLayout.iconTools, CustomToolbarSample.TITLE, "ToolbarAndControllers",
						CustomToolbarSample.FACTORY),

				// Security samples:
				new SampleTreeNode(I18nProvider.getSampleMessages().treeGroupSecurity(),
						"[ISOMORPHIC]/geomajas/silk/key.png", "Security", "topLevel"),
				new SampleTreeNode(I18nProvider.getSampleMessages().loginTitle(),
						"[ISOMORPHIC]/geomajas/staticsecurity/key_go.png", LoginSample.LOGIN_TITLE, "Security",
						LoginSample.FACTORY),
				new SampleTreeNode(I18nProvider.getSampleMessages().layerSecurityTitle(),
						"[ISOMORPHIC]/geomajas/staticsecurity/key_go.png", LayerSecuritySample.LAYER_SECUTIRY_TITLE,
						"Security", LayerSecuritySample.FACTORY),
				new SampleTreeNode(I18nProvider.getSampleMessages().filterSecurityTitle(),
						"[ISOMORPHIC]/geomajas/staticsecurity/key_go.png", FilterSecuritySample.TITLE, "Security",
						FilterSecuritySample.FACTORY),
				new SampleTreeNode(I18nProvider.getSampleMessages().attributeSecurityTitle(),
						"[ISOMORPHIC]/geomajas/staticsecurity/key_go.png", AttributeSecuritySample.TITLE, "Security",
						AttributeSecuritySample.FACTORY),
				new SampleTreeNode(I18nProvider.getSampleMessages().commandSecurityTitle(),
						"[ISOMORPHIC]/geomajas/staticsecurity/key_go.png", CommandSecuritySample.TITLE, "Security",
						CommandSecuritySample.FACTORY),
				new SampleTreeNode(I18nProvider.getSampleMessages().toolSecurityTitle(),
						"[ISOMORPHIC]/geomajas/staticsecurity/key_go.png", ToolSecuritySample.TITLE, "Security",
						ToolSecuritySample.FACTORY),

				// MapWidget samples:
				new SampleTreeNode(I18nProvider.getSampleMessages().treeGroupGeneral(),
						"[ISOMORPHIC]/geomajas/osgeo/settings.png", "General", "topLevel"),
				new SampleTreeNode(I18nProvider.getSampleMessages().serverErrorTitle(),
						"[ISOMORPHIC]/geomajas/osgeo/help-contents.png", ServerErrorSample.TITLE, "General",
						ServerErrorSample.FACTORY) };
	}
}
