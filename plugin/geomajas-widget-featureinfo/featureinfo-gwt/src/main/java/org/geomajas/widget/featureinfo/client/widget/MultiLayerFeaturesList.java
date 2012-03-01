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

package org.geomajas.widget.featureinfo.client.widget;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.configuration.AttributeInfo;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.layer.Layer;
import org.geomajas.gwt.client.map.layer.RasterLayer;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.featureinfo.client.FeatureInfoMessages;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.AutoFitWidthApproach;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.GroupStartOpen;
import com.smartgwt.client.widgets.grid.HoverCustomizer;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;

/**
 * <p>
 * The <code>MultiLayerFeaturesList</code> is a class providing a floating window that shows a list of all the features
 * (possibly from different layers) provided to it. Clicking on a feature in the list, results in a call of the provided
 * {@link org.geomajas.widget.featureinfo.client.widget.FeatureClickHandler}.
 * </p>
 * 
 * @author An Buyle
 * @author Oliver May
 */
public class MultiLayerFeaturesList extends ListGrid {

	private static final int MAX_ROWS = 25;

	private FeatureInfoMessages messages = GWT.create(FeatureInfoMessages.class);

	private Map<String, VectorLayer> vectorLayers = new HashMap<String, VectorLayer>();

	private Map<String, RasterLayer> rasterLayers = new HashMap<String, RasterLayer>();

	private Map<String, Feature> vectorFeatures = new HashMap<String, Feature>();

	private Map<String, Feature> rasterFeatures = new HashMap<String, Feature>();

	/**
	 * external handler, called when clicking on a feature in the list
	 */
	private FeatureClickHandler featureClickHandler;

	private MapWidget mapWidget;

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------

	/**
	 * Create an instance.
	 * 
	 * @param mapWidget
	 * @param featureClickHandler
	 */
	public MultiLayerFeaturesList(final MapWidget mapWidget, FeatureClickHandler featureClickHandler) {
		super();
		this.mapWidget = mapWidget;
		this.featureClickHandler = featureClickHandler;
		buildWidget();
	}

	/**
	 * Feed a map of features to the widget, so it can be built.
	 * 
	 * @param featureMap
	 */
	public void setFeatures(Map<String, List<org.geomajas.layer.feature.Feature>> featureMap) {
		MapModel mapModel = mapWidget.getMapModel();
		
		for (String clientLayerId : featureMap.keySet()) {
			Layer<?> layer = mapModel.getLayer(clientLayerId);
			if (null != layer) {
				List<org.geomajas.layer.feature.Feature> orgFeatures = featureMap.get(clientLayerId);
				if (!orgFeatures.isEmpty()) {
					if (layer instanceof VectorLayer) {
						VectorLayer vLayer = (VectorLayer) layer;
						vectorLayers.put(layer.getId(), (VectorLayer) layer);
						for (org.geomajas.layer.feature.Feature featDTO : orgFeatures) {
							Feature feat = new Feature(featDTO, vLayer);
							vLayer.getFeatureStore().addFeature(feat);
							vectorFeatures.put(getFullFeatureId(feat, vLayer), feat);
							addFeature(feat, vLayer);
						}

					} else if (layer instanceof RasterLayer) {
						RasterLayer rLayer = (RasterLayer) layer;
						rasterLayers.put(layer.getId(), (RasterLayer) layer);
						for (org.geomajas.layer.feature.Feature featDTO : orgFeatures) {
							Feature feat = new Feature(featDTO, null);
							rasterFeatures.put(getFullFeatureId(feat, layer), feat);
							addRasterFeature(feat, (RasterLayer) layer);
						}
					}
				}
			}
		}
	}

	@Override
	/* Override getCellCSSText to implement padding-left of ordinary feature rows */
	protected String getCellCSSText(ListGridRecord record, int rowNum, int colNum) {
		// Note: using listGrid.setCellPadding() would also padd group rows
		String newStyle;
		String style = record.getCustomStyle(); /* returns groupNode if group row, else e.g. null */

		if ("label".equals(getFieldName(colNum)) && (null == style || !"groupNode".equalsIgnoreCase(style))) {
			newStyle = "padding-left: 40px;";
		} else { /* groupCell */
			newStyle = "padding-left: 5px;";
		}
		if (null != super.getCellCSSText(record, rowNum, colNum)) {
			newStyle = super.getCellCSSText(record, rowNum, colNum) + newStyle; /*
																				 * add padding after original, the
																				 * latter specified wins.
																				 */
		}
		return newStyle;
	}

	/**
	 * Adds a new feature to the grid list. A {@link VectorLayer} must have been set first, and the feature must belong
	 * to that VectorLayer.
	 * 
	 * @param feature
	 *            The feature to be added to the grid list.
	 * @return Returns true in case of success, and false if the feature is null or if the feature does not belong to
	 *         the correct layer or if the layer has not yet been set.
	 */
	private boolean addFeature(Feature feature, VectorLayer layer) {
		// Basic checks:
		if (feature == null || layer == null || !feature.getLayer().getId().equals(layer.getId())) {
			return false;
		}

		// Feature checks out, add it to the grid:
		ListGridRecord record = new ListGridRecord();
		record.setAttribute("label", feature.getLabel());
		record.setAttribute("featureId", getFullFeatureId(feature, layer));
		record.setAttribute("layerId", layer.getId());
		record.setAttribute("layerLabel", layer.getLabel());

		addData(record);
		return true;
	}
	
	private boolean addRasterFeature(Feature feat, RasterLayer layer) {
		if (feat == null) {
			return false;
		}
		
		// Feature checks out, add it to the grid:
		ListGridRecord record = new ListGridRecord();
		record.setAttribute("label", feat.getId());
		record.setAttribute("featureId", getFullFeatureId(feat, layer));
		record.setAttribute("layerId", layer.getId());
		record.setAttribute("layerLabel", layer.getLabel());

		addData(record);
		
		return true;
	}

	private static String getFullFeatureId(Feature feature, Layer layer) {
		return layer.getId() + "." + feature.getId();
	}

	/**
	 * Build the entire widget.
	 * 
	 */
	private void buildWidget() {
//		setTitle(messages.nearbyFeaturesListTooltip());
		setShowEmptyMessage(true);
		setWidth100();
		setHeight100();
		setShowHeader(false);
		setShowAllRecords(true);

		// List size calculation
		setDefaultWidth(400);
		setDefaultHeight(300);
		setAutoFitData(Autofit.BOTH);
		setAutoFitMaxRecords(MAX_ROWS);
		setAutoFitFieldWidths(true);
		setAutoFitWidthApproach(AutoFitWidthApproach.BOTH);

		ListGridField labelField = new ListGridField("label");
		ListGridField featIdField = new ListGridField("featureId");
		ListGridField layerField = new ListGridField("layerId");
		ListGridField layerLabelField = new ListGridField("layerLabel");

		setGroupByField("layerLabel");

		setGroupStartOpen(GroupStartOpen.ALL);
		setFields(/* dummyIndentField, */labelField, layerField, featIdField, layerLabelField);
		hideField("layerId");
		hideField("featureId");
		hideField("layerLabel");
		addRecordClickHandler(new RecordClickHandler() {

			public void onRecordClick(RecordClickEvent event) {
				String featureId = event.getRecord().getAttribute("featureId");
				String layerId = event.getRecord().getAttribute("layerId");
				Feature feat = vectorFeatures.get(featureId);

				if (feat != null) {
					featureClickHandler.onClick(feat, feat.getLayer());
				} else {
					Layer rasterLayer = rasterLayers.get(layerId);
					feat = rasterFeatures.get(featureId);
					featureClickHandler.onClick(feat, rasterLayer);
				}
			}

		});
		
		setShowHover(true);
		setCanHover(true);
		setHoverWidth(200);
		setHoverCustomizer(new HoverCustomizer() {
			
			public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
				String featureId = record.getAttribute("featureId");
				Feature feat = vectorFeatures.get(featureId);
				
				String tooltip = "";
				
				if (feat != null) {
					for (AttributeInfo a : feat.getLayer().getLayerInfo().getFeatureInfo().getAttributes()) {
						if (a.isIdentifying()) {
							tooltip += "<b>" + a.getLabel() + "</b>: " + feat.getAttributeValue(a.getName()) + "<br/>";
						}
					}
					tooltip += messages.nearbyFeaturesListTooltip();
				}
				return tooltip;
			}
		});

	}
}
