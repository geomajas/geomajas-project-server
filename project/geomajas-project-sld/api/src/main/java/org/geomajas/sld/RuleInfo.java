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
package org.geomajas.sld;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.geomajas.annotation.Api;
import org.geomajas.sld.filter.FilterTypeInfo;

/**
 * 
 A Rule is used to attach property/scale conditions to and group the individual symbolizers used for rendering.
 * 
 * 
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:element
 * xmlns:ns="http://www.opengis.net/sld"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="Rule">
 *   &lt;xs:complexType>
 *     &lt;xs:sequence>
 *       &lt;xs:element ref="ns:Name" minOccurs="0"/>
 *       &lt;xs:element ref="ns:Title" minOccurs="0"/>
 *       &lt;xs:element ref="ns:Abstract" minOccurs="0"/>
 *       &lt;xs:element ref="ns:LegendGraphic" minOccurs="0"/>
 *       &lt;xs:choice minOccurs="0">
 *         &lt;!-- Reference to inner class ChoiceInfo -->
 *       &lt;/xs:choice>
 *       &lt;xs:element ref="ns:MinScaleDenominator" minOccurs="0"/>
 *       &lt;xs:element ref="ns:MaxScaleDenominator" minOccurs="0"/>
 *       &lt;xs:element ref="ns:Symbolizer" maxOccurs="unbounded"/>
 *     &lt;/xs:sequence>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public class RuleInfo implements Serializable {

	private static final long serialVersionUID = 100;

	private String name;

	private String title;

	private AbstractInfo aAbstract;

	private LegendGraphicInfo legendGraphic;

	private ChoiceInfo choice;

	private MinScaleDenominatorInfo minScaleDenominator;

	private MaxScaleDenominatorInfo maxScaleDenominator;

	private List<SymbolizerTypeInfo> symbolizerList = new ArrayList<SymbolizerTypeInfo>();

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
	 * Get the 'Abstract' element value.
	 * 
	 * @return value
	 */
	public AbstractInfo getAbstract() {
		return aAbstract;
	}

	/**
	 * Set the 'Abstract' element value.
	 * 
	 * @param _abstract
	 */
	public void setAbstract(AbstractInfo aAbstract) {
		this.aAbstract = aAbstract;
	}

	/**
	 * Get the 'LegendGraphic' element value.
	 * 
	 * @return value
	 */
	public LegendGraphicInfo getLegendGraphic() {
		return legendGraphic;
	}

	/**
	 * Set the 'LegendGraphic' element value.
	 * 
	 * @param legendGraphic
	 */
	public void setLegendGraphic(LegendGraphicInfo legendGraphic) {
		this.legendGraphic = legendGraphic;
	}

	/**
	 * Get the choice value.
	 * 
	 * @return value
	 */
	public ChoiceInfo getChoice() {
		return choice;
	}

	/**
	 * Set the choice value.
	 * 
	 * @param choice
	 */
	public void setChoice(ChoiceInfo choice) {
		this.choice = choice;
	}

	/**
	 * Get the 'MinScaleDenominator' element value.
	 * 
	 * @return value
	 */
	public MinScaleDenominatorInfo getMinScaleDenominator() {
		return minScaleDenominator;
	}

	/**
	 * Set the 'MinScaleDenominator' element value.
	 * 
	 * @param minScaleDenominator
	 */
	public void setMinScaleDenominator(MinScaleDenominatorInfo minScaleDenominator) {
		this.minScaleDenominator = minScaleDenominator;
	}

	/**
	 * Get the 'MaxScaleDenominator' element value.
	 * 
	 * @return value
	 */
	public MaxScaleDenominatorInfo getMaxScaleDenominator() {
		return maxScaleDenominator;
	}

	/**
	 * Set the 'MaxScaleDenominator' element value.
	 * 
	 * @param maxScaleDenominator
	 */
	public void setMaxScaleDenominator(MaxScaleDenominatorInfo maxScaleDenominator) {
		this.maxScaleDenominator = maxScaleDenominator;
	}

	/**
	 * Get the list of 'Symbolizer' element items.
	 * 
	 * @return list
	 */
	public List<SymbolizerTypeInfo> getSymbolizerList() {
		return symbolizerList;
	}

	/**
	 * Set the list of 'Symbolizer' element items.
	 * 
	 * @param list
	 */
	public void setSymbolizerList(List<SymbolizerTypeInfo> list) {
		symbolizerList = list;
	}

	/**
	 * Schema fragment(s) for this class:...
	 * 
	 * <pre>
	 * &lt;xs:choice
	 * xmlns:ns="http://www.opengis.net/ogc"
	 * xmlns:ns1="http://www.opengis.net/sld"
	 * xmlns:xs="http://www.w3.org/2001/XMLSchema" minOccurs="0">
	 *   &lt;xs:element ref="ns:Filter"/>
	 *   &lt;xs:element ref="ns1:ElseFilter"/>
	 * &lt;/xs:choice>
	 * </pre>
	 */
	public static class ChoiceInfo implements Serializable {

		private static final long serialVersionUID = 100;

		private int choiceSelect = -1;

		private static final int FILTER_CHOICE = 0;

		private static final int ELSE_FILTER_CHOICE = 1;

		private FilterTypeInfo filter;

		private ElseFilterInfo elseFilter;

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
		 * Check if Filter is current selection for choice.
		 * 
		 * @return <code>true</code> if selection, <code>false</code> if not
		 */
		public boolean ifFilter() {
			return choiceSelect == FILTER_CHOICE;
		}

		/**
		 * Get the 'Filter' element value.
		 * 
		 * @return value
		 */
		public FilterTypeInfo getFilter() {
			return filter;
		}

		/**
		 * Set the 'Filter' element value.
		 * 
		 * @param filter
		 */
		public void setFilter(FilterTypeInfo filter) {
			setChoiceSelect(FILTER_CHOICE);
			this.filter = filter;
		}

		/**
		 * Check if ElseFilter is current selection for choice.
		 * 
		 * @return <code>true</code> if selection, <code>false</code> if not
		 */
		public boolean ifElseFilter() {
			return choiceSelect == ELSE_FILTER_CHOICE;
		}

		/**
		 * Get the 'ElseFilter' element value.
		 * 
		 * @return value
		 */
		public ElseFilterInfo getElseFilter() {
			return elseFilter;
		}

		/**
		 * Set the 'ElseFilter' element value.
		 * 
		 * @param elseFilter
		 */
		public void setElseFilter(ElseFilterInfo elseFilter) {
			setChoiceSelect(ELSE_FILTER_CHOICE);
			this.elseFilter = elseFilter;
		}

		@Override
		@java.lang.SuppressWarnings("all")
		public java.lang.String toString() {
			return "RuleInfo.ChoiceInfo(choiceSelect=" + this.choiceSelect + ", filter=" + this.getFilter()
					+ ", elseFilter=" + this.getElseFilter() + ")";
		}

		@Override
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
			if (this.choiceSelect != other.choiceSelect) {
				return false;
			}
			if (this.getFilter() == null ? other.getFilter() != null : !this.getFilter().equals(
					(java.lang.Object) other.getFilter())) {
				return false;
			}
			if (this.getElseFilter() == null ? other.getElseFilter() != null : !this.getElseFilter().equals(
					(java.lang.Object) other.getElseFilter())) {
				return false;
			}
			return true;
		}

		/**
		 * Is there a chance that the object are equal? Verifies that the other object has a comparable type.
		 *
		 * @param other other object
		 * @return true when other is an instance of this type
		 */
		public boolean canEqual(final java.lang.Object other) {
			return other instanceof ChoiceInfo;
		}

		@Override
		@java.lang.SuppressWarnings("all")
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = result * prime + this.choiceSelect;
			result = result * prime + (this.getFilter() == null ? 0 : this.getFilter().hashCode());
			result = result * prime + (this.getElseFilter() == null ? 0 : this.getElseFilter().hashCode());
			return result;
		}
	}

	@Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "RuleInfo(name=" + this.getName() + ", title=" + this.getTitle() + ", aAbstract=" + this.aAbstract
				+ ", legendGraphic=" + this.getLegendGraphic() + ", choice=" + this.getChoice()
				+ ", minScaleDenominator=" + this.getMinScaleDenominator() + ", maxScaleDenominator="
				+ this.getMaxScaleDenominator() + ", symbolizerList=" + this.getSymbolizerList() + ")";
	}

	@Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof RuleInfo)) {
			return false;
		}
		final RuleInfo other = (RuleInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (this.getName() == null ? other.getName() != null : !this.getName().equals(
				(java.lang.Object) other.getName())) {
			return false;
		}
		if (this.getTitle() == null ? other.getTitle() != null : !this.getTitle().equals(
				(java.lang.Object) other.getTitle())) {
			return false;
		}
		if (this.aAbstract == null ? other.aAbstract != null : !this.aAbstract
				.equals((java.lang.Object) other.aAbstract)) {
			return false;
		}
		if (this.getLegendGraphic() == null ? other.getLegendGraphic() != null : !this.getLegendGraphic().equals(
				(java.lang.Object) other.getLegendGraphic())) {
			return false;
		}
		if (this.getChoice() == null ? other.getChoice() != null : !this.getChoice().equals(
				(java.lang.Object) other.getChoice())) {
			return false;
		}
		if (this.getMinScaleDenominator() == null ? other.getMinScaleDenominator() != null : !this
				.getMinScaleDenominator().equals((java.lang.Object) other.getMinScaleDenominator())) {
			return false;
		}
		if (this.getMaxScaleDenominator() == null ? other.getMaxScaleDenominator() != null : !this
				.getMaxScaleDenominator().equals((java.lang.Object) other.getMaxScaleDenominator())) {
			return false;
		}
		if (this.getSymbolizerList() == null ? other.getSymbolizerList() != null : !this.getSymbolizerList().equals(
				(java.lang.Object) other.getSymbolizerList())) {
			return false;
		}
		return true;
	}

	/**
	 * Is there a chance that the object are equal? Verifies that the other object has a comparable type.
	 *
	 * @param other other object
	 * @return true when other is an instance of this type
	 */
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof RuleInfo;
	}

	@Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + (this.getName() == null ? 0 : this.getName().hashCode());
		result = result * prime + (this.getTitle() == null ? 0 : this.getTitle().hashCode());
		result = result * prime + (this.aAbstract == null ? 0 : this.aAbstract.hashCode());
		result = result * prime + (this.getLegendGraphic() == null ? 0 : this.getLegendGraphic().hashCode());
		result = result * prime + (this.getChoice() == null ? 0 : this.getChoice().hashCode());
		result = result * prime
				+ (this.getMinScaleDenominator() == null ? 0 : this.getMinScaleDenominator().hashCode());
		result = result * prime
				+ (this.getMaxScaleDenominator() == null ? 0 : this.getMaxScaleDenominator().hashCode());
		result = result * prime + (this.getSymbolizerList() == null ? 0 : this.getSymbolizerList().hashCode());
		return result;
	}
}