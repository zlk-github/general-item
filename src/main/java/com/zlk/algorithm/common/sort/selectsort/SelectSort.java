package com.zlk.algorithm.common.sort.selectsort;

import com.zlk.algorithm.common.sort.bean.Student;
import com.zlk.algorithm.common.sort.util.RandomArray;

import java.util.ArrayList;
import java.util.List;

/**desc:选择排序
 * @author  zhoulk
 * date: 2019/5/16 0016
 */
public class SelectSort<T> {

    /**
     * 选择排序
     *
     *n个记录的直接选择排序可经过n-1趟直接选择排序得到有序结果。（有序集 | 无序集）
     *描述：
     * 首先在未排序序列找到一个最小值，并将其存放在排序序列的第一个位置。
     * 接着从未排序序列中找到一个最小值将其放入排序序列的末尾。依次类推，直到序列排序完毕。
     * （**当未排序序列中首位置不是最小时，将其与最小值的位置做交互，剩余未排序元素位置不变**）
     */
    public static void main(String[] args) {
        Integer[] array = RandomArray.getArray(1, 1000, 1);

        Long startTime = System.currentTimeMillis();
        SelectSort<Integer[]> selectSort = new SelectSort();
        Integer[] returnArr = selectSort.sortAll(array);
        Long endTime = System.currentTimeMillis();
        System.out.println("花费时间："+(float)(endTime-startTime)/1000+"秒");

        System.out.println("直接使用引用对象-----------------------");
        for (Integer v:array) {
            System.out.println(v+",");
        }
        System.out.println("使用泛型的返回值-----------------------");

        for (Integer v:returnArr) {
            System.out.println(v+",");
        }
        System.out.println("对象比较-----------------------");

        Student[] students = { new Student("Zhang", 20),
                new Student("Li", 23),
                new Student("Wang", 50),
                new Student("Liu", 17),
                new Student("Aiu", 17),
                new Student("Ma", 19)
        };
         selectSort.sortAll(students);
        for (Student v:students) {
            System.out.println(v+",");
        }
    }

    /**
     * 选择排序（从小到大）
     * @param arr 需要排序数组
     * @param <T> 结果
     * @return
     */
    public <T extends Comparable<T>> T[]  sortAll(T[] arr){
        if(arr==null||arr.length==0){
           return null;
        }

        for(int i = 0;i<arr.length-1;i++){
           int minIndex = i;
           for(int j = i+1;j<arr.length;j++){
                if(arr[minIndex].compareTo(arr[j])>0){
                    minIndex = j;
                }
           }

            T temp = arr[i];
            arr[i] = arr[minIndex];
            arr[minIndex] = temp;
        }
        return arr;
    }


    /**
     * 测试泛型传值与返回(未测试通过)
     * @param t 传值
     * @param <E> 返回值
     * @return
     */
    public <E> List<E> test(Class<T> t){
        System.out.println("t = [" + t + "]");
        List list = new ArrayList<>();
        list.add("123");
        return list;
    }
}
