package com.zlk.algorithm.data.sort.bubblesort;



import com.zlk.algorithm.data.sort.insertsort.InsertSort;
import com.zlk.algorithm.data.sort.selectsort.SelectSort;
import com.zlk.algorithm.data.sort.util.RandomArray;

import java.util.Random;

/**
 * 冒泡排序
 * @author  zhoulk
 * date: 2019/5/20 0020
 */
public class BubbleSort {
    /**
     * 相邻两个元素比较，如果第一个大，将他们交换位置。再将大的一个和相邻比较。直到他最大。（不和每趟的最后一个结果比，因为他已经有序）
     */

    public static void main(String[] args) {
        Integer[] array = RandomArray.getArray(1, 100000, 1000000);

        Integer[] arr1 = new Integer[80000];
        Integer[] arr2 = new Integer[80000];
        Integer[] arr3 = new Integer[80000];
        Random rand = new Random();
        for(int i = 0;i<80000;i++){
            arr1[i] = rand.nextInt(10001);
            arr2[i] = rand.nextInt(10001);
            arr3[i] = rand.nextInt(10001);
        }

        SelectSort<Integer[]> selectSort = new SelectSort();

        //选择排序
        Long startTime = System.currentTimeMillis();
        Integer[] returnArr = selectSort.sortAll(arr1);
        Long endTime = System.currentTimeMillis();
        System.out.println("花费时间："+(float)(endTime-startTime)/1000+"秒");

        //插入排序
        //InsertSort.sort(arr2);
        InsertSort.sortOptimize(array);
        Long startTime1 = System.currentTimeMillis();
        Integer[] returnArr1 = selectSort.sortAll(arr2);
        Long endTime1 = System.currentTimeMillis();
        System.out.println("花费时间："+(float)(endTime1-startTime1)/1000+"秒");

        //冒泡排序
        Long startTime2 = System.currentTimeMillis();
        BubbleSort.sort(arr3);
        Long endTime2 = System.currentTimeMillis();
        System.out.println("花费时间："+(float)(endTime2-startTime2)/1000+"秒");
        for (Integer v:array) {
            System.out.print(v+",");
        }
    }

   public static void sort(Integer[] arr){
       //优化方案，记录最后一次位置，再交换
       //趟数
       for (int i = 0; i <arr.length-1 ; i++) {
           //标记某趟是否已经有序，有序为true
           boolean flag = true;
           for (int j = 0; j <arr.length-i-1 ; j++) {
               if(arr[j]>arr[j+1]){
                    int temp = arr[j+1];
                    arr[j+1] = arr[j];
                    arr[j] = temp;
                    flag = false;
               }
           }

           if (flag){
               //已经有序，结束循环
               break;
           }
       }
    }
}
