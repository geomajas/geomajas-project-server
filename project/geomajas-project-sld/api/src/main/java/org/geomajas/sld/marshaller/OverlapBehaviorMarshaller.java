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
package org.geomajas.sld.marshaller;

import org.geomajas.annotations.Api;
import org.geomajas.sld.OverlapBehaviorInfo;
import org.geomajas.sld.OverlapBehaviorInfo.OverlapBehaviorInfoInner;
import org.jibx.runtime.IAliasable;
import org.jibx.runtime.IMarshaller;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshaller;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;
import org.jibx.runtime.impl.MarshallingContext;
import org.jibx.runtime.impl.UnmarshallingContext;

/**
 * Marshaller to convert OverlapBehavior child elements to enumeration {@link OverlapBehaviorInfoInner}.
 * 
 * @author Jan De Moerloose
 * @since 1.10.0
 */
@Api(allMethods = true)
public class OverlapBehaviorMarshaller implements IMarshaller, IUnmarshaller, IAliasable {

	private String uri;

	private int index;

	private String name;

	/**
	 * Aliased constructor. This takes a name definition for the top-level element. It'll be used by JiBX when a name is
	 * supplied by the mapping which references this custom marshaller/unmarshaller.
	 * 
	 * @param uri
	 *            namespace URI for the top-level element (also used for all other names within the binding)
	 * @param index
	 *            namespace index corresponding to the defined URI within the marshalling context definitions
	 * @param name
	 *            local name for the top-level element
	 */
	public OverlapBehaviorMarshaller(String uri, int index, String name) {
		this.uri = uri;
		this.index = index;
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jibx.runtime.IMarshaller#marshal(java.lang.Object, org.jibx.runtime.IMarshallingContext)
	 */
	public void marshal(Object obj, IMarshallingContext ictx) throws JiBXException {

		// make sure the parameters are as expected
		if (!(obj instanceof OverlapBehaviorInfo)) {
			throw new JiBXException("Invalid object type for marshaller");
		} else if (!(ictx instanceof MarshallingContext)) {
			throw new JiBXException("Invalid object type for marshaller");
		} else {
			OverlapBehaviorInfo overlapBehaviorInfo = (OverlapBehaviorInfo) obj;
			// start by generating start tag for container
			MarshallingContext ctx = (MarshallingContext) ictx;
			ctx.startTag(index, name).content(overlapBehaviorInfo.getOverlapBehavior().xmlValue())
					.closeStartContent();
			ctx.endTag(index, name);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jibx.runtime.IMarshaller#isExtension(java.lang.String)
	 */
	public boolean isExtension(String mapname) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jibx.runtime.IUnmarshaller#isPresent(org.jibx.runtime.IUnmarshallingContext)
	 */
	public boolean isPresent(IUnmarshallingContext ctx) throws JiBXException {
		return ctx.isAt(uri, name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jibx.runtime.IUnmarshaller#unmarshal(java.lang.Object, org.jibx.runtime.IUnmarshallingContext)
	 */
	public Object unmarshal(Object obj, IUnmarshallingContext ictx) throws JiBXException {

		// make sure we're at the appropriate start tag
		UnmarshallingContext ctx = (UnmarshallingContext) ictx;
		if (!ctx.isAt(uri, name)) {
			ctx.throwStartTagNameError(uri, name);
		}

		// create new hashmap if needed
		OverlapBehaviorInfo overlapBehaviorInfo = (OverlapBehaviorInfo) obj;
		if (overlapBehaviorInfo == null) {
			overlapBehaviorInfo = new OverlapBehaviorInfo();
		}

		// process all entries present in document
		ctx.parsePastStartTag(uri, name);
		boolean found = false;
		for (OverlapBehaviorInfoInner inner : OverlapBehaviorInfoInner.values()) {
			if (ctx.parseIfStartTag(uri, inner.name())) {
				found = true;
				overlapBehaviorInfo.setOverlapBehavior(inner);
				ctx.parsePastEndTag(uri, inner.name());
				break;
			}
		}
		if (!found) {
			ctx.throwStartTagException("Unrecognized inner element");
		}
		ctx.parsePastEndTag(uri, name);
		return overlapBehaviorInfo;
	}
}