/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.nasa.cms.Layers;

import gov.nasa.worldwind.layers.BasicTiledImageLayer;
import gov.nasa.worldwind.util.WWXML;
import org.w3c.dom.Document;

/**
 *
 * @author kjdickin
 */
public class Apollo15 extends BasicTiledImageLayer
{
    public Apollo15()
    {
        super(getConfigurationDocument(), null);
    }

    protected static Document getConfigurationDocument()
    {
        return WWXML.openDocumentFile("gov/nasa/cms/config/Moon/Apollo/Apollo15.xml", null);
    }
}