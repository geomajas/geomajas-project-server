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

package org.geomajas.example.gwt.client.samples.plugin;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import org.geomajas.example.gwt.client.samples.base.SamplePanel;
import org.geomajas.example.gwt.client.samples.base.SamplePanelFactory;
import org.geomajas.example.gwt.client.samples.i18n.I18nProvider;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.Toolbar;
import org.geomajas.plugin.geocoder.client.GeocoderWidget;

/**
 * Sample to demonstrate default printing action.
 *
 * @author Jan De Moerloose
 *
 */
public class GeocoderSample extends SamplePanel {

	public static final String TITLE = "Geocoder";

	private static final String FIELD_LABEL = "label";
	private static final String FIELD_REGEX = "regex";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new GeocoderSample();
		}
	};

	/**
	 * @return The viewPanel Canvas
	 */
	public Canvas getViewPanel() {
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();

		final MapWidget map = new MapWidget("osmMap", "gwt-samples");

		final Toolbar toolbar = new Toolbar(map);
		toolbar.setButtonSize(Toolbar.BUTTON_SIZE_BIG);
		final GeocoderWidget geocoderWidget = new GeocoderWidget(map, "description", "Geocoder");
		toolbar.addMember(geocoderWidget);

		SelectItem geocoderSource = new SelectItem();
		geocoderSource.setOptionDataSource(getGeocoderSelectDataSource());
		geocoderSource.setDisplayField(FIELD_LABEL);
		geocoderSource.setValueField(FIELD_REGEX);
		geocoderSource.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				geocoderWidget.setServicePattern((String) event.getValue());
			}
		});
		DynamicForm geocoderSourceForm = new DynamicForm();
		geocoderSourceForm.setFields(geocoderSource);
		toolbar.addMember(geocoderSourceForm);

		layout.addMember(toolbar);
		layout.addMember(map);
		return layout;
	}

	private DataSource getGeocoderSelectDataSource() {
		DataSource dataSource = new DataSource();

		DataSourceField label = new DataSourceTextField(FIELD_LABEL);
		DataSourceField regex = new DataSourceTextField(FIELD_REGEX);
		dataSource.setFields(label, regex);

		Record record;
		record = new Record();
		record.setAttribute(FIELD_LABEL, "Yahoo! PlaceFinder");
		record.setAttribute(FIELD_REGEX, "yahoo");
		dataSource.addData(new Record());
		record = new Record();
		record.setAttribute(FIELD_LABEL, "GeoNames");
		record.setAttribute(FIELD_REGEX, "geonames");
		dataSource.addData(new Record());
		record = new Record();
		record.setAttribute(FIELD_LABEL, "offline");
		record.setAttribute(FIELD_REGEX, "offline");
		dataSource.addData(new Record());

		return dataSource;
	}

	public String getDescription() {
		return I18nProvider.getSampleMessages().geocoderDescription();
	}

	public String getSourceFileName() {
		return "classpath:org/geomajas/example/gwt/client/samples/plugin/GeocoderSample.txt";
	}

	public String[] getConfigurationFiles() {
		return new String[]{"classpath:org/geomajas/example/gwt/clientcfg/geocoder.xml",
				"classpath:org/geomajas/example/gwt/servercfg/raster/layerOsm.xml",
				"classpath:org/geomajas/example/gwt/clientcfg/layer/mapOsm.xml"};
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
