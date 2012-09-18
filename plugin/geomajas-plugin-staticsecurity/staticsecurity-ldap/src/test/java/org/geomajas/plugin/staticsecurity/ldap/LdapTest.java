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

import static org.fest.assertions.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.plugin.staticsecurity.configuration.AuthorizationInfo;
import org.geomajas.plugin.staticsecurity.configuration.LayerAuthorizationInfo;
import org.geomajas.plugin.staticsecurity.configuration.UserInfo;
import org.junit.Test;

import com.unboundid.ldap.sdk.BindResult;
import com.unboundid.ldap.sdk.Filter;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.SearchRequest;
import com.unboundid.ldap.sdk.SearchResult;
import com.unboundid.ldap.sdk.SearchResultEntry;
import com.unboundid.ldap.sdk.SearchScope;
import com.unboundid.util.ssl.SSLUtil;
import com.unboundid.util.ssl.TrustAllTrustManager;

/**
 * Test to verify connecting with an LDAP server.
 *
 * @author Joachim Van der Auwera
 */
public class LdapTest extends LdapServerProvider {

	@Test
	public void testInMemBind() throws Exception {
		String user = "cn=test,dc=staticsecurity,dc=geomajas,dc=org";
		LDAPConnection connection = new LDAPConnection("localhost", PORT);

		BindResult auth = connection.bind(user, "cred");
		System.out.println("auth " + auth);
		System.out.println("  rc " + auth.getResultCode());
		System.out.println("  cu " + auth.getResultCode().isConnectionUsable());
		System.out.println("  dn " + auth.getMatchedDN());
		System.out.println("  dm " + auth.getDiagnosticMessage());

		SearchRequest request = new SearchRequest(user,
				SearchScope.ONE, Filter.createEqualityFilter("objectclass", "person"),
				"cn", "groupMembership", "givenName", "sn", "ou");
		SearchResult search = connection.search(request);
		System.out.println(search);
		System.out.println("count " + search.getEntryCount());
		System.out.println("count " + search.getSearchEntries());
		for (SearchResultEntry se : search.getSearchEntries()) {
			System.out.println("-- cn " + se.getAttributeValue("cn"));
			System.out.println("   givenName " + se.getAttributeValue("givenName"));
			System.out.println("   sn " + se.getAttributeValue("sn"));
			System.out.println("   ou " + se.getAttributeValue("ou"));
			System.out.println("   groupMembership ");
			if (null != se.getAttributeValues("groupMembership")) {
				for (String val : se.getAttributeValues("groupMembership")) {
					System.out.println("      " + val);
				}
			}
		}
	}

//	@Test
//	public void testUnboundidRealServer() throws Exception {
//		// LDAPConnection(LDAPConnectionOptions options, String host, int port, String bindDN, String password)
//		SSLUtil sslUtil = new SSLUtil(new TrustAllTrustManager());
//		LDAPConnection connection = new LDAPConnection(sslUtil.createSSLSocketFactory(), "localhost", 636);
//		SearchRequest request = new SearchRequest("ou=LND,o=KN",
//				SearchScope.SUB, Filter.createEqualityFilter("objectclass", "person"),
//				"cn", "groupMembership", "givenName", "sn", "ou");
//		com.unboundid.ldap.sdk.SearchResult search = connection.search(request);
//		System.out.println(search);
//		System.out.println("count " + search.getEntryCount());
//		System.out.println("count " + search.getSearchEntries());
//		for (SearchResultEntry se : search.getSearchEntries()) {
//			System.out.println("-- cn " + se.getAttributeValue("cn"));
//			System.out.println("   givenName " + se.getAttributeValue("givenName"));
//			System.out.println("   sn " + se.getAttributeValue("sn"));
//			System.out.println("   ou " + se.getAttributeValue("ou"));
//			System.out.println("   groupMembership ");
//			if (null != se.getAttributeValues("groupMembership")) {
//				for (String val : se.getAttributeValues("groupMembership")) {
//					System.out.println("      " + val);
//				}
//			}
//		}
//
//		//BindResult auth = connection.bind("cn=RefTest2,ou=Referral-Roles,ou=LND,o=KN", "dakota");
//		BindResult auth = connection.bind("cn=RefTest1", "dakota");
//		System.out.println("" + auth);
//		System.out.println("" + auth.getResultCode());
//		System.out.println("" + auth.getMatchedDN());
//		System.out.println("" + auth.getDiagnosticMessage());
//	}

}
