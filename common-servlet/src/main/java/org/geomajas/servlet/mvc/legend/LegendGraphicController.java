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
package org.geomajas.servlet.mvc.legend;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.geomajas.configuration.NamedStyleInfo;
import org.geomajas.global.ExceptionCode;
import org.geomajas.global.GeomajasException;
import org.geomajas.service.LegendGraphicService;
import org.geomajas.service.StyleService;
import org.geomajas.service.legend.DefaultLegendGraphicMetadata;
import org.geomajas.service.legend.LegendGraphicMetadata;
import org.geomajas.sld.FeatureTypeStyleInfo;
import org.geomajas.sld.RuleInfo;
import org.geomajas.sld.UserStyleInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Spring MVC controller that creates icons for a legend. The view is referenced by name.
 * 
 * @author Jan De Moerloose
 * @author Oliver May
 * 
 */
@Controller("/legendgraphic/**")
public class LegendGraphicController {

	public static final String LEGENDGRAPHIC_VIEW_NAME = "legendGraphicView";

	@Autowired
	protected LegendGraphicService legendService;

	@Autowired
	private StyleService styleService;

	/**
	 * Gets a legend graphic with the specified metadata parameters. All parameters are passed as request parameters.
	 * 
	 * @param layerId
	 *            the layer id
	 * @param styleName
	 *            the style name
	 * @param ruleIndex
	 *            the rule index
	 * @param format
	 *            the image format ('png','jpg','gif')
	 * @param width
	 *            the graphic's width
	 * @param height
	 *            the graphic's height
	 * @param scale
	 *            the scale denominator (not supported yet)
	 * @param allRules
	 *            if true the image will contain all rules stacked vertically
	 * @param request
	 *            the servlet request object
	 * @return the model and view
	 * @throws GeomajasException
	 *             when a style or rule does not exist or is not renderable
	 */
	@RequestMapping(value = "/legendgraphic", method = RequestMethod.GET)
	public ModelAndView getGraphic(@RequestParam("layerId") String layerId,
			@RequestParam(value = "styleName", required = false) String styleName,
			@RequestParam(value = "ruleIndex", required = false) Integer ruleIndex,
			@RequestParam(value = "format", required = false) String format,
			@RequestParam(value = "width", required = false) Integer width,
			@RequestParam(value = "height", required = false) Integer height,
			@RequestParam(value = "scale", required = false) Double scale,
			@RequestParam(value = "allRules", required = false) Boolean allRules, HttpServletRequest request)
			throws GeomajasException {
		if (!allRules) {
			return getGraphic(layerId, styleName, ruleIndex, format, width, height, scale);
		} else {
			return getGraphics(layerId, styleName, format, width, height, scale);
		}
	}

	/**
	 * @param layerId
	 *            the layer id
	 * @param styleName
	 *            the style name
	 * @param format
	 *            the image format ('png','jpg','gif')
	 * @param width
	 *            the graphic's width
	 * @param height
	 *            the graphic's height
	 * @param scale
	 *            the scale denominator (not supported yet)
	 * @return the model and view
	 * @throws GeomajasException
	 *             when a style or rule does not exist or is not renderable
	 */
	private ModelAndView getGraphics(String layerId, String styleName, String format, Integer width, Integer height,
			Double scale) throws GeomajasException {
		ModelAndView mav = new ModelAndView();
		mav.setViewName(LEGENDGRAPHIC_VIEW_NAME);

		if (styleName != null) {
			NamedStyleInfo namedStyle = styleService.retrieveStyle(layerId, styleName);
			if (namedStyle != null) {
				UserStyleInfo userStyle = namedStyle.getUserStyle();
				List<LegendGraphicMetadata> allLegendMetaData = new ArrayList<LegendGraphicMetadata>(userStyle
						.getFeatureTypeStyleList().size());
				for (RuleInfo ruleInfo : userStyle.getFeatureTypeStyleList().get(0).getRuleList()) {
					DefaultLegendGraphicMetadata legendMetadata = new DefaultLegendGraphicMetadata();
					legendMetadata.setLayerId(layerId);
					if (width != null) {
						legendMetadata.setWidth(width);
					}
					if (height != null) {
						legendMetadata.setHeight(height);
					}
					if (scale != null) {
						legendMetadata.setScale(scale);
					}
					if (styleName != null) {
						legendMetadata.setUserStyle(userStyle);
					}
					legendMetadata.setRuleInfo(ruleInfo);
					allLegendMetaData.add(legendMetadata);
				}
				mav.addObject(LegendGraphicView.IMAGE_FORMAT_KEY, format == null ? "png" : format);
				mav.addObject(LegendGraphicView.IMAGE_KEY, legendService.getLegendGraphics(allLegendMetaData));
			}
		}
		return mav;
	}

	/**
	 * @param layerId
	 *            the layer id
	 * @param styleName
	 *            the style name
	 * @param ruleIndex
	 *            the rule index
	 * @param format
	 *            the image format ('png','jpg','gif')
	 * @param width
	 *            the graphic's width
	 * @param height
	 *            the graphic's height
	 * @param scale
	 *            the scale denominator (not supported yet)
	 * @return the model and view
	 * @throws GeomajasException
	 *             when a style or rule does not exist or is not renderable
	 */
	private ModelAndView getGraphic(String layerId, String styleName, Integer ruleIndex, String format, Integer width,
			Integer height, Double scale) throws GeomajasException {
		ModelAndView mav = new ModelAndView();
		mav.setViewName(LEGENDGRAPHIC_VIEW_NAME);
		DefaultLegendGraphicMetadata legendMetadata = new DefaultLegendGraphicMetadata();
		legendMetadata.setLayerId(layerId);
		if (width != null) {
			legendMetadata.setWidth(width);
		}
		if (height != null) {
			legendMetadata.setHeight(height);
		}
		if (scale != null) {
			legendMetadata.setScale(scale);
		}
		if (styleName != null) {
			NamedStyleInfo namedStyle = styleService.retrieveStyle(layerId, styleName);
			if (namedStyle != null) {
				UserStyleInfo userStyle = namedStyle.getUserStyle();
				if (ruleIndex != null) {
					FeatureTypeStyleInfo featureTypeStyle = userStyle.getFeatureTypeStyleList().get(0);
					if (ruleIndex < 0 || featureTypeStyle.getRuleList().size() <= ruleIndex) {
						throw new GeomajasException(ExceptionCode.RULE_NOT_FOUND, ruleIndex, styleName);
					} else {
						legendMetadata.setRuleInfo(featureTypeStyle.getRuleList().get(ruleIndex));
					}
				} else {
					legendMetadata.setUserStyle(userStyle);
				}
			}
		}

		mav.addObject(LegendGraphicView.IMAGE_FORMAT_KEY, format == null ? "png" : format);
		mav.addObject(LegendGraphicView.IMAGE_KEY, legendService.getLegendGraphic(legendMetadata));
		return mav;
	}

	/**
	 * Gets a legend graphic with the specified metadata parameters. Assumes REST URL style:
	 * /legendgraphic/{layerId}/{styleName}/{ruleIndex}.{format}
	 * 
	 * @param layerId
	 *            the layer id
	 * @param styleName
	 *            the style name
	 * @param ruleIndex
	 *            the rule index, all for a combined image
	 * @param format
	 *            the image format ('png','jpg','gif')
	 * @param width
	 *            the graphic's width
	 * @param height
	 *            the graphic's height
	 * @param scale
	 *            the scale denominator (not supported yet)
	 * @param request
	 *            the servlet request object
	 * @return the model and view
	 * @throws GeomajasException
	 *             when a style or rule does not exist or is not renderable
	 */
	@RequestMapping(value = "/legendgraphic/{layerId}/{styleName}/{ruleIndex}.{format}", method = RequestMethod.GET)
	public ModelAndView getRuleGraphic(@PathVariable("layerId") String layerId,
			@PathVariable("styleName") String styleName, @PathVariable("ruleIndex") Integer ruleIndex,
			@PathVariable("format") String format, @RequestParam(value = "width", required = false) Integer width,
			@RequestParam(value = "height", required = false) Integer height,
			@RequestParam(value = "scale", required = false) Double scale, HttpServletRequest request)
			throws GeomajasException {
		return getGraphic(layerId, styleName, ruleIndex, format, width, height, scale, false, request);
	}

	/**
	 * Gets a legend graphic with the specified metadata parameters. Assumes REST URL style:
	 * /legendgraphic/{layerId}/{styleName}/all.{format}
	 * 
	 * @param layerId
	 *            the layer id
	 * @param styleName
	 *            the style name
	 * @param format
	 *            the image format ('png','jpg','gif')
	 * @param width
	 *            the graphic's width
	 * @param height
	 *            the graphic's height
	 * @param scale
	 *            the scale denominator (not supported yet)
	 * @param request
	 *            the servlet request object
	 * @return the model and view
	 * @throws GeomajasException
	 *             when a style or rule does not exist or is not renderable
	 */
	@RequestMapping(value = "/legendgraphic/{layerId}/{styleName}/all.{format}", method = RequestMethod.GET)
	public ModelAndView getAllRuleGraphic(@PathVariable("layerId") String layerId,
			@PathVariable("styleName") String styleName, @PathVariable("format") String format,
			@RequestParam(value = "width", required = false) Integer width,
			@RequestParam(value = "height", required = false) Integer height,
			@RequestParam(value = "scale", required = false) Double scale, HttpServletRequest request)
			throws GeomajasException {
		return getGraphic(layerId, styleName, null, format, width, height, scale, true, request);
	}

	/**
	 * Gets a legend graphic for a style with a default rule. Assumes REST url style:
	 * /legendgraphic/{layerId}/{styleName}.{format}
	 * 
	 * @param layerId
	 *            the layer id
	 * @param styleName
	 *            the style name
	 * @param format
	 *            the image format ('png','jpg','gif')
	 * @param width
	 *            the graphic's width
	 * @param height
	 *            the graphic's height
	 * @param scale
	 *            the scale denominator (not supported yet)
	 * @param request
	 *            the servlet request object
	 * @return the model and view
	 * @throws GeomajasException
	 *             when a style or rule does not exist or is not renderable
	 */
	@RequestMapping(value = "/legendgraphic/{layerId}/{styleName}.{format}", method = RequestMethod.GET)
	public ModelAndView getStyleGraphic(@PathVariable("layerId") String layerId,
			@PathVariable("styleName") String styleName, @PathVariable("format") String format,
			@RequestParam(value = "width", required = false) Integer width,
			@RequestParam(value = "height", required = false) Integer height,
			@RequestParam(value = "scale", required = false) Double scale, HttpServletRequest request)
			throws GeomajasException {
		return getGraphic(layerId, styleName, null, format, width, height, scale, false, request);
	}

	/**
	 * Gets a legend graphic for a layer with a default style and rule. Assumes REST url style:
	 * /legendgraphic/{layerId}.{format}
	 * 
	 * @param layerId
	 *            the layer id
	 * @param styleName
	 *            the style name
	 * @param format
	 *            the image format ('png','jpg','gif')
	 * @param width
	 *            the graphic's width
	 * @param height
	 *            the graphic's height
	 * @param scale
	 *            the scale denominator (not supported yet)
	 * @param request
	 *            the servlet request object
	 * @return the model and view
	 * @throws GeomajasException
	 *             when a style or rule does not exist or is not renderable
	 */
	@RequestMapping(value = "/legendgraphic/{layerId}.{format}", method = RequestMethod.GET)
	public ModelAndView getLayerGraphic(@PathVariable("layerId") String layerId, @PathVariable("format") String format,
			@RequestParam(value = "width", required = false) Integer width,
			@RequestParam(value = "height", required = false) Integer height,
			@RequestParam(value = "scale", required = false) Double scale, HttpServletRequest request)
			throws GeomajasException {
		return getGraphic(layerId, null, null, format, width, height, scale, false, request);
	}

	@ExceptionHandler
	public ModelAndView exception(GeomajasException exception, HttpServletResponse response) throws Exception {
		response.setContentType("text/html");
		switch (exception.getExceptionCode()) {
			case ExceptionCode.STYLE_NOT_FOUND:
			case ExceptionCode.LAYER_NOT_FOUND:
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				break;
			default:
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		response.getWriter().println(exception.getLocalizedMessage());
		return new ModelAndView();
	}

}
