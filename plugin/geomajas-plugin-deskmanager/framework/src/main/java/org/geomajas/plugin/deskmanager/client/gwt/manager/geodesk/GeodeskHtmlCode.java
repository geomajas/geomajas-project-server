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
package org.geomajas.plugin.deskmanager.client.gwt.manager.geodesk;

import org.geomajas.plugin.deskmanager.client.gwt.common.CommonLayout;
import org.geomajas.plugin.deskmanager.client.gwt.geodesk.util.CodeServer;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.GeodeskEvent;
import org.geomajas.plugin.deskmanager.client.gwt.manager.events.GeodeskSelectionHandler;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.CommService;
import org.geomajas.plugin.deskmanager.client.gwt.manager.service.DataCallback;
import org.geomajas.plugin.deskmanager.command.manager.dto.GetGeodeskUrlBaseResponse;
import org.geomajas.plugin.deskmanager.domain.dto.GeodeskDto;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Window;
import com.smartgwt.client.types.TitleOrientation;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * FIXME: must be moved to Magdageo specific project.
 * 
 * @author Kristof Heirwegh 
 */
public class GeodeskHtmlCode extends VLayout implements GeodeskSelectionHandler {
	//FIXME i18n
	private static final String PRE_HTML = "<iframe src=\"";

	private static final String POST_HTML = "?type=static&i=yes\" width=\"1100\" height=\"650\" frameborder=\"0\""
			+ " scrolling=\"no\" id=\"myFrame\">\n<p>Deze widget vereist iframes.</p>\n</iframe>";

	private TextAreaItem loketPublicHtml;

	private TextAreaItem loketLOHtml;

	private TextAreaItem loketVOHtml;

	private TextItem loketPublicUrl;

	private TextItem loketLOUrl;

	private TextItem loketVOUrl;

	private String loketUrlBaseVO;

	private String loketUrlBaseLO;

	private String loketUrlBasePublic;

	private boolean initialized;

	public GeodeskHtmlCode() {
		setMembersMargin(5);

		DynamicForm formA = new DynamicForm();
		formA.setPadding(5);
		formA.setIsGroup(true);
		formA.setGroupTitle("Link naar het loket.");
		formA.setWidth100();
		formA.setAutoHeight();
		formA.setTitleOrientation(TitleOrientation.TOP);

		loketPublicUrl = new TextItem();
		loketPublicUrl.setTitle("<b>Url</b>");
		loketPublicUrl.setWidth("*");
		loketPublicUrl.setAttribute("readOnly", true);

		loketVOUrl = new TextItem();
		loketVOUrl.setTitle("<b>Url Vlaamse Overheid</b>");
		loketVOUrl.setWidth("*");
		loketVOUrl.setAttribute("readOnly", true);

		loketLOUrl = new TextItem();
		loketLOUrl.setTitle("<b>Url Lokale Overheid</b>");
		loketLOUrl.setWidth("*");
		loketLOUrl.setAttribute("readOnly", true);

		formA.setFields(loketPublicUrl, loketVOUrl, loketLOUrl);
		formA.setNumCols(1);
		formA.setColWidths("*");

		// --

		DynamicForm formB = new DynamicForm();
		formB.setPadding(5);
		formB.setIsGroup(true);
		formB.setGroupTitle("Plaatst deze code in de <b>&lt;body&gt;</b> van uw pagina om het loket toe te voegen.");
		formB.setWidth100();
		formB.setHeight("*");
		formB.setTitleOrientation(TitleOrientation.TOP);

		loketPublicHtml = new TextAreaItem();
		loketPublicHtml.setValue("");
		loketPublicHtml.setTitle("<b>Geodesk</b>");
		loketPublicHtml.setHeight(75);
		loketPublicHtml.setWidth("*");
		loketPublicHtml.setAttribute("readOnly", true);
		// loketPublicHtml.hide();

		loketVOHtml = new TextAreaItem();
		loketVOHtml.setValue("");
		loketVOHtml.setTitle("<b>Geodesk Vlaamse Overheid</b>");
		loketVOHtml.setHeight(75);
		loketVOHtml.setWidth("*");
		loketVOHtml.setAttribute("readOnly", true);
		// loketVOHtml.hide();

		loketLOHtml = new TextAreaItem();
		loketLOHtml.setValue("");
		loketLOHtml.setTitle("<b>Geodesk Lokale Overheid</b>");
		loketLOHtml.setHeight(75);
		loketLOHtml.setWidth("*");
		loketLOHtml.setAttribute("readOnly", true);
		// loketLOHtml.hide();

		formB.setFields(loketPublicHtml, loketVOHtml, loketLOHtml);
		formB.setNumCols(1);
		formB.setColWidths("*");

		// --

		addMember(formA);
		addMember(formB);

		CommService.getGeodeskUrlBase(new DataCallback<GetGeodeskUrlBaseResponse>() {

			public void execute(GetGeodeskUrlBaseResponse result) {
				loketUrlBasePublic = (result.getLoketUrlBasePublic() == null ? Window.Location.getProtocol() + "//"
						+ Window.Location.getHost() : result.getLoketUrlBasePublic());
				loketUrlBaseVO = (result.getLoketUrlBaseVO() == null ? loketUrlBasePublic : result.getLoketUrlBaseVO());
				loketUrlBaseLO = (result.getLoketUrlBaseLO() == null ? loketUrlBasePublic : result.getLoketUrlBaseLO());
				initialized = true;
			}
		});
	}

	public void onGeodeskSelectionChange(GeodeskEvent geodeskEvent) {
		setGeodesk(geodeskEvent.getGeodesk());
	}
	
	private void setGeodesk(final GeodeskDto geodesk) {
		if (initialized) {
			if (geodesk == null) {
				showPublicFields();
				loketPublicHtml.setValue("");
				loketPublicUrl.setValue("");
			} else {
				String geodeskId = geodesk.getGeodeskId();
				if (geodesk.isPublic()) {
					showPublicFields();
					loketPublicUrl.setValue(createUrl(loketUrlBasePublic, geodeskId));
					loketPublicHtml.setValue(createHtml(loketUrlBasePublic, geodeskId));
				} else {
					showPrivateFields();
					loketVOUrl.setValue(createUrl(loketUrlBaseVO, geodeskId));
					loketLOUrl.setValue(createUrl(loketUrlBaseLO, geodeskId));
					loketVOHtml.setValue(createHtml(loketUrlBaseVO, geodeskId));
					loketLOHtml.setValue(createHtml(loketUrlBaseLO, geodeskId));
				}
			}
		} else {
			GWT.runAsync(new RunAsyncCallback() {

				public void onSuccess() {
					setGeodesk(geodesk);
				}

				public void onFailure(Throwable reason) {
				}
			});
		}
	}

	public static String createHtml(String baseUrl, String loketId) {
		return PRE_HTML + createUrl(baseUrl, loketId) + POST_HTML;
	}

	public static String createUrl(String baseUrl, String loketId) {
		//FIXME: get from geodeskUrlBaseCommand
		return baseUrl + CommonLayout.GEODESK_PREFIX + loketId + "/" + CodeServer.getCodeServer();
	}

	public static String createPreviewUrl(String loketId) {
		//FIXME: get from geodeskUrlBaseCommand
		return createUrl(GWT.getModuleBaseURL() + "../", loketId);
	}
	
	// -------------------------------------------------

	private void showPublicFields() {
		loketVOUrl.hide();
		loketLOUrl.hide();
		loketPublicUrl.show();

		loketVOHtml.hide();
		loketLOHtml.hide();
		loketPublicHtml.show();
	}

	private void showPrivateFields() {
		loketPublicUrl.hide();
		loketVOUrl.show();
		loketLOUrl.show();

		loketPublicHtml.hide();
		loketVOHtml.show();
		loketLOHtml.show();
	}
}
