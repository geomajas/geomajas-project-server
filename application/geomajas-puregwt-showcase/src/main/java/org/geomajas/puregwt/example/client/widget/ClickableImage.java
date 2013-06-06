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

package org.geomajas.puregwt.example.client.widget;

import org.geomajas.puregwt.example.client.Showcase;

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Image;

/**
 * Clickable image that displays a different image resource when the mouse hovers over it.
 * 
 * @author Pieter De Graef
 */
public class ClickableImage extends Image {

	private ImageResource normalResource;

	private ImageResource hoverResource;

	public ClickableImage() {
		super(Showcase.RESOURCE.iconArrowDown()); // Default, otherwiese this class is not initiated correctly...
		addStyleName(Showcase.RESOURCE.css().clickable());
		addMouseOverHandler(new MouseOverHandler() {

			public void onMouseOver(MouseOverEvent event) {
				if (hoverResource != null) {
					setResource(hoverResource);
				}
			}
		});
		addMouseOutHandler(new MouseOutHandler() {

			public void onMouseOut(MouseOutEvent event) {
				if (normalResource != null) {
					setResource(normalResource);
				}
			}
		});
	}

	// ------------------------------------------------------------------------
	// Getters and setters:
	// ------------------------------------------------------------------------

	public ImageResource getNormalResource() {
		return normalResource;
	}

	public void setNormalResource(ImageResource normalResource) {
		this.normalResource = normalResource;
		setResource(normalResource);
	}

	public ImageResource getHoverResource() {
		return hoverResource;
	}

	public void setHoverResource(ImageResource hoverResource) {
		this.hoverResource = hoverResource;
	}
}