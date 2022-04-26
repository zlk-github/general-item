package com.zlk.jdk.enums;

/**
 * @author likuan.zhou
 * @title: LanguageFieldEnum
 * @projectName speedaf-cheetah-pds
 * @description: 前端语种对应字段
 * @date 2021/9/2/002 19:22
 */
public enum LanguageClassEnum {
    /**枚举字段见枚举LanguageFieldEnum*/
    DEFAULT("LanguageFieldEnum", "LanguageFieldEnum"),
    ;

    private final String code;
    private String info;

    LanguageClassEnum(String code, String info) {
        this.code = code;


    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

    public static String getInfo(String code) {
        for (LanguageClassEnum ele : values()) {
            if(ele.getCode().equals(code)) {
                return ele.getInfo();
            }
        }
        return DEFAULT.getInfo();
    }
}

