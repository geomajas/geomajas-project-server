/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.layer.wms;

import junit.framework.Assert;
import org.junit.Test;

/**
 * Test for {@link WmsHttpServiceImpl}
 *
 * @author Joachim Van der Auwera
 */
public class WmsHttpServiceTest {

	@Test
	public void testAddCredentialsToUrl() throws Exception {
		WmsHttpService service = new WmsHttpServiceImpl();
		String base;
		WmsAuthentication auth = new WmsAuthentication();
		auth.setUser("u");
		auth.setPassword("p");

		base = "noParamYet";
		Assert.assertEquals(base, service.addCredentialsToUrl(base, null));
		Assert.assertEquals(base, service.addCredentialsToUrl(base, auth));

		auth.setAuthenticationMethod(WmsAuthenticationMethod.URL);
		Assert.assertEquals(base + "?user=u&password=p", service.addCredentialsToUrl(base, auth));
		base = "url?bla";
		Assert.assertEquals(base + "&user=u&password=p", service.addCredentialsToUrl(base, auth));

		auth.setUserKey("who");
		auth.setPasswordKey("secret");
		Assert.assertEquals(base + "&who=u&secret=p", service.addCredentialsToUrl(base, auth));
	}

}
