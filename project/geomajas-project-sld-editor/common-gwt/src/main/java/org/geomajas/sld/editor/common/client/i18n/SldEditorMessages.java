/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.sld.editor.client.i18n;

import com.google.gwt.i18n.client.Messages;

/**
 * <p>
 * Localization constants for the GWT SldEditor.
 * </p>
 * 
 * @author An Buyle
 */
public interface SldEditorMessages extends Messages {

	// String activeLayer(String layerName);

	String externalGraphicsSelectTitle();

	String markerSelectTitle();

	String confirmSavingChangesBeforeUnloadingSld();

	String confirmSavingChangesBeforeUnloadingIncompleteSld();

	String layerTitle();

	String geometryTitle();

	String styleTitle();

	String hRefTitle();

	String generalFormTitle();

	String ruleDetailContainerTitle();

	String confirmDeleteOfStyle(String styleName);

	String symbologyTabTitle();

	String filterTabTitle();

	String typeofGraphicsTitleInSymbologyTab();

	String nameOfSymbolTitleInSymbologyTab();

	String formatTitleInPointInSymbologyTab();

	String enableFillInSymbologyTab();

	String enableBorderInSymbologyTab();

	String opacityTitleInSymbologyTab();

	String opacityTooltipInSymbologyTab();

	String fillColorInSymbologyTab();

	String borderColor();

	String borderWidthTitle();

	String borderWidthTooltip();

	String strokeColorTitle();

	String sizeOfGraphicInSymbologyTab();

	String rotationOfGraphicInSymbologyTab();

	String rotationOfGraphicTooltipInSymbologyTab();

	String triangleTitle();

	String nameUnspecified();

	// Filter editor messages
	String warnMessageUnsupportedOperator(String operator);

	String likeFilterSpecTemplate(String wildCard, String singleChar, String escape);

	// Rule overview pane
	String ruleOverviewGroupTitle();

	String ruleTitleFieldTitle();

	String closeButtonTitle();

	String closeButtonTooltip();

	String cancelButtonTitle();

	String saveButtonTitle();

	String saveButtonTooltip();

	String listingOfSldsTitle();

	String listingOfSldsTooltip();

	String addSldButtonTooltip();

	String removeSldButtonTooltip();

	String ruleTitleUnspecified();

	String ruleTitleDefault();

	String pointTitle();

	String lineTitle();

	String polygonTitle();

	String emptyRuleList();

	String createButtonTitle();

	String createSldCancelButtonTitle();

	String createButtonTooltip();

	String nameSldCanNotBeEmpty();

	String nameSld();

	String unsupportedEmpty();

	String unsupportedMultipleNamedLayer();

	String unsupportedUserLayer();

	String unsupportedEmptyNamedLayer();

	String unsupportedMultipleNamedStyle();

	String unsupportedNamedStyle();

	String unsupportedNoFeatureTypeStyle();

	String unsupportedMultipleFeatureTypeStyle();

	String noSldMessage();

	String unsupportedFilter();

}
