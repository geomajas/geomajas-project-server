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
package org.geomajas.gwt2.client.gfx;

import org.geomajas.geometry.Bbox;

import com.google.gwt.core.client.Callback;

/**
 * Gin factory for HTML images.
 * 
 * @author Jan De Moerloose
 */
public interface HtmlImageFactory {

	/**
	 * Create an HtmlImage widget that represents an HTML IMG element.
	 * 
	 * @param src Pointer to the actual image.
	 * @param bbox The bounding box of the image.
	 */
	HtmlImage create(String src, Bbox bbox);

	/**
	 * Create an HtmlImage widget that represents an HTML IMG element.
	 * 
	 * @param src Pointer to the actual image.
	 * @param bbox The bounding box of the image.
	 * @param onLoadingDone Call-back to be executed when the image finished loading, or when an error occurs while
	 *        loading.
	 */
	HtmlImage create(String src, Bbox bbox, Callback<String, String> onLoadingDone);

	/**
	 * Create an HtmlImage widget that represents an HTML IMG element.
	 * 
	 * @param src Pointer to the actual image.
	 * @param bbox The bounding box of the image.
	 * @param onLoadingDone Call-back to be executed when the image finished loading, or when an error occurs while
	 *        loading.
	 * @param nrRetries Total number of retries should loading fail. Default is 0.
	 */
	HtmlImage create(String src, Bbox bbox, Callback<String, String> onLoadingDone,
			int nrRetries);
}