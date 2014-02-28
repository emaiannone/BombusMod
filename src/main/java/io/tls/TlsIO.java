/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package io.tls;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.bombusmod.tls.AndroidTls;

import java.net.Socket;

/**
 * @author Vitaly
 */
public abstract class TlsIO {

    public static TlsIO create(Object conn, InputStream in, OutputStream out, String authHost)
            throws Exception {
        return new AndroidTls((Socket) conn, in, out, authHost);
    }

    public abstract InputStream getTlsInputStream() throws IOException;

    public abstract OutputStream getTlsOutputStream() throws IOException;
}
