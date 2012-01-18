package org.geomajas.plugin.rasterizing.gwt.example;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;


/**
 * Verify that the application loads properly.
 *
 * @author Jan De Moerloose
 */
public class LoadsProperlyTestInt {

	private WebDriver driver;

	@Before
	public void setUp() {
		driver = new FirefoxDriver();
	}

	@After
	public void tearDown() {
		driver.quit();
	}

	/**
	 * Simple test which verifies that the demo starts.
	 *
	 * @throws Exception oops
	 */
	@Test
	public void testDemoLoadsProperly() throws Exception {
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.pollingEvery(500, TimeUnit.MILLISECONDS);

		driver.get("http://localhost:9080/");

		// checks for sc startup 
		wait.until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				return null != d.findElement(By.id("isc_M"));
			}
		});
	}

}
