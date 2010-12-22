package org.geomajas.puregwt.client.map.layer;

import java.util.Set;

import org.geomajas.layer.feature.Feature;


public interface FeaturesSupported {

	void setFilter(String filter);
	
	String getFilter();
	
	boolean isFeatureSelected(String featureId);
	
	boolean selectFeature(Feature feature);
	
	boolean deselectFeature(Feature feature);
	
	void clearSelectedFeatures();
	
	Set<String> getSelectedFeatureIds();
	
	//VectorLayerStore getFeatureStore();
}