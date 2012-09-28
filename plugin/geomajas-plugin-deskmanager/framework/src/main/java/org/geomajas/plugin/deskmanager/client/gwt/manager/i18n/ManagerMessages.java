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
 * @author BuyleA
 *
 */
public interface ManagerMessages extends Messages {
	
	String editButtonText();
	String cancelButtonText();
	String saveButtonText();
	String resetButtonText();
	String resetButtonTooltip();
	String configAddDelete(); //TODO: needed?

	
	String removeTitle();
	
	String wizardVorigeButtonText();
	String wizardVolgendeButtonText();
	
	String formWarnNotvalid();
	String loadingConfig();
	String titlePleaseWait();
	
	String gridColumnActions();

	String settingsLatestChangeBy();
	String settingsLatestChangeWhen();

	
	// Configuration of Themes
	String themeConfigThemeTitle();
	String themeConfigThemeTitleText();

	String themeConfigThemeMouseOver();
	String themeConfigThemeMouseOverText();  //TODO: needed?

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
	String mainTabBlueprints();

	//Geodesks main Tab
	String newGeodeskButtonText();
	
	String geodeskGridColumnName();
	String geodeskGridColumnNameBlueprint();
	String geodeskGridColumnDeskId();
	String geodeskGridColumnAuthor();
	String geodeskGridColumnAuthorTooltip();
	String geodeskGridColumnPublic();
	String geodeskPublicTooltip();
	String geodeskGridColumnActiv();
	String geodeskActivTooltip();
	String geodeskGridColumnActions();
	String geodeskGridColumnActionsTooltip();
	String geodeskGridActionsColumnPreviewTooltip();
	String geodeskGridActionsColumnRemoveTooltip();

	String geodeskLabel();

	String geodeskRemoveTitle();
	String geodeskRemoveConfirmQuestion(String attribute);

	String geodeskLoading();


	String settingsNameGeodesk();

	String geodeskSettingsNameBlueprint();

	String geodeskSettingsId();

	String settingsNameBlueprintTooltip();

	String settingGeodeskWarnGeodeskIdNotUnique();

	String validatorWarnGeodeskIdNotUnique();
		
	String geodeskSettingsIdTooltip();

	String geodeskSettingsAdmin();

	String geodeskSettingsActiv();

	String geodeskSettingsPublic();
	String geodeskSettingsWarnCannotBePublic();
	String geodeskSettingsWarnGeodeskInactivedByBlueprint();
	
	String settingsLimitToTerritoryAdministrator();
	String settingsLimitToTerritoryAdministratorTooltip();
	String settingsLimitToTerritoryUser();
	String settingsLimitToTerritoryUserTooltip();
	
	String settingsFormGroupSettings();
	String geodeskSettingsWarnNoBlueprints();
	String datalagenGroup();
	String layerTreeFormGroup();
	String geodeskAccessRightsFormGroup();
	String geodeskNotificationsFormGroup();
	String geodeskNotificationsWarnInvalidList();
	String geodeskNotificationsWarnNoChanges();

	String geodeskDetailTabSettings();
	String geodeskDetailTabDataLayers();
	String geodeskDetailTabLayerTree();
	String geodeskDetailTabAccessRights();
	String geodeskDetailTabNotifications();
	String geodeskDetailTabLayout();

	String geodeskDetailTabHTMLCode();

	String geodeskDetailTabThemes();

	
	String chooseBlueprintTitle();
	String chooseBlueprintRequired();
	String chooseBlueprintLoading();
	String chooseBlueprintTooltip();
	String chooseBlueprintCreate();
	String chooseBlueprinWarnNoBlueprints();

	String newBlueprintButtonText();
	String chooseAppTitle();

	String datalayerConnectionParametersGroup();
	String datalayerConnectionParametersCapabilitiesURL();
	String datalayerConnectionParametersUserName();
	String datalayerConnectionParametersPassword();
	String datalayerConnectionParametersHost();
	String datalayerConnectionParametersPort();
	String datalayerConnectionParametersScheme();
	String datalayerConnectionParametersDatabase();

	String blueprintGridColumnName();
	String blueprintGridColumnLimitToTerritory();
	String blueprintGridColumnPublic();
	String blueprintAttributePublicTooltip();
	String blueprintGridColumnActiv();
	String blueprintActivTooltip();
	String blueprintAttributeGeodesksActiv();
	String blueprintAttributeGeodesksActivTooltip();
	String blueprintGridColumnActions();
	String bleuprintGridColumnActionsTooltip();
	String blueprintGridActionsColumnRemoveTooltip();
	String blueprintRemoveConfirmQuestion(String attribute);
	String blueprintsLoading();

	String blueprintDetailTabSettings();
	String blueprintDetailTabDataLayers();
	String blueprintDetailTabLayerTree();
	String blueprintDetailTabAccessRights();
	String blueprintDetailTabLayout();
	String blueprintDetailTabThemes();
	String blueprintDetailLoadingConfig();

	// Blueprint settings form in Blueprint Settings Tab of Blueprint detail panel
	String blueprintSettingsNameBlueprint();
	String blueprintSettingsClientApplicationName();
	String blueprintSettingsActiv();
	String blueprintSettingsActivTooltip();
	String blueprintSettingsPublic();
	String blueprintSettingsWarnCannotBePublic();
	
	String chooseAppRequired();
	String chooseAppTooltip();
	String chooseAppCreate();

	String datalayerDetailTabSettings();
	String datalayerDetailTabUpload();
	String datalayerDetailTabConnectionparameters();
	String datalayerDetailTabNotifications();
	String datalayerDetailTabStyle();

	String datalayerGridColumnLayerName();
	String datalayerGridColumnGroup();
	String datalayerGridColumnLayerType();
	String datalayerGridColumnPublic();
	String datalayerGridColumnPublicTooltip();
	String datalayerGridColumnActive();
	String datalayerGridColumnActiveTooltip();
	String datalayerGridColumnActions();
	
	String datalayerGridActionsColumnRemoveTooltip();
	String datalayerGridWarnPublicCannotBeRemoved();
	String datalayerGridControlOnLayerUseBeforeRemove();
	String datalayerGridCannotRemoveLayerInUse();
	String datalayerGridRemoveConfirmQuestion(String name);
	String datalayerGridLoading();
	
	String datalayerNotificationsFormGroup();	
	String datalayerNotificationsWarnInvalidFormData();
	
	String datalayerNotificationsNoFormChanges();
	String datalayersNewDatalayerButtonText();
	String datalayersNewLayerIsBeingSaved();
	String datalayerShapeUploadFormGroup();
	String datalayerShapeUploadOverwriteConfirmTitle();
	String datalayerShapeUploadOverwriteConfirmQuestion();
	String datalayerShapeUploadNoFileSelected();
	String datalayerStyleFormGroup();
	
	
	String newLayerModelWindowTitle();
	String newLayerModelWizardCreateButtonText();
	String newLayerModelWizardWarnNoPanel();

	String uploadShapefileLabelText();
	String uploadShapefileUploadingFile();
	String uploadShapefileResponseInvalidFile();
	String uploadShapefileResponseNoRights();
	String uploadShapefileResponseOK();
	String uploadShapefileResponseOkButWrong(String result);
	String uploadShapefileResponseDeafultNOK(String result);
	String uploadShapefileResponseInvalidLayer();

	String layerConfigurationConfigureLayer();
	String layerConfigurationLayerProperties();
	String layerConfigurationName();
	String layerConfigurationNameTooltip();
	String layerConfigurationPublicLayer();
	String layerConfigurationLayerVisibleByDefault();
	String layerConfigurationLayerVisibleByDefaultTooltip();
	String layerConfigurationMinimumScale();
	String layerConfigurationMinimumScaleTooltip();
	String layerConfigurationMaximumScale();
	String layerConfigurationMaximumScaleTooltip();
	String layerConfigConfirmRestoreText();
	String layerConfigConfirmRestoreTitle();

	
	String layerListGridColumnPublicTooltip();
	String layerListGridConfigurate();
	
	String layerGroupConfigWindowTitle();
	String layerGroupConfigGroupTitleProperties();
	String layerGroupConfigName();
	String layerGroupConfigNameTooltip();
	String layerGroupConfigOpenedAtStart();
	String layerGroupConfigOpenedAtStartTooltip();
	String layerSelectPanelHelpText();
	String layerSelectAvailableLayers();
	String layerSelectSelectedLayers();
	String layerSelectSelectedLayersTooltip();
	String layerTreegridCreateMap();
	String layerTreegridRemoveMap();
	String layerTreegridColumnPublic();
	String layerTreegridColumnPublicTooltip();
	String layerTreegridRemoveMapErrorNoSelection();
	String layerTreegridCreateMapAskValue();
	
	String mailGridInvalidAddress();
	String mailGridColumnName();
	String mailGridColumnAddress();
	String mailGridActionDeleteTooltip();
	String mailGridConfirmDeleteTitle();
	String mailGridConfirmDeleteText(String attribute);
	String mailManageNewButtonText();
	
	String layerAttributesGridLoadingText();
	String layerAttributesGridColumnAttribute();
	String layerAttributesGridColumnType();
	String layerAttributesGridColumnCoreInfo();
	String layerAttributesGridColumnCoreInfoTooltip();
	String layerAttributesGridColumnIdField();
	String layerAttributesGridColumnIdFieldTooltip();
	String layerAttributesGridColumnLabelField();
	String layerAttributesGridColumnLabelFieldTooltip();
	String layerAttributesGridColumnName();
	String layerAttributesGridColumnNameTooltip();
	String layerAttributesGriDeselectIdAttribute();
	String layerAttributesGriDeselectLabelAttribute();

}
