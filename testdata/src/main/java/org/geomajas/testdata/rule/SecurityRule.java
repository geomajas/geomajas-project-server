/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2015 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.testdata.rule;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * JUnit rule which makes sure the user is logged in (you can set the token explicitly) and that the user is logged
 * out at the end. This will work automatically when the AllowedAll security context is used.
 *
 * @author Joachim Van der Auwera
 */
public class SecurityRule implements TestRule {

	@Autowired
	private org.geomajas.security.SecurityManager securityManager;

	private String token;

	// CHECKSTYLE THROWS_THROWABLE: OFF
	public Statement apply(final Statement statement, final Description description) {
		return new Statement() {
			@Override
			public void evaluate() throws Throwable {
				securityManager.createSecurityContext(token);
				try {
					statement.evaluate();
				} finally {
					securityManager.clearSecurityContext();
				}
			}
		};
	}
	// CHECKSTYLE THROWS_THROWABLE: ON

	/**
	 * Token which should be used to login.
	 *
	 * @param token token
	 */
	public void setToken(String token) {
		this.token = token;
	}

}
