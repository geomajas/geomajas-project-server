package org.geomajas.plugin.deskmanager.test.service.security;

import junit.framework.Assert;
import org.geomajas.plugin.deskmanager.domain.security.AuthenticationSession;
import org.geomajas.plugin.deskmanager.domain.security.GroupMember;
import org.geomajas.plugin.deskmanager.domain.security.User;
import org.geomajas.plugin.deskmanager.service.security.AuthenticationSessionService;
import org.geomajas.plugin.deskmanager.service.security.UserService;
import org.geomajas.plugin.deskmanager.test.service.ExampleDatabaseProvisioningServiceImpl;
import org.geomajas.security.GeomajasSecurityException;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.DistinctRootEntityResultTransformer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Test the functions of {@link AuthenticationSessionService} as implemented by
 * {@link org.geomajas.plugin.deskmanager.service.security.impl.AuthenticationSessionServiceImpl}.
 *
 * @author Jan Venstermans
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/plugin/deskmanager/spring/**/*.xml", "/applicationContext.xml" })
@Transactional
public class AuthenticationSessionServiceTest {

	@Autowired
	AuthenticationSessionService authenticationSessionService;

	@Autowired
	UserService userService;

	@Autowired
	SessionFactory sessionFactory;

	private User findUserNiko() throws GeomajasSecurityException {
		return userService.findByAddress(ExampleDatabaseProvisioningServiceImpl.USER_NIKO_EMAIL);
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

	/* tests createSession method */

	@Test
	public void testCreateSessionNormal() throws Exception {
		User user = findUserNiko();
		int countBefore = getAllSessions().size();

		authenticationSessionService.createSession(user, 1);

		Assert.assertEquals(countBefore + 1, getAllSessions().size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateSessionNegativeLifetime() throws Exception {
		User user = findUserNiko();
		authenticationSessionService.createSession(user, -8);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateSessionNoLifetime() throws Exception {

		User user = findUserNiko();
		authenticationSessionService.createSession(user, 0);
	}

	/* tests isActive method */

	@Test
	public void testIsSessionActiveNonExpiredSession() throws Exception {
		User user = findUserNiko();
		AuthenticationSession activeSession = authenticationSessionService.createSession(user, 1);

		boolean isActive = authenticationSessionService.isActive(activeSession);

		Assert.assertTrue(isActive);
	}

	@Test
	public void testIsSessionActiveExpiredSession() throws Exception {
		User user = findUserNiko();
		AuthenticationSession expiredSession = createExpiredSession(user);

		boolean isActive = authenticationSessionService.isActive(expiredSession);

		Assert.assertFalse(isActive);
	}

	/* tests getActiveSessionsOfUser method */

	@Test
	public void testGetSessionOfUserNone() throws Exception {
		User user = findUserNiko();
		List<AuthenticationSession> sessionsOfUser = authenticationSessionService.getActiveSessionsOfUser(user.getId());

		Assert.assertEquals(0 , sessionsOfUser.size());
	}

	@Test
	public void testGetSessionOfUserOne() throws Exception {
		User user = findUserNiko();
		// create one session
		AuthenticationSession session = authenticationSessionService.createSession(user, 1);

		List<AuthenticationSession> sessionsOfUser = authenticationSessionService.getActiveSessionsOfUser(user.getId());

		Assert.assertEquals(1 , sessionsOfUser.size());
		Assert.assertEquals(session , sessionsOfUser.get(0));
	}

	@Test
	public void testGetSessionOfUserMultiple() throws Exception {
		User user = findUserNiko();
		int amountOfSessions = 4;
		AuthenticationSession[] sessions = new AuthenticationSession[amountOfSessions];
		// create more than one session
		for (int i = 0 ; i < amountOfSessions; i++) {
			sessions[i] = authenticationSessionService.createSession(user, 1);
		}

		List<AuthenticationSession> sessionsOfUser = authenticationSessionService.getActiveSessionsOfUser(user.getId());

		Assert.assertEquals(amountOfSessions , sessionsOfUser.size());
		for (int i = 0 ; i < amountOfSessions; i++) {
			Assert.assertEquals(sessions[i] , sessionsOfUser.get(i));
		}
	}

	@Test
	public void testGetSessionsOfUserDoesNotReturnExpired() throws Exception {
		User user = findUserNiko();
		// create an expired session
		AuthenticationSession expiredSession = createExpiredSession(user);
		sessionFactory.getCurrentSession().saveOrUpdate(expiredSession);

		List<AuthenticationSession> sessionsOfUser = authenticationSessionService.getActiveSessionsOfUser(user.getId());

		Assert.assertEquals(0 , sessionsOfUser.size());
	}

	/* tests getActiveSessionOfToken method */

	@Test
	public void testGetSessionOfTokenNullInput() throws Exception {
		AuthenticationSession sessionOfToken = authenticationSessionService.getActiveSessionOfToken(null);

		Assert.assertNull(sessionOfToken);
	}

	@Test
	public void testGetSessionOfTokenNone() throws Exception {
		String dummyToken = "dummyToken";

		AuthenticationSession sessionOfToken = authenticationSessionService.getActiveSessionOfToken(dummyToken);

		Assert.assertNull(sessionOfToken);
	}

	@Test
	public void testGetSessionOfTokenExisting() throws Exception {
		User user = findUserNiko();
		AuthenticationSession session = authenticationSessionService.createSession(user, 1);

		AuthenticationSession sessionOfToken = authenticationSessionService.getActiveSessionOfToken(session.getAuthenticationSessionToken());

		Assert.assertNotNull(sessionOfToken);
		Assert.assertEquals(session , sessionOfToken);
	}

	@Test
	public void testGetSessionOfExpiredToken() throws Exception {
		User user = findUserNiko();
		AuthenticationSession session = createExpiredSession(user);
		sessionFactory.getCurrentSession().saveOrUpdate(session);

		AuthenticationSession sessionOfToken = authenticationSessionService.getActiveSessionOfToken(session.getAuthenticationSessionToken());

		Assert.assertNull(sessionOfToken);
	}

	/* tests dropSession method */

	@Test
	public void testDropExistingSession() throws Exception {
		User user = findUserNiko();
		AuthenticationSession session = authenticationSessionService.createSession(user, 1);

		// check if added
		AuthenticationSession sessionOfToken = authenticationSessionService.getActiveSessionOfToken(session.getAuthenticationSessionToken());
		Assert.assertNotNull(sessionOfToken);

		authenticationSessionService.dropSession(sessionOfToken);

		AuthenticationSession sessionOfToken2 = authenticationSessionService.getActiveSessionOfToken(session.getAuthenticationSessionToken());
		Assert.assertNull(sessionOfToken2);
	}

	@Test
	public void testDropNonExistingSession() throws Exception {
		User user = findUserNiko();
		String dummyToken = "dummyToken";
		AuthenticationSession unsavedSession = new AuthenticationSession(dummyToken, user, new Date());

		authenticationSessionService.dropSession(unsavedSession);

		// don't expect errors
	}

	@After
	public void removeSessionsOfUser() throws Exception {
		User user = findUserNiko();
		for (AuthenticationSession session : getSessionsOfUser(user.getId())) {
			sessionFactory.getCurrentSession().delete(session);
		}
	}

	/* private mehtods */

	private List<AuthenticationSession> getAllSessions() {
		Criteria crit = sessionFactory.getCurrentSession().createCriteria(AuthenticationSession.class);
		crit.setResultTransformer(DistinctRootEntityResultTransformer.INSTANCE);
		return crit.list();
	}

	private Date getCurrentDatePlusDays(int numberOfDays) {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, numberOfDays);
		return c.getTime();
	}

	private List<AuthenticationSession> getSessionsOfUser(Long userId) {
		try {
			User user = userService.findById(userId);
			Criteria crit = sessionFactory.getCurrentSession().createCriteria(AuthenticationSession.class);
			crit.add(Restrictions.eq("user", user));
			crit.setResultTransformer(DistinctRootEntityResultTransformer.INSTANCE);
			return crit.list();
		} catch (GeomajasSecurityException e) {
			e.printStackTrace();
		}
		return new ArrayList<AuthenticationSession>();
	}

	private AuthenticationSession createExpiredSession(User user) {
		return new AuthenticationSession("dummyToken", user, getCurrentDatePlusDays(-5));
	}

}
