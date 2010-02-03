/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.geomajas.layer.feature;

import com.vividsolutions.jts.geom.Geometry;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.layer.LayerException;

import java.util.Map;

/**
 * A feature model provides sufficient information to generically create/read and update a feature. This level of
 * indirection is necessary in order to allow features to follow the POJO (Plain Old Java Object) model without
 * mandating it. Currently supports flat features. Warning for implementors : clients cannot be prevented from modifying
 * attributes or geometries obtained by the accessor methods. Return cloned or unmodifiable objects if you want to avoid
 * this !
 *
 * @author Jan De Moerloose
 */
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
	Object getAttribute(Object feature, String name) throws LayerException;

	/**
	 * Return all attributes as a map with the attribute name as the key.
	 *
	 * @param feature the feature
	 * @return map of attribute values
	 * @throws LayerException oops
	 */
	Map<String, Object> getAttributes(Object feature) throws LayerException;

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
	void setAttributes(Object feature, java.util.Map<String, Object> attributes) throws LayerException;

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
	 * Return the class extended by this feature model's class.
	 *
	 * @return the super class or null if there is no super class
	 * @throws LayerException if class could not be retrieved TODO: replace by super feature model !!!
	 */
	Class<?> getSuperClass() throws LayerException;

	/**
	 * Can a given 'feature' object be handled by this FeatureModel?
	 *
	 * @param feature A object from a layer.
	 * @return true when feature can be handled by this feature model
	 */
	boolean canHandle(Object feature);
}
