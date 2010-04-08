/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.geomajas.command.dto;

import org.geomajas.command.CommandRequest;
import org.geomajas.geometry.Geometry;
import org.geomajas.global.Api;

/**
 * Request object for {@link org.geomajas.command.geometry.MergePolygonCommand}.
 *
 * @author Joachim Van der Auwera
 */
@Api(allMethods = true)
public class MergePolygonRequest implements CommandRequest {

	private static final long serialVersionUID = 151L;

	private Geometry[] polygons;

	private boolean allowMultiPolygon;

	public Geometry[] getPolygons() {
		return polygons;
	}

	public void setPolygons(Geometry[] polygons) {
		this.polygons = polygons;
	}

	public boolean isAllowMultiPolygon() {
		return allowMultiPolygon;
	}

	public void setAllowMultiPolygon(boolean allowMultiPolygon) {
		this.allowMultiPolygon = allowMultiPolygon;
	}
}
