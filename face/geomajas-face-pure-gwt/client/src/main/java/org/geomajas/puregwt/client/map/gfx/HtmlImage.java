/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.puregwt.client.map.gfx;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;

/**
 * <p>
 * Extension of an HtmlObject that represents a single HTML image (IMG tag). Next to the default values provided by the
 * HtmlObject, an extra 'href' field is provided. This string value should point to the actual image.
 * </p>
 * <p>
 * If for any reason the loading of the actual images results in errors, this class will automatically retry a number of
 * times. It is possible to add an {@link EventListener} to the constructors, that is called when the image is loaded.
 * If there where too many errors and no more attempts are made, this event listener is called as well.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class HtmlImage extends HtmlObject {

	private static final int NR_RETRIES = 5;

	// ------------------------------------------------------------------------
	// Constructors:
	// ------------------------------------------------------------------------

	/**
	 * Create an HtmlImage widget that represents an HTML IMG element.
	 * 
	 * @param href
	 *            Pointer to the actual image.
	 * @param width
	 *            The width for this image, expressed in pixels.
	 * @param height
	 *            The height for this image, expressed in pixels.
	 * @param top
	 *            How many pixels should this image be placed from the top (relative to the parent origin).
	 * @param left
	 *            How many pixels should this image be placed from the left (relative to the parent origin).
	 */
	public HtmlImage(String href, int width, int height, int top, int left) {
		this(href, width, height, top, left, null);
	}

	/**
	 * Create an HtmlImage widget that represents an HTML IMG element.
	 * 
	 * @param href
	 *            Pointer to the actual image.
	 * @param width
	 *            The width for this image, expressed in pixels.
	 * @param height
	 *            The height for this image, expressed in pixels.
	 * @param top
	 *            How many pixels should this image be placed from the top (relative to the parent origin).
	 * @param left
	 *            How many pixels should this image be placed from the left (relative to the parent origin).
	 * @param onLoadListener
	 *            Extra listener that is notified when the actual image is loaded. This happens through the
	 *            "Event.ONLOAD" event.
	 */
	public HtmlImage(String href, int width, int height, int top, int left, EventListener onLoadListener) {
		super("img", width, height, top, left);

		DOM.sinkEvents(getElement(), Event.ONLOAD | Event.ONERROR);
		DOM.setEventListener(getElement(), new RetryLoadListener(href, onLoadListener));
		DOM.setElementProperty(getElement(), "src", href);
	}

	// ------------------------------------------------------------------------
	// Getters and setters:
	// ------------------------------------------------------------------------

	/**
	 * Get the pointer to the actual image. In HTML this is represented by the 'src' attribute in an IMG element.
	 * 
	 * @return The pointer to the actual image.
	 */
	public String getHref() {
		return DOM.getElementProperty(getElement(), "src");
	}

	/**
	 * Set the pointer to the actual image. In HTML this is represented by the 'src' attribute in an IMG element.
	 * 
	 * @param href
	 *            The new image pointer.
	 */
	public void setHref(String href) {
		DOM.setElementProperty(getElement(), "src", href);
	}

	// ------------------------------------------------------------------------
	// Private classes:
	// ------------------------------------------------------------------------

	/**
	 * Listener attached to images that try to fetch an image multiple times in case some network error occurred and the
	 * image did not arrive correctly.
	 */
	private class RetryLoadListener implements EventListener {

		private int retries = NR_RETRIES;

		private String href;

		private EventListener onLoadListener;

		public RetryLoadListener(String href, EventListener onLoadListener) {
			this.href = href;
			this.onLoadListener = onLoadListener;
		}

		public void onBrowserEvent(Event event) {
			switch (DOM.eventGetType(event)) {
				case Event.ONLOAD:
					if (onLoadListener != null) {
						onLoadListener.onBrowserEvent(event);
					}
					break;
				case Event.ONERROR:
					retries--;
					if (retries > 0) {
						DOM.setElementProperty(getElement(), "src", href);
					} else if (onLoadListener != null) {
						onLoadListener.onBrowserEvent(event);
					}
					break;
			}
		}
	}
}