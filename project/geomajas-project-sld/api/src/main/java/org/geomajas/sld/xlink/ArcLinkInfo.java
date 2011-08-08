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
package org.geomajas.sld.xlink;

import java.io.Serializable;

import org.geomajas.annotation.Api;

/**
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:attributeGroup
 * xmlns:xlink="http://www.w3.org/1999/xlink"
 * 
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="arcLink">
 *   &lt;xs:attribute type="xs:string" fixed="arc" form="qualified" name="type"/>
 *   &lt;xs:attribute use="optional" ref="xlink:arcrole">
 *     &lt;!-- Reference to inner class ArcroleInfo -->
 *   &lt;/xs:attribute>
 *   &lt;xs:attribute use="optional" ref="xlink:title">
 *     &lt;!-- Reference to inner class TitleInfo -->
 *   &lt;/xs:attribute>
 *   &lt;xs:attribute use="optional" ref="xlink:show"/>
 *   &lt;xs:attribute use="optional" ref="xlink:actuate"/>
 *   &lt;xs:attribute use="optional" ref="xlink:from">
 *     &lt;!-- Reference to inner class FromInfo -->
 *   &lt;/xs:attribute>
 *   &lt;xs:attribute use="optional" ref="xlink:to">
 *     &lt;!-- Reference to inner class ToInfo -->
 *   &lt;/xs:attribute>
 * &lt;/xs:attributeGroup>
 * </pre>
 *
 * @author Jan De Moerloose
 * @since 1.10.0
 */
@Api(allMethods = true)
public class ArcLinkInfo implements Serializable {

	private static final long serialVersionUID = 1100;

	private String type;

	private ArcroleInfo arcrole;

	private TitleInfo title;

	private ShowInfo show;

	private ActuateInfo actuate;

	private FromInfo from;

	private ToInfo to;

	/**
	 * Get the 'type' attribute value.
	 * 
	 * @return value
	 */
	public String getType() {
		return type;
	}

	/**
	 * Set the 'type' attribute value.
	 * 
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Get the 'arcrole' attribute value.
	 * 
	 * @return value
	 */
	public ArcroleInfo getArcrole() {
		return arcrole;
	}

	/**
	 * Set the 'arcrole' attribute value.
	 * 
	 * @param arcrole
	 */
	public void setArcrole(ArcroleInfo arcrole) {
		this.arcrole = arcrole;
	}

	/**
	 * Get the 'title' attribute value.
	 * 
	 * @return value
	 */
	public TitleInfo getTitle() {
		return title;
	}

	/**
	 * Set the 'title' attribute value.
	 * 
	 * @param title
	 */
	public void setTitle(TitleInfo title) {
		this.title = title;
	}

	/**
	 * Get the 'show' attribute value.
	 * 
	 * @return value
	 */
	public ShowInfo getShow() {
		return show;
	}

	/**
	 * Set the 'show' attribute value.
	 * 
	 * @param show
	 */
	public void setShow(ShowInfo show) {
		this.show = show;
	}

	/**
	 * Get the 'actuate' attribute value.
	 * 
	 * @return value
	 */
	public ActuateInfo getActuate() {
		return actuate;
	}

	/**
	 * Set the 'actuate' attribute value.
	 * 
	 * @param actuate
	 */
	public void setActuate(ActuateInfo actuate) {
		this.actuate = actuate;
	}

	/**
	 * Get the 'from' attribute value.
	 * 
	 * @return value
	 */
	public FromInfo getFrom() {
		return from;
	}

	/**
	 * Set the 'from' attribute value.
	 * 
	 * @param from
	 */
	public void setFrom(FromInfo from) {
		this.from = from;
	}

	/**
	 * Get the 'to' attribute value.
	 * 
	 * @return value
	 */
	public ToInfo getTo() {
		return to;
	}

	/**
	 * Set the 'to' attribute value.
	 * 
	 * @param to
	 */
	public void setTo(ToInfo to) {
		this.to = to;
	}

	/**
	 * Schema fragment(s) for this class:...
	 * 
	 * <pre>
	 * &lt;xs:attribute
 * xmlns:ns="http://www.w3.org/1999/xlink" 
	 *
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" use="optional" ref="ns:arcrole"/>
	 * 
	 * &lt;xs:attribute
 * xmlns:ns="http://www.w3.org/1999/xlink" 
	 *
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" type="xs:string" name="arcrole"/>
	 * </pre>
	 */
	public static class ArcroleInfo implements Serializable {

		private static final long serialVersionUID = 1100;

		private String arcrole;

		/**
		 * Get the 'arcrole' attribute value.
		 * 
		 * @return value
		 */
		public String getArcrole() {
			return arcrole;
		}

		/**
		 * Set the 'arcrole' attribute value.
		 * 
		 * @param arcrole
		 */
		public void setArcrole(String arcrole) {
			this.arcrole = arcrole;
		}
	}

	/**
	 * Schema fragment(s) for this class:...
	 * 
	 * <pre>
	 * &lt;xs:attribute
 * xmlns:ns="http://www.w3.org/1999/xlink" 
	 *
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" use="optional" ref="ns:title"/>
	 * 
	 * &lt;xs:attribute
 * xmlns:ns="http://www.w3.org/1999/xlink" 
	 *
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" type="xs:string" name="title"/>
	 * </pre>
	 */
	public static class TitleInfo implements Serializable {

		private static final long serialVersionUID = 1100;

		private String title;

		/**
		 * Get the 'title' attribute value.
		 * 
		 * @return value
		 */
		public String getTitle() {
			return title;
		}

		/**
		 * Set the 'title' attribute value.
		 * 
		 * @param title
		 */
		public void setTitle(String title) {
			this.title = title;
		}
	}

	/**
	 * Schema fragment(s) for this class:...
	 * 
	 * <pre>
	 * &lt;xs:attribute
 * xmlns:ns="http://www.w3.org/1999/xlink"
	 * 
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" use="optional" ref="ns:from"/>
	 * 
	 * &lt;xs:attribute
 * xmlns:ns="http://www.w3.org/1999/xlink"
	 * 
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" type="xs:string" name="from"/>
	 * </pre>
	 */
	public static class FromInfo implements Serializable {

		private static final long serialVersionUID = 1100;

		private String from;

		/**
		 * Get the 'from' attribute value.
		 * 
		 * @return value
		 */
		public String getFrom() {
			return from;
		}

		/**
		 * Set the 'from' attribute value.
		 * 
		 * @param from
		 */
		public void setFrom(String from) {
			this.from = from;
		}
	}

	/**
	 * Schema fragment(s) for this class:...
	 * 
	 * <pre>
	 * &lt;xs:attribute
 * xmlns:ns="http://www.w3.org/1999/xlink" 
	 *
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" use="optional" ref="ns:to"/>
	 * 
	 * &lt;xs:attribute
 * xmlns:ns="http://www.w3.org/1999/xlink" 
	 *
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" type="xs:string" name="to"/>
	 * </pre>
	 */
	public static class ToInfo implements Serializable {

		private static final long serialVersionUID = 1100;

		private String to;

		/**
		 * Get the 'to' attribute value.
		 * 
		 * @return value
		 */
		public String getTo() {
			return to;
		}

		/**
		 * Set the 'to' attribute value.
		 * 
		 * @param to
		 */
		public void setTo(String to) {
			this.to = to;
		}
	}
}
