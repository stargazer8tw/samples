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

import org.junit.Assert;
import org.junit.Test;
import org.sample.nio.ssl.SSLUtils;

/**
 * @author michael lin
 *
 */
public class SSLTest {
	@Test
	public void testKeyStore() {
		try {
			SSLUtils.genKeyStore("test".toCharArray(), "test-ks.jks");
		} catch (Exception e) {
			Assert.fail();
		}
	}
	@Test
	public void testBCCertJcaBuilder() {
		try {
			SSLUtils.genKeyStore("test".toCharArray(), "test-jca.jks");
			SSLUtils.generateSelfSignedCertificateJcaCertBuilder("test.cert", "netpro.com.tw", "local.test", "test".toCharArray(), "test-jca.jks", "test".toCharArray());
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
	@Test
	public void testBCCertBuilder() {
		try {
			SSLUtils.genKeyStore("test".toCharArray(), "test-bc.jks");
			SSLUtils.generateSelfSignedCertificateJcaCertBuilder("test.cert", "netpro.com.tw", "local.test", "test".toCharArray(), "test-bc.jks", "test".toCharArray());
		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
	}
}
