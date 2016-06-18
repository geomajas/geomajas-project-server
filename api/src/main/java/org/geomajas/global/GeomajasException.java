/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2016 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.global;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.geomajas.annotation.Api;

/**
 * General exception thrown by Geomajas when problems occur.
 * This allows specifying exception codes through which the (translated) exception message is found.
 * The message can contain extra information which can be passed separately.
 * The pattern "${0}" is replaced by the first extra string etc. You can also use the pattern "$${0}" which will
 * attempt to translate the parameter as well.
 *
 * @author Joachim Van der Auwera
 * @since 1.6.0
 */
@Api(allMethods = true)
public class GeomajasException extends Exception {

	private static final long serialVersionUID = 6523420918533106345L;
	private int exceptionCode;
	private transient Object[] msgParameters; // transient to assure GeomajasException is Serializable
	
	private static final String RESOURCE_BUNDLE_NAME = "org.geomajas.global.GeomajasException";

	/**
	 * Create new GeomajasException.
	 */
	public GeomajasException() {
		super();
	}

	/**
	 * Create new GeomajasException.
	 *
	 * @param ex cause exception
	 */
	public GeomajasException(Throwable ex) {
		super(ex);
	}

	/**
	 * Create new GeomajasException.
	 *
	 * @param ex cause exception
	 * @param exceptionCode code which points to the message
	 * @param parameters possible extra parameters
	 */
	public GeomajasException(Throwable ex, int exceptionCode, Object... parameters) {
		super(ex);
		this.exceptionCode = exceptionCode;
		msgParameters = parameters;

	}

	/**
	 * Create new GeomajasException.
	 *
	 * @param ex cause exception
	 * @param exceptionCode code which points to the message
	 */
	public GeomajasException(Throwable ex, int exceptionCode) {
		super(ex);
		this.exceptionCode = exceptionCode;
	}

	/**
	 * Create new GeomajasException.
	 *
	 * @param exceptionCode code which points to the message
	 * @param parameters possible extra parameters
	 */
	public GeomajasException(int exceptionCode, Object... parameters) {
		this.exceptionCode = exceptionCode;
		msgParameters = parameters;
	}

	/**
	 * Create new GeomajasException.
	 *
	 * @param exceptionCode code which points to the message
	 */
	public GeomajasException(int exceptionCode) {
		this.exceptionCode = exceptionCode;
	}

	private String translate(Object messageObject, Locale locale) {
		if (null == messageObject) {
			return null;
		}
		String message = messageObject.toString();
		ResourceBundle bundleEn = ResourceBundle.getBundle(getResourceBundleName(), Locale.ENGLISH);
		ResourceBundle bundle = bundleEn;
		if (null != locale) {
			try {

				bundle = ResourceBundle.getBundle(getResourceBundleName(), locale);
			} catch (MissingResourceException mre) {
				// ignore, bundle is already set to English
			}
		}
		Object obj;
		try {
			obj = bundle.getObject(message);
		} catch (MissingResourceException mre) {
			try {
				obj = bundleEn.getObject(message);
			} catch (MissingResourceException mre2) {
				obj = message; // still can't find, use kay as translation
			}
		}
		if (null != obj && obj instanceof String) {
			return (String) obj;
		}
		return message; // fallback when no translation
	}

	/**
	 * Get message using the default locale.
	 *
	 * @return exception message
	 */
	public String getMessage() {
		return getMessage(Locale.getDefault());
	}

	/**
	 * Get the exception message using the requested locale.
	 *
	 * @param locale locale for message
	 * @return exception message
	 */
	public String getMessage(Locale locale) {
		if (getCause() != null) {
			String message = getShortMessage(locale) + ", " + translate("ROOT_CAUSE", locale) + " ";
			if (getCause() instanceof GeomajasException) {
				return message + ((GeomajasException) getCause()).getMessage(locale);
			}
			return message + getCause().getMessage();
		} else {
			return getShortMessage(locale);
		}
	}

	/**
	 * Get the short exception message using the requested locale. This does not include the cause exception message.
	 *
	 * @param locale locale for message
	 * @return (short) exception message
	 */
	public String getShortMessage(Locale locale) {
		String message;
		message = translate(Integer.toString(exceptionCode), locale);
		if (message != null && msgParameters != null && msgParameters.length > 0) {
			for (int i = 0; i < msgParameters.length; i++) {
				boolean isIncluded = false;
				String needTranslationParam = "$${" + i + "}";
				if (message.contains(needTranslationParam)) {
					String translation = translate(msgParameters[i], locale);
					if (null == translation && null != msgParameters[i]) {
						translation = msgParameters[i].toString();
					}
					if (null == translation) {
						translation = "[null]";
					}
					message = message.replace(needTranslationParam, translation);
					isIncluded = true;
				}
				String verbatimParam = "${" + i + "}";
				String rs = null == msgParameters[i] ? "[null]" : msgParameters[i].toString();
				if (message.contains(verbatimParam)) {
					message = message.replace(verbatimParam, rs);
					isIncluded = true;
				}
				if (!isIncluded) {
					message = message + " (" + rs + ")"; // NOSONAR replace/contains makes StringBuilder use difficult
				}
			}
		}
		return message;
	}

	/**
	 * Get the exception code for the problem.
	 *
	 * @return exception code
	 */
	public int getExceptionCode() {
		return exceptionCode;
	}

	/**
	 * Resource bundle to be used for translating the exception code into exception messages.
	 *
	 * @return resource bundle name
	 */
	public String getResourceBundleName() {
		return RESOURCE_BUNDLE_NAME;
	}
}
