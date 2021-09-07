package com.zlk.po;


import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author likuan.zhou
 * @title: SiteDto
 * @projectName speedaf-cheetah-pds
 * @description: 树结构
 * @date 2021/8/30/030 8:58
 */
@Data
public class Node implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 父id
     */
    private String parentId;

    /**
     * 是否启用（0禁用，1启用）
     */
    private Integer status;

    /**
     * 排序
     */
    private String sort;

    /**
     * 备注
     */
    private String remarks;


    /**
     * 子节点
     */
    private  List<Node> children = new ArrayList<>();

    private static final long serialVersionUID = 1L;
}
