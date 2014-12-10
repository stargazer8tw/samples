package org.sample.nio.reactor;

import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;

public interface ChannelContainer {
	SelectableChannel getChannel();
	void notifyChannelRegister(SelectionKey key);
	String getName();
}
