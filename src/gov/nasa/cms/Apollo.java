/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.nasa.cms;

import gov.nasa.worldwind.*;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.event.*;
import gov.nasa.worldwind.geom.*;
import gov.nasa.worldwind.layers.*;
import gov.nasa.worldwind.pick.*;
import gov.nasa.worldwind.render.*;
import gov.nasa.worldwind.util.*;
import gov.nasa.worldwindx.examples.util.PowerOfTwoPaddedImage;

import com.jogamp.opengl.*;
import static gov.nasa.worldwindx.examples.ApplicationTemplate.insertBeforeCompass;
import javax.swing.*;
import javax.swing.Box;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.net.URL;


/**
 *
 * @author twchoi
 */
public class Apollo 
{
    @SuppressWarnings("unchecked")
    private static class AppFrame extends ApplicationTemplate.AppFrame
    {
        // Above mean sea level globe annotation
        private class AMSLGlobeAnnotation extends GlobeAnnotation
        {
            public AMSLGlobeAnnotation(String text, Position position)
            {
                super(text, position);
            }

            public Vec4 getAnnotationDrawPoint(DrawContext dc)
            {
                return dc.getGlobe().computePointFromPosition(this.getPosition().getLatitude(),
                    this.getPosition().getLongitude(),
                    this.getPosition().getElevation() * dc.getVerticalExaggeration());
            }
        }

        private AnnotationLayer layer;
        private Annotation currentAnnotation;

//        // Static
//        private final static PowerOfTwoPaddedImage IMAGE_WWJ_SPLASH =
//            PowerOfTwoPaddedImage.fromPath("images/400x230-splash-nww.png");
//        private final static PowerOfTwoPaddedImage IMAGE_NASA =
//            PowerOfTwoPaddedImage.fromPath("images/32x32-icon-nasa.png");
//        private final static PowerOfTwoPaddedImage IMAGE_EARTH =
//            PowerOfTwoPaddedImage.fromPath("images/32x32-icon-earth.png");
        
        private final static PowerOfTwoPaddedImage APOLLO11 =
            PowerOfTwoPaddedImage.fromPath("images/Apollo11.jpg");
        private final static PowerOfTwoPaddedImage APOLLO12 =
            PowerOfTwoPaddedImage.fromPath("images/Apollo12.jpg");
        private final static PowerOfTwoPaddedImage APOLLO14 =
            PowerOfTwoPaddedImage.fromPath("images/Apollo14.jpg");
        private final static PowerOfTwoPaddedImage APOLLO15 =
            PowerOfTwoPaddedImage.fromPath("images/Apollo15.jpg");
        private final static PowerOfTwoPaddedImage APOLLO16 =
            PowerOfTwoPaddedImage.fromPath("images/Apollo16.jpg");
        private final static PowerOfTwoPaddedImage APOLLO17 =
            PowerOfTwoPaddedImage.fromPath("images/Apollo17.jpg");

        // UI components
        private JTextArea inputTextArea;
        private JCheckBox cbAdjustWidth;
        private JSlider widthSlider, heightSlider;
        private JSlider scaleSlider, opacitySlider, cornerRadiusSlider, borderWidthSlider, stippleFactorSlider;
        private JComboBox cbFontName, cbFontStyle, cbFontSize, cbTextAlign, cbShape, cbLeader;
        private JComboBox cbImage, cbImageRepeat, cbTextEffect;
        private JSlider leaderGapWidthSlider;
        private JSlider imageOpacitySlider, imageScaleSlider, imageOffsetXSlider, imageOffsetYSlider;
        private JSlider offsetXSlider, offsetYSlider;
        private JSlider distanceMinScaleSlider, distanceMaxScaleSlider, distanceMinOpacitySlider;
        private JSlider highlightScaleSlider;
        private JSpinner insetsTop, insetsRight, insetsBottom, insetsLeft;
        private JButton btTextColor, btBackColor, btBorderColor;
        private JComboBox cbTextColorAlpha, cbBackColorAlpha, cbBorderColorAlpha;
        private JButton btApply, btRemove;

        private boolean suspendUpdate = false;
        private Color savedBorderColor;
        private BufferedImage savedImage;

        private Annotation lastPickedObject;

        public AppFrame()
        {
            // Create example annotations
            this.setupAnnotations();


            // Add a select listener to select or highlight annotations on rollover
            this.setupSelection();
        }

        private void setupAnnotations()
        {
            GlobeAnnotation ga;
            this.layer = new AnnotationLayer();
            
            // Create a renderable layer of individual items
            RenderableLayer rl = new RenderableLayer();
            rl.setName("Apollo 11");
            insertBeforeCompass(this.getWwd(), rl);
            ga = this.makeTopImageBottomTextAnnotation(APOLLO11, "Apollo 11 - July 20, 1969. Mare Tranquillitatis", //shows image on screen twchoi
                Position.fromDegrees(0.6875, 23.4333, 0));    
            rl.addRenderable(ga);
            //create a single layer displaying all annotations
            layer.addAnnotation(ga);
            
            rl = new RenderableLayer();
            rl.setName("Apollo 12");
            insertBeforeCompass(this.getWwd(), rl);
            ga = this.makeTopImageBottomTextAnnotation(APOLLO12, "Apollo 12 - November 19, 1969. Oceanus Procellarum", //shows image on screen twchoi
                Position.fromDegrees(-3.1975, -23.3856, 0));
            rl.addRenderable(ga);
            layer.addAnnotation(ga);
            
            rl = new RenderableLayer();
            rl.setName("Apollo 14");
            insertBeforeCompass(this.getWwd(), rl);
            ga = this.makeTopImageBottomTextAnnotation(APOLLO14, "Apollo 14 - February 5, 1971. Fra Mauro Highlands", //shows image on screen twchoi
                Position.fromDegrees(-3.6733, -17.4653, 0));
            rl.addRenderable(ga);
            layer.addAnnotation(ga);
            
            rl = new RenderableLayer();
            rl.setName("Apollo 15");
            insertBeforeCompass(this.getWwd(), rl);
            ga = this.makeTopImageBottomTextAnnotation(APOLLO15, "Apollo 15 - July 30, 1971. Montes Apenninus", //shows image on screen twchoi
                Position.fromDegrees(26.1008, 3.6527, 0));
            rl.addRenderable(ga);
            layer.addAnnotation(ga);
            
            rl = new RenderableLayer();
            rl.setName("Apollo 16");
            insertBeforeCompass(this.getWwd(), rl);
            ga = this.makeTopImageBottomTextAnnotation(APOLLO16, "Apollo 16 - April 20, 1972. Descartes Highlands", //shows image on screen twchoi
                Position.fromDegrees(-8.9913, 15.5144, 0));
            rl.addRenderable(ga);
            layer.addAnnotation(ga);
            
            rl = new RenderableLayer();
            rl.setName("Apollo 17");
            insertBeforeCompass(this.getWwd(), rl);
            ga = this.makeTopImageBottomTextAnnotation(APOLLO17, "Apollo 17 - December 11, 1972. Taurus-Littrow Valley", //shows image on screen twchoi
                Position.fromDegrees(20.1653, 30.7658, 0));
            rl.addRenderable(ga);
            layer.addAnnotation(ga);
            
            // Add layer to the layer list and update the layer panel
            insertBeforeCompass(this.getWwd(), layer);
        }


        public GlobeAnnotation makeTopImageBottomTextAnnotation(PowerOfTwoPaddedImage image, String text,
            Position position)
        {
            // Create annotation
            GlobeAnnotation ga = new GlobeAnnotation(text, position);
            int inset = 10; // pixels
            ga.getAttributes().setInsets(new Insets(image.getOriginalHeight() + inset * 2, inset, inset, inset));
            ga.getAttributes().setImageSource(image.getPowerOfTwoImage());
            ga.getAttributes().setImageOffset(new Point(inset, inset));
            ga.getAttributes().setImageRepeat(AVKey.REPEAT_NONE);
            ga.getAttributes().setImageOpacity(1);
            ga.getAttributes().setSize(new Dimension(image.getOriginalWidth() + inset * 2, 0));
            ga.getAttributes().setAdjustWidthToText(AVKey.SIZE_FIXED);
            ga.getAttributes().setBackgroundColor(Color.WHITE);
            ga.getAttributes().setTextColor(Color.BLACK);
            ga.getAttributes().setTextAlign(AVKey.CENTER);
            ga.getAttributes().setBorderColor(Color.BLACK);
            return ga;
        }

        // --- Selection ---------------------------------------

        private void setupSelection()
        {
            // Add a select listener to select or highlight annotations on rollover
            this.getWwd().addSelectListener(new SelectListener()
            {
                private BasicDragger dragger = new BasicDragger(getWwd());

                public void selected(SelectEvent event)
                {
                    if (event.hasObjects() && event.getTopObject() instanceof Annotation)
                    {
                        // Handle cursor change on hyperlink
                        if (event.getTopPickedObject().getValue(AVKey.URL) != null)
                            ((Component) getWwd()).setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                        else
                            ((Component) getWwd()).setCursor(Cursor.getDefaultCursor());
                    }

                    // Select/unselect on left click on annotations
                    if (event.getEventAction().equals(SelectEvent.LEFT_CLICK))
                    {
                        if (event.hasObjects())
                        {
                            if (event.getTopObject() instanceof Annotation)
                            {
                                // Check for text or url
                                PickedObject po = event.getTopPickedObject();
                                if (po.getValue(AVKey.TEXT) != null)
                                {
                                    System.out.println("Text: \"" + po.getValue(AVKey.TEXT) + "\" Hyperlink: "
                                        + po.getValue(AVKey.URL));
                                    if (po.getValue(AVKey.URL) != null)
                                    {
                                        // Try to launch a browser with the clicked URL
                                        try
                                        {
                                            BrowserOpener.browse(new URL((String) po.getValue(AVKey.URL)));
                                        }
                                        catch (Exception ignore)
                                        {
                                        }
                                    }
                                    if (AppFrame.this.currentAnnotation == event.getTopObject())
                                        return;
                                }
                                // Left click on an annotation - select
                                if (AppFrame.this.currentAnnotation != null)
                                {
                                    // Unselect current
                                    //AppFrame.this.currentAnnotation.getAttributes().setHighlighted(false);
                                    AppFrame.this.currentAnnotation.getAttributes().setBorderColor(
                                        AppFrame.this.savedBorderColor);
                                }
                                if (AppFrame.this.currentAnnotation != event.getTopObject())
                                {
                                    // Select new one if not current one already
                                    AppFrame.this.currentAnnotation = (Annotation) event.getTopObject();
                                    //AppFrame.this.currentAnnotation.getAttributes().setHighlighted(true);
                                    AppFrame.this.savedBorderColor = AppFrame.this.currentAnnotation
                                        .getAttributes().getBorderColor();
                                    AppFrame.this.savedImage = AppFrame.this.currentAnnotation.getAttributes()
                                        .getImageSource() instanceof BufferedImage ?
                                        (BufferedImage) AppFrame.this.currentAnnotation.getAttributes().getImageSource()
                                        : null;
                                    AppFrame.this.currentAnnotation.getAttributes().setBorderColor(Color.YELLOW);
                                }
                                else
                                {
                                    // Clear current annotation
                                    AppFrame.this.currentAnnotation = null; // switch off
                                }
                                
                            }
                            else
                                System.out.println("Left click on " + event.getTopObject());
                        }
                    }
                    // Highlight on rollover
                    else if (event.getEventAction().equals(SelectEvent.ROLLOVER) && !this.dragger.isDragging())
                    {
                        AppFrame.this.highlight(event.getTopObject());
                    }

                }
            });
        }

        private void highlight(Object o)
        {
            // Manage highlighting of Annotations.
            if (this.lastPickedObject == o)
                return; // same thing selected

            // Turn off highlight if on.
            if (this.lastPickedObject != null) // && this.lastPickedObject != this.currentAnnotation)
            {
                this.lastPickedObject.getAttributes().setHighlighted(false);
                this.lastPickedObject = null;
            }

            // Turn on highlight if object selected.
            if (o != null && o instanceof Annotation)
            {
                this.lastPickedObject = (Annotation) o;
                this.lastPickedObject.getAttributes().setHighlighted(true);
            }
        }    
    }
    
    public static void main (String args[])
    {
        ApplicationTemplate.start("Apollo", AppFrame.class);
    }
}
    