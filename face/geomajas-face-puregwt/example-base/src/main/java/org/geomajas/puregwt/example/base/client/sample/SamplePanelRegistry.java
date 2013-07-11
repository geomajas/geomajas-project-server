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

package org.geomajas.puregwt.example.base.client.sample;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Singleton that stores all known samples.
 * 
 * @author Pieter De Graef
 */
public final class SamplePanelRegistry {

	private static final Map<String, List<ShowcaseSampleDefinition>> FACTORIES;

	private static final Map<String, Integer> CATEGORIES = new HashMap<String, Integer>();

	private static final int DEFAULT_WEIGHT = 50;

	private static int lowestWeight = Integer.MAX_VALUE;

	static {
		FACTORIES = new HashMap<String, List<ShowcaseSampleDefinition>>();
	}

	private SamplePanelRegistry() {
	}

	public static void registerCategory(String category, int weight) {
		if (!CATEGORIES.containsKey(category)) {
			CATEGORIES.put(category, weight);
			FACTORIES.put(category, new ArrayList<ShowcaseSampleDefinition>());
			if (weight < lowestWeight) {
				lowestWeight = weight;
			}
		}
	}

	public static void registerFactory(String category, ShowcaseSampleDefinition factory) {
		if (!FACTORIES.containsKey(category)) {
			registerCategory(category, DEFAULT_WEIGHT);
		}
		List<ShowcaseSampleDefinition> factoryList = FACTORIES.get(category);
		factoryList.add(factory);
	}

	public static List<String> getCategories() {
		List<String> categoryList = new ArrayList<String>(CATEGORIES.keySet());
		Collections.sort(categoryList, new Comparator<String>() {

			public int compare(String o1, String o2) {
				Integer weight1 = CATEGORIES.get(o1);
				Integer weight2 = CATEGORIES.get(o2);
				return weight2.compareTo(weight1); // Inverse order!
			}
		});
		return categoryList;
	}

	public static List<ShowcaseSampleDefinition> getFactories(String category) {
		return FACTORIES.get(category);
	}

	public static List<ShowcaseSampleDefinition> getFactories() {
		List<ShowcaseSampleDefinition> factories = new ArrayList<ShowcaseSampleDefinition>();
		for (List<ShowcaseSampleDefinition> factoriesPerCategory : FACTORIES.values()) {
			factories.addAll(factoriesPerCategory);
		}
		return factories;
	}

	public static int getLowestCategoryWeight() {
		return lowestWeight;
	}
}