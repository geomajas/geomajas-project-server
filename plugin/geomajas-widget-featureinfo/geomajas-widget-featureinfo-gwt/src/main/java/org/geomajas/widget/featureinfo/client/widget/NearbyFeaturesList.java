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

import org.geomajas.global.Api;
import org.geomajas.gwt.client.map.MapModel;
import org.geomajas.gwt.client.map.feature.Feature;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.featureinfo.client.FeatureInfoMessages;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.types.GroupStartOpen;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;

/**
 * <p>
 * The <code>NearbyFeaturesList</code> is a class providing a floating window that shows a list of 
 * all the features (possibly from different layers) provided to it. 
 * Clicking on a feature in the list, results in a call of the provided 
 * {@link org.geomajas.widget.featureinfo.client.widget.FeatClickHandler}.
 * </p>
 *
 * @author An Buyle
 * @since 1.8.0
 */
@Api
public class NearbyFeaturesList {

	private FeatureInfoMessages messages = GWT.create(FeatureInfoMessages.class);
	
	private boolean initialized; /* = false */

	/**
	 * Reference to the MapModel. Needed when we want to create a
	 * {@link org.geomajas.gwt.client.map.feature.FeatureTransaction} to actually save changes.
	 */
	private MapModel mapModel;

	private ListGrid listGrid;

	private Map<String/* layerId */, VectorLayer> vectorLayers = new HashMap<String, VectorLayer>();

	private Map<String/* serverLayerId+"."+featID */, Feature> features = new HashMap<String, Feature>();

	private FeatClickHandler featClickHandler; /* external handler, called when clicking on a feature in the list */

	// -------------------------------------------------------------------------
	// Constructor:
	// -------------------------------------------------------------------------

	/**
	 * Create an instance.
	 *
	 * @param mapWidget
	 * @param featureMap
	 * 					The features to be listed, grouped per serverLayerId
	 * @param featClickHandler
	 * @return
	 * @since 1.8.0
	 */
	@Api
	public NearbyFeaturesList(final MapWidget mapWidget,
			Map<String, List<org.geomajas.layer.feature.Feature>> featureMap,
			FeatClickHandler featClickHandler) {

		this.mapModel = mapWidget.getMapModel();
		this.featClickHandler = featClickHandler;
		createListGrid();
		buildWidget(featureMap);
		// this.markForRedraw();
	}

	// -------------------------------------------------------------------------
	// Public methods:
	// -------------------------------------------------------------------------

	/**
	 * Get the Canvas instance for the feature grid list.
	 *
	 * @return Canvas
	 * @since 1.8.0
	 */
	@Api
	public Canvas getCanvas() {
		return listGrid;
	}


	// -------------------------------------------------------------------------
	// Private methods:
	// -------------------------------------------------------------------------
	/**
	 * Builds up the list showing the features grouped by client layerId
	 *
	 * @param mapModel
	 *          The mapModel 
	 * @param featureMap
	 * 			The features to be listed, grouped per serverLayerId            
	 */
	private void buildList(MapModel mapModel, Map<String, List<org.geomajas.layer.feature.Feature>> featureMap) {
		listGrid.setTitle(messages.nearbyFeaturesListTooltip());
		listGrid.setShowEmptyMessage(true);
		listGrid.setWidth100();
		listGrid.setHeight100();
		listGrid.setShowHeader(false);
		listGrid.setShowAllRecords(true);
		// listGrid.setAutoFitMaxRecords(20); // Not needed
		// listGrid.setAutoFitData(Autofit.HORIZONTAL); // Not OK, expands too much so that vertical scroll bar can
														// become invisible without hor. scrolling

		ListGridField labelField = new ListGridField("label");
		ListGridField featIdField = new ListGridField("featureId");
		ListGridField layerField = new ListGridField("layerId");

		listGrid.setGroupByField("layerId");

		listGrid.setGroupStartOpen(GroupStartOpen.ALL);
		listGrid.setFields(/* dummyIndentField, */labelField, layerField, featIdField);
		listGrid.hideField("layerId");
		listGrid.hideField("featureId");
		// listGrid.setWrapCells(true);
		listGrid.setFixedRecordHeights(true);
		listGrid.addRecordClickHandler(new RecordClickHandler() {

			public void onRecordClick(RecordClickEvent event) {
				String featureId = event.getRecord().getAttribute("featureId");
				Feature feat = features.get(featureId);
				featClickHandler.onClick(feat);
			}

		});

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


	private void createListGrid() {
		listGrid = new ListGrid() {

			@Override
			/* Override getCellCSSText to implement padding-left of ordinary feature rows */
			protected String getCellCSSText(ListGridRecord record, int rowNum, int colNum) {
				// Note: using listGrid.setCellPadding() would also padd group rows
				String newStyle;
				String style = record.getCustomStyle(); /* returns groupNode if group row, else e.g. null */

				if ("label".equals(getFieldName(colNum)) && (null == style || !style.equalsIgnoreCase("groupNode"))) {
					newStyle = "padding-left: 40px;";
				} else { /* groupCell */
					newStyle = "padding-left: 5px;";
				}
				if (null != super.getCellCSSText(record, rowNum, colNum)) {
					newStyle = super.getCellCSSText(record, rowNum, colNum) + newStyle; /*
																						 * add padding after original,
																						 * the latter specified wins.
																						 */
				}
				return newStyle;
			}
		};
	}

	/**
	 * Adds a new feature to the grid list. A {@link VectorLayer} must have been set first, and 
	 * the feature must belong to that VectorLayer.
	 *
	 * @param feature
	 *            The feature to be added to the grid list.
	 * @return Returns true in case of success, and false if the feature is null
	 *         or if the feature does not belong to the correct layer or if the layer has not yet been set.
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

		listGrid.addData(record);
		return true;
	}


	private static String getFullFeatureId(Feature feature, VectorLayer layer) {
		return layer.getServerLayerId() + "." + feature.getId();
	}

	/**
	 * Build the entire widget.
	 *
	 * @param featureMap
	 * @param featureMap
	 * 			The features to be listed, grouped per serverLayerId     
	 */
	private void buildWidget(Map<String, List<org.geomajas.layer.feature.Feature>> featureMap) {

		if (initialized) {
			clearMembers();
			initialized = false;
		}
		buildList(mapModel, featureMap);
		listGrid.markForRedraw();
		initialized = true;
	}
	
	private void clearMembers() {
		vectorLayers.clear();
		features.clear();
		this.listGrid.destroy();
		this.listGrid = null;
		createListGrid();
	}

}
