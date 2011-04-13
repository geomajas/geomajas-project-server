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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.geomajas.configuration.AttributeInfo;
import org.geomajas.configuration.FeatureInfo;
import org.geomajas.layer.LayerException;
import org.geomajas.layer.feature.Attribute;
import org.geomajas.layer.feature.FeatureModel;

/**
 * Attribute map that supports lazy access of attributes. Attributes will only be accessed when explicitly requested.
 * 
 * @author Jan De Moerloose
 * 
 */
public class LazyAttributeMap implements Map<String, Attribute> {

	private FeatureModel featureModel;

	private Object feature;

	private Map<String, AttributeLoadInfo> loadInfos = new HashMap<String, LazyAttributeMap.AttributeLoadInfo>();

	private Map<String, Attribute> nonLazyAttributes;

	public LazyAttributeMap(FeatureModel featureModel, FeatureInfo featureInfo, Object feature) {
		this.featureModel = featureModel;
		this.feature = feature;
		for (AttributeInfo attrInfo : featureInfo.getAttributes()) {
			loadInfos.put(attrInfo.getName(), new AttributeLoadInfo(attrInfo.getName()));
		}
	}

	public void removeAttribute(String name) {
		loadInfos.remove(name);
		if (nonLazyAttributes != null) {
			nonLazyAttributes.remove(name);
		}
	}

	public void setAttributeEditable(String name, boolean editable) {
		loadInfos.get(name).setEditable(editable);
		if (nonLazyAttributes != null) {
			nonLazyAttributes.get(name).setEditable(editable);
		}
	}

	public int size() {
		return loadInfos.size();
	}

	public boolean isEmpty() {
		return loadInfos.size() != 0;
	}

	public void clear() {
		throw new UnsupportedOperationException();
	}

	public Set<String> keySet() {
		return loadInfos.keySet();
	}

	public Collection<Attribute> values() {
		return getNonLazyAttributes().values();
	}

	public Set<Entry<String, Attribute>> entrySet() {
		return getNonLazyAttributes().entrySet();
	}

	public Attribute put(String key, Attribute value) {
		throw new UnsupportedOperationException();
	}

	public void putAll(Map<? extends String, ? extends Attribute> m) {
		throw new UnsupportedOperationException();
	}

	public boolean containsKey(Object key) {
		return loadInfos.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return getNonLazyAttributes().containsValue(value);
	}

	public Attribute get(Object key) {
		try {
			if (loadInfos.containsKey(key)) {
				Attribute attr = featureModel.getAttribute(feature, (String) key);
				attr.setEditable(loadInfos.get(key).isEditable());
				return attr;
			} else {
				return null;
			}
		} catch (LayerException e) {
			// should not happen
			throw new IllegalArgumentException("Could not retrieve attribute from feature " + feature, e);
		}
	}

	public Attribute remove(Object key) {
		throw new UnsupportedOperationException();
	}

	private Map<String, Attribute> getNonLazyAttributes() {
		try {
			if (nonLazyAttributes == null) {
				nonLazyAttributes = new HashMap<String, Attribute>();
				for (AttributeLoadInfo loadInfo : loadInfos.values()) {
					Attribute attribute = featureModel.getAttribute(feature, loadInfo.getName());
					attribute.setEditable(loadInfo.isEditable());
					nonLazyAttributes.put(loadInfo.getName(), attribute);
				}
			}
			return nonLazyAttributes;
		} catch (LayerException e) {
			// should not happen
			throw new IllegalArgumentException("Could not retrieve attributes from feature " + feature, e);
		}
	}

	/**
	 * Keeps track of name/editable status for lazy attributes.
	 * 
	 * @author Jan De Moerloose
	 * 
	 */
	private class AttributeLoadInfo {

		private String name;

		private boolean editable;

		public AttributeLoadInfo(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public boolean isEditable() {
			return editable;
		}

		public void setEditable(boolean editable) {
			this.editable = editable;
		}

	}

}
