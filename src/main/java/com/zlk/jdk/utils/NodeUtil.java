package com.zlk.jdk.utils;

import com.zlk.po.Node;

import java.util.HashMap;
import java.util.List;

/**
 * @author likuan.zhou
 * @title: NodeUtile
 * @projectName practice
 * @description: TODO
 * @date 2021/9/3/003 8:36
 */
public class NodeUtil {

    /**
     * 转map
     *
     * @param siteVoList 树list
     * @return 树map<id,node>
     */
    public HashMap<Long, Node> siteMap(List<Node> siteVoList) {
        HashMap<Long, Node> map = new HashMap<>(16);
        for (Node node : siteVoList) {
            map.put(node.getId(), node);
        }
        return map;
    }

}
