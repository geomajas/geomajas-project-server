package org.geomajas.plugin.runtimeconfig.service;

import org.geomajas.plugin.runtimeconfig.RuntimeConfigException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test methods for non {@link ContextConfiguratorService} methods of {@link ContextConfiguratorServiceImpl}.
 * Interface methods are tested in {@link ContextConfiguratorServiceTest}.
 *
 * @author Jan Venstermans
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml"})
public class ContextConfiguratorServiceImplTest {

	@Mock
	private ApplicationContext applicationContextMock;

	@InjectMocks
	@Autowired
	private ContextConfiguratorServiceImpl configuratorService;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testCreatedGenericApplicationContextCallsParentOnResource() throws RuntimeConfigException {
		GenericApplicationContext genericApplicationContext = configuratorService.createGenericApplicationContext();
		String resourcePath = "resourcePathTest";
		genericApplicationContext.getResource(resourcePath);
		Mockito.verify(applicationContextMock).getResource(resourcePath);
	}
}