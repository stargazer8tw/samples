/**
 * Copyright (C) NetPro Information Service Ltd., 2014.
 * All rights reserved.
 * 
 * This software is covered by the license agreement between
 * the end user and NetPro Information Service Ltd., and may be 
 * used and copied only in accordance with the terms of the 
 * said agreement.
 * 
 * NetPro Information Service Ltd. assumes no responsibility or 
 * liability for any errors or inaccuracies in this software, 
 * or any consequential, incidental or indirect damage arising
 * out of the use of the software.
 */
package org.sample.nio.ssl;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManagerFactory;

/**
 * @author michael lin
 * 
 */
public class SSLServer {
	
	private String keystoreFilename = "keystore.jks";
	private String truststoreFilename = "truststore.jks";
	private char[] ksPassphrase = "trinitytest".toCharArray();
	private String keystoreAlias = "sslserver";
	private String certFilename = "sslserver.cert";
	private String clientFilename = "sslclient.cert";

	private char[] certPassphrase = "trinitycert".toCharArray();
	
	private String dc = "netpro.com.tw";
	
	private SSLContext sslContext;
	private SSLEngine serverEngine;
	

	public void init() throws Exception {
		initKeyStore();
		initTrustStore();
		initServerCert();
		initSslContext();
		initEngine();
	}
	private void initKeyStore() throws Exception {
		File ks = new File(keystoreFilename);
		if (!ks.exists()) {
			SSLUtils.genKeyStore(ksPassphrase, keystoreFilename);
		}
	}
	private void initTrustStore() throws Exception {
		File ks = new File(truststoreFilename);
		if (!ks.exists()) {
			SSLUtils.genKeyStore(ksPassphrase, truststoreFilename);
		}
	}
	private void initServerCert() throws Exception {
		File cert = new File(certFilename);
		if (!cert.exists()) {
			SSLUtils.generateSelfSignedCertificateJcaCertBuilder(certFilename, dc, keystoreAlias, certPassphrase, keystoreFilename, ksPassphrase);
		}
	}
	private void initSslContext() throws Exception {
		// First initialize the key and trust material.
		KeyStore ksKeys = KeyStore.getInstance("JKS");
		ksKeys.load(new FileInputStream(keystoreFilename), ksPassphrase);
		KeyStore ksTrust = KeyStore.getInstance("JKS");
		ksTrust.load(new FileInputStream(keystoreFilename), ksPassphrase);

		// KeyManager's decide which key material to use.
		KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
		kmf.init(ksKeys, ksPassphrase);

		// TrustManager's decide whether to allow connections.
		TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
		tmf.init(ksTrust);

		sslContext = SSLContext.getInstance("TLS");

		// Slowness of the First JSSE Access
		SecureRandom sr = new SecureRandom();
		sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), sr);

		// sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
		SSLParameters params = sslContext.getDefaultSSLParameters();
		
		// Do not send an SSL-2.0-compatible Client Hello.
		ArrayList<String> protocols = new ArrayList<String>(Arrays.asList(params.getProtocols()));
		protocols.remove("SSLv2Hello");
		params.setProtocols(protocols.toArray(new String[protocols.size()]));
		// Adjust the supported ciphers.
		ArrayList<String> ciphers = new ArrayList<String>(Arrays.asList(params
				.getCipherSuites()));
		ciphers.retainAll(Arrays.asList(SSLOptions.DEFAULT_SSL_CIPHER_SUITES));
		params.setCipherSuites(ciphers.toArray(new String[ciphers.size()]));
		for (String prot : params.getProtocols()) {
			System.out.println(prot);
		}
	}
	private void initEngine() {
		serverEngine = sslContext.createSSLEngine();
		serverEngine.setUseClientMode(false);
		serverEngine.setNeedClientAuth(true);
	}
	
	public static void main(String[] args) {
		SSLServer server = new SSLServer();
		try {
			server.init();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	void sample() {
		SSLContext ctx;
		try {
			ctx = SSLContext.getInstance("TLSv1.2", "SunJSSE");
//			ctx = SSLContext.getDefault();
		} catch (NoSuchAlgorithmException e) {
			try {
				ctx = SSLContext.getInstance("TLSv1", "SunJSSE");
			} catch (NoSuchAlgorithmException e1) {
				// The TLS 1.0 provider should always be available.
				throw new AssertionError(e1);
			} catch (NoSuchProviderException e1) {
				throw new AssertionError(e1);
			}
		} catch (NoSuchProviderException e) {
			// The SunJSSE provider should always be available.
			throw new AssertionError(e);
		}
		try {
			ctx.init(null, null, null);
			System.out.println(ctx.getProtocol());
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Prepare TLS parameters. These have to applied to every TLS
		// socket before the handshake is triggered.
		SSLParameters params = ctx.getDefaultSSLParameters();
		// Do not send an SSL-2.0-compatible Client Hello.
		ArrayList<String> protocols = new ArrayList<String>(
				Arrays.asList(params.getProtocols()));
		protocols.remove("SSLv2Hello");
		params.setProtocols(protocols.toArray(new String[protocols.size()]));
		// Adjust the supported ciphers.
		ArrayList<String> ciphers = new ArrayList<String>(Arrays.asList(params
				.getCipherSuites()));
		ciphers.retainAll(Arrays
				.asList("TLS_RSA_WITH_AES_128_CBC_SHA256",
						"TLS_RSA_WITH_AES_256_CBC_SHA256",
						"TLS_RSA_WITH_AES_256_CBC_SHA",
						"TLS_RSA_WITH_AES_128_CBC_SHA",
						"SSL_RSA_WITH_3DES_EDE_CBC_SHA",
						"SSL_RSA_WITH_RC4_128_SHA1",
						"SSL_RSA_WITH_RC4_128_MD5",
						"TLS_EMPTY_RENEGOTIATION_INFO_SCSV"));
		params.setCipherSuites(ciphers.toArray(new String[ciphers.size()]));
		for (String prot : params.getProtocols()) {
			System.out.println(prot);
		}
	}
}
