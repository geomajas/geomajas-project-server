package org.geomajas.plugin.deskmanager.test;

import org.geomajas.spring.ThreadScopeContextHolder;
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Base abstract class for test that need security. Will make sure after each test, the security context is cleared.
 * TODO Could be moved to static security?
 * @author Jan Venstermans
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml" })
public abstract class SecurityContainingTestBase {

	@After
	public void clearSecurityContext() {
		ThreadScopeContextHolder.clear();
	}
}
