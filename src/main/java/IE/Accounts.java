/*
 * Accounts.java
 *
 * Created on 14.06.2008., 17:12
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
//#ifdef IMPORT_EXPORT
package IE;

import Account.AccountStorage;
import Client.StaticData;
import io.NvStorage;
import io.file.FileIO;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import ui.Time;
import io.file.InternalResource;
import xmpp.Account;
import xmpp.Jid;

/**
 *
 * @author ad
 */
public class Accounts {

    private final static String userName = "userName";
    private final static String server = "server";
    private final static String password = "password";  // only in import
    private final static String hostAddr = "hostAddr";
    private final static String port = "port";
    private final static String nick = "nick";
    private final static String resource = "resource";
    private final static String useSSL = "useSSL";
    private final static String plainAuth = "plainAuth";
    private final static String mucOnly = "mucOnly";
//#if HTTPPOLL || HTTPCONNECT || HTTPBIND
//#     private final static String enableProxy = "enableProxy";
//#     private final static String proxyHostAddr = "proxyHostAddr";
//#     private final static String proxyPort = "proxyPort";
//#endif
    private final static String compression = "compression";
    private final static String keepAliveType = "keepAliveType";
    private final static String keepAlivePeriod = "keepAlivePeriod";
//#ifdef HTTPCONNECT
//#     private final static String proxyUser = "proxyUser";
//#     private final static String proxyPass = "proxyPass";
//#endif
    Vector accountList;
    private String file;

    /**
     * Creates a new instance of Accounts
     */
    public Accounts(String path, int direction, boolean fromResource) {
        accountList = null;
        accountList = new Vector();
        this.file = path;

        if (direction == 0) {
            importData(fromResource);
        } else {
            exportData();
        }
        accountList = null;
    }

    public final void importData(boolean fromResource) {
        String accounts = "";

        if (!fromResource) {
            FileIO fileIO = FileIO.createConnection(file);
            accounts = fileIO.fileReadUtf();
        } else {
            byte[] bodyMessage = new byte[4096];
            try {
                InputStream in = InternalResource.getResourceAsStream("/def_accounts.txt");
                if (in != null) {
                    in.read(bodyMessage);
                }
            } catch (IOException ex) {
                if (StaticData.Debug) {
                    ex.printStackTrace();
                }
            }
            if (bodyMessage != null) {
                accounts = new String(bodyMessage, 0, bodyMessage.length);
            }
        }

        if (accounts != null) {
            try {
                int pos = 0;
                int start_pos = 0;
                int end_pos = 0;

                boolean parse = true;

                while (parse) {
                    start_pos = accounts.indexOf("<a>", pos);
                    end_pos = accounts.indexOf("</a>", pos);

                    if (start_pos > -1 && end_pos > -1) {
                        pos = end_pos + 4;
                        String tempstr = accounts.substring(start_pos + 3, end_pos);

                        Account account = new Account();
                        account.JID = new Jid(findBlock(tempstr, userName),
                                findBlock(tempstr, server),
                                findBlock(tempstr, resource));
                        account.password = findBlock(tempstr, password);
                        account.hostAddr = findBlock(tempstr, hostAddr);
                        account.port = Integer.parseInt(findBlock(tempstr, port));
                        account.nick = findBlock(tempstr, nick);
                        account.plainAuth = (findBlock(tempstr, plainAuth).equals("1")) ? true : false;
                        account.mucOnly = (findBlock(tempstr, mucOnly).equals("1")) ? true : false;
//#if HTTPPOLL || HTTPCONNECT || HTTPBIND
//#                         account.setEnableProxy(findBlock(tempstr, enableProxy).equals("1") ? true : false);
//#                         account.proxyHostAddr = findBlock(tempstr, proxyHostAddr);
//#                         account.setProxyPort(Integer.parseInt(findBlock(tempstr, proxyPort)));
//#endif
                        account.setUseCompression((findBlock(tempstr, compression).equals("1")) ? true : false);
                        account.keepAlivePeriod = Integer.parseInt(findBlock(tempstr, keepAlivePeriod));
//#ifdef HTTPCONNECT
//#                         account.setProxyUser(findBlock(tempstr, proxyUser));
//#                         account.setProxyPass(findBlock(tempstr, proxyPass));
//#endif
                        accountList.addElement(account);
                    } else {
                        parse = false;
                    }
                }
                rmsUpdate();
            } catch (Exception e) {
            }
        }
    }

    public void rmsUpdate() {
        DataOutputStream outputStream = NvStorage.CreateDataOutputStream();
        for (int i = 0; i < getItemCount(); i++) {
            AccountStorage.saveToDataOutputStream(getAccount(i), outputStream);
        }
        NvStorage.writeFileRecord(outputStream, "accnt_db", 0, true); //Account.storage
    }

    private String findBlock(String source, String needle) {
        String startItem = "<" + needle + ">";
        int start = source.indexOf(startItem);
        int end = source.indexOf("</" + needle + ">");

        if (start > -1 && end > -1 && start != end) {
            return source.substring(start + startItem.length(), end);
        }

        return "";
    }

    private String createBlock(String needle, String value) {
        StringBuffer block = new StringBuffer("<").append(needle).append('>');
        if (value != null) {
            block.append(value);
        }
        block.append("</").append(needle).append('>');

        return block.toString();
    }

    public final void exportData() {
        StringBuffer body = new StringBuffer();

        getAccounts();

        for (int i = 0; i < getItemCount(); i++) {
            Account a = getAccount(i);
            StringBuffer account = new StringBuffer("<a>");
            account.append(createBlock(userName, a.JID.getNode()))
                    .append(createBlock(server, a.JID.getServer()))
                    .append(createBlock(hostAddr, a.hostAddr))
                    .append(createBlock(port, Integer.toString(a.port)))
                    .append(createBlock(nick, a.nick))
                    .append(createBlock(resource, a.JID.resource))
                    .append(createBlock(useSSL, "0"))
                    .append(createBlock(plainAuth, (a.plainAuth ? "1" : "0")))
                    .append(createBlock(mucOnly, (a.mucOnly ? "1" : "0")))
//#if HTTPPOLL || HTTPCONNECT || HTTPBIND
//#                     .append(createBlock(enableProxy, a.isEnableProxy() ? "1" : "0")).append(createBlock(proxyHostAddr, a.proxyHostAddr)).append(createBlock(proxyPort, Integer.toString(a.getProxyPort())))
//#endif
                    .append(createBlock(compression, (a.useCompression() ? "1" : "0"))).append(createBlock(keepAliveType, "")).append(createBlock(keepAlivePeriod, Integer.toString(a.keepAlivePeriod)))
//#ifdef HTTPCONNECT
//#                     .append(createBlock(proxyUser, a.getProxyUser())).append(createBlock(proxyPass, a.getProxyPass()))
//#endif
                    .append("</a>\r\n");
            body.append(account);
        }

        FileIO fileIO = FileIO.createConnection(file + "accounts_" + Time.localDate() + ".txt");
        fileIO.fileWriteUtf(body.toString());
    }

    private void getAccounts() {
        Account a;
        int index = 0;
        do {
            a = AccountStorage.createFromStorage(index);
            if (a != null) {
                accountList.addElement(a);
                index++;
            }
        } while (a != null);
    }

    public int getItemCount() {
        return accountList.size();
    }

    public Account getAccount(int index) {
        return (Account) accountList.elementAt(index);
    }
}

//#endif
