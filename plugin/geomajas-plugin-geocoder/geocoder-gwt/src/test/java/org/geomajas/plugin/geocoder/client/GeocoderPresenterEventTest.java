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
		GeocoderPresenter presenter = new GeocoderPresenter(null, null);
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
		GeocoderPresenter presenter = new GeocoderPresenter(null, null);
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
		GeocoderPresenter presenter = new GeocoderPresenter(null, null);
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
		GeocoderPresenter presenter = new GeocoderPresenter(null, null);
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
