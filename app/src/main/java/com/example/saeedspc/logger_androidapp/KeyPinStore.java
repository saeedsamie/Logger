package com.example.saeedspc.logger_androidapp;
//Skip to content
//
//        Search or jump to…
//
//        Pull requests
//        Issues
//        Marketplace
//        Explore
//@saeedsamie Sign out
//        1
//        4 5 echebukati/sec-android-post
//        Code  Issues 0  Pull requests 0  Projects 0  Wiki  Insights
//        sec-android-post/Android/app/src/main/java/ebc/secandroidpost/communication/KeyPinStore.java
//        effd948  on Jul 26, 2016
//@echebukati echebukati Initial Commit
//
//        68 lines (58 sloc)  2.65 KB
/**
 * Created by Ricardo Iramar dos Santos on 14/08/2015.
 * github.com/rirmar/
 * Edited by echeb on 26-Jul-16.
 */

        import java.io.BufferedInputStream;
        import java.io.FileInputStream;
        import java.io.IOException;
        import java.io.InputStream;
        import java.security.KeyManagementException;
        import java.security.KeyStore;
        import java.security.KeyStoreException;
        import java.security.NoSuchAlgorithmException;
        import java.security.cert.Certificate;
        import java.security.cert.CertificateException;
        import java.security.cert.CertificateFactory;
        import java.security.cert.X509Certificate;

        import javax.net.ssl.SSLContext;
        import javax.net.ssl.TrustManagerFactory;


public class KeyPinStore {

    private static KeyPinStore instance = null;
    private SSLContext sslContext = SSLContext.getInstance("TLS");

    public static synchronized KeyPinStore getInstance() throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        if (instance == null) {
            instance = new KeyPinStore();
        }
        return instance;
    }

    private KeyPinStore() throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        // https://developer.android.com/training/articles/security-ssl.html
        // Load CAs from an InputStream
        // (could be from a resource or ByteArrayInputStream or ...)
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        // bankingapp.xyz.crt should be in the Assets directory (tip from here http://littlesvr.ca/grumble/2014/07/21/android-programming-connect-to-an-https-server-with-self-signed-certificate/)
        InputStream caInput = new BufferedInputStream(new FileInputStream("bankingapp.xyz.crt"));
        Certificate ca;
        try {
            ca = cf.generateCertificate(caInput);
            System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
        } finally {
            caInput.close();
        }
        // Create a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        // Create a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        sslContext.init(null, tmf.getTrustManagers(), null);
    }

    public SSLContext getContext() {
        return sslContext;
    }
}