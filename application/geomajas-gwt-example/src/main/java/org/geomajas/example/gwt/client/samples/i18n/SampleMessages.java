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

package org.geomajas.example.gwt.client.samples.i18n;

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

	String sampleTitle(String version);

	String generalSourceButton();

	String generalSourceTitle();

	String generalJavaSource();

	String generalFile();

	String generalDescription();

	String treeGroupLayers();

	String treeGroupMap();

	String treeGroupSecurity();

	String treeGroupEditing();

	String treeGroupOverviewMap();

	String treeGroupMapController();

	String treeGroupToolbarAndControllers();

	String treeGroupAttributes();

	String introductionTitle();

	// OpenStreetMap sample:

	String osmTitle();

	String osmDescription();

	// WMS sample:

	String wmsTitle();

	String wmsDescription();

	// GeoTools sample:

	String geoTitle();

	String geoDescription();

	// Google sample:

	String googleTitle();

	String googleDescription();

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

	// Editing a point layer:

	String editPointLayerTitle();

	String editPointLayerDescription();

	// Editing a line layer:

	String editLineLayerTitle();

	String editLineLayerDescription();

	// Editing a polygon layer:

	String editPolygonLayerTitle();

	String editPolygonLayerDescription();

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

	// Login sample:

	String loginTitle();

	String loginDescription();

	String loginSuccess(String userToken);

	String loginFailure();

	String logoutSuccess();

	String logoutFailure();

	// LayerSecurity sample:

	String layerSecurityTitle();

	String layerSecurityDescription();

	String securityLogInWith(String name);

	// FeatureSecurity sample:

	String featureSecurityTitle();

	String featureSecurityDescription();

	// FilterSecurity sample:

	String filterSecurityTitle();

	String filterSecurityDescription();

	// CommandSecurity sample:

	String commandSecurityTitle();

	String commandSecurityDescription();

	// ToolSecurity sample:

	String toolSecurityTitle();

	String toolSecurityDescription();

	// AttributeSecurity sample:

	String attributeSecurityTitle();

	String attributeSecurityDescription();

	String attributeSecurityButtonTitle();

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
}