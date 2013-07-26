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

package org.geomajas.gwt.client.map;

import javax.annotation.PostConstruct;

import junit.framework.Assert;

import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.geometry.Bbox;
import org.geomajas.geometry.Coordinate;
import org.geomajas.gwt.client.GeomajasTestModule;
import org.geomajas.gwt.client.event.ViewPortChangedEvent;
import org.geomajas.gwt.client.event.ViewPortChangedHandler;
import org.geomajas.gwt.client.event.ViewPortScaledEvent;
import org.geomajas.gwt.client.event.ViewPortTranslatedEvent;
import org.geomajas.gwt.client.map.ZoomStrategy.ZoomOption;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.web.bindery.event.shared.Event;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * Unit test that checks if the correct events are fired by the ViewPortImpl.
 * 
 * @author Pieter De Graef
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml", "viewPortContext.xml",
		"mapViewPortBeans.xml", "mapBeansNoResolutions.xml", "layerViewPortBeans.xml" })
@DirtiesContext
public class ViewPortEventTest {

	private static final Injector INJECTOR = Guice.createInjector(new GeomajasTestModule());

	@Autowired
	@Qualifier(value = "mapViewPortBeans")
	private ClientMapInfo mapInfo;

	private MapEventBus eventBus;

	private ViewPort viewPort;

	private Event<ViewPortChangedHandler> event;

	@PostConstruct
	public void initialize() {
		eventBus = new MapEventBusImpl(this, INJECTOR.getInstance(EventBus.class));
		viewPort = INJECTOR.getInstance(ViewPort.class);
		viewPort.initialize(mapInfo, eventBus);
		viewPort.setMapSize(1000, 1000);
	}

	@Before
	public void prepareTest() {
		viewPort.applyBounds(viewPort.getMaximumBounds(), ZoomOption.LEVEL_CLOSEST);
		event = null;
	}

	@Test
	public void testApplyPosition() {
		viewPort.applyBounds(viewPort.getMaximumBounds(), ZoomOption.LEVEL_CLOSEST);
		event = null;

		Assert.assertEquals(4.0, viewPort.getScale());
		Assert.assertNull(event);

		HandlerRegistration reg = eventBus.addViewPortChangedHandler(new AllowTranslationHandler());

		viewPort.applyPosition(new Coordinate(342, 342));
		Assert.assertEquals(4.0, viewPort.getScale());
		Assert.assertNotNull(event);
		Assert.assertTrue(event instanceof ViewPortTranslatedEvent);

		reg.removeHandler();
	}

	@Test
	public void testApplyScale() {
		Assert.assertEquals(4.0, viewPort.getScale());
		Assert.assertNull(event);

		HandlerRegistration reg = eventBus.addViewPortChangedHandler(new AllowScalingHandler());

		viewPort.applyScale(2.0);
		Assert.assertEquals(2.0, viewPort.getScale());
		Assert.assertNotNull(event);
		Assert.assertTrue(event instanceof ViewPortScaledEvent);

		reg.removeHandler();
	}

	@Test
	public void testApplyBounds() {
		Assert.assertEquals(4.0, viewPort.getScale());
		Assert.assertNull(event);

		HandlerRegistration reg = eventBus.addViewPortChangedHandler(new AllowChangedHandler());

		// Now a changed event should occur:
		viewPort.applyBounds(new Bbox(0, 0, 100, 100));
		Assert.assertEquals(8.0, viewPort.getScale());
		Assert.assertNotNull(event);
		Assert.assertTrue(event instanceof ViewPortChangedEvent);

		reg.removeHandler();
		reg = eventBus.addViewPortChangedHandler(new AllowTranslationHandler());

		// Expect to end up at the same scale, so no changed event, but translation only:
		viewPort.applyBounds(new Bbox(-50, -50, 100, 100));
		Assert.assertEquals(8.0, viewPort.getScale());
		Assert.assertNotNull(event);
		Assert.assertTrue(event instanceof ViewPortTranslatedEvent);

		reg.removeHandler();
	}

	@Test
	public void testApplySameBounds() {
		Assert.assertEquals(4.0, viewPort.getScale());
		Assert.assertNull(event);

		HandlerRegistration reg = eventBus.addViewPortChangedHandler(new AllowNoEventsHandler());

		viewPort.applyBounds(viewPort.getBounds());
		Assert.assertNull(event);

		reg.removeHandler();
	}

	@Test
	public void testResize() {
		Assert.assertEquals(4.0, viewPort.getScale());
		Assert.assertNull(event);

		HandlerRegistration reg = eventBus.addViewPortChangedHandler(new AllowChangedHandler());
		viewPort.setMapSize(500, 500);
		
		Assert.assertEquals(4.0, viewPort.getScale());
		Assert.assertTrue(viewPort.getPosition().equalsDelta(new Coordinate(-62.5, 62.5), 0.00001));
		Assert.assertNotNull(event);
		Assert.assertTrue(event instanceof ViewPortChangedEvent);

		reg.removeHandler();
	}

	@Test
	public void testResizeSameSize() {
		Assert.assertEquals(4.0, viewPort.getScale());
		Assert.assertNull(event);

		HandlerRegistration reg = eventBus.addViewPortChangedHandler(new AllowNoEventsHandler());
		viewPort.setMapSize(viewPort.getMapWidth(), viewPort.getMapHeight());
		
		Assert.assertNull(event);

		reg.removeHandler();
	}
// ------------------------------------------------------------------------
	// Private classes that allows only one type of event to be fired.
	// ------------------------------------------------------------------------

	/**
	 * ViewPortHandler that allows only ViewPortTranslatedEvents.
	 * 
	 * @author Pieter De Graef
	 */
	private class AllowTranslationHandler implements ViewPortChangedHandler {

		public void onViewPortChanged(ViewPortChangedEvent event) {
			Assert.fail("No ViewPortChangedEvent should have been fired.");
		}

		public void onViewPortScaled(ViewPortScaledEvent event) {
			Assert.fail("No ViewPortScaledEvent should have been fired.");
		}

		public void onViewPortTranslated(ViewPortTranslatedEvent event) {
			ViewPortEventTest.this.event = event;
		}
	}

	/**
	 * ViewPortHandler that allows only ViewPortScaledEvents.
	 * 
	 * @author Pieter De Graef
	 */
	private class AllowScalingHandler implements ViewPortChangedHandler {

		public void onViewPortChanged(ViewPortChangedEvent event) {
			Assert.fail("No ViewPortChangedEvent should have been fired.");
		}

		public void onViewPortScaled(ViewPortScaledEvent event) {
			ViewPortEventTest.this.event = event;
		}

		public void onViewPortTranslated(ViewPortTranslatedEvent event) {
			Assert.fail("No ViewPortTranslatedEvent should have been fired.");
		}
	}

	/**
	 * ViewPortHandler that allows only ViewPortChangedEvents.
	 * 
	 * @author Pieter De Graef
	 */
	private class AllowChangedHandler implements ViewPortChangedHandler {

		public void onViewPortChanged(ViewPortChangedEvent event) {
			ViewPortEventTest.this.event = event;
		}

		public void onViewPortScaled(ViewPortScaledEvent event) {
			Assert.fail("No ViewPortScaledEvent should have been fired.");
		}

		public void onViewPortTranslated(ViewPortTranslatedEvent event) {
			Assert.fail("No ViewPortTranslatedEvent should have been fired.");
		}
	}

	/**
	 * ViewPortHandler that allows no ViewPort events.
	 * 
	 * @author Pieter De Graef
	 */
	private class AllowNoEventsHandler implements ViewPortChangedHandler {

		public void onViewPortChanged(ViewPortChangedEvent event) {
			Assert.fail("No ViewPortChangedEvent should have been fired.");
		}

		public void onViewPortScaled(ViewPortScaledEvent event) {
			Assert.fail("No ViewPortScaledEvent should have been fired.");
		}

		public void onViewPortTranslated(ViewPortTranslatedEvent event) {
			Assert.fail("No ViewPortTranslatedEvent should have been fired.");
		}
	}
}