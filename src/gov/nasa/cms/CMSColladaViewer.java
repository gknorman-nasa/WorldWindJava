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
import gov.nasa.worldwind.geom.Vec4;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.ogc.collada.ColladaRoot;
import gov.nasa.worldwind.ogc.collada.impl.ColladaController;
import gov.nasa.worldwind.view.orbit.BasicOrbitView;
import java.io.File;

/**
 * Shows how to load <a href="https://www.khronos.org/collada/">COLLADA</a> 3D models.
 */

/**
 *
 * @author twchoi
 */
public class CMSColladaViewer { 

    private WorldWindow wwd;
    private final String LanderPath = "cms-data/collada_files/lander_test.dae";
    private final String Astronaut1 = "cms-data/collada_files/advance_astro.dae";
    private final String Astronaut2 = "cms-data/collada_files/advance_astro.dae";
    
    public CMSColladaViewer(WorldWindow Wwd)
    {
        setWwd(Wwd);
    }
   
    public void createObjects(String location)
    {
        
        switch (location)
        {
            case "Apollo 11" :
                Position LunarLanderPosition = Position.fromDegrees(0.6875, 23.4993, 0);
                createLanderObject(LunarLanderPosition);

                Position FirstAstronautPosition = Position.fromDegrees(0.6862, 23.4823, 0);
                createFirstAstronaut(FirstAstronautPosition);
                 
                Position SecondAstronautPosition = Position.fromDegrees(0.6623, 23.4933, 0);
                createSecondAstronaut(SecondAstronautPosition);
                break;
            case "Apollo 12" :
                LunarLanderPosition = Position.fromDegrees(-3.01, -23.43, 0);
                createLanderObject(LunarLanderPosition);

                FirstAstronautPosition = Position.fromDegrees(-3.06, -23.43, 0);
                createFirstAstronaut(FirstAstronautPosition);
                 
                SecondAstronautPosition = Position.fromDegrees(-2.95, -23.43, 0);
                createSecondAstronaut(SecondAstronautPosition);
                break;
            case "Apollo 14" :
                LunarLanderPosition = Position.fromDegrees(-3.66, -17.4786, 0);
                createLanderObject(LunarLanderPosition);

                FirstAstronautPosition = Position.fromDegrees(-3.6720, -17.4453, 0);
                createFirstAstronaut(FirstAstronautPosition);
                 
                SecondAstronautPosition = Position.fromDegrees(-3.6845, -17.4853, 0);
                createSecondAstronaut(SecondAstronautPosition);
                break;
            case "Apollo 15" :
                LunarLanderPosition = Position.fromDegrees(26.1008, 3.6527, 0);
                createLanderObject(LunarLanderPosition);

                FirstAstronautPosition = Position.fromDegrees(26.0970, 3.6017, 0);
                createFirstAstronaut(FirstAstronautPosition);
                 
                SecondAstronautPosition = Position.fromDegrees(26.118, 3.6987, 0);
                createSecondAstronaut(SecondAstronautPosition);
                break;
            case "Apollo 16" :
                LunarLanderPosition = Position.fromDegrees(-8.9913, 15.5144, 0);
                createLanderObject(LunarLanderPosition);

                FirstAstronautPosition = Position.fromDegrees(-8.9899, 15.4944, 0);
                createFirstAstronaut(FirstAstronautPosition);
                 
                SecondAstronautPosition = Position.fromDegrees(-8.9999, 15.5344, 0);
                createSecondAstronaut(SecondAstronautPosition);
                break;
            case "Apollo 17" : 
                LunarLanderPosition = Position.fromDegrees(20.1653, 30.7658, 0);
                createLanderObject(LunarLanderPosition);

                FirstAstronautPosition = Position.fromDegrees(20.1640, 30.7255, 0);
                createFirstAstronaut(FirstAstronautPosition);
                 
                SecondAstronautPosition = Position.fromDegrees(20.12, 30.7461, 0);
                createSecondAstronaut(SecondAstronautPosition);;
                break;
            default :
                break;          
        }
              
    }
    
    public void createLanderObject(Position landerPos)
    {
        // Lunar Lander 3D Object
        File ColladaFile = new File(LanderPath);       
        String layerName = "Lunar Lander";
        setColladaProperties(layerName, landerPos, ColladaFile, new Vec4(600,600,600,600));
    }
    
    public void createFirstAstronaut(Position firstAstroPos)
    {
        // Astronaut 1 3D Object
        File ColladaFile2 = new File(Astronaut1);
        String layerName = "Astronaut 1";
        setColladaProperties(layerName, firstAstroPos, ColladaFile2, new Vec4(75,75,75,75));
    }
    
    public void createSecondAstronaut(Position secondAstroPos)
    {   
        // Astronaut 2 3D Object
        File ColladaFile3 = new File(Astronaut2);
        String layerName = "Astronaut 2";
        setColladaProperties(layerName, secondAstroPos, ColladaFile3, new Vec4(75,75,75,75)); 
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
    
    public void removeColladaObjects()
    {
        String[] ColladaLayers = 
        {
            "Lunar Lander", "Astronaut 1", "Astronaut 2"
        };
        for (String layer : ColladaLayers)
        {
            Layer selectedLayer = getWwd().getModel().getLayers().getLayerByName(layer);
            getWwd().getModel().getLayers().remove(selectedLayer);
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

