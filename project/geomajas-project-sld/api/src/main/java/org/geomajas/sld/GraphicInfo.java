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
 * @since 1.0.0
 */

@Api(allMethods = true)
public class GraphicInfo implements Serializable {

	private static final long serialVersionUID = 100;

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

		private static final long serialVersionUID = 100;

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
			mark = null;
			externalGraphic = null;
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

		@Override
		@java.lang.SuppressWarnings("all")
		public java.lang.String toString() {
			return "GraphicInfo.ChoiceInfo(choiceListSelect=" + this.choiceListSelect + ", externalGraphic="
					+ this.getExternalGraphic() + ", mark=" + this.getMark() + ")";
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
			if (this.choiceListSelect != other.choiceListSelect) {
				return false;
			}
			if (this.getExternalGraphic() == null ? other.getExternalGraphic() != null : !this.getExternalGraphic()
					.equals((java.lang.Object) other.getExternalGraphic())) {
				return false;
			}
			if (this.getMark() == null ? other.getMark() != null : !this.getMark().equals(
					(java.lang.Object) other.getMark())) {
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
			result = result * prime + this.choiceListSelect;
			result = result * prime + (this.getExternalGraphic() == null ? 0 : this.getExternalGraphic().hashCode());
			result = result * prime + (this.getMark() == null ? 0 : this.getMark().hashCode());
			return result;
		}
	}

	@Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "GraphicInfo(choiceList=" + this.getChoiceList() + ", opacity=" + this.getOpacity() + ", size="
				+ this.getSize() + ", rotation=" + this.getRotation() + ")";
	}

	@Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof GraphicInfo)) {
			return false;
		}
		final GraphicInfo other = (GraphicInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (this.getChoiceList() == null ? other.getChoiceList() != null : !this.getChoiceList().equals(
				(java.lang.Object) other.getChoiceList())) {
			return false;
		}
		if (this.getOpacity() == null ? other.getOpacity() != null : !this.getOpacity().equals(
				(java.lang.Object) other.getOpacity())) {
			return false;
		}
		if (this.getSize() == null ? other.getSize() != null : !this.getSize().equals(
				(java.lang.Object) other.getSize())) {
			return false;
		}
		if (this.getRotation() == null ? other.getRotation() != null : !this.getRotation().equals(
				(java.lang.Object) other.getRotation())) {
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
		return other instanceof GraphicInfo;
	}

	@Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + (this.getChoiceList() == null ? 0 : this.getChoiceList().hashCode());
		result = result * prime + (this.getOpacity() == null ? 0 : this.getOpacity().hashCode());
		result = result * prime + (this.getSize() == null ? 0 : this.getSize().hashCode());
		result = result * prime + (this.getRotation() == null ? 0 : this.getRotation().hashCode());
		return result;
	}
}