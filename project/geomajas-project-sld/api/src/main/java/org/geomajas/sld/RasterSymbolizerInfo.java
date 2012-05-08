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
import org.geomajas.annotation.Api;

/**
 * 
 A "RasterSymbolizer" is used to specify the rendering of raster/ matrix-coverage data (e.g., satellite images, DEMs).
 * 
 * 
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:element
 * xmlns:sld="http://www.opengis.net/sld"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" substitutionGroup="sld:Symbolizer" name="RasterSymbolizer">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="sld:SymbolizerType">
 *         &lt;xs:sequence>
 *           &lt;xs:element ref="sld:Geometry" minOccurs="0"/>
 *           &lt;xs:element ref="sld:Opacity" minOccurs="0"/>
 *           &lt;xs:element ref="sld:ChannelSelection" minOccurs="0"/>
 *           &lt;xs:element ref="sld:OverlapBehavior" minOccurs="0"/>
 *           &lt;xs:element ref="sld:ColorMap" minOccurs="0"/>
 *           &lt;xs:element ref="sld:ContrastEnhancement" minOccurs="0"/>
 *           &lt;xs:element ref="sld:ShadedRelief" minOccurs="0"/>
 *           &lt;xs:element ref="sld:ImageOutline" minOccurs="0"/>
 *         &lt;/xs:sequence>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public class RasterSymbolizerInfo extends SymbolizerTypeInfo implements Serializable {

	private static final long serialVersionUID = 100;

	private GeometryInfo geometry;

	private OpacityInfo opacity;

	private ChannelSelectionInfo channelSelection;

	private OverlapBehaviorInfo overlapBehavior;

	private ColorMapInfo colorMap;

	private ContrastEnhancementInfo contrastEnhancement;

	private ShadedReliefInfo shadedRelief;

	private ImageOutlineInfo imageOutline;

	/**
	 * Get the 'Geometry' element value.
	 * 
	 * @return value
	 */
	public GeometryInfo getGeometry() {
		return geometry;
	}

	/**
	 * Set the 'Geometry' element value.
	 * 
	 * @param geometry
	 */
	public void setGeometry(GeometryInfo geometry) {
		this.geometry = geometry;
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
	 * Get the 'ChannelSelection' element value.
	 * 
	 * @return value
	 */
	public ChannelSelectionInfo getChannelSelection() {
		return channelSelection;
	}

	/**
	 * Set the 'ChannelSelection' element value.
	 * 
	 * @param channelSelection
	 */
	public void setChannelSelection(ChannelSelectionInfo channelSelection) {
		this.channelSelection = channelSelection;
	}

	/**
	 * Get the 'OverlapBehavior' element value.
	 * 
	 * @return value
	 */
	public OverlapBehaviorInfo getOverlapBehavior() {
		return overlapBehavior;
	}

	/**
	 * Set the 'OverlapBehavior' element value.
	 * 
	 * @param overlapBehavior
	 */
	public void setOverlapBehavior(OverlapBehaviorInfo overlapBehavior) {
		this.overlapBehavior = overlapBehavior;
	}

	/**
	 * Get the 'ColorMap' element value.
	 * 
	 * @return value
	 */
	public ColorMapInfo getColorMap() {
		return colorMap;
	}

	/**
	 * Set the 'ColorMap' element value.
	 * 
	 * @param colorMap
	 */
	public void setColorMap(ColorMapInfo colorMap) {
		this.colorMap = colorMap;
	}

	/**
	 * Get the 'ContrastEnhancement' element value.
	 * 
	 * @return value
	 */
	public ContrastEnhancementInfo getContrastEnhancement() {
		return contrastEnhancement;
	}

	/**
	 * Set the 'ContrastEnhancement' element value.
	 * 
	 * @param contrastEnhancement
	 */
	public void setContrastEnhancement(ContrastEnhancementInfo contrastEnhancement) {
		this.contrastEnhancement = contrastEnhancement;
	}

	/**
	 * Get the 'ShadedRelief' element value.
	 * 
	 * @return value
	 */
	public ShadedReliefInfo getShadedRelief() {
		return shadedRelief;
	}

	/**
	 * Set the 'ShadedRelief' element value.
	 * 
	 * @param shadedRelief
	 */
	public void setShadedRelief(ShadedReliefInfo shadedRelief) {
		this.shadedRelief = shadedRelief;
	}

	/**
	 * Get the 'ImageOutline' element value.
	 * 
	 * @return value
	 */
	public ImageOutlineInfo getImageOutline() {
		return imageOutline;
	}

	/**
	 * Set the 'ImageOutline' element value.
	 * 
	 * @param imageOutline
	 */
	public void setImageOutline(ImageOutlineInfo imageOutline) {
		this.imageOutline = imageOutline;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "RasterSymbolizerInfo(geometry=" + this.getGeometry() + ", opacity=" + this.getOpacity()
				+ ", channelSelection=" + this.getChannelSelection() + ", overlapBehavior=" + this.getOverlapBehavior()
				+ ", colorMap=" + this.getColorMap() + ", contrastEnhancement=" + this.getContrastEnhancement()
				+ ", shadedRelief=" + this.getShadedRelief() + ", imageOutline=" + this.getImageOutline() + ")";
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof RasterSymbolizerInfo)) {
			return false;
		}
		final RasterSymbolizerInfo other = (RasterSymbolizerInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}
		if (this.getGeometry() == null ? other.getGeometry() != null : !this.getGeometry().equals(
				(java.lang.Object) other.getGeometry())) {
			return false;
		}
		if (this.getOpacity() == null ? other.getOpacity() != null : !this.getOpacity().equals(
				(java.lang.Object) other.getOpacity())) {
			return false;
		}
		if (this.getChannelSelection() == null ? other.getChannelSelection() != null : !this.getChannelSelection()
				.equals((java.lang.Object) other.getChannelSelection())) {
			return false;
		}
		if (this.getOverlapBehavior() == null ? other.getOverlapBehavior() != null : !this.getOverlapBehavior().equals(
				(java.lang.Object) other.getOverlapBehavior())) {
			return false;
		}
		if (this.getColorMap() == null ? other.getColorMap() != null : !this.getColorMap().equals(
				(java.lang.Object) other.getColorMap())) {
			return false;
		}
		if (this.getContrastEnhancement() == null ? other.getContrastEnhancement() != null : !this
				.getContrastEnhancement().equals((java.lang.Object) other.getContrastEnhancement())) {
			return false;
		}
		if (this.getShadedRelief() == null ? other.getShadedRelief() != null : !this.getShadedRelief().equals(
				(java.lang.Object) other.getShadedRelief())) {
			return false;
		}
		if (this.getImageOutline() == null ? other.getImageOutline() != null : !this.getImageOutline().equals(
				(java.lang.Object) other.getImageOutline())) {
			return false;
		}
		return true;
	}

	/** {@inheritDoc} */
	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof RasterSymbolizerInfo;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + super.hashCode();
		result = result * prime + (this.getGeometry() == null ? 0 : this.getGeometry().hashCode());
		result = result * prime + (this.getOpacity() == null ? 0 : this.getOpacity().hashCode());
		result = result * prime + (this.getChannelSelection() == null ? 0 : this.getChannelSelection().hashCode());
		result = result * prime + (this.getOverlapBehavior() == null ? 0 : this.getOverlapBehavior().hashCode());
		result = result * prime + (this.getColorMap() == null ? 0 : this.getColorMap().hashCode());
		result = result * prime
				+ (this.getContrastEnhancement() == null ? 0 : this.getContrastEnhancement().hashCode());
		result = result * prime + (this.getShadedRelief() == null ? 0 : this.getShadedRelief().hashCode());
		result = result * prime + (this.getImageOutline() == null ? 0 : this.getImageOutline().hashCode());
		return result;
	}
}