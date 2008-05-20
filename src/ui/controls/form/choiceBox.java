/*
 * choiceBox.java
 *
 * Created on 20.05.2008, 9:06
 *
 * Copyright (c) 2006-2008, Daniel Apatin (ad), http://apatin.net.ru
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
 */
package ui.controls.form;

import Colors.Colors;
import Fonts.FontCache;
import images.RosterIcons;
import java.util.Vector;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import ui.IconTextElement;

/**
 *
 * @author ad
 */
public class choiceBox 
        extends IconTextElement {
    
    private int index=0;

    private String name;
    //private String caption;
    
    private Vector items=new Vector();
    
    private boolean selectable=true;
    
    //protected Font font=FontCache.getMsgFont();
    
    /** Creates a new instance of choiceBox */
    public choiceBox(String name/*, String caption*/) {
        super(RosterIcons.getInstance());
        this.name=name;
        //this.caption=caption;
    }

    protected int getImageIndex() { return -1; }

    public int getColor() { return Colors.LIST_INK; }
    
    public String toString() {
        if (items.size()<1) return "";//caption;
        return (String) items.elementAt(index);
    }

    public void onSelect(){
        if (items.size()<1) return;
        index=(index+1)%items.size();
    }
    
    public int getValue() { return index; }
    
    public String getName() { return name; }
    
    public void append(String value) { items.addElement(value); }
    
    public void setSelectedIndex(int index) { this.index=index; }
    
    public int getSelectedIndex() { return index; }
    
    public void drawItem(Graphics g, int ofs, boolean sel) {
        int captionWidth=0;//getFont().stringWidth(caption)+4;
        
        int width=g.getClipWidth()-captionWidth;
        int height=g.getClipHeight();

        int oldColor=g.getColor();

        g.setColor(0xc0c0c0);
        g.drawRect(captionWidth+1, 1, width-2, height-2);
        
        g.setColor(0xffffff);
        g.fillRect(captionWidth+2, 2, width-4, height-4);
        
        g.setColor((sel)?0xff0000:0x668866);
        g.drawRect(captionWidth+0, 0, width-1, height-1);
        
        g.setColor(oldColor);

        RosterIcons.getInstance().drawImage(
                g, 
                0x26, 
                g.getClipX()+g.getClipWidth()-RosterIcons.getInstance().getWidth(), 
                0 
        );
        //g.drawString(caption, 2, 0, Graphics.LEFT|Graphics.TOP);
        //g.translate(captionWidth, 0);
        //g.setClip(0, 2, width-RosterIcons.getInstance().getWidth(), height-2);
        super.drawItem(g, ofs, sel);
    }
    
    public boolean isSelectable() { return selectable; }
}