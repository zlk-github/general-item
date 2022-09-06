package com.zlk.jdk.enums;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * @author likuan.zhou
 * @title: LocalFieldUtil
 * @projectName speedaf-cheetah-pds
 * @description: 语言字段处理
 * @date 2021/9/2/002 19:32
 */
@Slf4j
public class LocalFieldUtil {
    /**
     * 获取语言对应字段返回值
     *
     * @param obj 包含语言实体
     * @return 本地化值
     */
    public static String getFieldVal(Object obj,String language) {
        // 系统语言
        if (Objects.nonNull(obj)) {
            Class<?> zClass = obj.getClass();
            try {
                //获取本地化对应字段
                Field field = zClass.getDeclaredField(LanguageFieldEnum.getInfo(language));
                field.setAccessible(true);
                //获取本地化对应字段值
                String name = (String) field.get(obj);
                if (StringUtils.isNotEmpty(name)) {
                    return name;
                } else {
                    // 无值默认英文
                    Field field2 = zClass.getDeclaredField(LanguageFieldEnum.getInfo(LanguageFieldEnum.EN.getCode()));
                    field2.setAccessible(true);
                    //获取本地化对应字段值
                    return (String) field2.get(obj);
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     * 获取语言对应字段返回值
     *
     * @param obj 包含语言实体
     * @return 本地化值
     */
    public static String getFieldVal(Object obj,LanguageClassEnum languageClassEnum) {
        // 系统语言
        String language  = "zh";
        if (Objects.nonNull(obj)) {
            Class<?> zClass = obj.getClass();
            try {
                //获取本地化对应字段
                Field field = zClass.getDeclaredField(languageClassEnum.getInfo(language));
                field.setAccessible(true);
                //获取本地化对应字段值
                String name = (String) field.get(obj);
                if (StringUtils.isNotEmpty(name)) {
                    return name;
                } else {
                    // 无值默认英文
                    Field field2 = zClass.getDeclaredField(LanguageFieldEnum.getInfo(LanguageFieldEnum.EN.getCode()));
                    field2.setAccessible(true);
                    //获取本地化对应字段值
                    return (String) field2.get(obj);
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return "";
    }



}
