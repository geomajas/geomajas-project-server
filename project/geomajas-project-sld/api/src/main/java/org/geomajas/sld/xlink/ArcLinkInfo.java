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
 * @since 1.0.0
 */

@Api(allMethods = true)
public class ArcLinkInfo implements Serializable {

	private static final long serialVersionUID = 100;

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
			return "ArcLinkInfo.ArcroleInfo(arcrole=" + this.getArcrole() + ")";
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
		 * @param title title
		 */
		public void setTitle(String title) {
			this.title = title;
		}

		@Override
		@java.lang.SuppressWarnings("all")
		public java.lang.String toString() {
			return "ArcLinkInfo.TitleInfo(title=" + this.getTitle() + ")";
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

		private static final long serialVersionUID = 100;

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
		 * @param from from
		 */
		public void setFrom(String from) {
			this.from = from;
		}

		@Override
		@java.lang.SuppressWarnings("all")
		public java.lang.String toString() {
			return "ArcLinkInfo.FromInfo(from=" + this.getFrom() + ")";
		}

		@Override
		@java.lang.SuppressWarnings("all")
		public boolean equals(final java.lang.Object o) {
			if (o == this) {
				return true;
			}
			if (!(o instanceof FromInfo)) {
				return false;
			}
			final FromInfo other = (FromInfo) o;
			if (!other.canEqual((java.lang.Object) this)) {
				return false;
			}
			if (this.getFrom() == null ? other.getFrom() != null : !this.getFrom().equals(
					(java.lang.Object) other.getFrom())) {
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
			return other instanceof FromInfo;
		}

		@Override
		@java.lang.SuppressWarnings("all")
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = result * prime + (this.getFrom() == null ? 0 : this.getFrom().hashCode());
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
	 * xmlns:xs="http://www.w3.org/2001/XMLSchema" use="optional" ref="ns:to"/>
	 * 
	 * &lt;xs:attribute
	 * xmlns:ns="http://www.w3.org/1999/xlink" 
	 * 
	 * xmlns:xs="http://www.w3.org/2001/XMLSchema" type="xs:string" name="to"/>
	 * </pre>
	 */
	public static class ToInfo implements Serializable {

		private static final long serialVersionUID = 100;

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

		@Override
		@java.lang.SuppressWarnings("all")
		public java.lang.String toString() {
			return "ArcLinkInfo.ToInfo(to=" + this.getTo() + ")";
		}

		@Override
		@java.lang.SuppressWarnings("all")
		public boolean equals(final java.lang.Object o) {
			if (o == this) {
				return true;
			}
			if (!(o instanceof ToInfo)) {
				return false;
			}
			final ToInfo other = (ToInfo) o;
			if (!other.canEqual((java.lang.Object) this)) {
				return false;
			}
			if (this.getTo() == null ? other.getTo() != null : !this.getTo().equals((java.lang.Object) other.getTo())) {
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
			return other instanceof ToInfo;
		}

		@Override
		@java.lang.SuppressWarnings("all")
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = result * prime + (this.getTo() == null ? 0 : this.getTo().hashCode());
			return result;
		}
	}

	@Override
	@java.lang.SuppressWarnings("all")
	public java.lang.String toString() {
		return "ArcLinkInfo(type=" + this.getType() + ", arcrole=" + this.getArcrole() + ", title=" + this.getTitle()
				+ ", show=" + this.getShow() + ", actuate=" + this.getActuate() + ", from=" + this.getFrom() + ", to="
				+ this.getTo() + ")";
	}

	@Override
	@java.lang.SuppressWarnings("all")
	public boolean equals(final java.lang.Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof ArcLinkInfo)) {
			return false;
		}
		final ArcLinkInfo other = (ArcLinkInfo) o;
		if (!other.canEqual((java.lang.Object) this)) {
			return false;
		}
		if (this.getType() == null ? other.getType() != null : !this.getType().equals(
				(java.lang.Object) other.getType())) {
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
		if (this.getFrom() == null ? other.getFrom() != null : !this.getFrom().equals(
				(java.lang.Object) other.getFrom())) {
			return false;
		}
		if (this.getTo() == null ? other.getTo() != null : !this.getTo().equals((java.lang.Object) other.getTo())) {
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
		return other instanceof ArcLinkInfo;
	}

	@Override
	@java.lang.SuppressWarnings("all")
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = result * prime + (this.getType() == null ? 0 : this.getType().hashCode());
		result = result * prime + (this.getArcrole() == null ? 0 : this.getArcrole().hashCode());
		result = result * prime + (this.getTitle() == null ? 0 : this.getTitle().hashCode());
		result = result * prime + (this.getShow() == null ? 0 : this.getShow().hashCode());
		result = result * prime + (this.getActuate() == null ? 0 : this.getActuate().hashCode());
		result = result * prime + (this.getFrom() == null ? 0 : this.getFrom().hashCode());
		result = result * prime + (this.getTo() == null ? 0 : this.getTo().hashCode());
		return result;
	}
}