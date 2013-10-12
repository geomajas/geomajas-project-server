/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.sld.editor.expert.client;

import java.util.LinkedHashMap;
import java.util.List;

import org.geomajas.codemirror.client.widget.CodeMirrorPanel;
import org.geomajas.sld.editor.expert.client.domain.SldInfo;
import org.geomajas.sld.editor.expert.client.i18n.SldEditorExpertMessages;
import org.geomajas.sld.editor.expert.client.presenter.SldEditorExpertPresenter;
import org.geomajas.sld.editor.expert.client.presenter.event.SldCancelEvent;
import org.geomajas.sld.editor.expert.client.presenter.event.SldValidateEvent;
import org.geomajas.sld.editor.expert.client.presenter.event.TemplateSelectEvent;
import org.geomajas.sld.editor.expert.client.view.ListBox;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

/**
 * The main editor panel containing toolbar and textarea.
 * <p>Template dropdownbox is only shown when templates are actually loaded.
 * 
 * @author Kristof Heirwegh
 */
public class SldEditorPanel extends Composite {
	
	/**
	 * UIBinder interface.
	 */
	interface MyUiBinder extends UiBinder<Widget, SldEditorPanel> {
	}

	private static final MyUiBinder UI_BINDER = GWT.create(MyUiBinder.class);

	private static final SldEditorExpertMessages MSG = GWT.create(SldEditorExpertMessages.class);
	
	@UiField
	protected Label templateLabel;
	@UiField
	protected ListBox templateListBox;
	@UiField
	protected Button validateBtn;
	@UiField
	protected Button saveBtn;
	@UiField
	protected Button cancelBtn;
	@UiField
	protected CodeMirrorPanel codeArea;
	
	private SldEditorExpertPresenter.MyView view;
	
	private String oldTemplate;

	public SldEditorPanel(EventBus eventBus, SldEditorExpertPresenter.MyView view) {
		initWidget(UI_BINDER.createAndBindUi(this));
		this.view = view;

		templateLabel.setVisible(false);
		templateListBox.setVisible(false);
		validateBtn.setTitle(MSG.validateButtonTooltip());
		saveBtn.setTitle(MSG.saveButtonTooltip());
	}

	public void setTemplates(List<SldInfo> templates) {
		LinkedHashMap<String, String> data = new LinkedHashMap<String, String>();
		for (SldInfo info : templates) {
			data.put(info.getName(), info.getTitle() != null ? info.getTitle() : info.getName());
		}
		templateListBox.setItems(data);
		templateListBox.setVisible(data.size() > 0);
		templateLabel.setVisible(templateListBox.isVisible());
	}

	public void setData(String xml) {
		// because of codemirrorFoefelare we can't guarantee that the widget already exists, so deferring it if not.
		if (codeArea.getEditor() != null) {
			codeArea.getEditor().setContent(xml == null ? "" : xml);
			codeArea.getEditor().clearHistory();
		} else {
			codeArea.setInitialData(xml);
		}
	}

	public void selectTemplateCancelled() {
		templateListBox.setValue(oldTemplate);
	}
	
	public String getData() {
		if (codeArea.getEditor() != null) {
			return codeArea.getEditor().getContent();
		} else {
			return null;
		}
	}

	/**
	 * Please note that you cannot actually set dirty == true, only to false.
	 * @param dirty
	 */
	public void setDataDirty(boolean dirty) {
		if (!dirty && codeArea.getEditor() != null) {
			codeArea.getEditor().markClean();
		}
	}
	
	public boolean isDataDirty() {
		return !codeArea.getEditor().isClean();
	}
	
	public void clearValues() {
		codeArea.getEditor().setContent("");
		codeArea.getEditor().markClean();
		templateListBox.setValue(null);
	}

	// ---------------------------------------------------------------

	@UiHandler(value = "validateBtn")
	protected void onValidateClick(ClickEvent event) {
		SldValidateEvent.fire(view);
	}

	@UiHandler(value = "saveBtn")
	protected void onSaveClick(ClickEvent event) {
		SldValidateEvent.fire(view, new SldValidateEvent(true));
	}

	@UiHandler(value = "cancelBtn")
	protected void onCancelClick(ClickEvent event) {
		SldCancelEvent.fire(view);
	}

	@UiHandler(value = "templateListBox")
	protected void onTemplateFocus(FocusEvent event) {
		oldTemplate = templateListBox.getValue();
	}

	@UiHandler(value = "templateListBox")
	protected void onTemplateChange(ChangeEvent event) {
		TemplateSelectEvent.fire(view, (String) templateListBox.getValue());
	}
}
