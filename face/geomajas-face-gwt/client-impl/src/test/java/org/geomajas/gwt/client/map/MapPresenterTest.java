package org.geomajas.gwt.client.map;

import org.geomajas.command.dto.GetMapConfigurationResponse;
import org.geomajas.configuration.client.ClientMapInfo;
import org.geomajas.gwt.client.GeomajasTestModule;
import org.geomajas.gwt.client.event.MapInitializationEvent;
import org.geomajas.gwt.client.event.MapInitializationHandler;
import org.geomajas.gwt.client.service.CommandService;
import org.geomajas.gwt.client.service.MockCommandService;
import org.junit.Assert;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml", "beansContext.xml", "mapBeans.xml",
		"layerBeans1.xml", "layerBeans2.xml", "layerBeans3.xml" })
@DirtiesContext
public class MapPresenterTest implements MapInitializationHandler {

	private static final Injector INJECTOR = Guice.createInjector(new GeomajasTestModule());

	private MockCommandService commandService;

	@Autowired()
	@Qualifier("mapBeans")
	private ClientMapInfo mapBeans;

	private boolean initialized;

	@Before
	public void before() {
		commandService = (MockCommandService) INJECTOR.getInstance(CommandService.class);
		commandService.clear();
	}

	@Test
	public void testInitialization() {
		MapPresenter presenter = INJECTOR.getInstance(MapPresenter.class);
		// prepare a response
		GetMapConfigurationResponse response = new GetMapConfigurationResponse();
		response.setMapInfo(mapBeans);
		commandService.pushResponse(response);
		// expect initialization event
		presenter.getEventBus().addMapInitializationHandler(this);
		presenter.initialize("app", "map");
		// assert event was sent
		Assert.assertTrue(initialized);
		// assert 3 layers created
		Assert.assertEquals(3, presenter.getLayersModel().getLayerCount());
	}
	
	public void onMapInitialized(MapInitializationEvent event) {
		initialized = true;
	}
}