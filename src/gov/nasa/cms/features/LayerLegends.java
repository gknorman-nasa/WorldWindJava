/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.nasa.cms.features;

import gov.nasa.cms.CelestialMapper;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.ScreenImage;
import gov.nasa.worldwind.render.Size;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Kaitlyn Dickinson
 */
public class LayerLegends {

    protected WorldWindow wwd;

    public LayerLegends(WorldWindow wwd) {
        this.wwd = wwd;
        setupLegends();
    }

    protected void setupLegends() {
        final ScreenImage lolaSteelLegend = new ScreenImage();
        final ScreenImage lolaColorLegend = new ScreenImage();
        RenderableLayer legendLayer = new RenderableLayer();
        RenderableLayer colorLegend = new RenderableLayer();

        for (Layer layer : getWwd().getModel().getLayers()) {
            {
                if (layer.getName().equals("LOLA Color Shaded Relief Blue Steel")) {
                    try {
                         if (layer.isEnabled()) {

                        lolaSteelLegend.setImageSource(ImageIO.read(new File("cms-data/images/lolasteel-legend.png")));
                        Rectangle view = getWwd().getView().getViewport();
                        // Set the screen location to different points to offset the image size
                        lolaSteelLegend.setSize(new Size(Size.EXPLICIT_DIMENSION, 432, AVKey.PIXELS, Size.EXPLICIT_DIMENSION, 69, AVKey.PIXELS));

                        lolaSteelLegend.setScreenLocation(new Point(view.x + 950, view.y + 710));

                        legendLayer.addRenderable(lolaSteelLegend);
                        legendLayer.setName("LOLA Steel Legend");

                        getWwd().getModel().getLayers().add(legendLayer);
                         } else {
                          getWwd().getModel().getLayers().remove(legendLayer);
                         }

                    } catch (IOException ex) {
                        Logger.getLogger(CelestialMapper.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    //if (layer.getName().equals("LOLA Color Shaded Relief")) {
                    try {
                         if (layer.isEnabled()) {

                        lolaColorLegend.setImageSource(ImageIO.read(new File("cms-data/images/lolacolor-legend.png")));
                        Rectangle view = getWwd().getView().getViewport();
                        // Set the screen location to different points to offset the image size
                        // lolaColorLegend.setSize(new Size(Size.EXPLICIT_DIMENSION, 432, AVKey.PIXELS, Size.EXPLICIT_DIMENSION, 69, AVKey.PIXELS));

                        lolaColorLegend.setScreenLocation(new Point(view.x + 1150, view.y + 670));

                        colorLegend.addRenderable(lolaColorLegend);
                        colorLegend.setName("LOLA Color Legend");

                        getWwd().getModel().getLayers().add(colorLegend);
                        

                         } else {
                          getWwd().getModel().getLayers().remove(legendLayer);
                         }

                    } catch (IOException ex) {
                        Logger.getLogger(CelestialMapper.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
        }
    }
    
    public WorldWindow getWwd()
    {
        return this.wwd;
    }
}
