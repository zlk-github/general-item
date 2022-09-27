package com.zlk.domain.dto;

import lombok.Data;

/**
 * 字段比较对象
 *
 * @author likuan.zhou
 * @date 2022/7/5/005 15:22
 */
@Data
public class ComparisonDto {

    //@ApiModelProperty(value="字段名称")
    private String fieldName;

    //@ApiModelProperty(value="字段原始值")
    private Object beforeVal;

    //@ApiModelProperty(value="字段更新值")
    private Object afterVal;

    //@ApiModelProperty(value="是否更新(是true,否false)")
    private Boolean blUpdate;
}
