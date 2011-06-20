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

package org.geomajas.puregwt.client.map.layer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.puregwt.client.map.MapRenderer;
import org.geomajas.puregwt.client.map.ViewPort;
import org.geomajas.puregwt.client.map.event.EventBus;
import org.geomajas.puregwt.client.map.event.FeatureDeselectedEvent;
import org.geomajas.puregwt.client.map.event.FeatureSelectedEvent;
import org.geomajas.puregwt.client.map.event.LayerLabelHideEvent;
import org.geomajas.puregwt.client.map.event.LayerLabelShowEvent;
import org.geomajas.puregwt.client.map.feature.Feature;

/**
 * Vector layer representation.
 * 
 * @author Pieter De Graef
 */
public class VectorLayer extends AbstractLayer<ClientVectorLayerInfo> implements LabelsSupported, FeaturesSupported {

	private Map<String, Feature> selection;

	private String filter;

	private boolean labeled;

	private VectorLayerRenderer renderer;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	public VectorLayer(ClientVectorLayerInfo layerInfo, ViewPort viewPort, EventBus eventBus) {
		super(layerInfo, viewPort, eventBus);
		selection = new HashMap<String, Feature>();
		renderer = new VectorLayerRenderer(this, viewPort);
	}

	// ------------------------------------------------------------------------
	// FeaturesSupported implementation:
	// ------------------------------------------------------------------------

	public void setFilter(String filter) {
		this.filter = filter;
		renderer.clear();
		viewPort.applyBounds(viewPort.getBounds());
	}

	public String getFilter() {
		return filter;
	}

	public boolean isFeatureSelected(String featureId) {
		return selection.containsKey(featureId);
	}

	public boolean selectFeature(Feature feature) {
		if (!selection.containsValue(feature) && feature.getLayer() == this) {
			selection.put(feature.getId(), feature);
			eventBus.fireEvent(new FeatureSelectedEvent(feature));
		}
		return false;
	}

	public boolean deselectFeature(Feature feature) {
		if (selection.containsValue(feature)) {
			selection.remove(feature);
			eventBus.fireEvent(new FeatureDeselectedEvent(feature));
		}
		return false;
	}

	public void clearSelectedFeatures() {
		for (Feature feature : selection.values()) {
			deselectFeature(feature);
		}
	}

	public Collection<String> getSelectedFeatureIds() {
		return selection.keySet();
	}

	// ------------------------------------------------------------------------
	// LabelsSupported implementation:
	// ------------------------------------------------------------------------

	public void setLabeled(boolean labeled) {
		this.labeled = labeled;
		if (labeled) {
			eventBus.fireEvent(new LayerLabelShowEvent(this));
		} else {
			eventBus.fireEvent(new LayerLabelHideEvent(this));
		}
	}

	public boolean isLabeled() {
		return labeled;
	}

	// ------------------------------------------------------------------------
	// Getters and setters:
	// ------------------------------------------------------------------------

	public MapRenderer getRenderer() {
		return renderer;
	}
}