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
package org.geomajas.sld;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.geomajas.annotation.Api;

/**
 * 
 A NamedLayer is a layer of data that has a name advertised by a WMS.
 * 
 * 
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:element
 * xmlns:ns="http://www.opengis.net/sld"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="NamedLayer">
 *   &lt;xs:complexType>
 *     &lt;xs:sequence>
 *       &lt;xs:element ref="ns:Name"/>
 *       &lt;xs:element ref="ns:LayerFeatureConstraints" minOccurs="0"/>
 *       &lt;xs:choice minOccurs="0" maxOccurs="unbounded">
 *         &lt;!-- Reference to inner class ChoiceInfo -->
 *       &lt;/xs:choice>
 *     &lt;/xs:sequence>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public class NamedLayerInfo implements Serializable {

	private static final long serialVersionUID = 100;

	private String name;

	private LayerFeatureConstraintsInfo layerFeatureConstraints;

	private List<ChoiceInfo> choiceList = new ArrayList<ChoiceInfo>();

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
	 * Get the 'LayerFeatureConstraints' element value.
	 * 
	 * @return value
	 */
	public LayerFeatureConstraintsInfo getLayerFeatureConstraints() {
		return layerFeatureConstraints;
	}

	/**
	 * Set the 'LayerFeatureConstraints' element value.
	 * 
	 * @param layerFeatureConstraints
	 */
	public void setLayerFeatureConstraints(LayerFeatureConstraintsInfo layerFeatureConstraints) {
		this.layerFeatureConstraints = layerFeatureConstraints;
	}

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
	 * xmlns:ns="http://www.opengis.net/sld"
	 * xmlns:xs="http://www.w3.org/2001/XMLSchema" minOccurs="0" maxOccurs="unbounded">
	 *   &lt;xs:element ref="ns:NamedStyle"/>
	 *   &lt;xs:element ref="ns:UserStyle"/>
	 * &lt;/xs:choice>
	 * </pre>
	 */
	public static class ChoiceInfo implements Serializable {

		private static final long serialVersionUID = 100;

		private int choiceListSelect = -1;

		private static final int NAMED_STYLE_CHOICE = 0;

		private static final int USER_STYLE_CHOICE = 1;

		private NamedStyleInfo namedStyle;

		private UserStyleInfo userStyle;

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
			namedStyle = null;
			userStyle = null;
		}

		/**
		 * Check if NamedStyle is current selection for choice.
		 * 
		 * @return <code>true</code> if selection, <code>false</code> if not
		 */
		public boolean ifNamedStyle() {
			return choiceListSelect == NAMED_STYLE_CHOICE;
		}

		/**
		 * Get the 'NamedStyle' element value.
		 * 
		 * @return value
		 */
		public NamedStyleInfo getNamedStyle() {
			return namedStyle;
		}

		/**
		 * Set the 'NamedStyle' element value.
		 * 
		 * @param namedStyle
		 */
		public void setNamedStyle(NamedStyleInfo namedStyle) {
			setChoiceListSelect(NAMED_STYLE_CHOICE);
			this.namedStyle = namedStyle;
		}

		/**
		 * Check if UserStyle is current selection for choice.
		 * 
		 * @return <code>true</code> if selection, <code>false</code> if not
		 */
		public boolean ifUserStyle() {
			return choiceListSelect == USER_STYLE_CHOICE;
		}

		/**
		 * Get the 'UserStyle' element value.
		 * 
		 * @return value
		 */
		public UserStyleInfo getUserStyle() {
			return userStyle;
		}

		/**
		 * Set the 'UserStyle' element value.
		 * 
		 * @param userStyle
		 */
		public void setUserStyle(UserStyleInfo userStyle) {
			setChoiceListSelect(USER_STYLE_CHOICE);
			this.userStyle = userStyle;
		}

		@java.lang.Override
		@java.lang.SuppressWarnings("all")
		public java.lang.String toString() {
			return "NamedLayerInfo.ChoiceInfo(choiceListSelect=" + this.choiceListSelect + ", namedStyle="
					+ this.getNamedStyle() + ", userStyle=" + this.getUserStyle() + ")";
		}

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
			if (this.getNamedStyle() == null ? other.getNamedStyle() != null : !this.getNamedStyle().equals(
					(java.lang.Object) other.getNamedStyle())) {
				return false;
			}
			if (this.getUserStyle() == null ? other.getUserStyle() != null : !this.getUserStyle().equals(
					(java.lang.Object) other.getUserStyle())) {
				return false;
			}
			return true;
		}

		@java.lang.SuppressWarnings("all")
		public boolean canEqual(final java.lang.Object other) {
			return other instanceof ChoiceInfo;
		}

		@java.lang.Override
		@java.lang.SuppressWarnings("all")
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = result * prime + this.choiceListSelect;
			result = result * prime + (this.getNamedStyle() == null ? 0 : this.getNamedStyle().hashCode());
			result = result * prime + (this.getUserStyle() == null ? 0 : this.getUserStyle().hashCode());
			return result;
		}
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "NamedLayerInfo(name=" + this.getName() + ", layerFeatureConstraints="
				+ this.getLayerFeatureConstraints() + ", choiceList=" + this.getChoiceList() + ")";
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof NamedLayerInfo)) {
			return false;
		}
		final NamedLayerInfo other = (NamedLayerInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (this.getName() == null ? other.getName() != null : !this.getName().equals(
				(java.lang.Object) other.getName())) {
			return false;
		}
		if (this.getLayerFeatureConstraints() == null ? other.getLayerFeatureConstraints() != null : !this
				.getLayerFeatureConstraints().equals((java.lang.Object) other.getLayerFeatureConstraints())) {
			return false;
		}
		if (this.getChoiceList() == null ? other.getChoiceList() != null : !this.getChoiceList().equals(
				(java.lang.Object) other.getChoiceList())) {
			return false;
		}
		return true;
	}

	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof NamedLayerInfo;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + (this.getName() == null ? 0 : this.getName().hashCode());
		result = result * prime
				+ (this.getLayerFeatureConstraints() == null ? 0 : this.getLayerFeatureConstraints().hashCode());
		result = result * prime + (this.getChoiceList() == null ? 0 : this.getChoiceList().hashCode());
		return result;
	}
}