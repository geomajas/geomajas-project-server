package org.geomajas.sld.client.model;

import org.geomajas.sld.RuleInfo;
import org.geomajas.sld.SymbolizerTypeInfo;
import org.geomajas.sld.editor.client.GeometryType;

public interface RuleModel {

	/**
	 * @author An Buyle
	 * 
	 */
	public enum TypeOfRule {
		DEFAULT_RULE, INCOMPLETE_RULE, COMPLETE_RULE
	}

	/**
	 * Get the 'Name' element value.
	 * 
	 * @return value
	 */
	String getName();

	/**
	 * Set the 'Name' element value.
	 * 
	 * @param name
	 */
	void setName(String name);

	/**
	 * Get the 'Title' element value.
	 * 
	 * @return value
	 */
	String getTitle();

	/**
	 * Set the 'Title' element value.
	 * 
	 * @param title
	 */
	void setTitle(String title);

	TypeOfRule getTypeOfRule();

	void setTypeOfRule(TypeOfRule typeOfRule);

	RuleInfo getRuleInfo();

	FilterModel getFilterModel();

	SymbolizerTypeInfo getSymbolizerTypeInfo();

	GeometryType getGeometryType();

}