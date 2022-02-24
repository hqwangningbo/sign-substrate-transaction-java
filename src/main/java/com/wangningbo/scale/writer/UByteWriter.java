package com.wangningbo.scale.writer;

import com.wangningbo.scale.ScaleCodecWriter;
import com.wangningbo.scale.ScaleWriter;

import java.io.IOException;

public class UByteWriter implements ScaleWriter<Integer> {

    @Override
    public void write(ScaleCodecWriter wrt, Integer value) throws IOException {
        if (value < 0 || value > 0xff) {
            throw new IllegalArgumentException("Only values in range 0..255 are supported: " + value);
        }
        wrt.directWrite(value);
    }
}
