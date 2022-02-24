package com.wangningbo;
import com.sun.jna.Native;
import com.wangningbo.pojo.Constants;
import com.wangningbo.pojo.DecodeResult;
import com.wangningbo.utils.*;
import com.wangningbo.scale.ScaleCodecWriter;
import com.wangningbo.scale.writer.UInt16Writer;
import org.apache.commons.codec.DecoderException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestMain {
    public static void main(String[] args) throws IOException, DecoderException {
        String json_path = "src/main/resources/metadata.json";
        String pallet_name = "StudyStorage";
        String function_name = "add_person";
//        String pallet_name = "Balances";
//        String function_name = "transfer";
        byte[] call_prex = ParseMetadataUtils.parse(json_path, pallet_name, function_name);
        System.out.println(call_prex[0]+"-------"+call_prex[1]);
//        JSONObject study_person = (JSONObject) pallets.get(8);
//        System.out.println(study_person.toJSONString());

        byte[] name_encode = TypeEncodeUtils.StringEncode("name");
        byte[] age_encdoe = new byte[]{18};
        byte[] address_encode = TypeEncodeUtils.StringEncode("han");
        byte[] call = new byte[call_prex.length + name_encode.length + address_encode.length + age_encdoe.length];
        System.arraycopy(call_prex, 0, call, 0, call_prex.length);
        System.arraycopy(name_encode, 0, call, call_prex.length, name_encode.length);
        System.arraycopy(age_encdoe, 0, call, call_prex.length + name_encode.length, age_encdoe.length);
        System.arraycopy(address_encode, 0, call, call_prex.length + name_encode.length + age_encdoe.length, address_encode.length);
        for (byte b : call) {
            System.out.print((b&0xFF)+",");
        }
        System.out.println();
        byte[] extra = new byte[]{0, 4, 0};

        byte[] spec_version = new byte[]{100, 0, 0, 0};
        byte[] tx_version = new byte[]{1, 0, 0, 0};
        String genesis_hash_str = "37b22890424483ffceb4c06c728e3f3f9798ecf1f0be0dfdc81239fce417ca2d";
        byte[] genesis_hash = HexUtils.hexToBytes(genesis_hash_str);

        byte[] addition_signed = new byte[4 + 4 + 32 + 32];
        System.arraycopy(spec_version, 0, addition_signed, 0, 4);
        System.arraycopy(tx_version, 0, addition_signed, 4, 4);
        System.arraycopy(genesis_hash, 0, addition_signed, 4 + 4, 32);
        System.arraycopy(genesis_hash, 0, addition_signed, 4 + 4 + 32, 32);
        for (byte b : addition_signed) {
            System.out.print((b&0xFF)+",");
        }
        System.out.println();

        byte[] payload = new byte[call.length + extra.length + addition_signed.length];
        System.arraycopy(call, 0, payload, 0, call.length);
        System.arraycopy(extra, 0, payload, call.length, extra.length);
        System.arraycopy(addition_signed, 0, payload, call.length + extra.length, addition_signed.length);

        for (byte b : payload) {
            System.out.print((b&0xFF)+",");
        }
        System.out.println();

        DecodeResult decodeResult = new DecodeResult();
        //5GrwvaEF5zXb26Fz9rcQpDWS57CtERHpNehXCPcNoHGKutQY
        //5GrwvaEF5zXb26Fz9rcQpDWS57CtERHpNehXCPcNoHGKutQY
        //ca895a70fe944746b36942bff04339465c4e8a00b4cc3b3bf3c61b89aee5930efe6737f0931aee3ddec8ba114c73e081cbda7829c4a88a778546982780ec8a31
        //98319d4ff8a9508c4bb0cf0b5a78d760a0b2082c02775e6e82370816fedfff48925a225d97aa00682d6a59b95b18780c10d7032336e88f3442b42361f4a66011
        decodeResult.setPrivateKey(Utils.hexToU8a("98319d4ff8a9508c4bb0cf0b5a78d760a0b2082c02775e6e82370816fedfff48925a225d97aa00682d6a59b95b18780c10d7032336e88f3442b42361f4a66011"));
        System.out.println(decodeResult.getPrivateKey().length);
        decodeResult.setPublicKey(HexUtils.hexToBytes("d43593c715fdd31c61141abd04a99fd6822c8558854ccde39a5684e7a56da27d"));
        String address = CryptoUtils.encodeAddress(decodeResult.getPublicKey(), (byte) 42);

//        String mnemonic = "dawn wheat relax magnet exhaust weapon course infant enrich matter ability north";
//        Sign lib = Native.load("crust", Sign.class);
//        String my_prikey = lib.get_my_privkey(mnemonic);
//        String my_pubkey = lib.get_my_pubkey(my_prikey);
//        System.out.println(my_prikey);
//        System.out.println(my_pubkey);

//        byte[] sign = new byte[116];
//        lib.sign_test(
//                sign,
//                my_prikey,
//                call,
//                extra,
//                addition_signed
//        );
        byte[] sign = new byte[64];
        SR25519 sr25519 = new SR25519();
        sr25519.sr25519_ext_sr_sign(
                sign,
                decodeResult.getPublicKey(),
                decodeResult.getPrivateKey(),
                payload,
                payload.length
        );


        //101+call(12) = 113 再加上（4u8 | 0b1000_0000）
        ByteArrayOutputStream final_prex = new ByteArrayOutputStream();
        ScaleCodecWriter wrt = new ScaleCodecWriter(final_prex);
        Integer i = ((101 + call.length) << 2) | 0b01;
        System.out.println(i);
        wrt.write(new UInt16Writer(), i);


        //			0..=0b0011_1111 => dest.push_byte((*self.0 as u8) << 2),
        //			0..=0b0011_1111_1111_1111 => (((*self.0 as u16) << 2) | 0b01).encode_to(dest),
        //			0..=0b0011_1111_1111_1111_1111_1111_1111_1111 => ((*self.0 << 2) | 0b10).encode_to(dest),
        ByteArrayOutputStream final_ex = new ByteArrayOutputStream();
        final_ex.write(new byte[]{(byte) 201, 1});
        final_ex.write(132);
        final_ex.write(0);
        final_ex.write(decodeResult.getPublicKey());
        final_ex.write(1);
        final_ex.write(sign);
        final_ex.write(extra);
        final_ex.write(call);

        System.out.println(HexUtils.bytesToHex(final_ex.toByteArray()));
    }
}
