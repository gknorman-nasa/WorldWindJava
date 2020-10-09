/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.nasa.cms.features;

import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Vec4;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.ogc.collada.ColladaRoot;
import gov.nasa.worldwind.ogc.collada.impl.ColladaController;
import gov.nasa.worldwind.view.orbit.BasicOrbitView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JCheckBoxMenuItem;

/**
 *
 * @author twchoi
 */
public class SatelliteObject extends JCheckBoxMenuItem {
    
    private WorldWindow wwd;
    private boolean isItemEnabled;
    private final String soyuz = "cms-data/collada_files/soyuz.dae";

    
    public SatelliteObject(WorldWindow Wwd)
    {
        super("Satellite");
        
        this.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {
                isItemEnabled = ((JCheckBoxMenuItem) event.getSource()).getState();

                if (isItemEnabled)
                {
                    setWwd(Wwd); //sets Wwd to Wwd parameter from CelestialMapper
                    createSatellite();
                    zoomTo(LatLon.fromDegrees(0.6875, 23.4993), Angle.fromDegrees(40), Angle.fromDegrees(0), 350000);
                    
                } else
                {
                    removeSatellite();
                }
            }
        });
        
    }
   
    
    public void createSatellite()
    {
        // Lunar Lander 3D Object
        File ColladaFile = new File(soyuz);       
        String layerName = "Soyuz Satellite";
        Position soyuzPosition = Position.fromDegrees(0.6875, 23.4993, 300000);
        setColladaProperties(layerName, soyuzPosition, ColladaFile, new Vec4(2000,2000,2000,2000));
    }
    
    public void setColladaProperties(String layerName, Position objectPosition, Object colladaSource, Vec4 objectScale)
    {
        try {
        ColladaRoot colladaRoot = ColladaRoot.createAndParse(colladaSource);
        colladaRoot.setPosition(objectPosition);
        colladaRoot.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);
        colladaRoot.setModelScale(objectScale);
        //Angle pitch in parameter
        //colladaRoot.setPitch(pitch);
        
        // Create a ColladaController to adapt the ColladaRoot to the WorldWind renderable interface.
        ColladaController colladaController = new ColladaController(colladaRoot);
        
        // Adds a new layer containing the ColladaRoot to the end of the WorldWindow's layer list.
        RenderableLayer layer = new RenderableLayer();
        layer.setName(layerName);
        layer.addRenderable(colladaController);
        getWwd().getModel().getLayers().add(layer);
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
    
    public void removeSatellite()
    {
        String[] ColladaLayers = 
        {
            "Soyuz Satellite"
        };
        for (String layer : ColladaLayers)
        {
            Layer selectedLayer = getWwd().getModel().getLayers().getLayerByName(layer);
            getWwd().getModel().getLayers().remove(selectedLayer);
        }
        zoomTo(LatLon.fromDegrees(0, 0), Angle.fromDegrees(0), Angle.fromDegrees(0), 8e6);
    }
    
    
    protected void zoomTo(LatLon latLon, Angle heading, Angle pitch, double zoom)
    {
        BasicOrbitView view = (BasicOrbitView) getWwd().getView();
        view.stopMovement();
        view.addPanToAnimator(new Position(latLon, 300000), heading, pitch, zoom, true);
    }

    public void setWwd(WorldWindow Wwd)
    {
        this.wwd = Wwd;
    }

    public WorldWindow getWwd()
    {
        return this.wwd;
    }
}
