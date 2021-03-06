/*
 * AccountRegister.java
 *
 * Created on 24.04.2005, 2:36
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
package Account;

import xmpp.Account;
import Client.StaticData;
import Info.Version;
import Menu.MenuCommand;
import ServiceDiscovery.DiscoForm;
import ServiceDiscovery.DiscoForm.FormSubmitListener;
import ServiceDiscovery.FormField;
import com.alsutton.jabber.*;
import com.alsutton.jabber.datablocks.*;
import java.util.Enumeration;
import java.util.Vector;
import locale.SR;
import ui.*;
import ui.controls.form.DefForm;
import ui.controls.form.DropChoiceBox;
import ui.controls.form.TextInput;
import util.StringLoader;
import xmpp.Jid;
import xmpp.extensions.IqRegister;
import xmpp.extensions.IqRegister.RegistrationListener;
import xmpp.login.LoginListener;

/**
 *
 * @author Evg_S
 */
public class AccountRegister
	extends DefForm
	implements
	JabberListener,
	RegistrationListener,
	FormSubmitListener,
	Runnable {

    private Account raccount;
    private SplashScreen splash;
    private AccountSelect as;
    private DropChoiceBox serverChoice;
    Vector defs[];
    MenuCommand cmdSend = new MenuCommand(SR.MS_SEND, MenuCommand.OK, 1);

    /** Creates a new instance of AccountRegister
     * @param account
     */
    public AccountRegister(AccountSelect parent) {
	super(SR.MS_REGISTER, false);
	as = parent;
	splash = SplashScreen.getInstance();
	splash.setExit(as);
	splash.setProgress(SR.MS_STARTUP, 5);

	raccount = new Account();

	defs = new StringLoader().stringLoader("/def_servers.txt", 1);
	serverChoice = new DropChoiceBox("Select server for registration");
	if (defs == null) {
	    loadDefaults();
	    show();
	} else {
	    int serversCount = defs[0].size();
	    if (serversCount == 0) {
		serverChoice.items.addElement("jabber.ru");
		serverChoice.items.addElement("xmpp.ru");
		itemsList.addElement(serverChoice);
		show();
	    } else if (serversCount == 1) {
		raccount.JID = new Jid((String) defs[0].elementAt(0));
		new Thread(this).start();
	    } else {
		for (int i = 0; i < serversCount; i++) {
		    serverChoice.items.addElement((String) defs[0].elementAt(i));
		}
		itemsList.addElement(serverChoice);
		show();
	    }
	}

    }

    final void loadDefaults() {
	serverChoice.items.addElement("jabber.ru");
	serverChoice.items.addElement("xmpp.ru");
	itemsList.addElement(serverChoice);
    }

    public void run() {
        VirtualCanvas.getInstance().show(splash);
        try {
            splash.setProgress(SR.MS_CONNECT_TO_ + raccount.JID.getServer(), 30);
            //give a chance another thread to finish ui
            Thread.sleep(500);
            sd.account = raccount;
            sd.startConnection();

            sd.getTheStream().listener = this;
            sd.getTheStream().initiateStream();
        } catch (Exception e) {
            if (StaticData.Debug) {
                e.printStackTrace();
            }
            splash.setFailed();
        }
    }

    public void connectionTerminated(Exception e) {
    }

    public void beginConversation(LoginListener listener) {
	splash.setProgress(SR.MS_REGISTERING, 60);
	sd.getTheStream().addBlockListener(new IqRegister(this));
	Iq iqreg = new Iq(null, Iq.TYPE_GET, "regac" + System.currentTimeMillis());
	JabberDataBlock query = iqreg.addChild("query", null);
	query.setNameSpace(IqRegister.NS_REGS);
	sd.getTheStream().send(iqreg);
    }

    public int blockArrived(JabberDataBlock data) {
	return JabberBlockListener.BLOCK_REJECTED;
    }

    /*public void commandAction(Command c, Displayable d) {
    splash.setCommandListener(null);
    splash.removeCommand(cmdCancel);
    try {
    theStream.close();
    } catch (Exception e) {
    //e.printStackTrace();
    }
    as.rmsUpdate();
    splash.close(as);
    }*/
    public void registrationFormNotify(JabberDataBlock data) {
        new DiscoForm(null, this, null, data, sd.getTheStream(),
                "register" + System.currentTimeMillis(), "query")
                .fetchMediaElements(data.getChildBlock("query")
                .getChildBlocks());
    }

    public void registrationFailed(String errorText) {
	splash.setProgress(errorText, 100);
	VirtualCanvas.getInstance().show(splash);
	sd.getTheStream().close();
    }

    // TODO: fix this shit
    public void commandState() {
	menuCommands.removeAllElements();
	addMenuCommand(cmdSend);
    }

    public void menuAction(MenuCommand c, VirtualList v) {
	if (c == cmdSend) {
	    String serverName = (String) serverChoice.items.elementAt(serverChoice.getSelectedIndex());
	    raccount.JID = new Jid( serverName );
	    new Thread(this).start();
	} else {
	    super.menuAction(c, v);
	}
    }

    public void registrationSuccess() {
	as.itemsList.addElement(new AccountItem(raccount));
	as.rmsUpdate();
	String success = SR.MS_DONE;
	splash.setProgress(success, 100);
	VirtualCanvas.getInstance().show(splash);
	sd.getTheStream().close();
    }

    public void dispatcherException(Exception e, JabberDataBlock data) {
    }

    public void formSubmit(Vector fields) {
	for (Enumeration e = fields.elements(); e.hasMoreElements();) {
	    FormField field = (FormField) e.nextElement();
	    if (field != null && field.name != null) {
		if (field.name.equals("username")) {
		    raccount.JID = new Jid(((TextInput) field.formItem).getValue(), 
                            raccount.JID.getServer(), Version.NAME);
		}
		if (field.name.equals("password")) {
		    raccount.password = ((TextInput) field.formItem).getValue();
		}
	    }
	}
    }
    }
