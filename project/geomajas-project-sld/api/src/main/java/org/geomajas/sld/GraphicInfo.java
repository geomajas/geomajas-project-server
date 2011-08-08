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
 A "Graphic" specifies or refers to a "graphic symbol" with inherent shape, size, and coloring.
 * 
 * 
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:element
 * xmlns:ns="http://www.opengis.net/sld"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="Graphic">
 *   &lt;xs:complexType>
 *     &lt;xs:sequence>
 *       &lt;xs:choice minOccurs="0" maxOccurs="unbounded">
 *         &lt;!-- Reference to inner class ChoiceInfo -->
 *       &lt;/xs:choice>
 *       &lt;xs:sequence>
 *         &lt;xs:element ref="ns:Opacity" minOccurs="0"/>
 *         &lt;xs:element ref="ns:Size" minOccurs="0"/>
 *         &lt;xs:element ref="ns:Rotation" minOccurs="0"/>
 *       &lt;/xs:sequence>
 *     &lt;/xs:sequence>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 *
 * @author Jan De Moerloose
 * @since 1.10.0
 */
@Api(allMethods = true)
public class GraphicInfo implements Serializable {

	private static final long serialVersionUID = 1100;

	private List<ChoiceInfo> choiceList = new ArrayList<ChoiceInfo>();

	private OpacityInfo opacity;

	private SizeInfo size;

	private RotationInfo rotation;

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
	 * Get the 'Opacity' element value.
	 * 
	 * @return value
	 */
	public OpacityInfo getOpacity() {
		return opacity;
	}

	/**
	 * Set the 'Opacity' element value.
	 * 
	 * @param opacity
	 */
	public void setOpacity(OpacityInfo opacity) {
		this.opacity = opacity;
	}

	/**
	 * Get the 'Size' element value.
	 * 
	 * @return value
	 */
	public SizeInfo getSize() {
		return size;
	}

	/**
	 * Set the 'Size' element value.
	 * 
	 * @param size
	 */
	public void setSize(SizeInfo size) {
		this.size = size;
	}

	/**
	 * Get the 'Rotation' element value.
	 * 
	 * @return value
	 */
	public RotationInfo getRotation() {
		return rotation;
	}

	/**
	 * Set the 'Rotation' element value.
	 * 
	 * @param rotation
	 */
	public void setRotation(RotationInfo rotation) {
		this.rotation = rotation;
	}

	/**
	 * Schema fragment(s) for this class:...
	 * 
	 * <pre>
	 * &lt;xs:choice
 * xmlns:ns="http://www.opengis.net/sld"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" minOccurs="0" maxOccurs="unbounded">
	 *   &lt;xs:element ref="ns:ExternalGraphic"/>
	 *   &lt;xs:element ref="ns:Mark"/>
	 * &lt;/xs:choice>
	 * </pre>
	 */
	public static class ChoiceInfo implements Serializable {

		private static final long serialVersionUID = 1100;

		private int choiceListSelect = -1;

		private static final int EXTERNAL_GRAPHIC_CHOICE = 0;

		private static final int MARK_CHOICE = 1;

		private ExternalGraphicInfo externalGraphic;

		private MarkInfo mark;

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
		 * Check if ExternalGraphic is current selection for choice.
		 * 
		 * @return <code>true</code> if selection, <code>false</code> if not
		 */
		public boolean ifExternalGraphic() {
			return choiceListSelect == EXTERNAL_GRAPHIC_CHOICE;
		}

		/**
		 * Get the 'ExternalGraphic' element value.
		 * 
		 * @return value
		 */
		public ExternalGraphicInfo getExternalGraphic() {
			return externalGraphic;
		}

		/**
		 * Set the 'ExternalGraphic' element value.
		 * 
		 * @param externalGraphic
		 */
		public void setExternalGraphic(ExternalGraphicInfo externalGraphic) {
			setChoiceListSelect(EXTERNAL_GRAPHIC_CHOICE);
			this.externalGraphic = externalGraphic;
		}

		/**
		 * Check if Mark is current selection for choice.
		 * 
		 * @return <code>true</code> if selection, <code>false</code> if not
		 */
		public boolean ifMark() {
			return choiceListSelect == MARK_CHOICE;
		}

		/**
		 * Get the 'Mark' element value.
		 * 
		 * @return value
		 */
		public MarkInfo getMark() {
			return mark;
		}

		/**
		 * Set the 'Mark' element value.
		 * 
		 * @param mark
		 */
		public void setMark(MarkInfo mark) {
			setChoiceListSelect(MARK_CHOICE);
			this.mark = mark;
		}
	}
}
