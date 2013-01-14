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
 * DTO object for PageComponent.
 * 
 * @author Jan De Moerloose
 * @see org.geomajas.plugin.printing.component.PageComponent
 * @since 2.0.0
 *
 */
@Api(allMethods = true)
public class PageComponentInfo extends PrintComponentInfo implements Serializable {

	private static final long serialVersionUID = 200L;

}
