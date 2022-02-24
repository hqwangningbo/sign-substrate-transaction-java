package com.wangningbo.scale.reader;

import com.wangningbo.scale.ScaleCodecReader;
import com.wangningbo.scale.ScaleReader;

public class UnsupportedReader<T> implements ScaleReader<T> {

    private final String message;

    public UnsupportedReader() {
        this("Reading an unsupported value");
    }

    public UnsupportedReader(String message) {
        this.message = message;
    }

    @Override
    public T read(ScaleCodecReader rdr) {
        throw new IllegalStateException(message);
    }
}
