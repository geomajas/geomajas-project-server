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
package org.geomajas.widget.searchandfilter.client.util;

import java.util.List;

import org.geomajas.command.CommandResponse;
import org.geomajas.gwt.client.command.CommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.spatial.geometry.Geometry;
import org.geomajas.gwt.client.util.GeometryConverter;
import org.geomajas.widget.searchandfilter.command.dto.GeometryUtilsRequest;
import org.geomajas.widget.searchandfilter.command.dto.GeometryUtilsResponse;

/**
 * Convenience class with helper methods for commands.
 *
 * @author Kristof Heirwegh
 */
public final class CommService {

	/**
	 * Utility class
	 */
	private CommService() {
	}

	/**
	 * The returned result will contain an unbuffered as well as buffered result.
	 * @param geoms
	 * @param buffer
	 * @param onFinished
	 */
	public static void mergeAndBufferGeometries(List<Geometry> geoms, double buffer,
			final DataCallback<Geometry> onFinished) {
		GeometryUtilsRequest request = new GeometryUtilsRequest();
		request.setActionFlags(GeometryUtilsRequest.ACTION_BUFFER | GeometryUtilsRequest.ACTION_MERGE);
		request.setIntermediateResults(true);
		request.setBuffer(buffer);
		request.setGeometries(toDtoGeometries(geoms));

		GwtCommand command = new GwtCommand("command.searchandfilter.GeometryUtils");
		command.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(command, new CommandCallback() {
			public void execute(CommandResponse response) {
				if (response instanceof GeometryUtilsResponse) {
					GeometryUtilsResponse resp = (GeometryUtilsResponse) response;
					if (onFinished != null) {
						onFinished.execute(GeometryConverter.toGwt(resp.getGeometries()[0]));
					}
				}
			}
		});
	}

	/**
	 * The returned result will contain an unbuffered as well as buffered result.
	 * @param geoms
	 * @param buffer
	 * @param onFinished
	 */
	public static void mergeGeometries(List<Geometry> geoms, final DataCallback<Geometry> onFinished) {
		GeometryUtilsRequest request = new GeometryUtilsRequest();
		request.setActionFlags(GeometryUtilsRequest.ACTION_MERGE);
		request.setGeometries(toDtoGeometries(geoms));

		GwtCommand command = new GwtCommand("command.searchandfilter.GeometryUtils");
		command.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(command, new CommandCallback() {
			public void execute(CommandResponse response) {
				if (response instanceof GeometryUtilsResponse) {
					GeometryUtilsResponse resp = (GeometryUtilsResponse) response;
					if (onFinished != null) {
						onFinished.execute(GeometryConverter.toGwt(resp.getGeometries()[0]));
					}
				}
			}
		});
	}

	public static org.geomajas.geometry.Geometry[] toDtoGeometries(List<Geometry> geoms) {
		org.geomajas.geometry.Geometry[] dtoGeoms = new org.geomajas.geometry.Geometry[geoms.size()];
		for (int i = 0; i < geoms.size(); i++) {
			dtoGeoms[i] = GeometryConverter.toDto(geoms.get(i));
		}
		return dtoGeoms;
	}

	/**
	 * The returned result will contain an unbuffered as well as buffered result.
	 * @param geoms
	 * @param buffer
	 * @param onFinished
	 */
	public static void searchByLocation(Geometry geometry, double buffer,
			final DataCallback<GeometryUtilsResponse> onFinished) {
// TODO
		//		GeometryUtilsRequest request = new GeometryUtilsRequest();
//		request.setActionFlags(GeometryUtilsRequest.ACTION_BUFFER | GeometryUtilsRequest.ACTION_MERGE);
//		request.setBuffer(buffer);
//		request.setGeometries((Geometry[]) geoms.toArray());
//
//		GwtCommand command = new GwtCommand("command.searchandfilter.GeometryUtils");
//		command.setCommandRequest(request);
//		GwtCommandDispatcher.getInstance().execute(command, new CommandCallback() {
//			public void execute(CommandResponse response) {
//				if (response instanceof GeometryUtilsResponse) {
//					GeometryUtilsResponse resp = (GeometryUtilsResponse) response;
//					if (onFinished != null) {
//						onFinished.execute(resp);
//					}
//				}
//			}
//		});
	}

}
