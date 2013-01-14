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

import org.geomajas.annotation.FutureApi;
import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.jsapi.client.map.layer.FeaturesSupported;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

/**
 * Javascript exportable facade for a feature.
 * 
 * @author Oliver May
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Export
@ExportPackage("org.geomajas.jsapi.map.feature")
@FutureApi(allMethods = true)
public interface Feature extends Exportable {

	/**
	 * Return the features unique identifier.
	 * 
	 * @return Returns the ID as a string.
	 */
	String getId();

	/**
	 * Return the layer instance associated with this feature. Every feature can belong to only one layer.
	 * 
	 * @return Returns the layer. This value can never be null.
	 */
	FeaturesSupported getLayer();

	/**
	 * Get the value of a certain attribute.
	 * 
	 * @param attributeName
	 *            The name of the attribute. These names are configured within a layer.
	 * @return Returns the value of the attribute, or null. If the attribute does not exist, this method will also
	 *         return null.
	 */
	String getAttributeValue(String attributeName);

	/**
	 * Return the title of this feature. This is usually the value of one of the attributes (or derived from it).
	 * 
	 * @return Returns a readable label.
	 */
	String getLabel();

	/**
	 * Get the feature's geometry.
	 * 
	 * @return geometry
	 */
	Geometry getGeometry();
}