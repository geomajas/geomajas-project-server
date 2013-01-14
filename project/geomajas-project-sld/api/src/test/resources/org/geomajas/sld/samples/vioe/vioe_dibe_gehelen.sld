<?xml version="1.0" encoding="UTF-8"?>
<!--
  ~ This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
  ~
  ~ Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
  ~
  ~ The program is available in open source according to the Apache
  ~ License, Version 2.0. All contributions in this program are covered
  ~ by the Geomajas Contributors License Agreement. For full licensing
  ~ details, see LICENSE.txt in the project root.
  -->

<StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.0.0" xmlns:xlink="http://www.w3.org/1999/xlink" xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd">
 <NamedLayer>
  <Name>dibe_gehelen</Name>
  <UserStyle>
   <Title>dibe_gehelen</Title>
   
   <FeatureTypeStyle>
    <Rule>
     <Name>niet_vastgest</Name>
     <Title>Niet vastgesteld</Title>
     <ogc:Filter>
      <ogc:PropertyIsEqualTo>
       <ogc:PropertyName>vastges</ogc:PropertyName>
       <ogc:Literal>False</ogc:Literal>
      </ogc:PropertyIsEqualTo>
     </ogc:Filter>
     <MinScaleDenominator>0</MinScaleDenominator>
     <MaxScaleDenominator>500000</MaxScaleDenominator>
     <PolygonSymbolizer>
      <Fill>
       <CssParameter name="fill">#c4590a</CssParameter>
      </Fill>
     </PolygonSymbolizer>
     <PolygonSymbolizer>
      <Fill>
       <GraphicFill>
        <Graphic>
          <Mark>
            <WellKnownName>shape://backslash</WellKnownName>
            <Stroke>
              <CssParameter name="stroke">#e2660c</CssParameter>
              <CssParameter name="stroke-width">20</CssParameter>
            </Stroke>
          </Mark>
          <Size>50</Size>
         </Graphic>
       </GraphicFill>
      </Fill>
      <Stroke>
       <CssParameter name="stroke">#000000</CssParameter>
       <CssParameter name="stroke-width">0.26</CssParameter>
      </Stroke>
     </PolygonSymbolizer>
    </Rule>
   </FeatureTypeStyle>
   
   <FeatureTypeStyle>
    <Rule>
     <Name>vastgesteld</Name>
     <Title>Vastgesteld</Title>
     <ogc:Filter>
      <ogc:PropertyIsEqualTo>
       <ogc:PropertyName>vastges</ogc:PropertyName>
       <ogc:Literal>True</ogc:Literal>
      </ogc:PropertyIsEqualTo>
     </ogc:Filter>
     <MinScaleDenominator>0</MinScaleDenominator>
     <MaxScaleDenominator>500000</MaxScaleDenominator>
     <PolygonSymbolizer>
      <Fill>
       <CssParameter name="fill">#e2660c</CssParameter>
      </Fill>
      <Stroke>
       <CssParameter name="stroke">#000000</CssParameter>
       <CssParameter name="stroke-width">0.26</CssParameter>
      </Stroke>
     </PolygonSymbolizer>
    </Rule>
   </FeatureTypeStyle>
   
   
  </UserStyle>
 </NamedLayer>
</StyledLayerDescriptor>

