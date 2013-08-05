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

package org.geomajas.smartgwt.example.client.sample.i18n;

import com.google.gwt.i18n.client.Messages;

/**
 * <p>
 * Localization messages for the GWT test samples.
 * </p>
 * 
 * @author Pieter De Graef
 */
public interface SampleMessages extends Messages {

	// General messages:

	String treeGroupLayers();

	String treeGroupMap();

	String treeGroupEditing();

	String treeGroupMapController();

	String treeGroupToolbarAndControllers();

	String treeGroupAttributes();

	String treeGroupGeneral();

	String serverErrorTitle();

	String serverErrorDescription();

	String serverErrorButton();

	String pipelineConfigTitle();

	String pipelineConfigDescription();

	// OpenStreetMap sample:

	String osmTitle();

	String osmDescription();

	// WMS sample:

	String wmsTitle();

	String wmsDescription();

	// GeoTools sample:

	String geotoolsTitle();

	String geotoolsDescription();

	// Navigation sample:

	String navigationTitle();

	String navigationDescription();

	String navigationBtnZoomIn();

	String navigationBtnZoomOut();

	String navigationBtnPosition();

	String navigationBtnTranslate();

	String navigationBtnBbox();

	// CRS sample:

	String crsTitle();

	String crsDescription();

	// Unit types sample:

	String unitTypesTitle();

	String unitTypesDescription();

	String switchUnitTypes();

	String unitTypeEnglish();

	String unitTypeMetric();

	// Toggle maxbounds sample

	String maxBoundsToggleTitle();

	String maxBoundsToggleDescription();

	String toggleMaxBoundsBelgium();

	String toggleMaxBoundsWorld();

	// Toggle pan buttons and scalebar example

	String panScaleToggleTitle();

	String panScaleToggleDescription();

	String togglePanButtons();

	String toggleScaleBar();
	
	// Custom map addon examples
	
	String groupAndSingleTitle();
	
	String groupAndSingleDescription();
	
	String panAndSliderTitle();
	
	String panAndSliderDescription();

	// Rendering Sample:

	String renderingTitle();

	String renderingDescription();

	String renderingDrawCircle();

	String renderingDrawLineString();

	String renderingDrawPolygon();

	String renderingDrawText();

	String renderingDrawRectangle();

	String renderingDrawImage();

	String renderingTransform();

	String renderingNewCursor();

	String renderingDelete();

	// Screen versus World space

	String screenWorldTitle();

	String screenWorldDescription();

	String screenWorldBTNScreen();

	String screenWorldBTNWorld();

	String screenWorldBTNScreenDelete();

	String screenWorldBTNWorldDelete();

	// Change Layer opacity:

	String layerOpacityTitle();

	String layerOpacityDescription();

	// Editing a point layer:

	String editPointLayerTitle();

	String editPointLayerDescription();

	// Editing a multi-point layer:

	String editMultiPointLayerTitle();

	String editMultiPointLayerDescription();

	// Editing a line layer:

	String editLineLayerTitle();

	String editLineLayerDescription();

	// Editing a MultiLineString layer:

	String editMultiLineLayerTitle();

	String editMultiLineLayerDescription();

	// Editing a polygon layer:

	String editPolygonLayerTitle();

	String editPolygonLayerDescription();

	// Editing a MultiPolygon layer:

	String editMultiPolygonLayerTitle();

	String editMultiPolygonLayerDescription();

	// OverviewMap sample:

	String overviewMapTitle();

	String overviewMapDescription();

	String overviewMapToggleRectStyle();

	String overviewMapToggleExtentStyle();

	String overviewMapToggleExtent();

	// Custom Controller sample:

	String customControllerTitle();

	String customControllerDescription();

	String customControllerScreenCoordinates();

	String customControllerWorldCoordinates();

	// Controller On Element sample:
	String controllerOnElementTitle();

	String controllerOnElementDescription();

	// Rectange controller sample
	String rectangleControllerTitle();

	String rectangleControllerDescription();

	String rectangeControllerOutput(double roundedKmWidth, double roundedKmHeight, double roundedArea);

	// Circle controller sample:

	String circleControllerTitle();

	String circleControllerDescription();

	String circleControllerOutput(String area);

	// Fallback controller sample:

	String fallbackControllerTitle();

	String fallbackControllerDescription();

	String fallbackControllerExplanation();

	// MouseMoveListener sample:

	String mouseMoveListenerTitle();

	String mouseMoveListenerDescription();

	// MultipleListeners sample:

	String multipleListenersTitle();

	String multipleListenersDescription();

	String multipleListenersBtn();

	String multipleListenersPortletTitle();

	String multipleListenersPortletText();

	String multipleListenersCount(int count);

	// ToolbarNavigation Sample:

	String toolbarNavigationTitle();

	String toolbarNavigationDescription();

	// ToolbarSelection Sample:

	String toolbarSelectionTitle();

	String toolbarSelectionDescription();

	// ToolbarMeasure Sample:

	String toolbarMeasureTitle();

	String toolbarMeasureDescription();

	// ToolbarFeatureInfo Sample:

	String toolbarFeatureInfoTitle();

	String toolbarFeatureInfoDescription();

	// ScaleSelect with default zoomlevels

	String scaleSelectDefaultTitle();

	String scaleSelectDefaultDescription();

	// ScaleSelect with custom zoomlevels

	String scaleSelectCustomTitle();

	String scaleSelectCustomDescription();

	// Custom toolbartools sample
	String customToolbarToolsTitle();

	String customToolbarToolsDescription();

	String customToolbarToolsTooltip();

	// Custom toolbar sample

	String customToolbarTitle();

	String customToolbarDescription();

	// Layer tree
	String treeGroupLayerTree();

	// Layer tree sample

	String layertreeTitle();

	String layertreeDescription();

	// Legend sample:

	String legendTitle();

	String legendDescription();

	// Layer order sample:

	String layerOrderTitle();

	String layerOrderDescription();

	String layerOrderUpBtn();

	String layerOrderDownBtn();

	String layerOrderTxt();

	// FeatureListGrid sample:

	String fltTitle();

	String fltDescription();

	// FeatureSearch sample:

	String searchTitle();

	String searchDescription();

	// FeatureSearch - attribute test:

	String search2Title();

	String search2Description();

	String search2InnerTitle();

	// Editable grid

	String editableGridTitle();
	
	String editableGridDescription();

	// Edit Attribute sample:

	String editAttributeTitle();

	String editAttributeDescription();

	// Attributes IncludeInForm

	String attributeIncludeInFormTitle();

	String attributeIncludeInFormDescription();

	// Attributes CustomType

	String attributeCustomTypeTitle();

	String attributeCustomTypeDescription();

	// Attributes Custom Form

	String attributeCustomFormTitle();

	String attributeCustomFormDescription();

}