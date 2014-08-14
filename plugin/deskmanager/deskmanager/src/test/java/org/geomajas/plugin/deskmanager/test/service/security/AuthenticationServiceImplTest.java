package org.geomajas.plugin.deskmanager.test.service.security;

import junit.framework.Assert;
import org.geomajas.plugin.deskmanager.domain.security.AuthenticationSession;
import org.geomajas.plugin.deskmanager.domain.security.GroupMember;
import org.geomajas.plugin.deskmanager.domain.security.Profile;
import org.geomajas.plugin.deskmanager.domain.security.User;
import org.geomajas.plugin.deskmanager.security.AuthenticationService;
import org.geomajas.plugin.deskmanager.security.ProfileService;
import org.geomajas.plugin.deskmanager.service.security.AuthenticationSessionService;
import org.geomajas.plugin.deskmanager.service.security.UserService;
import org.geomajas.plugin.deskmanager.test.service.ExampleDatabaseProvisioningServiceImpl;
import org.geomajas.security.GeomajasSecurityException;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.transform.DistinctRootEntityResultTransformer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Test the functions of {@link AuthenticationService} as implemented by
 * {@link org.geomajas.plugin.deskmanager.service.security.impl.AuthenticationServiceImpl}.
 *
 * @author Jan Venstermans
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/plugin/deskmanager/spring/**/*.xml", "/applicationContext.xml" })
@Transactional
public class AuthenticationServiceImplTest {

	@Autowired
	AuthenticationService authenticationService;

	@Autowired
	AuthenticationSessionService authenticationSessionService;

	@Autowired
	UserService userService;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	private ProfileService profileService;

	private User findUserNiko() throws GeomajasSecurityException {
		return userService.findByAddress(ExampleDatabaseProvisioningServiceImpl.USER_NIKO_EMAIL);
	}

	private List<GroupMember> getCurrentGroups(Long userId) {
		return userService.findGroupsOfUser(userId);
	}

	@After
	public void removeAllAuthenticationSessions() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(AuthenticationSession.class);
		crit.setResultTransformer(DistinctRootEntityResultTransformer.INSTANCE);
		List<AuthenticationSession> sessions = crit.list();
		if (sessions != null) {
			for (AuthenticationSession authenticationSession : sessions) {
				sessionFactory.getCurrentSession().delete(authenticationSession);
			}
		}
	}

	/* tests authenticateUsernamePassword method */

	@Test
	public void testAuthenticateUsernamePasswordNoPriorSession() throws Exception {
		User user = findUserNiko();
		String token = authenticationService.authenticateUsernamePassword(user.getEmail(),
				ExampleDatabaseProvisioningServiceImpl.USER_NIKO_PASSWORD);

		Assert.assertNotNull(token);
		AuthenticationSession session = authenticationSessionService.getActiveSessionOfToken(token);
		Assert.assertNotNull(session);
		List<Profile> profiles = profileService.getProfiles(token);
		Assert.assertEquals(getCurrentGroups(user.getId()).size(), profiles.size());
	}

	@Test
	public void testAuthenticateUsernamePasswordOnePriorSession() throws Exception {
		User user = findUserNiko();
		AuthenticationSession session = authenticationSessionService.createSession(user, 5);

		String token = authenticationService.authenticateUsernamePassword(user.getEmail(),
				ExampleDatabaseProvisioningServiceImpl.USER_NIKO_PASSWORD);

		Assert.assertNotNull(token);
		AuthenticationSession activeSession = authenticationSessionService.getActiveSessionOfToken(token);
		Assert.assertEquals(session, activeSession);
		List<Profile> profiles = profileService.getProfiles(token);
		Assert.assertEquals(getCurrentGroups(user.getId()).size(), profiles.size());
	}

	@Test
	public void testAuthenticateUsernamePasswordMoreThanOnePriorSession() throws Exception {
		User user = findUserNiko();
		AuthenticationSession session1 = authenticationSessionService.createSession(user, 5);
		AuthenticationSession session2 = authenticationSessionService.createSession(user, 5);

		String token = authenticationService.authenticateUsernamePassword(user.getEmail(),
				ExampleDatabaseProvisioningServiceImpl.USER_NIKO_PASSWORD);

		Assert.assertNotNull(token);
		AuthenticationSession activeSession = authenticationSessionService.getActiveSessionOfToken(token);
		Assert.assertNotSame(session1, activeSession);
		Assert.assertNotSame(session2, activeSession);
		List<Profile> profiles = profileService.getProfiles(token);
		Assert.assertEquals(getCurrentGroups(user.getId()).size(), profiles.size());
	}

	/* tests getUsernameFromToken method */

	@Test(expected = GeomajasSecurityException.class)
	public void testGetUsernameFromNonExistingToken() throws Exception {
		authenticationService.getUsernameFromToken("dummyToken");
	}

	@Test
	public void testGetUsernameFromRegisteredActiveToken() throws Exception {
		User user = userService.findByAddress(ExampleDatabaseProvisioningServiceImpl.USER_NIKO_EMAIL);
		AuthenticationSession session1 = authenticationSessionService.createSession(user, 5);

		String username = authenticationService.getUsernameFromToken(session1.getAuthenticationSessionToken());

		Assert.assertEquals(user.getEmail(), username);
	}

	@Test(expected = GeomajasSecurityException.class)
	public void testGetUsernameFromInactiveToken() throws Exception {
		User user = userService.findByAddress(ExampleDatabaseProvisioningServiceImpl.USER_NIKO_EMAIL);
		AuthenticationSession session1 = createExpiredSession();
		sessionFactory.getCurrentSession().saveOrUpdate(session1);

		authenticationService.getUsernameFromToken(session1.getAuthenticationSessionToken());
	}

	/* tests removeAuthenticationSession method */

	@Test
	public void testRemoveAuthenticationSession() throws Exception {
		// perform authentication
		User user = userService.findByAddress(ExampleDatabaseProvisioningServiceImpl.USER_NIKO_EMAIL);
		//AuthenticationSession session1 = authenticationSessionService.createSession(user, 5);
		String token = authenticationService.authenticateUsernamePassword(user.getEmail(),
				ExampleDatabaseProvisioningServiceImpl.USER_NIKO_PASSWORD);

		authenticationService.removeAuthenticationSession(token);

		AuthenticationSession session = authenticationSessionService.getActiveSessionOfToken(token);
		Assert.assertNull(session);
		List<Profile> profiles = profileService.getProfiles(token);
		Assert.assertNotNull(profiles);
		Assert.assertEquals(0, profiles.size());
	}

	private AuthenticationSession createExpiredSession() throws GeomajasSecurityException {
		User user = findUserNiko();
		return new AuthenticationSession("dummyToken", user, getCurrentDatePlusDays(-5));
	}

	private Date getCurrentDatePlusDays(int numberOfDays) {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, numberOfDays);
		return c.getTime();
	}
}
