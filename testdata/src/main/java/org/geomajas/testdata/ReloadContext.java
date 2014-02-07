/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2014 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.testdata;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Test annotation which indicates that the {@link org.springframework.context.ApplicationContext ApplicationContext}
 * associated with a test should be reloaded instead of taken from the cache:
 * <ul>
 * <li>before the current test, when declared at the method level</li>
 * <li>before each test method in the current test class, when declared at the class level with class mode set to
 * {@link ClassMode#BEFORE_EACH_TEST_METHOD BEFORE_EACH_TEST_METHOD}</li>
 * <li>before the current test class, when declared at the class level with class mode set to
 * {@link ClassMode#BEFORE_CLASS BEFORE_CLASS}</li>
 * </ul>
 * <p>
 * Use this annotation to ensure that a test is supplied with a new context. If a tests fails without this annotation,
 * it means that some other test is making the context dirty. A better solution may therefore be to track down the other
 * test and adding a DirtiesContext annotation to it.
 * </p>
 * <p>
 * <code>&#064;ReloadContext</code> may be used as a class-level and method-level annotation within the same class. In
 * such scenarios, the <code>ApplicationContext</code> will be marked as <em>dirty</em> before any such annotated method
 * as well as before the entire class. If the {@link ClassMode} is set to {@link ClassMode#BEFORE_EACH_TEST_METHOD
 * BEFORE_EACH_TEST_METHOD}, the context will be marked dirty before each test method in the class.
 * </p>
 * 
 * @author Jan De Moerloose
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface ReloadContext {

	/**
	 * Defines <i>modes</i> which determine how <code>&#064;ReloadContext</code> is interpreted when used to annotate a
	 * test class.
	 */
	static enum ClassMode {

		/**
		 * The associated <code>ApplicationContext</code> will be marked as <em>dirty</em> before the test class.
		 */
		BEFORE_CLASS,

		/**
		 * The associated <code>ApplicationContext</code> will be marked as <em>dirty</em> before each test method in
		 * the class.
		 */
		BEFORE_EACH_TEST_METHOD
	}

	/**
	 * The <i>mode</i> to use when a test class is annotated with <code>&#064;ReloadContext</code>.
	 * <p>
	 * Defaults to {@link ClassMode#BEFORE_CLASS BEFORE_CLASS}.
	 * <p>
	 * Note: Setting the class mode on an annotated test method has no meaning, since the mere presence of the
	 * <code>&#064;ReloadContext</code> annotation on a test method is sufficient.
	 */
	ClassMode classMode() default ClassMode.BEFORE_CLASS;

}
