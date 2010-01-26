/*
 * This file is part of Geomajas, a component framework for building
 * rich Internet applications (RIA) with sophisticated capabilities for the
 * display, analysis and management of geographic information.
 * It is a building block that allows developers to add maps
 * and other geographic data capabilities to their web applications.
 *
 * Copyright 2008-2010 Geosparc, http://www.geosparc.com, Belgium
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.geomajas.global;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * General exception thrown by Geomajas when problems occur.
 * This allows specifying exception codes through which the (translated) exception message is found.
 * The message can contain extra information which can be passed separately.
 * The pattern "${0}" is replaced by the first extra string etc. You can also use the pattern "$${0}" which will
 * attempt to translate the parameter as well.
 *
 * @author Joachim Van der Auwera
 */
public class GeomajasException extends Exception {

	private static final long serialVersionUID = 6523420918533106345L;
	private int exceptionCode;
	private Object[] msgParameters;
	
	private static final String RESOURCE_BUNDLE_NAME = "org.geomajas.global.GeomajasException";

	public GeomajasException() {
	}

	public GeomajasException(Throwable ex) {
		super(ex);
	}

	public GeomajasException(Throwable ex, int exceptionCode, Object... parameters) {
		super(ex);
		this.exceptionCode = exceptionCode;
		msgParameters = parameters;

	}

	public GeomajasException(Throwable ex, int exceptionCode) {
		super(ex);
		this.exceptionCode = exceptionCode;
	}

	public GeomajasException(int exceptionCode, Object... parameters) {
		this.exceptionCode = exceptionCode;
		msgParameters = parameters;
	}

	public GeomajasException(int exceptionCode) {
		this.exceptionCode = exceptionCode;
	}

	private String translate(Object messageObject, Locale locale) {
		if (null == messageObject) {
			return null;
		}
		String message = messageObject.toString();
		ResourceBundle bundleEn = ResourceBundle.getBundle(getResourceBundleName(), Locale.ENGLISH);
		ResourceBundle bundle;
		try {
			bundle = ResourceBundle.getBundle(getResourceBundleName(), locale);
		} catch (MissingResourceException mre) {
			bundle = bundleEn;
		}
		Object obj;
		try {
			obj = bundle.getObject(message);
		} catch (MissingResourceException mre) {
			obj = bundleEn.getObject(message);
		}
		if (null != obj && obj instanceof String) {
			return (String) obj;
		}
		return message; // fallback when no translation
	}

	public String getMessage() {
		return getMessage(Locale.getDefault());
	}

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
					message = message + " (" + rs + ")";
				}
			}
		}
		return message;
	}

	public int getExceptionCode() {
		return exceptionCode;
	}

	public String getResourceBundleName() {
		return RESOURCE_BUNDLE_NAME;
	}
}
