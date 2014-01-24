/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.layer;

import java.util.Iterator;

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;
import org.geomajas.configuration.VectorLayerInfo;
import org.geomajas.layer.feature.FeatureModel;
import org.opengis.filter.Filter;

import com.vividsolutions.jts.geom.Envelope;

/**
 * A layer which contains vector data.
 * <p/>
 * It hold all the elements that belong to a layer within the bounds determined by the client's view. It is able to
 * manipulate the objects which are represented as features on the map.
 *
 * @author Pieter De Graef
 * @author Jan De Moerloose
 * @since 1.6.0
 */
@Api(allMethods = true)
@UserImplemented
public interface VectorLayer extends Layer<VectorLayerInfo> {

	/**
	 * Indicate whether the layer is capable of creating new features.
	 *
	 * @return true when creating new features is possible for this layer
	 */
	boolean isCreateCapable();

	/**
	 * Indicate whether the layer is capable of updating features.
	 *
	 * @return true when updating features is possible for this layer
	 */
	boolean isUpdateCapable();

	/**
	 * Indicate whether the layer is capable of creating new features.
	 *
	 * @return true when deleting features is possible for this layer
	 */
	boolean isDeleteCapable();

	/**
	 * Return the feature model of this layer model.
	 *
	 * @return feature model for this layer
	 */
	FeatureModel getFeatureModel();

	/**
	 * Creates a new feature.
	 *
	 * @param feature feature value object
	 * @return the new feature (with assigned or generated ID)
	 * @throws LayerException oops
	 */
	Object create(Object feature) throws LayerException;

	/**
	 * Update an existing feature of the model or creates a new feature.
	 *
	 * @param feature feature value object
	 * @return the feature itself
	 * @throws LayerException oops
	 */
	Object saveOrUpdate(Object feature) throws LayerException;

	/**
	 * Reads an existing feature of the model.
	 * <p/>
	 * When no feature with the requested id is found,
	 * {@link org.geomajas.global.ExceptionCode#LAYER_MODEL_FEATURE_NOT_FOUND} is thrown.
	 *
	 * @param featureId unique id of the feature
	 * @return feature value object
	 * @throws LayerException when reading failed, for example ExceptionCode.LAYER_MODEL_FEATURE_NOT_FOUND
	 */
	Object read(String featureId) throws LayerException;

	/**
	 * Deletes a feature of the model.
	 *
	 * @param featureId unique id of the feature
	 * @throws LayerException oops
	 */
	void delete(String featureId) throws LayerException;

	/**
	 * Retrieve all the features from the model that this filter accepts.
	 * <p><b>Note that not all vector layers may support the offset or maxResultSize parameters! Check the individual
	 * layer documentation to be certain.</b></p>
	 *
	 * @param filter filter to be applied
	 * @param offset Skip the first 'offset' features in the result. This is meant for paging.
	 * @param maxResultSize Limit the result to a maximum number of features. Used for paging. Zero for unlimited.
	 * @return reader of feature value objects
	 * @throws LayerException oops
	 */
	Iterator<?> getElements(Filter filter, int offset, int maxResultSize) throws LayerException;

	/**
	 * Retrieve the bounds of the specified features.
	 *
	 * @param filter filter to be applied
	 * @return the bounds of the specified features
	 * @throws LayerException oops
	 */
	Envelope getBounds(Filter filter) throws LayerException;

	/**
	 * Return the bounds of this model.
	 *
	 * @return bounds of this model
	 * @throws LayerException oops
	 */
	Envelope getBounds() throws LayerException;
}
