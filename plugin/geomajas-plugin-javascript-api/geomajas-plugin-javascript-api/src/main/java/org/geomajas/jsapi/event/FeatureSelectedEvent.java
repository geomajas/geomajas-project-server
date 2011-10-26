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

package org.geomajas.jsapi.event;

import org.geomajas.annotation.Api;
import org.geomajas.jsapi.map.feature.Feature;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

/**
 * This event is thrown when a feature has been selected.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
@Export
@ExportPackage("org.geomajas.jsapi.event")
public class FeatureSelectedEvent extends JsEvent<FeatureSelectedHandler> implements Exportable {

	private Feature feature;

	public FeatureSelectedEvent(Feature feature) {
		this.feature = feature;
	}

	public Class<FeatureSelectedHandler> getType() {
		return FeatureSelectedHandler.class;
	}

	protected void dispatch(FeatureSelectedHandler handler) {
		handler.onFeatureSelected(this);
	}

	public Feature getFeature() {
		return feature;
	}
}