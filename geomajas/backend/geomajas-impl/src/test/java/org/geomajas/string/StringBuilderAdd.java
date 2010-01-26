/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
