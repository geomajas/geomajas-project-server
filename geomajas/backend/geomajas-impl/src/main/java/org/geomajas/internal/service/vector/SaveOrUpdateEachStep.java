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

package org.geomajas.internal.service.vector;

import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.global.GeomajasSecurityException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.FeatureModel;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.rendering.pipeline.PipelineContext;
import org.geomajas.rendering.pipeline.PipelineService;
import org.geomajas.rendering.pipeline.PipelineStep;
import org.geomajas.security.SecurityContext;
import org.geotools.geometry.jts.JTS;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * Execute the vectorLayer.saveOrUpdateOne" pipeline for each of the features to saveOrUpdate.
 *
 * @author Joachim Van der Auwera
 */
public class SaveOrUpdateEachStep implements PipelineStep<SaveOrUpdateContainer, SaveOrUpdateContainer> {

	private String id;
	private String pipelineName;

	@Autowired
	private SecurityContext securityContext;

	@Autowired
	private PipelineService pipelineService;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setPipelineName(String pipelineName) {
		this.pipelineName = pipelineName;
	}

	public void execute(SaveOrUpdateContainer request, PipelineContext context,
			SaveOrUpdateContainer response) throws GeomajasException {
		/*
		SaveOrUpdateOneContainer oneContainer = new SaveOrUpdateOneContainer(request);
		PipelineInfo pipelineInfo = pipelineService.getPipeline(pipelineName, request.getLayerId());

		int count = request.getOldFeatures().size();
		for (int i = 0; i < count; i++) {
			oneContainer.setIndex(i);
			oneContainer.setOldFeature(request.getOldFeatures().get(i));
			oneContainer.setNewFeature(request.getNewFeatures().get(i));
			pipelineService.execute(pipelineInfo, oneContainer, oneContainer);
		}
		*/
		String layerId = request.getLayerId();
		VectorLayer layer = request.getLayer();
		FeatureModel featureModel = layer.getFeatureModel();
		MathTransform mapToLayer = request.getMapToLayer();

		int count = request.getOldFeatures().size();
		for (int i = 0; i < count; i++) {
			InternalFeature oldFeature = request.getOldFeatures().get(i);
			InternalFeature newFeature = request.getNewFeatures().get(i);
			if (null == newFeature) {
				// delete ?
				if (null != oldFeature) {
					if (securityContext.isFeatureDeleteAuthorized(layerId, oldFeature)) {
						layer.delete(oldFeature.getLocalId());
					} else {
						throw new GeomajasSecurityException(ExceptionCode.FEATURE_DELETE_PROHIBITED,
								oldFeature.getId(), securityContext.getUserId());
					}
				}
			} else {
				// create or update
				Object feature;
				if (null == oldFeature) {
					// create new feature
					transformGeometry(newFeature, mapToLayer);
					if (securityContext.isFeatureCreateAuthorized(layerId, oldFeature)) {
						if (newFeature.getLocalId() == null) {
							feature = featureModel.newInstance();
						} else {
							feature = featureModel.newInstance(newFeature.getLocalId());
						}
					} else {
						throw new GeomajasSecurityException(ExceptionCode.FEATURE_CREATE_PROHIBITED, securityContext
								.getUserId());
					}
				} else {
					if (null == oldFeature.getId() || !oldFeature.getId().equals(newFeature.getId())) {
						throw new GeomajasException(ExceptionCode.FEATURE_ID_MISMATCH);
					}
					transformGeometry(newFeature, mapToLayer);
					if (securityContext.isFeatureUpdateAuthorized(layerId, oldFeature, newFeature)) {
						feature = layer.read(newFeature.getLocalId());
					} else {
						throw new GeomajasSecurityException(ExceptionCode.FEATURE_UPDATE_PROHIBITED,
								oldFeature.getId(), securityContext.getUserId());
					}
				}

				// Assure only writable attributes are set
				Map<String, Attribute> requestAttributes = newFeature.getAttributes();
				Map<String, Attribute> filteredAttributes = new HashMap<String, Attribute>();
				if (null != requestAttributes) {
					for (String key : requestAttributes.keySet()) {
						if (securityContext.isAttributeWritable(layerId, newFeature, key)) {
							filteredAttributes.put(key, requestAttributes.get(key));
						}
					}
				}
				featureModel.setAttributes(feature, filteredAttributes);

				if (newFeature.getGeometry() != null) {
					featureModel.setGeometry(feature, newFeature.getGeometry());
				}
				feature = layer.saveOrUpdate(feature);

				// Not needed for existing features, but no problem to re-set feature id
				String id = featureModel.getId(feature);
				newFeature.setId(layerId + "." + id);

				filterAttributes(layerId, newFeature, featureModel.getAttributes(feature));

				newFeature.setEditable(securityContext.isFeatureUpdateAuthorized(layerId, newFeature));
				newFeature.setDeletable(securityContext.isFeatureDeleteAuthorized(layerId, newFeature));
			}
		}
	}

	/**
	 * Convert the geometry (if any) in the passed feature to layer crs.
	 *
	 * @param feature
	 *            feature in which the geometry should be updated
	 * @param mapToLayer
	 *            transformation to apply
	 * @throws GeomajasException
	 *             oops
	 */
	private void transformGeometry(InternalFeature feature, MathTransform mapToLayer) throws GeomajasException {
		if (feature.getGeometry() != null) {
			try {
				feature.setGeometry(JTS.transform(feature.getGeometry(), mapToLayer));
			} catch (TransformException te) {
				throw new GeomajasException(te, ExceptionCode.GEOMETRY_TRANSFORMATION_FAILED);
			}
		}
	}

	private Map<String, Attribute> filterAttributes(String layerId, InternalFeature feature,
													Map<String, Attribute> featureAttributes) {
		feature.setAttributes(featureAttributes); // to allow isAttributeReadable to see full object
		Map<String, Attribute> filteredAttributes = new HashMap<String, Attribute>();
		for (String key : featureAttributes.keySet()) {
			if (securityContext.isAttributeReadable(layerId, feature, key)) {
				Attribute attribute = featureAttributes.get(key);
				attribute.setEditable(securityContext.isAttributeWritable(layerId, feature, key));
				filteredAttributes.put(key, featureAttributes.get(key));
			}
		}
		feature.setAttributes(filteredAttributes);
		return filteredAttributes;
	}

}
