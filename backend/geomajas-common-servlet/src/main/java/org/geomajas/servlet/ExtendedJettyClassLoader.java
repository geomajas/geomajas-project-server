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

}
