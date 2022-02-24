package com.wangningbo.scale.reader;

import com.wangningbo.scale.ScaleReader;
import com.wangningbo.scale.ScaleCodecReader;

import java.math.BigInteger;

public class UInt64Reader implements ScaleReader<BigInteger> {

  @Override
  public BigInteger read(ScaleCodecReader rdr) {
    BigInteger result = BigInteger.ZERO;
    result = result.add(BigInteger.valueOf(rdr.readUByte()));
    result = result.add(BigInteger.valueOf(rdr.readUByte()).shiftLeft(8));
    result = result.add(BigInteger.valueOf(rdr.readUByte()).shiftLeft(16));
    result = result.add(BigInteger.valueOf(rdr.readUByte()).shiftLeft(24));
    result = result.add(BigInteger.valueOf(rdr.readUByte()).shiftLeft(32));
    result = result.add(BigInteger.valueOf(rdr.readUByte()).shiftLeft(40));
    result = result.add(BigInteger.valueOf(rdr.readUByte()).shiftLeft(48));
    result = result.add(BigInteger.valueOf(rdr.readUByte()).shiftLeft(56));
    return result;
  }
}
