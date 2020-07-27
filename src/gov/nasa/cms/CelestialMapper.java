/*
 * Copyright (C) 2012 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */
package gov.nasa.cms;

import gov.nasa.cms.features.MeasureToolPanel;
import gov.nasa.cms.features.MeasureToolUsage;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.geom.*;
import gov.nasa.worldwind.globes.Globe;
import gov.nasa.worldwind.layers.*;
import gov.nasa.worldwind.terrain.LocalElevationModel;
import gov.nasa.worldwindx.applications.sar.ControlPanel;
import gov.nasa.worldwindx.applications.sar.SARAnnotationSupport;
import gov.nasa.worldwindx.applications.sar.WWPanel;
import gov.nasa.worldwindx.examples.util.ExampleUtil;
import gov.nasa.worldwindx.examples.util.LayerManagerLayer;
import gov.nasa.worldwind.util.measure.*;
import gov.nasa.worldwind.geom.LatLon;
import static gov.nasa.worldwindx.examples.ApplicationTemplate.insertBeforePlacenames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.util.ArrayList;
import javax.swing.event.ChangeEvent;
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
        private ControlPanel controlPanel;
        private SARAnnotationSupport annotationSupport;
        private WWPanel wwPanel;
        private PropertyChangeListener measureToolListener = new MeasureToolListener(); 
         private int lastTabIndex = -1;
        private final JTabbedPane tabbedPane = new JTabbedPane();
        private final TerrainProfileLayer profile = new TerrainProfileLayer();
        
        public AppFrame() 
        { 

            //super(true, false, false); // disable layer menu and statisics panel for AppFrame
            //getWwd().getModel().getLayers().add(new LayerManagerLayer(getWwd())); // add layer box UI
            
            // Wait for the elevation to impor            
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

                item = new JMenuItem("Save...");
                item.setAccelerator(KeyStroke.getKeyStroke(
                        KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
                item.setActionCommand(SAVE);
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
                        tpl.setStartLatLon(LatLon.fromDegrees(60, 40)); // not working? 
                        tpl.setEndLatLon(LatLon.fromDegrees(40, 65));
                        ApplicationTemplate.insertBeforeCompass(getWwd(), tpl); // display on screen
                    }
                });
                tools.add(tp);
                
                JMenuItem mp = new JMenuItem("Measurement Panel");
                mp.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                        // Add terrain profile layer
                        profile.setEventSource(getWwd());
                        profile.setFollow(TerrainProfileLayer.FOLLOW_PATH);
                        profile.setShowProfileLine(false);
                        insertBeforePlacenames(getWwd(), profile);

                        // Add + tab
                        tabbedPane.add(new JPanel());
                        tabbedPane.setTitleAt(0, "+");
                        tabbedPane.addChangeListener((ChangeEvent changeEvent) -> 
                        {
                            if (tabbedPane.getSelectedIndex() == 0) 
                            {
                                // Add new measure tool in a tab when '+' selected
                                MeasureTool measureTool = new MeasureTool(getWwd());
                                measureTool.setController(new MeasureToolController());
                                tabbedPane.add(new MeasureToolPanel(getWwd(), measureTool));
                                tabbedPane.setTitleAt(tabbedPane.getTabCount() - 1, "" + (tabbedPane.getTabCount() - 1));
                                tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
                                switchMeasureTool();
                            } 
                            else 
                            {
                                switchMeasureTool();
                            }
                        });

                        // Add measure tool control panel to tabbed pane
                        MeasureTool measureTool = new MeasureTool(getWwd());
                        measureTool.setController(new MeasureToolController());
                        tabbedPane.add(new MeasureToolPanel(getWwd(), measureTool));
                        tabbedPane.setTitleAt(1, "1");
                        tabbedPane.setSelectedIndex(1);
                        switchMeasureTool();

                        getControlPanel().add(tabbedPane, BorderLayout.EAST);
                        pack();
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

            }
            menuBar.add(menu);
                       
            //======== "Apollo" ========          
            /* This menu likely will have to take a similar 
            approach to how the place names are done when revisited */
            
            JMenu apolloMenu = new JMenu();
            {
                apolloMenu.setText("Apollo");

                //---- "Apollo Annotation..." ----
                JMenuItem newAnnotation = new JMenuItem();
                newAnnotation.setText("Annotation");
                newAnnotation.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
                    Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
                newAnnotation.addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent e)
                    {
                    }
                });
                apolloMenu.add(newAnnotation);

            }
            menuBar.add(apolloMenu);
            
            this.cmsPlaceNamesMenu.setWwd(this.wwd); //sets window for place names        
        }
        
        public void makeMeasurementFrame(JFrame frame)
        {
            
        }
        
            private class MeasureToolListener implements PropertyChangeListener {

            @Override
            public void propertyChange(PropertyChangeEvent event) {
                // Measure shape position list changed - update terrain profile
                if (event.getPropertyName().equals(MeasureTool.EVENT_POSITION_ADD)
                        || event.getPropertyName().equals(MeasureTool.EVENT_POSITION_REMOVE)
                        || event.getPropertyName().equals(MeasureTool.EVENT_POSITION_REPLACE)) {
                    updateProfile(((MeasureTool) event.getSource()));
                }
            }
        }

        private void switchMeasureTool() {
            // Disarm last measure tool when changing tab and switching tool
            if (lastTabIndex != -1) {
                MeasureTool mt = ((MeasureToolPanel) tabbedPane.getComponentAt(lastTabIndex)).getMeasureTool();
                mt.setArmed(false);
                mt.removePropertyChangeListener(measureToolListener);
            }
            // Update terrain profile from current measure tool
            lastTabIndex = tabbedPane.getSelectedIndex();
            MeasureTool mt = ((MeasureToolPanel) tabbedPane.getComponentAt(lastTabIndex)).getMeasureTool();
            mt.addPropertyChangeListener(measureToolListener);
            updateProfile(mt);
        }

        private void updateProfile(MeasureTool mt) {
            ArrayList<? extends LatLon> positions = mt.getPositions();
            if (positions != null && positions.size() > 1) {
                profile.setPathPositions(positions);
                profile.setEnabled(true);
            } else {
                profile.setEnabled(false);
            }

            getWwd().redraw();
        }


    }
 }
