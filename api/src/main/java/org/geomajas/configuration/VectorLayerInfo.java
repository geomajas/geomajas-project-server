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
package org.geomajas.configuration;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.geomajas.annotation.Api;

/**
 * Information about a vector layer.
 * 
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class VectorLayerInfo extends LayerInfo implements IsInfo {

	private static final long serialVersionUID = 151L;

	private String filter;

	@NotNull
	private FeatureInfo featureInfo;

	private List<NamedStyleInfo> namedStyleInfos = new ArrayList<NamedStyleInfo>();
	
	private boolean allowEmptyGeometries;

	/**
	 * Get filter which needs to be applied to all queries on this layer.
	 *
	 * @return layer filter
	 */
	public String getFilter() {
		return filter;
	}

	/**
	 * Set filter which needs to be applied to all queries on this layer.
	 *
	 * @param filter layer filter
	 */
	public void setFilter(String filter) {
		this.filter = filter;
	}

	/**
	 * Get feature info, the configuration of the features for this layer.
	 *
	 * @return feature info
	 */
	public FeatureInfo getFeatureInfo() {
		return featureInfo;
	}

	/**
	 * Set feature info, the configuration of the features for this layer.
	 *
	 * @param featureInfo feature info
	 */
	public void setFeatureInfo(FeatureInfo featureInfo) {
		this.featureInfo = featureInfo;
	}
	

	/**
	 * Get possible styles which may be applied for this layer.
	 *
	 * @return possible layer styles
	 */
	public List<NamedStyleInfo> getNamedStyleInfos() {
		return namedStyleInfos;
	}

	/**
	 * Set possible styles which may be applied for this layer.
	 *
	 * @param namedStyleInfos possible layer styles
	 */
	public void setNamedStyleInfos(List<NamedStyleInfo> namedStyleInfos) {
		this.namedStyleInfos = namedStyleInfos;
	}

	/**
	 * Get layer style by name.
	 *
	 * @param name layer style name
	 * @return layer style
	 */
	public NamedStyleInfo getNamedStyleInfo(String name) {
		for (NamedStyleInfo info : namedStyleInfos) {
			if (info.getName().equals(name)) {
				return info;
			}
		}
		return null;
	}

	/**
	 * Is it allowed to create empty geometries? Default value is false.
	 * <p/>
	 * <b>Note: </b> empty geometries are not possible for {@link org.geomajas.layer.LayerType#POINT} layers.
	 * <p/>
	 * <b>Note: </b> empty geometries are insecure. This disables all security filtering on areas.
	 * 
	 * @return true when empty geometries are allowed
	 * @since 1.10.0
	 */
	public boolean isAllowEmptyGeometries() {
		return allowEmptyGeometries;
	}

	/**
	 * Is it allowed to create empty geometries? Default value is false.
	 * <p/>
	 * <b>Note: </b> empty geometries are not possible for {@link org.geomajas.layer.LayerType#POINT} layers.
	 * <p/>
	 * <b>Note: </b> empty geometries are insecure. This disables all security filtering on areas.
	 *
	 * @param allowEmptyGeometries true when empty geometries are allowed
	 * @since 1.10.0
	 */
	public void setAllowEmptyGeometries(boolean allowEmptyGeometries) {
		this.allowEmptyGeometries = allowEmptyGeometries;
	}
}
