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

package org.geomajas.puregwt.client.map.layer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.configuration.FeatureStyleInfo;
import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.configuration.client.ClientVectorLayerInfo;
import org.geomajas.gwt.client.util.UrlBuilder;
import org.geomajas.puregwt.client.event.FeatureDeselectedEvent;
import org.geomajas.puregwt.client.event.FeatureSelectedEvent;
import org.geomajas.puregwt.client.event.LayerLabelHideEvent;
import org.geomajas.puregwt.client.event.LayerLabelShowEvent;
import org.geomajas.puregwt.client.map.MapEventBus;
import org.geomajas.puregwt.client.map.ViewPort;
import org.geomajas.puregwt.client.map.feature.Feature;
import org.geomajas.puregwt.client.service.EndPointService;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

/**
 * Vector layer representation.
 * 
 * @author Pieter De Graef
 */
public class VectorServerLayerImpl extends AbstractServerLayer<ClientVectorLayerInfo> implements VectorServerLayer {

	private Map<String, Feature> selection;

	private String filter;

	private boolean labeled;

	private EndPointService endPointService;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	@Inject
	public VectorServerLayerImpl(@Assisted ClientVectorLayerInfo layerInfo, @Assisted final ViewPort viewPort,
			@Assisted final MapEventBus eventBus, final EndPointService endPointService) {
		super(layerInfo, viewPort, eventBus);
		this.endPointService = endPointService;
		selection = new HashMap<String, Feature>();
	}

	// ------------------------------------------------------------------------
	// Layer implementation:
	// ------------------------------------------------------------------------

	@Override
	public List<LayerStylePresenter> getStylePresenters() {
		List<LayerStylePresenter> stylePresenters = new ArrayList<LayerStylePresenter>();

		NamedStyleInfo styleInfo = layerInfo.getNamedStyleInfo();
		for (int i = 0; i < styleInfo.getFeatureStyles().size(); i++) {
			FeatureStyleInfo sfi = styleInfo.getFeatureStyles().get(i);
			UrlBuilder url = new UrlBuilder(endPointService.getLegendServiceUrl());
			url.addPath(getServerLayerId());
			url.addPath(styleInfo.getName());
			url.addPath(i + LEGEND_ICON_EXTENSION);
			stylePresenters.add(new ServerLayerStylePresenter(i, url.toString(), sfi.getName()));
		}
		return stylePresenters;
	}

	// ------------------------------------------------------------------------
	// FeaturesSupported implementation:
	// ------------------------------------------------------------------------

	@Override
	public void setFilter(String filter) {
		this.filter = filter;
		refresh();
	}

	@Override
	public String getFilter() {
		return filter;
	}

	@Override
	public boolean isFeatureSelected(String featureId) {
		return selection.containsKey(featureId);
	}

	@Override
	public boolean selectFeature(Feature feature) {
		if (!selection.containsValue(feature) && feature.getLayer() == this) {
			selection.put(feature.getId(), feature);
			eventBus.fireEvent(new FeatureSelectedEvent(this, feature));
		}
		return false;
	}

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
	public void clearSelectedFeatures() {
		for (Feature feature : selection.values()) {
			eventBus.fireEvent(new FeatureDeselectedEvent(this, feature));
		}
		selection.clear();
	}

	@Override
	public Collection<Feature> getSelectedFeatures() {
		return selection.values();
	}

	// ------------------------------------------------------------------------
	// LabelsSupported implementation:
	// ------------------------------------------------------------------------

	@Override
	public void setLabeled(boolean labeled) {
		this.labeled = labeled;
		if (labeled) {
			eventBus.fireEvent(new LayerLabelShowEvent(this));
		} else {
			eventBus.fireEvent(new LayerLabelHideEvent(this));
		}
	}

	@Override
	public boolean isLabeled() {
		return labeled;
	}
}