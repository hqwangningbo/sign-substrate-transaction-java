package com.wangningbo.scale;

import java.io.IOException;

public interface ScaleWriter<T> {
    void write(ScaleCodecWriter wrt, T value) throws IOException;
}
