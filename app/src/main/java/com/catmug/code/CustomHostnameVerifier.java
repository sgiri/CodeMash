package com.catmug.code;
import java.io.IOException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

public class CustomHostnameVerifier implements org.apache.http.conn.ssl.X509HostnameVerifier {


    public boolean verify(String host, SSLSession session) {
        HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
        return hv.verify(host, session);
    }


    public void verify(String host, SSLSocket ssl) throws IOException {
    }


    public void verify(String host, X509Certificate cert) throws SSLException {

    }


    public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {

    }
}