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
		return names.toArray(new String[names.size()]);
	}

}
