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

package org.geomajas.puregwt.client;

import org.geomajas.puregwt.client.widget.MapWidget;
import org.geomajas.puregwt.client.widget.MapWidgetImpl;

import com.google.gwt.inject.client.AbstractGinModule;

/**
 * Module definition.
 *
 * @author Jan De Moerloose
 */
public class PureGwtExampleModule extends AbstractGinModule {

	protected void configure() {
		bind(MapWidget.class).to(MapWidgetImpl.class);
	}
}