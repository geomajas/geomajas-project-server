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
package org.geomajas.sld;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.geomajas.annotations.Api;

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
 * @since 1.10.0
 */
@Api(allMethods = true)
public class UserLayerInfo implements Serializable {

	private static final long serialVersionUID = 1100;

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

		private static final long serialVersionUID = 1100;

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
	}
}
