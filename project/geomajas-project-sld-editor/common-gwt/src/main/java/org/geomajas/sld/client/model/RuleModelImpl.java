/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.sld.client.model;

import org.geomajas.sld.RuleInfo;
import org.geomajas.sld.SymbolizerTypeInfo;
import org.geomajas.sld.editor.client.GeometryType;
import org.geomajas.sld.editor.client.SldUtils;

/**
 * @author An Buyle
 * 
 */
public class RuleModelImpl implements RuleModel {

	private String name;

	private String title;

	private TypeOfRule typeOfRule;

	private RuleInfo ruleInfo;

	private FilterModel filterModel;

	private SymbolizerTypeInfo symbolizerTypeInfo;

	private GeometryType geometryType;

	public RuleModelImpl(RuleInfo ruleInfo) {
		this.ruleInfo = ruleInfo;
		this.geometryType = SldUtils.GetGeometryType(ruleInfo);
		this.typeOfRule = TypeOfRule.COMPLETE_RULE;
		this.symbolizerTypeInfo = ruleInfo.getSymbolizerList().get(0); // retrieve the first symbolizer specification
		if(ruleInfo.getChoice().ifFilter()) {
			this.filterModel = new FilterModelImpl(ruleInfo.getChoice().getFilter());
		} else {
			this.filterModel = new FilterModelImpl();
		}
		this.name = ruleInfo.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.sld.client.model.RuleModelIntf#getName()
	 */
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.sld.client.model.RuleModelIntf#setName(java.lang.String)
	 */
	public void setName(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.sld.client.model.RuleModelIntf#getTitle()
	 */
	public String getTitle() {
		return title;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.sld.client.model.RuleModelIntf#setTitle(java.lang.String)
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	public TypeOfRule getTypeOfRule() {
		return typeOfRule;
	}

	public void setTypeOfRule(TypeOfRule typeOfRule) {
		this.typeOfRule = typeOfRule;
	}

	public RuleInfo getRuleInfo() {
		return ruleInfo;
	}

	public FilterModel getFilterModel() {
		return filterModel;
	}

	public GeometryType getGeometryType() {
		return geometryType;
	}
	
	public SymbolizerTypeInfo getSymbolizerTypeInfo() {
		return symbolizerTypeInfo;
	}
	
	public void setSymbolizerTypeInfo(SymbolizerTypeInfo symbolizerTypeInfo) {
		this.symbolizerTypeInfo = symbolizerTypeInfo;
	}
	

}
