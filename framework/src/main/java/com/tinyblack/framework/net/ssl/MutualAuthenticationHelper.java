package com.tinyblack.framework.net.ssl;


import android.util.Base64;

import com.tinyblack.framework.controller.NetworkController;
import com.tinyblack.framework.log.TLog;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.security.KeyFactory;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;
import java.util.Collection;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

/**
 * 双向认证的 ssl
 *
 * @author yubiao
 */
public class MutualAuthenticationHelper {

    private final static String CERTIFICATE_STANDARD = "X.509";
    private final static String SERVER = "server";
    private final static String CLIENT = "client";
    private final static String PROTOCOL_TYPE = "TLS";

    private static KeyStore getServerKeyStore() {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance(CERTIFICATE_STANDARD);
            // 从string中获取
            BufferedInputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(NetworkController.getCaCrt().getBytes()));
            Collection<Certificate> certificateCollection = (Collection<Certificate>) certificateFactory.generateCertificates(inputStream);
            Certificate[] certificates = new Certificate[certificateCollection.size()];

            KeyStore clientKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            clientKeyStore.load(null);

            int i = 0;
            for (Certificate certificate : certificateCollection) {
                certificates[i++] = certificate;
                clientKeyStore.setCertificateEntry(SERVER + i, certificate);
            }
            inputStream.close();
            return clientKeyStore;
        } catch (CertificateException | IOException | KeyStoreException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            TLog.e(e.getMessage(), e);
        }
        return null;
    }

    private static KeyStore getClientKeyStore() {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance(CERTIFICATE_STANDARD);
            // 从string中获取
            BufferedInputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(NetworkController.getClientCrt().getBytes()));
            Collection<Certificate> certificateCollection = (Collection<Certificate>) certificateFactory.generateCertificates(inputStream);
            Certificate[] certificates = new Certificate[certificateCollection.size()];

            KeyStore serverKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            serverKeyStore.load(null);

            int i = 0;
            for (Certificate certificate : certificateCollection) {
                certificates[i++] = certificate;
                serverKeyStore.setCertificateEntry(CLIENT + i, certificate);
            }
            inputStream.close();

            PrivateKey privateKey = getPrivateKey(NetworkController.getClientKey());

            serverKeyStore.setKeyEntry(CLIENT, privateKey, null, certificates);

            return serverKeyStore;
        } catch (Exception e) {
            e.printStackTrace();
            TLog.e(e.getMessage(), e);
        }
        return null;
    }


    public static PrivateKey getPrivateKey(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        StringBuilder pkcs8Lines = new StringBuilder();
        BufferedReader rdr = new BufferedReader(new StringReader(privateKey));
        String line;
        while ((line = rdr.readLine()) != null) {
            pkcs8Lines.append(line);
        }
        // Remove the "BEGIN" and "END" lines, as well as any whitespace
        String pkcs8Pem = pkcs8Lines.toString();
        pkcs8Pem = pkcs8Pem.replace("-----BEGIN RSA PRIVATE KEY-----", "");
        pkcs8Pem = pkcs8Pem.replace("-----END RSA PRIVATE KEY-----", "");
        pkcs8Pem = pkcs8Pem.replaceAll("\\s+", "");
        // Base64 decode the result
        byte[] pkcs8EncodedBytes = Base64.decode(pkcs8Pem, Base64.DEFAULT);
        // extract the private key
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pkcs8EncodedBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(keySpec);
    }


    public static void addSSLCertification(OkHttpClient.Builder builder) {
        try {
            KeyStore clientKeyStore = getClientKeyStore();
            KeyStore serverKeyStore = getServerKeyStore();

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(clientKeyStore, null);

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(serverKeyStore);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
            }
            X509TrustManager trustManager = (X509TrustManager) trustManagers[0];
            //SSLContext
            SSLContext sslContext = SSLContext.getInstance(PROTOCOL_TYPE);
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagers, new SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            builder.sslSocketFactory(sslSocketFactory, trustManager);

        } catch (UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            TLog.e(e.getMessage(), e);
        }
    }
}
