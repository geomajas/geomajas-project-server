/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.deskmanager.client.gwt.manager.i18n;

import com.google.gwt.i18n.client.Messages;

/**
 * @author Oliver May
 *
 */
public interface ManagerMessages extends Messages {
	// Configuration of Themes
	String themeConfigThemeTitle();

	String themeConfigThemeMouseOver();

	String themeConfigThemeImage();

	String themeConfigThemeTurnsOtherLayersOff();

	String themeConfigThemeGridNameField();

	String themeConfigThemeConfigGroup();

	String themeConfigBreadcrumbThemeConfig();

	String themeConfigBreadcrumbViewConfig();

	String themeConfigBreadcrumbRangeConfig();

	String themeConfigBreadcrumbLayerConfig();

	String themeConfigViewRemove();

	String themeConfigViewRemoveConfirm();
	
	String themeConfigViewAdd();

	String themeConfigViewDefaultNewTitle();

	String themeConfigViewName();

	String themeConfigViewDescription();

	String themeConfigViewConfigGroup();

	String themeConfigViewGridMinScaleField();

	String themeConfigViewGridMaxScaleField();

	String themeConfigRangeRemove(); 

	String themeConfigRangeRemoveConfirm();

	String themeConfigRangeTitle();

	String themeConfigRangeMinScale();

	String themeConfigRangeMaxScale();

	String themeConfigLayerGridNameField();

	String themeConfigLayerGridVisibleField();

	String themeConfigLayerGridOpacityField();

	String themeConfigLayerRemove();

	String themeConfigLayerAdd();

	String themeConfigLayerRemoveConfirm();

	String themeConfigLayerTitle();

	String themeConfigLayerVisibility();

	String themeConfigLayerVisibilityOn();

	String themeConfigLayerVisibilityOff();
	
	String themeConfigLayerOpacity();

	String themeConfigRangeAddInline();

	String themeConfigRangeAdd();

	String themeConfigLayerSelect();

	// Main section tabs
	String mainTabGeodesks();
	String mainTabDataLayers();
	String mainTabBluePrints();

	//Geodesks main Tab
	String newGeodeskButtonText();
	
	String geodeskTableColumnName();
	String geodeskTableColumnNameBlueprint();
	String geodeskTableColumnDeskId();
	String geodeskTableColumnAuthor();
	String geodeskTableColumnAuthorTooltip();
	String geodeskTableColumnPublic();
	String geodeskPublicTooltip();
	String geodeskTableColumnActiv();
	String geodeskActivTooltip();
	String geodeskTableColumnActions();
	String geodeskTableColumnActionsTooltip();
	String geodeskTableActionsColumnPreviewTooltip();
	String geodeskTableActionsColumnRemoveTooltip();

	String geodeskLabel();

	String geodeskRemoveTitle();
	String geodeskRemoveConfirmQuestion(String attribute);

	String geodeskLoading();


	String settingsNameGeodesk();

	String settingsNameBlueprint();

	String settingsGeodeskId();

	String settingsNameBlueprintTooltip();

	String warnGeodeskIdNotUnique();

	String validatorWarnGeodeskIdNotUnique();

	String settingsGeodeskIdTooltip();

	String settingsGeodeskAdmin();

	String settingsGeodeskLatestChangeBy();

	String settingsGeodeskLatestChangeWhen();

	String settingsGeodeskActiv();

	String settingsGeodeskPublic();
	String warnGeodeskCannotBePublic();

	String settingsGeodeskLimitToTerritoryAdministrator();
	String settingsGeodeskLimitToTerritoryAdministratorTooltip();
	String settingsGeodeskLimitToTerritoryUser();
	String settingsGeodeskLimitToTerritoryUserTooltip();

	String datalagenGroup();
	String layerTreeFormGroup();
	String geodeskAccessRightsFormGroup();

}
