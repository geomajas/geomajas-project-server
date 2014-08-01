package org.geomajas.plugin.deskmanager.test.domain.security;

import org.geomajas.plugin.deskmanager.domain.security.Territory;
import org.hibernate.SessionFactory;
import org.hibernate.exception.GenericJDBCException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/plugin/deskmanager/spring/**/*.xml", "/applicationContext.xml" })
@Transactional
public class PersistenceTest {

	@Autowired
	SessionFactory sessionFactory;

	@Test(expected=GenericJDBCException.class)
	public void testPersistTerritoryWithoutRequiredFields() throws Exception {
		Territory territory = new Territory();
		sessionFactory.getCurrentSession().saveOrUpdate(territory);
	}

	/*@Test(expected=IdentifierGenerationException.class)
	public void testPersistSessionWithoutIdentifier() throws Exception {
		sessionFactory.getCurrentSession().saveOrUpdate(new Session());
	} */

	/*@Test(expected=GenericJDBCException.class)
	public void testPersistSessionWithoutRequiredFields() throws Exception {
		String token = "token0";
		Session session = new Session();
		session.setAuthenticationSessionToken(token);
		sessionFactory.getCurrentSession().saveOrUpdate(session);
	}*/

	/*@Test
	public void testPersistSession() throws Exception {
		String token = "token1";
		Session sessionFromDb = (Session) sessionFactory.getCurrentSession().get(Session.class, token);
		if (sessionFromDb != null) {
			sessionFactory.getCurrentSession().delete(sessionFromDb);
		}

		Session session = new Session();
		session.setAuthenticationSessionToken("token1");
		sessionFactory.getCurrentSession().saveOrUpdate(session);

		Session sessionFromDbAfter = (Session) sessionFactory.getCurrentSession().get(Session.class, token);
		Assert.assertNotNull(sessionFromDbAfter);
	}*/
}
