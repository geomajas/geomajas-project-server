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
package org.geomajas.widget.searchandfilter.client.widget.search;

import java.util.List;

import org.geomajas.gwt.client.widget.FeatureSearch;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.widget.searchandfilter.client.SearchAndFilterMessages;
import org.geomajas.widget.searchandfilter.search.dto.AndCriterion;
import org.geomajas.widget.searchandfilter.search.dto.AttributeCriterion;
import org.geomajas.widget.searchandfilter.search.dto.Criterion;
import org.geomajas.widget.searchandfilter.search.dto.OrCriterion;

import com.google.gwt.core.client.GWT;

/**
 * Searchpanel using the default attributesearch widget.
 *
 * @see SearchWidgetRegistry.
 * @author Kristof Heirwegh
 */
public class AttributeSearchPanel extends SearchPanel {

	public static final String IDENTIFIER = "AttributeSearch";

	private final SearchAndFilterMessages messages = GWT.create(SearchAndFilterMessages.class);

	private FeatureSearch featureSearch;

	public AttributeSearchPanel(final MapWidget mapWidget) {
		super(mapWidget);
		featureSearch = new FeatureSearch(mapWidget.getMapModel(), true);
		featureSearch.setWidth100();
		featureSearch.setHeight100();
		setWidth(550);
		setHeight(350);
		addChild(featureSearch);
	}

	@Override
	public boolean validate() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Criterion getFeatureSearchCriterion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void reset() {
	}

	@Override
	public void initialize(Criterion criterion) {
		List<Criterion> critters;
		if (criterion == null) {
			reset();
			return;
		} else if (criterion instanceof OrCriterion) {
			critters = ((OrCriterion) criterion).getCriteria();

		} else if (criterion instanceof AndCriterion) {
			critters = ((AndCriterion) criterion).getCriteria();

		} else {
			throw new IllegalArgumentException("Criterion must be of type Or or And.");
		}

		for (Criterion critter : critters) {
			if (critter instanceof AttributeCriterion) {
				// TODO add to gui
			} else {
				throw new IllegalArgumentException("Criterion must be of type AttributeCriterion.");
			}
		}
	}

	@Override
	public String getName() {
		return messages.attributeSearchWidgetTitle();
	}
}
