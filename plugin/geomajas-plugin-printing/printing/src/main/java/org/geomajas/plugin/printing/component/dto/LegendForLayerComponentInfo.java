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

package org.geomajas.plugin.printing.component.dto;

import java.io.Serializable;

import org.geomajas.annotation.Api;

/**
 * DTO object for {@link org.geomajas.plugin.printing.component.impl.LegendForLayerComponentImpl}.
 * 
 * @author An Buyle
 * @since 2.4.0
 */
@Api(allMethods = true)
public class LegendForLayerComponentInfo extends PrintComponentInfo implements Serializable {

	private static final long serialVersionUID = 240L;

	/** No-arguments constructor. */
	public LegendForLayerComponentInfo() {
		getLayoutConstraint().setAlignmentX(LayoutConstraintInfo.LEFT);
		getLayoutConstraint().setAlignmentY(LayoutConstraintInfo.BOTTOM); // TODO ???
		getLayoutConstraint().setFlowDirection(LayoutConstraintInfo.FLOW_Y);
		getLayoutConstraint().setMarginX(0);
		getLayoutConstraint().setMarginY(5);
	}
}