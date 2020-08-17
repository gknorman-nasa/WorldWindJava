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
import gov.nasa.worldwindx.examples.util.LayerManagerLayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
/**
 * TO DO
 *
 */
public class CelestialMapper
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
    protected static class AppFrame extends ApplicationTemplate.AppFrame 
    {
        ActionListener controller;
        protected RenderableLayer airspaceLayer;
        private CMSPlaceNamesMenu cmsPlaceNamesMenu;
        private WorldWindow wwd;
        private Apollo apollo;
        private CMS_KMLViewer cmsKMLViewer;
        
        public AppFrame() 
        { 
            super(true, false, false); // disable layer menu and statisics panel for AppFrame
            wwd = getWwd();
            getWwd().getModel().getLayers().add(new LayerManagerLayer(getWwd())); // add layer box UI
                        
            // Wait for the elevation to import            
            this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
            
            // Import the elevation model on a new thread to avoid freezing the UI
            Thread em = new Thread(new Runnable()
            {
                public void run()
                {
                    importElevations();
                    setCursor(Cursor.getDefaultCursor());
                }
            });
            em.start(); // Load the elevation model   
            makeMenuBar(this, this.controller); // Make the menu bar
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

                        // Set the view to look at the imported elevations
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

        
        // Menu bar creation
        public void makeMenuBar(JFrame frame, final ActionListener controller) {
            JMenuBar menuBar = new JMenuBar();
                 
            //======== "File" ========
            JMenu menu = new JMenu("File");
            {
                JMenuItem  item = new JMenuItem("Import Imagery");
                item.setActionCommand(OPEN_URL);
                item.addActionListener(controller);
                menu.add(item);
            }
            menuBar.add(menu);
            
            //======== "CMS Place Names" ========          
            cmsPlaceNamesMenu = new CMSPlaceNamesMenu(this, this.getWwd());
            menuBar.add(cmsPlaceNamesMenu);
            
            //======== "Tools" ========        
            JMenu tools = new JMenu("Tools");
            {                
                JMenuItem tp = new JMenuItem("Terrain Profile");
                tp.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        // Add TerrainProfileLayer
                        TerrainProfileLayer tpl = new TerrainProfileLayer();
                        tpl.setEventSource(getWwd());
                        ApplicationTemplate.insertBeforeCompass(getWwd(), tpl); // display on screen
                    }
                });
                tools.add(tp);
                
                JMenuItem mp = new JMenuItem("Measurement Panel");
                mp.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                     }});
                     tools.add(mp);
            }
            menuBar.add(tools);

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
            
            //======== "View" ========           
            menu = new JMenu("View");
            {
                // Apollo menu item
                apollo = new Apollo(this, this.getWwd());              
                menu.add(apollo);
            }
            menuBar.add(menu);
             
            //========== "KML Viewer" ===========
            cmsKMLViewer = new CMS_KMLViewer(this, this.getWwd());
            menuBar.add(cmsKMLViewer);
           
            
            this.cmsPlaceNamesMenu.setWwd(this.wwd); //sets window for place names   
            this.apollo.setWwd(this.wwd); //sets window for apollo annotations
            this.cmsKMLViewer.setWwd(this.wwd);
        }
    }
 }
