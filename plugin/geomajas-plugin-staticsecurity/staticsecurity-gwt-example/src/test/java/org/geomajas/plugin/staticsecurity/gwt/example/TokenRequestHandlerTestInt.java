/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.staticsecurity.gwt.example;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Verify that the application loads properly, that the token request window is displayed and behaves properly.
 *
 * @author Joachim Van der Auwera
 */
public class TokenRequestHandlerTestInt {

	private WebDriver driver;

	@Before
	public void setUp() {
		// Create a new instance of the html unit driver
		driver = new FirefoxDriver();
	}

	@After
	public void tearDown() {
		driver.quit();
	}

	@Test
	public void testTokenRequest() throws Exception {
		driver.get("http://localhost:9080/");

		/*
		// the login window should appear
		(new WebDriverWait(driver, 90)).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				return null != d.findElement(By.className("tokenRequestWindow"));
			}
		});

		// login in using faulty user name/login combination
		WebElement userName = driver.findElement(By.name("userName"));
		WebElement password = driver.findElement(By.name("password"));
		WebElement login = driver.findElement(By.partialLinkText("log in"));
		userName.sendKeys("blabla");
		password.sendKeys("blabla");
		login.click();
		driver.findElement(By.xpath("//*[contains(.,'Login attempt failed')]"));
		*/

		// check that the correct error message is displayed

		// press reset and verify that form is cleared

		// no password -> error

		// no login -> error

		// login using correct credentials

		// check that login window disappears and map appears
	}
}
