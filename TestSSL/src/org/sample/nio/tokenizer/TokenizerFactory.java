package org.sample.nio.tokenizer;

public interface TokenizerFactory<T> {
   MessageTokenizer<T> create();
}
