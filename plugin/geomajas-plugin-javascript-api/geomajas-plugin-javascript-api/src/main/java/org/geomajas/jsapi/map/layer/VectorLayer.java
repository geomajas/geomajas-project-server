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
package org.geomajas.jsapi.map.layer;

import org.geomajas.annotation.Api;
import org.geomajas.annotation.FutureApi;
import org.geomajas.jsapi.map.Feature;
import org.geomajas.jsapi.spatial.geometry.Bbox;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportClosure;
import org.timepedia.exporter.client.Exportable;


/**
 * Javascript exportable facade for a Vectorlayer.
 * 
 * @author Oliver May
 *
 * @since 1.0.0
 */
@FutureApi(allMethods = true)
public interface VectorLayer extends Layer {

	/**
	 * Apply a filter on the layer. Such a filter specifies which features are to be shown on the map, and which aren't.
	 * This filter is actually used on the back-end and therefore follows the CQL standards.
	 * 
	 * @param filter
	 *            The CQL filter, based upon the layer attribute definitions. Use null to disable filtering.
	 */
	void setFilter(String filter);
	
	/**
	 * Returns the filter that has been set onto this layer.
	 * 
	 * @return Returns the CQL filter.
	 */
	String getFilter();
	
	/**
	 * Is a certain feature selected or not?
	 * 
	 * @param featureId
	 *            The unique identifier of the feature within this layer.
	 * @return Returns true if the feature has been selected, false otherwise.
	 */
	boolean isFeatureSelected(String featureId);

	/**
	 * Select the given feature.
	 * 
	 * @param feature
	 *            The feature to select. Must be part of this layer.
	 * @param callback
	 *            Callback including feature when selection was successful, or null if feature was not found.
	 */
	void selectFeature(String featureId, FeatureCallback callback);
	
	/**
	 * Deselect the given feature.
	 * 
	 * @param feature
	 *            The feature to deselect. Must be part of this layer.
	 * @param callback
	 *            Callback including feature when selection was successful, or null if feature was not found.
	 */
	void deSelectFeature(String featureId, FeatureCallback callback);

	/** 
	 * Deselect all features within this layer.
	 * 
	 */
	void clearSelectedFeatures();

	/**
	 * Return a collection of all selected features within this layer.
	 * 
	 * @return Returns the identifiers, not the actual features.
	 */
	String[] getSelectedFeatureIds();

	/**
	 * Return a feature based on it's id.
	 * 
	 * @param featureId the id of the feature to fetch.
	 * @param callBack the callback method called when the feature is retrieved.
	 */
	void getFeature(String featureId, final FeatureCallback callBack);

	
	/**
	 * Return a list of features based on a bounding box.
	 * 
	 * @param bounds the bounds in wich to search for features.
	 * @param callBack the callback method called when the feature is retrieved.
	 */
	void getFeatures(Bbox bounds, final FeaturesCallback callBack);
	
	/**
	 * Callback object for async methods that deal with a feature.
	 * 
	 * @author Oliver May
	 * @since 1.0.0 
	 */
	@Export
	@ExportClosure
	@Api(allMethods = true)
	interface FeatureCallback extends Exportable {
		void execute(Feature feature);
	}

	/**
	 * Callback object for async methods that deal with features.
	 * 
	 * @author Oliver May
	 * @since 1.0.0 
	 */
	@Export
	@ExportClosure
	@Api(allMethods = true)
	interface FeaturesCallback extends Exportable {
		void execute(String[] features);
	}
	

}
