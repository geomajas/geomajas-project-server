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
import org.geomajas.sld.editor.common.client.i18n.SldEditorMessages;
import org.geomajas.sld.editor.common.client.view.ViewUtil;
import org.geomajas.sld.editor.expert.client.domain.RawSld;
import org.geomajas.sld.editor.expert.client.domain.SldInfo;
import org.geomajas.sld.editor.expert.client.i18n.SldEditorExpertMessages;
import org.geomajas.sld.editor.expert.client.presenter.SldEditorExpertPresenter;
import org.geomajas.sld.editor.expert.client.presenter.event.TemplateSelectEvent;

import com.google.gwt.core.shared.GWT;
import com.google.web.bindery.event.shared.EventBus;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * The main editor panel containing toolbar and textarea.
 * 
 * @author Kristof Heirwegh
 */
public class SldEditorPanel extends VLayout {
	
	private static final SldEditorMessages MSG = GWT.create(SldEditorMessages.class);
	private static final SldEditorExpertMessages EXP_MSG = GWT.create(SldEditorExpertMessages.class);
	
	private CodeMirrorPanel codeArea;
	private ToolStrip buttonBar;
	private SelectItem templateSelect;
	private SldEditorExpertPresenter.MyView view;
	private ViewUtil viewUtil;

	public SldEditorPanel(EventBus eventBus, ViewUtil viewUtil, SldEditorExpertPresenter.MyView view) {
		super(0);
		this.viewUtil = viewUtil;
		this.view = view;

		buttonBar = new ToolStrip();
		buttonBar.setWidth("650px");
		codeArea = new CodeMirrorPanel();
		codeArea.setHeight("400px");
		codeArea.setWidth("650px");

		addMember(buttonBar);
		addMember(codeArea);

		createActions();
		// make configurable
	}

	public void setTemplates(List<SldInfo> templates) {
		LinkedHashMap<String, String> data = new LinkedHashMap<String, String>();
		for (SldInfo info : templates) {
			data.put(info.getName(), info.getTitle() != null ? info.getTitle() : info.getName());
		}
		templateSelect.clearValue();
		templateSelect.setValueMap(data);
	}
	
	public void setData(RawSld raw) {
		codeArea.getEditor().setContent(raw.getXml());
	}

	public void clearValues() {
		codeArea.getEditor().setContent("");
		
		// should this clear the templates dropdownbox?
	}
	
	// ---------------------------------------------------------------

	private void createActions() {
		ToolStripButton saveBtn = new ToolStripButton();
		saveBtn.setIcon("[ISOMORPHIC]/" + "icons/silk/disk.png");
		saveBtn.setTitle(MSG.saveButtonTitle());
		saveBtn.setTooltip(MSG.saveButtonTooltip());
		saveBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				onSave();
			}
		});
		
		ToolStripButton cancelBtn = new ToolStripButton();
		cancelBtn.setIcon("[ISOMORPHIC]/" + "icons/silk/cancel.png");
		cancelBtn.setTitle(MSG.cancelButtonTitle());
		cancelBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				onCancel();
			}
		});

		ToolStripButton validateBtn = new ToolStripButton();
		validateBtn.setIcon("[ISOMORPHIC]/" + "icons/silk/tick.png");
		validateBtn.setTitle(EXP_MSG.validateButtonTitle());
		validateBtn.setTooltip(EXP_MSG.validateButtonTooltip());
		validateBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				onValidate();
			}
		});
		
		templateSelect = new SelectItem();
		templateSelect.setTitle(EXP_MSG.templateSelectTitle());
		templateSelect.setTooltip(EXP_MSG.templateSelectTooltip());
		//fontItem.setShowTitle(false);
		templateSelect.setWidth(150);
		templateSelect.addChangedHandler(new ChangedHandler() {
			public void onChanged(ChangedEvent event) {
				// check dirty or not empty
//				viewUtil.showYesNoMessage("Bent u zeker dat u de SLD '" + record.getAttribute(SLD_NAME_ATTRIBUTE_NAME)
//						+ "' wilt verwijderen?", new YesNoCallback() {
//
//					public void onYes() {
//						SldListRemoveEvent.fire(StyledLayerDescriptorListView.this);
//					}
//
//					public void onNo() {
//					}
//
//					public void onCancel() {
//					}
//				});

				TemplateSelectEvent.fire(view, (String) templateSelect.getValue());
			}
		});
		
		buttonBar.addFormItem(templateSelect);
		buttonBar.addButton(validateBtn);
		buttonBar.addSeparator();
		buttonBar.addButton(cancelBtn);
		buttonBar.addButton(saveBtn);
	}

	// ---------------------------------------------------------------

	private void onSave() {
		SC.say("Saving...");
	}
	
	private void onCancel() {
		SC.say("Canceling...");
	}
	
	private void onValidate() {
		SC.say("Validating...");
	}
}
