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

import org.geomajas.geometry.Bbox;

import com.google.gwt.core.client.Callback;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import org.geomajas.gwt.client.util.Dom;

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
 */
public class HtmlImageImpl extends AbstractHtmlObject implements HtmlImage {

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	/**
	 * Create an HtmlImage widget that represents an HTML IMG element.
	 * 
	 * @param src Pointer to the actual image.
	 * @param bbox The bounding box of the image.
	 */
	@AssistedInject
	HtmlImageImpl(@Assisted String src, @Assisted Bbox bbox) {
		this(src, bbox, null);
	}

	/**
	 * Create an HtmlImage widget that represents an HTML IMG element.
	 * 
	 * @param src Pointer to the actual image.
	 * @param bbox The bounding box of the image.
	 * @param onLoadingDone Call-back to be executed when the image finished loading, or when an error occurs while
	 *        loading.
	 */
	@AssistedInject
	HtmlImageImpl(@Assisted String src, @Assisted Bbox bbox, @Assisted Callback<String, String> onLoadingDone) {
		this(src, bbox, onLoadingDone, 0);
	}

	/**
	 * Create an HtmlImage widget that represents an HTML IMG element.
	 * 
	 * @param src Pointer to the actual image.
	 * @param bbox The bounding box of the image.
	 * @param onLoadingDone Call-back to be executed when the image finished loading, or when an error occurs while
	 *        loading.
	 * @param nrRetries Total number of retries should loading fail. Default is 0.
	 */
	@AssistedInject
	HtmlImageImpl(@Assisted String src, @Assisted Bbox bbox, @Assisted Callback<String, String> onLoadingDone,
			@Assisted int nrRetries) {
		this(src, (int) bbox.getWidth(), (int) bbox.getHeight(), (int) bbox.getY(), (int) bbox.getX(), onLoadingDone,
				nrRetries);
	}

	/**
	 * Create an HtmlImage widget that represents an HTML IMG element.
	 * 
	 * @param src Pointer to the actual image.
	 * @param width The width for this image, expressed in pixels.
	 * @param height The height for this image, expressed in pixels.
	 * @param top How many pixels should this image be placed from the top (relative to the parent origin).
	 * @param left How many pixels should this image be placed from the left (relative to the parent origin).
	 */
	public HtmlImageImpl( String src,  int width,  int height,  int top, int left) {
		this(src, width, height, top, left, null);
	}

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
	public HtmlImageImpl( String src,  int width,  int height,  int top,
			 int left,  Callback<String, String> onLoadingDone) {
		this(src, width, height, top, left, onLoadingDone, 0);
	}

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
	public HtmlImageImpl( String src,  int width,  int height,  int top,
			 int left,  Callback<String, String> onLoadingDone,  int nrRetries) {
		super("img", width, height, top, left);

		DOM.setStyleAttribute(getElement(), "border", "none");
		DOM.setElementProperty(getElement(), "src", Dom.makeUrlAbsolute(src));
		// set visible when loaded !
		setVisible(false);
		onLoadingDone(onLoadingDone, nrRetries);
	}

	// ------------------------------------------------------------------------
	// HtmlImage implementation:
	// ------------------------------------------------------------------------

	/**
	 * Apply a call-back that is executed when the image is done loading. This image is done loading when it has either
	 * loaded successfully or when 5 attempts have failed. In any case, the callback's execute method will be invoked,
	 * thereby indicating success or failure.
	 * 
	 * @param onLoadingDone The call-back to be executed when loading has finished. The boolean value indicates whether
	 *        or not it was successful while loading. Both the success and failure type expect a String. This is used to
	 *        pass along the image URL.
	 * @param nrRetries Total number of retries should loading fail. Default is 0.
	 */
	public void onLoadingDone(Callback<String, String> onLoadingDone, int nrRetries) {
		DOM.sinkEvents(getElement(), Event.ONLOAD | Event.ONERROR);
		ImageReloader reloader = new ImageReloader(getSrc(), onLoadingDone, nrRetries);
		addHandler(reloader, LoadEvent.getType());
		addHandler(reloader, ErrorEvent.getType());
	}

	/**
	 * Get the pointer to the actual image. In HTML this is represented by the 'src' attribute in an IMG element.
	 * 
	 * @return The pointer to the actual image.
	 */
	public String getSrc() {
		return DOM.getElementProperty(getElement(), "src");
	}

	/**
	 * Set the pointer to the actual image. In HTML this is represented by the 'src' attribute in an IMG element.
	 * 
	 * @param src The new image pointer.
	 */
	public void setSrc(String src) {
		DOM.setImgSrc(getElement(), src);
	}

	/**
	 * DOM event handler that attempts up to 5 times to reload the requested image. When the image is loaded (or the 5
	 * attempts have failed), it notifies the given {@link Callback}, calling the execute method with true or false
	 * indicating whether or not the image was really loaded.
	 * 
	 * @author Pieter De Graef
	 */
	public class ImageReloader implements LoadHandler, ErrorHandler {

		private int nrAttempts = 5;

		private String src;

		private Callback<String, String> onDoneLoading;

		public ImageReloader(String src, Callback<String, String> onDoneLoading, int nrRetries) {
			this.src = src;
			this.onDoneLoading = onDoneLoading;
			this.nrAttempts = nrRetries + 1;
		}

		public void onLoad(LoadEvent event) {
			setVisible(true);
			if (onDoneLoading != null) {
				onDoneLoading.onSuccess(src);
			}
		}

		public void onError(ErrorEvent event) {
			nrAttempts--;
			if (nrAttempts > 0) {
				DOM.setImgSrc(getElement(), src);
			} else if (onDoneLoading != null) {
				onDoneLoading.onFailure(src);
			}
		}
	}
}