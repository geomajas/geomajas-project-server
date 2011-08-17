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

package org.geomajas.widget.advancedviews.client;

import com.google.gwt.i18n.client.Messages;


/**
 * Messages for the Advanced Views widgets.
 *
 * @author Kristof Heirwegh
 */
public interface AdvancedViewsMessages extends Messages {

	String layerTreeWithLegendLayerActionsToolTip();

	String layerActionsWindowTitle();
	String layerActionsOpacity();
	String layerActionsOpacitySliderLabelWidth();
	String layerActionsLabels();
	String layerActionsLabelsToolTip();
	String layerActionsShowLegend();
	String layerActionsRemoveFilter();
	
	String layerInfoWindowTitle();
	String layerInfoLayerInfo();
	String layerInfoLayerInfoValue();
	
	String layerInfoLayerInfoFldLayer();
	String layerInfoLayerInfoFldLayerType();
	String layerInfoLayerInfoFldMaxViewScale();
	String layerInfoLayerInfoFldMinViewScale();
	String layerInfoLayerInfoFldVisible();
	String layerInfoLayerInfoFldVisibleStatusVisible();
	String layerInfoLayerInfoFldVisibleStatusHidden();
	String layerInfoLayerInfoFldLayerTypeRaster();
	String layerInfoLayerInfoFldLayerTypeVector();
	String layerInfoLayerInfoAttAttribute(); 
	String layerInfoLayerInfoAttLabel(); 
	String layerInfoLayerInfoAttType(); 
	String layerInfoLayerInfoAttEditable(); 
	String layerInfoLayerInfoAttIdentifying(); 
	String layerInfoLayerInfoAttHidden(); 
	String layerInfoLayerInfoAttNumeric(); 
	String layerInfoLayerInfoAttYes(); 
	String layerInfoLayerInfoAttNo(); 
	String layerInfoLayerInfoLegend();
	
	String expandingThemeWidgetNoThemeSelected();
	
}
