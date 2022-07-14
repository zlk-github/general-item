package com.zlk.jdk.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * TODO
 *
 * @author likuan.zhou
 * @date 2022/7/10/010 20:06
 */
@Slf4j
public class JsonUtil {

    /**
     2 * 判断字符串是否可以转化为json对象
     3 * @param content
     4 * @return
     5 */
    public static boolean isJsonObject(Object content) {
        // 此处应该注意，不要使用StringUtils.isEmpty(),因为当content为"  "空格字符串时，JSONObject.parseObject可以解析成功，
        // 实际上，这是没有什么意义的。所以content应该是非空白字符串且不为空，判断是否是JSON数组也是相同的情况。
        if(Objects.isNull(content) || StringUtils.isBlank(content.toString())) {
            return false;
        }
        try {
            String jsonString = JSON.toJSON(content).toString();
            JSONObject jsonStr = JSONObject.parseObject(jsonString);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断字符串是否可以转化为JSON数组
     * @param content
     * @return
     */
    public static boolean isJsonArray(Object content) {
        if(Objects.isNull(content) || StringUtils.isBlank(content.toString())) {
            return false;
        }
        try {
            String jsonString = JSON.toJSON(content).toString();
            JSONArray jsonStr = JSONArray.parseArray(jsonString);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 对象转json字符串
     * @return
     */
    public static String toJsonString(Object obj) {
        try {
            if(Objects.nonNull(obj)) {
                return JSON.toJSONString(obj);
                // 转单个对象JSONObject
                //String trackParamJson = JSONObject.toJSONString(trackParam, SerializerFeature.WriteMapNullValue);
            }
        } catch (Exception e) {
            log.error("转换失败，对象：{}",obj,e);
        }
        return null;
    }

    /**
     * 转java对象
     * @return
     */
    public static <T> T toJavaBean(String jSONObjectString, Class<T> clazz) {
        try {
            if(StringUtils.isNotBlank(jSONObjectString)) {
                JSONObject msgParamJson= JSONObject.parseObject(jSONObjectString);
                return JSONObject.toJavaObject(msgParamJson, clazz);
            }
        } catch (Exception e) {
            log.error("转换失败",e);
        }
        return null;
    }
}
