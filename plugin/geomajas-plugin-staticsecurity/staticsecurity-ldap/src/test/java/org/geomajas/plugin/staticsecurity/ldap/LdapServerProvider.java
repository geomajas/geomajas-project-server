/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.staticsecurity.ldap;

import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.listener.InMemoryListenerConfig;
import org.junit.After;
import org.junit.Before;

/**
 * Base class for testing which created an in-memory LDAP server.
 *
 * @author Joachim Van der Auwera
 */
public class LdapServerProvider {

	protected static final int PORT = 3636;

	protected InMemoryDirectoryServer server;

	@Before
	public void before() throws Exception {
		InMemoryDirectoryServerConfig config = new InMemoryDirectoryServerConfig("dc=org");
		config.addAdditionalBindCredentials("cn=test,dc=staticsecurity,dc=geomajas,dc=org", "cred");
		InMemoryListenerConfig listenerConfig = new InMemoryListenerConfig("test", null, PORT, null, null, null);
		config.setListenerConfigs(listenerConfig);
		config.setSchema(null); // do not check (attribute) schema
		server = new InMemoryDirectoryServer(config);
		server.startListening();
		server.add("dn: dc=org", "objectClass: top", "objectClass: domain", "dc: org");
		server.add("dn: dc=geomajas,dc=org", "objectClass: top", "objectClass: domain", "dc: geomajas");
		server.add("dn: dc=roles,dc=geomajas,dc=org", "objectClass: top", "objectClass: domain",
				"dc: roles");
		server.add("dn: dc=staticsecurity,dc=geomajas,dc=org", "objectClass: top", "objectClass: domain",
				"dc: staticsecurity");
		server.add("dn: cn=testgroup,dc=roles,dc=geomajas,dc=org", "objectClass: groupOfUniqueNames",
				"cn: testgroup");
		server.add("dn: cn=devgroup,dc=roles,dc=geomajas,dc=org", "objectClass: groupOfUniqueNames",
				"cn: devgroup");
		server.add("dn: cn=test,dc=staticsecurity,dc=geomajas,dc=org", "objectClass: person", "locale: nl_BE",
				"sn: Tester", "givenName: Joe", "cn: test", "memberOf: cn=testgroup,dc=roles,dc=geomajas,dc=org");
		server.add("dn: cn=jill,dc=staticsecurity,dc=geomajas,dc=org", "objectClass: person", "locale: nl_BE",
				"sn: Developer", "givenName: Jill", "cn: dev", "memberOf: cn=devgroup,dc=roles,dc=geomajas,dc=org");
	}

	@After
	public void shutdown() {
		server.shutDown(true);
	}

}
