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
package org.geomajas.widget.featureinfo.client.widget.factory;

import org.geomajas.gwt.client.map.feature.Feature;

import com.smartgwt.client.widgets.Window;

/**
 * A convenience interface to create FeatureDetail builders.
 * @author Kristof Heirwegh
 *
 */
public interface FeatureDetailWidgetBuilder extends WidgetBuilder {

	/**
	 * This method is optional, just return null if you don't have a special Window version of your detailpanel.
	 * A detailcanvas will be requested & wrapped in a window
	 * 
	 * @param feature
	 * @param editingAllowed
	 * @return
	 */
	Window createFeatureDetailWindow(Feature feature, boolean editingAllowed);
	
}
