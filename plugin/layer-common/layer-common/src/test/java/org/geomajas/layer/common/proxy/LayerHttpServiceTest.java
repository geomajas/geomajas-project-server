/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.layer.common.proxy;

import java.io.IOException;
import java.util.UUID;

import junit.framework.Assert;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.CredentialsProvider;
import org.geomajas.plugin.caching.service.CacheCategory;
import org.geomajas.plugin.caching.service.CacheManagerService;
import org.infinispan.Cache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Test for {@link LayerHttpServiceImpl}
 * 
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/org/geomajas/spring/geomajasContext.xml",
		"/org/geomajas/layer/common/layerHttpTest.xml" })
public class LayerHttpServiceTest {

	@Autowired
	CachingLayerHttpService cachingLayerHttpService;

	@Autowired
	CacheManagerService cacheManagerService;

	@Autowired
	LayerHttpServiceInterceptors interceptors;

	@Test
	public void testAddCredentialsToUrl() throws Exception {
		LayerHttpServiceImpl service = new LayerHttpServiceImpl();
		String base;
		LayerAuthentication auth = new LayerAuthentication();
		auth.setUser("u");
		auth.setPassword("p");

		base = "noParamYet";
		Assert.assertEquals(base, service.addCredentialsToUrl(base, null));
		Assert.assertEquals(base, service.addCredentialsToUrl(base, auth));

		auth.setAuthenticationMethod(LayerAuthenticationMethod.URL);
		Assert.assertEquals(base + "?user=u&password=p", service.addCredentialsToUrl(base, auth));
		base = "url?bla";
		Assert.assertEquals(base + "&user=u&password=p", service.addCredentialsToUrl(base, auth));

		auth.setUserKey("who");
		auth.setPasswordKey("secret");
		Assert.assertEquals(base + "&who=u&secret=p", service.addCredentialsToUrl(base, auth));
	}

	@Test
	public void testAuthentication() throws IOException {
		// TODO: there is something wrong with cache invalidation, using uuid for now !!!
		try {
			cachingLayerHttpService.getStream("http://somehost/" + UUID.randomUUID().toString(), new MockProxyLayer(
					"layer1"));
		} catch (Exception e) {
			// ok to fail
		}
		// check the authentication
		CredentialsProvider p = cachingLayerHttpService.getClient().getCredentialsProvider();
		Credentials cc = p.getCredentials(new AuthScope("somehost", 80, "realm-layer1"));
		Assert.assertEquals("user-layer1", cc.getUserPrincipal().getName());
		Assert.assertEquals("password-layer1", cc.getPassword());

		// check for layer 2
		try {
			cachingLayerHttpService.getStream("https://somehost/" + UUID.randomUUID().toString(), new MockProxyLayer(
					"layer2"));
		} catch (Exception e) {
			// ok to fail
		}

		// check the authentication
		p = cachingLayerHttpService.getClient().getCredentialsProvider();
		cc = p.getCredentials(new AuthScope("somehost", 443, "realm-layer2"));
		Assert.assertEquals("user-layer2", cc.getUserPrincipal().getName());
		Assert.assertEquals("password-layer2", cc.getPassword());
		
		// TODO: test functioning of interceptors (without setting up connection ?) !!!
		Assert.assertEquals(2, interceptors.getMap().size());
	}

}
