/*
 * Copyright (C) 2012 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */
package gov.nasa.worldwindx.examples;

import gov.nasa.worldwind.formats.shapefile.*;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.util.*;
import gov.nasa.worldwind.render.Renderable;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import gov.nasa.worldwind.render.PointPlacemarkAttributes;
import gov.nasa.worldwind.render.PointPlacemark;
import javax.imageio.ImageIO;

/**
 * Illustrates how to import ESRI Shapefiles into WorldWind. This uses a <code>{@link ShapefileLayerFactory}</code> to
 * parse a Shapefile's contents and convert the shapefile into an equivalent WorldWind shape.
 *
 * @version $Id: Shapefiles.java 3212 2015-06-18 02:45:56Z tgaskins $ 
 */
public class Shapefiles extends ApplicationTemplate {

    public static class AppFrame extends ApplicationTemplate.AppFrame implements ShapefileRenderable.AttributeDelegate {

        public AppFrame() {
            ShapefileLayerFactory factory = new ShapefileLayerFactory();

            try {
                InputStream is = WWIO.openFileOrResourceStream("images/yellow-dot.png", this.getClass());
                BufferedImage placemarkImage = ImageIO.read(is);
                PointPlacemarkAttributes placeMarkAttrs = new PointPlacemarkAttributes();
                placeMarkAttrs.setImage(placemarkImage);
                factory.setNormalPointAttributes(placeMarkAttrs);
                factory.setAttributeDelegate(this);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            String[] shapeFiles = {
                "testData/shapefiles/MoonPlaceNames/Oceanus/Oceanus.shp",
                "testData/shapefiles/MoonPlaceNames/Mare/Mare.shp", 
                "testData/shapefiles/MoonPlaceNames/Mons/Mons.shp", 
                "testData/shapefiles/MoonPlaceNames/Catena/Catena.shp",
                "testData/shapefiles/MoonPlaceNames/Vallis/Vallis.shp",
                "testData/shapefiles/MoonPlaceNames/Crater/Crater.shp",
                "testData/shapefiles/MoonPlaceNames/Dorsum/Dorsum.shp",
                "testData/shapefiles/MoonPlaceNames/Lacus/Lacus.shp",
                "testData/shapefiles/MoonPlaceNames/Sinus/Sinus.shp",
                "testData/shapefiles/MoonPlaceNames/Rima/Rima.shp",
                "testData/shapefiles/MoonPlaceNames/Promontorium/Promontorium.shp",
                "testData/shapefiles/MoonPlaceNames/Palus/Palus.shp",
                "testData/shapefiles/MoonPlaceNames/Rupes/Rupes.shp",
                "testData/shapefiles/MoonPlaceNames/Landing Site/LandingSiteName.shp",
                "testData/shapefiles/MoonPlaceNames/Planitia/Planitia.shp",
                "testData/shapefiles/MoonPlaceNames/Albedo/Albedo.shp",
                "testData/shapefiles/MoonPlaceNames/Satellite/Satellite.shp",
            };           
            ShapefileLayerFactory.CompletionCallback callBack = new ShapefileLayerFactory.CompletionCallback() {
                @Override
                public void completion(Object result) {
                    final Layer layer = (Layer) result; // the result is the layer the factory created
                    String layerName = layer.getName();
                    layerName = layerName.substring(0, layerName.length()-4);
                    layer.setName(WWIO.getFilename(layerName));
      
                    // Add the layer to the WorldWindow's layer list on the Event Dispatch Thread.
                    SwingUtilities.invokeLater(new Runnable() {                           
                        @Override
                        public void run() {                            
                            AppFrame.this.getWwd().getModel().getLayers().add(layer);                                                                    
                        }                            
                    });
                }

                @Override
                public void exception(Exception e) {
                    Logging.logger().log(java.util.logging.Level.SEVERE, e.getMessage(), e);
                }
            };
            for (String shapeFile : shapeFiles) {
                // Load the shapefile. Define the completion callback.
                factory.createFromShapefileSource(shapeFile, callBack);
            }

        }

        @Override
        public void assignAttributes(ShapefileRecord shapefileRecord, ShapefileRenderable.Record renderableRecord) {
        }

        @Override
        public void assignRenderableAttributes(ShapefileRecord shapefileRecord, Renderable renderable) {
            DBaseRecord attrs = shapefileRecord.getAttributes();
            String label = attrs.getValue("name").toString();
            if (renderable instanceof PointPlacemark) {
                PointPlacemark pp = (PointPlacemark) renderable;
                pp.setLabelText(label);
                pp.setEnableDecluttering(true);
            }
        }
    }

    public static void main(String[] args) {
        start("WorldWind Shapefiles", AppFrame.class);
    }
}
