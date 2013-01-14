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
package org.geomajas.widget.searchandfilter.command.dto;

import org.geomajas.command.EmptyCommandRequest;

/**
 * Request for {@link org.geomajas.widget.searchandfilter.command.searchandfilter.GetSearchFavouritesCommand}.
 * 
 * @author Kristof Heirwegh
 */
public class GetSearchFavouritesRequest extends EmptyCommandRequest {

	private static final long serialVersionUID = 100L;

	public static final String COMMAND = "command.searchandfilter.GetSearchFavourites";

}
