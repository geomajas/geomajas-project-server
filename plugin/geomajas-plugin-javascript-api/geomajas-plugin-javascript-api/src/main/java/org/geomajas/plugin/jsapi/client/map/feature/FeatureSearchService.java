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

import org.geomajas.geometry.Bbox;
import org.geomajas.plugin.jsapi.client.map.layer.FeaturesSupported;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

/**
 * Service definition for searching for features. It defines a list of methods that do nothing but presenting different
 * ways of searching features.
 * 
 * @author Pieter De Graef
 */
@Export
@ExportPackage("org.geomajas.jsapi.map.feature")
public interface FeatureSearchService extends Exportable {

	/**
	 * Search a single feature within a certain layer, using the feature ID.
	 * 
	 * @param layer
	 *            The features supported layer wherein to search.
	 * @param id
	 *            The unique ID of the feature within the layer.
	 * @param callback
	 *            Call-back method executed on return (when the feature has been found).
	 */
	void searchById(FeaturesSupported layer, String[] id, FeatureArrayCallback callback);

	/**
	 * Search all features within a certain layer that intersect a certain bounding box.
	 * 
	 * @param layer
	 *            The features supported layer wherein to search.
	 * @param bbox
	 *            The bounding box wherein to search.
	 * @param callback
	 *            Call-back method executed on return (when features have been found).
	 */
	void searchInBounds(FeaturesSupported layer, Bbox bbox, FeatureArrayCallback callback);
}