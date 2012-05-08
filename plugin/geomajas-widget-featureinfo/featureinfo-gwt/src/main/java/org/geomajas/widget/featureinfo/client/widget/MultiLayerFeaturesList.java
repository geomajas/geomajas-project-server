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
import java.util.Map.Entry;

import org.geomajas.configuration.AbstractAttributeInfo;
import org.geomajas.configuration.AbstractReadOnlyAttributeInfo;
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
 * The <code>MultiLayerFeaturesList</code> is a class providing a floating
 * window that shows a list of all the features (possibly from different layers)
 * provided to it. Clicking on a feature in the list, results in a call of the
 * provided
 * {@link org.geomajas.widget.featureinfo.client.widget.FeatureClickHandler}.
 * </p>
 * 
 * @author An Buyle
 * @author Oliver May
 * @author Wout Swartenbroekx
 */
public class MultiLayerFeaturesList extends ListGrid {

	private static final int MAX_ROWS = 25;

	private static final FeatureInfoMessages MESSAGES = GWT.create(FeatureInfoMessages.class);

	private final Map<String, RasterLayer> rasterLayers = new HashMap<String, RasterLayer>();

	private final Map<String, Feature> vectorFeatures = new HashMap<String, Feature>();

	private final Map<String, Feature> rasterFeatures = new HashMap<String, Feature>();

	/**
	 * external handler, called when clicking on a feature in the list
	 */
	private final FeatureClickHandler featureClickHandler;

	private final MapWidget mapWidget;

	private Map<String, String> featuresListLabels;
	
	private static final String LABEL = "LABEL";
	private static final String FEATURE_ID = "FEATURE_ID";
	private static final String LAYER_ID = "LAYER_ID";
	private static final String LAYER_LABEL = "LAYER_LABEL";
	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------

	/**
	 * Create an instance.
	 * 
	 * @param mapWidget map widget
	 * @param featureClickHandler handler
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
	 * @param featureMap feature map
	 */
	public void setFeatures(Map<String, List<org.geomajas.layer.feature.Feature>> featureMap) {
		MapModel mapModel = mapWidget.getMapModel();

		for (Entry<String, List<org.geomajas.layer.feature.Feature>> clientLayerId : featureMap.entrySet()) {
			Layer<?> layer = mapModel.getLayer(clientLayerId.getKey());
			if (null != layer) {
				List<org.geomajas.layer.feature.Feature> orgFeatures = clientLayerId.getValue();
				if (!orgFeatures.isEmpty()) {
					addFeatures(layer, orgFeatures);
				}
			}
		}
	}

	private void addFeatures(Layer<?> layer, List<org.geomajas.layer.feature.Feature> orgFeatures) {
		if (layer instanceof VectorLayer) {
			VectorLayer vLayer = (VectorLayer) layer;
			for (org.geomajas.layer.feature.Feature featDTO : orgFeatures) {
				Feature feat = new Feature(featDTO, vLayer);
				vLayer.getFeatureStore().addFeature(feat);
				vectorFeatures.put(getFullFeatureId(feat, vLayer), feat);
				addFeature(feat, vLayer);
			}
		} else if (layer instanceof RasterLayer) {
			RasterLayer rLayer = (RasterLayer) layer;
			rasterLayers.put(layer.getId(), rLayer);
			for (org.geomajas.layer.feature.Feature featDTO : orgFeatures) {
				Feature feat = new Feature(featDTO, null);
				rasterFeatures.put(getFullFeatureId(feat, rLayer), feat);
				addFeature(feat, rLayer);
			}
		}
	}

	@Override
	/* Override getCellCSSText to implement padding-left of ordinary feature rows */
	protected String getCellCSSText(ListGridRecord record, int rowNum, int colNum) {
		// Note: using listGrid.setCellPadding() would also padd group rows
		String newStyle;
		String style = record.getCustomStyle(); /* returns groupNode if group row, else e.g. null */

		if (LABEL.equals(getFieldName(colNum)) && (null == style || !"groupNode".equalsIgnoreCase(style))) {
			newStyle = "padding-left: 40px;";
		} else { /* groupCell */
			newStyle = "padding-left: 5px;";
		}
		if (null != super.getCellCSSText(record, rowNum, colNum)) { 
			newStyle = super.getCellCSSText(record, rowNum, colNum) + newStyle; 
			/* add padding after original, the latter specified wins. */
		}
		return newStyle;
	}

	/**
	 * Adds a new feature to the grid list. A {@link VectorLayer} must have been
	 * set first, and the feature must belong to that VectorLayer.
	 * 
	 * @param feature
	 *            The feature to be added to the grid list.
	 * @return Returns true in case of success, and false if the feature is null
	 *         or if the feature does not belong to the correct layer or if the
	 *         layer has not yet been set.
	 */
	private boolean addFeature(Feature feature, Layer<?> layer) {
		// Basic checks:
		if (feature == null || layer == null || !feature.getLayer().getId().equals(layer.getId())) {
			return false;
		}
		// Feature checks out, add it to the grid:
		ListGridRecord record = new ListGridRecord();
		if (layer instanceof VectorLayer) {
			record.setAttribute(LABEL, getLabel(feature));
		} else if (layer instanceof RasterLayer) {
			record.setAttribute(LABEL, feature.getId());
		}
		record.setAttribute(FEATURE_ID, getFullFeatureId(feature, layer));
		record.setAttribute(LAYER_ID, layer.getId());
		record.setAttribute(LAYER_LABEL, layer.getLabel());
		addData(record);
		return true;
	}

	private String getLabel(Feature f) {
		if (null != featuresListLabels) {
			for (Map.Entry<String, String> entry : featuresListLabels.entrySet()) {
				if (f.getLayer().getId().equalsIgnoreCase(entry.getKey())) {
					return (String) f.getAttributeValue(entry.getValue());
				}
			}
		}
		return f.getLabel();
	}

	private static String getFullFeatureId(Feature feature, Layer<?> layer) {
		return layer.getId() + "." + feature.getId();
	}

	/**
	 * Build the entire widget.
	 */
	private void buildWidget() {
		// setTitle(MESSAGES.nearbyFeaturesListTooltip());
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

		ListGridField labelField = new ListGridField(LABEL);
		ListGridField featIdField = new ListGridField(FEATURE_ID);
		ListGridField layerField = new ListGridField(LAYER_ID);
		ListGridField layerLabelField = new ListGridField(LAYER_LABEL);

		setGroupByField(LAYER_LABEL);

		setGroupStartOpen(GroupStartOpen.ALL);
		setFields(/* dummyIndentField, */labelField, layerField, featIdField, layerLabelField);
		hideField(LAYER_ID);
		hideField(FEATURE_ID);
		hideField(LAYER_LABEL);
		addRecordClickHandler(new RecordClickHandler() {

			public void onRecordClick(RecordClickEvent event) {
				String fId = event.getRecord().getAttribute(FEATURE_ID);
				String lId = event.getRecord().getAttribute(LAYER_ID);
				Feature feat = vectorFeatures.get(fId);

				if (feat != null) {
					featureClickHandler.onClick(feat, feat.getLayer());
				} else {
					Layer<?> rasterLayer = rasterLayers.get(lId);
					feat = rasterFeatures.get(fId);
					featureClickHandler.onClick(feat, rasterLayer);
				}
			}

		});

		setShowHover(true);
		setCanHover(true);
		setHoverWidth(200);
		setHoverCustomizer(new HoverCustomizer() {

			public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
				String fId = record.getAttribute(FEATURE_ID);
				Feature feat = vectorFeatures.get(fId);

				StringBuilder tooltip = new StringBuilder();

				if (feat != null) {
					for (AbstractAttributeInfo a : feat.getLayer().getLayerInfo().getFeatureInfo().getAttributes()) {
						if (a instanceof AbstractReadOnlyAttributeInfo &&
								((AbstractReadOnlyAttributeInfo) a).isIdentifying()) {
							tooltip.append("<b>");
							tooltip.append(((AbstractReadOnlyAttributeInfo) a).getLabel());
							tooltip.append("</b>: ");
							tooltip.append(feat.getAttributeValue(a.getName()));
							tooltip.append("<br/>");
						}
					}
					tooltip.append(MESSAGES.nearbyFeaturesListTooltip());
				}
				return tooltip.toString();
			}
		});
	}

	/**
	 * @param featuresListLabels
	 *            the featuresListLabels to set
	 */
	public void setFeaturesListLabels(Map<String, String> featuresListLabels) {
		this.featuresListLabels = featuresListLabels;
	}
}
