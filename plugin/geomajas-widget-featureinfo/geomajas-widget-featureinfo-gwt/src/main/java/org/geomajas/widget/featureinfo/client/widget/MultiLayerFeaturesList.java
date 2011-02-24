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

package org.geomajas.widget.featureinfo.client.widget;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.featureinfo.client.FeatureInfoMessages;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.AutoFitWidthApproach;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.GroupStartOpen;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;

/**
 * <p>
 * The <code>NearbyFeaturesList</code> is a class providing a floating window that shows a list of all the features
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

	private Map<String/* layerId */, VectorLayer> vectorLayers = new HashMap<String, VectorLayer>();

	private Map<String/* serverLayerId+"."+featID */, Feature> features = new HashMap<String, Feature>();

	private FeatureClickHandler featureClickHandler; /*
													 * external handler, called when clicking on a feature in the list
													 */

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

		this.featureClickHandler = featureClickHandler;
		buildWidget();
	}

	/**
	 * Feed a map of features to the widget, so it can be built.
	 * 
	 * @param featureMap
	 */
	public void setFeatures(MapWidget mapWidget, Map<String, List<org.geomajas.layer.feature.Feature>> featureMap) {

		MapModel mapModel = mapWidget.getMapModel();
		for (String serverLayerId : featureMap.keySet()) {
			List<VectorLayer> layers = mapModel.getVectorLayersByServerId(serverLayerId); /*
																						 * ??there can be more than one
																						 * Client VectorLayer for the
																						 * same serverLayerId
																						 */
			for (VectorLayer vectorLayer : layers) {

				List<org.geomajas.layer.feature.Feature> orgFeatures = featureMap.get(serverLayerId);
				if (!orgFeatures.isEmpty()) {
					vectorLayers.put(vectorLayer.getId(), vectorLayer);
				}
				for (org.geomajas.layer.feature.Feature featDTO : orgFeatures) {
					Feature feat = new Feature(featDTO, vectorLayer);
					vectorLayer.getFeatureStore().addFeature(feat);
					features.put(getFullFeatureId(feat, vectorLayer), feat);
					addFeature(feat, vectorLayer);
				}
			}
		}
	}

	@Override
	/* Override getCellCSSText to implement padding-left of ordinary feature rows */
	protected String getCellCSSText(ListGridRecord record, int rowNum, int colNum) {
		// Note: using listGrid.setCellPadding() also pads group rows
		String newStyle;
		String style = record.getCustomStyle(); /* returns groupNode if group row, else e.g. null */

		if ("label".equals(getFieldName(colNum)) && (null == style || !style.equalsIgnoreCase("groupNode"))) {
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

	private static String getFullFeatureId(Feature feature, VectorLayer layer) {
		return layer.getServerLayerId() + "." + feature.getId();
	}

	/**
	 * Build the entire widget.
	 * 
	 */
	private void buildWidget() {
		setTitle(messages.nearbyFeaturesListTooltip());
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
				Feature feat = features.get(featureId);
				featureClickHandler.onClick(feat);
			}

		});

	}
}
