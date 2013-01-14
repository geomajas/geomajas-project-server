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
 * xmlns:xlink="http://www.w3.org/1999/xlink"
 * 
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" name="locatorLink">
 *   &lt;xs:attribute type="xs:string" fixed="locator" form="qualified" name="type"/>
 *   &lt;xs:attribute use="required" ref="xlink:href"/>
 *   &lt;xs:attribute use="optional" ref="xlink:role">
 *     &lt;!-- Reference to inner class RoleInfo -->
 *   &lt;/xs:attribute>
 *   &lt;xs:attribute use="optional" ref="xlink:title">
 *     &lt;!-- Reference to inner class TitleInfo -->
 *   &lt;/xs:attribute>
 *   &lt;xs:attribute use="optional" ref="xlink:label">
 *     &lt;!-- Reference to inner class LabelInfo -->
 *   &lt;/xs:attribute>
 * &lt;/xs:attributeGroup>
 * 
 * &lt;xs:attribute
 * xmlns:xlink="http://www.w3.org/1999/xlink" 
 * 
 * xmlns:xs="http://www.w3.org/2001/XMLSchema" type="xs:string" name="href"/>
 * </pre>
 * 
 * @author Jan De Moerloose
 * @since 1.0.0
 */

@Api(allMethods = true)
public class LocatorLinkInfo implements Serializable {

	private static final long serialVersionUID = 100;

	private String type;

	private String href;

	private RoleInfo role;

	private TitleInfo title;

	private LabelInfo label;

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
	 * Get the 'label' attribute value.
	 * 
	 * @return value
	 */
	public LabelInfo getLabel() {
		return label;
	}

	/**
	 * Set the 'label' attribute value.
	 * 
	 * @param label
	 */
	public void setLabel(LabelInfo label) {
		this.label = label;
	}

	/**
	 * Schema fragment(s) for this class:...
	 * 
	 * <pre>
	 * &lt;xs:attribute
	 * xmlns:ns="http://www.w3.org/1999/xlink"
	 * 
	 * xmlns:xs="http://www.w3.org/2001/XMLSchema" use="optional" ref="ns:role"/>
	 * 
	 * &lt;xs:attribute
	 * xmlns:ns="http://www.w3.org/1999/xlink"
	 * 
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

		/** {@inheritDoc} */
		@java.lang.Override
		@java.lang.SuppressWarnings("all")
		public java.lang.String toString() {
			return "LocatorLinkInfo.RoleInfo(role=" + this.getRole() + ")";
		}

		/** {@inheritDoc} */
		@java.lang.Override
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

		/** {@inheritDoc} */
		@java.lang.SuppressWarnings("all")
		public boolean canEqual(final java.lang.Object other) {
			return other instanceof RoleInfo;
		}

		/** {@inheritDoc} */
		@java.lang.Override
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
		 * @param title
		 */
		public void setTitle(String title) {
			this.title = title;
		}

		/** {@inheritDoc} */
		@java.lang.Override
		@java.lang.SuppressWarnings("all")
		public java.lang.String toString() {
			return "LocatorLinkInfo.TitleInfo(title=" + this.getTitle() + ")";
		}

		/** {@inheritDoc} */
		@java.lang.Override
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

		/** {@inheritDoc} */
		@java.lang.SuppressWarnings("all")
		public boolean canEqual(final java.lang.Object other) {
			return other instanceof TitleInfo;
		}

		/** {@inheritDoc} */
		@java.lang.Override
		@java.lang.SuppressWarnings("all")
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = result * prime + (this.getTitle() == null ? 0 : this.getTitle().hashCode());
			return result;
		}
	}

	/**
	 * Schema fragment(s) for this class:...
	 * 
	 * <pre>
	 * &lt;xs:attribute
	 * xmlns:ns="http://www.w3.org/1999/xlink"
	 * 
	 * xmlns:xs="http://www.w3.org/2001/XMLSchema" use="optional" ref="ns:label"/>
	 * 
	 * &lt;xs:attribute
	 * xmlns:ns="http://www.w3.org/1999/xlink"
	 * 
	 * xmlns:xs="http://www.w3.org/2001/XMLSchema" type="xs:string" name="label"/>
	 * </pre>
	 */
	public static class LabelInfo implements Serializable {

		private static final long serialVersionUID = 100;

		private String label;

		/**
		 * Get the 'label' attribute value.
		 * 
		 * @return value
		 */
		public String getLabel() {
			return label;
		}

		/**
		 * Set the 'label' attribute value.
		 * 
		 * @param label
		 */
		public void setLabel(String label) {
			this.label = label;
		}

		/** {@inheritDoc} */
		@java.lang.Override
		@java.lang.SuppressWarnings("all")
		public java.lang.String toString() {
			return "LocatorLinkInfo.LabelInfo(label=" + this.getLabel() + ")";
		}

		/** {@inheritDoc} */
		@java.lang.Override
		@java.lang.SuppressWarnings("all")
		public boolean equals(final java.lang.Object o) {
			if (o == this) {
				return true;
			}
			if (!(o instanceof LabelInfo)) {
				return false;
			}
			final LabelInfo other = (LabelInfo) o;
			if (!other.canEqual((java.lang.Object) this)) {
				return false;
			}
			if (this.getLabel() == null ? other.getLabel() != null : !this.getLabel().equals(
					(java.lang.Object) other.getLabel())) {
				return false;
			}
			return true;
		}

		/** {@inheritDoc} */
		@java.lang.SuppressWarnings("all")
		public boolean canEqual(final java.lang.Object other) {
			return other instanceof LabelInfo;
		}

		/** {@inheritDoc} */
		@java.lang.Override
		@java.lang.SuppressWarnings("all")
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = result * prime + (this.getLabel() == null ? 0 : this.getLabel().hashCode());
			return result;
		}
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "LocatorLinkInfo(type=" + this.getType() + ", href=" + this.getHref() + ", role=" + this.getRole()
				+ ", title=" + this.getTitle() + ", label=" + this.getLabel() + ")";
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof LocatorLinkInfo)) {
			return false;
		}
		final LocatorLinkInfo other = (LocatorLinkInfo) o;
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
		if (this.getTitle() == null ? other.getTitle() != null : !this.getTitle().equals(
				(java.lang.Object) other.getTitle())) {
			return false;
		}
		if (this.getLabel() == null ? other.getLabel() != null : !this.getLabel().equals(
				(java.lang.Object) other.getLabel())) {
			return false;
		}
		return true;
	}

	/** {@inheritDoc} */
	@java.lang.SuppressWarnings("all")
	public boolean canEqual(final java.lang.Object other) {
		return other instanceof LocatorLinkInfo;
	}

	/** {@inheritDoc} */
	@java.lang.Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + (this.getType() == null ? 0 : this.getType().hashCode());
		result = result * prime + (this.getHref() == null ? 0 : this.getHref().hashCode());
		result = result * prime + (this.getRole() == null ? 0 : this.getRole().hashCode());
		result = result * prime + (this.getTitle() == null ? 0 : this.getTitle().hashCode());
		result = result * prime + (this.getLabel() == null ? 0 : this.getLabel().hashCode());
		return result;
	}
}