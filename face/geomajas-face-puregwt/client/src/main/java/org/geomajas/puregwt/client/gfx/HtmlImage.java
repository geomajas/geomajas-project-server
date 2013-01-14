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

package org.geomajas.puregwt.client.gfx;

import org.geomajas.annotation.FutureApi;

import com.google.gwt.core.client.Callback;

/**
 * <p>
 * Extension of an HtmlObject that represents a single HTML image (IMG tag). Next to the default values provided by the
 * HtmlObject, an extra 'src' field is provided. This string value should point to the actual image.
 * </p>
 * <p>
 * Instances of this class can be initiated with a {@link Callback} that is notified when the image is done loading. The
 * image is done loading when it has either loaded successfully or when 5 attempts have failed. In any case, the
 * callback's execute method will be invoked, thereby indicating success or failure.
 * </p>
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@FutureApi
public interface HtmlImage extends HtmlObject {

	/**
	 * Apply a call-back that is executed when the image is done loading. This image is done loading when it has either
	 * loaded successfully or when 5 attempts have failed. In any case, the callback's execute method will be invoked,
	 * thereby indicating success or failure.
	 * 
	 * @param onLoadingDone
	 *            The call-back to be executed when loading has finished. The boolean value indicates whether or not it
	 *            was successful while loading.
	 * @param nrRetries
	 *            Total number of retries should loading fail. Default is 0.
	 */
	void onLoadingDone(Callback<String, String> onLoadingDone, int nrRetries);

	/**
	 * Get the pointer to the actual image. In HTML this is represented by the 'src' attribute in an IMG element.
	 * 
	 * @return The pointer to the actual image.
	 */
	String getSrc();

	/**
	 * Set the pointer to the actual image. In HTML this is represented by the 'src' attribute in an IMG element.
	 * 
	 * @param src
	 *            The new image pointer.
	 */
	void setSrc(String src);
}