/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.nasa.cms.config.Moon;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import gov.nasa.worldwind.View;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.event.SelectEvent;
import gov.nasa.worldwind.event.SelectListener;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Vec4;
import gov.nasa.worldwind.layers.AnnotationLayer;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.pick.PickedObject;
import gov.nasa.worldwind.pick.PickedObjectList;
import gov.nasa.worldwind.render.Annotation;
import gov.nasa.worldwind.render.AnnotationAttributes;
import gov.nasa.worldwind.render.DrawContext;
import gov.nasa.worldwind.render.FrameFactory;
import gov.nasa.worldwind.render.GlobeAnnotation;
import gov.nasa.worldwind.render.PatternFactory;
import gov.nasa.worldwind.render.ScreenAnnotation;
import gov.nasa.worldwind.render.ScreenRelativeAnnotation;
import gov.nasa.worldwind.render.TextRenderer;
import gov.nasa.worldwind.util.BasicDragger;
import gov.nasa.worldwind.util.BrowserOpener;
import gov.nasa.worldwindx.examples.ApplicationTemplate;
import static gov.nasa.worldwindx.examples.ApplicationTemplate.insertBeforeCompass;
import gov.nasa.worldwindx.examples.util.PowerOfTwoPaddedImage;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Illustrates how to use a WorldWind <code>{@link Annotation}</code> to display on-screen information on Apollo landing sites
 * to the user in the form of a text label with a mission image. Annotations are attached to a geographic position or a point on
 * the screen.
 *
 * @author Tyler Choi
 * @version $Id: Apollo.java 2020-07-29 09:47:38Z twchoi $
 */
public class Apollo extends ApplicationTemplate
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

        // Static
        private final static PowerOfTwoPaddedImage IMAGE_WWJ_SPLASH =
            PowerOfTwoPaddedImage.fromPath("images/400x230-splash-nww.png");
        private final static PowerOfTwoPaddedImage IMAGE_NASA =
            PowerOfTwoPaddedImage.fromPath("images/32x32-icon-nasa.png");
        private final static PowerOfTwoPaddedImage IMAGE_EARTH =
            PowerOfTwoPaddedImage.fromPath("images/32x32-icon-earth.png");

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

            // Create a renderable layer
            RenderableLayer rl = new RenderableLayer();
            rl.setName("Annotations (stand alone)");
            insertBeforeCompass(this.getWwd(), rl);

            // Add above ground level annotation with fixed height in real world
            ga = new GlobeAnnotation("AGL Annotation\nElev 1000m", Position.fromDegrees(10, 25, 1000));
            ga.setHeightInMeter(10e3); // ten kilometer hight
            rl.addRenderable(ga);

            // Add above mean sea level annotation with fixed height in real world
            AMSLGlobeAnnotation amsla = new AMSLGlobeAnnotation("AMSL Annotation\nAlt 1000m",
                Position.fromDegrees(10, 20, 1000));
            amsla.setHeightInMeter(10e3); // ten kilometer hight
            rl.addRenderable(amsla);

            // Create an annotation with an image and some text below it
            ga = this.makeTopImageBottomTextAnnotation(IMAGE_WWJ_SPLASH, "Text below image", //shows image on screen twchoi
                Position.fromDegrees(0, -40, 0));
            rl.addRenderable(ga);

            // Create an AnnotationLayer with lots of annotations
            this.layer = new AnnotationLayer();

            // Create default attributes
            AnnotationAttributes defaultAttributes = new AnnotationAttributes();
            defaultAttributes.setCornerRadius(10);
            defaultAttributes.setInsets(new Insets(8, 8, 8, 8));
            defaultAttributes.setBackgroundColor(new Color(0f, 0f, 0f, .5f));
            defaultAttributes.setTextColor(Color.WHITE);
            defaultAttributes.setDrawOffset(new Point(25, 25));
            defaultAttributes.setDistanceMinScale(.5);
            defaultAttributes.setDistanceMaxScale(2);
            defaultAttributes.setDistanceMinOpacity(.5);
            defaultAttributes.setLeaderGapWidth(14);
            defaultAttributes.setDrawOffset(new Point(20, 40));

            // Add some annotations to the layer
            // NOTE: use unicode for annotation text

            // Towns
            AnnotationAttributes townAttr = new AnnotationAttributes();
            townAttr.setDefaults(defaultAttributes);
            townAttr.setFont(Font.decode("Arial-BOLD-12"));
            layer.addAnnotation(new GlobeAnnotation("MONACO", Position.fromDegrees(43.7340, 7.4211, 0), townAttr));
            layer.addAnnotation(new GlobeAnnotation("NICE", Position.fromDegrees(43.696, 7.27, 0), townAttr));
            layer.addAnnotation(new GlobeAnnotation("ANTIBES", Position.fromDegrees(43.5810, 7.1248, 0), townAttr));
            layer.addAnnotation(new GlobeAnnotation("CANNES", Position.fromDegrees(43.5536, 7.0171, 0), townAttr));
            layer.addAnnotation(new GlobeAnnotation("GRASSE", Position.fromDegrees(43.6590, 6.9240, 0), townAttr));
            layer.addAnnotation(new GlobeAnnotation("FREJUS", Position.fromDegrees(43.4326, 6.7356, 0), townAttr));
            layer.addAnnotation(
                new GlobeAnnotation("SAINTE MAXIME", Position.fromDegrees(43.3087, 6.6353, 0), townAttr));
            layer.addAnnotation(
                new GlobeAnnotation("SAINT TROPEZ", Position.fromDegrees(43.2710, 6.6386, 0), townAttr));
            layer.addAnnotation(new GlobeAnnotation("TOULON", Position.fromDegrees(43.1264, 5.9126, 0), townAttr));
            layer.addAnnotation(new GlobeAnnotation("MARSEILLE", Position.fromDegrees(43.2904, 5.3806, 0), townAttr));
            layer.addAnnotation(
                new GlobeAnnotation("AIX EN PROVENCE", Position.fromDegrees(43.5286, 5.4485, 0), townAttr));
            // Special places
            AnnotationAttributes spAttr = new AnnotationAttributes();
            spAttr.setDefaults(defaultAttributes);
            spAttr.setFont(Font.decode("Arial-BOLDITALIC-10"));
            spAttr.setTextColor(Color.YELLOW);
            layer.addAnnotation(new GlobeAnnotation("A\u00e9roport International\nNice C\u00f4te d'Azur",
                Position.fromDegrees(43.6582, 7.2167, 0), spAttr));
            layer.addAnnotation(new GlobeAnnotation("Sophia Antipolis",
                Position.fromDegrees(43.6222, 7.0474, 0), spAttr));

            // Geographical features - use a common default AnnotationAttributes object
            AnnotationAttributes geoAttr = new AnnotationAttributes();
            geoAttr.setDefaults(defaultAttributes);
            geoAttr.setFrameShape(AVKey.SHAPE_NONE);  // No frame
            geoAttr.setFont(Font.decode("Arial-ITALIC-12"));
            geoAttr.setTextColor(Color.GREEN);
            geoAttr.setTextAlign(AVKey.CENTER);
            geoAttr.setDrawOffset(new Point(0, 5)); // centered just above
            geoAttr.setEffect(AVKey.TEXT_EFFECT_OUTLINE);  // Black outline
            geoAttr.setBackgroundColor(Color.BLACK);
            layer.addAnnotation(new GlobeAnnotation("Mont Chauve\nFort militaire\nAlt: 853m",
                Position.fromDegrees(43.7701, 7.2544, 0), geoAttr));
            layer.addAnnotation(new GlobeAnnotation("Mont Agel\nFort militaire\nAlt: 1148m",
                Position.fromDegrees(43.7704, 7.4203, 0), geoAttr));
            layer.addAnnotation(new GlobeAnnotation("Cap Ferrat",
                Position.fromDegrees(43.6820, 7.3290, 0), geoAttr));
            layer.addAnnotation(new GlobeAnnotation("Gorges du Loup",
                Position.fromDegrees(43.7351, 6.9988, 0), geoAttr));
            layer.addAnnotation(new GlobeAnnotation("Cap d'Antibes",
                Position.fromDegrees(43.5526, 7.1297, 0), geoAttr));
            layer.addAnnotation(new GlobeAnnotation("Iles de L\u00e9rins",
                Position.fromDegrees(43.5125, 7.0467, 0), geoAttr));
            layer.addAnnotation(new GlobeAnnotation("Montagne du Cheiron\nAlt: 1778m",
                Position.fromDegrees(43.8149, 6.9669, 0), geoAttr));
            layer.addAnnotation(new GlobeAnnotation("Giens",
                Position.fromDegrees(43.0394, 6.1384, 0), geoAttr));
            layer.addAnnotation(new GlobeAnnotation("Iles de Porquerolles",
                Position.fromDegrees(42.9974, 6.2147, 0), geoAttr));
            layer.addAnnotation(new GlobeAnnotation("Ile du Levent",
                Position.fromDegrees(43.0315, 6.4702, 0), geoAttr));
            layer.addAnnotation(new GlobeAnnotation("Ile de Port Cros",
                Position.fromDegrees(43.0045, 6.3959, 0), geoAttr));
            layer.addAnnotation(new GlobeAnnotation("Montagne Sainte Victoire\nAlt: 1010m",
                Position.fromDegrees(43.5319, 5.6120, 0), geoAttr));
            layer.addAnnotation(new GlobeAnnotation("Sainte Baume\nAlt: 1147m",
                Position.fromDegrees(43.3373, 5.8008, 0), geoAttr));
            layer.addAnnotation(new GlobeAnnotation("Pic de l'Ours\nAlt: 496m",
                Position.fromDegrees(43.4763, 6.9042, 0), geoAttr));

            // Water bodies - ellipse shape and centered text
            AnnotationAttributes waterAttr = new AnnotationAttributes();
            waterAttr.setDefaults(defaultAttributes);
            waterAttr.setFrameShape(AVKey.SHAPE_ELLIPSE);
            waterAttr.setTextAlign(AVKey.CENTER);
            waterAttr.setFont(Font.decode("Arial-ITALIC-12"));
            waterAttr.setTextColor(Color.CYAN);
            waterAttr.setInsets(new Insets(8, 12, 9, 12));
            layer.addAnnotation(new GlobeAnnotation("Lac de Sainte Croix",
                Position.fromDegrees(43.7720, 6.1879, 0), waterAttr));
            layer.addAnnotation(new GlobeAnnotation("Lac de Castillon",
                Position.fromDegrees(43.9008, 6.5348, 0), waterAttr));
            layer.addAnnotation(new GlobeAnnotation("Lac de Serre Pon\u00e7on",
                Position.fromDegrees(44.5081, 6.3242, 0), waterAttr));

            // Longer text, custom colors and text align
            ga = new GlobeAnnotation("Transition Permien-Trias\nDate: 251Ma \nPlus grand \u00e9pisode "
                + "d'extinction massive.",
                Position.fromDegrees(44.0551, 7.1215, 0), Font.decode("Arial-ITALIC-12"), Color.DARK_GRAY);
            ga.getAttributes().setTextAlign(AVKey.RIGHT);
            ga.getAttributes().setBackgroundColor(new Color(.8f, .8f, .8f, .7f));
            ga.getAttributes().setBorderColor(Color.BLACK);
            layer.addAnnotation(ga);

            // With HTML tags and background image no repeat
            ga = new GlobeAnnotation("<p>\n<b><font color=\"#664400\">LA CLAPI\u00c8RE</font></b><br />\n<i>Alt: "
                + "1100-1700m</i>\n</p>\n<p>\n<b>Glissement de terrain majeur</b> dans la haute Tin\u00e9e, sur "
                + "un flanc du <a href=\"http://www.mercantour.eu\">Parc du Mercantour</a>, Alpes Maritimes.\n</p>\n"
                + "<p>\nRisque aggrav\u00e9 d'<b>inondation</b> du village de <i>Saint \u00c9tienne de Tin\u00e9e</i> "
                + "juste en amont.\n</p>",
                Position.fromDegrees(44.2522, 6.9424, 0), Font.decode("Serif-PLAIN-14"), Color.DARK_GRAY);
            ga.setMinActiveAltitude(10e3);
            ga.setMaxActiveAltitude(1e6);
            ga.getAttributes().setTextAlign(AVKey.RIGHT);
            ga.getAttributes().setBackgroundColor(new Color(1f, 1f, 1f, .7f));
            ga.getAttributes().setBorderColor(Color.BLACK);
            ga.getAttributes().setSize(
                new Dimension(220, 0));  // Preferred max width, no length limit (default max width is 160)
            ga.getAttributes().setImageSource(IMAGE_EARTH.getPowerOfTwoImage());
            ga.getAttributes().setImageRepeat(AVKey.REPEAT_NONE);
            ga.getAttributes().setImageOpacity(.6);
            ga.getAttributes().setImageScale(.7);
            ga.getAttributes().setImageOffset(new Point(7, 7));
            layer.addAnnotation(ga);

            // With some border stippling, width and antialias
            ga = new GlobeAnnotation("Latitude: 44.0 N\nLongitude: 7.0 W",
                Position.fromDegrees(44.0000, 7.000, 0), Font.decode("Arial-ITALIC-12"), Color.DARK_GRAY);
            ga.getAttributes().setTextAlign(AVKey.CENTER);
            ga.getAttributes().setBackgroundColor(new Color(.9f, .9f, .8f, .7f));
            ga.getAttributes().setBorderColor(Color.BLACK);
            ga.getAttributes().setBorderWidth(2);
            ga.getAttributes().setBorderStippleFactor(3);
            layer.addAnnotation(ga);

            // With background texture repeat Y
            ga = new GlobeAnnotation("SAHARA DESERT\n\nThe Sahara is technically the world's second largest desert "
                + "after Antarctica.\n\nAt over 9,000,000 square kilometres (3,500,000 sq mi), it covers most parts "
                + "of northern Africa. ", Position.fromDegrees(22, 12, 0), Font.decode("Arial-BOLD-12"));
            ga.getAttributes().setDefaults(defaultAttributes);
            ga.getAttributes().setImageSource(IMAGE_NASA.getPowerOfTwoImage());
            ga.getAttributes().setImageRepeat(AVKey.REPEAT_Y);
            ga.getAttributes().setImageOpacity(.6);
            ga.getAttributes().setImageScale(.7);
            ga.getAttributes().setImageOffset(new Point(1, 1));
            ga.getAttributes().setInsets(new Insets(6, 28, 6, 6));
            layer.addAnnotation(ga);

            // Splash screen with NPOT background texture
            ga = new GlobeAnnotation("Java SDK", Position.fromDegrees(20, 0, 0), Font.decode("Arial-BOLD-14"));
            ga.getAttributes().setTextAlign(AVKey.RIGHT);
            ga.getAttributes().setImageSource(IMAGE_WWJ_SPLASH.getPowerOfTwoImage());
            ga.getAttributes().setImageRepeat(AVKey.REPEAT_NONE);
            ga.getAttributes().setImageScale(.5);    // scale texture to half size
            ga.getAttributes().setSize(new Dimension(200, 115));  // use this dimensions (half texture)
            ga.getAttributes().setAdjustWidthToText(
                AVKey.SIZE_FIXED);  // use strict dimension - dont follow text width
            ga.getAttributes().setCornerRadius(0);
            layer.addAnnotation(ga);

            // With background pattern and forced height
            AnnotationAttributes patternAttr = new AnnotationAttributes();
            patternAttr.setDefaults(defaultAttributes);
            patternAttr.setFont(Font.decode("Arial-BOLD-16"));
            patternAttr.setTextColor(Color.GRAY);
            ga = new GlobeAnnotation("Background patterns...", Position.fromDegrees(10, 100, 0), patternAttr);
            ga.getAttributes().setImageSource(PatternFactory.createPattern(PatternFactory.GRADIENT_VLINEAR,
                new Dimension(32, 128), 1f, Color.WHITE, new Color(0f, 0f, 0f, 0f)));  // White to transparent
            ga.getAttributes().setSize(new Dimension(200, 128));  // force height to 128
            layer.addAnnotation(ga);
            ga = new GlobeAnnotation("Background patterns...", Position.fromDegrees(10, 110, 0), patternAttr);
            ga.getAttributes().setImageSource(PatternFactory.createPattern(PatternFactory.GRADIENT_VLINEAR,
                new Dimension(32, 64), 1f, Color.LIGHT_GRAY, Color.WHITE));  // gray/white
            ga.getAttributes().setSize(new Dimension(200, 128));
            layer.addAnnotation(ga);
            ga = new GlobeAnnotation("Background patterns...", Position.fromDegrees(10, 120, 0), patternAttr);
            ga.getAttributes().setImageSource(PatternFactory.createPattern(PatternFactory.PATTERN_DIAGONAL_UP,
                Color.YELLOW));  // yellow stripes
            ga.getAttributes().setSize(new Dimension(200, 128));
            layer.addAnnotation(ga);

            ga = new GlobeAnnotation("Background patterns...", Position.fromDegrees(0, 100, 0), patternAttr);
            ga.getAttributes().setImageSource(PatternFactory.createPattern(PatternFactory.GRADIENT_HLINEAR,
                new Dimension(256, 32), 1f, Color.WHITE, new Color(0f, 0f, 0f, 0f)));  // White to transparent
            ga.getAttributes().setSize(new Dimension(200, 128));
            layer.addAnnotation(ga);
            ga = new GlobeAnnotation("Background patterns...", Position.fromDegrees(0, 110, 0), patternAttr);
            ga.getAttributes().setImageSource(PatternFactory.createPattern(PatternFactory.GRADIENT_HLINEAR,
                new Dimension(32, 64), 1f, Color.LIGHT_GRAY, Color.WHITE));  // gray/white
            ga.getAttributes().setSize(new Dimension(200, 128));
            layer.addAnnotation(ga);
            ga = new GlobeAnnotation("Background patterns...", Position.fromDegrees(0, 120, 0), patternAttr);
            ga.getAttributes().setImageSource(
                PatternFactory.createPattern(PatternFactory.PATTERN_SQUARES, Color.YELLOW));  // yellow circles
            ga.getAttributes().setSize(new Dimension(200, 128));
            layer.addAnnotation(ga);

            ga = new GlobeAnnotation("Background patterns...", Position.fromDegrees(-10, 100, 0), patternAttr);
            ga.getAttributes().setImageSource(PatternFactory.createPattern(PatternFactory.GRADIENT_HLINEAR,
                new Dimension(16, 16), 1f, Color.BLACK, Color.WHITE));  // Black to white
            ga.getAttributes().setImageRepeat(AVKey.REPEAT_Y);
            ga.getAttributes().setBackgroundColor(Color.WHITE);
            ga.getAttributes().setSize(new Dimension(200, 128));
            layer.addAnnotation(ga);
            ga = new GlobeAnnotation("Background patterns...", Position.fromDegrees(-10, 110, 0), patternAttr);
            ga.getAttributes().setImageSource(PatternFactory.createPattern(PatternFactory.GRADIENT_VLINEAR,
                new Dimension(16, 16), 1f, Color.BLACK, Color.WHITE));  // Black to white
            ga.getAttributes().setImageRepeat(AVKey.REPEAT_X);
            ga.getAttributes().setBackgroundColor(Color.WHITE);
            ga.getAttributes().setSize(new Dimension(200, 128));
            layer.addAnnotation(ga);
            ga = new GlobeAnnotation("Background patterns...", Position.fromDegrees(-10, 120, 0), patternAttr);
            ga.getAttributes().setImageSource(PatternFactory.createPattern(PatternFactory.PATTERN_HVLINE,
                .15f, Color.GREEN));  // green + lines
            ga.getAttributes().setImageScale(.4);
            ga.getAttributes().setSize(new Dimension(200, 128));
            layer.addAnnotation(ga);
            // Shows pattern scale effect on circles pattern
            for (int i = 1; i <= 10; i++)
            {
                ga = new GlobeAnnotation("Pattern scale:" + (float) i / 10,
                    Position.fromDegrees(-20, 97 + i * 3, 0), patternAttr);
                ga.getAttributes().setImageSource(PatternFactory.createPattern(PatternFactory.PATTERN_CIRCLES,
                    (float) i / 10, Color.LIGHT_GRAY));
                ga.getAttributes().setImageScale(.4);
                ga.getAttributes().setSize(new Dimension(160, 60));
                layer.addAnnotation(ga);
            }

            // Using a GlobeAnnotation subclass to override drawing
            class SimpleGlobeAnnotation extends GlobeAnnotation
            {
                Font font = Font.decode("Arial-PLAIN-12");

                public SimpleGlobeAnnotation(String text, Position position)
                {
                    super(text, position);
                }

                protected void applyScreenTransform(DrawContext dc, int x, int y, int width, int height, double scale)
                {
                    GL2 gl = dc.getGL().getGL2(); // GL initialization checks for GL2 compatibility.
                    gl.glTranslated(x, y, 0);
                    gl.glScaled(scale, scale, 1);
                }

                protected void doDraw(DrawContext dc, int width, int height, double opacity, Position pickPosition)
                {
                    if (dc.isPickingMode())
                        return;

                    TextRenderer textRenderer = this.getTextRenderer(dc, this.font);

                    // Draw text centered just above the screen point - use annotation's colors
                    String text = getText().split("\n")[0]; // First line only
                    int textWidth = (int) textRenderer.getBounds(text).getWidth();
                    Color textColor = this.modulateColorOpacity(this.getAttributes().getTextColor(), opacity);
                    Color backColor = this.modulateColorOpacity(this.getAttributes().getBackgroundColor(), opacity);
                    textRenderer.begin3DRendering();
                    textRenderer.setColor(backColor);
                    textRenderer.draw(text, -textWidth / 2 + 1, 12 - 1);   // Background 'shadow'
                    textRenderer.setColor(textColor);
                    textRenderer.draw(text, -textWidth / 2, 12);           // Foreground text
                    textRenderer.end3DRendering();

                    // Draw little square around screen point - use annotation's color
                    Color borderColor = this.getAttributes().getBorderColor();
                    this.applyColor(dc, borderColor, opacity, false);
                    // Draw 3x3 shape from its bottom left corner
                    GL2 gl = dc.getGL().getGL2(); // GL initialization checks for GL2 compatibility.
                    gl.glDisable(GL.GL_LINE_SMOOTH);
                    gl.glDisable(GL2.GL_LINE_STIPPLE);
                    gl.glLineWidth(1);
                    gl.glTranslated(-1, -1, 0);
                    FrameFactory.drawShape(dc, AVKey.SHAPE_RECTANGLE, 3, 3, GL.GL_LINE_STRIP, 0);
                }
            }

            ga = new SimpleGlobeAnnotation("Mount Rainier\nAlt: 4392m", Position.fromDegrees(46.8534, -121.7609, 0));
            layer.addAnnotation(ga);
            ga = new SimpleGlobeAnnotation("Mount Adams\nAlt: 3742m", Position.fromDegrees(46.2018, -121.4931, 0));
            layer.addAnnotation(ga);
            ga = new SimpleGlobeAnnotation("Mount Saint Helens\nAlt: 4392m",
                Position.fromDegrees(46.1991, -122.1882, 0));
            layer.addAnnotation(ga);

            // Using an anonymous subclass to change annotation text on the fly
            ga = new GlobeAnnotation("DRAG ME!", Position.fromDegrees(42, -118, 0), Font.decode("Arial-BOLD-18"))
            {
                public void doDraw(DrawContext dc, int width, int height, double opacity, Position pickPosition)
                {
                    // if annotation has moved, set its text
                    if (getPosition().getLatitude().degrees != 42 || getPosition().getLongitude().degrees != -118)
                        setText(String.format("Lat %7.4f\u00B0\nLon %7.4f\u00B0", getPosition().getLatitude().degrees,
                            getPosition().getLongitude().degrees));

                    // Keep rendering
                    super.doDraw(dc, width, height, opacity, pickPosition);
                }
            };
            layer.addAnnotation(ga);

            // Using post drawing code in an anonymous subclass
            ga = new GlobeAnnotation("Annotation with extra frames drawn by a render delegate.",
                Position.fromDegrees(40, -116, 0), Font.decode("Serif-BOLD-18"), Color.DARK_GRAY)
            {
                public void doDraw(DrawContext dc, int width, int height, double opacity, Position pickPosition)
                {
                    // Let normal rendering happen
                    super.doDraw(dc, width, height, opacity, pickPosition);

                    java.awt.Rectangle insetBounds = this.computeInsetBounds(width, height);
                    java.awt.Rectangle freeBounds = this.computeFreeBounds(dc, width, height);

                    // Draw second light gray frame outside draw rectangle
                    // Refers to scaleFactor, alphaFactor, drawRectangle and freeRectangle which have been
                    // set during drawing.
                    this.applyColor(dc, Color.BLACK, 0.5 * opacity, true);
                    // Translate to draw area bottom left corner, 3 pixels outside
                    GL2 gl = dc.getGL().getGL2(); // GL initialization checks for GL2 compatibility.
                    gl.glTranslated(insetBounds.x - 3, insetBounds.y - 3, 0);
                    FrameFactory.drawShape(dc, AVKey.SHAPE_RECTANGLE, insetBounds.width + 6,
                        insetBounds.height + 6, GL.GL_LINE_STRIP, 4);

                    // Draw another frame in the free space if any
                    if (freeBounds.height > 0)
                    {
                        gl.glTranslated(+3, +3, 0);
                        FrameFactory.drawShape(dc, AVKey.SHAPE_ELLIPSE, freeBounds.width,
                            freeBounds.height, GL.GL_TRIANGLE_FAN, 0);
                    }
                }
            };
            ga.getAttributes().setTextAlign(AVKey.CENTER);
            ga.getAttributes().setBackgroundColor(new Color(1f, 1f, 1f, .7f));
            ga.getAttributes().setBorderColor(Color.BLACK);
            ga.getAttributes().setSize(new Dimension(160, 200));
            layer.addAnnotation(ga);

            // Using a ScreenAnnotation
            ScreenAnnotation sa = new ScreenAnnotation("Fixed position annotation", new Point(20, 50));
            sa.getAttributes().setDefaults(defaultAttributes);
            sa.getAttributes().setCornerRadius(0);
            sa.getAttributes().setSize(new Dimension(200, 0));
            sa.getAttributes().setAdjustWidthToText(AVKey.SIZE_FIXED); // use strict dimension width - 200
            sa.getAttributes().setDrawOffset(new Point(100, 0)); // screen point is annotation bottom left corner
            sa.getAttributes().setHighlightScale(1);             // No highlighting either
            layer.addAnnotation(sa);

            makeRelativeAnnotations(layer);

            // Add layer to the layer list and update the layer panel
            insertBeforeCompass(this.getWwd(), layer);
        }

        public void makeRelativeAnnotations(AnnotationLayer layer)
        {
            AnnotationAttributes defaultAttributes = new AnnotationAttributes();
            defaultAttributes.setBackgroundColor(new Color(0f, 0f, 0f, 0f));
            defaultAttributes.setTextColor(Color.YELLOW);
            defaultAttributes.setLeaderGapWidth(14);
            defaultAttributes.setCornerRadius(0);
            defaultAttributes.setSize(new Dimension(300, 0));
            defaultAttributes.setAdjustWidthToText(AVKey.SIZE_FIT_TEXT); // use strict dimension width - 200
            defaultAttributes.setFont(Font.decode("Arial-BOLD-24"));
            defaultAttributes.setBorderWidth(0);
            defaultAttributes.setHighlightScale(1);             // No highlighting either
            defaultAttributes.setCornerRadius(0);

            ScreenRelativeAnnotation lowerLeft = new ScreenRelativeAnnotation("Lower Left", .1, 0.01);
            lowerLeft.setKeepFullyVisible(true);
            lowerLeft.setXMargin(5);
            lowerLeft.setYMargin(5);
            lowerLeft.getAttributes().setDefaults(defaultAttributes);

            ScreenRelativeAnnotation upperLeft = new ScreenRelativeAnnotation("Upper Left", 0.1, 0.99);
            upperLeft.setKeepFullyVisible(true);
            upperLeft.setXMargin(5);
            upperLeft.setYMargin(5);
            upperLeft.getAttributes().setDefaults(defaultAttributes);

            ScreenRelativeAnnotation upperRight = new ScreenRelativeAnnotation("Upper Right", 0.99, 0.99);
            upperRight.setKeepFullyVisible(true);
            upperRight.setXMargin(5);
            upperRight.setYMargin(5);
            upperRight.getAttributes().setDefaults(defaultAttributes);

            ScreenRelativeAnnotation lowerRight = new ScreenRelativeAnnotation("Lower Right", 0.99, 0.01);
            lowerRight.setKeepFullyVisible(true);
            lowerRight.setXMargin(5);
            lowerRight.setYMargin(5);
            lowerRight.getAttributes().setDefaults(defaultAttributes);

            ScreenRelativeAnnotation center = new ScreenRelativeAnnotation("Center", 0.5, 0.5);
            center.setKeepFullyVisible(true);
            center.setXMargin(5);
            center.setYMargin(5);
            center.getAttributes().setDefaults(defaultAttributes);

            layer.addAnnotation(lowerLeft);
            layer.addAnnotation(upperLeft);
            layer.addAnnotation(upperRight);
            layer.addAnnotation(lowerRight);
            layer.addAnnotation(center);
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

    public static void main(String[] args)
    {
        ApplicationTemplate.start("Apollo", AppFrame.class);
    }
}
