package org.geomajas.puregwt.client.spatial;

import org.geomajas.puregwt.client.util.SpatialService;
import org.geomajas.puregwt.client.util.SpatialServiceImpl;

import com.google.inject.AbstractModule;

public class TestConfigurationModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(GeometryFactory.class).to(GeometryFactoryImpl.class);
		bind(SpatialService.class).to(SpatialServiceImpl.class);
	}

}
