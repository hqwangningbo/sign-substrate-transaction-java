package com.wangningbo.scale.reader;

import com.wangningbo.scale.ScaleCodecReader;
import com.wangningbo.scale.ScaleReader;

public class UInt16Reader implements ScaleReader<Integer> {

    @Override
    public Integer read(ScaleCodecReader rdr) {
        int result = 0;
        result += rdr.readUByte();
        result += rdr.readUByte() << 8;
        return result;
    }

}
