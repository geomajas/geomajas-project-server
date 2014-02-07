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

package org.geomajas.global;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation which indicates that a class and/or method are planned to be part of the supported Geomajas API.
 * <p/>
 * This is an annotation to indicate that this should be part of the API, but there is still some room for discussion
 * or validation before changing to {@link Api}.
 * <p/>
 * The intention is that these annotations only remain for a short time, either removing the annotation or changing to
 * {@link Api}. In principle this should be checked before a release. It is recommended to add some comments to
 * indicate why it is not yet @Api and/or indicate when it should become {@link Api}.
 *
 * @author Joachim Van der Auwera
 * @deprecated use {@link org.geomajas.annotation.FutureApi}
 * @since 1.9.0.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.LOCAL_VARIABLE,
		ElementType.ANNOTATION_TYPE })
@Documented
@Api(allMethods = true)
@Deprecated
public @interface FutureApi {

	/**
	 * For class level annotation, does it apply to all methods?
	 */
	boolean allMethods() default false;
}
