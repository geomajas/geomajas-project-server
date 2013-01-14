/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
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
 * @since 1.0.0
 */

@Api(allMethods = true)
public class FeatureTypeStyleInfo implements Serializable {

	private static final long serialVersionUID = 100;

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

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "FeatureTypeStyleInfo(name=" + this.getName() + ", title=" + this.getTitle() + ", aAbstract="
				+ this.aAbstract + ", featureTypeName=" + this.getFeatureTypeName() + ", semanticTypeIdentifierList="
				+ this.getSemanticTypeIdentifierList() + ", ruleList=" + this.getRuleList() + ")";
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof FeatureTypeStyleInfo)) {
			return false;
		}
		final FeatureTypeStyleInfo other = (FeatureTypeStyleInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (this.getName() == null ? other.getName() != null : !this.getName().equals(
				(java.lang.Object) other.getName())) {
			return false;
		}
		if (this.getTitle() == null ? other.getTitle() != null : !this.getTitle().equals(
				(java.lang.Object) other.getTitle())) {
			return false;
		}
		if (this.aAbstract == null ? other.aAbstract != null : !this.aAbstract
				.equals((java.lang.Object) other.aAbstract)) {
			return false;
		}
		if (this.getFeatureTypeName() == null ? other.getFeatureTypeName() != null : !this.getFeatureTypeName().equals(
				(java.lang.Object) other.getFeatureTypeName())) {
			return false;
		}
		if (this.getSemanticTypeIdentifierList() == null ? other.getSemanticTypeIdentifierList() != null : !this
				.getSemanticTypeIdentifierList().equals((java.lang.Object) other.getSemanticTypeIdentifierList())) {
			return false;
		}
		if (this.getRuleList() == null ? other.getRuleList() != null : !this.getRuleList().equals(
				(java.lang.Object) other.getRuleList())) {
			return false;
		}
		return true;
	}

	/** {@inheritDoc} */
	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof FeatureTypeStyleInfo;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + (this.getName() == null ? 0 : this.getName().hashCode());
		result = result * prime + (this.getTitle() == null ? 0 : this.getTitle().hashCode());
		result = result * prime + (this.aAbstract == null ? 0 : this.aAbstract.hashCode());
		result = result * prime + (this.getFeatureTypeName() == null ? 0 : this.getFeatureTypeName().hashCode());
		result = result * prime
				+ (this.getSemanticTypeIdentifierList() == null ? 0 : this.getSemanticTypeIdentifierList().hashCode());
		result = result * prime + (this.getRuleList() == null ? 0 : this.getRuleList().hashCode());
		return result;
	}
}