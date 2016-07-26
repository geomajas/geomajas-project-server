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

package org.geomajas.internal.layer.vector.lazy;

import java.util.List;

import org.geomajas.internal.layer.feature.AttributeService;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.FeatureModel;
import org.geomajas.layer.feature.attribute.AssociationValue;
import org.geomajas.layer.feature.attribute.OneToManyAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lazy variant of a {@link OneToManyAttribute}. The value is only converted at first use, not at instantiation.
 *
 * @author Joachim Van der Auwera
 */
public class LazyOneToManyAttribute extends OneToManyAttribute implements LazyAttribute<List<AssociationValue>> {

	private static final long serialVersionUID = 190L;

	private final AttributeService attributeService;
	private final FeatureModel featureModel;
	private final Object pojo;
	private final String name;
	private boolean gotValue;

	public LazyOneToManyAttribute(AttributeService attributeService, FeatureModel featureModel, Object pojo,
			String attribute) {
		super();
		this.attributeService = attributeService;
		this.featureModel = featureModel;
		this.pojo = pojo;
		this.name = attribute;
	}

	@SuppressWarnings("unchecked")
	public Attribute<List<AssociationValue>> instantiate() {
		Attribute<List<AssociationValue>> attribute = new OneToManyAttribute(getValue());
		attributeService.setAttributeEditable(attribute, isEditable());
		return attribute;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<AssociationValue> getValue() {
		if (!gotValue) {
			try {
				Attribute<List<AssociationValue>> attribute = featureModel.getAttribute(pojo, name);
				attributeService.setAttributeEditable(attribute, isEditable());
				super.setValue(attribute.getValue());
			} catch (LayerException le) {
				Logger log = LoggerFactory.getLogger(LazyOneToManyAttribute.class);
				log.error("Could not lazily get attribute " + name, le);
			} catch (ClassCastException cce) {
				Logger log = LoggerFactory.getLogger(LazyOneToManyAttribute.class);
				log.error("Could not lazily get attribute " + name, cce);
			}
		}
		return super.getValue();
	}

	@Override
	public void setValue(List<AssociationValue> value) {
		gotValue = true;
		super.setValue(value);
	}

	@Override
	public LazyOneToManyAttribute clone() { // NOSONAR
		return new LazyOneToManyAttribute(attributeService, featureModel, pojo, name);
	}

}
