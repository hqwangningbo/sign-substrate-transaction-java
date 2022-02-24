package com.wangningbo.scale.writer;

import com.wangningbo.scale.ScaleCodecWriter;
import com.wangningbo.scale.ScaleWriter;

import java.io.IOException;

public class UInt16Writer implements ScaleWriter<Integer> {
    @Override
    public void write(ScaleCodecWriter wrt, Integer value) throws IOException {
        wrt.directWrite(value & 0xff);
        wrt.directWrite((value >> 8) & 0xff);
    }
}
