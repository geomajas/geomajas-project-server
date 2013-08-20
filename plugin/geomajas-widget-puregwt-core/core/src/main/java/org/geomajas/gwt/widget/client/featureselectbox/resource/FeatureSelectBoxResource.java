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
package org.geomajas.gwt.widget.client.featureselectbox.resource;

import org.geomajas.annotation.Api;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;

/**
 * Client resource bundle interface for pure GWT widgets.
 * 
 * @author Dosi Bingov
 * 
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface FeatureSelectBoxResource extends ClientBundle {

	/**
	 * Instance for use outside UIBinder.
	 */
	FeatureSelectBoxResource INSTANCE = GWT.create(FeatureSelectBoxResource.class);

	/**
	 * Get the css resource.
	 * @return the css resource
	 */
	@Source("featureSelectBox.css")
	FeatureSelectBoxCssResource css();	
	
}