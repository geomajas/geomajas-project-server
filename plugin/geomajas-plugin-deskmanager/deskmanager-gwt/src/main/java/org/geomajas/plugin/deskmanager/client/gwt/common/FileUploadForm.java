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
package org.geomajas.plugin.deskmanager.client.gwt.common;

import java.util.ArrayList;
import java.util.List;

import org.geomajas.annotation.Api;
import org.geomajas.gwt.client.Geomajas;
import org.geomajas.gwt.client.util.Notify;
import org.geomajas.gwt.client.util.UrlBuilder;
import org.geomajas.plugin.deskmanager.client.gwt.common.i18n.CommonMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.layout.HLayout;

/**
 * A form that uploads files to the server.
 * 
 * @author Kristof Heirwegh
 * @author Oliver May
 * @since 1.0.0
 */
@Api(allMethods = true)
public class FileUploadForm extends HLayout {
	
	private static final CommonMessages MESSAGES = GWT.create(CommonMessages.class);

	private static final String SERVICE_NAME = GWT.getHostPageBaseURL() + "d/fileUpload";

	private List<ChangedHandler> changedHandlers = new ArrayList<ChangedHandler>();

	private FileUpload upload;

	private FormPanel form;

	private Label lbl;

	private String url;

	private Img previewImage;

	@Override
	public void setDisabled(boolean disabled) {
		if (upload != null) {
			upload.setEnabled(!disabled);
		}
		if (lbl != null) {
			lbl.setDisabled(disabled);
		}
	}

	/**
	 * Default contstuctor, don't use, instead use FileUploadForm(String label, String tooltip).
	 */
	public FileUploadForm() {
		this(null, null);
	}
	
	/**
	 * Construct a file upload form.
	 * 
	 * @param label the label.
	 * @param tooltip the tooltip.
	 */
	public FileUploadForm(String label, String tooltip) {
		super(5);
		setHeight(50);
		form = new FormPanel();
		form.setAction(SERVICE_NAME);
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);
		form.setHeight("16");

		VerticalPanel panel = new VerticalPanel();
		form.setWidget(panel);

		upload = new FileUpload();
		upload.setName("uploadFormElement");
		panel.add(upload);
		upload.addChangeHandler(new ChangeHandler() {

			public void onChange(ChangeEvent event) {
				if (hasFile()) {
					Notify.info(MESSAGES.fileIsUploading());
					form.submit();
				}
			}
		});

		form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {

			public void onSubmitComplete(SubmitCompleteEvent event) {
				String res = event.getResults().trim();
				if (res.contains("OK[")) {
					int start = res.indexOf("[");
					int stop = res.indexOf("]");
					String result = res.substring(start + 1, stop);
					UrlBuilder builder = new UrlBuilder(GdmLayout.FILEDOWNLOAD_URL);
					builder.addParameter(GdmLayout.FILEDOWNLOAD_ID, result);
					String oldResult = url;
					setUrl(builder.toString());
					fireChangedEvent(new ChangedEvent(this, oldResult, getUrl()));
				} else {
					SC.say(MESSAGES.errorWhileUploadingFile() + "<br />("
							+ event.getResults() + ")");
				}
			}
		});

		// ----------------------------------------------------------

		if (label != null) {
			lbl = new Label(label);
			lbl.setWidth(147);
			lbl.setHeight(20);
			lbl.setAlign(Alignment.RIGHT);
			lbl.setBaseStyle("formTitle");
			lbl.setShowDisabled(true);
			lbl.setDisabled(true);
			if (tooltip != null) {
				lbl.setTooltip(tooltip);
			}
	
			addMember(lbl);
		}
		addMember(form);
		previewImage = new Img();
		previewImage.setHeight(50);
		previewImage.setWidth(50);
		previewImage.setShowDisabled(false);
		addMember(previewImage);
	}

	/**
	 * Check if the upload form contains a file.
	 * 
	 * @return true if the upload form contains a file.
	 */
	public boolean hasFile() {
		String val = upload.getFilename();
		return (val != null && !"".equals(val));
	}

	/**
	 * Get the url of the uploaded file.
	 * 
	 * @return the url to the file
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Set the url of the current file.
	 * 
	 * @param url the url
	 */
	public void setUrl(String url) {
		previewImage.setSrc(Geomajas.getDispatcherUrl() + url);
		this.url = url;
	}

	/**
	 * Register a changed handler.
	 * 
	 * @param handler the handler to add
	 */
	public void addChangedHandler(ChangedHandler handler) {
		changedHandlers.add(handler);
	}

	/**
	 * Unregister a changed handler.
	 * 
	 * @param handler the handler to remove
	 */
	public void removeChangedHandler(ChangedHandler handler) {
		if (changedHandlers.contains(handler)) {
			changedHandlers.remove(handler);
		}
	}

	private void fireChangedEvent(ChangedEvent event) {
		for (ChangedHandler handler : changedHandlers) {
			handler.onChange(event);
		}
	}

	/**
	 * Event handler for the file upload form.
	 * 
	 * @author Oliver May
	 *
	 */
	public interface ChangedHandler {

		void onChange(ChangedEvent event);
	}

	/**
	 * Event for the file upload form.
	 * 
	 * @author Oliver May
	 *
	 */
	public static final class ChangedEvent {

		private Object source;

		private String newValue;

		private String oldValue;

		private ChangedEvent(Object source, String oldValue, String newValue) {
			this.source = source;
			this.oldValue = oldValue;
			this.newValue = newValue;
		}

		public Object getSource() {
			return source;
		}

		public String getNewValue() {
			return newValue;
		}

		public String getOldValue() {
			return oldValue;
		}
	}
}
