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

package org.geomajas.internal.layer.vector.lazy;

import org.geomajas.internal.layer.feature.AttributeService;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.FeatureModel;
import org.geomajas.layer.feature.attribute.AssociationValue;
import org.geomajas.layer.feature.attribute.ManyToOneAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lazy variant of a {@link ManyToOneAttribute}. The value is only converted at first use, not at instantiation.
 *
 * @author Joachim Van der Auwera
 */
public class LazyManyToOneAttribute extends ManyToOneAttribute implements LazyAttribute<AssociationValue> {

	private static final long serialVersionUID = 190L;

	private final AttributeService attributeService;
	private final FeatureModel featureModel;
	private final Object pojo;
	private final String name;
	private boolean gotValue;

	public LazyManyToOneAttribute(AttributeService attributeService, FeatureModel featureModel, Object pojo,
			String attribute) {
		super();
		this.attributeService = attributeService;
		this.featureModel = featureModel;
		this.pojo = pojo;
		this.name = attribute;
	}

	@SuppressWarnings("unchecked")
	public Attribute<AssociationValue> instantiate() {
		Attribute<AssociationValue> attribute = new ManyToOneAttribute(getValue());
		attributeService.setAttributeEditable(attribute, isEditable());
		return attribute;
	}

	@Override
	@SuppressWarnings("unchecked")
	public AssociationValue getValue() {
		if (!gotValue) {
			try {
				Attribute<AssociationValue> attribute = featureModel.getAttribute(pojo, name);
				attributeService.setAttributeEditable(attribute, isEditable());
				super.setValue(attribute.getValue());
			} catch (LayerException le) {
				Logger log = LoggerFactory.getLogger(LazyManyToOneAttribute.class);
				log.error("Could not lazily get attribute " + name, le);
			} catch (ClassCastException cce) {
				Logger log = LoggerFactory.getLogger(LazyManyToOneAttribute.class);
				log.error("Could not lazily get attribute " + name, cce);
			}
		}
		return super.getValue();
	}

	@Override
	public void setValue(AssociationValue value) {
		gotValue = true;
		super.setValue(value);
	}

	@Override
	public LazyManyToOneAttribute clone() { // NOSONAR
		return new LazyManyToOneAttribute(attributeService, featureModel, pojo, name);
	}

}
