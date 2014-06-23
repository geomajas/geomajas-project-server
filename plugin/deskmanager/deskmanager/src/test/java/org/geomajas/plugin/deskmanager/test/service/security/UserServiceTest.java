package org.geomajas.plugin.deskmanager.test.service.security;

import org.geomajas.plugin.deskmanager.domain.security.GroupMember;
import org.geomajas.plugin.deskmanager.domain.security.User;
import org.geomajas.plugin.deskmanager.service.common.DtoConverterService;
import org.geomajas.plugin.deskmanager.service.security.GroupService;
import org.geomajas.plugin.deskmanager.service.security.UserService;
import org.geomajas.plugin.deskmanager.service.security.UserServiceImpl;
import org.geomajas.security.GeomajasSecurityException;
import org.hibernate.SessionFactory;
import org.junit.Assert;
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
public class UserServiceTest {

	@Autowired
	UserService userService;

	@Autowired
	SessionFactory sessionFactory;

	@Test
	public void createUser() throws GeomajasSecurityException {
		// create user
		User user = userService.createUser("johnny", "hoes", "johnny.hoes@schlagerfestival.com", "seoh");
		sessionFactory.getCurrentSession().flush();
		sessionFactory.getCurrentSession().clear();
		Assert.assertNotNull(user);
		Assert.assertNotNull(user.getId());
		// check details
		Assert.assertEquals("johnny", user.getName());
		Assert.assertEquals("hoes", user.getSurname());
		Assert.assertEquals("johnny.hoes@schlagerfestival.com", user.getEmail());
		String encodedPassword = UserServiceImpl.encodePassword(user.getEmail(), "seoh");
		Assert.assertEquals(encodedPassword, user.getPassword());
		Assert.assertTrue(user.isActive());		
	}
	
	@Test
	public void deleteUser() throws GeomajasSecurityException {
		// find the user
		User user = userService.findByAddress("niko.haak@gmail.com");
		userService.deleteUser(user.getId());
		sessionFactory.getCurrentSession().flush();
		sessionFactory.getCurrentSession().clear();
		user = userService.findByAddress("niko.haak@gmail.com");
		Assert.assertNull(user);
	}

	@Test
	public void findByAddress() throws GeomajasSecurityException {
		// find the user
		User user = userService.findByAddress("niko.haak@gmail.com");
		Assert.assertNotNull(user);
		// check details
		Assert.assertEquals("niko", user.getName());
		Assert.assertEquals("haak", user.getSurname());
		Assert.assertEquals(1, user.getGroups().size());
		// consulting of NL group
		GroupMember member = user.getGroups().get(0);
		Assert.assertEquals("NL", member.getGroup().getCode());
	}

	@Test
	public void deleteByAddress() throws GeomajasSecurityException {
		userService.deleteByAddress("niko.haak@gmail.com");
		sessionFactory.getCurrentSession().flush();
		sessionFactory.getCurrentSession().clear();
		User user = userService.findByAddress("niko.haak@gmail.com");
		Assert.assertNull(user);
	}
	
	@Test
	public void updateUser() throws GeomajasSecurityException {
		// find the user
		User user = userService.findByAddress("niko.haak@gmail.com");
		user.setName("cale");
		user.setSurname("jj");
		user.setEmail("jj.cale@cnn.com");
		userService.updateUser(user);
		sessionFactory.getCurrentSession().flush();
		sessionFactory.getCurrentSession().clear();
		// check details
		user = userService.findById(user.getId());
		Assert.assertEquals("jj", user.getSurname());
		Assert.assertEquals("cale", user.getName());
		// can't update email:
		Assert.assertEquals("niko.haak@gmail.com", user.getEmail());
		
	}

	@Test
	public void setUserActive() throws GeomajasSecurityException {
		// find the user
		User user = userService.findByAddress("niko.haak@gmail.com");
		userService.setUserActive(user.getId(), false);
		sessionFactory.getCurrentSession().flush();
		sessionFactory.getCurrentSession().clear();
		// check details
		user = userService.findById(user.getId());
		Assert.assertFalse(user.isActive());	
	}


	@Test
	public void changePassword() throws GeomajasSecurityException {
		// find the user
		User user = userService.findByAddress("niko.haak@gmail.com");
		userService.changePassword(user.getId(), "haak");
		sessionFactory.getCurrentSession().flush();
		sessionFactory.getCurrentSession().clear();
		// check details
		user = userService.findById(user.getId());
		String encodedPassword = UserServiceImpl.encodePassword(user.getEmail(), "haak");
		Assert.assertEquals(encodedPassword, user.getPassword());
	}

}
