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
import org.geomajas.annotation.Api;

/**
 * 
 "ContrastEnhancement" defines the 'stretching' of contrast for a channel of a false-color image or for a whole
 * grey/color image. Contrast enhancement is used to make ground features in images more visible.
 * 
 * 
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:element
 * xmlns:ns="http://www.opengis.net/sld"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="ContrastEnhancement">
 *   &lt;xs:complexType>
 *     &lt;xs:sequence>
 *       &lt;xs:choice minOccurs="0">
 *         &lt;!-- Reference to inner class ChoiceInfo -->
 *       &lt;/xs:choice>
 *       &lt;xs:element ref="ns:GammaValue" minOccurs="0"/>
 *     &lt;/xs:sequence>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public class ContrastEnhancementInfo implements Serializable {

	private static final long serialVersionUID = 100;

	private ChoiceInfo choice;

	private GammaValueInfo gammaValue;

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
	 * Get the 'GammaValue' element value.
	 * 
	 * @return value
	 */
	public GammaValueInfo getGammaValue() {
		return gammaValue;
	}

	/**
	 * Set the 'GammaValue' element value.
	 * 
	 * @param gammaValue
	 */
	public void setGammaValue(GammaValueInfo gammaValue) {
		this.gammaValue = gammaValue;
	}

	/**
	 * Schema fragment(s) for this class:...
	 * 
	 * <pre>
	 * &lt;xs:choice
	 * xmlns:ns="http://www.opengis.net/sld"
	 * xmlns:xs="http://www.w3.org/2001/XMLSchema" minOccurs="0">
	 *   &lt;xs:element ref="ns:Normalize"/>
	 *   &lt;xs:element ref="ns:Histogram"/>
	 * &lt;/xs:choice>
	 * </pre>
	 */
	public static class ChoiceInfo implements Serializable {

		private static final long serialVersionUID = 100;

		private int choiceSelect = -1;

		private static final int NORMALIZE_CHOICE = 0;

		private static final int HISTOGRAM_CHOICE = 1;

		private NormalizeInfo normalize;

		private HistogramInfo histogram;

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
		 * Check if Normalize is current selection for choice.
		 * 
		 * @return <code>true</code> if selection, <code>false</code> if not
		 */
		public boolean ifNormalize() {
			return choiceSelect == NORMALIZE_CHOICE;
		}

		/**
		 * Get the 'Normalize' element value.
		 * 
		 * @return value
		 */
		public NormalizeInfo getNormalize() {
			return normalize;
		}

		/**
		 * Set the 'Normalize' element value.
		 * 
		 * @param normalize
		 */
		public void setNormalize(NormalizeInfo normalize) {
			setChoiceSelect(NORMALIZE_CHOICE);
			this.normalize = normalize;
		}

		/**
		 * Check if Histogram is current selection for choice.
		 * 
		 * @return <code>true</code> if selection, <code>false</code> if not
		 */
		public boolean ifHistogram() {
			return choiceSelect == HISTOGRAM_CHOICE;
		}

		/**
		 * Get the 'Histogram' element value.
		 * 
		 * @return value
		 */
		public HistogramInfo getHistogram() {
			return histogram;
		}

		/**
		 * Set the 'Histogram' element value.
		 * 
		 * @param histogram
		 */
		public void setHistogram(HistogramInfo histogram) {
			setChoiceSelect(HISTOGRAM_CHOICE);
			this.histogram = histogram;
		}

		/** {@inheritDoc} */
		@java.lang.Override
		@java.lang.SuppressWarnings("all")
		public java.lang.String toString() {
			return "ContrastEnhancementInfo.ChoiceInfo(choiceSelect=" + this.choiceSelect + ", normalize="
					+ this.getNormalize() + ", histogram=" + this.getHistogram() + ")";
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
			if (this.getNormalize() == null ? other.getNormalize() != null : !this.getNormalize().equals(
					(java.lang.Object) other.getNormalize())) {
				return false;
			}
			if (this.getHistogram() == null ? other.getHistogram() != null : !this.getHistogram().equals(
					(java.lang.Object) other.getHistogram())) {
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
			result = result * prime + (this.getNormalize() == null ? 0 : this.getNormalize().hashCode());
			result = result * prime + (this.getHistogram() == null ? 0 : this.getHistogram().hashCode());
			return result;
		}
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "ContrastEnhancementInfo(choice=" + this.getChoice() + ", gammaValue=" + this.getGammaValue() + ")";
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof ContrastEnhancementInfo)) {
			return false;
		}
		final ContrastEnhancementInfo other = (ContrastEnhancementInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (this.getChoice() == null ? other.getChoice() != null : !this.getChoice().equals(
				(java.lang.Object) other.getChoice())) {
			return false;
		}
		if (this.getGammaValue() == null ? other.getGammaValue() != null : !this.getGammaValue().equals(
				(java.lang.Object) other.getGammaValue())) {
			return false;
		}
		return true;
	}

	/** {@inheritDoc} */
	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof ContrastEnhancementInfo;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + (this.getChoice() == null ? 0 : this.getChoice().hashCode());
		result = result * prime + (this.getGammaValue() == null ? 0 : this.getGammaValue().hashCode());
		return result;
	}
}