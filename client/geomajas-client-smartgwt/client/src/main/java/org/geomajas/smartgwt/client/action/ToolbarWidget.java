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

package org.geomajas.smartgwt.client.action;

import com.google.gwt.user.client.ui.Widget;

/**
 * Interface which marks a {@link ToolbarBaseAction} to indicate that it provides a GWT {@link Widget} to include in the
 * toolbar.
 * <p/>
 * The other methods of the {@link ToolbarBaseAction} will not be used in this case (they need to be provided to comply
 * with the api).
 *
 * @author Joachim Van der Auwera
 * @since 1.10.0
 */
public interface ToolbarWidget {

	/**
	 * Get the GWT widget which needs to be included in the toolbar.
	 *
	 * @return widget to include in toolbar
	 */
	Widget getWidget();

}
