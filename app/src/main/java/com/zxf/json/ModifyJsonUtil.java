package com.zxf.json;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ModifyJsonUtil {
	public List<?> StringFromJson (String jsondata)
	{     
		Type listType = new TypeToken<List<?>>(){}.getType();
		Gson gson=new Gson();
		ArrayList<?> list=gson.fromJson(jsondata, listType);
		return list;

	}
}
