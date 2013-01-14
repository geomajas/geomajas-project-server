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

package org.geomajas.plugin.geocoder.gwt.example.client;

import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.Toolbar;
import org.geomajas.gwt.example.base.SamplePanel;
import org.geomajas.gwt.example.base.SamplePanelFactory;
import org.geomajas.plugin.geocoder.client.GeocoderWidget;
import org.geomajas.plugin.geocoder.gwt.example.client.i18n.GeocoderMessages;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Sample to demonstrate use of the geocoder plug-in.
 *
 * @author Joachim Van der Auwera
 */
public class GeocoderPanel extends SamplePanel {

	public static final String TITLE = "Geocoder";

	public static final GeocoderMessages MESSAGES = GWT.create(GeocoderMessages.class);

	private static final String FIELD_LABEL = "label";
	private static final String FIELD_REGEX = "regex";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new GeocoderPanel();
		}
	};

	/** {@inheritDoc} */
	public Canvas getViewPanel() {
		VLayout layout = new VLayout();
		layout.setWidth100();
		layout.setHeight100();

		// @extract-start createGwtWidget, Create GWT widget in toolbar
		final MapWidget map = new MapWidget("mapGeocoderOsm", "appGeocoder");

		final Toolbar toolbar = new Toolbar(map);
		// @extract-skip-start
		toolbar.setButtonSize(WidgetLayout.toolbarLargeButtonSize);
		// @extract-skip-end
		final GeocoderWidget geocoderWidget = new GeocoderWidget(map, "description", "Geocoder");
		toolbar.addMember(geocoderWidget);
		// @extract-end

		SelectItem geocoderSource = new SelectItem("geocoderSource", "");
		geocoderSource.setDefaultToFirstOption(true);
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

		// add button to toggle title
		final Button button = new Button("Hide title");
		button.addClickHandler(new ClickHandler() {

			private boolean showTitle;

			public void onClick(ClickEvent event) {
				geocoderWidget.setShowTitle(showTitle);
				button.setTitle(showTitle ? MESSAGES.hideTitle() : MESSAGES.showTitle());
				showTitle = !showTitle;
				
			}
		});
		toolbar.addMember(button);
		
		layout.addMember(toolbar);
		layout.addMember(map);
		return layout;
	}

	private DataSource getGeocoderSelectDataSource() {
		DataSource dataSource = new DataSource();
		dataSource.setClientOnly(true);
		DataSourceField label = new DataSourceTextField(FIELD_LABEL);
		DataSourceField regex = new DataSourceTextField(FIELD_REGEX);
		dataSource.setFields(label, regex);

		Record record;
		record = new Record();
		record.setAttribute(FIELD_LABEL, "all");
		record.setAttribute(FIELD_REGEX, ".*");
		dataSource.addData(record);
		record = new Record();
		record.setAttribute(FIELD_LABEL, "Yahoo! PlaceFinder");
		record.setAttribute(FIELD_REGEX, "yahoo");
		dataSource.addData(record);
		record = new Record();
		record.setAttribute(FIELD_LABEL, "GeoNames");
		record.setAttribute(FIELD_REGEX, "geonames");
		dataSource.addData(record);
		record = new Record();
		record.setAttribute(FIELD_LABEL, "offline");
		record.setAttribute(FIELD_REGEX, "offline");
		dataSource.addData(record);

		return dataSource;
	}

	/** {@inheritDoc} */
	public String getDescription() {
		return MESSAGES.geocoderDescription();
	}

	/** {@inheritDoc} */
	public String[] getConfigurationFiles() {
		return new String[]{"classpath:org/geomajas/plugin/geocoder/gwt/example/context/geocoder.xml",
				"classpath:org/geomajas/plugin/geocoder/gwt/example/context/appGeocoder.xml",
				"classpath:org/geomajas/plugin/geocoder/gwt/example/context/mapGeocoderOsm.xml",
				"classpath:org/geomajas/gwt/example/base/layerOsm.xml"};
	}

	/** {@inheritDoc} */
	public String ensureUserLoggedIn() {
		return "luc";
	}
}
