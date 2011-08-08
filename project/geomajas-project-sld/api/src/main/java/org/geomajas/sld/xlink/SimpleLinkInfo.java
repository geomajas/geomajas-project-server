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

import org.geomajas.annotations.Api;

/**
 * Schema fragment(s) for this class:...
 * 
 * <pre>
 * &lt;xs:attributeGroup
 * xmlns:ns="http://www.w3.org/1999/xlink"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="simpleLink">
 *   &lt;xs:attribute type="xs:string" fixed="simple" form="qualified" name="type"/>
 *   &lt;xs:attribute use="optional" ref="ns:href">
 *     &lt;!-- Reference to inner class HrefInfo -->
 *   &lt;/xs:attribute>
 *   &lt;xs:attribute use="optional" ref="ns:role">
 *     &lt;!-- Reference to inner class RoleInfo -->
 *   &lt;/xs:attribute>
 *   &lt;xs:attribute use="optional" ref="ns:arcrole">
 *     &lt;!-- Reference to inner class ArcroleInfo -->
 *   &lt;/xs:attribute>
 *   &lt;xs:attribute use="optional" ref="ns:title">
 *     &lt;!-- Reference to inner class TitleInfo -->
 *   &lt;/xs:attribute>
 *   &lt;xs:attribute use="optional" ref="ns:show"/>
 *   &lt;xs:attribute use="optional" ref="ns:actuate"/>
 * &lt;/xs:attributeGroup>
 * </pre>
 *
 * @author Jan De Moerloose
 * @since 1.10.0
 */
@Api(allMethods = true)
public class SimpleLinkInfo implements Serializable {

	private static final long serialVersionUID = 1100;

	private String type;

	private HrefInfo href;

	private RoleInfo role;

	private ArcroleInfo arcrole;

	private TitleInfo title;

	private ShowInfo show;

	private ActuateInfo actuate;

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
	 * Get the 'href' attribute value.
	 * 
	 * @return value
	 */
	public HrefInfo getHref() {
		return href;
	}

	/**
	 * Set the 'href' attribute value.
	 * 
	 * @param href
	 */
	public void setHref(HrefInfo href) {
		this.href = href;
	}

	/**
	 * Get the 'role' attribute value.
	 * 
	 * @return value
	 */
	public RoleInfo getRole() {
		return role;
	}

	/**
	 * Set the 'role' attribute value.
	 * 
	 * @param role
	 */
	public void setRole(RoleInfo role) {
		this.role = role;
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
	 * Schema fragment(s) for this class:...
	 * 
	 * <pre>
	 * &lt;xs:attribute
 * xmlns:ns="http://www.w3.org/1999/xlink"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" use="optional" ref="ns:href"/>
	 * 
	 * &lt;xs:attribute
 * xmlns:ns="http://www.w3.org/1999/xlink"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" type="xs:string" name="href"/>
	 * </pre>
	 */
	public static class HrefInfo implements Serializable {

		private static final long serialVersionUID = 1100;

		private String href;

		/**
		 * Get the 'href' attribute value.
		 * 
		 * @return value
		 */
		public String getHref() {
			return href;
		}

		/**
		 * Set the 'href' attribute value.
		 * 
		 * @param href
		 */
		public void setHref(String href) {
			this.href = href;
		}
	}

	/**
	 * Schema fragment(s) for this class:...
	 * 
	 * <pre>
	 * &lt;xs:attribute
 * xmlns:ns="http://www.w3.org/1999/xlink"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" use="optional" ref="ns:role"/>
	 * 
	 * &lt;xs:attribute
 * xmlns:ns="http://www.w3.org/1999/xlink"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" type="xs:string" name="role"/>
	 * </pre>
	 */
	public static class RoleInfo implements Serializable {

		private static final long serialVersionUID = 1100;

		private String role;

		/**
		 * Get the 'role' attribute value.
		 * 
		 * @return value
		 */
		public String getRole() {
			return role;
		}

		/**
		 * Set the 'role' attribute value.
		 * 
		 * @param role
		 */
		public void setRole(String role) {
			this.role = role;
		}
	}

	/**
	 * Schema fragment(s) for this class:...
	 * 
	 * <pre>
	 * &lt;xs:attribute
 * xmlns:ns="http://www.w3.org/1999/xlink"
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" use="optional" ref="ns:arcrole"/>
	 * 
	 * &lt;xs:attribute
 * xmlns:ns="http://www.w3.org/1999/xlink"
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
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" use="optional" ref="ns:title"/>
	 * 
	 * &lt;xs:attribute
 * xmlns:ns="http://www.w3.org/1999/xlink"
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
}
