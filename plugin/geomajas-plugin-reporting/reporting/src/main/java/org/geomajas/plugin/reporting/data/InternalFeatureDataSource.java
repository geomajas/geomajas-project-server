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
package org.geomajas.plugin.reporting.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRRewindableDataSource;

import org.geomajas.configuration.LabelStyleInfo;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.InternalFeature;

/**
 * Jasper Reports data source for a collection of internal features. Report fields should have the attribute names as
 * their name.
 *
 * @author Jan De Moerloose
 * @author Joachim Van der Auwera
 */
public class InternalFeatureDataSource implements JRRewindableDataSource {

	private List<InternalFeature> features;

	private Iterator<InternalFeature> iterator;

	private InternalFeature currentFeature;

	public InternalFeatureDataSource(Collection<InternalFeature> features) {
		this.features = new ArrayList<InternalFeature>(features);
	}

	public boolean next() {
		if (iterator == null) {
			iterator = features.iterator();
		}
		if (iterator.hasNext()) {
			currentFeature = iterator.next();
			return true;
		} else {
			return false;
		}
	}

	public Object getFieldValue(JRField field) throws JRException {
		if (currentFeature != null) {
			String fieldName = field.getName();
			if (LabelStyleInfo.ATTRIBUTE_NAME_ID.equals(fieldName)) {
				return currentFeature.getId();
			}
			Map<String, Attribute> attributes = currentFeature.getAttributes();
			if (null != attributes) {
				Attribute attribute = attributes.get(field.getName());
				if (null != attribute) {
					return attribute.getValue();
				}
			}
		}
		return null;
	}

	/**
	 * Moves to the position before the first element. Calling {@link #next()} after this will move to the first
	 * element.
	 */
	public void moveFirst() {
		iterator = features.iterator();
		currentFeature = null;
	}

}
