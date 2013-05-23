/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the Apache
 * License, Version 2.0. All contributions in this program are covered
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
 * @since 1.0.0
 */

@Api(allMethods = true)
public class SimpleLinkInfo implements Serializable {

	private static final long serialVersionUID = 100;

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

		private static final long serialVersionUID = 100;

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

		@Override
		@java.lang.SuppressWarnings("all")
		public java.lang.String toString() {
			return "SimpleLinkInfo.HrefInfo(href=" + this.getHref() + ")";
		}

		@Override
		@java.lang.SuppressWarnings("all")
		public boolean equals(final java.lang.Object o) {
			if (o == this) {
				return true;
			}
			if (!(o instanceof HrefInfo)) {
				return false;
			}
			final HrefInfo other = (HrefInfo) o;
			if (!other.canEqual((java.lang.Object) this)) {
				return false;
			}
			if (this.getHref() == null ? other.getHref() != null : !this.getHref().equals(
					(java.lang.Object) other.getHref())) {
				return false;
			}
			return true;
		}

		/**
		 * Is there a chance that the object are equal? Verifies that the other object has a comparable type.
		 *
		 * @param other other object
		 * @return true when other is an instance of this type
		 */
		public boolean canEqual(final java.lang.Object other) {
			return other instanceof HrefInfo;
		}

		@Override
		@java.lang.SuppressWarnings("all")
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = result * prime + (this.getHref() == null ? 0 : this.getHref().hashCode());
			return result;
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

		private static final long serialVersionUID = 100;

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

		@Override
		@java.lang.SuppressWarnings("all")
		public java.lang.String toString() {
			return "SimpleLinkInfo.RoleInfo(role=" + this.getRole() + ")";
		}

		@Override
		@java.lang.SuppressWarnings("all")
		public boolean equals(final java.lang.Object o) {
			if (o == this) {
				return true;
			}
			if (!(o instanceof RoleInfo)) {
				return false;
			}
			final RoleInfo other = (RoleInfo) o;
			if (!other.canEqual((java.lang.Object) this)) {
				return false;
			}
			if (this.getRole() == null ? other.getRole() != null : !this.getRole().equals(
					(java.lang.Object) other.getRole())) {
				return false;
			}
			return true;
		}

		/**
		 * Is there a chance that the object are equal? Verifies that the other object has a comparable type.
		 *
		 * @param other other object
		 * @return true when other is an instance of this type
		 */
		public boolean canEqual(final java.lang.Object other) {
			return other instanceof RoleInfo;
		}

		@Override
		@java.lang.SuppressWarnings("all")
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = result * prime + (this.getRole() == null ? 0 : this.getRole().hashCode());
			return result;
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

		private static final long serialVersionUID = 100;

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

		@Override
		@java.lang.SuppressWarnings("all")
		public java.lang.String toString() {
			return "SimpleLinkInfo.ArcroleInfo(arcrole=" + this.getArcrole() + ")";
		}

		@Override
		@java.lang.SuppressWarnings("all")
		public boolean equals(final java.lang.Object o) {
			if (o == this) {
				return true;
			}
			if (!(o instanceof ArcroleInfo)) {
				return false;
			}
			final ArcroleInfo other = (ArcroleInfo) o;
			if (!other.canEqual((java.lang.Object) this)) {
				return false;
			}
			if (this.getArcrole() == null ? other.getArcrole() != null : !this.getArcrole().equals(
					(java.lang.Object) other.getArcrole())) {
				return false;
			}
			return true;
		}

		/**
		 * Is there a chance that the object are equal? Verifies that the other object has a comparable type.
		 *
		 * @param other other object
		 * @return true when other is an instance of this type
		 */
		public boolean canEqual(final java.lang.Object other) {
			return other instanceof ArcroleInfo;
		}

		@Override
		@java.lang.SuppressWarnings("all")
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = result * prime + (this.getArcrole() == null ? 0 : this.getArcrole().hashCode());
			return result;
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

		private static final long serialVersionUID = 100;

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
		 * @param title title
		 */
		public void setTitle(String title) {
			this.title = title;
		}

		@Override
		@java.lang.SuppressWarnings("all")
		public java.lang.String toString() {
			return "SimpleLinkInfo.TitleInfo(title=" + this.getTitle() + ")";
		}

		@Override
		@java.lang.SuppressWarnings("all")
		public boolean equals(final java.lang.Object o) {
			if (o == this) {
				return true;
			}
			if (!(o instanceof TitleInfo)) {
				return false;
			}
			final TitleInfo other = (TitleInfo) o;
			if (!other.canEqual((java.lang.Object) this)) {
				return false;
			}
			if (this.getTitle() == null ? other.getTitle() != null : !this.getTitle().equals(
					(java.lang.Object) other.getTitle())) {
				return false;
			}
			return true;
		}

		/**
		 * Is there a chance that the object are equal? Verifies that the other object has a comparable type.
		 *
		 * @param other other object
		 * @return true when other is an instance of this type
		 */
		public boolean canEqual(final java.lang.Object other) {
			return other instanceof TitleInfo;
		}

		@Override
		@java.lang.SuppressWarnings("all")
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = result * prime + (this.getTitle() == null ? 0 : this.getTitle().hashCode());
			return result;
		}
	}

	@Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "SimpleLinkInfo(type=" + this.getType() + ", href=" + this.getHref() + ", role=" + this.getRole()
				+ ", arcrole=" + this.getArcrole() + ", title=" + this.getTitle() + ", show=" + this.getShow()
				+ ", actuate=" + this.getActuate() + ")";
	}

	@Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof SimpleLinkInfo)) {
			return false;
		}
		final SimpleLinkInfo other = (SimpleLinkInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (this.getType() == null ? other.getType() != null : !this.getType().equals(
				(java.lang.Object) other.getType())) {
			return false;
		}
		if (this.getHref() == null ? other.getHref() != null : !this.getHref().equals(
				(java.lang.Object) other.getHref())) {
			return false;
		}
		if (this.getRole() == null ? other.getRole() != null : !this.getRole().equals(
				(java.lang.Object) other.getRole())) {
			return false;
		}
		if (this.getArcrole() == null ? other.getArcrole() != null : !this.getArcrole().equals(
				(java.lang.Object) other.getArcrole())) {
			return false;
		}
		if (this.getTitle() == null ? other.getTitle() != null : !this.getTitle().equals(
				(java.lang.Object) other.getTitle())) {
			return false;
		}
		if (this.getShow() == null ? other.getShow() != null : !this.getShow().equals(
				(java.lang.Object) other.getShow())) {
			return false;
		}
		if (this.getActuate() == null ? other.getActuate() != null : !this.getActuate().equals(
				(java.lang.Object) other.getActuate())) {
			return false;
		}
		return true;
	}

	/**
	 * Is there a chance that the object are equal? Verifies that the other object has a comparable type.
	 *
	 * @param other other object
	 * @return true when other is an instance of this type
	 */
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof SimpleLinkInfo;
	}

	@Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + (this.getType() == null ? 0 : this.getType().hashCode());
		result = result * prime + (this.getHref() == null ? 0 : this.getHref().hashCode());
		result = result * prime + (this.getRole() == null ? 0 : this.getRole().hashCode());
		result = result * prime + (this.getArcrole() == null ? 0 : this.getArcrole().hashCode());
		result = result * prime + (this.getTitle() == null ? 0 : this.getTitle().hashCode());
		result = result * prime + (this.getShow() == null ? 0 : this.getShow().hashCode());
		result = result * prime + (this.getActuate() == null ? 0 : this.getActuate().hashCode());
		return result;
	}
}