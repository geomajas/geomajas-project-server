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

package org.geomajas.example.gwt.client.samples.attribute;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.configuration.AttributeInfo;
import org.geomajas.example.gwt.client.samples.base.SamplePanel;
import org.geomajas.example.gwt.client.samples.base.SamplePanelFactory;
import org.geomajas.example.gwt.client.samples.i18n.I18nProvider;
import org.geomajas.gwt.client.map.event.MapModelEvent;
import org.geomajas.gwt.client.map.event.MapModelHandler;
import org.geomajas.gwt.client.map.layer.VectorLayer;
import org.geomajas.gwt.client.widget.FeatureAttributeEditor;
import org.geomajas.gwt.client.widget.MapWidget;
import org.geomajas.gwt.client.widget.attribute.FeatureForm;
import org.geomajas.gwt.client.widget.attribute.FeatureFormFactory;
import org.geomajas.gwt.client.widget.attribute.AttributeFormFieldRegistry;
import org.geomajas.gwt.client.widget.attribute.AttributeFormFieldRegistry.DataSourceFieldFactory;
import org.geomajas.gwt.client.widget.attribute.AttributeFormFieldRegistry.FormItemFactory;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.RowSpacerItem;
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

		// Creating the custom form factory:
		final FeatureFormFactory myFactory = new FeatureFormFactory() {

			public FeatureForm createFeatureForm(VectorLayer layer) {
				// Create the default form:
				FeatureForm form = new FeatureForm(layer);
				form.getWidget().setNumCols(4);
				form.getWidget().setWidth(450);
				form.getWidget().setColWidths(100, 180, 20, 150);

				form.getWidget().setGroupTitle("Custom Attribute Form");
				form.getWidget().setIsGroup(true);

				DataSource source = new DataSource();
				List<FormItem> formItems = new ArrayList<FormItem>();

				// Go over all attribute definitions:
				for (AttributeInfo info : layer.getLayerInfo().getFeatureInfo().getAttributes()) {
					if (info.isIncludedInForm()) {
						DataSourceField field = AttributeFormFieldRegistry.createDataSourceField(layer, info);
						field.setCanEdit(info.isEditable());
						source.addField(field);

						FormItem formItem = AttributeFormFieldRegistry.createFormItem(layer, info);
						formItem.setWidth("*");

						if ("dateAttr".equals(info.getName())) {
							formItem.setColSpan(4);
						} else if ("stringAttr".equals(info.getName())) {
							// Quickly insert a row spacer before the 'stringAttr' item (which is the text area).
							formItems.add(new RowSpacerItem());
						}
						formItems.add(formItem);
					}
				}

				// Finish the form:
				form.getWidget().setDataSource(source);
				form.getWidget().setFields(formItems.toArray(new FormItem[] {}));
				return form;
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
