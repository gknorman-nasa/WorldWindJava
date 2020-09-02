/*
 * Copyright (C) 2012 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */
package gov.nasa.cms;

import gov.nasa.cms.CelestialMapper;
import gov.nasa.worldwind.Configuration;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.*;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Position;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;

/**
 *
 * @author twchoi
 */
public class CMSStereo { // extends JCheckBoxMenuItem {

    // private WorldWindow wwd;
//    private boolean isItemEnabled;
    // private WorldWindowGLCanvas wwc;
    //private WorldWindowGLCanvas originalCanvas;

//    public WorldWindow getWwd()
//    {
//        return this.wwd;
//    }
//
//    public void setWwd(WorldWindow window)
//    {
//        this.wwd = window;
//    }
//    public CMSStereo(CelestialMapper cms) //, WorldWindow window)
//    {
//        super("Stereo");
//
//        this.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent event) {
//                // isItemEnabled = ((JCheckBoxMenuItem) event.getSource()).getState();
//                isItemEnabled = !isItemEnabled;
//
//                if (isItemEnabled) {
//
////                    if (wwc == null)
////                    {
////                        //WorldWind.shutDown();
////                        cms.wwjPanel.getWwd().shutdown();
////                        //window.shutdown();
////                        cms.getContentPane().remove((Component) cms.wwjPanel); //removing component's parent must be JPanel
////                        //cms.getContentPane().remove((Component) cms.wwjPanel.getWwd()); //does not work bc wwd parent is AppPanel, not JPanel
////                        //cms.wwjPanel.wwd = null;
////                    }
////                    else {
////                        wwc.shutdown();
////                        cms.getContentPane().remove((Component) wwc);
////                        //cms.getContentPane().remove((Component) cms.wwjPanel);
////                        wwc = null;
////                    }
//                    launchStereoView();
//                    cms.restart();
//
////                    wwc = new WorldWindowGLCanvas();
////
////                    cms.initialize(); // isItemEnabled, isItemEnabled, isItemEnabled);
////                    wwc.setPreferredSize(cms.wwjPanel.getSize());
////                    wwc.setModel(window.getModel());
////                    
////                    
////                    //below 2 lines initialize layer panel and control layer to original cms window but disables wwd connection with place names
//////                    cms.initialize(true, false, false);
//////                    cms.getWwd().getModel().getLayers().add(new LayerManagerLayer(cms.getWwd()));
////                    
////                    cms.getContentPane().add(wwc, java.awt.BorderLayout.CENTER);
////                    cms.pack();
//                } else {
//
////                    wwc.shutdown();
////                    cms.getContentPane().remove((Component) wwc);
////                    //cms.getContentPane().remove((Component) cms.wwjPanel);
////                    wwc = null;
//                    disableStereo();
//                    cms.restart();
//
////                    cms.initialize(true, false, false);
////                    cms.getWwd().getModel().getLayers().add(new LayerManagerLayer(cms.getWwd()));
////                    cms.importElevations();
////                    wwc = (WorldWindowGLCanvas) cms.wwjPanel.createWorldWindow();
////                    //wwc = (WorldWindowGLCanvas) cms.getWwd();
////                    //cms.createAppPanel(cms.wwjPanel.getSize(), true);
////                    
////                    wwc.setPreferredSize(cms.wwjPanel.getSize());
////                    wwc.setModel(new BasicModel());
////                    cms.getContentPane().add(wwc, java.awt.BorderLayout.CENTER);
////                    cms.pack();
//                }
//            }
//        });
//
//    }
//
//    //set stereo view property in system
//    private void launchStereoView() {
//        // Set the stereo.mode property to request stereo. Request red-blue anaglyph in this case. Can also request
//        // "device" if the display device supports stereo directly. To prevent stereo, leave the property unset or set
//        // it to an empty string.
//        System.setProperty("gov.nasa.worldwind.stereo.mode", "redblue");
//        //  Configure the initial view parameters so that the balloons are immediately visible.
//        Configuration.setValue(AVKey.INITIAL_LATITUDE, 20);
//        Configuration.setValue(AVKey.INITIAL_LONGITUDE, 30);
//        Configuration.setValue(AVKey.INITIAL_ALTITUDE, 10e4);
//        Configuration.setValue(AVKey.INITIAL_HEADING, 500);
//        Configuration.setValue(AVKey.INITIAL_PITCH, 80);
//
//        //ApplicationTemplate.start("Stereo View", CelestialMapper.AppFrame.class); 
//    }
//
//    //disables stereo view property in system
//    private void disableStereo() {
//        System.setProperty("gov.nasa.worldwind.stereo.mode", "");
////        Configuration.setValue(AVKey.INITIAL_LATITUDE, 20);
////        Configuration.setValue(AVKey.INITIAL_LONGITUDE, 30);
////        Configuration.setValue(AVKey.INITIAL_ALTITUDE, 10e4);
////        Configuration.setValue(AVKey.INITIAL_HEADING, 500);
////        Configuration.setValue(AVKey.INITIAL_PITCH, 80);
//
//    }
//
////    public static void main(String[] args)
////    {
////        // Set the stereo.mode property to request stereo. Request red-blue anaglyph in this case. Can also request
////        // "device" if the display device supports stereo directly. To prevent stereo, leave the property unset or set
////        // it to an empty string.
////        System.setProperty("gov.nasa.worldwind.stereo.mode", "redblue");
////
////        // Configure the initial view parameters so that the balloons are immediately visible.
////        Configuration.setValue(AVKey.INITIAL_LATITUDE, 46.7045);
////        Configuration.setValue(AVKey.INITIAL_LONGITUDE, -121.6242);
////        Configuration.setValue(AVKey.INITIAL_ALTITUDE, 10e3);
////        Configuration.setValue(AVKey.INITIAL_HEADING, 342);
////        Configuration.setValue(AVKey.INITIAL_PITCH, 80);  
////        ApplicationTemplate.start(APP_NAME, CelestialMapper.AppFrame.class); 
////    }
}
