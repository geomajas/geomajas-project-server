/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package org.geomajas.widget.advancedviews.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.geomajas.widget.advancedviews.AdvancedviewsException;
import org.geomajas.widget.advancedviews.service.LegendIconsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Spring MVC controller that creates icons for a legend. The view is
 * referenced by name.
 * 
 * @author Kristof Heirwegh
 * 
 */
@Controller("/legendIcons")
public class LegendIconsController {

	public static final String LEGENDICONS_VIEW_NAME = "legendIconsView";

	public static final String IMAGE_KEY = "image";

	@Autowired
	protected LegendIconsService iconService;

	@RequestMapping(value = "/legendIcons", method = RequestMethod.GET)
	public ModelAndView getIcon(@RequestParam("widgetId") String widgetId,
								@RequestParam("layerId") String layerId,
								@RequestParam(value = "styleName", required = false) String styleName,
								@RequestParam(value = "featureStyleId", required = false) String featureStyleId,
								HttpServletRequest request) throws AdvancedviewsException {
		ModelAndView mav = new ModelAndView();
		mav.setViewName(LEGENDICONS_VIEW_NAME);
		mav.addObject(IMAGE_KEY, iconService.createLegendIcon(widgetId, layerId, styleName, featureStyleId));
		return mav;
	}

	@ExceptionHandler
	public ModelAndView exception(AdvancedviewsException exception, HttpServletResponse response) throws Exception {
		response.setContentType("text/html");
		switch (exception.getExceptionCode()) {
		case AdvancedviewsException.NO_SUCH_LAYER:
		case AdvancedviewsException.NO_SUCH_NAMEDSTYLE:
		case AdvancedviewsException.NO_SUCH_FEATURESTYLE:
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			break;
		default:
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		response.getWriter().println(exception.getLocalizedMessage());
		return new ModelAndView();
	}

}
