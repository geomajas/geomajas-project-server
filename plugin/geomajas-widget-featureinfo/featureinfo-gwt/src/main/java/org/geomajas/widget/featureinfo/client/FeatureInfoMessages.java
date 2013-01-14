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

package org.geomajas.widget.featureinfo.client;

import com.google.gwt.i18n.client.Messages;


/**
 * Messages for the FeatureInfo widgets.
 *
 * @author An Buyle
 */
public interface FeatureInfoMessages extends Messages {

	String nearbyFeaturesModalActionTitle();
	String nearbyFeaturesModalActionTooltip();
	String nearbyFeaturesWindowTitle();
	String nearbyFeaturesListSectionTitle();
	String nearbyFeaturesDetailsSectionTitle();
	String nearbyFeaturesListTooltip();
	
	String showCoordinatesActionTitle();
	String showCoordinatesActionTooltip();
	String showCoordinatesViewX();
	String showCoordinatesViewY();
	String showCoordinatesWorldX();
	String showCoordinatesWorldY();

	String tooltipOnMouseoverActionTitle();
	String tooltipOnMouseoverActionTooltip();
	String tooltipOnMouseoverNoResult();
	
	String combinedFeatureInfoActionTitle();
	String combinedFeatureInfoActionTooltip();
	
	String urlFeatureDetailWidgetBuilderNoValue();
	
	String multiLayerFeatureInfoNoResult();
	String multiLayerFeatureInfoLayerNotFound();
}
