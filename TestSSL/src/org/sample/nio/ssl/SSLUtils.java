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

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;

import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

/**
 * @author michael lin
 * 
 */
public class SSLUtils {
	static {
		Security.addProvider(new BouncyCastleProvider());
	}
	private static final String BC = org.bouncycastle.jce.provider.BouncyCastleProvider.PROVIDER_NAME;
	private static final long ONEDAY = 1000L * 60 * 60 * 24;
	private static final long ONEYEAR = 10L * 60 * 60 * 24 * 36525;

	public static void loadSelfSignedCertificate(String certFilename, String domainName,
			String keystoreAlias, char[] keystoreEntryPass,
			String keystoreFile, char[] keystorepass) throws Exception {
		// First you need to generate a key pair. We are using "RSA" public-key
		// cryptography algorithm and a key size of 1024.
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(1024);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();

		// Then instantiate an X.509 cert. generator.
		X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE)
				.addRDN(BCStyle.OU, "None")
				.addRDN(BCStyle.O, "None")
				.addRDN(BCStyle.CN, domainName);

		Date notBefore = new Date(System.currentTimeMillis() - ONEDAY);
		Date notAfter = new Date(System.currentTimeMillis() + 10 * ONEYEAR);
		BigInteger serial = BigInteger.valueOf(System.currentTimeMillis());

		X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(
				builder.build(), serial, notBefore, notAfter, builder.build(),
				keyPair.getPublic());
		ContentSigner sigGen = new JcaContentSignerBuilder(
				"SHA256WithRSAEncryption").setProvider(BC).build(
				keyPair.getPrivate());
		X509Certificate cert = new JcaX509CertificateConverter()
				.setProvider(BC).getCertificate(certGen.build(sigGen));
		cert.checkValidity(new Date());
		cert.verify(cert.getPublicKey());

		// And store it in a file.
		FileOutputStream fos = new FileOutputStream(certFilename);
		fos.write(cert.getEncoded());
		fos.close();

		// Load the key store to memory.
		KeyStore privateKS = KeyStore.getInstance("JKS");
		FileInputStream fis = new FileInputStream(keystoreFile);
		privateKS.load(fis, keystoreEntryPass);

		// Import the private key to the key store
		privateKS.setKeyEntry(keystoreAlias, keyPair.getPrivate(),
				keystoreEntryPass,
				new java.security.cert.Certificate[] { cert });

		// Write the key store back to disk
		privateKS.store(new FileOutputStream(keystoreFile), keystorepass);
	}
	
	public static void generateSelfSignedCertificateJcaCertBuilder(String certFilename, String domainName,
			String keystoreAlias, char[] keystoreEntryPass,
			String keystoreFile, char[] keystorepass) throws Exception {
		// First you need to generate a key pair. We are using "RSA" public-key
		// cryptography algorithm and a key size of 1024.
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(1024);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();

		// Then instantiate an X.509 cert. generator.
		X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE)
				.addRDN(BCStyle.OU, "None")
				.addRDN(BCStyle.O, "None")
				.addRDN(BCStyle.CN, domainName);

		Date notBefore = new Date(System.currentTimeMillis() - ONEDAY);
		Date notAfter = new Date(System.currentTimeMillis() + 10 * ONEYEAR);
		BigInteger serial = BigInteger.valueOf(System.currentTimeMillis());

		X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(
				builder.build(), serial, notBefore, notAfter, builder.build(),
				keyPair.getPublic());
		ContentSigner sigGen = new JcaContentSignerBuilder(
				"SHA256WithRSAEncryption").setProvider(BC).build(
				keyPair.getPrivate());
		X509Certificate cert = new JcaX509CertificateConverter()
				.setProvider(BC).getCertificate(certGen.build(sigGen));
		cert.checkValidity(new Date());
		cert.verify(cert.getPublicKey());

		// And store it in a file.
		FileOutputStream fos = new FileOutputStream(certFilename);
		fos.write(cert.getEncoded());
		fos.close();
		
		// Load the key store to memory.
		KeyStore privateKS = KeyStore.getInstance("JKS");
		FileInputStream fis = new FileInputStream(keystoreFile);
		privateKS.load(fis, keystoreEntryPass);

		// Import the private key to the key store
		privateKS.setKeyEntry(keystoreAlias, keyPair.getPrivate(),
				keystoreEntryPass,
				new java.security.cert.Certificate[] { cert });

		// Write the key store back to disk
		privateKS.store(new FileOutputStream(keystoreFile), keystorepass);
	}
	public static void generateSelfSignedCertificateCertBuilder(String certFilename, String domainName,
			String keystoreAlias, char[] keystoreEntryPass,
			String keystoreFile, char[] keystorepass) throws Exception {
		// First you need to generate a key pair. We are using "RSA" public-key
		// cryptography algorithm and a key size of 1024.
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(1024);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();

		// Then instantiate an X.509 cert. generator.
		X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE)
				.addRDN(BCStyle.OU, "None")
				.addRDN(BCStyle.O, "None")
				.addRDN(BCStyle.CN, domainName);
		
		Date notBefore = new Date(System.currentTimeMillis() - ONEDAY);
		Date notAfter = new Date(System.currentTimeMillis() + 10 * ONEYEAR);
		BigInteger serial = BigInteger.valueOf(System.currentTimeMillis());

		X509v3CertificateBuilder certGen = new X509v3CertificateBuilder(
				builder.build(), serial, notBefore, notAfter, builder.build(),
				SubjectPublicKeyInfo.getInstance(keyPair.getPublic().getEncoded()));
		ContentSigner sigGen = new JcaContentSignerBuilder(
				"SHA256WithRSAEncryption").setProvider(BC).build(
				keyPair.getPrivate());
		X509Certificate cert = new JcaX509CertificateConverter()
				.setProvider(BC).getCertificate(certGen.build(sigGen));
		cert.checkValidity(new Date());
		cert.verify(cert.getPublicKey());

		// And store it in a file.
		FileOutputStream fos = new FileOutputStream(certFilename);
		fos.write(cert.getEncoded());
		fos.close();

		// Load the key store to memory.
		KeyStore privateKS = KeyStore.getInstance("JKS");
		FileInputStream fis = new FileInputStream(keystoreFile);
		privateKS.load(fis, keystoreEntryPass);

		// Import the private key to the key store
		privateKS.setKeyEntry(keystoreAlias, keyPair.getPrivate(),
				keystoreEntryPass,
				new java.security.cert.Certificate[] { cert });

		// Write the key store back to disk
		privateKS.store(new FileOutputStream(keystoreFile), keystorepass);
	}
	/**
	 * @param keyStoreType
	 *            , JKS
	 * @param certFilename
	 *            , "MyCert.cer"
	 * @param certType
	 *            , "X.509"
	 * @throws Exception
	 */
	public static void genKeyStore(String keyStoreType, String certFilename,
			String certType, String alias, String keyStoreFilename,
			char[] passPharse) throws Exception {
		// CREATE A KEYSTORE OF TYPE "Java Key Store"
		KeyStore ks = KeyStore.getInstance(keyStoreType);
		/*
		 * LOAD THE STORE The first time you're doing this (i.e. the keystore
		 * does not yet exist - you're creating it), you HAVE to load the
		 * keystore from a null source with null password. Before any methods
		 * can be called on your keystore you HAVE to load it first. Loading it
		 * from a null source and null password simply creates an empty
		 * keystore. At a later time, when you want to verify the keystore or
		 * get certificates (or whatever) you can load it from the file with
		 * your password.
		 */
		ks.load(null, passPharse);
		// GET THE FILE CONTAINING YOUR CERTIFICATE
		FileInputStream fis = new FileInputStream(certFilename);
		BufferedInputStream bis = new BufferedInputStream(fis);
		// I USE x.509 BECAUSE THAT'S WHAT keytool CREATES
		CertificateFactory cf = CertificateFactory.getInstance(certType);
		// NOTE: THIS IS java.security.cert.Certificate NOT
		// java.security.Certificate
		Certificate cert = null;
		/*
		 * I ONLY HAVE ONE CERT, I JUST USED "while" BECAUSE I'M JUST DOING
		 * TESTING AND WAS TAKING WHATEVER CODE I FOUND IN THE API
		 * DOCUMENTATION. I COULD HAVE DONE AN "if", BUT I WANTED TO SHOW HOW
		 * YOU WOULD HANDLE IT IF YOU GOT A CERT FROM VERISIGN THAT CONTAINED
		 * MULTIPLE CERTS
		 */
		// GET THE CERTS CONTAINED IN THIS ROOT CERT FILE
		while (bis.available() > 0) {
			cert = cf.generateCertificate(bis);
			ks.setCertificateEntry(alias, cert);
		}
		// ADD TO THE KEYSTORE AND GIVE IT AN ALIAS NAME
		ks.setCertificateEntry(alias, cert);
		// SAVE THE KEYSTORE TO A FILE
		/*
		 * After this is saved, I believe you can just do setCertificateEntry to
		 * add entries and then not call store. I believe it will update the
		 * existing store you load it from and not just in memory.
		 */
		ks.store(new FileOutputStream(keyStoreFilename), passPharse);
	}

	public static void genKeyStore(char[] keystorePass, String keystoreFile)
			throws Exception {
		KeyStore ks = KeyStore.getInstance("JKS");

		ks.load(null, keystorePass);

		// Store away the keystore.
		FileOutputStream fos = new FileOutputStream(keystoreFile);
		ks.store(fos, keystorePass);
		fos.close();
	}
}
