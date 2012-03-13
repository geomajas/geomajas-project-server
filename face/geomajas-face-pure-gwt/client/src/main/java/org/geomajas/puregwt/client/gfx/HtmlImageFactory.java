/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.puregwt.client.gfx;

import com.google.gwt.core.client.Callback;

/**
 * Gin factory for HTML images.
 * 
 * @author Jan De Moerloose
 * 
 */
public interface HtmlImageFactory {

	/**
	 * Create an HtmlImage widget that represents an HTML IMG element.
	 * 
	 * @param src Pointer to the actual image.
	 * @param width The width for this image, expressed in pixels.
	 * @param height The height for this image, expressed in pixels.
	 * @param top How many pixels should this image be placed from the top (relative to the parent origin).
	 * @param left How many pixels should this image be placed from the left (relative to the parent origin).
	 */
	HtmlImage create(String src, int width, int height, int top, int left);

	/**
	 * Create an HtmlImage widget that represents an HTML IMG element.
	 * 
	 * @param src Pointer to the actual image.
	 * @param width The width for this image, expressed in pixels.
	 * @param height The height for this image, expressed in pixels.
	 * @param top How many pixels should this image be placed from the top (relative to the parent origin).
	 * @param left How many pixels should this image be placed from the left (relative to the parent origin).
	 * @param onLoadingDone Call-back to be executed when the image finished loading, or when an error occurs while
	 *        loading.
	 */
	HtmlImage create(String src, int width, int height, int top, int left, Callback<String, String> onLoadingDone);

	/**
	 * Create an HtmlImage widget that represents an HTML IMG element.
	 * 
	 * @param src Pointer to the actual image.
	 * @param width The width for this image, expressed in pixels.
	 * @param height The height for this image, expressed in pixels.
	 * @param top How many pixels should this image be placed from the top (relative to the parent origin).
	 * @param left How many pixels should this image be placed from the left (relative to the parent origin).
	 * @param onLoadingDone Call-back to be executed when the image finished loading, or when an error occurs while
	 *        loading.
	 * @param nrRetries Total number of retries should loading fail. Default is 0.
	 */
	HtmlImage create(String src, int width, int height, int top, int left, Callback<String, String> onLoadingDone,
			int nrRetries);
}
