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

package org.geomajas.internal.layer.vector.lazy;

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
public class LazyManyToOneAttribute extends ManyToOneAttribute {

	private static final long serialVersionUID = 190L;

	private FeatureModel featureModel;
	private Object pojo;
	private String name;
	private boolean gotValue;

	public LazyManyToOneAttribute(FeatureModel featureModel, Object pojo, String attribute) {
		super();
		this.featureModel = featureModel;
		this.pojo = pojo;
		this.name = attribute;
	}

	@Override
	@SuppressWarnings("unchecked")
	public AssociationValue getValue() {
		if (!gotValue) {
			try {
				Attribute<AssociationValue> attribute = featureModel.getAttribute(pojo, name);
				super.setValue(attribute.getValue());
			} catch (LayerException le) {
				Logger log = LoggerFactory.getLogger(LazyPrimitiveAttribute.class);
				log.error("Could not lazily get attribute " + name, le);
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
		return new LazyManyToOneAttribute(featureModel, pojo, name);
	}

}
