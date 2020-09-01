/*
 * Copyright (C) 2012 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */
package gov.nasa.cms;

import gov.nasa.worldwind.Configuration;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.*;
import gov.nasa.worldwind.globes.Globe;
import gov.nasa.worldwind.layers.*;
import gov.nasa.worldwind.terrain.LocalElevationModel;
import gov.nasa.worldwind.util.Logging;
import gov.nasa.worldwindx.examples.util.ExampleUtil;
import gov.nasa.worldwindx.examples.util.LayerManagerLayer;
import gov.nasa.cms.features.Apollo;
import gov.nasa.worldwind.WorldWindow;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

/**
 * CelestialMapper.java
 *
 */
public class CelestialMapper extends AppFrame 
{
    protected static final String CMS_LAYER_NAME = "Celestial Shapes";
    protected static final String CLEAR_SELECTION = "CelestialMapper.ClearSelection";
    protected static final String ENABLE_EDIT = "CelestialMapper.EnableEdit";
    protected static final String OPEN = "CelestialMapper.Open";
    protected static final String OPEN_URL = "CelestialMapper.OpenUrl";
    protected static final String REMOVE_SELECTED = "CelestialMapper.RemoveSelected";
    protected static final String SAVE = "CelestialMapper.Save";
    protected static final String SELECTION_CHANGED = "CelestialMapper.SelectionChanged";
    protected static final String ELEVATIONS_PATH = "testData/lunar-dem.tif";

    //**************************************************************//
    //********************  Main  **********************************//
    //**************************************************************//
    ActionListener controller;
    protected RenderableLayer airspaceLayer;
    private CMSPlaceNamesMenu cmsPlaceNamesMenu;
    private ApolloMenu apolloMenu;
    private boolean stereo;
    private JCheckBoxMenuItem stereoCheckBox;

    public void restart() {
        getWwd().shutdown();
        //window.shutdown();
        getContentPane().remove(wwjPanel); //removing component's parent must be JPanel
        this.initialize();
    }

    @Override
    public void initialize() {
        super.initialize();
        getWwd().getModel().getLayers().add(new LayerManagerLayer(getWwd())); // add layer box UI

        // Wait for the elevation to import            
        this.setCursor(new Cursor(Cursor.WAIT_CURSOR));

        // Import the elevation model on a new thread to avoid freezing the UI
        Thread em = new Thread(new Runnable() {
            public void run() {
                importElevations();
                setCursor(Cursor.getDefaultCursor());
            }
        });
        em.start(); // Load the elevation model   
        makeMenuBar(this, this.controller); // Make the menu bar

    }

    /**
     * Causes the View attached to the specified WorldWindow to animate to the specified sector. The View starts
     * animating at its current location and stops when the sector fills the window.
     *
     * @param wwd the WorldWindow who's View animates.
     * @param sector the sector to go to.
     *
     * @throws IllegalArgumentException if either the <code>wwd</code> or the <code>sector</code> are <code>null</code>.
     */
    public static void goTo(WorldWindow wwd, Sector sector) {
        if (wwd == null) {
            String message = Logging.getMessage("nullValue.WorldWindow");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }

        if (sector == null) {
            String message = Logging.getMessage("nullValue.SectorIsNull");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }

        // Create a bounding box for the specified sector in order to estimate its size in model coordinates.
        gov.nasa.worldwind.geom.Box extent = Sector.computeBoundingBox(wwd.getModel().getGlobe(),
                wwd.getSceneController().getVerticalExaggeration(), sector);

        // Estimate the distance between the center position and the eye position that is necessary to cause the sector to
        // fill a viewport with the specified field of view. Note that we change the distance between the center and eye
        // position here, and leave the field of view constant.
        Angle fov = wwd.getView().getFieldOfView();
        double zoom = extent.getRadius() / fov.cosHalfAngle() / fov.tanHalfAngle();

        // Configure OrbitView to look at the center of the sector from our estimated distance. This causes OrbitView to
        // animate to the specified position over several seconds. To affect this change immediately use the following:
        // ((OrbitView) wwd.getView()).setCenterPosition(new Position(sector.getCentroid(), 0d));
        // ((OrbitView) wwd.getView()).setZoom(zoom);
        wwd.getView().goTo(new Position(sector.getCentroid(), 0d), zoom);
    }

    // Creates a local elevation model from ELEVATIONS_PATH and sets the view
    protected void importElevations() {
        try {
            // Download the data and save it in a temp file.
            File sourceFile = ExampleUtil.saveResourceToTempFile(ELEVATIONS_PATH, ".tif");

            // Create a local elevation model from the data.
            final LocalElevationModel elevationModel = new LocalElevationModel();
            elevationModel.addElevations(sourceFile);

            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    // Get current model
                    Globe globe = getWwd().getModel().getGlobe();
                    globe.setElevationModel(elevationModel);

                    // Set the view to look at the imported elevations
                    Sector modelSector = elevationModel.getSector();
                    goTo(getWwd(), modelSector);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Menu bar creation
    public void makeMenuBar(JFrame frame, final ActionListener controller) 
    {
        JMenuBar menuBar = new JMenuBar();


        //======== "CMS Place Names" ========          
        cmsPlaceNamesMenu = new CMSPlaceNamesMenu(this, this.getWwd());
        menuBar.add(cmsPlaceNamesMenu);

        //======== "Tools" ========        
        JMenu tools = new JMenu("Tools");
        {
            JMenuItem tp = new JMenuItem("Terrain Profile");
            tp.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // Add TerrainProfileLayer
                    TerrainProfileLayer tpl = new TerrainProfileLayer();
                    tpl.setEventSource(getWwd());
                    insertBeforeCompass(getWwd(), tpl); // display on screen
                }
            });
            tools.add(tp);

            JMenuItem mp = new JMenuItem("Measurement Panel");
            mp.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                }
            });
            tools.add(mp);
        }
        menuBar.add(tools);

        frame.setJMenuBar(menuBar);

        //======== "View" ========           
        JMenu menu = new JMenu("View");
        {
            

            // Stereo menu item
            // stereo = new CMSStereo(this); //, this.getWwd());
            stereoCheckBox = new JCheckBoxMenuItem("Stereo");
            stereoCheckBox.setSelected(stereo);
            stereoCheckBox.addActionListener((ActionEvent event) -> {
                stereo = !stereo;
                if (stereo) {
                    // Set the stereo.mode property to request stereo. Request red-blue anaglyph in this case. Can also request
                    // "device" if the display device supports stereo directly. To prevent stereo, leave the property unset or set
                    // it to an empty string.
                    System.setProperty("gov.nasa.worldwind.stereo.mode", "redblue");
                    //  Configure the initial view parameters so that the balloons are immediately visible.
                    Configuration.setValue(AVKey.INITIAL_LATITUDE, 20);
                    Configuration.setValue(AVKey.INITIAL_LONGITUDE, 30);
                    Configuration.setValue(AVKey.INITIAL_ALTITUDE, 10e4);
                    Configuration.setValue(AVKey.INITIAL_HEADING, 500);
                    Configuration.setValue(AVKey.INITIAL_PITCH, 80);
                } else {
                    System.setProperty("gov.nasa.worldwind.stereo.mode", "");
                    Configuration.setValue(AVKey.INITIAL_LATITUDE, 0);
                    Configuration.setValue(AVKey.INITIAL_LONGITUDE, 0);
                    Configuration.setValue(AVKey.INITIAL_ALTITUDE, 8e6);
                    Configuration.setValue(AVKey.INITIAL_HEADING, 0);
                    Configuration.setValue(AVKey.INITIAL_PITCH, 0);
                }
                restart();
            });
            menu.add(stereoCheckBox);
        }
        menuBar.add(menu);
    }
}
