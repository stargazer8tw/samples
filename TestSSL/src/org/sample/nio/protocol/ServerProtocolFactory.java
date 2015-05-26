package org.sample.nio.protocol;

public interface ServerProtocolFactory<T> {
   AsyncServerProtocol<T> create();
}
