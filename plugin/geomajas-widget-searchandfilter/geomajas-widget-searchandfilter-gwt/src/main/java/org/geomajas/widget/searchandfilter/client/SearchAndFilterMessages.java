/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
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

	String exportToCsvWindowTitle();
	String exportToCsvContentReady();
	String exportToCsvDownloadLink(String url);
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

	String geometricSearchWidgetSelectionSearchTitle();
	String geometricSearchWidgetSelectionSearchZoomToSelection();
	String geometricSearchWidgetSelectionSearchAddSelection();
	String geometricSearchWidgetSelectionSearchNothingSelected();

	String geometricSearchWidgetFreeDrawingSearchTitle();
	String geometricSearchWidgetFreeDrawingPoint();
	String geometricSearchWidgetFreeDrawingLine();
	String geometricSearchWidgetFreeDrawingPolygon();
	String geometricSearchWidgetFreeDrawingAdd();

}
