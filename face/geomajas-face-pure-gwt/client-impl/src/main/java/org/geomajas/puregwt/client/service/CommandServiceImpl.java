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
package org.geomajas.puregwt.client.service;

import org.geomajas.command.CommandResponse;
import org.geomajas.global.ExceptionDto;
import org.geomajas.gwt.client.command.CommandCallback;
import org.geomajas.gwt.client.command.CommandExceptionCallback;
import org.geomajas.gwt.client.command.CommunicationExceptionCallback;
import org.geomajas.gwt.client.command.Deferred;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.gwt.client.command.GwtCommandDispatcher;
import org.geomajas.gwt.client.command.TokenRequestHandler;
import org.geomajas.gwt.client.command.event.TokenChangedHandler;
import org.geomajas.gwt.client.util.Log;
import org.geomajas.puregwt.client.i18n.GlobalMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DialogBox.Caption;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;

/**
 * Default implementation of {@link CommandService}.
 * 
 * @author Jan De Moerloose
 * @author Emiel Ackermann
 * 
 */
public class CommandServiceImpl implements CommandService {
	
	private GlobalMessages messages = GWT.create(GlobalMessages.class);

	public CommandServiceImpl() {
		CommandExceptionCallbackImpl callback = new CommandExceptionCallbackImpl();
		GwtCommandDispatcher.getInstance().setCommandExceptionCallback(callback);
		GwtCommandDispatcher.getInstance().setCommunicationExceptionCallback(callback);
	}

	public Deferred execute(GwtCommand command, CommandCallback... callback) {
		return GwtCommandDispatcher.getInstance().execute(command, callback);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setUserToken(String userToken) {
		GwtCommandDispatcher.getInstance().setUserToken(userToken);
	}

	/**
	 * {@inheritDoc}
	 */
	public String getUserToken() {
		return GwtCommandDispatcher.getInstance().getUserToken();
	}

	/**
	 * {@inheritDoc}
	 */
	public HandlerRegistration addTokenChangedHandler(TokenChangedHandler handler) {
		return GwtCommandDispatcher.getInstance().addTokenChangedHandler(handler);
	}

	/**
	 * {@inheritDoc}
	 */
	public void setTokenRequestHandler(TokenRequestHandler tokenRequestHandler) {
		GwtCommandDispatcher.getInstance().setTokenRequestHandler(tokenRequestHandler);		
	}
	
	/**
	 * Simple callback implementation that writes all exceptions to {@link Log}.
	 * 
	 * @author Emiel Ackermann
	 * 
	 */
	public class CommandExceptionCallbackImpl implements
			CommandExceptionCallback, CommunicationExceptionCallback {

		public void onCommandException(CommandResponse response) {
			String msg = null;
			String stack = null;
			boolean first = true;
			for (ExceptionDto error : response.getExceptions()) {
				if (first) {
					msg = messages.commandError() + ":\n" + error.getMessage();
					first = false;
				}
				stack = getDetails(error);
			}
			showDialog(msg, stack);
		}

		public void onCommunicationException(Throwable error) {
			if (null != error) {
				String msg = messages.commandCommunicationError() + ":\n" + error.getMessage();
				Log.logWarn(msg);
				
				StringBuilder stack = new StringBuilder();
				processStackTrace(error.getStackTrace(), stack);
				
				showDialog(msg, stack.toString());
			}
		}
		
		/**
		 * Build details message for an exception.
		 *
		 * @param error error to build message for
		 * @return string with details message
		 */
		private String getDetails(ExceptionDto error) {
			if (null == error) {
				return "";
			}
			StringBuilder content = new StringBuilder();
			content.append(error.getClassName());
			if (error.getExceptionCode() != 0) {
				content.append(" (");
				content.append(error.getExceptionCode());
				content.append(")");
			}
			processStackTrace(error.getStackTrace(), content);
			content.append(getDetails(error.getCause()));
			return content.toString();
		}
		
		private void processStackTrace(StackTraceElement[] stackTrace, StringBuilder content) {
			for (StackTraceElement el : stackTrace) {
				content.append("  ");
				content.append(el.toString());
				content.append("\n");
			}
		}

		private void showDialog(String msg, String stack) {
			DialogCaption text = new DialogCaption(msg);
			text.setStyleName("gwt-DialogBox", true);
			text.setStyleName("Caption", true);
			text.setHeight("25");
			
			ScrollPanel details = new ScrollPanel(new Label(stack.toString()));
			details.setHeight("450");
			
			DialogBox warning = new DialogBox(true, true, text);
			warning.center();
			warning.setHeight("500");
			warning.setWidth("700");
			warning.add(details);
			warning.show();
		}

		/**
		 * Caption of an error dialog.
		 * 
		 * @author Emiel Ackermann
		 */
		public class DialogCaption extends Label implements Caption {

			public DialogCaption(String msg) {
				super(msg);
			}

			public String getHTML() {
				return null;
			}

			public void setHTML(String html) {
				
			}

			public void setHTML(SafeHtml html) {
			}
			
		}
	}
}
