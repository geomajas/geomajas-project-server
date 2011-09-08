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

package org.geomajas.gwt.client.command;

import org.geomajas.command.CommandResponse;
import org.geomajas.annotation.Api;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.ExceptionDto;
import org.geomajas.global.GeomajasConstant;
import org.geomajas.gwt.client.GeomajasService;
import org.geomajas.gwt.client.GeomajasServiceAsync;
import org.geomajas.gwt.client.command.event.DispatchStartedEvent;
import org.geomajas.gwt.client.command.event.DispatchStartedHandler;
import org.geomajas.gwt.client.command.event.DispatchStoppedEvent;
import org.geomajas.gwt.client.command.event.DispatchStoppedHandler;
import org.geomajas.gwt.client.command.event.HasDispatchHandlers;
import org.geomajas.gwt.client.i18n.I18nProvider;
import org.geomajas.gwt.client.util.Log;
import org.geomajas.gwt.client.widget.ExceptionWindow;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.smartgwt.client.core.Function;
import com.smartgwt.client.util.SC;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The central client side dispatcher for all commands. Use the {@link #execute(GwtCommand, CommandCallback...)}
 * function to execute an asynchronous command on the server.
 * <p/>
 * Set a {@link LoginHandler} to make sure the class automatically makes the user (re) login when needed.
 * 
 * @author Pieter De Graef
 * @author Oliver May
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public final class GwtCommandDispatcher implements HasDispatchHandlers {

	private static final String SECURITY_EXCEPTION_CLASS_NAME = "org.geomajas.security.GeomajasSecurityException";
	private static GwtCommandDispatcher instance;

	private GeomajasServiceAsync service;

	private HandlerManager manager = new HandlerManager(this);

	private int nrOfDispatchedCommands;

	private String locale;

	private String userToken;

	private boolean useLazyLoading;

	private int lazyFeatureIncludesDefault;

	private int lazyFeatureIncludesSelect;

	private int lazyFeatureIncludesAll;
	
	private boolean showError;

	private LoginHandler loginHandler;

	// map is not synchronized as this class runs in JavaScript which only has one execution thread
	private Map<String, List<RetryCommand>> afterLoginCommands = new HashMap<String, List<RetryCommand>>();

	private GwtCommandDispatcher() {
		locale = LocaleInfo.getCurrentLocale().getLocaleName();
		if ("default".equals(locale)) {
			locale = null;
		}
		service = (GeomajasServiceAsync) GWT.create(GeomajasService.class);
		ServiceDefTarget endpoint = (ServiceDefTarget) service;
		String moduleRelativeURL = GWT.getModuleBaseURL() + "geomajasService";
		endpoint.setServiceEntryPoint(moduleRelativeURL);
		setUseLazyLoading(true);
		setShowError(true);
	}

	// -------------------------------------------------------------------------
	// Public methods:
	// -------------------------------------------------------------------------

	/**
	 * Get the only static instance of this class. This should be the object you work with.
	 * 
	 * @return singleton instance
	 */
	public static GwtCommandDispatcher getInstance() {
		if (instance == null) {
			instance = new GwtCommandDispatcher();
		}
		return instance;
	}

	public HandlerRegistration addDispatchStartedHandler(DispatchStartedHandler handler) {
		return manager.addHandler(DispatchStartedEvent.getType(), handler);
	}

	public HandlerRegistration addDispatchStoppedHandler(DispatchStoppedHandler handler) {
		return manager.addHandler(DispatchStoppedEvent.getType(), handler);
	}

	/**
	 * The execution function. Executes a server side command.
	 * 
	 * @param command
	 *            The command to be executed. This command is a wrapper around the actual request object.
	 * @param callback
	 *            A <code>CommandCallback</code> function to be executed when the command successfully returns. The
	 *            callbacks may implement CommunicationExceptionCallback or CommandExceptionCallback to allow error 
	 *            handling.
	 * @return deferred object which can be used to add extra callbacks
	 */
	public Deferred execute(final GwtCommand command, final CommandCallback... callback) {
		final Deferred deferred = new Deferred();
		for (CommandCallback successCallback : callback) {
			deferred.addCallback(successCallback);
		}
		return execute(command, deferred);
	}

	/**
	 * The execution function. Executes a server side command.
	 *
	 * @param command The command to be executed. This command is a wrapper around the actual request object.
	 * @param deferred list of callbacks for the command
	 * @return original deferred object as passed as parameter
	 * @since 1.10.0
	 */
	public Deferred execute(final GwtCommand command, final Deferred deferred) {
		command.setLocale(locale);
		command.setUserToken(userToken);

		// shortcut, no need to invoke the server if whe know the token has expired
		if (null != userToken && userToken.length() > 0 && afterLoginCommands.containsKey(userToken)) {
			afterLogin(command, deferred);
			return deferred;
		}

		incrementDispatched();

		service.execute(command, new AsyncCallback<CommandResponse>() {

			public void onFailure(Throwable error) {
				try {
					for (Function callback : deferred.getErrorCallbacks()) {
						callback.execute();
					}
					boolean errorHandled = false;
					for (CommandCallback callback : deferred.getCallbacks()) {
						if (callback instanceof CommunicationExceptionCallback) {
							((CommunicationExceptionCallback) callback).onCommunicationException(error);
							errorHandled = true;
						}
					}
					if (!errorHandled) {
						onCommunicationException(error);
					}
				} catch (Throwable t) {
					String msg = "Command failed on error callback";
					GWT.log(msg, t);
					Log.logError(msg, t);
				} finally {
					decrementDispatched();
				}
			}

			public void onSuccess(CommandResponse response) {
				try {
					if (response.isError()) {
						// first check for authentication problems, these are handled separately
						boolean authenticationFailed = false;
						for (ExceptionDto exception : response.getExceptions()) {
							authenticationFailed |= SECURITY_EXCEPTION_CLASS_NAME.equals(exception.getClassName()) &&
									ExceptionCode.CREDENTIALS_MISSING_OR_INVALID == exception.getExceptionCode();
						}
						if (authenticationFailed && null != loginHandler) {
							handleLogin(command, deferred);
						} else {
							// normal error handling...

							boolean errorHandled = false;
							for (CommandCallback callback : deferred.getCallbacks()) {
								if (callback instanceof CommandExceptionCallback) {
									((CommandExceptionCallback) callback).onCommandException(response);
									errorHandled = true;
								}
							}
							// fallback to the default behaviour
							if (!errorHandled) {
								onCommandException(response);
							}
						}
					} else {
						if (!deferred.isCancelled()) {
							for (CommandCallback callback : deferred.getCallbacks()) {
								callback.execute(response);
							}
						}
					}
				} catch (Throwable t) {
					String msg = "Command failed on success callback";
					GWT.log(msg, t);
					Log.logError(msg, t);
				} finally {
					decrementDispatched();
				}
			}
		});
		return deferred;
	}

	/**
	 * Add a command and it's callbacks to the list of commands to retry after login.
	 *
	 * @param command command to retry
	 * @param deferred callbacks for the command
	 */
	private void afterLogin(GwtCommand command, Deferred deferred) {
		String token = notNull(command.getUserToken());
		if (!afterLoginCommands.containsKey(token)) {
			afterLoginCommands.put(token, new ArrayList<RetryCommand>());
		}
		afterLoginCommands.get(token).add(new RetryCommand(command, deferred));
	}

	/**
	 * Method which forces retry of a command after login.
	 * <p/>
	 * This method assumes the single threaded nature of JavaScript execution for correctness.
	 *
	 * @param command command which needs to be retried
	 * @param deferred callbacks for the command
	 */
	private void handleLogin(GwtCommand command, Deferred deferred) {
		final String oldToken = notNull(command.getUserToken());
		if (!afterLoginCommands.containsKey(oldToken)) {
			afterLoginCommands.put(oldToken, new ArrayList<RetryCommand>());
			loginHandler.login(new LoginCallback() {
				/**
				 * Login handling. @todo since declaration should be removed, needed because of bug in api checks
				 *
				 * @param token authentication token
				 * @since 1.10.0
				 */
				public void onLogin(String token) {
					setUserToken(token);
					List<RetryCommand> retryCommands = afterLoginCommands.remove(oldToken);
					for (RetryCommand retryCommand : retryCommands) {
						execute(retryCommand.getCommand(), retryCommand.getDeferred());
					}
				}
			});
		}
		afterLogin(command, deferred);
	}

	/**
	 * Assure that a string is not null, convert to empty string if it is.
	 *
	 * @param string string to convert
	 * @return converted string
	 */
	private String notNull(String string) {
		if (null == string) {
			return "";
		}
		return string;
	}

	/**
	 * Default behaviour for handling a communication exception. Shows a warning window to the user.
	 *
	 * @param error error to report
	 * @since 1.9.0
	 */
	public void onCommunicationException(Throwable error) {
		if (isShowError()) {
			String msg = I18nProvider.getGlobal().commandError() + ":\n" + error.getMessage();
			GWT.log(msg, null);
			SC.warn(msg, null);
		}
	}

	/**
	 * Default behaviour for handling a command execution exception. Shows an exception report to the user.
	 *
	 * @param response command response with error
	 * @since 1.9.0
	 */
	public void onCommandException(CommandResponse response) {
		if (isShowError()) {
			String message = I18nProvider.getGlobal().commandError() + ":";
			for (String error : response.getErrorMessages()) {
				message += "\n" + error;
			}
			GWT.log(message, null);
			if (response.getExceptions() == null || response.getExceptions().size() == 0) {
				SC.warn(message, null);
			} else {
				// The error messaging window only supports 1 exception to display:
				ExceptionWindow window = new ExceptionWindow(response.getExceptions().get(0));
				window.show();
			}
		}
	}

	/**
	 * Is the dispatcher busy ?
	 * 
	 * @return true if there are outstanding commands
	 */
	public boolean isBusy() {
		return nrOfDispatchedCommands != 0;
	}

	/**
	 * Set the user token, so it can be sent in very command.
	 * 
	 * @param userToken
	 *            user token
	 */
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

	/**
	 * Set the login handler which should be used to request aan authentication token.
	 *
	 * @param loginHandler login handler
	 * @since 1.10.0
	 */
	public void setLoginHandler(LoginHandler loginHandler) {
		this.loginHandler = loginHandler;
	}

	/**
	 * Is lazy feature loading enabled ?
	 * 
	 * @return true when lazy feature loading is enabled
	 */
	public boolean isUseLazyLoading() {
		return useLazyLoading;
	}

	/**
	 * Set lazy feature loading status.
	 * 
	 * @param useLazyLoading
	 *            lazy feature loading status
	 */
	public void setUseLazyLoading(boolean useLazyLoading) {
		if (useLazyLoading != this.useLazyLoading) {
			if (useLazyLoading) {
				lazyFeatureIncludesDefault = GeomajasConstant.FEATURE_INCLUDE_STYLE
						+ GeomajasConstant.FEATURE_INCLUDE_LABEL;
				lazyFeatureIncludesSelect = GeomajasConstant.FEATURE_INCLUDE_ALL;
				lazyFeatureIncludesAll = GeomajasConstant.FEATURE_INCLUDE_ALL;
			} else {
				lazyFeatureIncludesDefault = GeomajasConstant.FEATURE_INCLUDE_ALL;
				lazyFeatureIncludesSelect = GeomajasConstant.FEATURE_INCLUDE_ALL;
				lazyFeatureIncludesAll = GeomajasConstant.FEATURE_INCLUDE_ALL;
			}
		}
		this.useLazyLoading = useLazyLoading;
	}

	/**
	 * Get default value for "featureIncludes" when getting features.
	 * 
	 * @return default "featureIncludes" value
	 */
	public int getLazyFeatureIncludesDefault() {
		return lazyFeatureIncludesDefault;
	}

	/**
	 * Set default value for "featureIncludes" when getting features.
	 * 
	 * @param lazyFeatureIncludesDefault
	 *            default for "featureIncludes"
	 */
	public void setLazyFeatureIncludesDefault(int lazyFeatureIncludesDefault) {
		setUseLazyLoading(false);
		this.lazyFeatureIncludesDefault = lazyFeatureIncludesDefault;
	}

	/**
	 * Get "featureIncludes" to use when selecting features.
	 * 
	 * @return default "featureIncludes" for select commands
	 */
	public int getLazyFeatureIncludesSelect() {
		return lazyFeatureIncludesSelect;
	}

	/**
	 * Set default "featureIncludes" for select commands.
	 * 
	 * @param lazyFeatureIncludesSelect
	 *            default "featureIncludes" for select commands
	 */
	public void setLazyFeatureIncludesSelect(int lazyFeatureIncludesSelect) {
		setUseLazyLoading(false);
		this.lazyFeatureIncludesSelect = lazyFeatureIncludesSelect;
	}

	/**
	 * Value to use for "featureIncludes" when all should be included.
	 * 
	 * @return value for "featureIncludes" when all should be included
	 */
	public int getLazyFeatureIncludesAll() {
		return lazyFeatureIncludesAll;
	}

	/**
	 * Set "featureIncludes" value when all should be included.
	 * 
	 * @param lazyFeatureIncludesAll
	 *            "featureIncludes" value when all should be included
	 */
	public void setLazyFeatureIncludesAll(int lazyFeatureIncludesAll) {
		setUseLazyLoading(false);
		this.lazyFeatureIncludesAll = lazyFeatureIncludesAll;
	}
	
	/**
	 * Should the dispatcher show error messages ?
	 * 
	 * @return true if showing error messages, false otherwise
	 * @since 1.9.0
	 */
	public boolean isShowError() {
		return showError;
	}

	/**
	 * Sets whether the dispatcher should show error messages.
	 * 
	 * @param showError true if showing error messages, false otherwise
	 * @since 1.9.0
	 */
	public void setShowError(boolean showError) {
		this.showError = showError;
	}

	// -------------------------------------------------------------------------
	// Protected methods:
	// -------------------------------------------------------------------------

	
	protected void incrementDispatched() {
		boolean started = nrOfDispatchedCommands == 0;
		nrOfDispatchedCommands++;
		if (started) {
			manager.fireEvent(new DispatchStartedEvent());
		}
	}

	protected void decrementDispatched() {
		nrOfDispatchedCommands--;
		if (nrOfDispatchedCommands == 0) {
			manager.fireEvent(new DispatchStoppedEvent());
		}
	}

	/**
	 * Representation of a command which needs to be retried later.
	 *
	 * @author Joachim Van der Auwera
	 */
	private class RetryCommand {
		private GwtCommand command;
		private Deferred deferred;

		/**
		 * Create data to allow retying the command later.
		 *
		 * @param command command
		 * @param deferred callbacks
		 */
		public RetryCommand(GwtCommand command, Deferred deferred) {
			this.command = command;
			this.deferred = deferred;
		}

		/**
		 * Get the GwtCommand which needs to be retried.
		 *
		 * @return command
		 */
		public GwtCommand getCommand() {
			return command;
		}

		/**
		 * Get the callbacks for handling the retied command.
		 *
		 * @return callbacks
		 */
		public Deferred getDeferred() {
			return deferred;
		}
	}
}
