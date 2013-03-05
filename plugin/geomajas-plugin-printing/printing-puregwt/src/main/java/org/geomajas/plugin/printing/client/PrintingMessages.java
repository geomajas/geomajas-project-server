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
package org.geomajas.plugin.printing.client;

import com.google.gwt.i18n.client.Messages;

/**
 * Message bundle for printing plugin.
 * 
 * @author An Buyle
 * @author Jan De Moerloose
 * 
 */
public interface PrintingMessages extends Messages {

	String printPrefsTitleLabel();
	String printPrefsTitleTooltip();
	String printButtonTitle();
	
	String printPrefsChoose();
	
	String printPrefsPrint();
	
	String printPrefsSize();
	
	String printPrefsOrientation();
	
	String printPrefsPortrait();
	
	String printPrefsLandscape();

	String printPrefsWithArrow();
	
	String printPrefsWithScaleBar();
	
	String printPrefsRasterDPI();
	
	String printPrefsStatus();
	
	String printPrefsDownloadType();
	
	String printPrefsSaveAsFile();

	String printPrefsOpenInBrowserWindow();

	String printPrefsFileName();

	
	String printPrefsTitlePlaceholder();
	String defaultPrintTitle();

}
