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

package org.geomajas.gwt.example.client;

import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.gwt.example.base.SampleTreeNode;
import org.geomajas.gwt.example.base.SampleTreeNodeRegistry;
import org.geomajas.gwt.example.client.sample.attribute.AttributeCustomFormSample;
import org.geomajas.gwt.example.client.sample.attribute.AttributeCustomTypeSample;
import org.geomajas.gwt.example.client.sample.attribute.AttributeIncludeInFormSample;
import org.geomajas.gwt.example.client.sample.attribute.AttributeSearchSample;
import org.geomajas.gwt.example.client.sample.attribute.EditAttributeSample;
import org.geomajas.gwt.example.client.sample.attribute.EditableGridSample;
import org.geomajas.gwt.example.client.sample.attribute.FeatureListGridSample;
import org.geomajas.gwt.example.client.sample.attribute.SearchSample;
import org.geomajas.gwt.example.client.sample.controller.CircleControllerSample;
import org.geomajas.gwt.example.client.sample.controller.ControllerOnElementSample;
import org.geomajas.gwt.example.client.sample.controller.CustomControllerSample;
import org.geomajas.gwt.example.client.sample.controller.FallbackControllerSample;
import org.geomajas.gwt.example.client.sample.controller.MouseMoveListenerSample;
import org.geomajas.gwt.example.client.sample.controller.MultipleListenersSample;
import org.geomajas.gwt.example.client.sample.controller.RectangleControllerSample;
import org.geomajas.gwt.example.client.sample.editing.EditLineLayerSample;
import org.geomajas.gwt.example.client.sample.editing.EditMultiLineLayerSample;
import org.geomajas.gwt.example.client.sample.editing.EditMultiPolygonLayerSample;
import org.geomajas.gwt.example.client.sample.editing.EditPointLayerSample;
import org.geomajas.gwt.example.client.sample.editing.EditPolygonLayerSample;
import org.geomajas.gwt.example.client.sample.general.PipelineConfigSample;
import org.geomajas.gwt.example.client.sample.general.ServerErrorSample;
import org.geomajas.gwt.example.client.sample.i18n.SampleMessages;
import org.geomajas.gwt.example.client.sample.layer.OpenStreetMapSample;
import org.geomajas.gwt.example.client.sample.layertree.LayerOrderSample;
import org.geomajas.gwt.example.client.sample.layertree.LayertreeSample;
import org.geomajas.gwt.example.client.sample.layertree.LegendSample;
import org.geomajas.gwt.example.client.sample.mapwidget.CrsSample;
import org.geomajas.gwt.example.client.sample.mapwidget.GroupAndSingleAddonSample;
import org.geomajas.gwt.example.client.sample.mapwidget.LayerOpacitySample;
import org.geomajas.gwt.example.client.sample.mapwidget.MaxBoundsToggleSample;
import org.geomajas.gwt.example.client.sample.mapwidget.NavigationSample;
import org.geomajas.gwt.example.client.sample.mapwidget.OverviewMapSample;
import org.geomajas.gwt.example.client.sample.mapwidget.PanAndZoomSliderSample;
import org.geomajas.gwt.example.client.sample.mapwidget.PanScaleToggleSample;
import org.geomajas.gwt.example.client.sample.mapwidget.RenderingSample;
import org.geomajas.gwt.example.client.sample.mapwidget.UnitTypesSample;
import org.geomajas.gwt.example.client.sample.mapwidget.WorldScreenSample;
import org.geomajas.gwt.example.client.sample.toolbar.CustomToolbarSample;
import org.geomajas.gwt.example.client.sample.toolbar.CustomToolbarToolsSample;
import org.geomajas.gwt.example.client.sample.toolbar.ScaleSelectCustomSample;
import org.geomajas.gwt.example.client.sample.toolbar.ScaleSelectDefaultSample;
import org.geomajas.gwt.example.client.sample.toolbar.ToolbarFeatureInfoSample;
import org.geomajas.gwt.example.client.sample.toolbar.ToolbarMeasureSample;
import org.geomajas.gwt.example.client.sample.toolbar.ToolbarNavigationSample;
import org.geomajas.gwt.example.client.sample.toolbar.ToolbarSelectionSample;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;

/**
 * <p>
 * The GWT test case sample application. Here here!
 * </p>
 *
 * @author Pieter De Graef
 */
public class GwtFaceExample implements EntryPoint {

	private static final SampleMessages MESSAGES = GWT.create(SampleMessages.class);

	public void onModuleLoad() {

		// MapWidget samples:
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.treeGroupLayers(),
				"[ISOMORPHIC]/geomajas/osgeo/layer.png", "Layers", "topLevel"));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.osmTitle(),
				"[ISOMORPHIC]/geomajas/osgeo/layer-raster.png", OpenStreetMapSample.OSM_TITLE, "Layers",
				OpenStreetMapSample.FACTORY));

		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.treeGroupMap(),
				"[ISOMORPHIC]/geomajas/example/image/silk/world.png", "MapWidget", "topLevel"));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.navigationTitle(),
						"[ISOMORPHIC]/geomajas/example/image/silk/world.png", NavigationSample.TITLE, "MapWidget",
						NavigationSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.crsTitle(),
				"[ISOMORPHIC]/geomajas/example/image/silk/world.png", CrsSample.TITLE, "MapWidget",
				CrsSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.unitTypesTitle(),
				"[ISOMORPHIC]/geomajas/example/image/silk/world.png", UnitTypesSample.TITLE, "MapWidget",
				UnitTypesSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.maxBoundsToggleTitle(),
				"[ISOMORPHIC]/geomajas/example/image/silk/world.png", MaxBoundsToggleSample.TITLE,
				"MapWidget", MaxBoundsToggleSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.panScaleToggleTitle(),
				"[ISOMORPHIC]/geomajas/example/image/silk/world.png", PanScaleToggleSample.TITLE, "MapWidget",
				PanScaleToggleSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.groupAndSingleTitle(),
				"[ISOMORPHIC]/geomajas/example/image/silk/world.png", GroupAndSingleAddonSample.TITLE, "MapWidget",
				GroupAndSingleAddonSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.panAndSliderTitle(),
				"[ISOMORPHIC]/geomajas/example/image/silk/world.png", PanAndZoomSliderSample.TITLE, "MapWidget",
				PanAndZoomSliderSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.renderingTitle(),
				"[ISOMORPHIC]/geomajas/osgeo/edit.png", RenderingSample.TITLE, "MapWidget",
				RenderingSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.screenWorldTitle(),
				"[ISOMORPHIC]/geomajas/osgeo/edit.png", WorldScreenSample.TITLE, "MapWidget",
				WorldScreenSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.overviewMapTitle(),
				"[ISOMORPHIC]/geomajas/osgeo/region.png", OverviewMapSample.TITLE, "MapWidget",
				OverviewMapSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.layerOpacityTitle(),
				"[ISOMORPHIC]/geomajas/osgeo/layer-raster.png", LayerOpacitySample.TITLE, "MapWidget",
				LayerOpacitySample.FACTORY));

		// Editing:
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.treeGroupEditing(),
				WidgetLayout.iconEdit, "GeoGraphicEditing", "topLevel"));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.editPointLayerTitle(),
				WidgetLayout.iconEdit, EditPointLayerSample.TITLE, "GeoGraphicEditing",
				EditPointLayerSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.editLineLayerTitle(),
				WidgetLayout.iconEdit, EditLineLayerSample.TITLE, "GeoGraphicEditing",
				EditLineLayerSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.editPolygonLayerTitle(),
				WidgetLayout.iconEdit, EditPolygonLayerSample.TITLE, "GeoGraphicEditing",
				EditPolygonLayerSample.FACTORY));
		// SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.editMultiPointLayerTitle(),
		// "[ISOMORPHIC]/geomajas/osgeo/edit.png", EditMultiPointLayerSample.TITLE, "GeoGraphicEditing",
		// EditMultiPointLayerSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.editMultiLineLayerTitle(),
				WidgetLayout.iconEdit, EditMultiLineLayerSample.TITLE, "GeoGraphicEditing",
				EditMultiLineLayerSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.editMultiPolygonLayerTitle(),
				WidgetLayout.iconEdit, EditMultiPolygonLayerSample.TITLE, "GeoGraphicEditing",
				EditMultiPolygonLayerSample.FACTORY));

		// LayerTree & Legend samples:
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.treeGroupLayerTree(),
				"[ISOMORPHIC]/geomajas/osgeo/mapset.png", "Layertree", "topLevel"));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.layertreeTitle(),
				"[ISOMORPHIC]/geomajas/osgeo/mapset.png", LayertreeSample.TITLE, "Layertree",
				LayertreeSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.legendTitle(),
				"[ISOMORPHIC]/geomajas/osgeo/legend-add.png", LegendSample.TITLE, "Layertree",
				LegendSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.layerOrderTitle(),
				"[ISOMORPHIC]/geomajas/osgeo/mapset.png", LayerOrderSample.TITLE, "Layertree",
				LayerOrderSample.FACTORY));

		// Attribute samples:
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.treeGroupAttributes(),
				WidgetLayout.iconTable, "FeatureListGridGroup", "topLevel"));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.fltTitle(),
				WidgetLayout.iconTable, FeatureListGridSample.TITLE, "FeatureListGridGroup",
				FeatureListGridSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.searchTitle(),
				WidgetLayout.iconTable, SearchSample.TITLE, "FeatureListGridGroup",
				SearchSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.search2Title(),
				WidgetLayout.iconTable, AttributeSearchSample.TITLE, "FeatureListGridGroup",
				AttributeSearchSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.editableGridTitle(),
				WidgetLayout.iconTable, EditableGridSample.TITLE, "FeatureListGridGroup",
				EditableGridSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.editAttributeTitle(),
				WidgetLayout.iconTable, EditAttributeSample.TITLE, "FeatureListGridGroup",
				EditAttributeSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.attributeIncludeInFormTitle(),
				WidgetLayout.iconTable, AttributeIncludeInFormSample.TITLE,
				"FeatureListGridGroup", AttributeIncludeInFormSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.attributeCustomTypeTitle(),
				WidgetLayout.iconTable, AttributeCustomTypeSample.TITLE,
				"FeatureListGridGroup", AttributeCustomTypeSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.attributeCustomFormTitle(),
				WidgetLayout.iconTable, AttributeCustomFormSample.TITLE,
				"FeatureListGridGroup", AttributeCustomFormSample.FACTORY));

		// Map controller:
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.treeGroupMapController(),
				WidgetLayout.iconTools, "MapController", "topLevel"));

		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.customControllerTitle(),
				WidgetLayout.iconTools, CustomControllerSample.TITLE, "MapController",
				CustomControllerSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.controllerOnElementTitle(),
				WidgetLayout.iconTools, ControllerOnElementSample.TITLE, "MapController",
				ControllerOnElementSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.rectangleControllerTitle(),
				WidgetLayout.iconTools, RectangleControllerSample.TITLE, "MapController",
				RectangleControllerSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.circleControllerTitle(),
				WidgetLayout.iconTools, CircleControllerSample.TITLE, "MapController",
				CircleControllerSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.fallbackControllerTitle(),
				WidgetLayout.iconTools, FallbackControllerSample.TITLE, "MapController",
				FallbackControllerSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.mouseMoveListenerTitle(),
				"[ISOMORPHIC]/geomajas/silk/monitor.png", MouseMoveListenerSample.TITLE, "MapController",
				MouseMoveListenerSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.multipleListenersTitle(),
				"[ISOMORPHIC]/geomajas/silk/monitor.png", MultipleListenersSample.TITLE, "MapController",
				MultipleListenersSample.FACTORY));

		// Toolbar and controllers:
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.treeGroupToolbarAndControllers(),
				WidgetLayout.iconZoomIn, "ToolbarAndControllers", "topLevel"));

		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.toolbarNavigationTitle(),
				WidgetLayout.iconPan, ToolbarNavigationSample.TITLE, "ToolbarAndControllers",
				ToolbarNavigationSample.FACTORY));

		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.toolbarSelectionTitle(),
				"[ISOMORPHIC]/geomajas/osgeo/select.png", ToolbarSelectionSample.TITLE,
				"ToolbarAndControllers", ToolbarSelectionSample.FACTORY));

		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.toolbarMeasureTitle(),
				"[ISOMORPHIC]/geomajas/osgeo/length-measure.png", ToolbarMeasureSample.TITLE,
				"ToolbarAndControllers", ToolbarMeasureSample.FACTORY));

		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.toolbarFeatureInfoTitle(),
				WidgetLayout.iconInfo, ToolbarFeatureInfoSample.TITLE,
				"ToolbarAndControllers", ToolbarFeatureInfoSample.FACTORY));

		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.scaleSelectDefaultTitle(),
				WidgetLayout.iconTools, ScaleSelectDefaultSample.TITLE,
				"ToolbarAndControllers", ScaleSelectDefaultSample.FACTORY));

		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.scaleSelectCustomTitle(),
				WidgetLayout.iconTools, ScaleSelectCustomSample.TITLE,
				"ToolbarAndControllers", ScaleSelectCustomSample.FACTORY));

		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.customToolbarToolsTitle(),
				WidgetLayout.iconTools, CustomToolbarToolsSample.TITLE,
				"ToolbarAndControllers", CustomToolbarToolsSample.FACTORY));

		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.customToolbarTitle(),
				WidgetLayout.iconTools, CustomToolbarSample.TITLE, "ToolbarAndControllers",
				CustomToolbarSample.FACTORY));

		// General samples:
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.treeGroupGeneral(),
				"[ISOMORPHIC]/geomajas/osgeo/settings.png", "General", "topLevel"));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.serverErrorTitle(),
				"[ISOMORPHIC]/geomajas/osgeo/help-contents.png", ServerErrorSample.TITLE, "General",
				ServerErrorSample.FACTORY));
		SampleTreeNodeRegistry.addSampleTreeNode(new SampleTreeNode(MESSAGES.pipelineConfigTitle(),
				"[ISOMORPHIC]/geomajas/osgeo/help-contents.png", PipelineConfigSample.TITLE, "General",
				PipelineConfigSample.FACTORY));
	}

}
