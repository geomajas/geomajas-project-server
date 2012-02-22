/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.sld.editor.common.client.model;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.sld.editor.common.client.GeometryType;

/**
 * @author An Buyle
 * 
 */
public class RuleGroupImpl implements RuleGroup {

	private String name;

	private String title;

	private GeometryType geomType;

	private List<RuleModel> ruleModelList = new ArrayList<RuleModel>(); // List of "rules"

	public RuleGroupImpl() {
		System.out.println();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.sld.client.model.RuleGroup#getName()
	 */
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.sld.client.model.RuleGroup#setName(java.lang.String)
	 */
	public void setName(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.sld.client.model.RuleGroup#getTitle()
	 */
	public String getTitle() {
		return title;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.sld.client.model.RuleGroup#setTitle(java.lang.String)
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.sld.client.model.RuleGroup#getRuleModelList()
	 */
	public List<RuleModel> getRuleModelList() {
		return ruleModelList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.sld.client.model.RuleGroup#setRuleModelList(java.util.List)
	 */
	public void setRuleModelList(List<RuleModel> list) {
		ruleModelList = list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.sld.client.model.RuleGroup#getGeomType()
	 */
	public GeometryType getGeomType() {
		return geomType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.sld.client.model.RuleGroup#setGeomType(org.geomajas.sld.editor.client.GeometryType)
	 */
	public void setGeomType(GeometryType geomType) {
		this.geomType = geomType;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return this.getClass().getName() + "(name=" + this.getName() + ", title=" + this.getTitle()
				+ ", ruleModelList=" + this.getRuleModelList() + ")";
	}

	public RuleModel findByReference(RuleReference reference) {
		RuleReferenceImpl impl = (RuleReferenceImpl) reference;
		if (impl.getIndex() >= 0 && impl.getIndex() < getRuleModelList().size()) {
			return getRuleModelList().get(impl.getIndex());
		} else {
			return null;
		}
	}

}
