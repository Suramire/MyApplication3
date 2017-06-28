package com.zjw.json;

import com.google.gson.Gson;

import java.util.List;

public class WriteJson {
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
