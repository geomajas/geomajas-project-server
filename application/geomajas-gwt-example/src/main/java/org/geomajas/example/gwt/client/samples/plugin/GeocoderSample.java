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
import org.geomajas.gwt.client.util.WidgetLayout;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.Toolbar;
import org.geomajas.plugin.geocoder.client.GeocoderWidget;

/**
 * Sample to demonstrate use of the geocoder plug-in.
 *
 * @author Joachim Van der Auwera
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

		final MapWidget map = new MapWidget("mapOsm", "gwt-samples");

		final Toolbar toolbar = new Toolbar(map);
		toolbar.setButtonSize(WidgetLayout.toolbarLargeButtonSize);
		final GeocoderWidget geocoderWidget = new GeocoderWidget(map, "description", "Geocoder");
		toolbar.addMember(geocoderWidget);

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

	public String getDescription() {
		return I18nProvider.getSampleMessages().geocoderDescription();
	}

	public String getSourceFileName() {
		return "classpath:org/geomajas/example/gwt/client/samples/plugin/GeocoderSample.txt";
	}

	public String[] getConfigurationFiles() {
		return new String[]{"WEB-INF/geocoder.xml",
				"WEB-INF/layerOsm.xml",
				"WEB-INF/mapOsm.xml"};
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
