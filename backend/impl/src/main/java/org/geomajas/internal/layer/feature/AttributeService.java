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

package org.geomajas.internal.layer.feature;

import java.util.HashMap;
import java.util.Map;

import org.geomajas.configuration.AssociationAttributeInfo;
import org.geomajas.configuration.AttributeInfo;
import org.geomajas.configuration.FeatureInfo;
import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.configuration.PrimitiveType;
import org.geomajas.global.ExceptionCode;
import org.geomajas.internal.layer.vector.lazy.LazyManyToOneAttribute;
import org.geomajas.internal.layer.vector.lazy.LazyOneToManyAttribute;
import org.geomajas.internal.layer.vector.lazy.LazyPrimitiveAttribute;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.VectorLayerLazyFeatureConversionSupport;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.FeatureModel;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.security.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Service which provides utility methods for handling feature attributes.
 *
 * @author Joachim Van der Auwera
 */
@Component
public class AttributeService {

	@Autowired
	private SecurityContext securityContext;

	/**
	 * Get the attributes for a feature, and put them in the feature object.
	 * <p/>
	 * The attributes are converted lazily if requested by the layer.
	 * <p/>
	 * The feature is filled into the passed feature object. If the feature should not be visible according to security,
	 * null is returned (the original (passed) feature should be discarded in that case). The attributes are filtered
	 * according to security settings. The editable and deletable states for the feature are also set.
	 *
	 * @param layer layer which contains the feature
	 * @param feature feature for the result
	 * @param featureBean plain object for feature
	 * @return feature with filled attributes or null when feature not visible
	 * @throws LayerException problem converting attributes
	 */
	public InternalFeature getAttributes(VectorLayer layer, InternalFeature feature, Object featureBean)
			throws LayerException {
		String layerId = layer.getId();

		Map<String, Attribute> featureAttributes = getAttributes(layer, featureBean);
		feature.setAttributes(featureAttributes); // to allow isAttributeReadable to see full object
		if (securityContext.isFeatureVisible(layerId, feature)) {

			feature.setAttributes(filterAttributes(layerId, feature, featureAttributes));

			feature.setEditable(securityContext.isFeatureUpdateAuthorized(layerId, feature));
			feature.setDeletable(securityContext.isFeatureDeleteAuthorized(layerId, feature));

			return feature;
		}
		return null;
	}

	private Map<String, Attribute> filterAttributes(String layerId, InternalFeature feature,
													Map<String, Attribute> featureAttributes) {
		Map<String, Attribute> filteredAttributes = new HashMap<String, Attribute>();
		for (Map.Entry<String, Attribute> entry : featureAttributes.entrySet()) {
			String key = entry.getKey();
			if (securityContext.isAttributeReadable(layerId, feature, key)) {
				Attribute attribute = entry.getValue();
				attribute.setEditable(securityContext.isAttributeWritable(layerId, feature, key));
				filteredAttributes.put(key, attribute);
			}
		}
		return filteredAttributes;
	}

	private Map<String, Attribute> getAttributes(VectorLayer layer, Object featureBean) throws LayerException {
		FeatureModel featureModel = layer.getFeatureModel();
		FeatureInfo featureInfo = layer.getLayerInfo().getFeatureInfo();
		String geometryAttributeName = featureInfo.getGeometryType().getName();
		boolean lazy = layer instanceof VectorLayerLazyFeatureConversionSupport &&
				((VectorLayerLazyFeatureConversionSupport) layer).useLazyFeatureConversion();

		Map<String, Attribute> attributes = new HashMap<String, Attribute>();
		for (AttributeInfo attribute : featureInfo.getAttributes()) {
			String name = attribute.getName();
			if (!name.equals(geometryAttributeName)) {
				Attribute value;
				if (lazy) {
					// need to use the correct lazy type to allow instanceof to work
					if (attribute instanceof AssociationAttributeInfo) {
						switch (((AssociationAttributeInfo) attribute).getType()) {
							case MANY_TO_ONE:
								value = new LazyManyToOneAttribute(featureModel, featureBean, name);
								break;
							case ONE_TO_MANY:
								value = new LazyOneToManyAttribute(featureModel, featureBean, name);
								break;
							default:
								throw new LayerException(ExceptionCode.UNEXPECTED_PROBLEM,
										"Coding error, not all AssociationType options are covered");
						}
					} else {
						PrimitiveType type = ((PrimitiveAttributeInfo) attribute).getType();
						value = new LazyPrimitiveAttribute(type, featureModel, featureBean, name);
					}
				} else {
					value = featureModel.getAttribute(featureBean, name);
				}
				attributes.put(name, value);
			}
		}
		return attributes;
	}

}
