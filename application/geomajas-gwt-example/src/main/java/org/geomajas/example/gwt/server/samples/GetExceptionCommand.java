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

package org.geomajas.example.gwt.server.samples;

import org.geomajas.command.Command;
import org.geomajas.command.CommandResponse;
import org.geomajas.command.EmptyCommandRequest;
import org.geomajas.example.gwt.client.samples.base.GetResourcesResponse;
import org.springframework.stereotype.Component;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * <p>
 * Command that generates an exception with stack trace.
 * </p>
 * 
 * @author Pieter De Graef
 */
@Component
public class GetExceptionCommand implements Command<EmptyCommandRequest, CommandResponse> {

	public void execute(EmptyCommandRequest request, CommandResponse response) throws Exception {
		throw new UnserializableException();
	}

	public CommandResponse getEmptyCommandResponse() {
		return new GetResourcesResponse();
	}

	/**
	 * Exception that should be difficult to serialize.
	 * 
	 * @author Pieter De Graef
	 */
	public class UnserializableException extends Exception {

		private Geometry geometry;

		private static final long serialVersionUID = 180L;

		public UnserializableException() {
			super("Oops. I guess the server encountered some 'unexpected' exception.");
			GeometryFactory factory = new GeometryFactory();
			geometry = factory.createLineString(new Coordinate[] { new Coordinate(0, 0), new Coordinate(10, 0) });
		}

		public Geometry getGeometry() {
			return geometry;
		}

		public void setGeometry(Geometry geometry) {
			this.geometry = geometry;
		}
	}
}
