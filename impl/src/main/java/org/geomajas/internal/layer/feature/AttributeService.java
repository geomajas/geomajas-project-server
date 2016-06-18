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

package org.geomajas.internal.layer.feature;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.configuration.AbstractAttributeInfo;
import org.geomajas.configuration.AssociationAttributeInfo;
import org.geomajas.configuration.EditableAttributeInfo;
import org.geomajas.configuration.FeatureInfo;
import org.geomajas.configuration.PrimitiveAttributeInfo;
import org.geomajas.configuration.PrimitiveType;
import org.geomajas.configuration.SyntheticAttributeInfo;
import org.geomajas.global.ExceptionCode;
import org.geomajas.internal.layer.vector.lazy.LazyAttribute;
import org.geomajas.internal.layer.vector.lazy.LazyManyToOneAttribute;
import org.geomajas.internal.layer.vector.lazy.LazyOneToManyAttribute;
import org.geomajas.internal.layer.vector.lazy.LazyPrimitiveAttribute;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.VectorLayer;
import org.geomajas.layer.VectorLayerLazyFeatureConversionSupport;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.FeatureModel;
import org.geomajas.layer.feature.InternalFeature;
import org.geomajas.layer.feature.SyntheticAttributeBuilder;
import org.geomajas.layer.feature.attribute.AssociationValue;
import org.geomajas.layer.feature.attribute.ManyToOneAttribute;
import org.geomajas.layer.feature.attribute.OneToManyAttribute;
import org.geomajas.layer.feature.attribute.StringAttribute;
import org.geomajas.security.SecurityContext;
import org.geomajas.service.FeatureExpressionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Service which provides utility methods for handling feature attributes.
 *
 * @author Joachim Van der Auwera
 */
@Component
public class AttributeService {

	private final Logger log = LoggerFactory.getLogger(AttributeService.class);
	
	@Autowired
	private SecurityContext securityContext;
	
	@Autowired
	private FeatureExpressionService expressionService;

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

		Map<String, Attribute> featureAttributes = getRealAttributes(layer, featureBean);
		feature.setAttributes(featureAttributes); // to allow isAttributeReadable to see full object
		addSyntheticAttributes(feature, featureAttributes, layer);
		if (securityContext.isFeatureVisible(layerId, feature)) {

			feature.setAttributes(filterAttributes(layerId, layer.getLayerInfo().getFeatureInfo().getAttributesMap(),
					feature, featureAttributes));

			feature.setEditable(securityContext.isFeatureUpdateAuthorized(layerId, feature));
			feature.setDeletable(securityContext.isFeatureDeleteAuthorized(layerId, feature));

			return feature;
		}
		return null;
	}

	private Map<String, Attribute> filterAttributes(String layerId, Map<String, AbstractAttributeInfo> attributeInfo,
			InternalFeature feature, Map<String, Attribute> featureAttributes) {
		Map<String, Attribute> filteredAttributes = new HashMap<String, Attribute>();
		for (Map.Entry<String, Attribute> entry : featureAttributes.entrySet()) {
			String key = entry.getKey();
			if (securityContext.isAttributeReadable(layerId, feature, key)) {
				Attribute attribute = entry.getValue();
				boolean editable = false;
				AbstractAttributeInfo ai = attributeInfo.get(key);
				if (ai instanceof EditableAttributeInfo && ((EditableAttributeInfo) ai).isEditable()) {
					editable = securityContext.isAttributeWritable(layerId, feature, key);
				}
				setAttributeEditable(attribute, editable);
				filteredAttributes.put(key, attribute);
			}
		}
		return filteredAttributes;
	}

	/**
	 * Set editable state on an attribute. This needs to also set the state on the associated attributes.
	 *
	 * @param attribute attribute for which the editable state needs to be set
	 * @param editable new editable state
	 */
	public void setAttributeEditable(Attribute attribute, boolean editable) {
		attribute.setEditable(editable);
		if (!(attribute instanceof LazyAttribute)) { // should not instantiate lazy attributes!
			if (attribute instanceof ManyToOneAttribute) {
				setAttributeEditable(((ManyToOneAttribute) attribute).getValue(), editable);
			} else if (attribute instanceof OneToManyAttribute) {
				List<AssociationValue> values = ((OneToManyAttribute) attribute).getValue();
				for (AssociationValue value : values) {
					setAttributeEditable(value, editable);
				}
			}
		}
	}

	private void setAttributeEditable(AssociationValue association, boolean editable) {
		if (null != association) {
			for (Attribute attribute : association.getAllAttributes().values()) {
				setAttributeEditable(attribute, editable);
			}
		}
	}

	private Map<String, Attribute> getRealAttributes(VectorLayer layer, Object featureBean) throws LayerException {
		FeatureModel featureModel = layer.getFeatureModel();
		FeatureInfo featureInfo = layer.getLayerInfo().getFeatureInfo();
		String geometryAttributeName = featureInfo.getGeometryType().getName();
		boolean lazy = layer instanceof VectorLayerLazyFeatureConversionSupport &&
				((VectorLayerLazyFeatureConversionSupport) layer).useLazyFeatureConversion();

		Map<String, Attribute> attributes = new HashMap<String, Attribute>();
		for (AbstractAttributeInfo attribute : featureInfo.getAttributes()) {
			if (!(attribute instanceof SyntheticAttributeInfo)) {
				String name = attribute.getName();
				if (!name.equals(geometryAttributeName)) {
					Attribute value;
					if (lazy) {
						// need to use the correct lazy type to allow instanceof to work
						if (attribute instanceof AssociationAttributeInfo) {
							switch (((AssociationAttributeInfo) attribute).getType()) {
								case MANY_TO_ONE:
									value = new LazyManyToOneAttribute(this, featureModel, featureBean, name);
									break;
								case ONE_TO_MANY:
									value = new LazyOneToManyAttribute(this, featureModel, featureBean, name);
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
		}
		return attributes;
	}

	private void addSyntheticAttributes(InternalFeature feature, Map<String, Attribute> featureAttributes,
			VectorLayer layer) {
		FeatureInfo featureInfo = layer.getLayerInfo().getFeatureInfo();
		for (AbstractAttributeInfo attribute : featureInfo.getAttributes()) {
			if (attribute instanceof SyntheticAttributeInfo) {
				Attribute attributeObject;
				SyntheticAttributeInfo syntheticInfo = (SyntheticAttributeInfo) attribute;
				SyntheticAttributeBuilder builder = syntheticInfo.getAttributeBuilder();
				if (null != builder) {
					attributeObject = builder.getAttribute(syntheticInfo, feature);
				} else {
					Object value = null;
					try {
						value = expressionService.evaluate(syntheticInfo.getExpression(), feature);
					} catch (LayerException le) {
						log.error(le.getMessage(), le);
					}
					attributeObject = new StringAttribute((String) value);
				}
				featureAttributes.put(attribute.getName(), attributeObject);
			}
		}
	}

}
