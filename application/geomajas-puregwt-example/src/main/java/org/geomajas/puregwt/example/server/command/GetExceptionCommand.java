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

package org.geomajas.puregwt.example.server.command;

import org.geomajas.command.Command;
import org.geomajas.command.CommandResponse;
import org.geomajas.command.EmptyCommandRequest;
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
		return new CommandResponse();
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
			super("Oops. I guess the server encountered some 'unexpected' exception.", new NullPointerException());
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
