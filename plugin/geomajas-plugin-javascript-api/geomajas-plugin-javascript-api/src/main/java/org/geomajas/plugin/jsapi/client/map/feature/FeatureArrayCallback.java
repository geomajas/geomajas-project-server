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

package org.geomajas.plugin.jsapi.client.map.feature;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportClosure;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

/**
 * Call-back object for asynchronous methods that deal with an array of features.
 * 
 * @author Oliver May
 */
@Export
@ExportClosure
public interface FeatureArrayCallback extends Exportable {

	void execute(FeatureArrayHolder featureArrayHolder);

	/**
	 * Wrapper around a feature array, because the GWT exporter doesn't know how to handle arrays in a closure.
	 * 
	 * @author Pieter De Graef
	 */
	@Export
	@ExportPackage("org.geomajas.jsapi.map.feature")
	public class FeatureArrayHolder implements Exportable {

		private Feature[] features;

		public FeatureArrayHolder() {
		}

		public FeatureArrayHolder(Feature[] features) {
			this.features = features;
		}

		public Feature[] getFeatures() {
			return features;
		}

		public void setFeatures(Feature[] features) {
			this.features = features;
		}
	}
}