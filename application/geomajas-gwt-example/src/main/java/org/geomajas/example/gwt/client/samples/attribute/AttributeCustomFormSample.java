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

package org.geomajas.example.gwt.client.samples.attribute;

import org.geomajas.example.gwt.client.samples.base.SamplePanel;
import org.geomajas.example.gwt.client.samples.base.SamplePanelFactory;
import org.geomajas.example.gwt.client.samples.i18n.I18nProvider;
import org.geomajas.gwt.client.map.event.MapModelEvent;
import org.geomajas.gwt.client.map.event.MapModelHandler;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.widget.FeatureAttributeEditor;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.attribute.AttributeFormFieldRegistry;
import org.geomajas.gwt.client.widget.attribute.DataSourceFieldFactory;
import org.geomajas.gwt.client.widget.attribute.FormItemFactory;
import org.geomajas.gwt.client.widget.attribute.FeatureForm;
import org.geomajas.gwt.client.widget.attribute.FeatureFormFactory;

import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * <p>
 * Sample that shows how a custom attribute form can be used for editing features.
 * </p>
 * 
 * @author Pieter De Graef
 */
public class AttributeCustomFormSample extends SamplePanel {

	public static final String TITLE = "AttributeCustomForm";

	public static final SamplePanelFactory FACTORY = new SamplePanelFactory() {

		public SamplePanel createPanel() {
			return new AttributeCustomFormSample();
		}
	};

	public Canvas getViewPanel() {
		// Start out by registering a new type in the AttributeFormFieldRegistry. When an attribute is configured with
		// the 'formInputType' equal to "myTextAre", then this type will be used (a text area):
		AttributeFormFieldRegistry.registerCustomFormItem("myTextArea", new DataSourceFieldFactory() {

			public DataSourceField create() {
				return new DataSourceTextField();
			}
		}, new FormItemFactory() {

			public FormItem create() {
				TextAreaItem textAreaItem = new TextAreaItem();
				textAreaItem.setColSpan(4);
				textAreaItem.setHeight(150);
				return textAreaItem;
			}
		}, null);

		final FeatureFormFactory<DynamicForm> myFactory = new FeatureFormFactory<DynamicForm>() {

			public FeatureForm<DynamicForm> createFeatureForm(VectorLayer layer) {
				return new AttributeCustomForm(layer);
			}

		};

		// Continue creating the layout:
		final VLayout layout = new VLayout();
		layout.setMembersMargin(10);
		layout.setWidth100();
		layout.setHeight100();

		// Map with ID beansMap is defined in the XML configuration. (contains any type of attribute)
		final MapWidget map = new MapWidget("mapBeansCustomForm", "gwt-samples");
		map.setVisible(false);
		layout.addMember(map);
		map.init();

		map.getMapModel().addMapModelHandler(new MapModelHandler() {

			public void onMapModelChange(MapModelEvent event) {
				VectorLayer layer = (VectorLayer) map.getMapModel().getLayer("beansLayerCustomForm");
				FeatureAttributeEditor editor = new FeatureAttributeEditor(layer, false, myFactory);
				layout.addMember(editor);
			}
		});

		return layout;
	}

	public String getDescription() {
		return I18nProvider.getSampleMessages().attributeCustomFormDescription();
	}

	public String getSourceFileName() {
		return "classpath:org/geomajas/example/gwt/client/samples/attribute/AttributeCustomFormSample.txt";
	}

	public String[] getConfigurationFiles() {
		return new String[] { "WEB-INF/layerBeansCustomForm.xml", "WEB-INF/mapBeansCustomForm.xml" };
	}

	public String ensureUserLoggedIn() {
		return "luc";
	}
}
