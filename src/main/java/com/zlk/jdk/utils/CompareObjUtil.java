package com.zlk.jdk.utils;

import com.zlk.domain.dto.ComparisonDto;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @author likuan.zhou
 * @date 2022/7/5/005 15:20
 */
@Slf4j
public class CompareObjUtil {

    /**
     * 比较有变化的字段
     * @param beforeObj
     * @param afterObj
     * @return
     * @throws Exception
     */
    public static List<ComparisonDto> compareObj(Object beforeObj, Object afterObj) {
        List<ComparisonDto> diffs = new ArrayList<>();
        try {
            if (beforeObj == null) {
                log.error("对象字段转换比较异常。原对象不能为空");
                return diffs;
            }
            if (afterObj == null) {
                log.error("对象字段转换比较异常。新对象不能为空");
                return diffs;
            }
            if (!beforeObj.getClass().isAssignableFrom(afterObj.getClass())) {
                log.error("对象字段转换比较异常，两个对象不相同，无法比较。beforeObj：{}，afterObj：{}",beforeObj,afterObj);
                return diffs;
            }
            //取出属性
            Field[] beforeFields = beforeObj.getClass().getDeclaredFields();
            Field[] afterFields = afterObj.getClass().getDeclaredFields();
            Field.setAccessible(beforeFields, true);
            Field.setAccessible(afterFields, true);
            //遍历取出差异值
            //遍历取出差异值
            if (beforeFields != null && beforeFields.length > 0) {
                for (int i = 0; i < beforeFields.length; i++) {
                    Object beforeValue = beforeFields[i].get(beforeObj);
                    Object afterValue = afterFields[i].get(afterObj);
                    // AfterVale值为null，不会更新数据库。AfterVale值与beforeValue值相等不需要更新。
                    boolean blDbUpdate;
                    if (beforeValue instanceof Number) {
                        blDbUpdate= afterValue != null &&
                                // beforeValue == null 避免new BigDecimal(beforeValue.toString())空指针
                                (beforeValue == null || new BigDecimal(afterValue.toString()).compareTo(new BigDecimal(beforeValue.toString())) != 0);
                    } else {
                        blDbUpdate = afterValue != null
                                && !afterValue.equals(beforeValue);
                    }
                    if (blDbUpdate) {
                        ComparisonDto comparison = new ComparisonDto();
                        comparison.setFieldName(beforeFields[i].getName());
                        comparison.setBeforeVal(beforeValue);
                        comparison.setAfterVal(afterValue);
                        comparison.setBlUpdate(true);
                        diffs.add(comparison);
                    }
                }
            }
        } catch (Exception ex) {
            //log.error("对象字段转换比较异常。beforeObj：{}，afterObj：{}",beforeObj,afterObj,ex);
        }
        return diffs;
    }
}
