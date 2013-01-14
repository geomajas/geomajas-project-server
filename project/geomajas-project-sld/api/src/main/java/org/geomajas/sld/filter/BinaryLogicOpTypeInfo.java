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
import java.util.ArrayList;
import java.util.List;
import org.geomajas.annotation.Api;

/**
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:complexType
 * xmlns:ns="http://www.opengis.net/ogc"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="BinaryLogicOpType">
 *   &lt;xs:complexContent>
 *     &lt;xs:extension base="ns:LogicOpsType">
 *       &lt;xs:choice minOccurs="2" maxOccurs="unbounded">
 *         &lt;!-- Reference to inner class ChoiceInfo -->
 *       &lt;/xs:choice>
 *     &lt;/xs:extension>
 *   &lt;/xs:complexContent>
 * &lt;/xs:complexType>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public class BinaryLogicOpTypeInfo extends LogicOpsTypeInfo implements Serializable {

	private static final long serialVersionUID = 100;

	private List<ChoiceInfo> choiceList = new ArrayList<ChoiceInfo>();

	/**
	 * Get the list of choice items.
	 * 
	 * @return list
	 */
	public List<ChoiceInfo> getChoiceList() {
		return choiceList;
	}

	/**
	 * Set the list of choice items.
	 * 
	 * @param list
	 */
	public void setChoiceList(List<ChoiceInfo> list) {
		choiceList = list;
	}

	/**
	 * Schema fragment(s) for this class:...
	 * 
	 * <pre>
	 * &lt;xs:choice
	 * xmlns:ns="http://www.opengis.net/ogc"
	 * xmlns:xs="http://www.w3.org/2001/XMLSchema" minOccurs="2" maxOccurs="unbounded">
	 *   &lt;xs:element ref="ns:comparisonOps"/>
	 *   &lt;xs:element ref="ns:spatialOps"/>
	 *   &lt;xs:element ref="ns:logicOps"/>
	 * &lt;/xs:choice>
	 * </pre>
	 */
	public static class ChoiceInfo implements Serializable {

		private static final long serialVersionUID = 100;

		private int choiceListSelect = -1;

		private static final int COMPARISON_OPS_CHOICE = 0;

		private static final int SPATIAL_OPS_CHOICE = 1;

		private static final int LOGIC_OPS_CHOICE = 2;

		private ComparisonOpsTypeInfo comparisonOps;

		private SpatialOpsTypeInfo spatialOps;

		private LogicOpsTypeInfo logicOps;

		private void setChoiceListSelect(int choice) {
			if (choiceListSelect == -1) {
				choiceListSelect = choice;
			} else if (choiceListSelect != choice) {
				throw new IllegalStateException("Need to call clearChoiceListSelect() before changing existing choice");
			}
		}

		/**
		 * Clear the choice selection.
		 */
		public void clearChoiceListSelect() {
			choiceListSelect = -1;
			comparisonOps = null;
			spatialOps = null;
			logicOps = null;
		}

		/**
		 * Check if ComparisonOps is current selection for choice.
		 * 
		 * @return <code>true</code> if selection, <code>false</code> if not
		 */
		public boolean ifComparisonOps() {
			return choiceListSelect == COMPARISON_OPS_CHOICE;
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
			setChoiceListSelect(COMPARISON_OPS_CHOICE);
			this.comparisonOps = comparisonOps;
		}

		/**
		 * Check if SpatialOps is current selection for choice.
		 * 
		 * @return <code>true</code> if selection, <code>false</code> if not
		 */
		public boolean ifSpatialOps() {
			return choiceListSelect == SPATIAL_OPS_CHOICE;
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
			setChoiceListSelect(SPATIAL_OPS_CHOICE);
			this.spatialOps = spatialOps;
		}

		/**
		 * Check if LogicOps is current selection for choice.
		 * 
		 * @return <code>true</code> if selection, <code>false</code> if not
		 */
		public boolean ifLogicOps() {
			return choiceListSelect == LOGIC_OPS_CHOICE;
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
			setChoiceListSelect(LOGIC_OPS_CHOICE);
			this.logicOps = logicOps;
		}

		/** {@inheritDoc} */
		@java.lang.Override
		@java.lang.SuppressWarnings("all")
		public java.lang.String toString() {
			return "BinaryLogicOpTypeInfo.ChoiceInfo(choiceListSelect=" + this.choiceListSelect + ", comparisonOps="
					+ this.getComparisonOps() + ", spatialOps=" + this.getSpatialOps() + ", logicOps="
					+ this.getLogicOps() + ")";
		}

		/** {@inheritDoc} */
		@java.lang.Override
		@java.lang.SuppressWarnings("all")
		public boolean equals(final java.lang.Object o) {
			if (o == this) {
				return true;
			}
			if (!(o instanceof ChoiceInfo)) {
				return false;
			}
			final ChoiceInfo other = (ChoiceInfo) o;
			if (!other.canEqual((java.lang.Object) this)) {
				return false;
			}
			if (this.choiceListSelect != other.choiceListSelect) {
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
			return other instanceof ChoiceInfo;
		}

		/** {@inheritDoc} */
		@java.lang.Override
		@java.lang.SuppressWarnings("all")
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = result * prime + this.choiceListSelect;
			result = result * prime + (this.getComparisonOps() == null ? 0 : this.getComparisonOps().hashCode());
			result = result * prime + (this.getSpatialOps() == null ? 0 : this.getSpatialOps().hashCode());
			result = result * prime + (this.getLogicOps() == null ? 0 : this.getLogicOps().hashCode());
			return result;
		}
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "BinaryLogicOpTypeInfo(choiceList=" + this.getChoiceList() + ")";
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof BinaryLogicOpTypeInfo)) {
			return false;
		}
		final BinaryLogicOpTypeInfo other = (BinaryLogicOpTypeInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}
		if (this.getChoiceList() == null ? other.getChoiceList() != null : !this.getChoiceList().equals(
				(java.lang.Object) other.getChoiceList())) {
			return false;
		}
		return true;
	}

	/** {@inheritDoc} */
	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof BinaryLogicOpTypeInfo;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + super.hashCode();
		result = result * prime + (this.getChoiceList() == null ? 0 : this.getChoiceList().hashCode());
		return result;
	}
}