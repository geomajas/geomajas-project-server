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
package org.geomajas.sld.filter;

import java.io.Serializable;
import org.geomajas.annotation.Api;

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
 * @since 1.0.0
 */

@Api(allMethods = true)
public class UnaryLogicOpTypeInfo extends LogicOpsTypeInfo implements Serializable {

	private static final long serialVersionUID = 100;

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

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "UnaryLogicOpTypeInfo(choiceSelect=" + this.choiceSelect + ", comparisonOps=" + this.getComparisonOps()
				+ ", spatialOps=" + this.getSpatialOps() + ", logicOps=" + this.getLogicOps() + ")";
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof UnaryLogicOpTypeInfo)) {
			return false;
		}
		final UnaryLogicOpTypeInfo other = (UnaryLogicOpTypeInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}
		if (this.choiceSelect != other.choiceSelect) {
			return false;
		}
		if (this.getComparisonOps() == null ? other.getComparisonOps() != null : !this.getComparisonOps().equals(
				(java.lang.Object) other.getComparisonOps())) {
			return false;
		}
		if (this.getSpatialOps() == null ? other.getSpatialOps() != null : !this.getSpatialOps().equals(
				(java.lang.Object) other.getSpatialOps())) {
			return false;
		}
		if (this.getLogicOps() == null ? other.getLogicOps() != null : !this.getLogicOps().equals(
				(java.lang.Object) other.getLogicOps())) {
			return false;
		}
		return true;
	}

	/** {@inheritDoc} */
	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof UnaryLogicOpTypeInfo;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + super.hashCode();
		result = result * prime + this.choiceSelect;
		result = result * prime + (this.getComparisonOps() == null ? 0 : this.getComparisonOps().hashCode());
		result = result * prime + (this.getSpatialOps() == null ? 0 : this.getSpatialOps().hashCode());
		result = result * prime + (this.getLogicOps() == null ? 0 : this.getLogicOps().hashCode());
		return result;
	}
}