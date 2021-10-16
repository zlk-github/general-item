package com.zlk.algorithm.common.sort.insertsort;

import com.zlk.algorithm.common.sort.selectsort.SelectSort;
import com.zlk.algorithm.common.sort.util.RandomArray;

/**插入排序
 * @author  zhoulk
 * date: 2019/5/20 0020
 */
public class InsertSort {

    /**
     *默认第一个位置为有序，从第二个位置开始。每次取当前位置元素和前面元素相比。(插入时，前面元素是有序的)
     * 如果当前元素比前面元素小，则交换位置。
     * 如果当前元素大于或者等于前一个元素则插入位置。
     * 依次类推直到元素序列完全有序。
     */

    public static void main(String[] args) {
        Integer[] array = RandomArray.getArray(1, 1000, 5000);
        SelectSort<Integer[]> selectSort = new SelectSort();

        //选择排序
      /*  Long startTime = System.currentTimeMillis();
        Integer[] returnArr = selectSort.sortAll(array);
        Long endTime = System.currentTimeMillis();
        System.out.println("花费时间："+(float)(endTime-startTime)/1000+"秒");*/

        //插入排序
       // sort(array);
        sortOptimize(array);
        for (Integer v:array) {
            System.out.println(v+",");
        }
        /*Long startTime1 = System.currentTimeMillis();
        Integer[] returnArr1 = selectSort.sortAll(array);
        Long endTime1 = System.currentTimeMillis();
        System.out.println("花费时间："+(float)(endTime1-startTime1)/1000+"秒");*/
    }

    /**
     * 插入排序优化（小到大） -- 找到具体位置后再直接交换，需要靠后的数据顺移
     * @param arr 排序数组
     */
    public static void sortOptimize(Integer[] arr){
        for (int i = 1; i <arr.length ; i++) {
            //记录当前值需要插入的位
            int temp = arr[i] ;
            int j;
            for(j = i;j >0&&arr[j-1]>temp;j--){
                arr[j] = arr[j-1];
            }
            arr[j] = temp;
        }
    }

    /**
     * 插入排序（小到大）--需要多次交换位置
     * @param arr 排序数组
     */
    public static void sort(Integer[] arr){
        for (int i = 1; i <arr.length ; i++) {
            int temp;
            for(int j = i;j >0&&arr[j]<arr[j-1];j--){
                temp = arr[j];
                arr[j] = arr[j-1];
                arr[j-1] = temp;
            }
        }
    }
}
