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
package org.geomajas.gwt.client.util.impl;

/**
 * Extends {@link DomImpl} for IE9 browser.
 * 
 * @author Jan De Moerloose
 * 
 */
public class DomImplIE9 extends DomImpl {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isIE() {
		return true;
	}

}
