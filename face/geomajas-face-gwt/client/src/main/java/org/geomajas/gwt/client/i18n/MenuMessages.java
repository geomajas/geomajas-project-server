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

package org.geomajas.gwt.client.i18n;

import com.google.gwt.i18n.client.Messages;

/**
 * <p>
 * Localization constants for all menu actions.
 * </p>
 * 
 * @author Pieter De Graef
 */
public interface MenuMessages extends Messages {

	// Constants for menu actions regarding editing:

	String cancelEditing();

	String saveEditing();

	String insertPoint();

	String insertRing();

	String newFeature();

	String editFeature();

	String deleteFeature();

	String removePoint();

	String removeRing();

	String toggleEditMode();

	String toggleGeometricInfo();

	String undoOperation();

	// Constants for menu actions regarding measuring distances:

	String cancelMeasuring();

	String toggleMeasureSnapping();

	String getMeasureDistanceString(String distance, String radius);

	String distance();

	// Constants for menu actions regarding measuring distances:

	String deselectAll();

	String toggleSelection();

	String editAttributes();
	
	// Constants for menu actions regarding editing
	
	String editGeometricInfoLabelTitle();
	
	String editGeometricInfoLabelNoGeometry();
	
	String editGeometricInfoLabelInfo(String area, String length, int points);
}
