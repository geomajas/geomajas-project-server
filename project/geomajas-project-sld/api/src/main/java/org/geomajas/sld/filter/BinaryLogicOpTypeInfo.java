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
 * @since 1.10.0
 */
@Api(allMethods = true)
public class BinaryLogicOpTypeInfo extends LogicOpsTypeInfo implements Serializable {

	private static final long serialVersionUID = 1100;

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

		private static final long serialVersionUID = 1100;

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
	}
}
