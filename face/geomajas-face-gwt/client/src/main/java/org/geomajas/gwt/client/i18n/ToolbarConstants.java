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

package org.geomajas.gwt.client.i18n;

import com.google.gwt.i18n.client.Constants;

/**
 * <p>
 * Localization constants for all ToolbarActions and ToolbarSelectActions.
 * </p>
 *
 * @author Pieter De Graef
 */
public interface ToolbarConstants extends Constants {

	String editingSelect();

	String measureSelect();

	String selectionSelect();

	String zoomIn();

	String zoomOut();

	String pan();
	
	String panToSelection();

	String zoomToRectangle();

	String zoomToSelection();
	
	String zoomNext();

	String zoomPrevious();

	String scaleSelect();

	String refreshConfiguration();

	String featureInfo();

}
