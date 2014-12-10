package org.sample.nio.sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import org.sample.nio.protocol.AsyncServerProtocol;
import org.sample.nio.protocol.EchoProtocol;
import org.sample.nio.protocol.ServerProtocolFactory;
import org.sample.nio.reactor.ConnectionHandler;
import org.sample.nio.reactor.Roma;
import org.sample.nio.tokenizer.FixedSeparatorMessageTokenizer;
import org.sample.nio.tokenizer.MessageTokenizer;
import org.sample.nio.tokenizer.StringMessage;
import org.sample.nio.tokenizer.TokenizerFactory;

public class RomaEchoClient {

	public static void main(String[] args) {
		ServerProtocolFactory<StringMessage> protocolMaker = new ServerProtocolFactory<StringMessage>() {
			public AsyncServerProtocol<StringMessage> create() {
				return new EchoProtocol();
			}
		};

		
		final Charset charset = Charset.forName("UTF-8");
		TokenizerFactory<StringMessage> tokenizerMaker = new TokenizerFactory<StringMessage>() {
			public MessageTokenizer<StringMessage> create() {
				return new FixedSeparatorMessageTokenizer("\n", charset);
			}
		};
		Roma roma = new Roma();
		try {
			roma.start();
			ConnectionHandler handler = roma.connect("localhost", 7000);
			final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("press \"q\" to quit");
			do {
				final String input = reader.readLine();
				if (input == null || "q".equals(input)) {
					break;
				}
				handler.addOutData(ByteBuffer.wrap(input.getBytes()));
			} while (true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			roma.shutdown();
		}
		
	}

}
