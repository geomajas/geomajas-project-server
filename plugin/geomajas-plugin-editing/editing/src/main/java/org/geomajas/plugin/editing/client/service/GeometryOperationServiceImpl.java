/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.editing.client.service;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.command.dto.BufferInfo;
import org.geomajas.command.dto.GeometryBufferRequest;
import org.geomajas.command.dto.GeometryBufferResponse;
import org.geomajas.command.dto.GeometryConvexHullRequest;
import org.geomajas.command.dto.GeometryConvexHullResponse;
import org.geomajas.command.dto.GeometryMergeRequest;
import org.geomajas.command.dto.GeometryMergeResponse;
import org.geomajas.command.dto.UnionInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Geometry;
import org.geomajas.geometry.service.GeometryService;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;

import com.google.gwt.core.client.Callback;

/**
 * Service for available operations on {@link Geometry} instances. This class contains implementations for;
 * <ul>
 * <li>Applying a buffer distance on a geometry or several geometries</li>
 * <li>Merging several geometries (or obtaining a union)</li>
 * <li>Obtaining the convex hull of a geometry or several geometries</li>
 * <li>Obtaining the bounds between several geometries</li>
 * </ul>
 * 
 * @author Emiel Ackermann
 *
 */
public class GeometryOperationServiceImpl implements GeometryOperationService {

	public void buffer(Geometry geometry, BufferInfo bufferInfo, final Callback<Geometry, Throwable> callback) {
		List<Geometry> geometries = new ArrayList<Geometry>();
		geometries.add(geometry);
		buffer(geometries, bufferInfo, new Callback<List<Geometry>, Throwable>() {

			public void onFailure(Throwable reason) {
				callback.onFailure(reason);
			}

			public void onSuccess(List<Geometry> result) {
				callback.onSuccess(result.get(0));
			}
			
		});
	}

	public void buffer(List<Geometry> geometries, BufferInfo bufferInfo, 
			final Callback<List<Geometry>, Throwable> callback) {
		GeometryBufferRequest request = new GeometryBufferRequest();
		request.setGeometries(geometries);
		request.setBufferDistance(bufferInfo.getDistance());
		request.setQuadrantSegments(bufferInfo.getQuadrantSegments());
		GwtCommand command = new GwtCommand(GeometryBufferRequest.COMMAND);
		command.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(command, new AbstractCommandCallback<GeometryBufferResponse>() {

			public void execute(GeometryBufferResponse response) {
				callback.onSuccess(response.getGeometries());
				for (Throwable throwable : response.getErrors()) {
					callback.onFailure(throwable);
				}
			}
		});
	}

	public void union(List<Geometry> geometries, UnionInfo unionInfo, final Callback<Geometry, Throwable> callback) {
		GeometryMergeRequest request = new GeometryMergeRequest();
		request.setGeometries(geometries);
		request.setPrecision(unionInfo.getPrecision());
		request.setUsePrecisionAsBuffer(unionInfo.isUsePrecisionAsBuffer());
		GwtCommand command = new GwtCommand(GeometryMergeRequest.COMMAND);
		command.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(command, new AbstractCommandCallback<GeometryMergeResponse>() {

			public void execute(GeometryMergeResponse response) {
				callback.onSuccess(response.getGeometry());
				for (Throwable throwable : response.getErrors()) {
					callback.onFailure(throwable);
				}
			}
		});
	}

	public void convexHull(Geometry geometry, final Callback<Geometry, Throwable> callback) {
		List<Geometry> geometries = new ArrayList<Geometry>();
		geometries.add(geometry);
		convexHull(geometries, new Callback<List<Geometry>, Throwable>() {

			public void onFailure(Throwable reason) {
				callback.onFailure(reason);
			}

			public void onSuccess(List<Geometry> result) {
				callback.onSuccess(result.get(0));
			}
		});
	}

	public void convexHull(List<Geometry> geometries, final Callback<List<Geometry>, Throwable> callback) {
		GeometryConvexHullRequest request = new GeometryConvexHullRequest();
		request.setGeometries(geometries);
		GwtCommand command = new GwtCommand(GeometryConvexHullRequest.COMMAND);
		command.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(command, new AbstractCommandCallback<GeometryConvexHullResponse>() {

			public void execute(GeometryConvexHullResponse response) {
				callback.onSuccess(response.getGeometries());
				for (Throwable throwable : response.getErrors()) {
					callback.onFailure(throwable);
				}
			}
		});
	}

	public void bounds(List<Geometry> geometries, Callback<Bbox, Throwable> callback) {
		try {
			Bbox result = GeometryService.getBounds(geometries.get(0));
			double minX = result.getX();
			double minY = result.getY();
			double maxX = result.getMaxX();
			double maxY = result.getMaxY();
			
			for (int i = 1 ; i < geometries.size() ; i++) {
				Bbox bounds = GeometryService.getBounds(geometries.get(i));
				double boundsX = bounds.getX();
				minX = boundsX < minX ? boundsX : minX;
				
				double boundsY = bounds.getY();
				minY = boundsY < minY ? boundsY : minY;
				
				double boundsMaxX = bounds.getMaxX();
				maxX = boundsMaxX > maxX ? boundsMaxX : maxX;
				
				double boundsMaxY = bounds.getMaxY();
				maxY = boundsMaxY > maxY ? boundsMaxY : maxY;
			}
			result.setX(minX);
			result.setY(minY);
			result.setMaxX(maxX);
			result.setMaxY(maxY);
			callback.onSuccess(result);
		} catch (Exception e) {
			callback.onFailure(e);
		}
	}

}
