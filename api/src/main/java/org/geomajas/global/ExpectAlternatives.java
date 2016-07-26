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

import org.geomajas.annotation.Api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Indicates that an interface is expected to have more than one implementation.
 * This is used by the bean name generator to determine the default bean name.
 *
 * @author Joachim Van der Auwera
 * @deprecated use {@link org.geomajas.annotation.ExpectAlternatives}
 * @since 1.5.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Deprecated
@Api
public @interface ExpectAlternatives {

}
