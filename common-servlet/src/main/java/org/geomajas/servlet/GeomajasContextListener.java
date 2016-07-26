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
package org.geomajas.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;

import org.geomajas.global.GeomajasConstant;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.XmlWebApplicationContext;

/**
 * Note: This class is no longer needed, you can use the standard Spring ContextLoaderListener with the following 
 * configLocation entry:
 * <p><code>classpath:org/geomajas/spring/geomajasContext.xml</code><p>
 * 
 * 
 * Initialises the servlet context. This assures the Spring application context is created and stored in the servlet
 * context. The Geomajas configuration is also read and also stored in the servlet context.
 * <p/>
 * The spring application context can be extended by adding locations (on the classpath) where addition configuration
 * files are found. They should be defined in the "contextConfigLocation" context-param to the context instance and the
 * entry can contain multiple files, separated by commas or whitespace.
 * <p/>
 * Note: In case of multiple config locations, later bean definitions will override ones defined in earlier loaded
 * files. This can be leveraged to deliberately override certain bean definitions via an extra XML file.
 * 
 *
 * @author Joachim Van der Auwera
 * @deprecated use {@link org.springframework.web.context.ContextLoaderListener}
 * 
 */
@Deprecated
public class GeomajasContextListener implements ServletContextListener, ServletRequestListener {

	// private final Logger log = LoggerFactory.getLogger(GeomajasContextListener.class);

	/** Name of servlet context parameter that can specify additional config locations for the spring context. */
	public static final String CONFIG_LOCATION_PARAMETER = "contextConfigLocation";

	private static final String REQUEST_ATTRIBUTES_ATTRIBUTE =
			RequestContextListener.class.getName() + ".REQUEST_ATTRIBUTES";

	@edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "REC_CATCH_EXCEPTION")
	public void contextInitialized(ServletContextEvent servletContextEvent) {

		ServletContext servletContext = servletContextEvent.getServletContext();

		StringBuilder configLocation = new StringBuilder();
		configLocation.append("classpath:org/geomajas/spring/geomajasContext.xml");
		String additionalLocations = servletContext.getInitParameter(CONFIG_LOCATION_PARAMETER);
		if (null != additionalLocations) {
			for (String onePart : additionalLocations.split("\\s")) {
				String part = onePart.trim();
				if (part.length() > 0) {
					configLocation.append(',');
					int pos = part.indexOf(':');
					if (pos < 0) {
						// no protocol specified, use classpath.
						configLocation.append(GeomajasConstant.CLASSPATH_URL_PREFIX);
					} else if (0 == pos) {
						// location starts with colon, use default application context 
						part = part.substring(1);
					}
					configLocation.append(part);
				}
			}
		}
		ConfigurableWebApplicationContext applicationContext = new XmlWebApplicationContext();

		// Assign the best possible id value.
		String id;
		try {
			String contextPath = (String) ServletContext.class.getMethod("getContextPath").invoke(servletContext);
			id = ObjectUtils.getDisplayString(contextPath);
		} catch (Exception ex) {
			// Servlet <= 2.4: resort to name specified in web.xml, if any.
			String servletContextName = servletContext.getServletContextName();
			id = ObjectUtils.getDisplayString(servletContextName);			
		}

		applicationContext.setId("Geomajas:" + id);
		applicationContext.setServletContext(servletContext);
		applicationContext.setConfigLocation(configLocation.toString());
		applicationContext.refresh();

		ApplicationContextUtil.setApplicationContext(servletContext, applicationContext);
		servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, applicationContext);
	}

	/**
	 * Close the root web application context.
	 *
	 * @param servletContextEvent servlet context event
	 */
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		// nothing to do
	}

	public void requestInitialized(ServletRequestEvent servletRequestEvent) {
		if (servletRequestEvent.getServletRequest() instanceof HttpServletRequest) {
			HttpServletRequest request = (HttpServletRequest) servletRequestEvent.getServletRequest();
			ServletRequestAttributes attributes = new ServletRequestAttributes(request);
			request.setAttribute(REQUEST_ATTRIBUTES_ATTRIBUTE, attributes);
			LocaleContextHolder.setLocale(request.getLocale());
			RequestContextHolder.setRequestAttributes(attributes);
		}
	}

	public void requestDestroyed(ServletRequestEvent servletRequestEvent) {
		ServletRequestAttributes attributes = (ServletRequestAttributes) servletRequestEvent.getServletRequest()
				.getAttribute(REQUEST_ATTRIBUTES_ATTRIBUTE);
		ServletRequestAttributes threadAttributes =
				(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (threadAttributes != null) {
			if (attributes == null) {
				attributes = threadAttributes;
			}
			RequestContextHolder.setRequestAttributes(null);
			LocaleContextHolder.setLocale(null);
		}
		if (attributes != null) {
			attributes.requestCompleted();
		}
	}

}
