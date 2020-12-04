/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.nasa.cms.features;

import gov.nasa.cms.AppFrame;
import gov.nasa.worldwind.Configuration;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.globes.EarthFlat;
import gov.nasa.worldwind.layers.AnnotationLayer;
import gov.nasa.worldwind.render.GlobeAnnotation;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 *
 * @author kjdickin
 */
public class ViewMenu extends JMenu 
{
    private AppFrame frame;
    private WorldWindow wwd;
    private boolean stereo;
    private boolean flat;
    private boolean resetWindow;
    private boolean isChangeEnabled;
    
    private CMSColladaViewer collada;
    
    public ViewMenu(WorldWindow Wwd)
    {
        super("View");
        setupMenu();
    }
    protected void setupMenu()
    {
        //======== "Stereo" ==========
        JCheckBoxMenuItem stereoCheckBox = new JCheckBoxMenuItem("Stereo");
        stereoCheckBox.setSelected(stereo);
        stereoCheckBox.addActionListener((ActionEvent event) ->
        {

            stereo = !stereo;
            if (stereo && !flat)
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
            } else if (stereo && flat)
            {
                //without this else if loop, the canvas glitches               
            } else {
                System.setProperty("gov.nasa.worldwind.stereo.mode", "");
                Configuration.setValue(AVKey.INITIAL_LATITUDE, 0);
                Configuration.setValue(AVKey.INITIAL_LONGITUDE, 0);
                Configuration.setValue(AVKey.INITIAL_ALTITUDE, 8e6);
                Configuration.setValue(AVKey.INITIAL_HEADING, 0);
                Configuration.setValue(AVKey.INITIAL_PITCH, 0);
            }
            frame.restart();
        });
        this.add(stereoCheckBox); 

        //======== "2D Flat Globe" ==========
        JCheckBoxMenuItem flatGlobe = new JCheckBoxMenuItem("2D Flat");
        flatGlobe.setSelected(flat);
        flatGlobe.addActionListener((ActionEvent event) ->
        {
            flat = !flat;
            if (flat)
            {
                Configuration.setValue(AVKey.GLOBE_CLASS_NAME, EarthFlat.class.getName());
            } else 
            {
                Configuration.setValue(AVKey.GLOBE_CLASS_NAME, "gov.nasa.worldwind.globes.Earth");
            }
            frame.restart();
        });
        this.add(flatGlobe);  

        //======== "Reset" =========
        JMenuItem reset = new JMenuItem("Reset");
        reset.setSelected(resetWindow);
        reset.addActionListener((ActionEvent event) ->
        {
            resetWindow = !resetWindow;
            if (resetWindow)
            {
                frame.restart(); //resets window to launch status
            } 
        });
        this.add(reset);

        //======== "Chang'e 5 Landing Site" =========
        JCheckBoxMenuItem change5 = new JCheckBoxMenuItem("Chang'e 5 Landing Site");
        change5.setSelected(isChangeEnabled);
        change5.addActionListener((ActionEvent event) ->
        {
            isChangeEnabled = !isChangeEnabled;
            if (isChangeEnabled)
            {
                collada = new CMSColladaViewer(this.getWwd());
                collada.createChangeLander();

                AnnotationLayer change4Annotation = new AnnotationLayer();
                change4Annotation.setName("Chang'e 5 Landing Site");

                  GlobeAnnotation ga = new GlobeAnnotation("<p>\n<b><font color=\"#664400\">Chang'e 5 Landing Site</font></b><br />\n<i>Landing Date: "
                    + "1, December 2020</i>\n</p>\n"             
                    + "<p><b>Landing Site: </b>Mons Rümker region of Oceanus Procellarum</p>",
                    Position.fromDegrees(43.099, -51.837), Font.decode("Serif-PLAIN-14"), Color.DARK_GRAY);
                                      change4Annotation.addAnnotation(ga);
                ga.getAttributes().setTextAlign(AVKey.RIGHT);
                ga.setMinActiveAltitude(7e3);
                ga.setMaxActiveAltitude(1e7);
                ga.getAttributes().setBackgroundColor(new Color(1f, 1f, 1f, .7f));
                ga.getAttributes().setBorderColor(Color.BLACK);
                ga.getAttributes().setSize(
                    new Dimension(220, 0));  // Preferred max width, no length limit (default max width is 160)
                ga.getAttributes().setImageRepeat(AVKey.REPEAT_NONE);
                ga.getAttributes().setImageOpacity(.6);
                ga.getAttributes().setImageScale(.7);
                ga.getAttributes().setImageOffset(new Point(7, 7));

                getWwd().getModel().getLayers().add(change4Annotation);
            } else 
            {
                collada.removeColladaObjects();

            }
        });
        this.add(change5);            
    }
    
    private WorldWindow getWwd()
    {
        return this.wwd;
    }
    
    public void setWwd(WorldWindow Wwd)
    {
        this.wwd = Wwd;
    }
    
}


