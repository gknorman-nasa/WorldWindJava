/*
 * Copyright (C) 2012 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */
package gov.nasa.worldwindx.examples;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.formats.shapefile.DBaseRecord;
import gov.nasa.worldwind.formats.shapefile.ShapefileLayerFactory;
import gov.nasa.worldwind.formats.shapefile.ShapefileRecord;
import gov.nasa.worldwind.formats.shapefile.ShapefileRenderable;
import gov.nasa.worldwind.layers.*;
import gov.nasa.worldwind.render.*;
import gov.nasa.worldwind.util.Logging;
import gov.nasa.worldwind.util.WWIO;
import gov.nasa.worldwindx.applications.sar.SAR2;

import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import javax.imageio.ImageIO;

/**
 * Illustrates how to import ESRI Shapefiles into SARApp. This uses a <code>{@link ShapefileLayerFactory}</code> to
 * parse a Shapefile's contents and convert the shapefile into a Place Names menu in SARApp.
 * 
 * @version $Id: LunarPlaceNamesMenu.java 1171 2020-07-14 21:45:02Z twchoi $
 */
public class LunarPlaceNamesMenu extends JMenu implements ShapefileRenderable.AttributeDelegate
{
    private WorldWindow wwd;
    private boolean isItemEnabled;

    public LunarPlaceNamesMenu(SAR2 sar2, WorldWindow Wwd)
    {
        super("Place Names");      
        
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

                // Add the layer to the WorldWindow's layer list.
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {                       
                        if (isItemEnabled) {
                            Wwd.getModel().getLayers().add(layer);       
                        } 
                    }
                });
            }

            @Override
            public void exception(Exception e) {
                Logging.logger().log(java.util.logging.Level.SEVERE, e.getMessage(), e);
            }
        };
        for (String shapeFile : shapeFiles) 
        {
            // Edit shapefile names to display feature name
            String placeName = shapeFile.substring(shapeFile.lastIndexOf('/') + 1, shapeFile.lastIndexOf('.'));
            JCheckBoxMenuItem mi = new JCheckBoxMenuItem(placeName);
            mi.setMnemonic(placeName.charAt(0));           
            mi.addActionListener(new ActionListener()
            {               
                public void actionPerformed(ActionEvent event)
                {
                    
                    // Enable and disable when clicked 
                    isItemEnabled = ((JCheckBoxMenuItem) event.getSource()).getState();
               
                    if (isItemEnabled) {
                        // Load the shapefile. Define the completion callback.                       
                        factory.createFromShapefileSource(shapeFile, callBack);
                    }
                    else {
                        String chosenName = event.getActionCommand();
                        Layer selectedLayer = Wwd.getModel().getLayers().getLayerByName(chosenName);

                        // Remove chosen layers when disabled
                        switch(chosenName) {
                            case "Oceanus":
                                Wwd.getModel().getLayers().remove(selectedLayer);
                                break;
                            case "Mare":
                                Wwd.getModel().getLayers().remove(selectedLayer);
                                break;
                            case "Mons":
                                Wwd.getModel().getLayers().remove(selectedLayer);
                                break;
                            case "Catena":
                                Wwd.getModel().getLayers().remove(selectedLayer);
                                break;
                            case "Vallis":
                                Wwd.getModel().getLayers().remove(selectedLayer);
                                break;
                            case "Crater":
                                Wwd.getModel().getLayers().remove(selectedLayer);
                                break;
                            case "Dorsum":
                                Wwd.getModel().getLayers().remove(selectedLayer);
                                break;
                            case "Lacus":
                                Wwd.getModel().getLayers().remove(selectedLayer);
                                break;
                            case "Sinus":
                                Wwd.getModel().getLayers().remove(selectedLayer);
                                break;
                            case "Rima":
                                Wwd.getModel().getLayers().remove(selectedLayer);
                                break;
                            case "Promontorium":
                                Wwd.getModel().getLayers().remove(selectedLayer);
                                break;
                            case "Palus":
                                Wwd.getModel().getLayers().remove(selectedLayer);
                                break;
                            case "Rupes":
                                Wwd.getModel().getLayers().remove(selectedLayer);
                                break;
                            case "LandingSiteName":
                                Wwd.getModel().getLayers().remove(selectedLayer);
                                break;
                            case "Planitia":
                                Wwd.getModel().getLayers().remove(selectedLayer);
                                break;
                            case "Albedo":
                                Wwd.getModel().getLayers().remove(selectedLayer);
                                break;
                            case "Satellite":
                                Wwd.getModel().getLayers().remove(selectedLayer);
                                break;
                        }
                    }        
                    doClick(0); // keep layer menu open
                }
            });
            
            this.add(mi);          
        }
    }
    @Override
    public void assignAttributes(ShapefileRecord shapefileRecord, ShapefileRenderable.Record renderableRecord) {
    }

    @Override
    public void assignRenderableAttributes(ShapefileRecord shapefileRecord, Renderable renderable) {
        if (isItemEnabled) {
            DBaseRecord attrs = shapefileRecord.getAttributes();
            String label = attrs.getValue("name").toString();
            if (renderable instanceof PointPlacemark) {
                PointPlacemark pp = (PointPlacemark) renderable;
                pp.setLabelText(label);
                pp.setEnableDecluttering(true);
            }
        }       
    }
    public WorldWindow getWwd()
    {
        return wwd;
    }

    public void setWwd(WorldWindow wwd)
    {

    }
    
}
