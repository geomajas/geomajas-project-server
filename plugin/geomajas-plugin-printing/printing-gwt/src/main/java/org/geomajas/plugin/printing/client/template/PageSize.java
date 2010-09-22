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
package org.geomajas.plugin.printing.client.template;

import java.util.ArrayList;
import java.util.List;

/**
 * Page size for printing, including name and metric values.
 * 
 * @author Jan De Moerloose
 * 
 */
public class PageSize {

	/** ISO A0 format. */
	public static final PageSize A0 = new PageSize(2384, 3370, "A0");

	/** ISO A1 format. */
	public static final PageSize A1 = new PageSize(1684, 2384, "A1");

	/** ISO A2 format. */
	public static final PageSize A2 = new PageSize(1191, 1684, "A2");

	/** ISO A3 format. */
	public static final PageSize A3 = new PageSize(842, 1191, "A3");

	/** ISO A4 format. */
	public static final PageSize A4 = new PageSize(595, 842, "A4");

	private static final List<PageSize> ALL = new ArrayList<PageSize>();
	static {
		ALL.add(A4);
		ALL.add(A3);
		ALL.add(A2);
		ALL.add(A1);
		ALL.add(A0);
	}

	private double width;

	private double height;

	private String name;

	private double metricWidth;

	private double metricHeight;

	private static final double METERS_PER_INCH = 0.0254;

	public PageSize(double width, double height, String name) {
		this.width = width;
		this.height = height;
		this.name = name;
		metricWidth = width / 72.0 * METERS_PER_INCH;
		metricHeight = height / 72.0 * METERS_PER_INCH;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public String getName() {
		return name;
	}

	public double getMetricWidth() {
		return metricWidth;
	}

	public double getMetricHeight() {
		return metricHeight;
	}

	public static PageSize getByName(String name) {
		for (PageSize p : ALL) {
			if (p.getName().equalsIgnoreCase(name)) {
				return p;
			}
		}
		throw new IllegalArgumentException("Unknown page size " + name);
	}

	public static String[] getAllNames() {
		List<String> names = new ArrayList<String>();
		for (PageSize p : ALL) {
			names.add(p.getName());
		}
		return names.toArray(new String[0]);
	}

}
