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
package org.geomajas.sld;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.geomajas.annotation.Api;

/**
 * 
 LayerFeatureConstraints define what features &amp; feature types are referenced in a layer.
 * 
 * 
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:element
 * xmlns:ns="http://www.opengis.net/sld" 
 *
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="LayerFeatureConstraints">
 *   &lt;xs:complexType>
 *     &lt;xs:sequence>
 *       &lt;xs:element ref="ns:FeatureTypeConstraint" maxOccurs="unbounded"/>
 *     &lt;/xs:sequence>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 *
 * @author Jan De Moerloose
 * @since 1.10.0
 */
@Api(allMethods = true)
public class LayerFeatureConstraintsInfo implements Serializable {

	private static final long serialVersionUID = 1100;

	private List<FeatureTypeConstraintInfo> featureTypeConstraintList = new ArrayList<FeatureTypeConstraintInfo>();

	/**
	 * Get the list of 'FeatureTypeConstraint' element items.
	 * 
	 * @return list
	 */
	public List<FeatureTypeConstraintInfo> getFeatureTypeConstraintList() {
		return featureTypeConstraintList;
	}

	/**
	 * Set the list of 'FeatureTypeConstraint' element items.
	 * 
	 * @param list
	 */
	public void setFeatureTypeConstraintList(List<FeatureTypeConstraintInfo> list) {
		featureTypeConstraintList = list;
	}
}
