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

import java.util.ArrayList;
import java.util.List;

/**
 * @author An Buyle
 *
 */
public class RuleGroup {

	private String name;

	private String title;


	private List<RuleModel> ruleModelList = new ArrayList<RuleModel>(); // List of "rules" 

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
	 * Get the list of 'Rule' element items.
	 * 
	 * @return list
	 */
	public List<RuleModel> getRuleModelList() {
		return ruleModelList;
	}

	/**
	 * Set the list of 'Rule' element items.
	 * 
	 * @param list
	 */
	public void setRuleModelList(List<RuleModel> list) {
		ruleModelList = list;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return  this.getClass().getName() + "(name=" + this.getName() + ", title=" + this.getTitle()
					 + ", ruleModelList=" + this.getRuleModelList() + ")";
	}

}
