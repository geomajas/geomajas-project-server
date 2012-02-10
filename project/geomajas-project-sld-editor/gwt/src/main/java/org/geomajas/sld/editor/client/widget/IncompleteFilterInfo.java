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
package org.geomajas.sld.editor.client.widget;

/**
 * Contains SLD rule filter info in case the filter has not been fully specified.
 * 
 * @author An Buyle
 * 
 */
public class IncompleteFilterInfo {

	private String attributeName;

	private String operator;

	private String patternMatchingWildCard;

	private String patternMatchingSingleChar;

	private String patternMatchingEscape;

	public IncompleteFilterInfo(String attributeName, String operator) {
		this.attributeName = attributeName;
		this.operator = operator;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getPatternMatchingWildCard() {
		return patternMatchingWildCard;
	}

	public void setPatternMatchingWildCard(String patternMatchingWildCard) {
		this.patternMatchingWildCard = patternMatchingWildCard;
	}

	public String getPatternMatchingSingleChar() {
		return patternMatchingSingleChar;
	}

	public void setPatternMatchingSingleChar(String patternMatchingSingleChar) {
		this.patternMatchingSingleChar = patternMatchingSingleChar;
	}

	public String getPatternMatchingEscape() {
		return patternMatchingEscape;
	}

	public void setPatternMatchingEscape(String patternMatchingEscape) {
		this.patternMatchingEscape = patternMatchingEscape;
	}

	public String getOperator() {
		return operator;
	}

}
