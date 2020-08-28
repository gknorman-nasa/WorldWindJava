/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.nasa.cms;

import gov.nasa.cms.CelestialMapper.AppFrame;
import gov.nasa.worldwind.Configuration;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.*;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwindx.examples.util.LayerManagerLayer;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;

/**
 *
 * @author twchoi
 */
public class CMSStereo extends JCheckBoxMenuItem
{

    private WorldWindow wwd;
//    private ShutdownWindowAction shutdownAction;
//    private CreateWindowAction createWindowAction;
    private boolean isItemEnabled;
    //public static final String APP_NAME = "Celestial Mapping System";
    //private StereoOptionSceneController stereoController;
    private WorldWindowGLCanvas wwc;
    private WorldWindowGLCanvas originalCanvas;
    
    public WorldWindow getWwd()
    {
        return this.wwd;
    }

    public void setWwd(WorldWindow window)
    {
        this.wwd = window;
    }
    
    public CMSStereo(AppFrame cms, WorldWindow window)
    {   
        //JCheckBoxMenuItem item = new JCheckBoxMenuItem("Stereo");
        super("Stereo");

       //window = cms.wwjPanel.getWwd();
       originalCanvas = (WorldWindowGLCanvas) cms.wwjPanel.getWwd();
        
        this.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent event)
            {             
                isItemEnabled = ((JCheckBoxMenuItem) event.getSource()).getState();
                
                

                if (isItemEnabled) {
                    
                    
                    if (wwc == null)
                    {
                        //WorldWind.shutDown();
                        cms.wwjPanel.getWwd().shutdown();
                        //window.shutdown();
                        cms.getContentPane().remove((Component) cms.wwjPanel);
                        //cms.getContentPane().remove((Component) cms.wwjPanel.getWwd());
                        //cms.wwjPanel.wwd = null;
                    }
                    else {
                        wwc.shutdown();
                        cms.getContentPane().remove((Component) wwc);
                        //cms.getContentPane().remove((Component) cms.wwjPanel);
                        wwc = null;
                    }
                    
                     
                    launchStereoView();
                    
                    wwc = new WorldWindowGLCanvas();

                    wwc.setPreferredSize(cms.wwjPanel.getSize());
                    wwc.setModel(window.getModel());
                    //cms.createAppPanel(cms.wwjPanel.getSize(), true);
                    
                    
//                    cms.initialize(true, false, false);
//                    cms.getWwd().getModel().getLayers().add(new LayerManagerLayer(cms.getWwd()));
                    
                    cms.getContentPane().add(wwc, java.awt.BorderLayout.CENTER);
                    //cms.getContentPane().add(cms.getWwjPanel(), java.awt.BorderLayout.CENTER);
                                        
                    //cms.getContentPane().add(wwc.getParent(), java.awt.BorderLayout.WEST);
                    cms.pack();
                    //cms.setEnabled(true);
                                  
                    //this // ApplicationTemplate$AppPanel
                    //this.wwd // WorldWindowGLCanvas
                    //this.wwd.wwd // WorldWindowGLAutoDrawable
                    //this.wwd.wwd.sceneController // StereoOptionSceneController
                    
                    
                    // StereoOptionSceneController();
                     
                    //this.createWorldWindow().wwd.sceneController.setStereoMode(AVKey.STEREO_MODE_RED_BLUE);
                    //this.wwd.wwd.sceneController.setStereoMode(AVKey.STEREO_MODE_RED_BLUE);
                    
                    //ApplicationTemplate.start("Stereo View", CelestialMapper.AppFrame.class); 
                    /* launches a new window with stereo view enabled. 
                     * StereoOptionSceneController.java documentation states that stereo view property must be set before calling the constructor
                    */
                   
                }
                else {
                             
                    //WorldWindowGLCanvas wwc = new WorldWindowGLCanvas();
                    wwc.shutdown();
                    cms.getContentPane().remove((Component) wwc);
                    //cms.getContentPane().remove((Component) cms.wwjPanel);
                    wwc = null;

                    //wwc = cms.wwjPanel.getwWd*();
                    //wwc = window.
                    
                    disableStereo();
//                    cms.initialize(true, false, false);
//                    cms.getWwd().getModel().getLayers().add(new LayerManagerLayer(cms.getWwd()));
//                    cms.importElevations();
                    //wwc = new WorldWindowGLCanvas();
                    
                    wwc = (WorldWindowGLCanvas) cms.wwjPanel.createWorldWindow();
                    //wwc = (WorldWindowGLCanvas) cms.getWwd();
                    //cms.createAppPanel(cms.wwjPanel.getSize(), true);
                    
                    wwc.setPreferredSize(cms.wwjPanel.getSize());
                    wwc.setModel(new BasicModel());
                    
                    
                    //cms.getWwd().getModel().getLayers().add(new LayerManagerLayer(cms.getWwd()));
                    
                    //wwc = (WorldWindowGLCanvas) cms.wwjPanel.createWorldWindow();

                    cms.getContentPane().add(wwc, java.awt.BorderLayout.CENTER);
                    //cms.getContentPane().add((Component) cms.wwjPanel, java.awt.BorderLayout.CENTER);
                    

                    cms.pack();
                    
                }
            }
        });
        
       
        
        
        
    }
    
    private void launchStereoView()
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
               
        //ApplicationTemplate.start("Stereo View", CelestialMapper.AppFrame.class); 
    }
    
    private void disableStereo()
    {
        System.setProperty("gov.nasa.worldwind.stereo.mode", "");
        Configuration.setValue(AVKey.INITIAL_LATITUDE, 20);
        Configuration.setValue(AVKey.INITIAL_LONGITUDE, 30);
        Configuration.setValue(AVKey.INITIAL_ALTITUDE, 10e4);
        Configuration.setValue(AVKey.INITIAL_HEADING, 500);
        Configuration.setValue(AVKey.INITIAL_PITCH, 80);
        //System.setProperty("java.net.useSystemProxies", "true
        
    }
    
//    private void createWindow()
//    {
//        WorldWindowGLCanvas wwc = new WorldWindowGLCanvas();
//        wwc.setPreferredSize(new java.awt.Dimension(800, 600));
//        getContentPane().add(wwc, java.awt.BorderLayout.CENTER);
//        wwc.setModel(new BasicModel());
//        this.wwd = wwc;
//    }
//    
//    private void destroyCurrentWindow()
//    {
//        if (this.wwd != null)
//        {
//            getContentPane().remove((Component) wwd);
//            wwd = null;
//        }
//    }
//
//    private class ShutdownWindowAction extends AbstractAction
//    {
//        public ShutdownWindowAction()
//        {
//            super("Shutdown Window");
//        }
//
//        public void actionPerformed(ActionEvent e)
//        {
//            if (wwd != null)
//            {
//                wwd.shutdown();
//                destroyCurrentWindow();
//                this.setEnabled(false);
//                createWindowAction.setEnabled(true);
//            }
//        }
//    }
//
//    private class CreateWindowAction extends AbstractAction
//    {
//        public CreateWindowAction()
//        {
//            super("Create Window");
//            this.setEnabled(false);
//        }
//
//        public void actionPerformed(ActionEvent e)
//        {
//            createWindow();
//            pack();
//            this.setEnabled(false);
//            shutdownAction.setEnabled(true);
//        }
//    } 
    
    
    
    
//    public static void main(String[] args)
//    {
//        // Set the stereo.mode property to request stereo. Request red-blue anaglyph in this case. Can also request
//        // "device" if the display device supports stereo directly. To prevent stereo, leave the property unset or set
//        // it to an empty string.
//        System.setProperty("gov.nasa.worldwind.stereo.mode", "redblue");
//
//        // Configure the initial view parameters so that the balloons are immediately visible.
//        Configuration.setValue(AVKey.INITIAL_LATITUDE, 46.7045);
//        Configuration.setValue(AVKey.INITIAL_LONGITUDE, -121.6242);
//        Configuration.setValue(AVKey.INITIAL_ALTITUDE, 10e3);
//        Configuration.setValue(AVKey.INITIAL_HEADING, 342);
//        Configuration.setValue(AVKey.INITIAL_PITCH, 80);  
//        ApplicationTemplate.start(APP_NAME, CelestialMapper.AppFrame.class); 
//    }
}
