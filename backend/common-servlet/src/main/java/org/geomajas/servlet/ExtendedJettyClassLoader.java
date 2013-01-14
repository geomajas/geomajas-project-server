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
package org.geomajas.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;

/**
 * Wraps the class loader of Jetty. This fixes the problem of the null parent in the original class loader and the
 * partial workaround of the Google JettyLauncher (see http://gwt-code-reviews.appspot.com/496802/show). All methods are
 * delegated to the original class loader except for the scanning methods (findResources(), getResources()) which are
 * (optionally) inherited from the superclass and thus will call the parent.
 * 
 * @author Jan De Moerloose
 * 
 */
public class ExtendedJettyClassLoader extends URLClassLoader {

	private URLClassLoader delegate;

	private boolean extend;

	/**
	 * Constructs an extended class loader which delegates to the specified class loader and uses the specified parent
	 * class loader.
	 * 
	 * @param delegate
	 *            class loader to delegate to
	 * @param parent
	 *            the designated parent of this class loader
	 * @param extend
	 *            should be true if the scanning methods have to be delegated to the superclass
	 */
	public ExtendedJettyClassLoader(URLClassLoader delegate, ClassLoader parent, boolean extend) {
		super(delegate.getURLs(), parent);
		this.delegate = delegate;
		this.extend = extend;
	}

	public void clearAssertionStatus() {
		delegate.clearAssertionStatus();
	}

	public URL getResource(String name) {
		return delegate.getResource(name);
	}

	public InputStream getResourceAsStream(String name) {
		return delegate.getResourceAsStream(name);
	}

	public Enumeration<URL> getResources(String name) throws IOException {
		if (extend) {
			return super.getResources(name);
		} else {
			return delegate.getResources(name);
		}
	}

	public Class<?> loadClass(String name) throws ClassNotFoundException {
		return delegate.loadClass(name);
	}

	public void setClassAssertionStatus(String className, boolean enabled) {
		delegate.setClassAssertionStatus(className, enabled);
	}

	public void setDefaultAssertionStatus(boolean enabled) {
		delegate.setDefaultAssertionStatus(enabled);
	}

	public void setPackageAssertionStatus(String packageName, boolean enabled) {
		delegate.setPackageAssertionStatus(packageName, enabled);
	}

	public URL findResource(String name) {
		return delegate.findResource(name);
	}

	public Enumeration<URL> findResources(String name) throws IOException {
		if (extend) {
			return super.findResources(name);
		} else {
			return delegate.findResources(name);
		}
	}

	public URL[] getURLs() {
		return delegate.getURLs();
	}
	
	public static boolean isGwtJettyClassLoader(ClassLoader cl) {
		ClassLoader sys = ClassLoader.getSystemClassLoader();
		// move up until we find the system class loader or null
		while (cl != sys && cl != null) { // NOSONAR really is object equality
			cl = cl.getParent();
		}
		// found null, must be gwt classloader !
		return cl == null;
	}

}
