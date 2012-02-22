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

import java.util.List;

import org.geomajas.sld.editor.common.client.GeometryType;

/**
 * @author An Buyle
 * 
 */
public interface RuleGroup {

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

	/**
	 * Get the list of 'Rule' element items.
	 * 
	 * @return list
	 */
	List<RuleModel> getRuleModelList();

	/**
	 * Set the list of 'Rule' element items.
	 * 
	 * @param list
	 */
	void setRuleModelList(List<RuleModel> list);

	GeometryType getGeomType();

	void setGeomType(GeometryType geomType);

	RuleModel findByReference(RuleReference reference);

}
