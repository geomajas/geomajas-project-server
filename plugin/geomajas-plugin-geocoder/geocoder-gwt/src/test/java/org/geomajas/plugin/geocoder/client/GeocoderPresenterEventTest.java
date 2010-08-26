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

package org.geomajas.plugin.geocoder.client;

import com.google.gwt.junit.GWTMockUtilities;
import junit.framework.Assert;
import org.geomajas.plugin.geocoder.client.event.SelectAlternativeEvent;
import org.geomajas.plugin.geocoder.client.event.SelectAlternativeHandler;
import org.geomajas.plugin.geocoder.client.event.SelectLocationEvent;
import org.geomajas.plugin.geocoder.client.event.SelectLocationHandler;
import org.geomajas.plugin.geocoder.command.dto.GetLocationForStringAlternative;
import org.geomajas.plugin.geocoder.command.dto.GetLocationForStringResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Test for event handling in GeocoderWidget.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/eventTestContext.xml"})
public class GeocoderPresenterEventTest {

	private static final String CRS = "EPSG:900913";

	private GetLocationForStringResponse matchedResponse;
	private GetLocationForStringResponse alternativesResponse;

	@Before
	public void disableWidgets() {
		GWTMockUtilities.disarm();

		matchedResponse = new GetLocationForStringResponse();
		matchedResponse.setLocationFound(true);

		alternativesResponse = new GetLocationForStringResponse();
		List<GetLocationForStringAlternative> list = new ArrayList<GetLocationForStringAlternative>();
		list.add(new GetLocationForStringAlternative());
		list.add(new GetLocationForStringAlternative());
		alternativesResponse.setAlternatives(list);
	}

	@After
	public void reEnableWidgets() {
		GWTMockUtilities.restore();
	}

	@Test
	public void testSelectLocationEvent() throws Exception {
		final ObjectContainer container = new ObjectContainer();
		GeocoderPresenter presenter = new GeocoderPresenter(null, null, CRS);
		presenter.setSelectLocationHandler(new SelectLocationHandler() {
			public void onSelectLocation(SelectLocationEvent event) {
				container.object = "ok";
			}
		});
		presenter.goToLocation(matchedResponse, "1");
		Assert.assertEquals("SelectLocation handler not called", "ok", container.object);
	}

	@Test
	public void testSelectLocationOnlyOneEvent() throws Exception {
		final ObjectContainer container = new ObjectContainer();
		GeocoderPresenter presenter = new GeocoderPresenter(null, null, CRS);
		presenter.setSelectLocationHandler(new SelectLocationHandler() {
			public void onSelectLocation(SelectLocationEvent event) {
				Assert.fail("This handler should be overwritten");
			}
		});
		presenter.setSelectLocationHandler(new SelectLocationHandler() {
			public void onSelectLocation(SelectLocationEvent event) {
				container.object = "ok";
			}
		});
		presenter.goToLocation(matchedResponse, "2");
		Assert.assertEquals("SelectLocation handler not called", "ok", container.object);
	}

	@Test
	public void testSelectAlternativeEvent() throws Exception {
		final ObjectContainer container = new ObjectContainer();
		GeocoderPresenter presenter = new GeocoderPresenter(null, null, CRS);
		presenter.setSelectAlternativeHandler(new SelectAlternativeHandler() {
			public void onSelectAlternative(SelectAlternativeEvent event) {
				container.object = "ok";
			}
		});
		presenter.goToLocation(alternativesResponse, "3");
		Assert.assertEquals("SelectAlternative handler not called", "ok", container.object);
	}

	@Test
	public void testSelectAlternativeOnlyOneEvent() throws Exception {
		final ObjectContainer container = new ObjectContainer();
		GeocoderPresenter presenter = new GeocoderPresenter(null, null, CRS);
		presenter.setSelectAlternativeHandler(new SelectAlternativeHandler() {
			public void onSelectAlternative(SelectAlternativeEvent event) {
				Assert.fail("This handler should be overwritten");
			}
		});
		presenter.setSelectAlternativeHandler(new SelectAlternativeHandler() {
			public void onSelectAlternative(SelectAlternativeEvent event) {
				container.object = "ok";
			}
		});
		presenter.goToLocation(alternativesResponse, "4");
		Assert.assertEquals("SelectAlternative handler not called", "ok", container.object);
	}

	private class ObjectContainer {

		public Object object = null;
	}
}
