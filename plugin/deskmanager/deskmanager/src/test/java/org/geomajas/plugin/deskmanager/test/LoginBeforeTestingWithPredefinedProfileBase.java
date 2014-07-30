package org.geomajas.plugin.deskmanager.test;

import org.geomajas.global.GeomajasException;
import org.geomajas.plugin.deskmanager.command.security.dto.RetrieveRolesRequest;
import org.geomajas.plugin.deskmanager.domain.security.Profile;
import org.geomajas.plugin.deskmanager.domain.security.dto.Role;
import org.geomajas.plugin.deskmanager.security.DeskmanagerSecurityService;
import org.geomajas.plugin.deskmanager.security.ProfileService;
import org.geomajas.plugin.deskmanager.test.security.StubProfileService;
import org.geomajas.security.*;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * Base abstract class for test that need security. Will make sure after each test, the security context is cleared.
 * TODO Could be moved to static security?
 * @author Jan Venstermans
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml" })
public abstract class LoginBeforeTestingWithPredefinedProfileBase extends SecurityContainingTestBase {

	protected Map<Role, String> tokenMap;

	@Autowired
	private ProfileService profileService;

	@Autowired
	private org.geomajas.security.SecurityManager securityManager;

	@Autowired
	private SecurityService securityService;

	@PostConstruct
	public void createProfiles() throws GeomajasException {
		StubProfileService pService = (StubProfileService) profileService;
		DeskmanagerSecurityService deskmanagerSecurityService = (DeskmanagerSecurityService) securityService;

		tokenMap = new HashMap<Role, String>();

		createToken(pService, deskmanagerSecurityService, Role.ADMINISTRATOR);
		createToken(pService, deskmanagerSecurityService, Role.DESK_MANAGER);

		String guestToken = deskmanagerSecurityService.registerRole(RetrieveRolesRequest.MANAGER_ID,
				DeskmanagerSecurityService.createGuestProfile());
		tokenMap.put(Role.GUEST, guestToken);
	}

	private void createToken(StubProfileService pService,
							 DeskmanagerSecurityService deskmanagerSecurityService, Role role) {
		Profile profile = pService.getProfileByRole(role);
		String token = deskmanagerSecurityService.registerRole(RetrieveRolesRequest.MANAGER_ID,
				profile);
		tokenMap.put(role, token);
	}


	@Before
	public void loginBeforeTesting() throws Exception {
		login(getRoleToLoginWithBeforeTesting());
	}

	protected abstract Role getRoleToLoginWithBeforeTesting();

	protected String getToken(Role role) {
		if (role != null) {
			return tokenMap.get(role);
		}
		return null;
	}

	protected String getTokenOfLoggedInBeforeTesting() {
		return getToken(getRoleToLoginWithBeforeTesting());
	}

	protected void login(Role role) {
		securityManager.createSecurityContext(getToken(role));
	}
}
