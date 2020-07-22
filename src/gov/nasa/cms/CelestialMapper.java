/*
 * Copyright (C) 2012 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */
package gov.nasa.cms;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.geom.*;
import gov.nasa.worldwind.globes.Globe;
import gov.nasa.worldwind.layers.*;
import gov.nasa.worldwind.terrain.LocalElevationModel;
import gov.nasa.worldwindx.examples.util.ExampleUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

/**
 * TO DO
 *
 */
public class CelestialMapper extends ApplicationTemplate {

    protected static final String CMS_LAYER_NAME = "Celestial Shapes";
    protected static final String CLEAR_SELECTION = "CelestialMapper.ClearSelection";
    protected static final String SIZE_NEW_SHAPES_TO_VIEWPORT = "CelestialMapper.SizeNewShapesToViewport";
    protected static final String ENABLE_EDIT = "CelestialMapper.EnableEdit";
    protected static final String OPEN = "CelestialMapper.Open";
    protected static final String OPEN_URL = "CelestialMapper.OpenUrl";
    protected static final String OPEN_DEMO_AIRSPACES = "CelestialMapper.OpenDemoAirspaces";
    protected static final String NEW_AIRSPACE = "CelestialMapper.NewAirspace";
    protected static final String REMOVE_SELECTED = "CelestialMapper.RemoveSelected";
    protected static final String SAVE = "CelestialMapper.Save";
    protected static final String SELECTION_CHANGED = "CelestialMapper.SelectionChanged";
    protected static final String ELEVATIONS_PATH = "testData/lunar-dem.tif";

    //**************************************************************//
    //********************  Main  **********************************//
    //**************************************************************//
    protected static class AppFrame extends ApplicationTemplate.AppFrame 
    {
        ActionListener controller;
        protected RenderableLayer airspaceLayer;
        private CMSPlaceNamesMenu cmsPlaceNamesMenu;
        private WorldWindow wwd;

        
        public AppFrame() 
        {

            /* LOCAL ELEVATION MODEL */
            // Give a few seconds for the elevation to import
            this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            
            // Import the elevation model on a new thread to avoid freezing the UI
            Thread em = new Thread(new Runnable()
            {
                public void run()
                {
                    importElevations();

                    // Restore the cursor.
                    setCursor(Cursor.getDefaultCursor());
                }
            });
            em.start(); // Load the elevation model   
            makeMenuBar(this, this.controller);
        }
        
        
        // Creates a local elevation model from ELEVATIONS_PATH and sets the view
        protected void importElevations()
        {
            try
            {
                // Download the data and save it in a temp file.
                File sourceFile = ExampleUtil.saveResourceToTempFile(ELEVATIONS_PATH, ".tif");

                // Create a local elevation model from the data.
                final LocalElevationModel elevationModel = new LocalElevationModel();
                elevationModel.addElevations(sourceFile);

                SwingUtilities.invokeLater(new Runnable()
                {
                    public void run()
                    {
                        // Get current model
                        Globe globe = AppFrame.this.getWwd().getModel().getGlobe();
                        globe.setElevationModel(elevationModel);

                        // Set the view to look at the imported elevations, although they might be hard to detect. To
                        // make them easier to detect, replace the globe's CompoundElevationModel with the new elevation
                        // model rather than adding it.
                        Sector modelSector = elevationModel.getSector();
                        ExampleUtil.goTo(getWwd(), modelSector);
                    }
                });
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }



        public void makeMenuBar(JFrame frame, final ActionListener controller) {
            JMenuBar menuBar = new JMenuBar();
            final JCheckBoxMenuItem resizeNewShapesItem;
            final JCheckBoxMenuItem enableEditItem;
            
            //======== "File" ========
            
            JMenu menu = new JMenu("File");
            {
                JMenuItem item = new JMenuItem("Open...");
                item.setAccelerator(KeyStroke.getKeyStroke(
                        KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
                item.setActionCommand(OPEN);
                item.addActionListener(controller);
                menu.add(item);

                item = new JMenuItem("Open URL...");
                item.setActionCommand(OPEN_URL);
                item.addActionListener(controller);
                menu.add(item);
                
                item = new JMenuItem("Import Imagery");
                item.setActionCommand(OPEN_URL);
                item.addActionListener(controller);
                menu.add(item);

                item = new JMenuItem("Save...");
                item.setAccelerator(KeyStroke.getKeyStroke(
                        KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
                item.setActionCommand(SAVE);
                item.addActionListener(controller);
                menu.add(item);

                menu.addSeparator();

                item = new JMenuItem("Load Demo Shapes");
                item.setActionCommand(OPEN_DEMO_AIRSPACES);
                item.addActionListener(controller);
                menu.add(item);
            }
            menuBar.add(menu);

            //======== "Shape" ========
            
            menu = new JMenu("Shape");
            {


            }
            menuBar.add(menu);

            //======== "Selection" ========
            
            menu = new JMenu("Selection");
            {
                JMenuItem item = new JMenuItem("Deselect");
                item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,
                        Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
                item.setActionCommand(CLEAR_SELECTION);
                item.addActionListener(controller);
                menu.add(item);

                item = new JMenuItem("Delete");
                item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
                item.setActionCommand(REMOVE_SELECTED);
                item.addActionListener(controller);
                menu.add(item);
            }
            menuBar.add(menu);

            frame.setJMenuBar(menuBar);


            
            //======== "CMS Place Names" ========
            
            cmsPlaceNamesMenu = new CMSPlaceNamesMenu(this, this.getWwd());
            {
                cmsPlaceNamesMenu.setMnemonic('P');
            }
          
            menuBar.add(cmsPlaceNamesMenu);

            
            //======== "View" ========
            
            menu = new JMenu("View");
            {
                JCheckBoxMenuItem item = new JCheckBoxMenuItem("Scale Bar");
                menu.add(item);
                
                item = new JCheckBoxMenuItem("Terrain Profile");
                menu.add(item);
                
                menu.doClick(0);
            }
            menuBar.add(menu);
            
            menu = new JMenu("Annotation");
            {
                JMenuItem item = new JMenuItem("New Annotation");
               
                item.setMnemonic('N');
                menu.add(item);
                
                item = new JMenuItem("Remove Annotation");
                item.setMnemonic('R');

                menu.add(item);
                
                JCheckBoxMenuItem annotationsMenuItem = new JCheckBoxMenuItem("Show Annotations");
                annotationsMenuItem.setMnemonic('A');
                
                menu.add(annotationsMenuItem);
                menu.doClick(0);

                       
            }
            menuBar.add(menu);
            this.cmsPlaceNamesMenu.setWwd(this.wwd);
        }
    }

    public static void main(String[] args) 
    {
        ApplicationTemplate.start("Celestial Mapper System", AppFrame.class);
    }
 }
