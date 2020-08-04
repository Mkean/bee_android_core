package com.bee.core.network.config.ssl;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * @Description:
 */
public class TrustAllCerts implements X509TrustManager {

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }

    public static TrustAllCerts trustAllCerts = new TrustAllCerts();

    public static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory sslSocketFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");

            sc.init(null, new TrustManager[]{trustAllCerts}, new SecureRandom());

            sslSocketFactory = sc.getSocketFactory();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return sslSocketFactory;

    }
}
