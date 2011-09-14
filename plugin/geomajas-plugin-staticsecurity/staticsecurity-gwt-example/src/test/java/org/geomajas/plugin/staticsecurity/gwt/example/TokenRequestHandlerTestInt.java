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

import org.geomajas.plugin.staticsecurity.client.TokenRequestWindow;
import org.geomajas.plugin.staticsecurity.gwt.example.client.Application;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
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
		driver = new FirefoxDriver();
	}

	@After
	public void tearDown() {
		driver.quit();
	}

	@Test
	public void testTokenRequest() throws Exception {
		driver.get("http://localhost:9080/");

		// the login window should appear
		(new WebDriverWait(driver, 90)).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				return null != d.findElement(By.className(TokenRequestWindow.STYLE_NAME_WINDOW));
			}
		});

		// login in using faulty user name/login combination
		WebElement userName = driver.findElement(By.name("userName"));
		WebElement password = driver.findElement(By.name("password"));
		WebElement login = driver.findElement(By.xpath("//*[@aria-label='Log in']"));
		WebElement reset = driver.findElement(By.xpath("//*[@aria-label='Reset']"));
		userName.sendKeys("blabla");
		password.sendKeys("blabla");
		login.click();
		(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				return d.findElement(By.className(TokenRequestWindow.STYLE_NAME_ERROR)).getText().
						contains("Login attempt has failed");
			}
		});
		WebElement error = driver.findElement(By.className(TokenRequestWindow.STYLE_NAME_ERROR));

		// press reset and verify that form is cleared
		reset.click();
		Assert.assertEquals("", error.getText());

		// no login -> error
		reset.click();
		userName.clear();
		password.sendKeys("luc");
		login.click();
		(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				return null != d.findElement(By.xpath("//*[contains(.,'Please fill in a user name.')]"));
			}
		});

		// no password -> error
		reset.click();
		userName.sendKeys("luc");
		password.clear();
		login.click();
		(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				return null != d.findElement(By.xpath("//*[contains(.,'Please fill in a password.')]"));
			}
		});

		// login using correct credentials
		reset.click();
		userName.sendKeys("luc");
		password.sendKeys("luc");
		login.click();
		// map appears
		(new WebDriverWait(driver, 90)).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				return null != d.findElement(By.className(Application.APPLICATION_TITLE_STYLE));
			}
		});
		// login window should be gone
		Assert.assertEquals(0, driver.findElements(By.className(TokenRequestWindow.STYLE_NAME_WINDOW)).size());
	}
}
