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

package org.geomajas.string;

/**
 * ...
 *
 * @author Joachim Van der Auwera
 */
public class StringBuilderAdd {

	public static void main(String args[]) {
		long startMemory = Runtime.getRuntime().totalMemory();
		long startTime = System.currentTimeMillis();
		for (int i = 50000; i > 0; i--) {
			String base1 = "" + (char) (65 + (i % 26)); // assure we don't always start with the same letter
			String base2 = "" + (char) (65 + ((i + 1) % 26)); // assure we don't always start with the same letter
			String calc = "";
			for (int j = 100; j > 0; j--) {
				StringBuilder sb = new StringBuilder();
				sb.append(calc);
				sb.append(base1);
				sb.append(base2);
				calc = sb.toString();
				process(calc);
			}
		}
		long endTime = System.currentTimeMillis();
		long endMemory = Runtime.getRuntime().totalMemory();
		System.out.println(
				"time spent " + (endTime - startTime) + "ms, memory used " + ((endMemory - startMemory) / 1024) + "kB");
	}

	private static void process(String value) {
		// we don't do anything here, as that may slow the timing too much, but need string!
		//System.out.println(value);
	}

}
