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

package org.geomajas.plugin.geocoder.client;

import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import org.geomajas.annotation.Api;
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
		super();
		this.setWidth(300);
		this.setHeight(200);
		this.setCanEdit(false);
		this.setPadding(5);

		ListGridField locationField = new ListGridField(LOCATION_FIELD);
		locationField.setCanEdit(false);
		locationField.setCanSort(false);
		locationField.setCanGroupBy(false);
		this.setFields(locationField);
		this.addRecordClickHandler(new GeocoderRecordClickHandler(widget));
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

	/**
	 * Default click handler for the alterantives.
	 *
	 * @author Joachim Van der Auwera
	 */
	private static final class GeocoderRecordClickHandler implements RecordClickHandler {

		private GeocoderWidget widget;

		private GeocoderRecordClickHandler(GeocoderWidget widget) {
			this.widget = widget;
		}

		public void onRecordClick(RecordClickEvent recordClickEvent) {
			GetLocationForStringAlternative alternative;
			alternative = (GetLocationForStringAlternative) recordClickEvent.getRecord()
					.getAttributeAsObject(LOCATION_OBJECT);
			widget.fireEvent(new SelectLocationEvent(widget.getMap(), alternative));
		}
	}
}
