package org.geomajas.plugin.staticsecurity.security;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.geomajas.plugin.staticsecurity.configuration.SecurityServiceInfo;
import org.geomajas.plugin.staticsecurity.security.dto.RoleUserFilter;
import org.geomajas.plugin.staticsecurity.security.dto.UserFilter;
import org.geomajas.security.UserInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/plugin/staticsecurity/securitywithroles.xml"})
public class StaticSecurityServiceRoleTest {

	@Autowired
	private SecurityServiceInfo securityServiceInfo;
	
	@Test
	public void testSimpleRole() {
		UserFilter filter = new RoleUserFilter("editor");
		List<UserInfo> users = getUsers(filter);
		Assert.assertEquals(1, users.size());		
		filter = new RoleUserFilter("viewerA");
		users = getUsers(filter);
		Assert.assertEquals(2, users.size());
	}

	@Test
	public void testLogicalOperators() {
		UserFilter filter = new RoleUserFilter("viewerA");
		filter = filter.and(new RoleUserFilter("viewerB"));
		List<UserInfo> users = getUsers(filter);
		Assert.assertEquals(1, users.size());
		
		filter = new RoleUserFilter("editor");
		filter = filter.or(new RoleUserFilter("viewerA"));
		filter = filter.or(new RoleUserFilter("viewerB"));
		users = getUsers(filter);
		Assert.assertEquals(3, users.size());		
	}
	
	@Test
	public void testCustom(){
		UserFilter filter = new CustomFilter("L");
		List<UserInfo> users = getUsers(filter);
		Assert.assertEquals(1, users.size());				
	}


	private List<UserInfo> getUsers(UserFilter filter) {
		List<UserInfo> users = new ArrayList<UserInfo>();
		for (AuthenticationService authentication : securityServiceInfo.getAuthenticationServices()) {
			if(authentication instanceof UserDirectoryService) {
				users.addAll(((UserDirectoryService)authentication).getUsers(filter));
			}
		}
		return users;
	}

}
