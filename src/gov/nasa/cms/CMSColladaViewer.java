/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.nasa.cms;

import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Position;
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
 * Shows how to load <a href="https://www.khronos.org/collada/">COLLADA</a> 3D models.
 */

/**
 *
 * @author twchoi
 */
public class CMSColladaViewer extends JCheckBoxMenuItem {

    private WorldWindow wwd;
    //private AnnotationLayer layer;
    
    private boolean isItemEnabled;
    
    public CMSColladaViewer(WorldWindow Wwd)
    {
        super("3D Objects");
        setWwd(Wwd);
        
        this.addActionListener(new ActionListener(){
            
            public void actionPerformed(ActionEvent event)
            {
                isItemEnabled = ((JCheckBoxMenuItem) event.getSource()).getState();
                
                if (isItemEnabled)
                {
                    createObjects();
                    
                    zoomTo(LatLon.fromDegrees(20.1653, 30.7658), Angle.fromDegrees(30), Angle.fromDegrees(70), 3e4);
 
                }
                else 
                {
                    String[] ColladaLayers = 
                    {
                        "Lunar Lander", "Astronaut 1", "Astronaut 2"
                    };
                    for (String layer : ColladaLayers)
                    {
                        Layer selectedLayer = Wwd.getModel().getLayers().getLayerByName(layer);
                        Wwd.getModel().getLayers().remove(selectedLayer);
                    }
                    
                    zoomTo(LatLon.fromDegrees(0, 0), Angle.fromDegrees(0), Angle.fromDegrees(0), 8e6);
                    //getWwd().getView().setEyePosition(new Position(Angle.fromDegreesLatitude(0), Angle.fromDegreesLongitude(0), 8e6));
                }
            }
    
        });
        
        
    }
   
    protected void createObjects()
    {
        // Lunar Lander 3D Object
        Position LunarLanderPosition = Position.fromDegrees(20.1653, 30.7658, 200);
        File ColladaFile = new File("cms-data/collada_files/lander_test.dae");
        //"testData/collada/collada.dae"
        String layerName = "Lunar Lander";
        setColladaProperties(layerName, LunarLanderPosition, ColladaFile);
       
        
        // Astronaut 1 3D Object
        Position FirstAstronautPosition = Position.fromDegrees(20.1640, 30.7255, 200);
        File ColladaFile2 = new File("testData/collada/cube_triangulate.dae");
        layerName = "Astronaut 1";
        setColladaProperties(layerName, FirstAstronautPosition, ColladaFile2);

        
        // Astronaut 2 3D Object
        Position SecondAstronautPosition = Position.fromDegrees(20.12, 30.7461, 200);
        File ColladaFile3 = new File("testData/collada/duck_triangulate.dae");
        layerName = "Astronaut 2";
        setColladaProperties(layerName, SecondAstronautPosition, ColladaFile3);  
        
    }
    
    protected void setColladaProperties(String layerName, Position objectPosition, Object colladaSource)
    {
        try {
        ColladaRoot colladaRoot = ColladaRoot.createAndParse(colladaSource);
        colladaRoot.setPosition(objectPosition);
        colladaRoot.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);
        
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
    
    
    protected void zoomTo(LatLon latLon, Angle heading, Angle pitch, double zoom)
    {
        BasicOrbitView view = (BasicOrbitView) getWwd().getView();
        view.stopMovement();
        view.addPanToAnimator(new Position(latLon, 0), heading, pitch, zoom, true);
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

