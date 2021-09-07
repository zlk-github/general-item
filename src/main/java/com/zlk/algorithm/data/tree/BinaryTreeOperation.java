package com.zlk.algorithm.data.tree;

/**
 * @description: 二叉树操作
 * @author ZhouLiKuan
 * @date 2021/8/17/017 11:21
 */
public class BinaryTreeOperation {

    static class Node{
        Integer val;
        Node lc;
        Node rc;

        Node() {
        }
        Node(Integer data) {
            this.val = data;
            this.lc = null;
            this.rc = null;
        }
    }

    public static void main(String[] args) {
        Integer[] arr = {8,5,5,5,7,1,2,6,3,17,17,21};
        Node node = new Node();
       // node.val = arr[0];
        for (Integer i : arr) {
            insert(node,i);

        }
        System.out.println(node);

        //中序遍历
        middleOrder(node);
    }

    /**
     * 创建树
     * @param node 树
     * @param data 插入值
     * @return
     */
    static Node insert(Node node, Integer data){
        if (null == node) {
          return new Node();
        }
        if (data.compareTo(node.val) > 0) {
            if (node.rc == null) {
                node.rc = new Node(data);
            } else {
                insert(node.rc,data);
            }
        } else if (data.compareTo(node.val) < 0) {
            if (node.lc == null) {
                node.lc = new Node(data);
            } else {
                insert(node.lc,data);
            }
        }else {
            //相同值
        }
        return node;
    }


    /**
     * 中序递归(左根右)
     * @param node
     */
    public static void middleOrder(Node node) {
        if (node != null) {
            middleOrder(node.lc);
            System.out.print(node.val + ",");
            middleOrder(node.rc);
        }
    }
}
