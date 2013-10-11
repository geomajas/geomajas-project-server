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
package com.smartgwt.client.widgets.events;

import com.google.gwt.core.client.JavaScriptObject;


/**
 * Equivalent of SmartGWT CloseClickEvent class to assure @Api is not broken.
 *
 * @deprecated in Geomajas context use {@link CloseClickEvent}
 * @author Oliver May
 */
@Deprecated
public class CloseClientEvent extends CloseClickEvent {

	/**
	 * @see CloseClickEvent.
	 * 
	 * @param jsObj
	 */
	public CloseClientEvent(JavaScriptObject jsObj) {
		super(jsObj);
	}

}
