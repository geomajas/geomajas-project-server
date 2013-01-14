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

package org.geomajas.plugin.editing.client.service;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.Geometry;
import org.geomajas.plugin.editing.client.operation.GeometryOperationFailedException;
import org.junit.Before;
import org.junit.Test;

import com.google.gwt.core.client.Callback;

/**
 * Test case testing the method bounds() in the {@link GeometryOperationService}. 
 * Other methods call Gwt.create() (through GwtCommandDispatcher.getInstance()) so these are not tested here.
 * 
 * @author Pieter De Graef
 */
public class GeometryOperationServiceImplTest {

	private GeometryOperationService service = new GeometryOperationServiceImpl();
	
	@Before
	public void before() {
		
	}

	@Test
	public void testBounds() throws GeometryOperationFailedException {
		List <Geometry> geometries = new ArrayList<Geometry>();
		
		Geometry upperLeft = new Geometry(Geometry.LINE_STRING, 2, 0);
		upperLeft.setCoordinates(new Coordinate[]{
				new Coordinate(0, 200), new Coordinate(50, 300)				
		});
		geometries.add(upperLeft);
		
		Geometry upperRight = new Geometry(Geometry.POINT, 3, 0);
		upperRight.setCoordinates(new Coordinate[]{
				new Coordinate(500, 500)				
		});
		geometries.add(upperRight);
		
		Geometry lowerRight = new Geometry(Geometry.POINT, 3, 0);
		lowerRight.setCoordinates(new Coordinate[]{
				new Coordinate(500, 150)				
		});
		geometries.add(lowerRight);
		
		Geometry lowerLeft = new Geometry(Geometry.POLYGON, 1, 0);
		Geometry linearRing = new Geometry(Geometry.LINEAR_RING, 1, 0);
		linearRing.setCoordinates(new Coordinate[]{
				new Coordinate(0, 0), new Coordinate(0, 10), new Coordinate(5, 10), new Coordinate(0, 0)		
		});
		lowerLeft.setGeometries(new Geometry[]{linearRing});
		geometries.add(lowerLeft);
		service.bounds(geometries, new Callback<Bbox, Throwable>() {
			
			public void onSuccess(Bbox result) {
				Assert.assertEquals(0.0d, result.getX());
				Assert.assertEquals(0.0d, result.getY());
				Assert.assertEquals(500d, result.getMaxX());
				Assert.assertEquals(500d, result.getMaxY());
			}
			
			public void onFailure(Throwable reason) {
			}
		});
	}
	
	@Test
	public void testBounds1() throws GeometryOperationFailedException {
		List <Geometry> geometries = new ArrayList<Geometry>();
		
		Geometry upperLeft = new Geometry(Geometry.LINE_STRING, 2, 0);
		upperLeft.setCoordinates(new Coordinate[]{
				new Coordinate(0, 200), new Coordinate(50, 300)				
		});
		geometries.add(upperLeft);
		
		Geometry upperRight = new Geometry(Geometry.POINT, 3, 0);
		upperRight.setCoordinates(new Coordinate[]{
				new Coordinate(500, 500)				
		});
		geometries.add(upperRight);
		
		Geometry lowerRight = new Geometry(Geometry.POINT, 3, 0);
		lowerRight.setCoordinates(new Coordinate[]{
				new Coordinate(500, -150)				
		});
		geometries.add(lowerRight);
		
		Geometry lowerLeft = new Geometry(Geometry.POLYGON, 1, 0);
		Geometry linearRing = new Geometry(Geometry.LINEAR_RING, 1, 0);
		linearRing.setCoordinates(new Coordinate[]{
				new Coordinate(-30, 0), new Coordinate(0, 10), new Coordinate(5, 10), new Coordinate(-30, 0)		
		});
		lowerLeft.setGeometries(new Geometry[]{linearRing});
		geometries.add(lowerLeft);
		service.bounds(geometries, new Callback<Bbox, Throwable>() {
			
			public void onSuccess(Bbox result) {
				Assert.assertEquals(-30d, result.getX());
				Assert.assertEquals(-150d, result.getY());
				Assert.assertEquals(500d, result.getMaxX());
				Assert.assertEquals(500d, result.getMaxY());
			}
			
			public void onFailure(Throwable reason) {
			}
		});
	}
}