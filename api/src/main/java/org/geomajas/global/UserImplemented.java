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

import org.geomajas.annotation.Api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker annotation which indicates that an interface is expected to be implemented by client code and thus indicates
 * that no methods may be added to the interface without breaking API compatibility.
 *
 * @author Joachim Van der Auwera
 * @deprecated use {@link org.geomajas.annotation.UserImplemented}
 * @since 1.5.0
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ ElementType.TYPE })
@Documented
@Deprecated
@Api
public @interface UserImplemented {
}
