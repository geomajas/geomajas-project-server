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
package org.geomajas.plugin.deskmanager.client.gwt.manager.datalayer.panels;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.plugin.deskmanager.client.gwt.manager.i18n.ManagerMessages;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.DataCallback;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Dialog;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.form.events.ItemChangedEvent;
import com.smartgwt.client.widgets.form.events.ItemChangedHandler;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * @author Kristof Heirwegh
 */
public class UploadShapefileForm extends VLayout {

	private static final ManagerMessages MESSAGES = GWT.create(ManagerMessages.class);
	
	private Dialog loadDialog;

	private FormPanel form;

	private List<ItemChangedHandler> handlers = new ArrayList<ItemChangedHandler>();

	private static final String SERVICE_NAME = GWT.getHostPageBaseURL() + "d/shapefileUpload";

	private static final String RESPONSE_INVALID_FILE = "Invalid file";

	private static final String RESPONSE_INVALID_LAYER = "Invalid Layer";

	private static final String RESPONSE_NO_RIGHTS = "No rights";

	private static final String RESPONSE_OK = "OK";

	private static final String URL_TOKEN = "userToken";

	private Hidden hiddenLayerName;

	private Hidden hiddenToken;

	private FileUpload upload;

	private String layerName = "";

	private DataCallback<String> onUploadFinished;

	public UploadShapefileForm() {
		form = new FormPanel();
		form.setAction(SERVICE_NAME);
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);

		// Create a panel to hold all of the form widgets.
		VerticalPanel panel = new VerticalPanel();
		hiddenLayerName = new Hidden("layername");
		panel.add(hiddenLayerName);
		hiddenToken = new Hidden(URL_TOKEN);
		panel.add(hiddenToken);
		form.setWidget(panel);

		// Create a FileUpload widget.
		upload = new FileUpload();
		upload.setName("uploadFormElement");
		upload.addChangeHandler(new ChangeHandler() {

			public void onChange(ChangeEvent event) {
				for (ItemChangedHandler item : handlers) {
					ItemChangedEvent ice = new ItemChangedEvent(null);
					item.onItemChanged(ice);
				}
			}
		});
		HorizontalPanel hp = new HorizontalPanel();
		hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		hp.setSpacing(3);
		com.google.gwt.user.client.ui.Label lblUpload = new com.google.gwt.user.client.ui.Label(
												MESSAGES.uploadShapefileLabelText() + " : ");
		lblUpload.setStyleName("formTitle");
		hp.add(lblUpload);
		hp.add(upload);
		panel.add(hp);

		form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {

			public void onSubmitComplete(SubmitCompleteEvent event) {
				String res = event.getResults().trim();
				if (res.startsWith("<")) {
					int start = res.indexOf(">") + 1;
					int end = res.indexOf("<", start);
					res = res.substring(start, end);
				}
				uploadFinished(res);
			}
		});

		// -------------------------------------------------

		addMember(form);
	}

	/**
	 * Note that if you set the name it will be readonly and will overwrite existing data!
	 */
	public void setData(String layerName) {
		reset();
		if (layerName != null && !"".equals(layerName)) {
			this.layerName = layerName;
		} else {
			this.layerName = "";
		}
	}

	public String getFileName() {
		return upload.getFilename();
	}

	/**
	 * This is a thin wrapper, the returned events are useless, so don't use it...
	 * 
	 * @param handler
	 */
	public void addItemChangedHandler(ItemChangedHandler handler) {
		handlers.add(handler);
	}

	public void setDisabled(boolean disabled) {
		super.setDisabled(disabled);
		upload.setEnabled(!disabled);
	}

	public void reset() {
		this.layerName = "";
		form.reset();
	}

	public boolean validate() {
		return (upload.getFilename() != null && !"".equals(upload.getFilename()));
	}

	public void setColWidths(Object... colWidths) {
	}

	/**
	 * Returns dataSourceName.
	 * 
	 * @param onUploadFinished
	 */
	public void upload(DataCallback<String> onUploadFinished) {
		this.onUploadFinished = onUploadFinished;
		if (validate()) {
			HTMLFlow msg = new HTMLFlow(MESSAGES.uploadShapefileUploadingFile());
			msg.setWidth100();
			msg.setHeight100();
			msg.setAlign(Alignment.CENTER);
			msg.setPadding(20);
			msg.setOverflow(Overflow.HIDDEN);
			loadDialog = new Dialog();
			loadDialog.setShowCloseButton(false);
			loadDialog.setWidth(330);
			loadDialog.setHeight(100);
			loadDialog.setIsModal(true);
			loadDialog.setShowModalMask(true);
			loadDialog.setTitle(MESSAGES.titlePleaseWait());
			loadDialog.addItem(msg);
			loadDialog.show();
			hiddenLayerName.setValue(layerName);
			hiddenToken.setValue(GwtCommandDispatcher.getInstance().getUserToken());
			form.submit();
		}
	}

	private void uploadFinished(String result) {
		String dataSourceName = null;
		loadDialog.hide();
		if (RESPONSE_INVALID_FILE.equals(result)) {
			SC.warn(MESSAGES.uploadShapefileResponseInvalidFile());
		} else if (RESPONSE_INVALID_LAYER.equals(result)) {
			SC.warn(MESSAGES.uploadShapefileResponseInvalidLayer());
		} else if (RESPONSE_NO_RIGHTS.equals(result)) {
			SC.warn(MESSAGES.uploadShapefileResponseNoRights());
		} else if (result.startsWith(RESPONSE_OK)) {
			String[] res = result.split("_", 2);
			if (res != null && res.length == 2) {
				dataSourceName = res[1];
				SC.say(MESSAGES.uploadShapefileResponseOK());
			} else {
				SC.warn(MESSAGES.uploadShapefileResponseOkButWrong(result));
			}
		} else {
			SC.warn(MESSAGES.uploadShapefileResponseDeafultNOK(result));
		}
		if (onUploadFinished != null) {
			onUploadFinished.execute(dataSourceName);
		}
	}
}
