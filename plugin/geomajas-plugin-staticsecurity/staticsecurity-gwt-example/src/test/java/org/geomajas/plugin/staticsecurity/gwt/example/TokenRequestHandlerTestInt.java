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

package org.geomajas.plugin.staticsecurity.gwt.example;

import org.geomajas.plugin.staticsecurity.client.TokenRequestWindow;
import org.geomajas.plugin.staticsecurity.gwt.example.client.Application;
import org.geomajas.testdata.CommandCountAssert;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Verify that the application loads properly, that the token request window is displayed and behaves properly.
 *
 * @author Joachim Van der Auwera
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/org/geomajas/testdata/commandCount9080Context.xml"})
public class TokenRequestHandlerTestInt {

	private static final String LAYER_VECTOR = "-clientLayerCountries";
	private static final int LAYER_VECTOR_LENGTH = LAYER_VECTOR.length() - 1;
	private static final String LAYER_VECTOR_XPATH =
			"//*[substring(@id, string-length(@id)-" + LAYER_VECTOR_LENGTH + ")= '" + LAYER_VECTOR + "']";
	private static final String LAYER_RASTER = "-clientLayerOsm";
	private static final int LAYER_RASTER_LENGTH = LAYER_RASTER.length() - 1;
	private static final String LAYER_RASTER_XPATH =
			"//*[substring(@id, string-length(@id)-" + LAYER_RASTER_LENGTH + ")= '" + LAYER_RASTER + "']";

	private WebDriver driver;

	@Autowired
	private CommandCountAssert commandCountAssert;

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
		String source;
		List<WebElement> elements;
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.pollingEvery(500, TimeUnit.MILLISECONDS);
		commandCountAssert.init();

		driver.get("http://localhost:9080/");

		// the login window should appear
		wait.until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				return null != d.findElement(By.className(TokenRequestWindow.STYLE_NAME_WINDOW));
			}
		});
		commandCountAssert.assertEquals(1);

		// login in using faulty user name/login combination
		WebElement userName = driver.findElement(By.name("userName"));
		WebElement password = driver.findElement(By.name("password"));
		WebElement login = driver.findElement(By.xpath("//*[contains(.,'Log in')]"));
		WebElement reset = driver.findElement(By.xpath("//*[contains(.,'Reset')]"));
		userName.sendKeys("blabla");
		password.sendKeys("blabla");
		login.click();
		wait.until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				return d.findElement(By.className(TokenRequestWindow.STYLE_NAME_ERROR)).getText().
						contains("Login attempt has failed");
			}
		});
		WebElement error = driver.findElement(By.className(TokenRequestWindow.STYLE_NAME_ERROR));
		commandCountAssert.assertEquals(1);

		// press reset and verify that form is cleared
		reset.click();
		Assert.assertEquals("", error.getText());

		// no login -> error
		reset.click();
		userName.clear();
		password.sendKeys("luc");
		login.click();
		wait.until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				return null != d.findElement(By.xpath("//*[contains(.,'Please fill in a user name.')]"));
			}
		});
		commandCountAssert.assertEquals(0);

		// no password -> error
		reset.click();
		userName.sendKeys("luc");
		password.clear();
		login.click();
		wait.until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				return null != d.findElement(By.xpath("//*[contains(.,'Please fill in a password.')]"));
			}
		});
		commandCountAssert.assertEquals(0);

		// login using correct credentials
		reset.click();
		userName.sendKeys("luc");
		password.sendKeys("luc");
		login.click();
		// map appears
		wait.until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				return null != d.findElement(By.className(Application.APPLICATION_TITLE_STYLE));
			}
		});
		wait.until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				List<WebElement> elements = driver.findElements(By.xpath(LAYER_VECTOR_XPATH));
				return !elements.isEmpty();
			}
		});
		WebElement user = driver.findElement(By.className(Application.APPLICATION_USER_STYLE));
		Assert.assertEquals("user: Luc Van Lierde", user.getText());
		elements = driver.findElements(By.xpath(LAYER_RASTER_XPATH));
		Assert.assertFalse(elements.isEmpty()); // there should be a raster layer
		WebElement blabla = driver.findElement(By.xpath("//*[contains(.,'blabla')]")); // blabla button
		Assert.assertNotNull(blabla); // should exist
		Assert.assertFalse(blabla.getAttribute("style").contains("visibility: hidden")); // and not invisible
		// login window should be gone
		Assert.assertEquals(0, driver.findElements(By.className(TokenRequestWindow.STYLE_NAME_WINDOW)).size());
		// expecting approx 30 command invocations
		commandCountAssert.assertBetween(20, 40);

		WebElement logout = driver.findElement(By.xpath("//*[contains(.,'Log out')]"));
		logout.click();
		// the login window should appear
		wait.until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				return null != d.findElement(By.className(TokenRequestWindow.STYLE_NAME_WINDOW));
			}
		});
		source = driver.getPageSource();
		Assert.assertFalse(source.contains(LAYER_VECTOR));
		Assert.assertFalse(source.contains(LAYER_RASTER));
		commandCountAssert.assertEquals(2); // one with invalid token, one proper

		// login as other user
		userName = driver.findElement(By.name("userName"));
		password = driver.findElement(By.name("password"));
		login = driver.findElement(By.xpath("//*[contains(.,'Log in')]"));
		userName.sendKeys("marino");
		password.sendKeys("marino");
		login.click();

		wait.until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				List<WebElement> elements = driver.findElements(By.xpath(LAYER_RASTER_XPATH));
				return !elements.isEmpty();
			}
		});
		source = driver.getPageSource();
		Assert.assertFalse(source.contains(LAYER_VECTOR));
		blabla = driver.findElement(By.xpath("//*[contains(.,'blabla')]"));
		Assert.assertTrue(blabla.getAttribute("style").contains("visibility: hidden"));
	}

}
