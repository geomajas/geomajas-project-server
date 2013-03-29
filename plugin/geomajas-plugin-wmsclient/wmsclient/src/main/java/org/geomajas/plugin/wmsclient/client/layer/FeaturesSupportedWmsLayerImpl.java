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

	/** {@inheritDoc} */
	@Override
	public void setFilter(String filter) {
		this.filter = filter;
		refresh();
	}

	/** {@inheritDoc} */
	@Override
	public String getFilter() {
		return filter;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isFeatureSelected(String featureId) {
		return selection.containsKey(featureId);
	}

	/** {@inheritDoc} */
	@Override
	public boolean selectFeature(Feature feature) {
		if (!selection.containsValue(feature) && feature.getLayer() == this) {
			selection.put(feature.getId(), feature);
			eventBus.fireEvent(new FeatureSelectedEvent(this, feature));
		}
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public boolean deselectFeature(Feature feature) {
		if (selection.containsKey(feature.getId())) {
			selection.remove(feature.getId());
			eventBus.fireEvent(new FeatureDeselectedEvent(this, feature));
			return true;
		}
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public void clearSelectedFeatures() {
		for (Feature feature : selection.values()) {
			eventBus.fireEvent(new FeatureDeselectedEvent(this, feature));
		}
		selection.clear();
	}

	/** {@inheritDoc} */
	@Override
	public Collection<String> getSelectedFeatureIds() {
		return selection.keySet();
	}

	/** {@inheritDoc} */
	@Override
	public void getFeatureInfo(Coordinate location, Callback<List<Feature>, String> callback) {
		wmsService.getFeatureInfo(this, location, callback);
	}

	/** {@inheritDoc} */
	@Override
	public void getFeatureInfo(Coordinate location, GetFeatureInfoFormat format, Callback<Object, String> callback) {
		wmsService.getFeatureInfo(this, location, format, callback);
	}
}