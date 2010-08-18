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

import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItemIcon;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.IconClickEvent;
import com.smartgwt.client.widgets.form.fields.events.IconClickHandler;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import org.geomajas.command.CommandResponse;
import org.geomajas.global.Api;
import org.geomajas.gwt.client.command.CommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.map.MapView;
import org.geomajas.gwt.client.spatial.Bbox;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.plugin.geocoder.command.dto.GetLocationForStringAlternative;
import org.geomajas.plugin.geocoder.command.dto.GetLocationForStringRequest;
import org.geomajas.plugin.geocoder.command.dto.GetLocationForStringResponse;

import java.util.List;

/**
 * Widget for starting a geocoder location search.
 *
 * @author Joachim Van der Auwera
 * @since 1.0.0
 */
@Api
public class GeocoderWidget extends DynamicForm {

	private static final String LOCATION_FIELD = "Location";
	private static final String LOCATION_OBJECT = "Object";
	private MapWidget map;
	private TextItem textItem;
	private Window altWindow;
	private ListGrid altGrid;

	/**
	 * Create geocoder widget with default name and title.
	 *
	 * @param map map to apply search results
	 */
	@Api
	public GeocoderWidget(MapWidget map) {
		this(map, "description", "Search");
	}

	/**
	 * Create geocoder widget which allows searching a location from a string.
	 *
	 * @param map map to apply search results
	 * @param name widget name
	 * @param title label which is displayed left of the widget
	 */
	@Api
	public GeocoderWidget(MapWidget map, String name, String title) {
		textItem = new TextItem(name, title);
		this.map = map;

		textItem.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent keyPressEvent) {
				if ("enter".equalsIgnoreCase(keyPressEvent.getKeyName())) {
					goToLocation();
				}
			}
		});

		final PickerIcon findIcon = new PickerIcon(PickerIcon.SEARCH);
		final PickerIcon clearIcon = new PickerIcon(PickerIcon.CLEAR);
		textItem.setIcons(findIcon, clearIcon);

		textItem.addIconClickHandler(new IconClickHandler() {
			public void onIconClick(IconClickEvent iconClickEvent) {
				FormItemIcon icon = iconClickEvent.getIcon();
				if (clearIcon.getSrc().equals(icon.getSrc())) {
					clearLocation();
				} else {
					goToLocation();
				}
			}
		});

		this.setFields(textItem);
	}

	private void clearLocation() {
		textItem.setValue("");
	}

	private void goToLocation() {
		final String location = (String) textItem.getValue();

		GwtCommand command = new GwtCommand("command.geocoder.GetLocationForString");
		GetLocationForStringRequest request = new GetLocationForStringRequest();
		request.setCrs(map.getMapModel().getCrs());
		request.setLocation(location);
		command.setCommandRequest(request);
		GwtCommandDispatcher.getInstance().execute(command, new CommandCallback() {

			public void execute(CommandResponse commandResponse) {
				if (commandResponse instanceof GetLocationForStringResponse) {
					GetLocationForStringResponse response = (GetLocationForStringResponse) commandResponse;
					if (response.isLocationFound()) {
						removeAltWindow();
						org.geomajas.geometry.Bbox bbox = response.getBbox();
						map.getMapModel().getMapView().applyBounds(new Bbox(bbox), MapView.ZoomOption.LEVEL_FIT);
						textItem.setValue(response.getCanonicalLocation());
					} else {
						List<GetLocationForStringAlternative> alternatives = response.getAlternatives();
						if (null != alternatives && alternatives.size() > 0) {
							chooseAlternative(alternatives);
						} else {
							SC.say("Location " + location + " not found");
						}
					}
				}
			}
		});
	}

	private void removeAltWindow() {
		if (null != altWindow) {
			altWindow.destroy();
			altWindow = null;
		}
	}

	private void chooseAlternative(List<GetLocationForStringAlternative> alternatives) {
		if (null == altWindow) {
			altGrid = new ListGrid();
			altGrid.setWidth(300);
			altGrid.setHeight(200);
			altGrid.setCanEdit(false);
			altGrid.setPadding(5);

			ListGridField locationField = new ListGridField(LOCATION_FIELD);
			locationField.setCanEdit(false);
			locationField.setCanSort(false);
			locationField.setCanGroupBy(false);
			altGrid.setFields(locationField);
			altGrid.addRecordClickHandler(new RecordClickHandler() {
				public void onRecordClick(RecordClickEvent recordClickEvent) {
					GetLocationForStringAlternative alternative;
					alternative = (GetLocationForStringAlternative) recordClickEvent.getRecord()
							.getAttributeAsObject(LOCATION_OBJECT);
					org.geomajas.geometry.Bbox bbox = alternative.getBbox();
					map.getMapModel().getMapView().applyBounds(new Bbox(bbox), MapView.ZoomOption.LEVEL_FIT);
					textItem.setValue(alternative.getCanonicalLocation());
				}
			});

			altWindow = new Window();
			altWindow.setAutoSize(true);
			altWindow.setTitle("Location to jump to");
			altWindow.setAutoSize(true);
			altWindow.setLeft(20);
			altWindow.setTop(20);
			altWindow.setCanDragReposition(true);
			altWindow.setCanDragResize(true);
			altWindow.addItem(altGrid);
			altWindow.addCloseClickHandler(new CloseClickHandler() {
				public void onCloseClick(CloseClientEvent closeClientEvent) {
					removeAltWindow();
				}
			});

			map.addChild(altWindow);
		}
		altGrid.setData(toRecords(alternatives));
		altGrid.scrollTo(0, 0);
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
