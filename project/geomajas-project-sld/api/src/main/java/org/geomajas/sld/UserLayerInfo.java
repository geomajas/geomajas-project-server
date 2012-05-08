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
 A UserLayer allows a user-defined layer to be built from WFS and WCS data.
 * 
 * 
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:element
 * xmlns:ns="http://www.opengis.net/sld"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="UserLayer">
 *   &lt;xs:complexType>
 *     &lt;xs:sequence>
 *       &lt;xs:element ref="ns:Name" minOccurs="0"/>
 *       &lt;xs:choice minOccurs="0">
 *         &lt;!-- Reference to inner class ChoiceInfo -->
 *       &lt;/xs:choice>
 *       &lt;xs:element ref="ns:LayerFeatureConstraints"/>
 *       &lt;xs:element ref="ns:UserStyle" maxOccurs="unbounded"/>
 *     &lt;/xs:sequence>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public class UserLayerInfo implements Serializable {

	private static final long serialVersionUID = 100;

	private String name;

	private ChoiceInfo choice;

	private LayerFeatureConstraintsInfo layerFeatureConstraints;

	private List<UserStyleInfo> userStyleList = new ArrayList<UserStyleInfo>();

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
	 * Get the list of 'UserStyle' element items.
	 * 
	 * @return list
	 */
	public List<UserStyleInfo> getUserStyleList() {
		return userStyleList;
	}

	/**
	 * Set the list of 'UserStyle' element items.
	 * 
	 * @param list
	 */
	public void setUserStyleList(List<UserStyleInfo> list) {
		userStyleList = list;
	}

	/**
	 * Schema fragment(s) for this class:...
	 * 
	 * <pre>
	 * &lt;xs:choice
	 * xmlns:ns="http://www.opengis.net/sld"
	 * xmlns:xs="http://www.w3.org/2001/XMLSchema" minOccurs="0">
	 *   &lt;xs:element ref="ns:InlineFeature"/>
	 *   &lt;xs:element ref="ns:RemoteOWS" minOccurs="0"/>
	 * &lt;/xs:choice>
	 * </pre>
	 */
	public static class ChoiceInfo implements Serializable {

		private static final long serialVersionUID = 100;

		private int choiceSelect = -1;

		private static final int INLINE_FEATURE_CHOICE = 0;

		private static final int REMOTE_OWS_CHOICE = 1;

		private InlineFeatureInfo inlineFeature;

		private RemoteOWSInfo remoteOWS;

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
		 * Check if InlineFeature is current selection for choice.
		 * 
		 * @return <code>true</code> if selection, <code>false</code> if not
		 */
		public boolean ifInlineFeature() {
			return choiceSelect == INLINE_FEATURE_CHOICE;
		}

		/**
		 * Get the 'InlineFeature' element value.
		 * 
		 * @return value
		 */
		public InlineFeatureInfo getInlineFeature() {
			return inlineFeature;
		}

		/**
		 * Set the 'InlineFeature' element value.
		 * 
		 * @param inlineFeature
		 */
		public void setInlineFeature(InlineFeatureInfo inlineFeature) {
			setChoiceSelect(INLINE_FEATURE_CHOICE);
			this.inlineFeature = inlineFeature;
		}

		/**
		 * Check if RemoteOWS is current selection for choice.
		 * 
		 * @return <code>true</code> if selection, <code>false</code> if not
		 */
		public boolean ifRemoteOWS() {
			return choiceSelect == REMOTE_OWS_CHOICE;
		}

		/**
		 * Get the 'RemoteOWS' element value.
		 * 
		 * @return value
		 */
		public RemoteOWSInfo getRemoteOWS() {
			return remoteOWS;
		}

		/**
		 * Set the 'RemoteOWS' element value.
		 * 
		 * @param remoteOWS
		 */
		public void setRemoteOWS(RemoteOWSInfo remoteOWS) {
			setChoiceSelect(REMOTE_OWS_CHOICE);
			this.remoteOWS = remoteOWS;
		}

		/** {@inheritDoc} */
		@java.lang.Override
		@java.lang.SuppressWarnings("all")
		public java.lang.String toString() {
			return "UserLayerInfo.ChoiceInfo(choiceSelect=" + this.choiceSelect + ", inlineFeature="
					+ this.getInlineFeature() + ", remoteOWS=" + this.getRemoteOWS() + ")";
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
			if (this.choiceSelect != other.choiceSelect) {
				return false;
			}
			if (this.getInlineFeature() == null ? other.getInlineFeature() != null : !this.getInlineFeature().equals(
					(java.lang.Object) other.getInlineFeature())) {
				return false;
			}
			if (this.getRemoteOWS() == null ? other.getRemoteOWS() != null : !this.getRemoteOWS().equals(
					(java.lang.Object) other.getRemoteOWS())) {
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
			result = result * prime + this.choiceSelect;
			result = result * prime + (this.getInlineFeature() == null ? 0 : this.getInlineFeature().hashCode());
			result = result * prime + (this.getRemoteOWS() == null ? 0 : this.getRemoteOWS().hashCode());
			return result;
		}
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "UserLayerInfo(name=" + this.getName() + ", choice=" + this.getChoice() + ", layerFeatureConstraints="
				+ this.getLayerFeatureConstraints() + ", userStyleList=" + this.getUserStyleList() + ")";
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof UserLayerInfo)) {
			return false;
		}
		final UserLayerInfo other = (UserLayerInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (this.getName() == null ? other.getName() != null : !this.getName().equals(
				(java.lang.Object) other.getName())) {
			return false;
		}
		if (this.getChoice() == null ? other.getChoice() != null : !this.getChoice().equals(
				(java.lang.Object) other.getChoice())) {
			return false;
		}
		if (this.getLayerFeatureConstraints() == null ? other.getLayerFeatureConstraints() != null : !this
				.getLayerFeatureConstraints().equals((java.lang.Object) other.getLayerFeatureConstraints())) {
			return false;
		}
		if (this.getUserStyleList() == null ? other.getUserStyleList() != null : !this.getUserStyleList().equals(
				(java.lang.Object) other.getUserStyleList())) {
			return false;
		}
		return true;
	}

	/** {@inheritDoc} */
	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof UserLayerInfo;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + (this.getName() == null ? 0 : this.getName().hashCode());
		result = result * prime + (this.getChoice() == null ? 0 : this.getChoice().hashCode());
		result = result * prime
				+ (this.getLayerFeatureConstraints() == null ? 0 : this.getLayerFeatureConstraints().hashCode());
		result = result * prime + (this.getUserStyleList() == null ? 0 : this.getUserStyleList().hashCode());
		return result;
	}
}