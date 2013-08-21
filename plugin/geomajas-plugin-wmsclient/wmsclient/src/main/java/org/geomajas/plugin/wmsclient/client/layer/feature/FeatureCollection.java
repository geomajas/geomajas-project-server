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

package org.geomajas.plugin.wmsclient.client.layer.feature;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.configuration.AbstractReadOnlyAttributeInfo;
import org.geomajas.gwt.client.map.feature.Feature;

/**
 * DTO containing a list of features + the attribute descriptors for all attributes that can be in use by the features.
 * 
 * @author An Buyle
 */
public class FeatureCollection {

	private List<Feature> features;

	private List<AbstractReadOnlyAttributeInfo> attributeDescriptors;

	public FeatureCollection() {
		features = new ArrayList<Feature>();
		attributeDescriptors = new ArrayList<AbstractReadOnlyAttributeInfo>();
	}

	public FeatureCollection(List<Feature> features, List<AbstractReadOnlyAttributeInfo> attributeDescriptors) {
		this.features = features;
		this.attributeDescriptors = attributeDescriptors;
	}

	public List<Feature> getFeatures() {
		return features;
	}

	public void setFeatures(List<Feature> features) {
		this.features = features;
	}

	public List<AbstractReadOnlyAttributeInfo> getAttributeDescriptors() {
		return attributeDescriptors;
	}

	public void setAttributeDescriptors(List<AbstractReadOnlyAttributeInfo> attributeDescriptors) {
		this.attributeDescriptors = attributeDescriptors;
	}
}