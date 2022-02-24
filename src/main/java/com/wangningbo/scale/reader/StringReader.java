package com.wangningbo.scale.reader;

import com.wangningbo.scale.ScaleCodecReader;
import com.wangningbo.scale.ScaleReader;

/**
 * Read string, encoded as UTF-8 bytes
 */
public class StringReader implements ScaleReader<String> {
    @Override
    public String read(ScaleCodecReader rdr) {
        return rdr.readString();
    }
}
