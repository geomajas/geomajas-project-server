/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.plugin.deskmanager.client.gwt.manager.common;

import org.geomajas.plugin.deskmanager.client.gwt.common.FileUploadForm;
import org.geomajas.plugin.deskmanager.configuration.client.GeodeskLayoutInfo;

import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ColorPickerItem;
import com.smartgwt.client.widgets.form.fields.HiddenItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.validator.RegExpValidator;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * @author Kristof Heirwegh
 * FIXME: this should no longer user {@link GeodeskLayoutDto} but use a clientwidgetinfo
 */
public class GeodeskLayoutPanel extends HLayout {

	private GeodeskLayoutInfo geodeskLayout;

	private TextItem title;

	private ColorPickerItem titleColor;

	private ColorPickerItem bgColor;

	private DynamicForm form;

	private FileUploadForm logoFileForm;

	private FileUploadForm bannerFileForm;

	private HiddenItem logoUrl;

	private HiddenItem bannerUrl;

	private TextItem logoAlt;

	private ColorPickerItem borderColor;

	private ColorPickerItem titleBarColor;

	private TextItem logoHref;

	private GeodeskLayoutPreviewPanel preview;

	public void setDisabled(boolean disabled) {
		super.setDisabled(disabled);
		logoFileForm.setDisabled(disabled);
		bannerFileForm.setDisabled(disabled);
	}

	public GeodeskLayoutPanel() {
		setMembersMargin(5);

		// --- properties ---
		form = new DynamicForm();
		form.setWidth100();
		form.setHeight100();
		form.setAutoFocus(true); /* Set focus on first field */
		form.setNumCols(2);
		form.setColWidths(150, "*");

		title = new TextItem();
		title.setTitle("Titel geoloket");
		title.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				if (geodeskLayout != null) {
					geodeskLayout.setTitle((String) title.getValue());
					preview.refresh();
				}
			}
		});

		titleColor = new ColorPickerItem();
		titleColor.setTitle("Titel kleur");
		titleColor.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				if (geodeskLayout != null) {
					geodeskLayout.setTitleColor((String) titleColor.getValue());
					preview.refresh();
				}
			}
		});

		titleBarColor = new ColorPickerItem();
		titleBarColor.setTitle("Titel achtergrond kleur");
		titleBarColor.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				if (geodeskLayout != null) {
					geodeskLayout.setTitleBarColor((String) titleBarColor.getValue());
					preview.refresh();
				}
			}
		});

		bgColor = new ColorPickerItem();
		bgColor.setTitle("Achtergrond kleur");
		bgColor.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				if (geodeskLayout != null) {
					geodeskLayout.setBgColor((String) bgColor.getValue());
					preview.refresh();
				}
			}
		});

		borderColor = new ColorPickerItem();
		borderColor.setTitle("Kleur vensterranden");
		borderColor.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				if (geodeskLayout != null) {
					geodeskLayout.setBorderColor((String) borderColor.getValue());
					preview.refresh();
				}
			}
		});

		RegExpValidator regExpValidator = new RegExpValidator();
		regExpValidator
				.setExpression("^(http|https)\\://[a-zA-Z0-9\\-\\.]+\\.[a-zA-Z]{2,3}(:[a-zA-Z0-9]*)" +
						"?/?([a-zA-Z0-9\\-\\._\\?\\,\\'/\\\\\\+&amp;%\\$#\\=~])*[^\\.\\,\\)\\(\\s]$");

		logoUrl = new HiddenItem();
		bannerUrl = new HiddenItem();

		logoHref = new TextItem();
		logoHref.setTitle("Logo website link");
		logoHref.setValidators(regExpValidator);
		logoHref.setValidateOnChange(true);
		logoHref.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				if (geodeskLayout != null && logoHref.validate()) {
					geodeskLayout.setLogoHref((String) logoHref.getValue());
					preview.refresh();
				}
			}
		});
		logoAlt = new TextItem();
		logoAlt.setTitle("Alt text van het logo");
		logoAlt.setPrompt("Deze tekst verschijnt wanneer men met de muis over het logo gaat," +
				" of het logo niet beschikbaar is.");
		logoAlt.addChangedHandler(new ChangedHandler() {

			public void onChanged(ChangedEvent event) {
				if (geodeskLayout != null) {
					geodeskLayout.setLogoAlt((String) logoAlt.getValue());
					preview.refresh();
				}
			}
		});

		form.setTitleOrientation(TitleOrientation.LEFT);
		form.setFields(title, titleColor, titleBarColor, bgColor, borderColor, logoUrl, logoHref, logoAlt, bannerUrl);
		form.setAutoHeight();

		logoFileForm = new FileUploadForm("Logo :", "Ideale afmeting: 180 x 110px");
		logoFileForm.setDisabled(true);
		logoFileForm.addChangedHandler(new FileUploadForm.ChangedHandler() {

			public void onChange(FileUploadForm.ChangedEvent event) {
				logoUrl.setValue(event.getNewValue());
				if (geodeskLayout != null && logoUrl.getValue() != null && !"".equals(logoUrl.getValue())) {
					geodeskLayout.setLogoUrl(event.getNewValue());
					preview.refresh();
				}
			}
		});

		bannerFileForm = new FileUploadForm("Banner :", "Ideale afmeting: 500 x 226px");
		bannerFileForm.setDisabled(true);
		bannerFileForm.addChangedHandler(new FileUploadForm.ChangedHandler() {

			public void onChange(FileUploadForm.ChangedEvent event) {
				bannerUrl.setValue(event.getNewValue());
				if (geodeskLayout != null) {
					geodeskLayout.setBannerUrl(event.getNewValue());
					preview.refresh();
				}
			}
		});

		// --- form ---
		VLayout formLayout = new VLayout(3);
		formLayout.setPadding(5);
		formLayout.setIsGroup(true);
		formLayout.setGroupTitle("Geodesk layout");
		formLayout.addMember(form);
		formLayout.addMember(logoFileForm);
		formLayout.addMember(bannerFileForm);
		addMember(formLayout);

		// --- preview ---
		preview = new GeodeskLayoutPreviewPanel();
		preview.setWidth(350);
		preview.setHeight100();
		addMember(preview);
	}

	public void setGeodeskLayout(GeodeskLayoutInfo layout) {
		geodeskLayout = layout;
		preview.setLoketLayout(geodeskLayout);
		title.setValue(geodeskLayout.getTitle());
		titleColor.setValue(geodeskLayout.getTitleColor());
		bgColor.setValue(geodeskLayout.getBgColor());
		logoUrl.setValue(geodeskLayout.getLogoUrl());
		logoFileForm.setUrl(geodeskLayout.getLogoUrl());
		logoAlt.setValue(geodeskLayout.getLogoAlt());
		logoHref.setValue(geodeskLayout.getLogoHref());
		bannerUrl.setValue(geodeskLayout.getBannerUrl());
		bannerFileForm.setUrl(geodeskLayout.getBannerUrl());
		titleBarColor.setValue(geodeskLayout.getTitleBarColor());
		borderColor.setValue(geodeskLayout.getBorderColor());
	}

	public GeodeskLayoutInfo getLoketLayout() {
		return geodeskLayout;
	}
}
