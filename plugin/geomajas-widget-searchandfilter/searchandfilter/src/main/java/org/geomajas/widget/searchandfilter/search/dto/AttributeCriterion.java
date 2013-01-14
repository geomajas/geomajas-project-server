/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.widget.searchandfilter.search.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Criterion with attribute criteria.
 * 
 * @author Kristof Heirwegh
 */
public class AttributeCriterion implements Criterion {

	private static final long serialVersionUID = 100L;

	private String serverLayerId;

	private String attributeName;

	private String displayText;

	public AttributeCriterion() {}

	public AttributeCriterion(String serverLayerId, String attributeName, String operator, String value) {
		this.serverLayerId = serverLayerId;
		this.attributeName = attributeName;
		this.operator = operator;
		this.value = value;
	}

	private static final String OPERATORS = ",<,<=,=,<>,>,=>,LIKE,DURING,BEFORE,AFTER,";
	/**
	 * <, <=, =, <>, >=, >, like, during, before, after.
	 */
	private String operator;

	private String value;

	// ----------------------------------------------------------

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public String getDisplayText() {
		return displayText;
	}
	
	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}

	public String getServerLayerId() {
		return serverLayerId;
	}

	public void setServerLayerId(String serverLayerId) {
		this.serverLayerId = serverLayerId;
	}

	/** {@inheritDoc} */
	public boolean isValid() {
		return (serverLayerId != null && attributeName != null && isValidOperator() && value != null);
	}

	public boolean isValidOperator() {
		return operator != null &&
				OPERATORS.contains("," + operator.toUpperCase() + ",") && operator.indexOf(',') < 0;
	}

	/** {@inheritDoc} */
	public void serverLayerIdVisitor(Set<String> layerIds) {
		layerIds.add(serverLayerId);
	}
	
	/** {@inheritDoc} */
	public List<Criterion> getCriteria() {
		return new ArrayList<Criterion>();
	}

	@Override
	public String toString() {
		return "(" + attributeName + " " + operator + " " + value + ")";
	}
}
