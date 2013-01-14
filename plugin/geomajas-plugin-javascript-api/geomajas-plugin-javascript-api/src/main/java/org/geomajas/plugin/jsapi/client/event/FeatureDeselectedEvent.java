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

package org.geomajas.plugin.jsapi.client.event;

import org.geomajas.annotation.Api;
import org.geomajas.plugin.jsapi.client.map.feature.Feature;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

/**
 * This event is thrown when a feature has been deselected.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
@Export
@ExportPackage("org.geomajas.jsapi.event")
public class FeatureDeselectedEvent extends JsEvent<FeatureDeselectedHandler> implements Exportable {

	private Feature feature;

	/**
	 * Constructor.
	 *
	 * @param feature deselected feature
	 */
	public FeatureDeselectedEvent(Feature feature) {
		this.feature = feature;
	}

	/** {@inheritDoc} */
	public Class<FeatureDeselectedHandler> getType() {
		return FeatureDeselectedHandler.class;
	}

	/** {@inheritDoc} */
	protected void dispatch(FeatureDeselectedHandler handler) {
		handler.onFeatureDeselected(this);
	}

	/**
	 * Get deselected feature.
	 *
	 * @return deselected feature
	 */
	public Feature getFeature() {
		return feature;
	}
}