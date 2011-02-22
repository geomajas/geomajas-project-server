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

package org.geomajas.puregwt.client.widget;

import org.geomajas.puregwt.client.map.WorldContainer;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * ...
 * 
 * @author Jan De Moerloose
 */
public class MapWidgetImpl extends Widget implements MapWidget {

	@Inject
	public MapWidgetImpl() {
		DivElement container = Document.get().createDivElement();
		setElement(container);
	}

}