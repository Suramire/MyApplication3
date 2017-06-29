package com.zxf.json;

import java.util.List;

import com.google.gson.Gson;

public class ModifyWriteJson {
    /*
     * 通过引入gson jar包 写入 json 数据
     */
    public String getJsonData(List<?> list)
    {

        Gson gson=new Gson();
        String jsonstring=gson.toJson(list);
        return jsonstring;
    }
}
