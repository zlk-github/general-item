package com.zlk.algorithm.structure.list.array;

import java.util.Arrays;

/**
 * 数据结构之数组（线性表）
 *
 * @author likuan.zhou
 * @date 2021/11/6/006 18:00
 */
public class Array<T> {
   final private static int END = 5;
    /**插入第一个元素位置(或者数组长度不够)或者删除第一个元素，最坏情况会导致0(N)
      -- 插入第一个位置少见，非排序情况（当前不演示）
      下标获取为O(1)
      遍历所有0(N)  */
    // 数组不适合做删除插入等操作，适合查询、添加和遍历
    /** 数组*/
    private T[] array;
    /** 默认初始化长度*/
    private static final int DEFAULT_CAPACITY = 10;
    /** 数组的有效长度*/
    private int elements;

    public Array() {
        array = (T[]) new Object[DEFAULT_CAPACITY];
    }

    public Array(int size) {
        if (size > 0) {
            array = (T[]) new Object[size];
        } else {
            array = (T[]) new Object[DEFAULT_CAPACITY];
        }
    }

    /**private int arr1[] = new int[]{1,3,5,7,9}; */
    /**private int[] arr2 = {2, 4, 6, 8, 10}; */

    public static void main(String[] args) {
        // 初始一个长度
        Array<Integer> array = new Array<>();
        array.add(12);
        System.out.println(array.elements);
    }

    /**
     * 插入数据到某个位置
     * @param val 元素
     */
    private  void add(T val) {
        if (elements>=array.length) {
           // 2倍扩容，此时会导致O（N）
            Arrays.copyOf(array,array.length*2);
        }
        array[elements++] = val;
    }

    /**
     * 元素个数
     */
    private int size() {
       return elements;
    }


    //https://blog.csdn.net/booy123/article/details/105917424
}
