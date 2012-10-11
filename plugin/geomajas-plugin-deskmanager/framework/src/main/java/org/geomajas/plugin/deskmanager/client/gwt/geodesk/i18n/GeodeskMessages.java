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
package org.geomajas.plugin.deskmanager.client.gwt.geodesk.i18n;

import com.google.gwt.i18n.client.Messages;

/**
 * Interface for i18n messages.
 * 
 * @author Jan De Moerloose
 *
 */
public interface GeodeskMessages extends Messages {

	String viewFeatureDetailButtonTitle();

	String zoomFeatureButtonTitle();

	String zoomFeatureButtonTooltip();

	String searchFavoriteTitle();

	String searchFavoriteTooltip();

	String searchCombinedTitle();

	String searchCombinedTooltip();

	String searchSpatialTitle();

	String searchSpatialTooltip();

	String searchFreeTitle();

	String searchFreeTooltip();

	String loadingScreenMessage();
	
	String userFriendlyLayerErrorMessage();
	
	String userFriendlySecurityErrorMessage();
	
	String userFriendlyCommunicationErrorMessage();

	String noGeodeskIdGivenError();
}
