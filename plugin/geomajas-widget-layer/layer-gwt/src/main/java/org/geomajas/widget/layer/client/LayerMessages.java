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
package org.geomajas.widget.layer.client;

import com.google.gwt.i18n.client.Messages;


/**
 * @author Oliver May
 *
 */
public interface LayerMessages extends Messages {
	String layerTreeWithLegendLayerActionsToolTip();

	String layerActionsWindowTitle();
	String layerActionsOpacity();
	String layerActionsOpacitySliderLabelWidth();
	String layerActionsLabels();
	String layerActionsLabelsToolTip();
	String layerActionsShowLegend();
	String layerActionsShowLegendAndFields();
	String layerActionsRemoveFilter();
	String layerActionsShowLayer();
	String layerActionsShowLayerToolTip();

	String layerInfoLayerInfo();
	String layerInfoLayerActions();
	String layerInfoLayerInfoValue();
	String layerInfoLayerInfoSource();
	String layerInfoLayerInfoDate();

	String layerInfoWindowLegendTitle();

	String layerInfoLayerInfoFldInfo();
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
	String layerInfoLayerInfoLegendNoLegend();
}
