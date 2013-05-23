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

package org.geomajas.plugin.wmsclient.client.layer;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.geometry.Coordinate;
import org.geomajas.plugin.wmsclient.client.service.WmsService.GetFeatureInfoFormat;
import org.geomajas.puregwt.client.event.FeatureDeselectedEvent;
import org.geomajas.puregwt.client.event.FeatureSelectedEvent;
import org.geomajas.puregwt.client.map.feature.Feature;

import com.google.gwt.core.client.Callback;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * Default implementation of the {@link FeaturesSupportedWmsLayer}.
 * 
 * @author Pieter De Graef
 */
public class FeaturesSupportedWmsLayerImpl extends WmsLayerImpl implements FeaturesSupportedWmsLayer {

	private final Map<String, Feature> selection = new HashMap<String, Feature>();

	private String filter;

	@Inject
	public FeaturesSupportedWmsLayerImpl(@Assisted String title, @Assisted WmsLayerConfiguration wmsConfig,
			@Assisted WmsTileConfiguration tileConfig) {
		super(title, wmsConfig, tileConfig);
	}

	// ------------------------------------------------------------------------
	// FeaturesSupported implementation:
	// ------------------------------------------------------------------------

	@Override
	@Override
	public void setFilter(String filter) {
		this.filter = filter;
		refresh();
	}

	@Override
	@Override
	public String getFilter() {
		return filter;
	}

	@Override
	@Override
	public boolean isFeatureSelected(String featureId) {
		return selection.containsKey(featureId);
	}

	@Override
	@Override
	public boolean selectFeature(Feature feature) {
		if (!selection.containsValue(feature) && feature.getLayer() == this) {
			selection.put(feature.getId(), feature);
			eventBus.fireEvent(new FeatureSelectedEvent(this, feature));
		}
		return false;
	}

	@Override
	@Override
	public boolean deselectFeature(Feature feature) {
		if (selection.containsKey(feature.getId())) {
			selection.remove(feature.getId());
			eventBus.fireEvent(new FeatureDeselectedEvent(this, feature));
			return true;
		}
		return false;
	}

	@Override
	@Override
	public void clearSelectedFeatures() {
		for (Feature feature : selection.values()) {
			eventBus.fireEvent(new FeatureDeselectedEvent(this, feature));
		}
		selection.clear();
	}

	@Override
	@Override
	public Collection<String> getSelectedFeatureIds() {
		return selection.keySet();
	}

	@Override
	@Override
	public void getFeatureInfo(Coordinate location, Callback<List<Feature>, String> callback) {
		wmsService.getFeatureInfo(this, location, callback);
	}

	@Override
	@Override
	public void getFeatureInfo(Coordinate location, GetFeatureInfoFormat format, Callback<Object, String> callback) {
		wmsService.getFeatureInfo(this, location, format, callback);
	}
}