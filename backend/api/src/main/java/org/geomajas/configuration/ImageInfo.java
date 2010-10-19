/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.geomajas.configuration;

import java.io.Serializable;

import org.geomajas.global.Api;
import org.geomajas.global.CacheableObject;

/**
 * Image configuration information. Contains a link to the normal image, a link to the selection image, a width and a
 * height. The selection image is the image used, when a feature is selected. Note that both images must have the same
 * width and height.
 * 
 * @author Pieter De Graef
 * @since 1.6.0
 */
@Api(allMethods = true)
public class ImageInfo implements Serializable, CacheableObject {

	private static final long serialVersionUID = 160L;

	private String href;

	private String selectionHref;

	private int width;

	private int height;

	/** @return Return the link to the image. */
	public String getHref() {
		return href;
	}

	/**
	 * Set the link to the image.
	 * 
	 * @param href
	 *            The image.
	 */
	public void setHref(String href) {
		this.href = href;
	}

	/**
	 * @return Return the link to the image to be used when a feature is selected.
	 */
	public String getSelectionHref() {
		return selectionHref;
	}

	/**
	 * Set the link to the image to be used when a feature is selected.
	 * 
	 * @param selectionHref
	 *            The selection image.
	 */
	public void setSelectionHref(String selectionHref) {
		this.selectionHref = selectionHref;
	}

	/** @return Get the width of the image. */
	public int getWidth() {
		return width;
	}

	/**
	 * Set the width of the image.
	 * 
	 * @param width
	 *            The image's width.
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/** @return Get the height of the image. */
	public int getHeight() {
		return height;
	}

	/**
	 * Set the height of the image.
	 * 
	 * @param height
	 *            The image's height.
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * String identifier which is guaranteed to include sufficient information to assure to be different for two
	 * instances which could produce different result. It is typically used as basis for calculation of hash
	 * codes (like MD5, SHA1, SHA2 etc) of (collections of) objects.
	 *
	 * @return cacheId
	 * @since 1.8.0
	 */
	public String getCacheId() {
		return "ImageInfo{" +
				"href='" + href + '\'' +
				", selectionHref='" + selectionHref + '\'' +
				", width=" + width +
				", height=" + height +
				'}';
	}

	/**
	 * String representation of object.
	 *
	 * @return string representation of object
	 * @since 1.8.0
	 */
	@Override
	public String toString() {
		return getCacheId();
	}

	/**
	 * Are the two objects equal?
	 *
	 * @param o object to compare
	 * @return true when objects are equal
	 * @since 1.8.0
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) { return true; }
		if (!(o instanceof ImageInfo)) { return false; }

		ImageInfo imageInfo = (ImageInfo) o;

		if (height != imageInfo.height) { return false; }
		if (width != imageInfo.width) { return false; }
		if (href != null ? !href.equals(imageInfo.href) : imageInfo.href != null) { return false; }
		if (selectionHref != null ? !selectionHref.equals(imageInfo.selectionHref) : imageInfo.selectionHref != null) {
			return false;
		}

		return true;
	}

	/**
	 * Calculate object hash code.
	 *
	 * @return hash code
	 * @since 1.8.0
	 */
	@Override
	public int hashCode() {
		int result = href != null ? href.hashCode() : 0;
		result = 31 * result + (selectionHref != null ? selectionHref.hashCode() : 0);
		result = 31 * result + width;
		result = 31 * result + height;
		return result;
	}
}
