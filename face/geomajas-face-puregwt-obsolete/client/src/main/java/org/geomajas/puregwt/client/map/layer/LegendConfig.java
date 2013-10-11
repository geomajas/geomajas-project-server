/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.puregwt.client.map.layer;

import org.geomajas.annotation.Api;
import org.geomajas.configuration.FontStyleInfo;
import org.geomajas.geometry.Bbox;

/**
 * Generic configuration object for legend creation, used in the {@link LegendUrlSupported} interface. It is important
 * to note that not all legend creation services may support all the fields within this configuration object. It is up
 * to the {@link LegendUrlSupported} implementation to specify what is supported and what is not.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public class LegendConfig {

	/** Default font family used in legend text rendering. */
	public static final String DEFAULT_FONT_FAMILY = "arial";

	/** Default font size used for legend style labels. */
	public static final int DEFAULT_FONT_SIZE = 13;

	/** Default font color used in legend text rendering. */
	public static final String DEFAULT_FONT_COLOR = "0x000000";

	/** Default icon size used the legend icons. */
	public static final int DEFAULT_ICON_SIZE = 20;

	/** Default icon size used the legend icons. */
	public static final String DEFAULT_IMAGE_FORMAT = "PNG";

	/** Default DPI used for legend images. */
	public static final int DEFAULT_DPI = 72;

	private int iconWidth = DEFAULT_ICON_SIZE;

	private int iconHeight = DEFAULT_ICON_SIZE;

	private FontStyleInfo fontStyle;

	private String imageFormat = DEFAULT_IMAGE_FORMAT;

	private String exceptions = DEFAULT_IMAGE_FORMAT;

	private Bbox bounds;
	
	private int styleIndex;
	
	private int dpi = DEFAULT_DPI;


	/** Default constructor. Makes sure everything has a default value, to minimize the chance of a NPE. */
	public LegendConfig() {
		fontStyle = new FontStyleInfo();
		fontStyle.setColor(DEFAULT_FONT_COLOR);
		fontStyle.setFamily(DEFAULT_FONT_FAMILY);
		fontStyle.setOpacity(1);
		fontStyle.setSize(DEFAULT_FONT_SIZE);
		fontStyle.setStyle("normal");
		fontStyle.setWeight("normal");
		fontStyle.applyDefaults();
	}

	/**
	 * Get the width for the icons in the legend, expressed in pixels.
	 * 
	 * @return The width for the icons in the legend, expressed in pixels.
	 */
	public int getIconWidth() {
		return iconWidth;
	}

	/**
	 * Set the width for the icons in the legend, expressed in pixels.
	 * 
	 * @param iconWidth
	 *            The new width.
	 */
	public void setIconWidth(int iconWidth) {
		this.iconWidth = iconWidth;
	}

	/**
	 * Get the height for the icons in the legend, expressed in pixels.
	 * 
	 * @return The height for the icons in the legend, expressed in pixels.
	 */
	public int getIconHeight() {
		return iconHeight;
	}

	/**
	 * Set the height for the icons in the legend, expressed in pixels.
	 * 
	 * @param iconHeight
	 *            The new height.
	 */
	public void setIconHeight(int iconHeight) {
		this.iconHeight = iconHeight;
	}

	/**
	 * Get the preferred font style for the labels within the legend.
	 * 
	 * @return The preferred font style.
	 */
	public FontStyleInfo getFontStyle() {
		return fontStyle;
	}

	/**
	 * Set the preferred font style for the labels within the legend.
	 * 
	 * @param fontStyle
	 *            The preferred font style.
	 */
	public void setFontStyle(FontStyleInfo fontStyle) {
		this.fontStyle = fontStyle;
	}

	/**
	 * Get the image format for the legend image (i.e. PNG, JPG, GIF).
	 * 
	 * @return The preferred image format.
	 */
	public String getImageFormat() {
		return imageFormat;
	}

	/**
	 * Set the image format for the legend image (i.e. PNG, JPG, GIF).
	 * 
	 * @param imageFormat
	 *            The new image format.
	 */
	public void setImageFormat(String imageFormat) {
		this.imageFormat = imageFormat;
	}

	/**
	 * Get the bounding box for the legend. It may be that a legend image is built reflecting styles within a certain
	 * area. Note that this setting is not often supported.
	 * 
	 * @return The bounding box used for legend creation.
	 */
	public Bbox getBounds() {
		return bounds;
	}

	/**
	 * Set the bounding box for the legend. It may be that a legend image is built reflecting styles within a certain
	 * area. Note that this setting is not often supported.
	 * 
	 * @param bounds
	 *            The bounds to use for legend creation.
	 */
	public void setBounds(Bbox bounds) {
		this.bounds = bounds;
	}

	/** Get the DPI used for legend images.
	 * 
	 * @return The DPI used for legend images.
	 */
	public int getDpi() {
		return dpi;
	}

	/** Set the DPI used for legend images.
	 * 
	 * @param dpi
	 * 			The DPI used for legend images.
	 */

	public void setDpi(int dpi) {
		this.dpi = dpi;
	}
	
	/**
	 * Get the mimetype to be used in case of an exception.
	 * 
	 * @return The mimetype to be used in case of an exception.
	 */
	public String getExceptions() {
		return exceptions;
	}
	
	/**
	 * @return  The index of the desired active style   
	 */
	public int getStyleIndex() {
		return styleIndex;
	}

	/**
	 * @param styleIndex The index of the desired active style
	 */
	public void setStyleIndex(int styleIndex) {
		this.styleIndex = styleIndex;
	}

	/**
	 * Set the mimetype to be used in case of an exception.
	 * 
	 * @param exceptions
	 *            The mimetype to be used in case of an exception.
	 */
	public void setExceptions(String exceptions) {
		this.exceptions = exceptions;
	}
}