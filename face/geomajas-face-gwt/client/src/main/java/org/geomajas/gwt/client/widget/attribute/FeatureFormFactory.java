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

package org.geomajas.gwt.client.widget.attribute;

import org.geomajas.annotation.Api;
import org.geomajas.annotation.UserImplemented;
import org.geomajas.gwt.client.map.layer.VectorLayer;

import com.google.gwt.user.client.ui.Widget;

/**
 * Factory that creates a {@link FeatureForm} for all layers of an application. Used by editors such as
 * {@link org.geomajas.gwt.client.widget.FeatureAttributeEditor}.
 * 
 * @param <W> the widget class of the form
 * 
 * @author Pieter De Graef
 * @author Jan De Moerloose
 * @since 1.11.1
 */
@Api(allMethods = true)
@UserImplemented
public interface FeatureFormFactory<W extends Widget> {

	/**
	 * Creates a form using the specified attribute information.
	 * 
	 * @param layer layer for which a form needs to be created.
	 * @return An attribute form that allows for editing of it's values.
	 */
	FeatureForm<W> createFeatureForm(VectorLayer layer);
}