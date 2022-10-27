package com.zlk.jdk.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;

/**
 * 阿拉伯语处理
 */
public class ArabUtil {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String content = "@SS@العرب";
        // \u200E固定，该值代表从左到右标记字符.兼容阿拉伯语加入数字与英文乱序
        String biasLeft = "\u200E";
        String message = new String(content.getBytes(), "UTF-8");
        String content2 =  message.replaceAll("@SS@", biasLeft+quoteReplacement("1234455"));
        System.out.printf(content2);
    }

    /**
     * 特殊字符处理（避免匹配正则报错）
     * @return
     */
    public static String quoteReplacement(String replacement) {
        if (StringUtils.isEmpty(replacement)) {
            replacement = "";
        }
        // 处理特殊字符
        return  java.util.regex.Matcher.quoteReplacement(replacement);
    }
}
