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

package org.geomajas.puregwt.client.map.layer;

import org.geomajas.annotation.Api;
import org.geomajas.sld.RuleInfo;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * Presenter for a layer style. Note that it's a widget! This presenter should always be able to present the style for a
 * layer. Just add it to the GUI.
 * 
 * @author Pieter De Graef
 * @since 1.0.0
 */
@Api(allMethods = true)
public interface LayerStylePresenter extends IsWidget {

	/**
	 * Get the style index. This is used to order different styles within a layer.
	 * 
	 * @return The style index within a layer.
	 */
	int getIndex();

	/**
	 * Get the SLD rule for this style if it presents a style-able layer.
	 * 
	 * @return the rule
	 */
	RuleInfo getRule();
}