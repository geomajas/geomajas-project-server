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

package org.geomajas.puregwt.client;

import org.geomajas.puregwt.client.map.MapPresenter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.gwt.junit.GWTMockUtilities;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Test case to see if GIN/Guice injects properly.
 * 
 * @author Pieter De Graef
 */
public class GinjectorTest {

	private MapPresenter mapPresenter;

	public GinjectorTest() {
		GWTMockUtilities.disarm();
	}

	@Before
	public void setup() {
		Injector myInjector = Guice.createInjector(new GeomajasTestModule());
		mapPresenter = myInjector.getInstance(MapPresenter.class);
	}

	@Test
	public void injectionTest() {
		Assert.assertNotNull(mapPresenter);
		Assert.assertNotNull(mapPresenter.getEventBus());
		Assert.assertNotNull(mapPresenter.getViewPort());
		Assert.assertNotNull(mapPresenter.getLayersModel());
		// test map widget has no widget
		Assert.assertNull(mapPresenter.asWidget());
	}
}