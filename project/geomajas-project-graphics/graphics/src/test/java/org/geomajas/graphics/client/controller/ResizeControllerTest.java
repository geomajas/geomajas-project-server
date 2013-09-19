/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.graphics.client.controller;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.geometry.service.BboxService;
import org.geomajas.graphics.client.controller.ResizeController.ResizeHandler;
import org.geomajas.graphics.client.service.GraphicsObjectContainer;
import org.geomajas.graphics.client.service.GraphicsObjectContainer.Space;
import org.geomajas.graphics.client.service.GraphicsService;
import org.geomajas.graphics.client.service.GraphicsServiceImpl;
import org.geomajas.graphics.client.util.BboxPosition;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.web.bindery.event.shared.SimpleEventBus;

public class ResizeControllerTest {
	
	@Mock
	private GraphicsObjectContainer objectContainer;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		when(objectContainer.transform(new Coordinate(105, 110), Space.SCREEN, Space.USER)).thenReturn(new Coordinate(105, 110));
		when(objectContainer.transform(new Coordinate(100, 100), Space.SCREEN, Space.USER)).thenReturn(new Coordinate(100, 100));
		when(objectContainer.transform(BboxPosition.CORNER_LL, Space.SCREEN, Space.USER)).thenReturn(BboxPosition.CORNER_UL);
		when(objectContainer.transform(BboxPosition.CORNER_LR, Space.SCREEN, Space.USER)).thenReturn(BboxPosition.CORNER_UR);
		when(objectContainer.transform(BboxPosition.CORNER_UL, Space.SCREEN, Space.USER)).thenReturn(BboxPosition.CORNER_LL);
		when(objectContainer.transform(BboxPosition.CORNER_UR, Space.SCREEN, Space.USER)).thenReturn(BboxPosition.CORNER_LR);
		when(objectContainer.transform(BboxPosition.MIDDLE_LEFT, Space.SCREEN, Space.USER)).thenReturn(BboxPosition.MIDDLE_LEFT);
		when(objectContainer.transform(BboxPosition.MIDDLE_LOW, Space.SCREEN, Space.USER)).thenReturn(BboxPosition.MIDDLE_UP);
		when(objectContainer.transform(BboxPosition.MIDDLE_RIGHT, Space.SCREEN, Space.USER)).thenReturn(BboxPosition.MIDDLE_RIGHT);
		when(objectContainer.transform(BboxPosition.MIDDLE_UP, Space.SCREEN, Space.USER)).thenReturn(BboxPosition.MIDDLE_LOW);		
	}


	@Test
	public void testResize() {
		MockResizable m = new MockResizable(new Coordinate(5, 6), new Bbox(0, 0, 50, 50));
		SimpleEventBus eventBus = new SimpleEventBus();
		GraphicsService service = new GraphicsServiceImpl(eventBus, false);
		service.setObjectContainer(objectContainer);
		ResizeController r = new ResizeController(m, service, true);
		List<ResizeHandler> handlers = new ArrayList<ResizeHandler>();
		for (BboxPosition type : BboxPosition.values()) {
			handlers.add(r.new ResizeHandler(type));
		}
		for (ResizeHandler h : handlers) {
			h.onDragStart(100, 100);
			h.onDragStop(105, 110, false);
			switch (h.getBboxPosition()) {
				case CORNER_LL:
					Assert.assertTrue(BboxService.equals(m.getUserBounds(), new Bbox(5, 0, 45, 60), 0.0001));
					break;
				case CORNER_LR:
					Assert.assertTrue(BboxService.equals(m.getUserBounds(), new Bbox(0, 0, 55, 60), 0.0001));
					break;
				case CORNER_UL:
					Assert.assertTrue(BboxService.equals(m.getUserBounds(), new Bbox(5, 10, 45, 40), 0.0001));
					break;
				case CORNER_UR:
					Assert.assertTrue(BboxService.equals(m.getUserBounds(), new Bbox(0, 10, 55, 40), 0.0001));
					break;
				case MIDDLE_LEFT:
					Assert.assertTrue(BboxService.equals(m.getUserBounds(), new Bbox(5, 0, 45, 50), 0.0001));
					break;
				case MIDDLE_LOW:
					Assert.assertTrue(BboxService.equals(m.getUserBounds(), new Bbox(0, 0, 50, 60), 0.0001));
					break;
				case MIDDLE_RIGHT:
					Assert.assertTrue(BboxService.equals(m.getUserBounds(), new Bbox(0, 0, 55, 50), 0.0001));
					break;
				case MIDDLE_UP:
					Assert.assertTrue(BboxService.equals(m.getUserBounds(), new Bbox(0, 10, 50, 40), 0.0001));
					break;
			}
			service.undo();
		}
	}

	@Test
	public void testDrag() {
		MockResizable m = new MockResizable(new Coordinate(5, 6), new Bbox(0, 0, 50, 50));
		SimpleEventBus eventBus = new SimpleEventBus();
		GraphicsService service = new GraphicsServiceImpl(eventBus, false);
		service.setObjectContainer(objectContainer);
		ResizeController r = new ResizeController(m, service, true);
		GraphicsObjectDragHandler h = new GraphicsObjectDragHandler(m, service, r);
		h.onDragStart(100, 100);
		h.onDragStop(105, 110);
		Assert.assertTrue(m.getPosition().equalsDelta(new Coordinate(10, 16), 0.0001));
		service.undo();
		Assert.assertTrue(m.getPosition().equalsDelta(new Coordinate(5, 6), 0.0001));
	}

}
