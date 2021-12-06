package com.zlk.algorithm.structure.list.array;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 数据结构线性表之数组，数据必须要连续
 * java 对应ArrayList
 *
 * @author likuan.zhou
 * @date 2021/11/6/006 18:00
 */
@Slf4j
public class Array<T> {
   final private static int END = 5;
    /**插入第一个元素位置(或者数组长度不够)或者删除第一个元素，最坏情况会导致0(N)
      -- 插入第一个位置少见，非排序情况（当前不演示）
      下标获取为O(1)
      遍历所有0(N)  */
    // 数组不适合做删除插入添加等操作，适合查询、遍历
    /** 数组*/
    private T[] array;
    /** 默认初始化长度*/
    private static final int DEFAULT_CAPACITY = 10;
    /** 数组的有效长度(实际有值)*/
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
        // 新增
        array.add(1);
        array.add(null);
        array.add(null);
        array.add(12);
        array.add(35);
        array.add(78);
        array.add(12);
        array.add(35);
        array.add(78);
        array.add(78);
        // 插入
        //array.insert(45,1);
        // 索引获取值
        //Integer val = array.get(5);
        //System.out.println(val);
        // 删除对应索引值
        array.delete(2);
        Integer[] dd = new Integer[6];
        System.out.println(dd.length);

        // java数据结构数组
        ArrayList<Object> arrayList = new ArrayList<>(1);
        arrayList.add(11);
    }

    /**
     * 新增
     * @param val 元素
     */
    private void add(T val) {
        // 正常新增O(1),当需要扩容需要复制为O(N)
        if (elements>=array.length) {
           // 2倍扩容，此时会导致O（N）
            Arrays.copyOf(array,array.length*2);
        }
        array[elements++] = val;
    }

    /**
     * 下标获取元素
     * @param index 下标
     */
    @SneakyThrows
    private T get(int index) {
       if (index<0 || index>elements) {
            log.error("数组下标越界");
            throw new ArrayIndexOutOfBoundsException();
        }
       return array[index];
    }

    /**
     * 插入数据到某个位置(如果插入的位置不合法，则插在数组的最后一个元素之后)
     * @param val 元素
     * @param index 索引位置(0开始)
     */
    private void insert(T val,int index) {
        // 当需要扩容需要复制或者插入第一个位置为最坏情况O(N)

        //如果插入的位置不合法，则插在数组的最后一个元素之后(避免出现数组不相邻且浪费大量空间)
        if (index<0 || index>elements) {
            index = elements;
        }

        System.out.println("插入前的数组:"+Arrays.toString(array)+"。数组有效长度："+elements);
        if (elements>=array.length) {
            // 2倍扩容，此时会导致O（N）
            System.out.println("扩容前长度:"+array.length+"。扩容前有效长度:"+elements);
            array = Arrays.copyOf(array,array.length*2);
            System.out.println("扩容后长度:"+array.length+"。扩容前有效长度:"+elements);
        }

        // index下标[index,elements)下标开始需要全部往后移位，把index留给val
        for (int i = elements ; i > index; i--) {
            array[i] = array[i-1];
        }
        array[index] = val;
        // 实际长度+1
        elements++;
        System.out.println("插入后的数组:"+Arrays.toString(array)+"。数组有效长度："+elements);
    }

    /**
     * 插入数据到某个位置(如果插入的位置不合法，则插在数组的最后一个元素之后)
     * @param index 索引位置(0开始)
     */
    private void delete(int index) {
        System.out.println("删除前数组有效长度："+elements+"，删除前的数组:"+Arrays.toString(array));
        // 当需要删除第一个位置为最坏情况O(N)
        if (index<0 || index>elements) {
            log.error("数组下标越界");
            throw new ArrayIndexOutOfBoundsException();
        }

        // index下标（index,elements)下标开始需要全部往前移位
        for (int i = index+1 ; i < elements; i++) {
            System.out.println("移动数据:"+array[i]);
            array[i-1] = array[i];
        }
        // 实际长度-1,最后一位置空
        array[--elements] = null;
        System.out.println("删除后数组有效长度："+elements+"，删除后的数组:"+Arrays.toString(array));
    }

    /**
     * 插入数据到某个位置(如果插入的位置不合法，则插在数组的最后一个元素之后)
     * @param val
     */
    private void delete(T val) {
        System.out.println("删除前数组有效长度："+elements+"，删除前的数组:"+Arrays.toString(array));
        // 最简单的方式使用另外一个数组配合（不赘述）

        // 匹配到索引index（index,elements)下标开始需要全部往前移位
        // 每匹配到一次实际长度-1,最后一位置空
        array[--elements] = null;
        System.out.println("删除后数组有效长度："+elements+"，删除后的数组:"+Arrays.toString(array));
    }

    /**
     * 元素个数
     */
    private int size() {
       return elements;
    }

    //https://blog.csdn.net/booy123/article/details/105917424
}
