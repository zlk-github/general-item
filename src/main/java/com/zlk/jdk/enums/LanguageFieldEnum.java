package com.zlk.jdk.enums;

/**
 * @author likuan.zhou
 * @title: LanguageFieldEnum
 * @projectName speedaf-cheetah-pds
 * @description: 前端语种对应字段
 * @date 2021/9/2/002 19:22
 */
public enum LanguageFieldEnum {
    /**英文*/
    EN("en", "enName"),
    /**中文*/
    ZH("zh", "zhName"),
    /**法文*/
    FR("fr", "frName"),
    /**阿拉伯*/
    AR("ar", "arName");

    private final String code;
    private final String info;

    LanguageFieldEnum(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public String getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

    public static String getInfo(String code) {
        for (LanguageFieldEnum ele : values()) {
            if(ele.getCode().equals(code)) {
                return ele.getInfo();
            }

        }
        return LanguageFieldEnum.EN.getInfo();
    }

}
