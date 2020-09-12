/*
 * Copyright (C) 2012 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */
package gov.nasa.cms;

import gov.nasa.cms.features.ApolloMenu;
import gov.nasa.cms.features.CMSProfile;
import gov.nasa.cms.features.LayerManagerLayer;
import gov.nasa.cms.features.MeasureDialog;
import gov.nasa.cms.features.MoonElevationModel;
import gov.nasa.worldwind.Configuration;
import gov.nasa.worldwind.util.measure.MeasureTool;
import gov.nasa.worldwind.layers.*;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Sector;
import gov.nasa.worldwind.render.ScreenImage;
import gov.nasa.worldwind.util.Logging;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * CelestialMapper.java
 *
 */
public class CelestialMapper extends AppFrame
{
    //**************************************************************//
    //********************  Main  **********************************//
    //**************************************************************//
    ActionListener controller;
    protected RenderableLayer airspaceLayer;
    private CMSPlaceNamesMenu cmsPlaceNamesMenu;
    private ApolloMenu apolloMenu;
    private MoonElevationModel elevationModel;
    private CMSProfile profile;
    private MeasureDialog measureDialog;
    private MeasureTool measureTool;

    private boolean stereo;
    private boolean isMeasureDialogOpen;

    private JCheckBoxMenuItem stereoCheckBox;
    private JCheckBoxMenuItem measurementCheckBox;

    public void restart()
    {
        getWwd().shutdown();
        getContentPane().remove(wwjPanel); //removing component's parent must be JPanel
        this.initialize();
    }

    @Override
    public void initialize()
    {
        super.initialize();
        getWwd().getModel().getLayers().add(new LayerManagerLayer(getWwd())); // add layer box UI

        // Make the menu bar
        makeMenuBar(this, this.controller);

        // Import the lunar elevation data
        elevationModel = new MoonElevationModel(this.getWwd());
        
        // Display the ScreenImage CMS logo as a RenderableLayer
        this.renderLogo();

    }

    /**
     * Causes the View attached to the specified WorldWindow to animate to the
     * specified sector. The View starts animating at its current location and
     * stops when the sector fills the window.
     *
     * @param wwd the WorldWindow who's View animates.
     * @param sector the sector to go to.
     *
     * @throws IllegalArgumentException if either the <code>wwd</code> or the
     * <code>sector</code> are <code>null</code>.
     */
    public static void goTo(WorldWindow wwd, Sector sector)
    {
        if (wwd == null)
        {
            String message = Logging.getMessage("nullValue.WorldWindow");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }

        if (sector == null)
        {
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
            // Terrain Profiler
            profile = new CMSProfile(this.getWwd());
            tools.add(profile);
            menuBar.add(tools);

            // Measure Tool
            measurementCheckBox = new JCheckBoxMenuItem("Measurement");
            measurementCheckBox.setSelected(isMeasureDialogOpen);
            measurementCheckBox.addActionListener((ActionEvent event) ->
            {
                isMeasureDialogOpen = !isMeasureDialogOpen;
                if (isMeasureDialogOpen)
                {
                    // Only open if the MeasureDialog has never been opened
                    if (measureDialog == null)
                    {
                        // Create the dialog from the WorldWindow, MeasureTool and AppFrame
                        measureDialog = new MeasureDialog(getWwd(), measureTool, this);
                    }
                    // Display on screen
                    measureDialog.setVisible(true);
                } else // Hide the dialog
                {
                    measureDialog.setVisible(false);
                }
            });
            tools.add(measurementCheckBox);
        }
        menuBar.add(tools);

        //======== "Apollo" ========      
        apolloMenu = new ApolloMenu(this.getWwd());
        menuBar.add(apolloMenu);

        //======== "View" ========           
        JMenu view = new JMenu("View");
        {
            stereoCheckBox = new JCheckBoxMenuItem("Stereo");
            stereoCheckBox.setSelected(stereo);
            stereoCheckBox.addActionListener((ActionEvent event) ->
            {
                stereo = !stereo;
                if (stereo)
                {
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
                } else
                {
                    System.setProperty("gov.nasa.worldwind.stereo.mode", "");
                    Configuration.setValue(AVKey.INITIAL_LATITUDE, 0);
                    Configuration.setValue(AVKey.INITIAL_LONGITUDE, 0);
                    Configuration.setValue(AVKey.INITIAL_ALTITUDE, 8e6);
                    Configuration.setValue(AVKey.INITIAL_HEADING, 0);
                    Configuration.setValue(AVKey.INITIAL_PITCH, 0);
                }
                restart();
            });
            view.add(stereoCheckBox);
        }
        menuBar.add(view);
        frame.setJMenuBar(menuBar);
    }

    // Renders the logo for CMS in the northwest corner of the screen 
    private void renderLogo()
    {
        final ScreenImage cmsLogo = new ScreenImage();

        try
        {
            cmsLogo.setImageSource(ImageIO.read(new File("src/gov/nasa/cms/images/cms-logo.png")));
            Rectangle view = getWwd().getView().getViewport();
            // Set the screen location to different points to offset the image size
            cmsLogo.setScreenLocation(new Point(view.x + 55, view.y + 70));
        } catch (IOException ex) 
        {
            Logger.getLogger(CelestialMapper.class.getName()).log(Level.SEVERE, null, ex);
        }

        RenderableLayer layer = new RenderableLayer();
        layer.addRenderable(cmsLogo);
        layer.setName("Logo");

        getWwd().getModel().getLayers().add(layer);
    }
}
