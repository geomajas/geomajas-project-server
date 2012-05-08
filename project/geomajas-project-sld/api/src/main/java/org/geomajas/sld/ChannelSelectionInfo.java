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
 "ChannelSelection" specifies the false-color channel selection for a multi-spectral raster source.
 * 
 * 
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:element
 * xmlns:ns="http://www.opengis.net/sld"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="ChannelSelection">
 *   &lt;xs:complexType>
 *     &lt;xs:choice>
 *       &lt;xs:sequence>
 *         &lt;xs:element ref="ns:RedChannel"/>
 *         &lt;xs:element ref="ns:GreenChannel"/>
 *         &lt;xs:element ref="ns:BlueChannel"/>
 *       &lt;/xs:sequence>
 *       &lt;xs:element ref="ns:GrayChannel"/>
 *     &lt;/xs:choice>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public class ChannelSelectionInfo implements Serializable {

	private static final long serialVersionUID = 100;

	private int channelSelectionSelect = -1;

	private static final int RED_CHANNEL_CHOICE = 0;

	private static final int GRAY_CHANNEL_CHOICE = 1;

	private RedChannelInfo redChannel;

	private GreenChannelInfo greenChannel;

	private BlueChannelInfo blueChannel;

	private GrayChannelInfo grayChannel;

	private void setChannelSelectionSelect(int choice) {
		if (channelSelectionSelect == -1) {
			channelSelectionSelect = choice;
		} else if (channelSelectionSelect != choice) {
			throw new IllegalStateException(
					"Need to call clearChannelSelectionSelect() before changing existing choice");
		}
	}

	/**
	 * Clear the choice selection.
	 */
	public void clearChannelSelectionSelect() {
		channelSelectionSelect = -1;
	}

	/**
	 * Check if RedChannel is current selection for choice.
	 * 
	 * @return <code>true</code> if selection, <code>false</code> if not
	 */
	public boolean ifRedChannel() {
		return channelSelectionSelect == RED_CHANNEL_CHOICE;
	}

	/**
	 * Get the 'RedChannel' element value.
	 * 
	 * @return value
	 */
	public RedChannelInfo getRedChannel() {
		return redChannel;
	}

	/**
	 * Set the 'RedChannel' element value.
	 * 
	 * @param redChannel
	 */
	public void setRedChannel(RedChannelInfo redChannel) {
		setChannelSelectionSelect(RED_CHANNEL_CHOICE);
		this.redChannel = redChannel;
	}

	/**
	 * Get the 'GreenChannel' element value.
	 * 
	 * @return value
	 */
	public GreenChannelInfo getGreenChannel() {
		return greenChannel;
	}

	/**
	 * Set the 'GreenChannel' element value.
	 * 
	 * @param greenChannel
	 */
	public void setGreenChannel(GreenChannelInfo greenChannel) {
		setChannelSelectionSelect(RED_CHANNEL_CHOICE);
		this.greenChannel = greenChannel;
	}

	/**
	 * Get the 'BlueChannel' element value.
	 * 
	 * @return value
	 */
	public BlueChannelInfo getBlueChannel() {
		return blueChannel;
	}

	/**
	 * Set the 'BlueChannel' element value.
	 * 
	 * @param blueChannel
	 */
	public void setBlueChannel(BlueChannelInfo blueChannel) {
		setChannelSelectionSelect(RED_CHANNEL_CHOICE);
		this.blueChannel = blueChannel;
	}

	/**
	 * Check if GrayChannel is current selection for choice.
	 * 
	 * @return <code>true</code> if selection, <code>false</code> if not
	 */
	public boolean ifGrayChannel() {
		return channelSelectionSelect == GRAY_CHANNEL_CHOICE;
	}

	/**
	 * Get the 'GrayChannel' element value.
	 * 
	 * @return value
	 */
	public GrayChannelInfo getGrayChannel() {
		return grayChannel;
	}

	/**
	 * Set the 'GrayChannel' element value.
	 * 
	 * @param grayChannel
	 */
	public void setGrayChannel(GrayChannelInfo grayChannel) {
		setChannelSelectionSelect(GRAY_CHANNEL_CHOICE);
		this.grayChannel = grayChannel;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "ChannelSelectionInfo(channelSelectionSelect=" + this.channelSelectionSelect + ", redChannel="
				+ this.getRedChannel() + ", greenChannel=" + this.getGreenChannel() + ", blueChannel="
				+ this.getBlueChannel() + ", grayChannel=" + this.getGrayChannel() + ")";
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof ChannelSelectionInfo)) {
			return false;
		}
		final ChannelSelectionInfo other = (ChannelSelectionInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (this.channelSelectionSelect != other.channelSelectionSelect) {
			return false;
		}
		if (this.getRedChannel() == null ? other.getRedChannel() != null : !this.getRedChannel().equals(
				(java.lang.Object) other.getRedChannel())) {
			return false;
		}
		if (this.getGreenChannel() == null ? other.getGreenChannel() != null : !this.getGreenChannel().equals(
				(java.lang.Object) other.getGreenChannel())) {
			return false;
		}
		if (this.getBlueChannel() == null ? other.getBlueChannel() != null : !this.getBlueChannel().equals(
				(java.lang.Object) other.getBlueChannel())) {
			return false;
		}
		if (this.getGrayChannel() == null ? other.getGrayChannel() != null : !this.getGrayChannel().equals(
				(java.lang.Object) other.getGrayChannel())) {
			return false;
		}
		return true;
	}

	/** {@inheritDoc} */
	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof ChannelSelectionInfo;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + this.channelSelectionSelect;
		result = result * prime + (this.getRedChannel() == null ? 0 : this.getRedChannel().hashCode());
		result = result * prime + (this.getGreenChannel() == null ? 0 : this.getGreenChannel().hashCode());
		result = result * prime + (this.getBlueChannel() == null ? 0 : this.getBlueChannel().hashCode());
		result = result * prime + (this.getGrayChannel() == null ? 0 : this.getGrayChannel().hashCode());
		return result;
	}
}