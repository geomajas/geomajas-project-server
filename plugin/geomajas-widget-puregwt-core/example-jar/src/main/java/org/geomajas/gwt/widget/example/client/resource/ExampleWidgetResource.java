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
package org.geomajas.gwt.widget.example.client.resource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * Client resource bundle interface for pure GWT widgets example project.
 *
 * @author Dosi Bingov
 * @since 1.0.0
 */
public interface ExampleWidgetResource extends ClientBundle {

	/**
	 * Instance for use outside UIBinder.
	 */
	ExampleWidgetResource INSTANCE = GWT.create(ExampleWidgetResource.class);

	/**
	 * Get the css resource.
	 *
	 * @return the css resource
	 */
	@Source("example-widget-core.css")
	ExampleWidgetCssResource css();

	/**
	 * Get the css resource.
	 *
	 * @return Image resource og geomajas logo.
	 */
	@Source("image/logo-geomajas.png")
	ImageResource geomajasLogo();
}