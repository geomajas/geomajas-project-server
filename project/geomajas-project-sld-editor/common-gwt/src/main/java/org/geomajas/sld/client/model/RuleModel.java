package org.geomajas.sld.client.model;

import org.geomajas.sld.RuleInfo;
import org.geomajas.sld.SymbolizerTypeInfo;
import org.geomajas.sld.editor.client.GeometryType;

/**
 * 
 * @author Jan De Moerloose
 * 
 */
public interface RuleModel {

	/**
	 * @author An Buyle
	 * @author Jan De Moerloose
	 * 
	 */
	public enum RuleModelState {
		INCOMPLETE, // missing some data
		COMPLETE // all data complete (dirty flag is kept at SldModel level)
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

	RuleModelState getState();

	RuleInfo getRuleInfo();

	RuleReference getReference();

	FilterModel getFilterModel();

	SymbolizerTypeInfo getSymbolizerTypeInfo();

	GeometryType getGeometryType();
	
	void checkState();

}