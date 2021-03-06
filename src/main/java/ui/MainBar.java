/*
 * MainBar.java
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * You can also redistribute and/or modify this program under the
 * terms of the Psi License, specified in the accompanied COPYING
 * file, as published by the Psi Project; either dated January 1st,
 * 2005, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */

package ui;

import Client.Config;
import Colors.ColorTheme;
import Fonts.FontCache;
import images.RosterIcons;
import javax.microedition.lcdui.Graphics;

public class MainBar extends ComplexString{
   
//#ifdef GRADIENT
    public int startColor, endColor;
//#endif    
    
    public boolean lShift = false;
    public boolean rShift = false;
    
    public MainBar(int size, Object first, Object second, boolean bold) {
        this (size);
        if (first!=null) setElementAt(first,0);
        if (second!=null) setElementAt(second,1);
        
        font = FontCache.getFont(bold, FontCache.bar);
    }
    
    public MainBar(Object obj) {
        this(1, obj, null, false);
    }
    
    public MainBar(Object obj, boolean bold) {
        this(1, obj, null, bold);
    }
    public MainBar(Object obj, boolean bold, boolean centered) {
        this(1, obj, null, bold);
        this.centered = centered;
    }
    
    public int getColor() { return ColorTheme.getColor(ColorTheme.BAR_INK); }
    public int getColorBGnd() { return ColorTheme.getColor(ColorTheme.BAR_BGND); }    
    
    public MainBar(int size) {
        super (RosterIcons.getInstance());
        setSize(size);
    }
    public int getVHeight() {
       /*     if (centered && Config.getInstance().advTouch)
                return super.getVHeight() << 1;*/
        return Math.max(Config.getInstance().minItemHeight, super.getVHeight());
    }
    public void drawItem(Graphics g, int offset, boolean selected) {
        int xo = g.getClipX();
        int yo = g.getClipY();
        int wo = g.getClipWidth();
        int ho = g.getClipHeight();
        int h = getVHeight() + 1;
//#ifdef GRADIENT
        Gradient gradient;
        
        if (startColor != endColor) {
            gradient = new Gradient(0, 0, VirtualCanvas.getInstance().getWidth(), h, startColor, endColor, false);
            gradient.paint(g);
        } else {
            g.setColor(getColorBGnd());
            g.fillRect(0, 0, VirtualCanvas.getInstance().getWidth(), h);
        }
//#else
//#             g.setColor(getColorBGnd());
//#             g.fillRect(0, 0, VirtualCanvas.getInstance().getWidth(), h);
//#endif
        g.setColor(getColor());

        g.clipRect((lShift) ? 20 : 0, 0, g.getClipWidth() - ((rShift) ? 20 : 0), g.getClipHeight());
        super.drawItem(g, offset, selected);
        //g.setClip(xo, yo, wo, ho);
    }
}
