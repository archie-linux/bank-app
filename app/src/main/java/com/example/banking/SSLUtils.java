package com.example.banking;

import android.content.Context;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Collections;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class SSLUtils {

    public static SSLContext createSSLContext(Context context) {
        try {
            // Load the certificate from resources
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            InputStream certInputStream = context.getResources().openRawResource(R.raw.cert);
            X509Certificate cert = (X509Certificate) cf.generateCertificate(certInputStream);

            // Create a KeyStore containing the certificate
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            keyStore.setCertificateEntry("myCertificate", cert);

            // Create a TrustManager that trusts the certificate
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            // Create an SSLContext that uses the TrustManager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

            return sslContext;
        } catch (Exception e) {
            throw new RuntimeException("Error loading certificate", e);
        }
    }
}
