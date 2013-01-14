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

package org.geomajas.widget.searchandfilter.client;

import com.google.gwt.i18n.client.Messages;


/**
 * Messages for the Search and filter widgets.
 *
 * @author Kristof Heirwegh
 */
public interface SearchAndFilterMessages extends Messages {

	String multiFeatureListGridNoData();
	String multiFeatureListGridButtonFocusSelection();
	String multiFeatureListGridButtonShowDetail();
	String multiFeatureListGridButtonExportToCSV();
	String multiFeatureListGridButtonFocusSelectionTooltip();
	String multiFeatureListGridButtonShowDetailTooltip();
	String multiFeatureListGridButtonExportToCSVTooltip();
	String multiFeatureListGridMaxSizeExceededMessage();

	String exportToCsvWindowTitle();
	String exportToCsvContentReady();
	String exportToCsvDownloadLinkDescription();
	String exportToCsvDownloadLinkName();
	String exportToCsvSeparatorChar();
	String exportToCsvQuoteChar();
	String exportToCsvEncoding();
	/**
	 * Used for date/times and decimal numbers.
	 * @return
	 */
	String exportToCsvLocale();

	String geometricSearchWidgetTitle();
	String geometricSearchWidgetStartSearch();
	String geometricSearchWidgetReset();
	String geometricSearchWidgetNoGeometry();
	String geometricSearchWidgetBufferLabel();
	String geometricSearchWidgetNoResult();
	String geometricSearchWidgetNoLayers();

	String geometricSearchWidgetSelectionSearchTitle();
	String geometricSearchWidgetSelectionSearchZoomToSelection();
	String geometricSearchWidgetSelectionSearchAddSelection();
	String geometricSearchWidgetSelectionSearchNothingSelected();
	String geometricSearchWidgetSelectionSearchNothingSelectedTooltip();
	
	String geometricSearchWidgetFreeDrawingSearchTitle();
	String geometricSearchWidgetFreeDrawingPoint();
	String geometricSearchWidgetFreeDrawingLine();
	String geometricSearchWidgetFreeDrawingPolygon();
	String geometricSearchWidgetFreeDrawingAdd();
	String geometricSearchWidgetFreeDrawingInvalidGeometry();
	String geometricSearchWidgetFreeDrawingNothingDrawn();
	String geometricSearchWidgetFreeDrawingUndo();
	String geometricSearchWidgetFreeDrawingRedo();

	String attributeSearchWidgetTitle();
	String attributeSearchWidgetNoLayerSelected();
	String attributeSearchWidgetNoValidCriterionUnsupportedType();
	String attributeSearchWidgetNoValidCriterionNoLayer();
	String attributeSearchWidgetNoValidCriterionNoCriteria();

	String searchWidgetSearch();
	String searchWidgetReset();
	String searchWidgetSave();
	String searchWidgetCancel();
	String searchWidgetAddToFavourites();
	String searchWidgetFilterLayer();
	String searchWidgetRemoveFilter();

	String combinedSearchWidgetTitle();
	String combinedSearchWidgetType();
	String combinedSearchWidgetAdd();
	String combinedSearchWidgetRemove();
	String combinedSearchWidgetAnd();
	String combinedSearchWidgetOr();
	String combinedSearchWidgetSelectSearch();
	String combinedSearchWidgetNoValue();

	String searchControllerSearchingTitle();
	String searchControllerSearchingMessage();
	String searchControllerFailureMessage();

	String favouritesControllerAddTitle();
	String favouritesControllerAddName();
	String favouritesControllerAddNameTooltip();
	String favouritesControllerAddShared();
	String favouritesControllerAddSharedTooltip();
	String favouritesControllerAddAdd();
	String favouritesControllerAddCancel();
	String favouritesControllerAddCrudError();
	String favouritesControllerAddGroupTitle();

	String searchFavouritesListWidgetTitle();
	String searchFavouritesListWidgetFavourites();
	String searchFavouritesListWidgetNoSelection();
	String searchFavouritesListWidgetGroupTitle();
	String searchFavouritesListWidgetLastChange();
	String searchFavouritesListWidgetLastChangeBy();
	String searchFavouritesListWidgetSave();
	String searchFavouritesListWidgetCancel();
	String searchFavouritesListWidgetDelete();
	String searchFavouritesListWidgetDeleteMessage();
	String searchFavouritesListWidgetEditFilter();
	String searchFavouritesListWidgetEditFilterTooltip();
	String searchFavouritesListWidgetSearchWindowNotFound();
	String searchFavouritesListWidgetFilter();

	String searchWidgetRegistryCriterionTypeAnd();
	String searchWidgetRegistryCriterionTypeOr();
	String searchWidgetRegistryCriterionTypeGeometry();
	String searchWidgetRegistryCriterionTypeAttribute();
}
