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
package org.geomajas.sld.filter;

import java.io.Serializable;

import org.geomajas.annotations.Api;

/**
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:complexType
 * xmlns:ns="http://www.opengis.net/ogc"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="UnaryLogicOpType">
 *   &lt;xs:complexContent>
 *     &lt;xs:extension base="ns:LogicOpsType">
 *       &lt;xs:sequence>
 *         &lt;xs:choice>
 *           &lt;xs:element ref="ns:comparisonOps"/>
 *           &lt;xs:element ref="ns:spatialOps"/>
 *           &lt;xs:element ref="ns:logicOps"/>
 *         &lt;/xs:choice>
 *       &lt;/xs:sequence>
 *     &lt;/xs:extension>
 *   &lt;/xs:complexContent>
 * &lt;/xs:complexType>
 * </pre>
 *
 * @author Jan De Moerloose
 * @since 1.10.0
 */
@Api(allMethods = true)
public class UnaryLogicOpTypeInfo extends LogicOpsTypeInfo implements Serializable {

	private static final long serialVersionUID = 1100;

	private int choiceSelect = -1;

	private static final int COMPARISON_OPS_CHOICE = 0;

	private static final int SPATIAL_OPS_CHOICE = 1;

	private static final int LOGIC_OPS_CHOICE = 2;

	private ComparisonOpsTypeInfo comparisonOps;

	private SpatialOpsTypeInfo spatialOps;

	private LogicOpsTypeInfo logicOps;

	private void setChoiceSelect(int choice) {
		if (choiceSelect == -1) {
			choiceSelect = choice;
		} else if (choiceSelect != choice) {
			throw new IllegalStateException("Need to call clearChoiceSelect() before changing existing choice");
		}
	}

	/**
	 * Clear the choice selection.
	 */
	public void clearChoiceSelect() {
		choiceSelect = -1;
	}

	/**
	 * Check if ComparisonOps is current selection for choice.
	 * 
	 * @return <code>true</code> if selection, <code>false</code> if not
	 */
	public boolean ifComparisonOps() {
		return choiceSelect == COMPARISON_OPS_CHOICE;
	}

	/**
	 * Get the 'comparisonOps' element value.
	 * 
	 * @return value
	 */
	public ComparisonOpsTypeInfo getComparisonOps() {
		return comparisonOps;
	}

	/**
	 * Set the 'comparisonOps' element value.
	 * 
	 * @param comparisonOps
	 */
	public void setComparisonOps(ComparisonOpsTypeInfo comparisonOps) {
		setChoiceSelect(COMPARISON_OPS_CHOICE);
		this.comparisonOps = comparisonOps;
	}

	/**
	 * Check if SpatialOps is current selection for choice.
	 * 
	 * @return <code>true</code> if selection, <code>false</code> if not
	 */
	public boolean ifSpatialOps() {
		return choiceSelect == SPATIAL_OPS_CHOICE;
	}

	/**
	 * Get the 'spatialOps' element value.
	 * 
	 * @return value
	 */
	public SpatialOpsTypeInfo getSpatialOps() {
		return spatialOps;
	}

	/**
	 * Set the 'spatialOps' element value.
	 * 
	 * @param spatialOps
	 */
	public void setSpatialOps(SpatialOpsTypeInfo spatialOps) {
		setChoiceSelect(SPATIAL_OPS_CHOICE);
		this.spatialOps = spatialOps;
	}

	/**
	 * Check if LogicOps is current selection for choice.
	 * 
	 * @return <code>true</code> if selection, <code>false</code> if not
	 */
	public boolean ifLogicOps() {
		return choiceSelect == LOGIC_OPS_CHOICE;
	}

	/**
	 * Get the 'logicOps' element value.
	 * 
	 * @return value
	 */
	public LogicOpsTypeInfo getLogicOps() {
		return logicOps;
	}

	/**
	 * Set the 'logicOps' element value.
	 * 
	 * @param logicOps
	 */
	public void setLogicOps(LogicOpsTypeInfo logicOps) {
		setChoiceSelect(LOGIC_OPS_CHOICE);
		this.logicOps = logicOps;
	}
}
