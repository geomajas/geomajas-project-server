/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.layer.feature;

import java.util.Map;

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.layer.LayerException;

import com.vividsolutions.jts.geom.Geometry;

/**
 * A feature model provides sufficient information to generically create/read and update a feature. This level of
 * indirection is necessary in order to allow features to follow the POJO (Plain Old Java Object) model without
 * mandating it. Currently supports flat features. Warning for implementors : clients cannot be prevented from modifying
 * attributes or geometries obtained by the accessor methods. Return cloned or unmodifiable objects if you want to avoid
 * this !
 *
 * @author Jan De Moerloose
 * @since 1.6.0
 */
@Api(allMethods = true)
@UserImplemented
public interface FeatureModel {

	/**
	 * Set the layer info for this layer model.
	 *
	 * @param vectorLayerInfo vector layer info
	 * @throws LayerException oops
	 */
	void setLayerInfo(VectorLayerInfo vectorLayerInfo) throws LayerException;

	/**
	 * Return the attribute value with the given name.
	 *
	 * @param feature the feature
	 * @param name the attribute name
	 * @return attribute value
	 * @throws LayerException oops
	 */
	Attribute getAttribute(Object feature, String name) throws LayerException;

	/**
	 * Return all attributes as a map with the attribute name as the key.
	 *
	 * @param feature the feature
	 * @return map of attribute values
	 * @throws LayerException oops
	 * @deprecated the back-end implements this itself since 1.9.0 to provide lazy attribute loading if needed
	 */
	@Deprecated
	Map<String, Attribute> getAttributes(Object feature) throws LayerException;

	/**
	 * Return the id of a feature.
	 *
	 * @param feature the feature
	 * @return the feature's id
	 * @throws LayerException oops
	 */
	String getId(Object feature) throws LayerException;

	/**
	 * Return the geometry of a feature.
	 *
	 * @param feature the feature
	 * @return the feature's geometry
	 * @throws LayerException oops
	 */
	Geometry getGeometry(Object feature) throws LayerException;

	/**
	 * Set the attributes of a feature.
	 *
	 * @param feature the feature
	 * @param attributes the attributes
	 * @throws LayerException oops
	 */
	void setAttributes(Object feature, java.util.Map<String, Attribute> attributes) throws LayerException;

	/**
	 * Set the geometry of a feature.
	 *
	 * @param feature the feature
	 * @param geometry the geometry
	 * @throws LayerException oops
	 */
	void setGeometry(Object feature, Geometry geometry) throws LayerException;

	/**
	 * Create a default feature.
	 *
	 * @return a new feature with default properties only
	 * @throws LayerException oops
	 */
	Object newInstance() throws LayerException;

	/**
	 * Create a default feature.
	 *
	 * @param id Optional ID. use null, if you want the id to be generated.
	 * @return a new feature with default properties only
	 * @throws LayerException oops
	 */
	Object newInstance(String id) throws LayerException;

	/**
	 * Return the spatial reference id of the coordinate reference system.
	 *
	 * @return spatial reference id
	 * @throws LayerException oops
	 */
	int getSrid() throws LayerException;

	/**
	 * Return the name which can be used to get the geometric attribute.
	 * Can for example be be the name of a column in a SQL table, or the name of a method in a java object.
	 *
	 * @return attribute name
	 * @throws LayerException oops
	 */
	String getGeometryAttributeName() throws LayerException;

	/**
	 * Can a given 'feature' object be handled by this FeatureModel?
	 *
	 * @param feature A object from a layer.
	 * @return true when feature can be handled by this feature model
	 */
	boolean canHandle(Object feature);
}
