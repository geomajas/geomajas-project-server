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
import java.util.ArrayList;
import java.util.List;
import org.geomajas.annotation.Api;

/**
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:complexType
 * xmlns:ns="http://www.opengis.net/ogc"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="FilterType">
 *   &lt;xs:choice>
 *     &lt;xs:element ref="ns:spatialOps"/>
 *     &lt;xs:element ref="ns:comparisonOps"/>
 *     &lt;xs:element ref="ns:logicOps"/>
 *     &lt;xs:element ref="ns:FeatureId" maxOccurs="unbounded"/>
 *   &lt;/xs:choice>
 * &lt;/xs:complexType>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.10.0
 */

@Api(allMethods = true)
public class FilterTypeInfo implements Serializable {

	private static final long serialVersionUID = 1100;

	private int choiceSelect = -1;

	private static final int SPATIAL_OPS_CHOICE = 0;

	private static final int COMPARISON_OPS_CHOICE = 1;

	private static final int LOGIC_OPS_CHOICE = 2;

	private static final int FEATURE_ID_LIST_CHOICE = 3;

	private SpatialOpsTypeInfo spatialOps;

	private ComparisonOpsTypeInfo comparisonOps;

	private LogicOpsTypeInfo logicOps;

	private List<FeatureIdTypeInfo> featureIdList = new ArrayList<FeatureIdTypeInfo>();

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
		spatialOps = null;
		comparisonOps = null;
		logicOps = null;
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

	/**
	 * Check if FeatureIdList is current selection for choice.
	 * 
	 * @return <code>true</code> if selection, <code>false</code> if not
	 */
	public boolean ifFeatureIdList() {
		return choiceSelect == FEATURE_ID_LIST_CHOICE;
	}

	/**
	 * Get the list of 'FeatureId' element items.
	 * 
	 * @return list
	 */
	public List<FeatureIdTypeInfo> getFeatureIdList() {
		return featureIdList;
	}

	/**
	 * Set the list of 'FeatureId' element items.
	 * 
	 * @param list
	 */
	public void setFeatureIdList(List<FeatureIdTypeInfo> list) {
		setChoiceSelect(FEATURE_ID_LIST_CHOICE);
		featureIdList = list;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "FilterTypeInfo(choiceSelect=" + this.choiceSelect + ", spatialOps=" + this.getSpatialOps()
				+ ", comparisonOps=" + this.getComparisonOps() + ", logicOps=" + this.getLogicOps()
				+ ", featureIdList=" + this.getFeatureIdList() + ")";
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof FilterTypeInfo)) {
			return false;
		}
		final FilterTypeInfo other = (FilterTypeInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (this.choiceSelect != other.choiceSelect) {
			return false;
		}
		if (this.getSpatialOps() == null ? other.getSpatialOps() != null : !this.getSpatialOps().equals(
				(java.lang.Object) other.getSpatialOps())) {
			return false;
		}
		if (this.getComparisonOps() == null ? other.getComparisonOps() != null : !this.getComparisonOps().equals(
				(java.lang.Object) other.getComparisonOps())) {
			return false;
		}
		if (this.getLogicOps() == null ? other.getLogicOps() != null : !this.getLogicOps().equals(
				(java.lang.Object) other.getLogicOps())) {
			return false;
		}
		if (this.getFeatureIdList() == null ? other.getFeatureIdList() != null : !this.getFeatureIdList().equals(
				(java.lang.Object) other.getFeatureIdList())) {
			return false;
		}
		return true;
	}

	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof FilterTypeInfo;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + this.choiceSelect;
		result = result * prime + (this.getSpatialOps() == null ? 0 : this.getSpatialOps().hashCode());
		result = result * prime + (this.getComparisonOps() == null ? 0 : this.getComparisonOps().hashCode());
		result = result * prime + (this.getLogicOps() == null ? 0 : this.getLogicOps().hashCode());
		result = result * prime + (this.getFeatureIdList() == null ? 0 : this.getFeatureIdList().hashCode());
		return result;
	}
}