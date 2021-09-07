package com.zlk.jdk.aggregate.list;

import java.util.LinkedList;
import java.util.List;

/**
 * @author ZhouLiKuan
 * @description: TODO
 * @date 2021/8/19/019 8:24
 */
public class ListTest {
    public static void testLinkedList(){
        List<Integer> linkedList = new LinkedList<>();
        //测试null,可存放多个null
        linkedList.add(null);
        linkedList.add(null);
        System.out.println("list长度："+linkedList.size());
    }

    public static void main(String[] args) {
        testLinkedList();
    }

}
