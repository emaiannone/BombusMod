/*
 * StaticData.java
 *
 * Created on 20.02.2005, 17:10
 * Copyright (c) 2005-2008, Eugene Stahov (evgs), http://bombus-im.org
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

package Client;

import org.bombusmod.App;
import org.bombusmod.android.service.XmppService;
import xmpp.Account;
import com.alsutton.jabber.JabberStream;
import util.ClipBoardIO;

import java.io.IOException;

/**
 *
 * @author Eugene Stahov
 */
public final class StaticData {
    
    private static StaticData sd;
    
    public Roster roster;
    
    public Account account;
    
//#ifdef FILE_IO
    public String previousPath="";
//#endif
    
    //public int screenWidth;
    
    public long traffic = 0;

    private long trafficOut;
    private long trafficIn;

    public void updateTrafficIn() { trafficIn=System.currentTimeMillis(); }
    public long getTrafficIn() { return trafficIn; }
    public void updateTrafficOut() { trafficOut=System.currentTimeMillis(); }
    public long getTrafficOut() { return trafficOut; }

    public static StaticData getInstance() {
        if (sd == null) {
            sd = new StaticData();
        }
        return sd;
    }
    public JabberStream getTheStream() {
        XmppService xmpp = App.getInstance().getXmppService();
        if (xmpp != null) {
            return xmpp.getTheStream();
        }
        return null;
    }
    public void startConnection() throws IOException {
        App.getInstance().getXmppService().startConnection();
    }
    public static final boolean Debug = true;
    public static final boolean XmlDebug = true;
    public static final boolean NonSaslAuth = false;
}
