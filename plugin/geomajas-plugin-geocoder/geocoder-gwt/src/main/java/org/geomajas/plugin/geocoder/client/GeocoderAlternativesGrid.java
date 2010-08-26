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

package org.geomajas.plugin.geocoder.client;

import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import org.geomajas.global.Api;
import org.geomajas.plugin.geocoder.client.event.SelectLocationEvent;
import org.geomajas.plugin.geocoder.command.dto.GetLocationForStringAlternative;

import java.util.List;

/**
 * Grid object with geocoder alternatives.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api
public class GeocoderAlternativesGrid extends ListGrid {

	private static final String LOCATION_FIELD = "Location";
	private static final String LOCATION_OBJECT = "Object";

	/**
	 * Create a grid with the alternatives.
	 *
	 * @param widget geocoder widget
	 * @param alternatives list of alternatives
	 */
	@Api
	public GeocoderAlternativesGrid(final GeocoderWidget widget, List<GetLocationForStringAlternative> alternatives) {
		this.setWidth(300);
		this.setHeight(200);
		this.setCanEdit(false);
		this.setPadding(5);

		ListGridField locationField = new ListGridField(LOCATION_FIELD);
		locationField.setCanEdit(false);
		locationField.setCanSort(false);
		locationField.setCanGroupBy(false);
		this.setFields(locationField);
		this.addRecordClickHandler(new RecordClickHandler() {
			public void onRecordClick(RecordClickEvent recordClickEvent) {
				GetLocationForStringAlternative alternative;
				alternative = (GetLocationForStringAlternative) recordClickEvent.getRecord()
						.getAttributeAsObject(LOCATION_OBJECT);
				widget.fireEvent(new SelectLocationEvent(widget.getMap(), alternative));
			}
		});
		update(alternatives);
	}

	/**
	 * Update alternatives grid with given alternatives.
	 *
	 * @param alternatives alternatives
	 */
	@Api
	public void update(List<GetLocationForStringAlternative> alternatives) {
		this.setData(toRecords(alternatives));
		this.scrollTo(0, 0);

	}

	private ListGridRecord[] toRecords(List<GetLocationForStringAlternative> alternatives) {
		ListGridRecord[] records = new ListGridRecord[alternatives.size()];
		for (int i = 0; i < records.length; i++) {
			GetLocationForStringAlternative alt = alternatives.get(i);
			ListGridRecord record = new ListGridRecord();

			record.setAttribute(LOCATION_FIELD, alt.getCanonicalLocation());
			record.setAttribute(LOCATION_OBJECT, alt);

			records[i] = record;
		}
		return records;
	}

}
