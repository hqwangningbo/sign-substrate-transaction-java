package com.wangningbo.scale.reader;

import com.wangningbo.scale.CompactMode;
import com.wangningbo.scale.ScaleCodecReader;
import com.wangningbo.scale.ScaleReader;

public class CompactUIntReader implements ScaleReader<Integer> {

    /**
     *
     * @param rdr reader with the encoded data
     * @return integer value
     * @throws UnsupportedOperationException if the value is encoded with more than four bytes (use {@link CompactBigIntReader})
     */
    @Override
    public Integer read(ScaleCodecReader rdr) {
        int i = rdr.readUByte();
        System.out.println(i);
        CompactMode mode = CompactMode.byValue((byte)(i & 0b11));
        if (mode == CompactMode.SINGLE) {
            return i >> 2;
        }
        if (mode == CompactMode.TWO) {
            return (i >> 2)
                    + (rdr.readUByte() << 6);
        }
        if (mode == CompactMode.FOUR) {
            return (i >> 2) +
                    (rdr.readUByte() << 6) +
                    (rdr.readUByte() << (6 + 8)) +
                    (rdr.readUByte() << (6 + 2 * 8));
        }
        throw new UnsupportedOperationException("Mode " + mode  + " is not implemented");
    }
}
