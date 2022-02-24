package com.wangningbo.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ParseMetadataUtils {
    public static byte[] parse(String json_path,String pallet_name,String function_name) throws IOException {
        Path path = Paths.get(json_path);
        String json = Files.readString(path);
        JSONObject metadata = JSON.parseArray(json).getJSONObject(1).getJSONObject("V14");
        JSONArray pallets = metadata.getJSONArray("pallets");
        JSONArray types = metadata.getJSONObject("types").getJSONArray("types");
        JSONObject palletObject = new JSONObject();
        for (Object pallet : pallets) {
            JSONObject tmp = (JSONObject) pallet;
            String name = tmp.getString("name");
            if (pallet_name.equals(name)) {
                palletObject = tmp;
            }
        }
        Integer calls_id = palletObject.getJSONObject("calls").getInteger("ty");
        Integer pallet_id = palletObject.getInteger("index");
        JSONObject functionObject = new JSONObject();
        for (Object type : types) {
            JSONObject tmp = (JSONObject) type;
            if (calls_id.equals(tmp.getInteger("id"))) {
                functionObject = tmp;
            }
        }
        JSONArray variants = functionObject.getJSONObject("type").getJSONObject("def").getJSONObject("variant").getJSONArray("variants");
        Integer function_id = null;
        for (Object variant : variants) {
            JSONObject tmp = (JSONObject) variant;
            if (function_name.equals(tmp.getString("name"))) {
                function_id = tmp.getInteger("index");
            }
        }
        byte[] bytes = new byte[2];
        bytes[0] = pallet_id.byteValue();
        bytes[1] = function_id.byteValue();
        return bytes;
    }
}
