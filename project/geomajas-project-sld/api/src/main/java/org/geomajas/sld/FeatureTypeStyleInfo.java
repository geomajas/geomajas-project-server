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

import org.geomajas.annotations.Api;

/**
 * 
 A FeatureTypeStyle contains styling information specific to one feature type. This is the SLD level that separates
 * the 'layer' handling from the 'feature' handling.
 * 
 * 
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:element
 *
 * xmlns:ns="http://www.opengis.net/sld"	
 *
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="FeatureTypeStyle">
 *   &lt;xs:complexType>
 *     &lt;xs:sequence>
 *       &lt;xs:element ref="ns:Name" minOccurs="0"/>
 *       &lt;xs:element ref="ns:Title" minOccurs="0"/>
 *       &lt;xs:element ref="ns:Abstract" minOccurs="0"/>
 *       &lt;xs:element ref="ns:FeatureTypeName" minOccurs="0"/>
 *       &lt;xs:element ref="ns:SemanticTypeIdentifier" minOccurs="0" maxOccurs="unbounded"/>
 *       &lt;xs:element ref="ns:Rule" maxOccurs="unbounded"/>
 *     &lt;/xs:sequence>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 *
 * @author Jan De Moerloose
 * @since 1.10.0
 */
@Api(allMethods = true)
public class FeatureTypeStyleInfo implements Serializable {

	private static final long serialVersionUID = 1100;

	private String name;

	private String title;

	private AbstractInfo aAbstract;

	private FeatureTypeNameInfo featureTypeName;

	private List<SemanticTypeIdentifierInfo> semanticTypeIdentifierList = new ArrayList<SemanticTypeIdentifierInfo>();

	private List<RuleInfo> ruleList = new ArrayList<RuleInfo>();

	/**
	 * Get the 'Name' element value.
	 * 
	 * @return value
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the 'Name' element value.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get the 'Title' element value.
	 * 
	 * @return value
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Set the 'Title' element value.
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Get the 'Abstract' element value.
	 * 
	 * @return value
	 */
	public AbstractInfo getAbstract() {
		return aAbstract;
	}

	/**
	 * Set the 'Abstract' element value.
	 * 
	 * @param _abstract
	 */
	public void setAbstract(AbstractInfo aAbstract) {
		this.aAbstract = aAbstract;
	}

	/**
	 * Get the 'FeatureTypeName' element value.
	 * 
	 * @return value
	 */
	public FeatureTypeNameInfo getFeatureTypeName() {
		return featureTypeName;
	}

	/**
	 * Set the 'FeatureTypeName' element value.
	 * 
	 * @param featureTypeName
	 */
	public void setFeatureTypeName(FeatureTypeNameInfo featureTypeName) {
		this.featureTypeName = featureTypeName;
	}

	/**
	 * Get the list of 'SemanticTypeIdentifier' element items.
	 * 
	 * @return list
	 */
	public List<SemanticTypeIdentifierInfo> getSemanticTypeIdentifierList() {
		return semanticTypeIdentifierList;
	}

	/**
	 * Set the list of 'SemanticTypeIdentifier' element items.
	 * 
	 * @param list
	 */
	public void setSemanticTypeIdentifierList(List<SemanticTypeIdentifierInfo> list) {
		semanticTypeIdentifierList = list;
	}

	/**
	 * Get the list of 'Rule' element items.
	 * 
	 * @return list
	 */
	public List<RuleInfo> getRuleList() {
		return ruleList;
	}

	/**
	 * Set the list of 'Rule' element items.
	 * 
	 * @param list
	 */
	public void setRuleList(List<RuleInfo> list) {
		ruleList = list;
	}
}
