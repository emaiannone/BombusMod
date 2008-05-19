/*
 * testForm.java
 *
 * Created on 19.05.2008, 22:22
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

import java.util.Vector;
import javax.microedition.lcdui.Display;
import ui.MainBar;
import ui.VirtualElement;
import ui.VirtualList;

/**
 *
 * @author ad
 */
public class testForm 
        extends VirtualList {
    
    private Vector itemsList=new Vector();
    
    /** Creates a new instance of testForm */
    public testForm(Display display) {
        super();
        //this.display=display;
        setMainBarItem(new MainBar("test form"));
        
        simpleString testSimpleString1=new simpleString("testSimpleString1", "test string 1");
        itemsList.addElement(testSimpleString1);        
        checkBox testCheckBox1=new checkBox("testCheckBox1", "test1", false);
        itemsList.addElement(testCheckBox1);
        
        spacerItem testSpacerItem1=new spacerItem("testSpacerItem1");
        itemsList.addElement(testSpacerItem1);     
        
        simpleString testSimpleString2=new simpleString("testSimpleString2", "test string 2");
        itemsList.addElement(testSimpleString2);   
        checkBox testCheckBox2=new checkBox("testCheckBox2", "test2", true);
        itemsList.addElement(testCheckBox2);
        
        spacerItem testSpacerItem2=new spacerItem("testSpacerItem2");
        itemsList.addElement(testSpacerItem2);  

        simpleString testSimpleString3=new simpleString("testSimpleString3", "test string 3");
        itemsList.addElement(testSimpleString3);
        textInput testTextInput1=new textInput(display, "testTextInput1", "test input text 1");
        itemsList.addElement(testTextInput1);
        
        spacerItem testSpacerItem3=new spacerItem("testSpacerItem3");
        itemsList.addElement(testSpacerItem3);  
        
        simpleString testSimpleString4=new simpleString("testSimpleString4", "test string 4");
        itemsList.addElement(testSimpleString4);
        attachDisplay(display);
    }

    protected int getItemCount() {
        return itemsList.size();
    }

    protected VirtualElement getItemRef(int index) {
        return (VirtualElement)itemsList.elementAt(index);
    }
}