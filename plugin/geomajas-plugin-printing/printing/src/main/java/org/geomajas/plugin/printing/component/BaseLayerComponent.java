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
package org.geomajas.plugin.printing.component;

import org.geomajas.plugin.printing.component.dto.PrintComponentInfo;

/**
 * Base layer component for printing.
 * 
 * @author Jan De Moerloose
 *
 * @param <T> DTO object class
 */
public interface BaseLayerComponent<T extends PrintComponentInfo> extends PrintComponent<T> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.plugin.printing.component.IBaseLayerComponent#isVisible()
	 */
	boolean isVisible();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.plugin.printing.component.IBaseLayerComponent#isSelected()
	 */
	boolean isSelected();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.geomajas.plugin.printing.component.IBaseLayerComponent#getLayerId()
	 */
	String getLayerId();

}