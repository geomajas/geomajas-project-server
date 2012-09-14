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
package org.geomajas.plugin.deskmanager.client.gwt.geodesk.widget.infowindow;

import java.util.LinkedList;
import java.util.List;

import org.geomajas.annotation.FutureApi;
import org.geomajas.command.CommandResponse;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.plugin.deskmanager.client.gwt.geodesk.GeodeskLayout;
import org.geomajas.widget.featureinfo.client.widget.NotificationHandler;
import org.geomajas.widget.featureinfo.client.widget.Notify;

import com.google.gwt.user.client.Timer;
import com.smartgwt.client.types.AnimationEffect;
import com.smartgwt.client.types.Positioning;
import com.smartgwt.client.widgets.AnimationCallback;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.HTMLFlow;

/**
 * Shows an error message as a croc eye, visible for an amount of time. The position of the notification can be set
 * unsing {@link GeodeskLayout}.
 * 
 * @author Jan De Moerloose
 *
 */
@FutureApi
public final class NotificationWindow extends AbstractCommandCallback<CommandResponse> {

	private static final int TIMEOUT = 10000; // millisec

	private static final String INFOMESSAGE_STYLE = "infoMessage";

	private static final String ERRORMESSAGE_STYLE = "errorMessage";

	private static NotificationWindow instance;

	private NotificationWindow() {
		Notify.getInstance().setHandler(new NotificationHandler() {

			public void handleInfo(String message) {
				showInfoMessage(message);
			}

			public void handleError(String message) {
				showErrorMessage(message);
			}
		});
	}

	public static NotificationWindow getInstance() {
		if (instance == null) {
			instance = new NotificationWindow();
		}
		return instance;
	}

	public static void showInfoMessage(String message) {
		getInstance().addMessage(message, INFOMESSAGE_STYLE);
	}

	public static void showErrorMessage(String message) {
		getInstance().addMessage(message, ERRORMESSAGE_STYLE);
	}

	public static void clearMessages() {
		getInstance().hideInfoPanel();
	}

	// ----------------------------------------------------------

	public void onCommunicationException(Throwable error) {
		addMessage("Er is een communicatiefout opgetreden: " + error.getMessage(), ERRORMESSAGE_STYLE);
	}

	public void onCommandException(CommandResponse response) {
		String message = "";
		List<String> msgs = response.getErrorMessages();
		for (int i = 0; i < msgs.size(); i++) {
			if (i == 0) {
				message += msgs.get(i);
			} else {
				message += "- " + msgs.get(i);
			}
			if (i < msgs.size() - 1) {
				message += "<br />";
			}
		}
		addMessage(message, ERRORMESSAGE_STYLE);
	}

	// ----------------------------------------------------------

	private int timeout = TIMEOUT;

	private int width = 200;

	private int maxItems = 5;

	private Canvas parent;

	private HTMLFlow infoPanel;

	private List<String> messages = new LinkedList<String>();

	private void addMessage(String message, String style) {
		messages.add("<div class=\"" + style + "\">" + message + "</div>"); // SafeHtmlUtils.htmlEscape(message)
		if (messages.size() > maxItems) {
			messages.remove(0);
		}
		showInfoPanel(buildHtml());
		new Timer() {

			public void run() {
				if (messages.size() > 0) {
					messages.remove(0);
				}
				if (messages.size() > 0) {
					showInfoPanel(buildHtml());
				} else {
					hideInfoPanel();
				}
			}
		} .schedule(timeout);
	}

	private String buildHtml() {
		String html = "";
		for (int i = 0; i < messages.size(); i++) {
			html += messages.get(i);
			if (i < (messages.size() - 1)) {
				html += "<div class=\"infoWindowBreak\"></div>";
			}
		}
		return html;
	}

	private void showInfoPanel(String contents) {
		if (infoPanel == null) {
			int left = GeodeskLayout.crocEyePositionLeft;
			int top = GeodeskLayout.crocEyePositionTop;

			infoPanel = new HTMLFlow(contents);
			infoPanel.setWidth(width);
			infoPanel.setAutoHeight();
			if (parent != null) {
				infoPanel.setParentElement(parent);
			}
			infoPanel.setPosition(Positioning.ABSOLUTE);
			infoPanel.setShowEdges(true);
			infoPanel.setEdgeSize(1);
			infoPanel.setPadding(3);
			infoPanel.setTop(top);
			infoPanel.setLeft(left);
			infoPanel.setBackgroundColor("#FFFFFF");
			infoPanel.bringToFront();
			infoPanel.setZIndex(2000000);
			infoPanel.setKeepInParentRect(true);

			infoPanel.setAnimateTime(500);
			infoPanel.animateShow(AnimationEffect.FADE);
		} else {
			infoPanel.setContents(contents);
		}
		infoPanel.bringToFront();
		infoPanel.setZIndex(2000000);
	}

	private void hideInfoPanel() {
		if (infoPanel != null) {
			infoPanel.animateFade(0, new AnimationCallback() {

				public void execute(boolean earlyFinish) {
					HTMLFlow temp = infoPanel;
					infoPanel = null;
					temp.destroy();
				}
			});
		}
	}

	// ----------------------------------------------------------

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public void init(Canvas parent) {
		this.parent = parent;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getMaxItems() {
		return maxItems;
	}

	public void setMaxItems(int maxItems) {
		this.maxItems = maxItems;
	}

	public void execute(CommandResponse response) {
	}
}
