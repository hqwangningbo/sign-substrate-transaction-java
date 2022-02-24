package com.wangningbo.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class TypeEncodeUtils {
    public static byte[] StringEncode(String str) throws IOException {
        byte[] bytes = str.getBytes();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        if (bytes.length*4 >= 256) {
            outputStream.write((bytes.length*4) % 256);
            outputStream.write((bytes.length*4) / 256);
        }else {
            outputStream.write(bytes.length*4);
        }
       outputStream.write(bytes);
        return outputStream.toByteArray();
    }
    //byte 与 int 的相互转换
    public static byte intToByte(int x) {
        return (byte) x;
    }

    public static int byteToInt(byte b) {
        //Java 总是把 byte 当做有符处理；我们可以通过将其和 0xFF 进行二进制与得到它的无符值
        return b & 0xFF;
    }
}
