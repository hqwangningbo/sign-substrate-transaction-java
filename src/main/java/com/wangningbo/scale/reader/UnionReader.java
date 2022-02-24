package com.wangningbo.scale.reader;

import com.wangningbo.scale.ScaleCodecReader;
import com.wangningbo.scale.ScaleReader;
import com.wangningbo.scale.UnionValue;

import java.util.Arrays;
import java.util.List;

public class UnionReader<T> implements ScaleReader<UnionValue<T>> {

    private final List<ScaleReader<? extends T>> mapping;

    public UnionReader(List<ScaleReader<? extends T>> mapping) {
        this.mapping = mapping;
    }

    @SuppressWarnings("unchecked")
    public UnionReader(ScaleReader<? extends T>... mapping) {
        this(Arrays.asList(mapping));
    }

    @Override
    @SuppressWarnings("unchecked")
    public UnionValue<T> read(ScaleCodecReader rdr) {
        int index = rdr.readUByte();
        if (mapping.size() <= index) {
            throw new IllegalStateException("Unknown type index: " + index);
        }
        T value = (T) mapping.get(index).read(rdr);
        return new UnionValue<>(index, value);
    }
}
