package org.sample.nio.sample;

import java.io.IOException;

import org.sample.nio.reactor.Roma;

public class RomaEchoServer {

	public static void main(String[] args) {
		Roma roma = new Roma();
		try {
			roma.bind("localhost", 7000);
			roma.start();
			System.out.println("press any key to stop");
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			roma.shutdown();
		}
		
	}

}
