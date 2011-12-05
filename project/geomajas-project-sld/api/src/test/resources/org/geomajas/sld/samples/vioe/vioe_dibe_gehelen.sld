<?xml version="1.0" encoding="UTF-8"?>
<StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.0.0" xmlns:xlink="http://www.w3.org/1999/xlink" xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd">
 <NamedLayer>
  <Name>dibe_gehelen</Name>
  <UserStyle>
   <Title>dibe_gehelen</Title>
   
   <FeatureTypeStyle>
    <Rule>
     <Title>Niet vastgesteld</Title>
     <Name>niet_vastgest</Name>
     <MinScaleDenominator>0</MinScaleDenominator>
     <MaxScaleDenominator>500000</MaxScaleDenominator>
     <ogc:Filter>
      <ogc:PropertyIsEqualTo>
       <ogc:PropertyName>vastges</ogc:PropertyName>
       <ogc:Literal>False</ogc:Literal>
      </ogc:PropertyIsEqualTo>
     </ogc:Filter>
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
     <MinScaleDenominator>0</MinScaleDenominator>
     <MaxScaleDenominator>500000</MaxScaleDenominator>
     <ogc:Filter>
      <ogc:PropertyIsEqualTo>
       <ogc:PropertyName>vastges</ogc:PropertyName>
       <ogc:Literal>True</ogc:Literal>
      </ogc:PropertyIsEqualTo>
     </ogc:Filter>
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

