package com.zlk.algorithm.structure.list.array;

/**
 * 数据结构之数组（线性表）
 *
 * @author likuan.zhou
 * @date 2021/11/6/006 18:00
 */
public class Array {
   final private static int END = 5;
    /**插入第一个元素位置或者删除第一个元素，最坏情况会导致0(N)
      下标获取为O(1)
      遍历所有0(N)  */
    private static Integer[] array = new Integer[10];
    /**private int arr1[] = new int[]{1,3,5,7,9}; */
    /**private int[] arr2 = {2, 4, 6, 8, 10}; */

    public static void main(String[] args) {

    }

    /**
     * 插入数据到某个位置
     * @param val 元素
     * @param index 数组下标
     */
    private static boolean add(Integer val,int index) {
        if (index>=array.length) {
            // 超过下标
            return false;
        }
        // 如果插入数据位置无值，可以直接插入
        if (array[index] == null) {
            array[index] = val;
        }

        Integer temp;
        //如果插入数据位置有值,插入位置的数据起，全部需要往后移动一位。
        for (int i = index; i < END; i++) {
            // 会出现越界
            temp = array[i+1];
            array[i+1] = array[i];
        }
        array[index] = val;
        return false;
    }

}
