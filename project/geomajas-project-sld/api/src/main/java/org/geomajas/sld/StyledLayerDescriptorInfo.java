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
 A StyledLayerDescriptor is a sequence of styled layers, represented at the first level by NamedLayer and UserLayer
 * elements.
 * 
 * 
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:element
 * xmlns:ns="http://www.opengis.net/sld"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="StyledLayerDescriptor">
 *   &lt;xs:complexType>
 *     &lt;xs:sequence>
 *       &lt;xs:element ref="ns:Name" minOccurs="0"/>
 *       &lt;xs:element ref="ns:Title" minOccurs="0"/>
 *       &lt;xs:element ref="ns:Abstract" minOccurs="0"/>
 *       &lt;xs:choice minOccurs="0" maxOccurs="unbounded">
 *         &lt;!-- Reference to inner class ChoiceInfo -->
 *       &lt;/xs:choice>
 *     &lt;/xs:sequence>
 *     &lt;xs:attribute type="xs:string" use="required" fixed="1.0.0" name="version"/>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public class StyledLayerDescriptorInfo implements Serializable {

	private static final long serialVersionUID = 100;

	private String name;

	private String title;

	private AbstractInfo aAbstract;

	private List<ChoiceInfo> choiceList = new ArrayList<ChoiceInfo>();

	private String version;

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
	 * Get the 'version' attribute value.
	 * 
	 * @return value
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Set the 'version' attribute value.
	 * 
	 * @param version
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * Schema fragment(s) for this class:...
	 * 
	 * <pre>
	 * &lt;xs:choice
	 * xmlns:ns="http://www.opengis.net/sld"
	 * xmlns:xs="http://www.w3.org/2001/XMLSchema" minOccurs="0" maxOccurs="unbounded">
	 *   &lt;xs:element ref="ns:NamedLayer"/>
	 *   &lt;xs:element ref="ns:UserLayer"/>
	 * &lt;/xs:choice>
	 * </pre>
	 */
	public static class ChoiceInfo implements Serializable {

		private static final long serialVersionUID = 100;

		private int choiceListSelect = -1;

		private static final int NAMED_LAYER_CHOICE = 0;

		private static final int USER_LAYER_CHOICE = 1;

		private NamedLayerInfo namedLayer;

		private UserLayerInfo userLayer;

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
			namedLayer = null;
			userLayer = null;
		}

		/**
		 * Check if NamedLayer is current selection for choice.
		 * 
		 * @return <code>true</code> if selection, <code>false</code> if not
		 */
		public boolean ifNamedLayer() {
			return choiceListSelect == NAMED_LAYER_CHOICE;
		}

		/**
		 * Get the 'NamedLayer' element value.
		 * 
		 * @return value
		 */
		public NamedLayerInfo getNamedLayer() {
			return namedLayer;
		}

		/**
		 * Set the 'NamedLayer' element value.
		 * 
		 * @param namedLayer
		 */
		public void setNamedLayer(NamedLayerInfo namedLayer) {
			setChoiceListSelect(NAMED_LAYER_CHOICE);
			this.namedLayer = namedLayer;
		}

		/**
		 * Check if UserLayer is current selection for choice.
		 * 
		 * @return <code>true</code> if selection, <code>false</code> if not
		 */
		public boolean ifUserLayer() {
			return choiceListSelect == USER_LAYER_CHOICE;
		}

		/**
		 * Get the 'UserLayer' element value.
		 * 
		 * @return value
		 */
		public UserLayerInfo getUserLayer() {
			return userLayer;
		}

		/**
		 * Set the 'UserLayer' element value.
		 * 
		 * @param userLayer
		 */
		public void setUserLayer(UserLayerInfo userLayer) {
			setChoiceListSelect(USER_LAYER_CHOICE);
			this.userLayer = userLayer;
		}

		@java.lang.Override
		@java.lang.SuppressWarnings("all")
		public java.lang.String toString() {
			return "StyledLayerDescriptorInfo.ChoiceInfo(choiceListSelect=" + this.choiceListSelect + ", namedLayer="
					+ this.getNamedLayer() + ", userLayer=" + this.getUserLayer() + ")";
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
			if (this.getNamedLayer() == null ? other.getNamedLayer() != null : !this.getNamedLayer().equals(
					(java.lang.Object) other.getNamedLayer())) {
				return false;
			}
			if (this.getUserLayer() == null ? other.getUserLayer() != null : !this.getUserLayer().equals(
					(java.lang.Object) other.getUserLayer())) {
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
			result = result * prime + (this.getNamedLayer() == null ? 0 : this.getNamedLayer().hashCode());
			result = result * prime + (this.getUserLayer() == null ? 0 : this.getUserLayer().hashCode());
			return result;
		}
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "StyledLayerDescriptorInfo(name=" + this.getName() + ", title=" + this.getTitle() + ", aAbstract="
				+ this.aAbstract + ", choiceList=" + this.getChoiceList() + ", version=" + this.getVersion() + ")";
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof StyledLayerDescriptorInfo)) {
			return false;
		}
		final StyledLayerDescriptorInfo other = (StyledLayerDescriptorInfo) o;
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
		if (this.getChoiceList() == null ? other.getChoiceList() != null : !this.getChoiceList().equals(
				(java.lang.Object) other.getChoiceList())) {
			return false;
		}
		if (this.getVersion() == null ? other.getVersion() != null : !this.getVersion().equals(
				(java.lang.Object) other.getVersion())) {
			return false;
		}
		return true;
	}

	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof StyledLayerDescriptorInfo;
	}

	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + (this.getName() == null ? 0 : this.getName().hashCode());
		result = result * prime + (this.getTitle() == null ? 0 : this.getTitle().hashCode());
		result = result * prime + (this.aAbstract == null ? 0 : this.aAbstract.hashCode());
		result = result * prime + (this.getChoiceList() == null ? 0 : this.getChoiceList().hashCode());
		result = result * prime + (this.getVersion() == null ? 0 : this.getVersion().hashCode());
		return result;
	}
}