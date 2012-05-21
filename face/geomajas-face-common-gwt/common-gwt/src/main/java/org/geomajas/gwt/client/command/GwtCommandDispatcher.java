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

package org.geomajas.gwt.client.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geomajas.annotation.Api;
import org.geomajas.command.CommandResponse;
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
import org.geomajas.gwt.client.command.event.TokenChangedEvent;
import org.geomajas.gwt.client.command.event.TokenChangedHandler;
import org.geomajas.gwt.client.util.EqualsUtil;
import org.geomajas.gwt.client.util.Log;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.Window.ClosingHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.smartgwt.client.core.Function;

/**
 * The central client side dispatcher for all commands. Use the {@link #execute(GwtCommand, CommandCallback...)}
 * function to execute an asynchronous command on the server.
 * <p/>
 * Set a {@link TokenRequestHandler} to make sure the class automatically makes the user (re) login when needed.
 * 
 * @author Pieter De Graef
 * @author Oliver May
 * @author Joachim Van der Auwera
 * @since 0.0.0
 */
@Api(allMethods = true)
public final class GwtCommandDispatcher implements HasDispatchHandlers, CommandExceptionCallback,
		CommunicationExceptionCallback {

	private static final String SECURITY_EXCEPTION_CLASS_NAME = "org.geomajas.security.GeomajasSecurityException";

	private static final String RANDOM_UNLIKELY_TOKEN = "%t@kén§#";

	private static GwtCommandDispatcher instance = new GwtCommandDispatcher();

	private final GeomajasServiceAsync service;

	private final HandlerManager manager = new HandlerManager(this);

	private final List<Deferred> deferreds;

	private int nrOfDispatchedCommands;

	private String locale;

	private String userToken;

	private UserDetail userDetail = new UserDetail();

	private boolean useLazyLoading;

	private int lazyFeatureIncludesDefault;

	private int lazyFeatureIncludesSelect;

	private int lazyFeatureIncludesAll;

	private boolean showError;

	private TokenRequestHandler tokenRequestHandler;

	private CommandExceptionCallback commandExceptionCallback;

	private CommunicationExceptionCallback communicationExceptionCallback;

	// map is not synchronized as this class runs in JavaScript which only has one execution thread
	private final Map<String, List<RetryCommand>> afterLoginCommands = new HashMap<String, List<RetryCommand>>();

	private GwtCommandDispatcher() {
		locale = LocaleInfo.getCurrentLocale().getLocaleName();
		if ("default".equals(locale)) {
			locale = null;
		}
		deferreds = new ArrayList<Deferred>();
		service = (GeomajasServiceAsync) GWT.create(GeomajasService.class);
		setServiceEndPointUrl(GWT.getModuleBaseURL() + "geomajasService");
		setUseLazyLoading(true);
		setShowError(true);

		Window.addWindowClosingHandler(new ClosingHandler() {

			public void onWindowClosing(ClosingEvent event) {
				GwtCommandDispatcher.getInstance().setShowError(false);

				// Cancel all outstanding requests:
				for (Deferred deferred : deferreds) {
					deferred.cancel();
				}
			}
		});
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
		return instance;
	}

	/** {@inheritDoc} */
	public HandlerRegistration addDispatchStartedHandler(DispatchStartedHandler handler) {
		return manager.addHandler(DispatchStartedHandler.TYPE, handler);
	}

	/** {@inheritDoc} */
	public HandlerRegistration addDispatchStoppedHandler(DispatchStoppedHandler handler) {
		return manager.addHandler(DispatchStoppedHandler.TYPE, handler);
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
	 * @param command
	 *            The command to be executed. This command is a wrapper around the actual request object.
	 * @param deferred
	 *            list of callbacks for the command
	 * @return original deferred object as passed as parameter
	 * @since 1.0.0
	 */
	public Deferred execute(final GwtCommand command, final Deferred deferred) {
		if (!deferreds.contains(deferred)) {
			deferreds.add(deferred);
		}

		command.setLocale(locale);
		command.setUserToken(userToken);

		// shortcut, no need to invoke the server if we know the token has expired
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
					if (!errorHandled && deferred.isLogCommunicationExceptions()) {
						onCommunicationException(error);
					}
				} catch (Throwable t) {
					if (deferred.isLogCommunicationExceptions()) {
						Log.logError("Command failed on error callback", t);
					}
				} finally {
					decrementDispatched();
					deferreds.remove(deferred);
				}
			}

			public void onSuccess(CommandResponse response) {
				try {
					if (response.isError()) {
						// first check for authentication problems, these are handled separately
						boolean authenticationFailed = false;
						for (ExceptionDto exception : response.getExceptions()) {
							authenticationFailed |= SECURITY_EXCEPTION_CLASS_NAME.equals(exception.getClassName())
									&& (ExceptionCode.CREDENTIALS_MISSING_OR_INVALID == exception.getExceptionCode() 
									|| command.getUserToken() == null);
						}
						if (authenticationFailed && null != tokenRequestHandler) {
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
					Log.logError("Command failed on success callback", t);
				} finally {
					decrementDispatched();
					deferreds.remove(deferred);
				}
			}
		});
		return deferred;
	}

	/**
	 * Add a command and it's callbacks to the list of commands to retry after login.
	 * 
	 * @param command
	 *            command to retry
	 * @param deferred
	 *            callbacks for the command
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
	 * @param command
	 *            command which needs to be retried
	 * @param deferred
	 *            callbacks for the command
	 */
	private void handleLogin(GwtCommand command, Deferred deferred) {
		final String oldToken = notNull(command.getUserToken());
		if (!afterLoginCommands.containsKey(oldToken)) {
			afterLoginCommands.put(oldToken, new ArrayList<RetryCommand>());
			login(oldToken);
		}
		afterLogin(command, deferred);
	}

	/**
	 * Assure that a string is not null, convert to empty string if it is.
	 * 
	 * @param string
	 *            string to convert
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
	 * @param error
	 *            error to report
	 * @since 0.0.0
	 */
	public void onCommunicationException(Throwable error) {
		if (isShowError()) {
			communicationExceptionCallback.onCommunicationException(error);
		}
	}

	/**
	 * Default behaviour for handling a command execution exception. Shows an exception report to the user.
	 * 
	 * @param response command response with error
	 * @since 0.0.0
	 */
	public void onCommandException(CommandResponse response) {
		if (isShowError()) {
			commandExceptionCallback.onCommandException(response);
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
	 * Set the service end point URL to a different value. If pointing to a different context, make sure the
	 * GeomajasController of that context supports this.
	 * 
	 * @see org.geomajas.gwt.server.mvc.GeomajasController
	 * 
	 * @param url the new URL
	 * @since 1.0.0
	 */
	public void setServiceEndPointUrl(String url) {
		ServiceDefTarget endpoint = (ServiceDefTarget) service;
		endpoint.setServiceEntryPoint(url);
	}

	/**
	 * Force request a new user token. This is not used for extending the user token, that is handled automatically.
	 *
	 * @since 1.1.0
	 */
	public void login() {
		logout();
		login(RANDOM_UNLIKELY_TOKEN);
	}

	/**
	 * Force request a new login, the dangling commands for the previous token are retried when logged in.
	 *
	 * @param oldToken previous token
	 */
	private void login(final String oldToken) {
		tokenRequestHandler.login(new TokenChangedHandler() {

			public void onTokenChanged(TokenChangedEvent event) {
				setToken(event.getToken(), event.getUserDetail());
				List<RetryCommand> retryCommands = afterLoginCommands.remove(oldToken);
				if (null != retryCommands) {
					for (RetryCommand retryCommand : retryCommands) {
						execute(retryCommand.getCommand(), retryCommand.getDeferred());
					}
				}
			}
		});
	}
	/**
	 * Logout. Clear the user token.
	 *
	 * @since 1.1.0
	 */
	public void logout() {
		setToken(null, null);
	}

	/**
	 * Set the user token, so it can be included in every command.
	 * 
	 * @param userToken user token
	 * @deprecated use {@link #login()} or {@link #logout()}
	 */
	@Deprecated
	public void setUserToken(String userToken) {
		setToken(userToken, null);
	}

	/**
	 * Set the user token, so it can be sent in every command.
	 * 
	 * @param userToken user token
	 * @param userDetail user details
	 * @since 1.0.0
	 * @deprecated use {@link #login()} or {@link #logout()}
	 */
	@Deprecated
	public void setUserToken(String userToken, UserDetail userDetail) {
		setToken(userToken, userDetail);
	}

	/**
	 * Set the user token, so it can be sent in every command.
	 * This is the internal version, used by the token changed handler.
	 *
	 * @param userToken user token
	 * @param userDetail user details
	 */
	private void setToken(String userToken, UserDetail userDetail) {
		boolean changed = !EqualsUtil.isEqual(this.userToken, userToken);
		this.userToken = userToken;
		if (null == userDetail) {
			userDetail = new UserDetail();
		}
		this.userDetail = userDetail;
		if (changed) {
			TokenChangedEvent event = new TokenChangedEvent(userToken, userDetail);
			manager.fireEvent(event);
		}
	}

	/**
	 * Get currently active user authentication token.
	 * 
	 * @return authentication token
	 * @since 1.0.0
	 */
	public String getUserToken() {
		return userToken;
	}

	/**
	 * Get details for the current user.
	 * <p/>
	 * Object is always not-null, but the entries mey be.
	 * 
	 * @return user details object
	 * @since 1.0.0
	 */
	public UserDetail getUserDetail() {
		return userDetail;
	}

	/**
	 * Add handler which is notified when the user token changes.
	 * 
	 * @param handler token changed handler
	 * @return handler registration
	 * @since 1.0.0
	 */
	public HandlerRegistration addTokenChangedHandler(TokenChangedHandler handler) {
		return manager.addHandler(TokenChangedHandler.TYPE, handler);
	}

	/**
	 * Set the login handler which should be used to request aan authentication token.
	 * 
	 * @param tokenRequestHandler login handler
	 * @since 1.0.0
	 */
	public void setTokenRequestHandler(TokenRequestHandler tokenRequestHandler) {
		this.tokenRequestHandler = tokenRequestHandler;
	}

	/**
	 * Get the current token request handler.
	 *
	 * @return token request handler
	 * @since 1.1.0
	 */
	public TokenRequestHandler getTokenRequestHandler() {
		return tokenRequestHandler;
	}

	/**
	 * Set default command exception callback.
	 * 
	 * @param commandExceptionCallback command exception callback
	 * @since 1.0.0
	 */
	public void setCommandExceptionCallback(CommandExceptionCallback commandExceptionCallback) {
		this.commandExceptionCallback = commandExceptionCallback;
	}

	/**
	 * Set default communication exception callback.
	 * 
	 * @param communicationExceptionCallback communication exception callback
	 * @since 1.0.0
	 */
	public void setCommunicationExceptionCallback(CommunicationExceptionCallback communicationExceptionCallback) {
		this.communicationExceptionCallback = communicationExceptionCallback;
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
	 * @param useLazyLoading lazy feature loading status
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
	 * @param lazyFeatureIncludesDefault default for "featureIncludes"
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
	 * @param lazyFeatureIncludesSelect default "featureIncludes" for select commands
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
	 * @param lazyFeatureIncludesAll "featureIncludes" value when all should be included
	 */
	public void setLazyFeatureIncludesAll(int lazyFeatureIncludesAll) {
		setUseLazyLoading(false);
		this.lazyFeatureIncludesAll = lazyFeatureIncludesAll;
	}

	/**
	 * Should the dispatcher show error messages ?
	 * 
	 * @return true if showing error messages, false otherwise
	 * @since 0.0.0
	 */
	public boolean isShowError() {
		return showError;
	}

	/**
	 * Sets whether the dispatcher should show error messages.
	 * 
	 * @param showError true if showing error messages, false otherwise
	 * @since 0.0.0
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
	private static class RetryCommand {

		private final GwtCommand command;

		private final Deferred deferred;

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
