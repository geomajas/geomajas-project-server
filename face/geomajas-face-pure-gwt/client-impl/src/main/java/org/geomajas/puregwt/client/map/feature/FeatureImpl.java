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

package org.geomajas.puregwt.client.map.feature;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.geomajas.geometry.Geometry;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.puregwt.client.map.layer.FeaturesSupported;
import org.geomajas.puregwt.client.map.layer.Layer;

/**
 * Default implementation of the Feature interface. Represents the individual objects of vector layers.
 * 
 * @author Pieter De Graef
 */
public class FeatureImpl implements Feature {

	private final FeaturesSupported<?> layer;

	private String id;

	private Map<String, Attribute<?>> attributes;

	private Geometry geometry;

	private String label;

	private boolean updatable;

	private boolean deletable;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	@SuppressWarnings("rawtypes")
	FeatureImpl(org.geomajas.layer.feature.Feature feature, FeaturesSupported<?> layer) {
		this.layer = layer;
		id = feature.getId();
		attributes = new HashMap<String, Attribute<?>>();
		for (Entry<String, Attribute> entry : feature.getAttributes().entrySet()) {
			attributes.put(entry.getKey(), entry.getValue());
		}
		label = feature.getLabel();
		updatable = feature.isUpdatable();
		deletable = feature.isDeletable();
		geometry = feature.getGeometry();
	}

	// ------------------------------------------------------------------------
	// Feature implementation:
	// ------------------------------------------------------------------------

	public String getId() {
		return id;
	}

	public Object getAttributeValue(String attributeName) {
		Attribute<?> attribute = attributes.get(attributeName);
		if (attribute != null) {
			return attribute.getValue();
		}
		return null;
	}

	public String getLabel() {
		return label;
	}

	public Map<String, Attribute<?>> getAttributes() {
		return attributes;
	}

	public Geometry getGeometry() {
		return geometry;
	}

	public boolean isSelected() {
		return layer.isFeatureSelected(id);
	}

	public Layer<?> getLayer() {
		return layer;
	}

	public boolean isUpdatable() {
		return updatable;
	}

	public boolean isDeletable() {
		return deletable;
	}
}