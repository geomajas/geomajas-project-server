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
package org.geomajas.testdata;

import java.lang.reflect.Method;

import org.geomajas.testdata.ReloadContext.ClassMode;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 * Execution listener that forces a context reload before the test method is executed on methods/classes annotated with
 * {@link ReloadContext}.
 * 
 * WARNING: if used in conjunction with {@link DependencyInjectionTestExecutionListener}, the
 * {@link ReloadContextTestExecutionListener} should come first!
 * 
 * @author Jan De Moerloose
 * 
 */
public class ReloadContextTestExecutionListener extends AbstractTestExecutionListener {

	/**
	 * Marks the {@link ApplicationContext application context} of the supplied {@link TestContext test context} as
	 * {@link TestContext#markApplicationContextDirty() dirty}, and sets the
	 * {@link DependencyInjectionTestExecutionListener#REINJECT_DEPENDENCIES_ATTRIBUTE REINJECT_DEPENDENCIES_ATTRIBUTE}
	 * in the test context to <code>true</code> .
	 */
	protected void reloadContext(TestContext testContext) {
		testContext.markApplicationContextDirty();
		testContext
				.setAttribute(DependencyInjectionTestExecutionListener.REINJECT_DEPENDENCIES_ATTRIBUTE, Boolean.TRUE);
	}

	/**
	 * Forces context reload before test method if the annotation is present on the method or if the annotation is
	 * present on the class and {@link ReloadContext.ClassMode} is set to <code>ClassMode.BEFORE_EACH_TEST_METHOD</code>
	 * .
	 */
	@Override
	public void beforeTestMethod(TestContext testContext) throws Exception {
		Class<?> testClass = testContext.getTestClass();
		Method testMethod = testContext.getTestMethod();
		final Class<ReloadContext> annotationType = ReloadContext.class;

		boolean methodReloadContext = testMethod.isAnnotationPresent(annotationType);
		boolean classReloadContext = testClass.isAnnotationPresent(annotationType);
		ReloadContext classReloadContextAnnotation = testClass.getAnnotation(annotationType);
		ClassMode classMode = classReloadContext ? classReloadContextAnnotation.classMode() : null;

		if (methodReloadContext || (classReloadContext && classMode == ClassMode.BEFORE_EACH_TEST_METHOD)) {
			reloadContext(testContext);
		}
	}

	/**
	 * Forces context reload before test method if the annotation is present on the class and
	 * {@link ReloadContext.ClassMode} is set to <code>ClassMode.BEFORE_CLASS</code> .
	 */
	@Override
	public void beforeTestClass(TestContext testContext) throws Exception {
		Class<?> testClass = testContext.getTestClass();
		final Class<ReloadContext> annotationType = ReloadContext.class;
		boolean reloadContext = testClass.isAnnotationPresent(ReloadContext.class);
		if (reloadContext) {
			ReloadContext reloadContextAnnotation = testClass.getAnnotation(annotationType);
			if (reloadContextAnnotation.classMode() == ClassMode.BEFORE_CLASS) {
				reloadContext(testContext);
			}
		}
	}

}
