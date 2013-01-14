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

package org.geomajas.testdata;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Service which allows you to test the number of command invocations.
 * <p/>
 * You have to init before first use. You can assert the number of invocations since either init or the previous assert.
 *
 * @author Joachim Van der Auwera
 */
public class CommandCountAssert {

	@Autowired
	private CommandCountService commandCountService;

	private long lastCount;

	public void init() {
		lastCount = commandCountService.getCount();
	}

	/**
	 * Verify exactly a specific number of command invocations have been done.
	 *
	 * @param count number of expected command invocations
	 */
	public void assertEquals(long count) {
		long newValue = commandCountService.getCount();
		Assert.assertEquals("Expected " + count + " command invocations, was " + (newValue - lastCount) + ".",
				lastCount + count, newValue);
		lastCount = newValue;
	}

	/**
	 * Verify exactly that less than a number of command invocations have been done.
	 * 
	 * @param maximum maximum number of expected command invocations
	 */
	public void assertMaximum(long maximum) {
		long newValue = commandCountService.getCount();
		Assert.assertTrue("Expected maximum " + maximum + " command invocations, was " + (newValue - lastCount) + ".",
				lastCount + maximum >= newValue);
		lastCount = newValue;
	}

	/**
	 * Verify that the number of command invocations is in a certain range.
	 * 
	 * @param minimum minimum number of expected command invocations
	 * @param maximum maximum number of expected command invocations
	 */
	public void assertBetween(long minimum, long maximum) {
		long newValue = commandCountService.getCount();
		Assert.assertTrue("Expected between " + minimum + " and " + maximum + " command invocations, was "
				+ (newValue - lastCount) + ".", lastCount + minimum <= newValue && lastCount + maximum >= newValue);
		lastCount = newValue;
	}

}
