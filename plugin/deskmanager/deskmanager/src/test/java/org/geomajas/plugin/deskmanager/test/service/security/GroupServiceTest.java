package org.geomajas.plugin.deskmanager.test.service.security;

import org.geomajas.plugin.deskmanager.domain.security.Territory;
import org.geomajas.plugin.deskmanager.service.security.GroupService;
import org.geomajas.security.GeomajasSecurityException;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/plugin/deskmanager/spring/**/*.xml", "/applicationContext.xml" })
@Transactional
public class GroupServiceTest {

	private static final String POLYGON1 = "POLYGON ((100000 100000,100000 200000,200000 200000,200000 100000,100000 100000 ))";

	@Autowired
	GroupService groupService;

	@Autowired
	SessionFactory sessionFactory;

	@Test
	public void createGroup() throws ParseException, GeomajasSecurityException {
		// create group
		Territory de = groupService.findByCode("DE");
		// create group
		Territory group = groupService.createGroup("group1", "key1", "EPSG:31370", new WKTReader().read(POLYGON1), null);
		sessionFactory.getCurrentSession().flush();
		sessionFactory.getCurrentSession().clear();
		Assert.assertNotNull(group);
		Assert.assertNotNull(group.getId());
		// check details
		Assert.assertEquals("group1", group.getName());
		Assert.assertEquals("key1", group.getCode());
		Assert.assertEquals("EPSG:31370", group.getCrs());
		Assert.assertEquals(new WKTReader().read(POLYGON1), group.getGeometry());
	}

	@Test
	public void updateGroup() throws ParseException, GeomajasSecurityException {
		// create group
		Territory de = groupService.findByCode("DE");
		groupService.updateGroup(de.getId(), "Westhoek", "WH");
		de = groupService.findById(de.getId());
		// check details
		Assert.assertEquals("Westhoek", de.getName());
		Assert.assertEquals("WH", de.getCode());
	}

	// Territory createGroup(String name, String key, String crs, Geometry geometry);
	//
	// Territory updateGroup(long groupId, String name, String key);
	//
	// Territory updateGroupGeometry(long groupId, String crs, Geometry geometry);
	//
	// List<Territory> findAll();
	//
	// void addUserToGroup(long userId, long groupId, Role role) throws GeomajasSecurityException;
	//
	// void removeUserFromGroupInRole(long userId, long groupId, Role role) throws GeomajasSecurityException;

}
