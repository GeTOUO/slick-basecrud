package com.util;

import com.alibaba.fastjson.JSONObject;

public class JsonUtil {
    /**
     * JSON 转 POJO
     */
     public static <T> T getObject(String pojo, Class<T> tclass) {
            try {
                return JSONObject.parseObject(pojo, tclass);
            } catch (Exception e) {
                System.err.println(tclass + "转 JSON 失败");
            }
            return null;
     }
     
     /**
      * POJO 转 JSON    
      */
     public static <T> String getJson(T tResponse){
         String pojo = JSONObject.toJSONString(tResponse);
         return pojo;
     }
     
}