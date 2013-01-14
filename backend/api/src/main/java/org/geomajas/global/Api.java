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

package org.geomajas.global;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation which indicates that a class and/or method are part of the supported Geomajas API.
 * <p/>
 * Only classes and methods with this annotation are guaranteed to be supported for increasing versions with the same
 * major version number.
 * <p/>
 * When the class is annotated, "allMethods" can be used to indicate that the annotation also applies to all public
 * methods in the class (or interface). When this is not used, the class is guaranteed to exist, but the individual
 * supported methods also need to be annotated. Note that the annotation explicitly does not apply for inner classes,
 * they need explicit annotation.
 * 
 * @author Joachim Van der Auwera
 * @deprecated use {@link org.geomajas.annotation.Api}
 * @since 1.5.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.LOCAL_VARIABLE,
		ElementType.ANNOTATION_TYPE })
@Documented
@Api(allMethods = true)
@Deprecated
public @interface Api {

	/**
	 * For class level annotation, does it apply to all methods?
	 */
	boolean allMethods() default false;
}
