package com.wangningbo.scale.writer;

import com.wangningbo.scale.ScaleCodecWriter;
import com.wangningbo.scale.ScaleWriter;

import java.io.IOException;

public class BoolWriter implements ScaleWriter<Boolean> {
    @Override
    public void write(ScaleCodecWriter wrt, Boolean value) throws IOException {
        if (value) {
            wrt.directWrite(1);
        } else {
            wrt.directWrite(0);
        }
    }
}
