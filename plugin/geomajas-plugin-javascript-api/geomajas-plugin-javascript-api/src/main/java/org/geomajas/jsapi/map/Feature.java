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
package org.geomajas.jsapi.map;

import java.util.HashMap;
import java.util.Map;

import org.geomajas.annotation.FutureApi;
import org.geomajas.jsapi.map.layer.Layer;
import org.geomajas.jsapi.map.layer.VectorLayer;
import org.geomajas.jsapi.spatial.geometry.Bbox;
import org.geomajas.layer.feature.Attribute;
import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;
import org.timepedia.exporter.client.NoExport;

/**
 * Javascript exportable facade for a feature.
 * 
 * @author Oliver May
 * @since 1.0.0
 */
@Export
@ExportPackage("org.geomajas.jsapi.map")
@FutureApi(allMethods = true)
public class Feature implements Exportable {

	private String id;

	private Map<String, Attribute> attributes = new HashMap<String, Attribute>();
	
	private String label;

	private Bbox bounds;

	private Layer layer;

	private boolean selected;
	
	/**
	 * Constructor for a Feature object.
	 * 
	 * @param id the id of the feature
	 * @param attributes the attributes
	 * @param label the label
	 * @param geometry the geometry
	 * @param bounds the bounds of the geometry
	 * @param layer the layer
	 * @param selected if the feature is selected
	 */
	public Feature(String id, Map<String, Attribute> attributes, String label, String geometry, Bbox bounds, 
			VectorLayer layer, boolean selected) {
		this.id = id;
		this.attributes = attributes;
		this.label = label;
		this.bounds = bounds;
		this.layer = layer;
		this.selected = selected;
	}
	
	/**
	 * Return the features unique identifier.
	 * 
	 * @return Returns the ID as a string.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Get the value of a certain attribute.
	 * 
	 * @param attributeName
	 *            The name of the attribute. These names are configured within a layer.
	 * @return Returns the value of the attribute, or null. If the attribute does not exist, this method will also
	 *         return null.
	 */
	public String getAttributeValue(String attributeName) {
		return attributes.get(attributeName).toString();
	}

	/**
	 * Return the title of this feature. This is usually the value of one of the attributes (or derived from it).
	 * 
	 * @return Returns a readable label.
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Get the feature's geometry, , null when it needs to be lazy loaded.
	 * 
	 * @return geometry
	 */
	public Bbox getBounds() {
		return bounds;
	}

	/**
	 * Has this feature been selected or not?
	 * 
	 * @return Returns true or false.
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * Return the layer instance associated with this feature. Every feature can belong to only one layer.
	 * 
	 * @return Returns the layer. This value can never be null.
	 */
	@NoExport
	public Layer getLayer() {
		return layer;
	}

	/**
	 * Get the attribute keys, null when it needs to be lazy loaded.
	 * 
	 * @return attribute keys
	 */
	public String[] getAttributes() {
		return attributes.keySet().toArray(new String[attributes.size()]);
	}

	
}
